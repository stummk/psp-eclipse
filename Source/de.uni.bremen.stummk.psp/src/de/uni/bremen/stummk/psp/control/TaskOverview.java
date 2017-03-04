package de.uni.bremen.stummk.psp.control;


import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.menus.IMenuService;
import org.eclipse.ui.part.ViewPart;

import de.uni.bremen.stummk.psp.Activator;
import de.uni.bremen.stummk.psp.calculation.AddAction;
import de.uni.bremen.stummk.psp.calculation.DeleteAction;
import de.uni.bremen.stummk.psp.calculation.Filter;
import de.uni.bremen.stummk.psp.calculation.Manager;
import de.uni.bremen.stummk.psp.calculation.ToolbarAction;
import de.uni.bremen.stummk.psp.calculation.ToolbarController;
import de.uni.bremen.stummk.psp.data.PersistenceItem;
import de.uni.bremen.stummk.psp.data.Project;
import de.uni.bremen.stummk.psp.data.Task;
import de.uni.bremen.stummk.psp.data.TimeRecord;
import de.uni.bremen.stummk.psp.ui.AddButton;
import de.uni.bremen.stummk.psp.ui.DeleteButton;
import de.uni.bremen.stummk.psp.ui.Table;
import de.uni.bremen.stummk.psp.ui.SearchField;
import de.uni.bremen.stummk.psp.ui.TaskOverviewComposite;
import de.uni.bremen.stummk.psp.utility.CheckOperation;
import de.uni.bremen.stummk.psp.utility.Constants;

/**
 * Represents an Overview of all tasks of a project
 * 
 * @author Konstantin
 */
public class TaskOverview extends ViewPart {
  private TaskOverviewComposite container;
  private Project projectID;
  private ISelectionListener listener;
  private List<Task> tasks = new ArrayList<Task>();
  private AddButton btnNew;
  private DeleteButton btnDel;
  private ToolbarController tbContr;
  private boolean isDispose;

  /**
   * Contructor
   */
  public TaskOverview() {
    // listener which handle the shown tasks
    isDispose = false;
    listener = new ISelectionListener() {
      public void selectionChanged(IWorkbenchPart part, ISelection sel) {
        if (!(sel instanceof IStructuredSelection)) {
          return;
        }

        IProject p = CheckOperation.getProjectBySelection(sel);
        if (p != null && CheckOperation.projectContainFile(p)) {
          projectID = Manager.getInstance().getProjectByName(p.getName());
          ((AddButton) container.getAddButton()).changeProject(projectID);
          refresh(container.getTable().getSelection());
          btnNew.activate();
          btnDel.activate();
        }
      }
    };
  }

  @Override
  public void createPartControl(Composite parent) {
    this.container = new TaskOverviewComposite(parent, SWT.NONE, projectID);
    getSite().getPage().addSelectionListener(listener);

    // Searchfield
    SearchField searchField = container.getSearchField();
    searchField.getSearchField().addModifyListener(new ModifyListener() {

      @Override
      public void modifyText(ModifyEvent e) {
        if (searchField.getSearchField().getText().equals("")) {
          refresh(container.getTable().getSelection());
        } else {
          refresh(container.getTable().getSelection());
          searchField.search(container.getTable());
        }
      }
    });

    // Add task button
    btnNew = (AddButton) container.getAddButton();
    btnNew.deactivate();

    // Delete button
    btnDel = (DeleteButton) container.getDeleteButton();
    btnDel.deactivate();

    // table mouse listener
    container.getTable().getTable().addMouseListener(new MouseAdapter() {

      @Override
      public void mouseDown(MouseEvent e) {
        setRunningTaskData();
        if (container.getTable().getSelection() instanceof Task) {
          markTimeRecords((Task) container.getTable().getSelection());
        } else {
          refreshTimeRecordLog();
        }

        // open context menu if mouse button 3 is clicked
        if (e.button == 3 && projectID != null) {
          createContextMenu();
        }

        super.mouseDown(e);
      }

      @Override
      public void mouseDoubleClick(MouseEvent e) {
        if (container.getTable().getSelection() instanceof Task) {
          new AddTaskDialog(getSite().getShell(), projectID, (Task) container.getTable().getSelection()).open();
        }
        super.mouseDoubleClick(e);
      }
    });

    // create toolbar
    tbContr = new ToolbarController(this);
    tbContr.deactivateAllActions();
    tbContr.setEnableTaskCompleteAction(false);

    // table focus listener
    container.getTable().getTable().addFocusListener(new FocusListener() {

      @Override
      public void focusLost(FocusEvent e) {
        if (!tbContr.taskIsRunning()) {
          tbContr.deactivateAllActions();
          tbContr.setEnableTaskCompleteAction(false);
          tbContr.clear();
        } else {
          tbContr.setEnableTaskCompleteAction(false);
        }
      }

      @Override
      public void focusGained(FocusEvent e) {
        setRunningTaskData();
      }
    });

    btnDel.setTable(getTable());
  }

