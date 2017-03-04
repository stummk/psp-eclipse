package de.uni.bremen.stummk.psp.test.control;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.uni.bremen.stummk.psp.calculation.Manager;
import de.uni.bremen.stummk.psp.control.Editor;
import de.uni.bremen.stummk.psp.control.FormPage;
import de.uni.bremen.stummk.psp.data.PSPProject;
import de.uni.bremen.stummk.psp.data.Project;
import de.uni.bremen.stummk.psp.data.ProjectPlanSummary;
import de.uni.bremen.stummk.psp.utility.Constants;
import de.uni.bremen.stummk.psp.utility.DBConnection;
import de.uni.bremen.stummk.psp.utility.PSPCSVParser;

/**
 * This class should be executed as JUNIT PLUGIN TEST. Tests the editor
 * 
 * @author Konstantin
 *
 */
public class EditorTest {
  private static final String projectName = "TESTPROJECT";
  private Project p;
  private Editor classUnderTest;

  @BeforeClass
  public static void startUp() {
    // setting up the db
    DBConnection.init();

    if (Manager.getInstance().containsProject(projectName)) {
      Manager.getInstance().deleteCompleteProject(projectName);
    }
  }

  @Before
  public void setUp() throws Exception {
    // loading project from file and open editor
    waitForJobs();

    try {

      PSPProject psp = loadTestData();
      Manager.getInstance().saveBackupProject(psp);
      assertEquals(psp.getProject().getProjectName(),
          Manager.getInstance().loadBackupProject(projectName).getProject().getProjectName());
      p = psp.getProject();

      IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
      IWorkspace ws = ResourcesPlugin.getWorkspace();
      IProject project = ws.getRoot().getProject(projectName);
      if (!project.exists()) {
        project.create(null);
      }
      if (!project.isOpen()) {
        project.open(null);
      }

      Path location = new Path(getAbsolutePath());
      IFile file = project.getFile(location.lastSegment());
      file.createLink(location, IResource.NONE, null);
      IEditorPart ed = IDE.openEditor(page, file, true);
      if (ed instanceof Editor) {
        classUnderTest = (Editor) ed;
      }

      assertNotNull(classUnderTest);
    } catch (NoClassDefFoundError e) {

    }

    waitForJobs();
    delay(3000);
  }

