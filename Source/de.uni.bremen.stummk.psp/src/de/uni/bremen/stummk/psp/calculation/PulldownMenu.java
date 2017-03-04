package de.uni.bremen.stummk.psp.calculation;

import org.eclipse.jface.action.ActionContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuCreator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.ui.IViewActionDelegate;
import org.eclipse.ui.IViewPart;

import de.uni.bremen.stummk.psp.Activator;
import de.uni.bremen.stummk.psp.utility.Constants;

/**
 * Class represents a pulldown menu for the diagrams in the editor toolbar
 * 
 * @author Konstantin
 *
 */
public class PulldownMenu implements IViewActionDelegate, IMenuCreator {
  private IAction action;
  private Menu menu;
  private EditorToolbarController controller;

  /**
   * Constructor
   * 
   * @param controller Toolbar controller of the pulldown menu
   */
  public PulldownMenu(EditorToolbarController controller) {
    this.controller = controller;
  }

  @Override
  public void run(IAction action) {}

  @Override
  public void selectionChanged(IAction action, ISelection selection) {
    if (action != this.action) {
      action.setMenuCreator(this);
      this.action = action;
    }
  }

  @Override
  public void dispose() {
    if (menu != null) {
      menu.dispose();
    }
  }

  @Override
  public Menu getMenu(Control parent) {
    Menu menu = new Menu(parent);

    // create actions
    EditorToolbarAction diagramAction = new EditorToolbarAction(Constants.COMMAND_PLAN_ACTUAL_DIAGRAM, controller);
    diagramAction.setText("Show Plan vs. Actual Value Diagram");
    diagramAction.setImageDescriptor(Activator.getImageDescriptor("resources/icons/diagram.png"));
    addActionToMenu(menu, diagramAction);

    EditorToolbarAction timeAction = new EditorToolbarAction(Constants.COMMAND_TIME_IN_PHASE_PERCENTAGE, controller);
    timeAction.setText("Show Distribution of time per phase");
    timeAction.setImageDescriptor(Activator.getImageDescriptor("resources/icons/piechart.png"));
    addActionToMenu(menu, timeAction);

    EditorToolbarAction defInjAction =
        new EditorToolbarAction(Constants.COMMAND_DEFECT_INJECTED_PERCENTAGE, controller);
    defInjAction.setText("Show Distribution of Injected Defects");
    defInjAction.setImageDescriptor(Activator.getImageDescriptor("resources/icons/piechart.png"));
    addActionToMenu(menu, defInjAction);

    EditorToolbarAction defRmdAction = new EditorToolbarAction(Constants.COMMAND_DEFECT_REMOVED_PERCENTAGE, controller);
    defRmdAction.setText("Show Distribution of Removed Defects");
    defRmdAction.setImageDescriptor(Activator.getImageDescriptor("resources/icons/piechart.png"));
    addActionToMenu(menu, defRmdAction);

    EditorToolbarAction timeTrackAction = new EditorToolbarAction(Constants.COMMAND_TIME_TRACKING, controller);
    timeTrackAction.setText("Time Progress in Project");
    timeTrackAction.setImageDescriptor(Activator.getImageDescriptor("resources/icons/linechart.png"));
    addActionToMenu(menu, timeTrackAction);

    EditorToolbarAction valueTrackAction = new EditorToolbarAction(Constants.COMMAND_EARNED_VALUE_TRACKING, controller);
    valueTrackAction.setText("Earned Value Tracking");
    valueTrackAction.setImageDescriptor(Activator.getImageDescriptor("resources/icons/linechart.png"));
    addActionToMenu(menu, valueTrackAction);

    return menu;
  }

  private void addActionToMenu(Menu menu, IAction action) {
    ActionContributionItem item = new ActionContributionItem(action);
    item.fill(menu, -1);
  }

  @Override
  public Menu getMenu(Menu parent) {
    return null;
  }

  @Override
  public void init(IViewPart view) {}

}