  protected void createContextMenu() {
    IMenuService mSvc = (IMenuService) getSite().getWorkbenchWindow().getService(IMenuService.class);
    MenuManager mgr = new MenuManager();
    mSvc.populateContributionManager(mgr, "popup:de.uni.bremen.stummk.psp.menuContribution");
    container.getTable().getTable().setMenu(null);

    AddAction action = new AddAction(container.getShell(), Constants.ID_TASK_OVERVIEW, projectID, null,
        Constants.COMMAND_ADD, "Add", Activator.getImageDescriptor("resources/icons/add.png"));
    action.setPhase(container.getTable().getPhaseOfSelection());
    mgr.add(action);

    if (container.getTable().getSelection() instanceof Task) {
      mgr.add(new AddAction(container.getShell(), Constants.ID_TASK_OVERVIEW, projectID,
          container.getTable().getSelection(), Constants.COMMAND_ADD_TIME_RECORD, "Add Time Record",
          Activator.getImageDescriptor("resources/icons/addtime.png")));
      mgr.add(new AddAction(container.getShell(), Constants.ID_TASK_OVERVIEW, projectID,
          container.getTable().getSelection(), Constants.COMMAND_ADD, "Edit",
          Activator.getImageDescriptor("resources/icons/edit.png")));
      mgr.add(new DeleteAction("Delete", Constants.COMMAND_DELETE, container.getTable(),
          Activator.getImageDescriptor("resources/icons/delete.png")));

      if (!((Task) container.getTable().getSelection()).isComplete()) {
        mgr.insertBefore(Constants.COMMAND_ADD, new ToolbarAction("Complete Task", Constants.COMMAND_COMPLETE_TASK,
            tbContr, Activator.getImageDescriptor("resources/icons/complete.png")));
        mgr.insertAfter(Constants.COMMAND_COMPLETE_TASK, new Separator());

        if (!tbContr.taskIsRunning()) {
          mgr.insertBefore(Constants.COMMAND_COMPLETE_TASK, new ToolbarAction("Resume Task", Constants.COMMAND_START,
              tbContr, Activator.getImageDescriptor("resources/icons/play.png")));
        } else if (selectionIsRunningTask() && tbContr.getStatus() == Constants.STATUS_TASK_RUNNING) {
          mgr.insertBefore(Constants.COMMAND_COMPLETE_TASK, new ToolbarAction("Interrupt Task",
              Constants.COMMAND_INTERRUPT, tbContr, Activator.getImageDescriptor("resources/icons/interupt.png")));
          mgr.insertBefore(Constants.COMMAND_COMPLETE_TASK, new ToolbarAction("Stop Task", Constants.COMMAND_STOP,
              tbContr, Activator.getImageDescriptor("resources/icons/stop.png")));
        } else if (selectionIsRunningTask() && tbContr.getStatus() == Constants.STATUS_TASK_INTERRUPT) {
          mgr.insertBefore(Constants.COMMAND_COMPLETE_TASK, new ToolbarAction("Resume Task", Constants.COMMAND_START,
              tbContr, Activator.getImageDescriptor("resources/icons/play.png")));
          mgr.insertBefore(Constants.COMMAND_COMPLETE_TASK, new ToolbarAction("Stop Task", Constants.COMMAND_STOP,
              tbContr, Activator.getImageDescriptor("resources/icons/stop.png")));
        }
      }
    }

    container.getTable().getTable().setMenu(mgr.createContextMenu(container.getTable().getTable()));
  }

  private boolean selectionIsRunningTask() {
    if (container.getTable().getSelection() instanceof Task) {
      Task task = (Task) container.getTable().getSelection();
      return (tbContr.taskIsRunning() && tbContr.getRunningTask().getID() == task.getID());
    }
    return false;
  }

  /**
   * refreshes the time recording log in the editor
   */
  protected void refreshTimeRecordLog() {
    Editor edit = getEditor();
    if (edit != null && edit.getActivePageInstance() instanceof FormPage) {
      FormPage page = (FormPage) edit.getActivePageInstance();
      if (page.getId().equals(Constants.ID_TIME_RECORD_FORM)) {
        page.refresh(Constants.ID_TIME_RECORD_FORM);
      }
    }
  }

  @Override
  public void setFocus() {}


  @Override
  public void dispose() {
    isDispose = true;
    stopRunningTask();
    getSite().getPage().removeSelectionListener(listener);
  }

