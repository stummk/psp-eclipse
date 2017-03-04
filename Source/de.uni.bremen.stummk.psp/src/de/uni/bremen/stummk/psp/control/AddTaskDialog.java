package de.uni.bremen.stummk.psp.control;

import java.time.LocalDate;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.TableWrapData;

import de.uni.bremen.stummk.psp.calculation.Calc;
import de.uni.bremen.stummk.psp.calculation.Manager;
import de.uni.bremen.stummk.psp.data.Phase;
import de.uni.bremen.stummk.psp.data.Project;
import de.uni.bremen.stummk.psp.data.Task;
import de.uni.bremen.stummk.psp.utility.Constants;

/**
 * This class represents an dialog, with which a task can be added
 *
 * @author Konstantin
 *
 */
public class AddTaskDialog extends WindowDialog {

  private Text txtName;
  private Combo cPhase, cPriority;
  private Spinner sPlanTime, sPlanLoc, sPlanDefInjected, sPlanDefRemoved;
  private DateTime dtPlannedMonday;
  private String[] labels = {"Task Name", "Priority", "Phase", "Estimated Time (min.)", "Planned Finish Date",
      "Estimated LoC", "Estimated Defects Injected", "Estimated Defects Removed"};
  private Task selection;
  private Phase selectedPhase;

  /**
   * Create the dialog.
   *
   * @param parentShell the parent shell, where the dialog is called
   * @param project the project id
   */
  public AddTaskDialog(Shell parentShell, Project project) {
    super(parentShell, project);
    super.setComponents(labels);
  }

  /**
   * Create the dialog.
   *
   * @param shell the parent shell, where the dialog is called
   * @param projectID the project id
   * @param selection the selection of a table
   */
  public AddTaskDialog(Shell shell, Project projectID, Task selection) {
    super(shell, projectID, selection);
    super.setComponents(labels);
  }

  /**
   * Create the dialog.
   * 
   * @param shell the parent shell, where the dialog is called
   * @param project the project id
   * @param selection the selection of a table
   * @param phase the selected phase
   */
  public AddTaskDialog(Shell shell, Project project, Task selection, Phase phase) {
    super(shell, project, selection);
    super.setComponents(labels);
    this.selectedPhase = phase;
  }

  @Override
  protected void configureShell(Shell newShell) {
    super.configureShell(newShell);
    String text = " Task to project : " + super.getProject().getProjectName();
    if (super.getSelection() instanceof Task) {
      newShell.setText("Edit" + text);
    } else {
      newShell.setText("Add" + text);
    }
  }

  @Override
  protected void addComponent(Composite container, int num) {
    switch (num) {
      case 0:
        txtName = new Text(container, SWT.BORDER);
        txtName.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
        break;
      case 1:
        cPriority = new Combo(container, SWT.DROP_DOWN | SWT.READ_ONLY);
        cPriority.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
        cPriority.setItems("HIGH", "NORMAL", "LOW");
        cPriority.select(1);
        break;
      case 2:
        cPhase = new Combo(container, SWT.DROP_DOWN | SWT.READ_ONLY);
        cPhase.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
        cPhase.setItems(Phase.getValues());

        TaskOverview view = (TaskOverview) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
            .findView(Constants.ID_TASK_OVERVIEW);
        if (view != null && view.getToolbarController().taskIsRunning() && selectedPhase == null) {
          cPhase.select(super.getRunningPhase());
        } else {
          if (selectedPhase != null) {
            cPhase.select(Phase.getIndexOfValue(selectedPhase.toString()));
          } else {
            cPhase.select(0);
          }
        }
        break;
      case 3:
        sPlanTime = new Spinner(container, SWT.BORDER);
        sPlanTime.setLayoutData(new TableWrapData(TableWrapData.FILL, TableWrapData.TOP, 1, 1));
        sPlanTime.setMaximum(Integer.MAX_VALUE);
        sPlanTime.setTextLimit(6);
        break;
      case 4:
        dtPlannedMonday = new DateTime(container, SWT.DATE | SWT.DROP_DOWN);
        dtPlannedMonday.setLayoutData(new TableWrapData(TableWrapData.FILL, TableWrapData.TOP, 1, 1));
        break;
      case 5:
        sPlanLoc = new Spinner(container, SWT.BORDER);
        sPlanLoc.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
        sPlanLoc.setMaximum(Integer.MAX_VALUE);
        sPlanLoc.setTextLimit(6);
        break;
      case 6:
        sPlanDefInjected = new Spinner(container, SWT.BORDER);
        sPlanDefInjected.setLayoutData(new TableWrapData(TableWrapData.FILL, TableWrapData.TOP, 1, 1));
        sPlanDefInjected.setMaximum(Integer.MAX_VALUE);
        sPlanDefInjected.setTextLimit(6);
        break;
      case 7:
        sPlanDefRemoved = new Spinner(container, SWT.BORDER);
        sPlanDefRemoved.setLayoutData(new TableWrapData(TableWrapData.FILL, TableWrapData.TOP, 1, 1));
        sPlanDefRemoved.setMaximum(Integer.MAX_VALUE);
        sPlanDefRemoved.setTextLimit(6);
        break;
    }
  }

