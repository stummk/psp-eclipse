package de.uni.bremen.stummk.psp.calculation;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.ProgressMonitorDialog;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import de.uni.bremen.stummk.psp.control.BarChart;
import de.uni.bremen.stummk.psp.control.LineChart;
import de.uni.bremen.stummk.psp.control.PieChart;
import de.uni.bremen.stummk.psp.data.PSPProject;
import de.uni.bremen.stummk.psp.data.ScheduleEntry;
import de.uni.bremen.stummk.psp.utility.CheckOperation;
import de.uni.bremen.stummk.psp.utility.Constants;
import de.uni.bremen.stummk.psp.utility.DataIO;
import de.uni.bremen.stummk.psp.utility.FileHash;

/**
 * Class represents an action of the toolbar in the editor
 * 
 * @author Konstantin
 *
 */
public class EditorToolbarAction extends Action implements IWorkbenchAction {
  private EditorToolbarController etc;

  /**
   * Constructor
   * 
   * @param id the Id of the Action
   * @param editorToolbarController the {@link EditorToolbarController} of the
   *        {@link EditorToolbarAction}
   */
  public EditorToolbarAction(String id, EditorToolbarController editorToolbarController) {
    setId(id);
    this.etc = editorToolbarController;
  }

  @Override
  public void run() {
    handleAction(getId());
  }

  private void handleAction(String id) {
    // execute action depending on id
    switch (id) {
      case Constants.COMMAND_SYNC:
        exportData();
        break;
      case Constants.COMMAND_PLAN_ACTUAL_DIAGRAM:
        new BarChart(etc.getProjectPlanSummary(),
            "Plan vs. Actual Values - " + etc.getProjectPlanSummary().getProject().getProjectName());
        break;
      case Constants.COMMAND_TIME_IN_PHASE_PERCENTAGE:
        new PieChart(etc.getProjectPlanSummary(), Constants.KEY_TIME_IN_PHASE_IDX,
            "Distribution of time in phase - " + etc.getProjectPlanSummary().getProject().getProjectName());
        break;
      case Constants.COMMAND_DEFECT_INJECTED_PERCENTAGE:
        new PieChart(etc.getProjectPlanSummary(), Constants.KEY_DEFECTS_INJECTED_IDX,
            "Distribution of injected defects - " + etc.getProjectPlanSummary().getProject().getProjectName());
        break;
      case Constants.COMMAND_DEFECT_REMOVED_PERCENTAGE:
        new PieChart(etc.getProjectPlanSummary(), Constants.KEY_DEFECTS_REMOVED_IDX,
            "Distribution of removed defects - " + etc.getProjectPlanSummary().getProject().getProjectName());
        break;
      case Constants.COMMAND_TIME_TRACKING:
        List<ScheduleEntry> entries =
            Manager.getInstance().getSchedulePlanning(etc.getProjectPlanSummary().getProject().getProjectName());
        new LineChart("Time Progress in Project - " + etc.getProjectPlanSummary().getProject().getProjectName(),
            Constants.CHART_TIME, entries);
        break;
      case Constants.COMMAND_EARNED_VALUE_TRACKING:
        List<ScheduleEntry> e =
            Manager.getInstance().getSchedulePlanning(etc.getProjectPlanSummary().getProject().getProjectName());
        new LineChart("Earned Value Tracking in Project - " + etc.getProjectPlanSummary().getProject().getProjectName(),
            Constants.CHART_VALUE, e);
        break;
    }
  }

  private void exportData() {
    // exports data to psp-file and create hash
    try {
      Shell activeShell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
      IRunnableWithProgress op = new IRunnableWithProgress() {

        @Override
        public void run(IProgressMonitor monitor) throws InvocationTargetException, InterruptedException {
          try {
            monitor.beginTask("Export data to psp.csv file", 2);
            PSPProject psp =
                Manager.getInstance().loadBackupProject(etc.getProjectPlanSummary().getProject().getProjectName());
            if (psp != null && psp.getSummary() != null) {
              DataIO.saveToFile(etc.getProjectPlanSummary().getProject().getProjectName(), psp, null);
            }
            monitor.worked(1);
            if (psp != null && psp.getSummary() != null) {
              IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
              for (IProject project : projects) {
                if (project.getName().equals(etc.getProjectPlanSummary().getProject().getProjectName())) {
                  IFile file = CheckOperation.getProjectFile(project);
                  String hash = FileHash.hash(file);
                  try {
                    file.setPersistentProperty(Constants.PROPERTY_HASH, hash);
                  } catch (CoreException e) {
                    e.printStackTrace();
                  }
                }
              }
            }
            monitor.worked(1);
          } finally {
            monitor.done();
          }

        }
      };

      new ProgressMonitorDialog(activeShell).run(true, true, op);
    } catch (InvocationTargetException | InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void dispose() {}

}
