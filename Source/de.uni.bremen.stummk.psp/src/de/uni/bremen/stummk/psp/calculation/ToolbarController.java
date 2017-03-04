package de.uni.bremen.stummk.psp.calculation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import de.uni.bremen.stummk.psp.Activator;
import de.uni.bremen.stummk.psp.control.Editor;
import de.uni.bremen.stummk.psp.control.TaskOverview;
import de.uni.bremen.stummk.psp.data.DefectRecord;
import de.uni.bremen.stummk.psp.data.Project;
import de.uni.bremen.stummk.psp.data.ProjectPlanSummary;
import de.uni.bremen.stummk.psp.data.Task;
import de.uni.bremen.stummk.psp.data.TimeRecord;
import de.uni.bremen.stummk.psp.utility.Constants;

/**
 * The controller class of the {@link ToolbarAction}
 * 
 * @author Konstantin
 *
 */
public class ToolbarController {
  private Project project;
  private Task runningTask, taskToComplete;
  private TimeRecord timerecord;
  private int status;
  private TaskOverview view;
  private ToolbarAction startAction, interruptAction, stopAction, infoAction, completeTaskAction, filterAction;
  private List<LocalDateTime> startInterrupt;
  private List<LocalDateTime> endInterrupt;

  /**
   * Constructor
   * 
   * @param view the {@link TaskOverview} this controller belongs to
   */
  public ToolbarController(TaskOverview view) {
    this.view = view;
    this.status = Constants.STATUS_NO_TASK;
    this.startInterrupt = new ArrayList<>();
    this.endInterrupt = new ArrayList<>();
    createActions();
  }

  /**
   * activates the stop button
   */
  public void activateStop() {
    stopAction.setEnabled(true);
  }

  /**
   * Activates the interrupt button
   */
  public void activateInterrupt() {
    interruptAction.setEnabled(true);
  }

  /**
   * Deactivates all actions of the toolbar
   */
  public void deactivateAllActions() {
    startAction.setEnabled(false);
    interruptAction.setEnabled(false);
    stopAction.setEnabled(false);
  }

  /**
   * @return status of the controller <br>
   *         {@link Constants.STATUS_NO_TASK} if no task is running <br>
   *         {@link Constants.STATUS_TASK_RUNNING} if task is running <br>
   *         {@link Constants.STATUS_TASK_INTERRUPT} if running task is interrupted
   */
  public int getStatus() {
    return this.status;
  }

  /**
   * @return the project of the running task
   */
  public Project getProject() {
    return this.project;
  }

  /**
   * @return the current time record
   */
  public TimeRecord getTimeRecord() {
    return this.timerecord;
  }

  /**
   * @return the running task
   */
  public Task getRunningTask() {
    return this.runningTask;
  }

  /**
   * Sets the run action {@link #startAction} enabled
   */
  public void activateRun() {
    this.startAction.setEnabled(true);
  }

  /**
   * Sets the run action {@link #startAction} disabled
   */
  public void deactivateRun() {
    this.startAction.setEnabled(false);
  }

  /**
   * Sets the project of the running task
   * 
   * @param project the project
   */
  public void setProject(Project project) {
    this.project = project;
  }

  /**
   * Sets the running task
   * 
   * @param task the task, which will be running
   */
  public void setTask(Task task) {
    this.runningTask = task;
  }

  /**
   * Start the running task or resume a task, if it is interrupted. Create a new {@link TimeRecord}
   * or sets the endtime of an interrupt of an existing time record.
   */
  public void startTask() {
    if (this.status == Constants.STATUS_NO_TASK) {
      this.timerecord = new TimeRecord(LocalDate.now(), LocalTime.now().withNano(0), project,
          this.runningTask.getPhase(), runningTask);
    } else if (this.status == Constants.STATUS_TASK_INTERRUPT) {
      this.endInterrupt.add(LocalDateTime.now().withNano(0));
    }

    this.startAction.setEnabled(false);
    this.interruptAction.setEnabled(true);
    this.stopAction.setEnabled(true);
    this.runningTask.setStatus(Constants.STATUS_RUNNING);
    this.status = Constants.STATUS_TASK_RUNNING;
    this.view.refresh(this.runningTask);
  }

