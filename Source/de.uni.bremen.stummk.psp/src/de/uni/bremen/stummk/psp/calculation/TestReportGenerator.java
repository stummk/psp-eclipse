package de.uni.bremen.stummk.psp.calculation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jdt.junit.TestRunListener;
import org.eclipse.jdt.junit.model.ITestCaseElement;
import org.eclipse.jdt.junit.model.ITestElement.Result;
import org.eclipse.jdt.junit.model.ITestRunSession;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import de.uni.bremen.stummk.psp.control.TaskOverview;
import de.uni.bremen.stummk.psp.data.Phase;
import de.uni.bremen.stummk.psp.data.Project;
import de.uni.bremen.stummk.psp.data.TestReport;
import de.uni.bremen.stummk.psp.data.TimeRecord;
import de.uni.bremen.stummk.psp.utility.Constants;

/**
 * Generates one Test report entry, when a junit test is launching
 * 
 * @author Konstantin
 *
 */
public class TestReportGenerator extends TestRunListener {
  private TimeRecord record;
  private TaskOverview view;

  @Override
  public void sessionLaunched(ITestRunSession session) {
    record = null;

    // checks if task is running
    Display.getDefault().syncExec(new Runnable() {
      @Override
      public void run() {
        IWorkbenchWindow[] iw = PlatformUI.getWorkbench().getWorkbenchWindows();
        for (IWorkbenchWindow w : iw) {
          IWorkbenchPage[] ip = w.getPages();
          for (IWorkbenchPage p : ip) {
            IViewReference[] vp = p.getViewReferences();
            for (IViewReference r : vp) {
              if (r.getId().equals(Constants.ID_TASK_OVERVIEW)) {
                view = (TaskOverview) r.getView(true);
              }
            }
          }
        }

        // if no Task Overview is open, create a new TimeRecord
        if (view == null) {
          createTimeRecord(session);
        }

        // If no task is running
        if (view != null && !view.getToolbarController().taskIsRunning()) {
          createTimeRecord(session);
        }

        // if running task is from phase test, dont create tie record
        if (view != null && view.getToolbarController().taskIsRunning()
            && view.getToolbarController().getRunningTask().getPhase().equals(Phase.TEST)) {
          view.getToolbarController().deactivateAllActions();
        }

        // if running task is not in phase test, stop it and create time record
        if (view != null && view.getToolbarController().taskIsRunning()
            && !view.getToolbarController().getRunningTask().getPhase().equals(Phase.TEST)) {
          view.getToolbarController().stopTask();
          createTimeRecord(session);
        }
      }

    });
    super.sessionLaunched(session);
  }

  @Override
  public void sessionFinished(ITestRunSession session) {
    Display.getDefault().syncExec(new Runnable() {
      @Override
      public void run() {

        IWorkbenchWindow[] iw = PlatformUI.getWorkbench().getWorkbenchWindows();
        for (IWorkbenchWindow w : iw) {
          IWorkbenchPage[] ip = w.getPages();
          for (IWorkbenchPage p : ip) {
            IViewReference[] vp = p.getViewReferences();
            for (IViewReference r : vp) {
              if (r.getId().equals(Constants.ID_TASK_OVERVIEW)) {
                view = (TaskOverview) r.getView(true);
              }
            }
          }
        }
        // if running task is in phase test activate buttons
        if (view != null && view.getToolbarController().taskIsRunning()
            && view.getToolbarController().getRunningTask().getPhase().equals(Phase.TEST)) {
          view.getToolbarController().activateInterrupt();
          view.getToolbarController().activateStop();
        }
        // if a new record has been created save it to db
        if (record != null) {
          record.setEndtime(LocalDate.now(), LocalTime.now().withNano(0));
          Manager.getInstance().saveToDB(record);
        }
      }
    });
    super.sessionFinished(session);
  }

  @Override
  public void testCaseFinished(ITestCaseElement testCaseElement) {
    Result result = testCaseElement.getTestResult(true);

    if (!result.equals(Result.IGNORED) || !result.equals(Result.UNDEFINED)) {
      String testname = testCaseElement.getTestClassName() + "." + testCaseElement.getTestMethodName();
      String projectName = testCaseElement.getTestRunSession().getLaunchedProject().getProject().getName();

      TestReport testReportEntry = Manager.getInstance().getTestReportEntry(projectName, testname);
      String expected = "";
      String actual = "";

      if (result.equals(Result.OK)) {
        // test has no error
        expected = "OK";
        actual = "OK";
      } else if (result.equals(Result.ERROR) || result.equals(Result.FAILURE)) {
        // test failes
        String failureTrace = testCaseElement.getFailureTrace().getTrace();
        Matcher matcher = Pattern.compile("<(.+?)>").matcher(failureTrace);
        List<String> groupValues = new ArrayList<String>();

        while (matcher.find()) {
          groupValues.add(matcher.group(1));
        }
        expected = groupValues.get(0);
        actual = failureTrace.split(" ")[0] + " - " + groupValues.get(1);
      }

      // add test entry to db
      if (testReportEntry != null) {
        testReportEntry.refreshItems(expected, actual);
        Manager.getInstance().update(testReportEntry);
      } else {
        Project project = Manager.getInstance().getProjectByName(projectName);
        if (project != null) {
          testReportEntry = new TestReport(project, testname, expected, actual);
          Manager.getInstance().saveToDB(testReportEntry);
        }
      }
    }
    super.testCaseFinished(testCaseElement);
  }

  private void createTimeRecord(ITestRunSession session) {
    Project p = Manager.getInstance().getProjectByName(session.getLaunchedProject().getProject().getName());
    if (p != null) {
      record = new TimeRecord(LocalDate.now(), LocalTime.now().withNano(0), p, Phase.TEST, null);
    }
  }

  /**
   * @return the time record
   */
  public TimeRecord getTimeRecord() {
    return record;
  }

}