  /**
   * Refreshes the {@link TaskOverview}
   */
  public void refresh(PersistenceItem persistenceItem) {
    container.getTable().saveExpandedPrefs();
    container.getTable().drawTaskOverviewColumns();
    if (projectID != null) {
      container.getTable().clear();
      tasks = Manager.getInstance().getTaskPlanning(projectID.getProjectName());

      // set running task to first cell
      if (tbContr.taskIsRunning()) {
        container.getTable().setRunningTask(tbContr.getRunningTask());
        tasks.remove(tbContr.getRunningTask());
      } else {
        container.getTable().setRunningTask(null);
      }

      tasks = Filter.filter(tasks);
      getTable().setDefaultItems(tasks);
      getTable().sort();
    }

    if (persistenceItem instanceof Task) {
      container.getTable().select((Task) persistenceItem);
    }

    if (!(container.getTable().getSelection() instanceof Task)) {
      tbContr.setEnableTaskCompleteAction(false);
    }

    if (container.getTable().getSelection() instanceof Task && ((Task) container.getTable().getSelection()).isComplete()
        && !tbContr.taskIsRunning()) {
      tbContr.setEnableTaskCompleteAction(false);
      tbContr.deactivateAllActions();
    } else if (container.getTable().getSelection() instanceof Task
        && ((Task) container.getTable().getSelection()).isComplete() && tbContr.taskIsRunning()) {
      tbContr.setEnableTaskCompleteAction(false);
    }

    btnDel.setTable(getTable());
  }

  private void setRunningTaskData() {
    Task task = null;
    if (container.getTable().getSelection() instanceof Task) {
      task = (Task) container.getTable().getSelection();
      btnDel.activate();
    }

    if (!(container.getTable().getSelection() instanceof Task) && !this.tbContr.taskIsRunning()) {
      task = null;
      tbContr.deactivateRun();
      tbContr.setEnableTaskCompleteAction(false);
      btnDel.deactivate();
    }

    if (!(container.getTable().getSelection() instanceof Task) && this.tbContr.taskIsRunning()) {
      task = null;
      tbContr.setEnableTaskCompleteAction(false);
      btnDel.deactivate();
    }

    // If completed task is selected
    if (task != null && task.isComplete() && !this.tbContr.taskIsRunning()) {
      tbContr.deactivateRun();
      tbContr.setEnableTaskCompleteAction(false);
    } else if (task != null && !task.isComplete() && !this.tbContr.taskIsRunning()) {
      // if no task is running and task is selected
      tbContr.setProject(this.projectID);
      tbContr.setTask((Task) container.getTable().getSelection());
      tbContr.activateRun();
      tbContr.setTaskToComplete(task);
      tbContr.setEnableTaskCompleteAction(true);
    } else if (task != null && !task.isComplete() && this.tbContr.taskIsRunning()) {
      // if task is running and task is not complete
      tbContr.setTaskToComplete(task);
      tbContr.setEnableTaskCompleteAction(true);
    }
  }

  private void markTimeRecords(Task selection) {
    // if timerecord page is open mark tasks
    Editor edit = getEditor();
    if (edit != null && edit.getActivePageInstance() instanceof FormPage) {
      FormPage page = (FormPage) edit.getActivePageInstance();
      if (page.getId().equals(Constants.ID_TIME_RECORD_FORM)) {
        page.refresh(Constants.ID_TIME_RECORD_FORM);
        TreeItem[] items = page.getTable().getTable().getItems();
        for (TreeItem item : items) {
          if (item.getData() instanceof TimeRecord && ((TimeRecord) item.getData()).getTask() != null
              && ((TimeRecord) item.getData()).getTask().getID() == selection.getID()) {
            item.setBackground(new Color(item.getDisplay(), 132, 112, 255));
          }
        }
      }
    }
  }

  /**
   * Stops the running task
   */
  public void stopRunningTask() {
    if (this.tbContr.taskIsRunning()) {
      this.tbContr.stopTask();
    }
  }

  /**
   * @return the {@link ToolbarController} of this {@link TaskOverview}
   */
  public ToolbarController getToolbarController() {
    return this.tbContr;
  }

  /**
   * @return the project of the task overview
   */
  public Project getProject() {
    return this.projectID;
  }

  /**
   * @return if this View is disposed
   */
  public boolean isDisposed() {
    return isDispose;
  }

  /**
   * @return the table of the view
   */
  public Table getTable() {
    return container.getTable();
  }

  private Editor getEditor() {
    IEditorReference[] editorReference =
        PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();
    for (IEditorReference reference : editorReference) {
      if (reference.getEditor(false) instanceof Editor) {
        Editor edit = ((Editor) reference.getEditor(false));
        if (projectID != null && edit.getProject().getProjectName().equals(projectID.getProjectName())) {
          return edit;
        }
      }
    }
    return null;
  }

}
