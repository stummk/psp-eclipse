package de.uni.bremen.stummk.psp.calculation;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.part.MultiPageEditorActionBarContributor;

import de.uni.bremen.stummk.psp.Activator;
import de.uni.bremen.stummk.psp.control.Editor;
import de.uni.bremen.stummk.psp.data.ProjectPlanSummary;
import de.uni.bremen.stummk.psp.utility.Constants;

/**
 * The controller class of the {@link EditorToolbarAction}
 * 
 * @author Konstantin
 *
 */
public class EditorToolbarController extends MultiPageEditorActionBarContributor {
  private IEditorPart activeEditor;
  private EditorToolbarAction diagramAction, syncAction;
  // , defInjAction, defRmdAction, timeAction, , timeTrackAction, valueTrackAction;
  private ProjectPlanSummary pps;

  /**
   * Constructor
   */
  public EditorToolbarController() {
    super();
    createActions();
  }

  /**
   * @return the {@link ProjectPlanSummary} of this Controller
   */
  public ProjectPlanSummary getProjectPlanSummary() {
    return this.pps;
  }

  @Override
  public void setActiveEditor(IEditorPart part) {
    if (this.activeEditor == part) {
      return;
    }
    this.activeEditor = part;

    // set new ProjectPlanSummary to the controller
    IActionBars actionBars = getActionBars();
    if (actionBars != null) {
      Editor editor = (part instanceof Editor) ? (Editor) part : null;
      if (editor != null) {
        this.pps = editor.getProjectSummaryForm().getProjectPlanSummary();
      }
    }
    super.setActiveEditor(part);
  }

  @Override
  public void setActivePage(IEditorPart activeEditor) {
    if (this.activeEditor == activeEditor) {
      return;
    }
    this.activeEditor = activeEditor;
  }

  @Override
  public void contributeToToolBar(IToolBarManager manager) {
    manager.add(new Separator());
    manager.add(syncAction);
    manager.add(diagramAction);
    // manager.add(timeAction);
    // manager.add(defInjAction);
    // manager.add(defRmdAction);
    // manager.add(timeTrackAction);
    // manager.add(valueTrackAction);
  }

  private void createActions() {
    syncAction = new EditorToolbarAction(Constants.COMMAND_SYNC, this);
    syncAction.setText("Export Data to psp.csv file");
    syncAction.setImageDescriptor(Activator.getImageDescriptor("resources/icons/sync.png"));

    // diagram menu
    diagramAction = new EditorToolbarAction("", this);
    diagramAction.setText("Show Diagrams for the project");
    diagramAction.setImageDescriptor(Activator.getImageDescriptor("resources/icons/diagram.png"));
    diagramAction.setAccelerator(IAction.AS_DROP_DOWN_MENU | IAction.AS_CHECK_BOX);
    diagramAction.setMenuCreator(new PulldownMenu(this));
    // view.getViewSite().getActionBars().getToolBarManager().add(sortAction);

    // diagramAction = new EditorToolbarAction(Constants.COMMAND_PLAN_ACTUAL_DIAGRAM, this);
    // diagramAction.setText("Show Plan vs. Actual Value Diagram");
    // diagramAction.setImageDescriptor(Activator.getImageDescriptor("resources/icons/diagram.png"));
    //
    // timeAction = new EditorToolbarAction(Constants.COMMAND_TIME_IN_PHASE_PERCENTAGE, this);
    // timeAction.setText("Show Distribution of time per phase");
    // timeAction.setImageDescriptor(Activator.getImageDescriptor("resources/icons/piechart.png"));
    //
    // defInjAction = new EditorToolbarAction(Constants.COMMAND_DEFECT_INJECTED_PERCENTAGE, this);
    // defInjAction.setText("Show Distribution of Injected Defects");
    // defInjAction.setImageDescriptor(Activator.getImageDescriptor("resources/icons/piechart.png"));
    //
    // defRmdAction = new EditorToolbarAction(Constants.COMMAND_DEFECT_REMOVED_PERCENTAGE, this);
    // defRmdAction.setText("Show Distribution of Removed Defects");
    // defRmdAction.setImageDescriptor(Activator.getImageDescriptor("resources/icons/piechart.png"));
    //
    // timeTrackAction = new EditorToolbarAction(Constants.COMMAND_TIME_TRACKING, this);
    // timeTrackAction.setText("Time Progress in Project");
    // timeTrackAction.setImageDescriptor(Activator.getImageDescriptor("resources/icons/linechart.png"));
    //
    // valueTrackAction = new EditorToolbarAction(Constants.COMMAND_EARNED_VALUE_TRACKING, this);
    // valueTrackAction.setText("Earned Value Tracking");
    // valueTrackAction.setImageDescriptor(Activator.getImageDescriptor("resources/icons/linechart.png"));
  }

}
