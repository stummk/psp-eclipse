package de.uni.bremen.stummk.psp.control;

import java.time.LocalDate;
import java.time.LocalTime;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.TableWrapData;

import de.uni.bremen.stummk.psp.calculation.Calc;
import de.uni.bremen.stummk.psp.calculation.Manager;
import de.uni.bremen.stummk.psp.calculation.MathCalc;
import de.uni.bremen.stummk.psp.data.DefectRecord;
import de.uni.bremen.stummk.psp.data.Phase;
import de.uni.bremen.stummk.psp.data.Project;
import de.uni.bremen.stummk.psp.data.Task;
import de.uni.bremen.stummk.psp.data.TimeRecord;
import de.uni.bremen.stummk.psp.utility.Constants;

/**
 * This class represents an dialog, with which a time record can be added
 *
 * @author Konstantin
 *
 */
public class AddTimeRecordDialog extends WindowDialog {

  private Text txtComment;
  private DateTime dtDate, dtEnddate, dtStarttime, dtStoptime;
  private Spinner sIntMin;
  private Combo cPhase;
  private String[] labels = {"Date", "Enddate", "Starttime", "Stoptime", "Interupt Min.", "Phase", "Comment"};
  private TimeRecord selection;
  private Task task;

  /**
   * Create the dialog.
   *
   * @param parentShell the parent shell, where the dialog is called
   * @param project the project
   */
  public AddTimeRecordDialog(Shell parentShell, Project project) {
    super(parentShell, project);
    super.setComponents(labels);
  }

  /**
   * Create the dialog.
   *
   * @param parentShell the parent shell, where the dialog is called
   * @param project the project
   */
  public AddTimeRecordDialog(Shell parentShell, Project project, Task task) {
    super(parentShell, project);
    super.setComponents(labels);
    this.task = task;
  }

  /**
   * Constructor
   * 
   * @param shell the parent shell calling the dialog
   * @param projectID the project
   * @param selection the selected {@link TimeRecord}
   */
  public AddTimeRecordDialog(Shell shell, Project projectID, TimeRecord selection) {
    super(shell, projectID, selection);
    super.setComponents(labels);
  }

  @Override
  protected void addComponent(Composite container, int num) {
    switch (num) {
      case 0:
        dtDate = new DateTime(container, SWT.DATE | SWT.DROP_DOWN);
        dtDate.setLayoutData(new TableWrapData(TableWrapData.FILL, TableWrapData.TOP, 1, 1));
        break;
      case 1:
        dtEnddate = new DateTime(container, SWT.DATE | SWT.DROP_DOWN);
        dtEnddate.setLayoutData(new TableWrapData(TableWrapData.FILL, TableWrapData.TOP, 1, 1));
        break;
      case 2:
        dtStarttime = new DateTime(container, SWT.TIME | SWT.DROP_DOWN);
        dtStarttime.setLayoutData(new TableWrapData(TableWrapData.FILL, TableWrapData.TOP, 1, 1));
        break;
      case 3:
        dtStoptime = new DateTime(container, SWT.TIME | SWT.DROP_DOWN);
        dtStoptime.setLayoutData(new TableWrapData(TableWrapData.FILL, TableWrapData.TOP, 1, 1));
        break;
      case 4:
        sIntMin = new Spinner(container, SWT.BORDER);
        sIntMin.setLayoutData(new TableWrapData(TableWrapData.FILL, TableWrapData.TOP, 1, 1));
        sIntMin.setMaximum(Integer.MAX_VALUE);
        sIntMin.setTextLimit(6);
        break;
      case 5:
        cPhase = new Combo(container, SWT.DROP_DOWN | SWT.READ_ONLY);
        cPhase.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
        cPhase.setItems(Phase.getValues());
        if (task != null) {
          cPhase.select(Phase.getIndexOfValue(task.getPhase().toString()));
        } else {
          cPhase.select(super.getRunningPhase());
        }
        break;
      case 6:
        txtComment = new Text(container, SWT.BORDER);
        txtComment.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
        break;
    }
  }

  @Override
  protected void configureShell(Shell newShell) {
    super.configureShell(newShell);
    String text = " Time Record to project : " + super.getProject().getProjectName();
    if (super.getSelection() instanceof TimeRecord) {
      newShell.setText("Edit" + text);
    } else {
      newShell.setText("Add" + text);
    }
  }

  @Override
  protected Point getInitialSize() {
    return new Point(450, 300);
  }

  @Override
  protected void setData() {
    selection = (TimeRecord) super.getSelection();
    dtDate.setDate(selection.getDate().getYear(), selection.getDate().getMonth().getValue() - 1,
        selection.getDate().getDayOfMonth());
    dtEnddate.setDate(selection.getEndDate().getYear(), selection.getEndDate().getMonth().getValue() - 1,
        selection.getEndDate().getDayOfMonth());
    dtStarttime.setTime(selection.getStarttime().getHour(), selection.getStarttime().getMinute(),
        selection.getStarttime().getSecond());
    dtStoptime.setTime(selection.getEndtime().getHour(), selection.getEndtime().getMinute(),
        selection.getEndtime().getSecond());
    sIntMin.setSelection((int) MathCalc.getInstance().fromSecondToMinute(selection.getInterruptTime()));

    int c = Phase.getIndexOfValue(selection.getPhase().toString());
    if (c >= 0) {
      cPhase.select(c);
    }

    txtComment.setText(selection.getComment());
  }