  /**
   * Interrupts a task. Sets the interrupt starttime of an time record.
   */
  public void interruptTask() {
    this.startInterrupt.add(LocalDateTime.now().withNano(0));
    this.interruptAction.setEnabled(false);
    this.startAction.setEnabled(true);
    this.stopAction.setEnabled(true);
    this.runningTask.setStatus(Constants.STATUS_INTERRUPT);
    this.status = Constants.STATUS_TASK_INTERRUPT;
    this.view.refresh(this.runningTask);
  }

  /**
   * Stops a running or interrupted task. If the task is complete, mark the running task as
   * complete.
   */
  public void stopTask() {
    deactivateAllActions();
    if (this.status == Constants.STATUS_TASK_INTERRUPT) {
      this.endInterrupt.add(LocalDateTime.now().withNano(0));
    }

    // calculate interruptTime
    long interruptTime = MathCalc.getInstance().calcSeconds(this.startInterrupt, this.endInterrupt);


    this.timerecord.setInterruptTime(interruptTime);
    this.timerecord.setEndtime(LocalDate.now(), LocalTime.now().withNano(0));

    this.runningTask.setActualTime(this.timerecord.getDeltaTime());

    if (!this.runningTask.isComplete()) {
      this.runningTask.setStatus(Constants.STATUS_STOPPED);
    }
    this.runningTask.setLastChange(LocalDateTime.now().withNano(0));

    // LoC count
    long loc = LoCCounter.count(project.getProjectName());
    ProjectPlanSummary plan = Manager.getInstance().getProjectSummary(project.getProjectName());
    double oldLoc = plan.get(Constants.KEY_PROGRAM_SIZE_IDX, Constants.KEY_SIZE_TOTAL_LOC, Constants.KEY_ACTUAL);
    plan.put(Constants.KEY_PROGRAM_SIZE_IDX, Constants.KEY_SIZE_TOTAL_LOC, Constants.KEY_ACTUAL, loc);
    plan.updateValue(Constants.KEY_PROGRAM_SIZE_IDX, Constants.KEY_SIZE_TOTAL_LOC, Constants.KEY_TO_DATE, -oldLoc);
    plan.updateValue(Constants.KEY_PROGRAM_SIZE_IDX, Constants.KEY_SIZE_TOTAL_LOC, Constants.KEY_TO_DATE, loc);
    Calc.getInstance().updateSummary(plan, Constants.KEY_ACTUAL);
    Calc.getInstance().updateSummary(plan, Constants.KEY_TO_DATE);
    Manager.getInstance().update(plan);

    // deleting overlapping time records, which are inserted manual
    List<TimeRecord> overlapping = Manager.getInstance().getTimeRecordOverlapps(this.timerecord.getID(),
        this.timerecord.getDate(), this.timerecord.getStarttime(), this.timerecord.getEndDate(),
        this.timerecord.getEndtime(), this.project.getProjectName());

    for (TimeRecord tr : overlapping) {
      Calc.getInstance().subSummaryValue(tr, this.project.getProjectName());
      Manager.getInstance().delete(tr);
    }

    // add time to defect record if task is defect fix
    if (runningTask.getType().equals(Constants.TASK_TYPE_DEFECT_FIX)) {
      DefectRecord rec = Manager.getInstance().getDefectRecord(project.getProjectName(), runningTask.getID());
      if (rec != null) {
        rec.updateFixTime(MathCalc.getInstance().fromSecondToMinute(timerecord.getDeltaTime()));
        Manager.getInstance().update(rec);
      }
    }

    Manager.getInstance().update(this.runningTask);
    Manager.getInstance().saveToDB(this.timerecord);
    Calc.getInstance().addSummaryValue(this.timerecord, this.project.getProjectName());
    Calc.getInstance().updateScheduleActualHour(this.project, this.timerecord);

    this.status = Constants.STATUS_NO_TASK;

    refresh(this.runningTask);
    setEnableTaskCompleteAction(false);
    clear();
  }

  /**
   * Refreshes the data in the {@link TaskOverview} and the pages of the {@link Editor}
   */
  public void refresh(Task task) {
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

    if (view != null && !view.isDisposed()) {
      view.refresh(task);
    }

    if (project != null) {

      IEditorReference[] editorReference =
          PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();
      for (IEditorReference reference : editorReference) {
        if (reference.getEditor(false) instanceof Editor) {
          Editor edit = ((Editor) reference.getEditor(false));
          if (edit.getProject().getProjectName().equals(project.getProjectName())) {
            edit.refreshPages();
          }
        }
      }
    }
  }

