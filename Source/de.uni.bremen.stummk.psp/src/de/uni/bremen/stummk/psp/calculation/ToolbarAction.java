package de.uni.bremen.stummk.psp.calculation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;

import de.uni.bremen.stummk.psp.control.FilterDialog;
import de.uni.bremen.stummk.psp.control.InfoDialog;
import de.uni.bremen.stummk.psp.utility.Constants;

/**
 * Class represents an action of the toolbar in the task overview
 * 
 * @author Konstantin
 *
 */
public class ToolbarAction extends Action implements IWorkbenchAction {

  private ToolbarController controller;

  /**
   * Constructor
   * 
   * @param id the unique action id
   * @param controller the {@link ToolbarController} of this class
   */
  public ToolbarAction(String id, ToolbarController controller) {
    setId(id);
    this.controller = controller;
  }

  /**
   * Constructor
   * 
   * @param text the text shown in a menu
   * @param id the unique action id
   * @param controller the {@link ToolbarController} of this class
   * @param descriptor the image descriptor of this action
   */
  public ToolbarAction(String text, String id, ToolbarController controller, ImageDescriptor descriptor) {
    super(text);
    setId(id);
    setImageDescriptor(descriptor);
    this.controller = controller;
  }

  @Override
  public void run() {
    handleAction(getId());
  }

  private void handleAction(String id) {
    // execute action depending on id
    Shell shell = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
    switch (id) {
      case Constants.COMMAND_START:
        this.controller.startTask();
        break;
      case Constants.COMMAND_INTERRUPT:
        this.controller.interruptTask();
        break;
      case Constants.COMMAND_STOP:
        this.controller.stopTask();
        break;
      case Constants.COMMAND_INFO:
        showDialog(shell);
        break;
      case Constants.COMMAND_COMPLETE_TASK:
        this.controller.completeTask();
        break;
      case Constants.COMMAND_FILTER:
        new FilterDialog(shell, controller).open();
        break;
    }
  }

  private void showDialog(Shell shell) {
    // info action is execute
    if (controller.getStatus() == Constants.STATUS_NO_TASK) {
      new InfoDialog(shell, "NO TASK IS RUNNING!").open();
    } else {
      List<LocalDateTime> start = controller.getStartInterrupt();
      List<LocalDateTime> end = new ArrayList<LocalDateTime>(controller.getEndInterrupt());
      if (end.size() == 0 || ((end.size() - start.size()) % 2) != 0) {
        end.add(LocalDateTime.now().withNano(0));
      }

      long deltaInt = 0;
      if (start.size() > 0) {
        deltaInt = MathCalc.getInstance().calcSeconds(start, end);
      }

      new InfoDialog(shell, controller.getProject().getProjectName(), controller.getRunningTask().getName(),
          "" + controller.getRunningTask().getPlanTime(),
          (MathCalc.getInstance().getSeconds(LocalDate.now(), LocalTime.now(), controller.getTimeRecord().getDate(),
              controller.getTimeRecord().getStarttime()) + controller.getRunningTask().getActualTime()) - deltaInt,
          controller).open();
    }
  }

  @Override
  public void dispose() {}

}