  @Override
  protected Point getInitialSize() {
    return new Point(450, 300);
  }

  @Override
  protected void setData() {
    selection = (Task) super.getSelection();
    txtName.setText(selection.getName());
    int c = Phase.getIndexOfValue(selection.getPhase().toString());
    if (c >= 0) {
      cPhase.select(c);
    }

    cPriority.select(selection.getPriority());
    sPlanTime.setSelection((int) (selection.getPlanTime()));
    dtPlannedMonday.setDate(selection.getPlannedMonday().getYear(), selection.getPlannedMonday().getMonthValue() - 1,
        selection.getPlannedMonday().getDayOfMonth());
    sPlanLoc.setSelection((int) selection.getPlanLoc());
    sPlanDefInjected.setSelection((int) selection.getPlanDefInjected());
    sPlanDefRemoved.setSelection((int) selection.getPlanDefRemoved());
  }

  @Override
  public void check() {
    if (checkValues()) {
      // add changes to db
      LocalDate date =
          LocalDate.of(dtPlannedMonday.getYear(), dtPlannedMonday.getMonth() + 1, dtPlannedMonday.getDay());
      if (selection != null) {
        Calc.getInstance().subSummaryValue(selection, super.getProject().getProjectName());
        Calc.getInstance().deletePlanHoursFromSchedule(super.getProject(), selection);
        selection.refreshItems(txtName.getText(), cPriority.getSelectionIndex(), Phase.fromString(cPhase.getText()),
            sPlanTime.getSelection(), date, sPlanLoc.getSelection(), sPlanDefInjected.getSelection(),
            sPlanDefRemoved.getSelection());
        super.setSelection(selection);
        Manager.getInstance().update(selection);
      } else {
        super.setSelection(
            new Task(txtName.getText(), cPriority.getSelectionIndex(), Phase.fromString(cPhase.getText()),
                sPlanTime.getSelection(), date, sPlanLoc.getSelection(), sPlanDefInjected.getSelection(),
                sPlanDefRemoved.getSelection(), super.getProject(), Constants.TASK_TYPE_TASK));
        Manager.getInstance().saveToDB(super.getSelection());
      }

      // update summary and schedule plan
      Calc.getInstance().updatePlannedValues(super.getProject().getProjectName());
      Calc.getInstance().addSummaryValues((Task) super.getSelection(), super.getProject().getProjectName());
      Calc.getInstance().createOrUpdateSchedule(super.getProject(), (Task) super.getSelection());
      refresh();
      close();
    }
  }


  private boolean checkValues() {
    super.getErrLbl().setText("");
    LocalDate date = LocalDate.of(dtPlannedMonday.getYear(), dtPlannedMonday.getMonth() + 1, dtPlannedMonday.getDay());

    // task anme is not set
    if (txtName.getText() == null || txtName.getText().isEmpty()) {
      super.getErrLbl().setText("Task name can not be empty");
      return false;
    }

    // no phase is selected
    if (cPhase.getSelectionIndex() == -1) {
      super.getErrLbl().setText("You must choose a phase!");
      return false;
    }

    // plan monday is before project start date
    if (date.isBefore(super.getProject().getTimestamp())) {
      super.getErrLbl().setText("Planned Finish Date cannot be before Project startdate");
      return false;
    }

    // task exists in database
    if (super.getSelection() == null
        && Manager.getInstance().taskExist(txtName.getText(), super.getProject().getProjectName())) {
      super.getErrLbl().setText("Task with Name already exists");
      return false;
    }

    return true;
  }

}
