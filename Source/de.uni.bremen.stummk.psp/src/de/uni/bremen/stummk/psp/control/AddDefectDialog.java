package de.uni.bremen.stummk.psp.control;

import java.time.LocalDate;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.TableWrapData;

import de.uni.bremen.stummk.psp.calculation.Calc;
import de.uni.bremen.stummk.psp.calculation.Manager;
import de.uni.bremen.stummk.psp.data.DefectRecord;
import de.uni.bremen.stummk.psp.data.DefectType;
import de.uni.bremen.stummk.psp.data.Phase;
import de.uni.bremen.stummk.psp.data.Project;
import de.uni.bremen.stummk.psp.data.Task;
import de.uni.bremen.stummk.psp.utility.Constants;

/**
 * This class represents an dialog, with which a defect record can be added
 * 
 * @author Konstantin
 */
public class AddDefectDialog extends WindowDialog {
  private Text txtdescription;
  private Combo cType, cInject, cRemove;
  private Spinner sFixTime;
  private Button btnFixManual;
  private DateTime dateTime;
  private String[] labels =
      {"Date", "Defect Type", "Inject Phase", "Remove Phase", "Enter Time Manual", "Fix Time (min.)", "Description"};
  private DefectRecord selection;
  private String selectionPath = "";

  /**
   * Create the dialog.
   *
   * @param parentShell the parent shell, where the dialog is called
   * @param project the project
   */
  public AddDefectDialog(Shell parentShell, Project project) {
    super(parentShell, project);
    super.setComponents(labels);
  }

  /**
   * Constructor
   * 
   * @param shell the parent shell calling the dialog
   * @param projectID the project id
   * @param selection the selected {@link DefectRecord}
   */
  public AddDefectDialog(Shell shell, Project projectID, DefectRecord selection) {
    super(shell, projectID, selection);
    super.setComponents(labels);
  }

  /**
   * Constructor
   * 
   * @param shell the parent shell calling the dialog
   * @param project the project id
   * @param selectionPath the project relative path of the selected resource
   */
  public AddDefectDialog(Shell shell, Project project, String selectionPath) {
    super(shell, project);
    super.setComponents(labels);
    this.selectionPath = selectionPath;
  }

  @Override
  protected void configureShell(Shell newShell) {
    super.configureShell(newShell);
    String text = " Defect Record to project : " + super.getProject().getProjectName();
    if (super.getSelection() instanceof DefectRecord) {
      newShell.setText("Edit" + text);
    } else {
      newShell.setText("Add" + text);
    }
  }

  @Override
  protected void addComponent(Composite container, int num) {
    // add components into the parent component
    switch (num) {
      case 0:
        dateTime = new DateTime(container, SWT.DATE | SWT.DROP_DOWN);
        dateTime.setLayoutData(new TableWrapData(TableWrapData.FILL, TableWrapData.TOP, 1, 1));
        break;
      case 1:
        cType = new Combo(container, SWT.DROP_DOWN | SWT.READ_ONLY);
        cType.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
        cType.setItems(DefectType.getValues());
        break;
      case 2:
        cInject = new Combo(container, SWT.DROP_DOWN | SWT.READ_ONLY);
        cInject.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
        cInject.setItems(Phase.getValues());
        cInject.select(super.getRunningPhase());
        break;
      case 3:
        cRemove = new Combo(container, SWT.DROP_DOWN | SWT.READ_ONLY);
        cRemove.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
        String removePhases[] = new String[(Phase.getValues().length + 1)];
        removePhases[0] = "Not Removed Yet";
        for (int i = 0; i < Phase.getValues().length; ++i) {
          removePhases[i + 1] = Phase.getValues()[i];
        }
        cRemove.setItems(removePhases);
        break;
      case 4:
        if (selection instanceof DefectRecord && !((DefectRecord) selection).isFixManual()) {
          break;
        }
        btnFixManual = new Button(container, SWT.CHECK);
        btnFixManual.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
        btnFixManual.addSelectionListener(new SelectionAdapter() {
          @Override
          public void widgetSelected(SelectionEvent e) {
            if (btnFixManual.getSelection()) {
              sFixTime.setEnabled(true);
            } else {
              sFixTime.setSelection(0);
              sFixTime.setEnabled(false);
            }
            super.widgetSelected(e);
          }
        });
        break;
      case 5:
        if (selection instanceof DefectRecord && !((DefectRecord) selection).isFixManual()) {
          break;
        }
        sFixTime = new Spinner(container, SWT.BORDER);
        sFixTime.setLayoutData(new TableWrapData(TableWrapData.FILL, TableWrapData.TOP, 1, 1));
        sFixTime.setMaximum(Integer.MAX_VALUE);
        sFixTime.setTextLimit(6);
        sFixTime.setEnabled(false);
        break;
      case 6:
        txtdescription = new Text(container, SWT.BORDER);
        txtdescription.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
        break;
    }
  }