  /**
   * Tests the Editor class
   */
  @Test
  public void testEditor() {
    try {
      // Test ifProject is set in the Editor
      assertEquals(projectName, classUnderTest.getProject().getProjectName());

      ProjectPlanSummary pps = Manager.getInstance().getProjectSummary(projectName);
      ProjectPlanSummary sum = classUnderTest.getProjectSummaryForm().getProjectPlanSummary();

      // Tests if the Project Plan Summary is set in the Editor
      assertEquals(pps.get(Constants.KEY_DEFECTS_INJECTED_IDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_ACTUAL),
          sum.get(Constants.KEY_DEFECTS_INJECTED_IDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_ACTUAL), 0.0);
      assertEquals(pps.get(Constants.KEY_DEFECTS_REMOVED_IDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_ACTUAL),
          sum.get(Constants.KEY_DEFECTS_REMOVED_IDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_ACTUAL), 0.0);
      assertEquals(pps.get(Constants.KEY_TIME_IN_PHASE_IDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_ACTUAL),
          sum.get(Constants.KEY_TIME_IN_PHASE_IDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_ACTUAL), 0.0);
      assertEquals(pps.get(Constants.KEY_PROGRAM_SIZE_IDX, Constants.KEY_SIZE_TOTAL_LOC, Constants.KEY_ACTUAL),
          sum.get(Constants.KEY_PROGRAM_SIZE_IDX, Constants.KEY_SIZE_TOTAL_LOC, Constants.KEY_ACTUAL), 0.0);

      // Tests the FormPage of the Defect Record Page
      classUnderTest.setActivePage(Constants.ID_DEFECT_RECORD_FORM);
      assertEquals(Constants.ID_DEFECT_RECORD_FORM, ((FormPage) classUnderTest.getActivePageInstance()).getId());
      assertEquals(projectName, ((FormPage) classUnderTest.getActivePageInstance()).getProject().getProjectName());
      assertEquals(Manager.getInstance().getDefectRecord(projectName).size(),
          ((FormPage) classUnderTest.getActivePageInstance()).getTable().getTable().getItemCount());

      // Tests the FormPage of the PIP Page
      classUnderTest.setActivePage(Constants.ID_PIP_FORM);
      assertEquals(Constants.ID_PIP_FORM, ((FormPage) classUnderTest.getActivePageInstance()).getId());
      assertEquals(projectName, ((FormPage) classUnderTest.getActivePageInstance()).getProject().getProjectName());
      assertEquals(Manager.getInstance().getPip(projectName).size(),
          ((FormPage) classUnderTest.getActivePageInstance()).getTable().getTable().getItemCount());

      // Tests the FormPage of the Schedule Planning Page
      classUnderTest.setActivePage(Constants.ID_SCHEDULE_PLANNING_FORM);
      assertEquals(Constants.ID_SCHEDULE_PLANNING_FORM, ((FormPage) classUnderTest.getActivePageInstance()).getId());
      assertEquals(projectName, ((FormPage) classUnderTest.getActivePageInstance()).getProject().getProjectName());
      assertEquals(Manager.getInstance().getSchedulePlanning(projectName).size(),
          ((FormPage) classUnderTest.getActivePageInstance()).getTable().getTable().getItemCount());

      // Tests the FormPage of the Task Planning Page
      classUnderTest.setActivePage(Constants.ID_TASK_PLANNING_FORM);
      assertEquals(Constants.ID_TASK_PLANNING_FORM, ((FormPage) classUnderTest.getActivePageInstance()).getId());
      assertEquals(projectName, ((FormPage) classUnderTest.getActivePageInstance()).getProject().getProjectName());
      assertEquals(Manager.getInstance().getTaskPlanning(projectName).size(),
          ((FormPage) classUnderTest.getActivePageInstance()).getTable().getTable().getItemCount());

      // Tests the FormPage of the Test Report Page
      classUnderTest.setActivePage(Constants.ID_TEST_REPROT_FORM);
      assertEquals(Constants.ID_TEST_REPROT_FORM, ((FormPage) classUnderTest.getActivePageInstance()).getId());
      assertEquals(projectName, ((FormPage) classUnderTest.getActivePageInstance()).getProject().getProjectName());
      assertEquals(Manager.getInstance().getTestReport(projectName).size(),
          ((FormPage) classUnderTest.getActivePageInstance()).getTable().getTable().getItemCount());

      // Tests the FormPage of the Time Record Page
      classUnderTest.setActivePage(Constants.ID_TIME_RECORD_FORM);
      assertEquals(Constants.ID_TIME_RECORD_FORM, ((FormPage) classUnderTest.getActivePageInstance()).getId());
      assertEquals(projectName, ((FormPage) classUnderTest.getActivePageInstance()).getProject().getProjectName());
      assertEquals(Manager.getInstance().getTimeRecord(projectName).size(),
          ((FormPage) classUnderTest.getActivePageInstance()).getTable().getTable().getItemCount());

    } catch (Exception e) {
    }

  }

  private void delay(long waitTimeMillis) {
    try {
      // wait milli seconds
      Display display = Display.getCurrent();

      if (display != null) {
        long endTimeMillis = System.currentTimeMillis() + waitTimeMillis;
        while (System.currentTimeMillis() < endTimeMillis) {
          if (!display.readAndDispatch())
            display.sleep();
        }
        display.update();
      }

      else {
        try {
          Thread.sleep(waitTimeMillis);
        } catch (InterruptedException e) {
        }
      }
    } catch (IllegalStateException e) {

    }
  }

  public void waitForJobs() {
    // wait for jobs
    while (!Job.getJobManager().isIdle())
      delay(1000);
  }

  @After
  public void cleanUp() {
    // clean up the database and close the editor
    waitForJobs();

    try {
      PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().closeEditor(classUnderTest, false);
    } catch (NoClassDefFoundError e) {

    }

    Manager.getInstance().deleteCompleteProject(projectName);
  }

  private String getAbsolutePath() {
    File file = new File("psp.csv");
    return file.getAbsolutePath();
  }

  private static PSPProject loadTestData() {
    File file = new File("psp.csv");
    PSPProject psp = null;

    try {
      // read data from file and generate the PSP Project data
      InputStream stream = new FileInputStream(file);

      byte[] b = new byte[stream.available()];
      stream.read(b);
      psp = PSPCSVParser.read(new StringReader(new String(b)));
      stream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return psp;
  }
}
