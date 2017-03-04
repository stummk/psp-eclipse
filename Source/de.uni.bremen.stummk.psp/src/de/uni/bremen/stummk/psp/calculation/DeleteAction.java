package de.uni.bremen.stummk.psp.calculation;

import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import de.uni.bremen.stummk.psp.control.Editor;
import de.uni.bremen.stummk.psp.control.FormPage;
import de.uni.bremen.stummk.psp.control.TaskOverview;
import de.uni.bremen.stummk.psp.data.DefectRecord;
import de.uni.bremen.stummk.psp.data.PersistenceItem;
import de.uni.bremen.stummk.psp.data.Project;
import de.uni.bremen.stummk.psp.data.Task;
import de.uni.bremen.stummk.psp.data.TimeRecord;
import de.uni.bremen.stummk.psp.ui.Table;
import de.uni.bremen.stummk.psp.utility.Constants;

/**
 * this class represents a delete action, which deletes an {@link PersistenceItem}
 * 
 * @author Konstantin
 *
 */
public class DeleteAction extends Action implements IWorkbenchAction {

  private Table table;
  private FormPage page;

  /**
   * Constructor
   * 
   * @param text the text which will be shown
   * @param id the id of the Action ({@link Constants.COMMAND_DELETE}
   * @param table table, from which an item should be deleted
   * @param page the calling page
   * @param descriptor The image descriptor of this action
   */
  public DeleteAction(String text, String id, Table table, FormPage page, ImageDescriptor descriptor) {
    super(text);
    setId(id);
    setImageDescriptor(descriptor);
    this.table = table;
    this.page = page;
  }

  /**
   * Constructor
   * 
   * @param text the text which will be shown
   * @param id the id of the Action ({@link Constants.COMMAND_DELETE}
   * @param table table, from which an item should be deleted
   * @param descriptor The image descriptor of this action
   */
  public DeleteAction(String text, String id, Table table, ImageDescriptor descriptor) {
    super(text);
    setId(id);
    setImageDescriptor(descriptor);
    this.table = table;
  }

  @Override
  public void run() {
    if (getId().equals(Constants.COMMAND_DELETE)) {
      delete();
    }
  }

  private void delete() {
    if (table.getTable().getSelection().length == 1) {
      if (isRunningTask(table)) {
        return;
      }

      PersistenceItem item = (PersistenceItem) table.getTable().getSelection()[0].getData();
      Project project = null;

      // If no page is calling, than get project from view
      if (page != null) {
        project = page.getProject();
      } else {
        TaskOverview view = (TaskOverview) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
            .findView(Constants.ID_TASK_OVERVIEW);
        project = view.getProject();
      }

      // if a task should be deleted deactivate all actions in the taskoverview and delete the task
      // if task is a defect fix delete the defect too
      if (item instanceof Task) {
        TaskOverview view = (TaskOverview) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
            .findView(Constants.ID_TASK_OVERVIEW);
        if (view != null && !view.getToolbarController().taskIsRunning()) {
          view.getToolbarController().deactivateAllActions();
        }
        if (((Task) item).getType().equals(Constants.TASK_TYPE_DEFECT_FIX)) {
          DefectRecord rec = Manager.getInstance().getDefectRecord(project.getProjectName(), ((Task) item).getID());
          if (rec != null) {
            item = rec;
            deleteDefectRecord((DefectRecord) item, project);
          }
        } else {
          deleteTask((Task) item, project);
        }
      }

      // delete defect record
      if (item instanceof DefectRecord) {
        deleteDefectRecord((DefectRecord) item, project);
      }

      // delete time record
      if (item instanceof TimeRecord) {
        deleteTimeRecord((TimeRecord) item, project);
      }

      Manager.getInstance().delete(item);

      // update the cumulative values in the schedule plan
      if (item instanceof Task || (item instanceof DefectRecord && ((DefectRecord) item).isFixManual())) {
        Calc.getInstance().updatePlannedValues(project.getProjectName());
        Calc.getInstance().updateCumHours(project);
        Calc.getInstance().updatePlanCumValue(project);
        Calc.getInstance().updateActualCumValue(project);
      }

      refresh();
    }
  }

  private boolean isRunningTask(Table table) {
    // checks if selected item in table is the running task
    TaskOverview view = (TaskOverview) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
        .findView(Constants.ID_TASK_OVERVIEW);
    if (view != null && view.getToolbarController().taskIsRunning()) {
      if (table.getTable().getSelection()[0].getData() instanceof Task) {
        Task task = (Task) table.getTable().getSelection()[0].getData();
        if (task.getName().equals(view.getToolbarController().getRunningTask().getName())) {
          MessageDialog.openError(table.getTable().getShell(), "Error",
              "Running task can not be deleted. Please, stop it first!");
          return true;
        }
      }
    }
    return false;
  }

  private void refresh() {
    // refreshes the taskoverview and editor
    TaskOverview view = (TaskOverview) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
        .findView(Constants.ID_TASK_OVERVIEW);

    if (view != null) {
      view.refresh(null);
    }
    if (page != null) {
      page.refresh(page.getId());
      ((Editor) page.getEditor()).refreshSummaryPage();
    } else {
      IEditorReference[] editorReference =
          PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();
      for (IEditorReference reference : editorReference) {
        if (reference.getEditor(false) instanceof Editor) {
          Editor edit = ((Editor) reference.getEditor(false));
          edit.refreshPages();
        }
      }
    }
  }

  private void deleteTask(Task item, Project project) {
    // deletes the task and updates the summary
    List<TimeRecord> list = Manager.getInstance().getTimeRecord(project.getProjectName(), ((Task) item).getID());
    if (list != null) {
      for (TimeRecord tr : list) {
        tr.setTask(null);
        Manager.getInstance().update(tr);
      }
    }
    Calc.getInstance().deletePlanHoursFromSchedule(project, (Task) item);
    Calc.getInstance().subSummaryValue((Task) item, project.getProjectName());
  }

  private void deleteTimeRecord(TimeRecord item, Project project) {
    // delete time from task and defect if defect fix
    Task task = item.getTask();
    if (task != null) {
      task.update(-item.getDeltaTime());
      Manager.getInstance().update(task);

      if (task.getType().equals(Constants.TASK_TYPE_DEFECT_FIX)) {
        DefectRecord rec = Manager.getInstance().getDefectRecord(project.getProjectName(), task.getID());
        if (rec != null) {
          rec.updateFixTime(-MathCalc.getInstance().fromSecondToMinute(item.getDeltaTime()));
          Manager.getInstance().update(rec);
        }
      }
    }

    // deletes values from summary
    Calc.getInstance().deleteActualHoursFromSchedule(project, (TimeRecord) item);
    Calc.getInstance().subSummaryValue((TimeRecord) item, project.getProjectName());
  }

  private void deleteDefectRecord(DefectRecord item, Project project) {
    // deletes task if is manual fix and delete values from summary
    Calc.getInstance().subSummaryValue((DefectRecord) item, project.getProjectName());
    if (!item.isFixManual()) {
      Task task = item.getTask();
      if (task != null) {
        deleteTask(task, project);
        item.setTask(null);
        Manager.getInstance().update(item);
        Manager.getInstance().delete(task);
      }
    }
  }

  @Override
  public void dispose() {}

}