  @Override
  protected Point getInitialSize() {
    return new Point(450, 300);
  }

  @Override
  protected void setData() {
    selection = (DefectRecord) super.getSelection();
    dateTime.setDate(selection.getDate().getYear(), selection.getDate().getMonth().getValue() - 1,
        selection.getDate().getDayOfMonth());

    int ct = DefectType.getIndexOfValue(selection.getType().toString());
    if (ct >= 0) {
      cType.select(ct);
    }

    int cj = Phase.getIndexOfValue(selection.getInjectPhase().toString());
    if (cj >= 0) {
      cInject.select(cj);;
    }

    if (selection.getRemovePhase() != null) {
      int cr = Phase.getIndexOfValue(selection.getRemovePhase().toString()) + 1;
      if (cr >= 0) {
        cRemove.select(cr);
      }
    } else {
      cRemove.select(0);
    }

    if (selection.isFixManual()) {
      btnFixManual.setSelection(true);
      btnFixManual.setEnabled(false);
      sFixTime.setSelection((int) selection.getFixTime());
      sFixTime.setEnabled(true);
    }

    txtdescription.setText(selection.getDescription());
  }

  @Override
  public void check() {
    if (checkValues()) {
      // save to db or update
      LocalDate date = LocalDate.of(dateTime.getYear(), dateTime.getMonth() + 1, dateTime.getDay());
      if (selection != null) {
        Calc.getInstance().subSummaryValue(selection, super.getProject().getProjectName());
        long fixTime = sFixTime != null ? sFixTime.getSelection() : selection.getFixTime();
        selection.refreshItems(date, DefectType.fromString(cType.getText()), Phase.fromString(cInject.getText()),
            Phase.fromString(cRemove.getText()), fixTime, txtdescription.getText());
        super.setSelection(selection);
        Manager.getInstance().update(selection);
      } else {
        super.setSelection(new DefectRecord(date, DefectType.fromString(cType.getText()),
            Phase.fromString(cInject.getText()), Phase.fromString(cRemove.getText()), sFixTime.getSelection(),
            txtdescription.getText(), super.getProject(), btnFixManual.getSelection(), selectionPath));

        if (!((DefectRecord) getSelection()).isFixManual()) {
          Task task = addTask();
          ((DefectRecord) getSelection()).setTask(task);
        }

        Manager.getInstance().saveToDB(super.getSelection());

        // save id of defect record to name of linked task
        if (((DefectRecord) super.getSelection()).getTask() != null) {
          DefectRecord rec = Manager.getInstance().getDefectRecord(super.getProject().getProjectName(),
              ((DefectRecord) super.getSelection()).getTask().getID());
          Task task = rec.getTask();
          task.setName("Fix Defect Nr. " + rec.getID());
        }
      }

      Calc.getInstance().addSummaryValue((DefectRecord) super.getSelection(), super.getProject().getProjectName());
      super.refresh();
      close();
    }
  }

  private Task addTask() {
    // adds task to the defect record
    Phase phase = cRemove.getSelectionIndex() == -1 || cRemove.getSelectionIndex() == 0
        ? Phase.fromString(cInject.getText()) : Phase.fromString(cRemove.getText());
    Task task = new Task("Fix Defect Nr. ", 1, phase, 0, ((DefectRecord) getSelection()).getDate(), 0, 0, 0,
        super.getProject(), Constants.TASK_TYPE_DEFECT_FIX);
    Manager.getInstance().saveToDB(task);
    Calc.getInstance().updatePlannedValues(super.getProject().getProjectName());
    Calc.getInstance().addSummaryValues(task, super.getProject().getProjectName());
    Calc.getInstance().createOrUpdateSchedule(super.getProject(), task);
    return task;
  }

  private boolean checkValues() {
    super.getErrLbl().setText("");
    LocalDate date = LocalDate.of(dateTime.getYear(), dateTime.getMonth() + 1, dateTime.getDay());
    // date before project start date
    if (date.isBefore(super.getProject().getTimestamp())) {
      super.getErrLbl().setText("Date can not be before Project start date");
      return false;
    }

    // no type or inject phase has been selected
    if (cType.getSelectionIndex() == -1 || cInject.getSelectionIndex() == -1) {
      super.getErrLbl().setText("You must choose a type and an inject phase!");
      return false;
    }

    // if is manual and no remove phase selected
    if (btnFixManual != null && btnFixManual.isVisible() && btnFixManual.getSelection()
        && (cRemove.getSelectionIndex() == -1 || cRemove.getSelectionIndex() == 0)) {
      super.getErrLbl().setText("You must choose a remove phase!");
      return false;
    }

    return true;
  }

}
