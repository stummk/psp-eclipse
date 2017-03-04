package de.uni.bremen.stummk.psp.calculation;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import de.uni.bremen.stummk.psp.control.AddDefectDialog;
import de.uni.bremen.stummk.psp.control.AddPIPDialog;
import de.uni.bremen.stummk.psp.control.AddTaskDialog;
import de.uni.bremen.stummk.psp.control.AddTimeRecordDialog;
import de.uni.bremen.stummk.psp.data.DefectRecord;
import de.uni.bremen.stummk.psp.data.PIP;
import de.uni.bremen.stummk.psp.data.PersistenceItem;
import de.uni.bremen.stummk.psp.data.Phase;
import de.uni.bremen.stummk.psp.data.Project;
import de.uni.bremen.stummk.psp.data.Task;
import de.uni.bremen.stummk.psp.data.TimeRecord;
import de.uni.bremen.stummk.psp.utility.Constants;

/**
 * Action which adds a new record or edit an existing record of a formular
 * 
 * @author Konstantin
 *
 */
public class AddAction extends Action implements IWorkbenchAction {
  private Shell shell;
  private String pageID;
  private Project project;
  private PersistenceItem selection;
  private Phase phase = null;

  /**
   * Constructor
   * 
   * @param shell the shell which calls the action
   * @param pageID the id of the form page, which calls the action
   * @param project the project the calling page belongs to
   * @param selection the selection or null if nothing is selected or a new record should be added
   * @param commandID the comand {@link Constants.COMMAND_ADD} or
   *        {@link Constants.COMMAND_ADD_TIME_RECORD} if time record should be added to a task
   * @param text the text shown in the context menu
   * @param decriptor the image descriptor of this action
   */
  public AddAction(Shell shell, String pageID, Project project, PersistenceItem selection, String commandID,
      String text, ImageDescriptor decriptor) {
    super(text);
    this.shell = shell;
    this.pageID = pageID;
    this.project = project;
    this.selection = selection;
    setId(commandID);
    setImageDescriptor(decriptor);
  }

  @Override
  public void run() {
    // run action depedning on page calling this action
    if (getId().equals(Constants.COMMAND_ADD_TIME_RECORD) && selection instanceof Task) {
      new AddTimeRecordDialog(shell, project, (Task) selection).open();
    } else {
      switch (pageID) {
        case Constants.ID_PIP_FORM:
          new AddPIPDialog(shell, project, (PIP) selection).open();
          break;
        case Constants.ID_TIME_RECORD_FORM:
          new AddTimeRecordDialog(shell, project, (TimeRecord) selection).open();
          break;
        case Constants.ID_DEFECT_RECORD_FORM:
          new AddDefectDialog(shell, project, (DefectRecord) selection).open();
          break;
        case Constants.ID_TASK_PLANNING_FORM:
          new AddTaskDialog(shell, project, (Task) selection).open();
          break;
        case Constants.ID_TASK_OVERVIEW:
          if (phase != null) {
            new AddTaskDialog(shell, project, (Task) selection, phase).open();
          } else {
            new AddTaskDialog(shell, project, (Task) selection).open();
          }
          break;
      }
    }
  }

  @Override
  public void dispose() {}

  /**
   * Sets the phase of the selection in the TaskOverview to select Phase by default
   * 
   * @param phaseOfSelection the phase which should be selected
   */
  public void setPhase(Phase phaseOfSelection) {
    this.phase = phaseOfSelection;
  }

}
