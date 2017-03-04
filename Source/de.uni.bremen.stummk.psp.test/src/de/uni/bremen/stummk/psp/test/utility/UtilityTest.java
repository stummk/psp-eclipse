package de.uni.bremen.stummk.psp.test.utility;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.jdt.internal.ui.packageview.PackageExplorerPart;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Display;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.uni.bremen.stummk.psp.data.PSPProject;
import de.uni.bremen.stummk.psp.data.Project;
import de.uni.bremen.stummk.psp.data.ProjectPlanSummary;
import de.uni.bremen.stummk.psp.utility.CheckOperation;
import de.uni.bremen.stummk.psp.utility.DataIO;
import de.uni.bremen.stummk.psp.utility.PropertyTester;

/**
 * This class should be executed as JUNIT PLUGIN TEST. Test the classes in the utility package
 * 
 * @author Konstantin
 *
 */
@SuppressWarnings("restriction")
public class UtilityTest {
  private static final String projectName = "TESTPROJECTCHECK";
  private static final String notPSPProjectName = "TESTPROJECTCHECKFALSE";
  private IProject project;
  private IWorkspace ws;

  @Before
  public void setUp() throws Exception {
    // setting up db an load project
    waitForJobs();

    try {
      ws = ResourcesPlugin.getWorkspace();
      project = ws.getRoot().getProject(projectName);
      if (!project.exists()) {
        project.create(null);
      }
      if (!project.isOpen()) {
        project.open(null);
      }
    } catch (IllegalStateException | NoClassDefFoundError e) {
    }

    waitForJobs();
    delay(3000);
  }

  /**
   * Testing DataIO. Save a Prject to a file load it and chekcs if it has been loaded
   */
  @Test
  public void testLoadSave() {
    LocalDate date = LocalDate.now();
    Project p = new Project(projectName, date);
    ProjectPlanSummary pps = new ProjectPlanSummary(p);
    PSPProject pspp = new PSPProject(p, pps);
    PSPProject resultPSP = null;

    try {
      DataIO.saveToFile(projectName, pspp, null);
      resultPSP = DataIO.loadFromFile(projectName);
      assertNotNull(resultPSP);
      assertEquals(projectName, resultPSP.getProject().getProjectName());
      assertEquals(date, resultPSP.getProject().getTimestamp());
      assertNotNull(resultPSP.getSummary());
    } catch (IllegalStateException e) {
    }
  }

  /**
   * Tests CheckOperations. Create PSP file in Project and check if it conatain a file. Select one
   * Project and chekc the anme of the project
   */
  @SuppressWarnings("restriction")
  @Test
  public void testcheckOperationes() {
    LocalDate date = LocalDate.now();
    Project p = new Project(projectName, date);
    ProjectPlanSummary pps = new ProjectPlanSummary(p);
    PSPProject pspp = new PSPProject(p, pps);
    try {
      DataIO.saveToFile(projectName, pspp, null);
      assertTrue(CheckOperation.projectContainFile(project));
      IProject pr = ws.getRoot().getProject(notPSPProjectName);
      if (!pr.exists()) {
        pr.create(null);
      }
      if (!pr.isOpen()) {
        pr.open(null);
      }
      assertFalse(CheckOperation.projectContainFile(pr));

      PackageExplorerPart part = PackageExplorerPart.getFromActivePerspective();
      IResource resource = project;
      part.selectAndReveal(resource);

      ISelection selection = part.getSite().getSelectionProvider().getSelection();

      IProject ip = CheckOperation.getProjectBySelection(selection);
      assertEquals(projectName, ip.getName());

      ip = CheckOperation.getProjectBySelection(null);
      assertNull(ip);

    } catch (CoreException | IllegalStateException e) {
    }
  }

  /**
   * Test the PropertYTester.
   */
  @Test
  public void testPropertryTester() {
    LocalDate date = LocalDate.now();
    Project p = new Project(projectName, date);
    ProjectPlanSummary pps = new ProjectPlanSummary(p);
    PSPProject pspp = new PSPProject(p, pps);
    try {
      DataIO.saveToFile(projectName, pspp, null);
      assertTrue(CheckOperation.projectContainFile(project));
      IProject pr = ws.getRoot().getProject(projectName);
      if (!pr.exists()) {
        pr.create(null);
      }
      if (!pr.isOpen()) {
        pr.open(null);
      }

      PropertyTester pt = new PropertyTester();
      assertTrue(pt.test(pr, "", null, null));

      pr = ws.getRoot().getProject(notPSPProjectName);
      if (!pr.exists()) {
        pr.create(null);
      }
      if (!pr.isOpen()) {
        pr.open(null);
      }
      assertFalse(pt.test(pr, "", null, null));
    } catch (CoreException | IllegalStateException e) {
    }
  }


  private void delay(long waitTimeMillis) {
    try {
      // delay millis
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
    while (!Job.getJobManager().isIdle())
      delay(1000);
  }

  @After
  public void cleanUp() {
    waitForJobs();
  }
}