  @Override
  public void check() {
    if (checkValues()) {
      // save to db
      LocalDate date = LocalDate.of(dtDate.getYear(), dtDate.getMonth() + 1, dtDate.getDay());
      LocalDate enddate = LocalDate.of(dtEnddate.getYear(), dtEnddate.getMonth() + 1, dtEnddate.getDay());
      LocalTime starttime = LocalTime.of(dtStarttime.getHours(), dtStarttime.getMinutes(), dtStarttime.getSeconds(), 0);
      LocalTime endtime = LocalTime.of(dtStoptime.getHours(), dtStoptime.getMinutes(), dtStoptime.getSeconds(), 0);
      if (selection != null) {
        Calc.getInstance().subSummaryValue(selection, super.getProject().getProjectName());
        Calc.getInstance().deleteActualHoursFromSchedule(super.getProject(), selection);
        updateTimeFromTaskAndDefect((TimeRecord) selection, -1);
        selection.refreshItems(date, enddate, starttime, endtime, sIntMin.getSelection(),
            Phase.fromString(cPhase.getText()), txtComment.getText());
        super.setSelection(selection);
        Manager.getInstance().update(selection);
      } else {
        super.setSelection(new TimeRecord(date, enddate, starttime, endtime, sIntMin.getSelection(),
            Phase.fromString(cPhase.getText()), txtComment.getText(), super.getProject()));
        ((TimeRecord) super.getSelection()).setTask(task);
        Manager.getInstance().saveToDB(super.getSelection());
      }

      // update the summary and schedule plan
      updateTimeFromTaskAndDefect((TimeRecord) getSelection(), 1);
      Calc.getInstance().addSummaryValue((TimeRecord) super.getSelection(), super.getProject().getProjectName());
      Calc.getInstance().updateScheduleActualHour(super.getProject(), (TimeRecord) super.getSelection());
      refresh();
      close();
    }
  }

  private void updateTimeFromTaskAndDefect(TimeRecord selection, long value) {
    Task task = selection.getTask();
    if (task != null) {
      task.update((value * selection.getDeltaTime()));
      Manager.getInstance().update(task);

      if (task.getType().equals(Constants.TASK_TYPE_DEFECT_FIX)) {
        DefectRecord rec = Manager.getInstance().getDefectRecord(selection.getProject().getProjectName(), task.getID());
        if (rec != null) {
          rec.updateFixTime((value * MathCalc.getInstance().fromSecondToMinute(selection.getDeltaTime())));
          Manager.getInstance().update(rec);
        }
      }
    }
  }

  private boolean checkValues() {
    super.getErrLbl().setText("");
    LocalDate date = LocalDate.of(dtDate.getYear(), dtDate.getMonth() + 1, dtDate.getDay());
    LocalDate enddate = LocalDate.of(dtEnddate.getYear(), dtEnddate.getMonth() + 1, dtEnddate.getDay());
    LocalTime starttime = LocalTime.of(dtStarttime.getHours(), dtStarttime.getMinutes(), dtStarttime.getSeconds(), 0);
    LocalTime endtime = LocalTime.of(dtStoptime.getHours(), dtStoptime.getMinutes(), dtStoptime.getSeconds(), 0);

    // start date is before project start date
    if (date.isBefore(super.getProject().getTimestamp())) {
      super.getErrLbl().setText("Startdate can not be before Project start date");
      return false;
    }

    // start date is after end date
    if (date.isEqual(super.getProject().getTimestamp()) && date.isAfter(enddate)) {
      super.getErrLbl().setText("Enddate can not be before startdate");
      return false;
    }

    // start time is after end time
    if (date.isEqual(enddate) && starttime.isAfter(endtime)) {
      super.getErrLbl().setText("Starttime must be before Stoptime");
      return false;
    }

    // no phase is selected
    if (cPhase.getSelectionIndex() == -1) {
      super.getErrLbl().setText("You must choose a phase!");
      return false;
    }

    // interruption time is greater than work time
    if (MathCalc.getInstance().getMin(enddate, endtime, date, starttime) < sIntMin.getSelection()) {
      super.getErrLbl().setText("Interrupt Time can not be greater than worked time");
      return false;
    }

    // Time record overlaps with existing
    TimeRecord tr = (TimeRecord) super.getSelection();
    boolean overlapps;
    if (tr != null) {
      overlapps = Manager.getInstance().timeRecordOverlapps(tr.getID(), date, starttime, enddate, endtime,
          super.getProject().getProjectName());
    } else {
      overlapps = Manager.getInstance().timeRecordOverlapps(-1, date, starttime, enddate, endtime,
          super.getProject().getProjectName());
    }

    if (overlapps) {
      super.getErrLbl().setText("Time Record overlapps with existing time record!");
      return false;
    }


    return true;
  }

}
