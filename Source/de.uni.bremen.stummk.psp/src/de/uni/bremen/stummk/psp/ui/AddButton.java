package de.uni.bremen.stummk.psp.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.FormToolkit;

import de.uni.bremen.stummk.psp.control.AddDefectDialog;
import de.uni.bremen.stummk.psp.control.AddPIPDialog;
import de.uni.bremen.stummk.psp.control.AddTaskDialog;
import de.uni.bremen.stummk.psp.control.AddTimeRecordDialog;
import de.uni.bremen.stummk.psp.control.TaskOverview;
import de.uni.bremen.stummk.psp.data.Phase;
import de.uni.bremen.stummk.psp.data.Project;
import de.uni.bremen.stummk.psp.utility.Constants;

/**
 * Represents the add button
 * 
 * @author Konstantin
 *
 */
public class AddButton {
  private Button btnAddNew;
  private Project projectID;
  private Composite composite;
  private Phase phase;


  /**
   * Constructor
   * 
   * @param parent the parent composite
   * @param toolkit the form toolkit
   * @param pageID the page id of the add button
   * @param projectID the project id of the page
   */
  public AddButton(Composite parent, FormToolkit toolkit, String pageID, Project projectID) {
    this.projectID = projectID;
    this.composite = parent;
    this.phase = null;
    this.btnAddNew = new Button(parent, SWT.NONE);
    toolkit.adapt(btnAddNew, true, true);
    btnAddNew.setText("Add New");

    this.btnAddNew.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent e) {
        if (pageID.equals(Constants.ID_TASK_OVERVIEW)) {
          TaskOverview view = (TaskOverview) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
              .findView(Constants.ID_TASK_OVERVIEW);
          if (view != null) {
            setPhase(view.getTable().getPhaseOfSelection());
          }
        }

        openAddDialog(pageID);
      }
    });
  }

  /**
   * Sets the phase of the task in the TaskOverview
   * 
   * @param phase the phase of the new phase
   */
  private void setPhase(Phase phase) {
    this.phase = phase;
  }

  private void openAddDialog(String pageID) {
    // opens add dialog depending on the page of the add button
    switch (pageID) {
      case Constants.ID_PIP_FORM:
        new AddPIPDialog(composite.getShell(), projectID).open();
        break;
      case Constants.ID_DEFECT_RECORD_FORM:
        new AddDefectDialog(composite.getShell(), projectID).open();
        break;
      case Constants.ID_TASK_PLANNING_FORM:
        new AddTaskDialog(composite.getShell(), projectID).open();
        break;
      case Constants.ID_TASK_OVERVIEW:
        if (phase != null) {
          new AddTaskDialog(composite.getShell(), projectID, null, phase).open();
        } else {
          new AddTaskDialog(composite.getShell(), projectID).open();
        }
        break;
      case Constants.ID_TIME_RECORD_FORM:
        new AddTimeRecordDialog(composite.getShell(), projectID).open();
        break;
    }
  }

  /**
   * Changes the project of the add button
   * 
   * @param project the project
   */
  public void changeProject(Project project) {
    this.projectID = project;
  }

  /**
   * Deactivate the button
   */
  public void deactivate() {
    btnAddNew.setEnabled(false);
  }

  /**
   * Activate the button
   */
  public void activate() {
    btnAddNew.setEnabled(true);
  }

}