  /**
   * Mark a selected task in the table of the {@link TaskOverview} as complete
   */
  public void completeTask() {
    if (this.taskToComplete != null) {
      markTaskComplete(this.taskToComplete);

      if (taskIsRunning() && this.runningTask.getID() == this.taskToComplete.getID()) {
        stopTask();
      }
    }
  }


  /**
   * Sets the running task, the project of the task and the time record to null
   */
  public void clear() {
    this.project = null;
    this.timerecord = null;
    this.runningTask = null;
    this.startInterrupt.clear();
    this.endInterrupt.clear();
  }

  /**
   * @return true if a task is running
   */
  public boolean taskIsRunning() {
    return this.status != Constants.STATUS_NO_TASK;
  }

  /**
   * Enables or disables the {@link Constants.COMMAND_COMPLETE_TASK}
   * 
   * @param enable true to enable, false to disable
   */
  public void setEnableTaskCompleteAction(boolean enable) {
    this.completeTaskAction.setEnabled(enable);
  }

  /**
   * Sets the selected task in the table of the view, which can be marked as complete
   * 
   * @param task the task, which can be marked as complete
   */
  public void setTaskToComplete(Task task) {
    this.taskToComplete = task;
  }

  /**
   * @return the interruption time in seconds
   */
  public long getInterrupTime() {
    return MathCalc.getInstance().calcSeconds(startInterrupt, endInterrupt);
  }

  private void createActions() {
    // create start Action
    startAction = new ToolbarAction(Constants.COMMAND_START, this);
    startAction.setText("Resume Task");
    startAction.setImageDescriptor(Activator.getImageDescriptor("resources/icons/play.png"));
    view.getViewSite().getActionBars().getToolBarManager().add(startAction);

    // create interrupt Action
    interruptAction = new ToolbarAction(Constants.COMMAND_INTERRUPT, this);
    interruptAction.setText("Interrupt Task");
    interruptAction.setImageDescriptor(Activator.getImageDescriptor("resources/icons/interupt.png"));
    view.getViewSite().getActionBars().getToolBarManager().add(interruptAction);

    // create stop action
    stopAction = new ToolbarAction(Constants.COMMAND_STOP, this);
    stopAction.setText("Stop Task");
    stopAction.setImageDescriptor(Activator.getImageDescriptor("resources/icons/stop.png"));
    view.getViewSite().getActionBars().getToolBarManager().add(stopAction);

    // create complete task action
    completeTaskAction = new ToolbarAction(Constants.COMMAND_COMPLETE_TASK, this);
    completeTaskAction.setText("Complete Task");
    completeTaskAction.setImageDescriptor(Activator.getImageDescriptor("resources/icons/complete.png"));
    view.getViewSite().getActionBars().getToolBarManager().add(completeTaskAction);

    // create info action
    infoAction = new ToolbarAction(Constants.COMMAND_INFO, this);
    infoAction.setText("Show Info");
    infoAction.setImageDescriptor(Activator.getImageDescriptor("resources/icons/info.png"));
    view.getViewSite().getActionBars().getToolBarManager().add(infoAction);

    // filter menu
    filterAction = new ToolbarAction(Constants.COMMAND_FILTER, this);
    filterAction.setText("Filter");
    filterAction.setImageDescriptor(Activator.getImageDescriptor("resources/icons/filter.png"));
    view.getViewSite().getActionBars().getToolBarManager().add(filterAction);
  }

  private void markTaskComplete(Task task) {
    if (!task.isComplete()) {

      task.markAsComplete(LocalDate.now());
      Manager.getInstance().update(task);
      Calc.getInstance().updateCumulativeValues(this.project.getProjectName());
      Calc.getInstance().updateActualCumValue(this.project);

      // if task is fix defect get defect -> save remove phase -> update summary with remove
      // error
      if (task.getType().equals(Constants.TASK_TYPE_DEFECT_FIX)) {
        DefectRecord rec = Manager.getInstance().getDefectRecord(task.getProject().getProjectName(), task.getID());
        if (rec != null) {
          rec.setRemovePhase(task.getPhase());
          Manager.getInstance().update(rec);
          Calc.getInstance().updateRemoveValues(rec.getProject(), rec);
        }
      }

      refresh(this.taskToComplete);
    }
  }

  /**
   * @return the start interrupts
   */
  protected List<LocalDateTime> getStartInterrupt() {
    return this.startInterrupt;
  }

  /**
   * @return the end interrupts
   */
  protected List<LocalDateTime> getEndInterrupt() {
    return this.endInterrupt;
  }

}
