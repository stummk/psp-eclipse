package de.uni.bremen.stummk.psp.test.control;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.time.LocalDate;

import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.PlatformUI;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.uni.bremen.stummk.psp.calculation.Manager;
import de.uni.bremen.stummk.psp.control.TaskOverview;
import de.uni.bremen.stummk.psp.data.PSPProject;
import de.uni.bremen.stummk.psp.data.Phase;
import de.uni.bremen.stummk.psp.data.Project;
import de.uni.bremen.stummk.psp.data.Task;
import de.uni.bremen.stummk.psp.utility.Constants;
import de.uni.bremen.stummk.psp.utility.DBConnection;
import de.uni.bremen.stummk.psp.utility.PSPCSVParser;

/**
 * This class should be executed as JUNIT PLUGIN TEST. Tests the TaskOverview
 * 
 * @author Konstantin
 *
 */
public class TaskOverviewTest {
  private static final String projectName = "TESTPROJECT";
  private TaskOverview testView;
  private Project p;
  private static final String taskName = "TASK_NAME";
  private static final Phase phase = Phase.POSTMORTEM;
  private static final long timeValue = 600;
  private static final long one = 1;
  private static final LocalDate date = LocalDate.now();

  @BeforeClass
  public static void startUp() {
    // setting up the database
    DBConnection.init();

    if (Manager.getInstance().containsProject(projectName)) {
      Manager.getInstance().deleteCompleteProject(projectName);
    }
  }

  @Before
  public void setUp() throws Exception {
    // creates the taskOveriew and load test data and save it in the db
    waitForJobs();

    try {
      testView = (TaskOverview) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
          .showView(Constants.ID_TASK_OVERVIEW);
    } catch (NoClassDefFoundError e) {

    }

    waitForJobs();
    delay(3000);
    PSPProject psp = loadTestData();
    Manager.getInstance().saveBackupProject(psp);
    assertEquals(psp.getProject().getProjectName(),
        Manager.getInstance().loadBackupProject(projectName).getProject().getProjectName());
    p = psp.getProject();
  }

  /**
   * Tests the TaskOverview
   */
  @Test
  public void testView() {
    try {
      // no task is running
      assertEquals(Constants.STATUS_NO_TASK, testView.getToolbarController().getStatus());
      assertFalse(testView.getToolbarController().taskIsRunning());

      // task and project are set
      Task task = new Task(taskName, 0, phase, timeValue, date, 0, 0, one, p, Constants.TASK_TYPE_TASK);
      testView.getToolbarController().setTask(task);
      testView.getToolbarController().setProject(p);

      assertEquals(p.getProjectName(), testView.getToolbarController().getProject().getProjectName());
      assertEquals(taskName, testView.getToolbarController().getRunningTask().getName());

      // Start running task and test if it is running
      testView.getToolbarController().activateRun();
      testView.getToolbarController().startTask();
      assertTrue(testView.getToolbarController().taskIsRunning());
      assertEquals(taskName, testView.getToolbarController().getRunningTask().getName());
      assertEquals(Constants.STATUS_TASK_RUNNING, testView.getToolbarController().getStatus());

      // interrupt task and check if it is interrupted
      testView.getToolbarController().interruptTask();
      assertEquals(Constants.STATUS_TASK_INTERRUPT, testView.getToolbarController().getStatus());
      assertTrue(testView.getToolbarController().taskIsRunning());
      assertEquals(taskName, testView.getToolbarController().getRunningTask().getName());

      // resume task and check if it is resumed
      testView.getToolbarController().startTask();
      assertEquals(Constants.STATUS_TASK_RUNNING, testView.getToolbarController().getStatus());
      assertTrue(testView.getToolbarController().taskIsRunning());
      assertEquals(taskName, testView.getToolbarController().getRunningTask().getName());

      /// test completeing a task
      Task task1 = new Task("completeTask", 0, phase, timeValue, date, 0, 0, one, p, Constants.TASK_TYPE_TASK);
      testView.getToolbarController().setTaskToComplete(task1);
      testView.getToolbarController().completeTask();
      assertEquals(taskName, testView.getToolbarController().getRunningTask().getName());

      // test stopping task and check if it is stopped
      testView.getToolbarController().stopTask();
      assertEquals(Constants.STATUS_NO_TASK, testView.getToolbarController().getStatus());
      assertFalse(testView.getToolbarController().taskIsRunning());
      assertNull(testView.getToolbarController().getRunningTask());
      assertNull(testView.getToolbarController().getProject());

    } catch (Exception e) {
    }
  }


  private void delay(long waitTimeMillis) {
    // delay millisec
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
  }

  public void waitForJobs() {
    while (!Job.getJobManager().isIdle())
      delay(1000);
  }

  @After
  public void cleanUp() {
    // clean ups. close TaskOverview and delete data from db
    waitForJobs();

    try {
      PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().hideView(testView);
    } catch (NoClassDefFoundError e) {

    }

    Manager.getInstance().deleteCompleteProject(projectName);
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
