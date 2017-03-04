package de.uni.bremen.stummk.psp.control;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.ui.forms.widgets.TableWrapData;

import de.uni.bremen.stummk.psp.calculation.Calc;
import de.uni.bremen.stummk.psp.calculation.Manager;
import de.uni.bremen.stummk.psp.data.Project;
import de.uni.bremen.stummk.psp.data.ProjectPlanSummary;
import de.uni.bremen.stummk.psp.utility.Constants;

/**
 * This class represents an dialog, with which a line of code number can be added
 *
 * @author Konstantin
 *
 */
public class AddLoCDialog extends WindowDialog {

  private Spinner spinner;
  private String[] labels = {"Add new LoC number:"};

  /**
   * Create the dialog.
   *
   * @param parentShell the parent shell, where the dialog is called
   * @param project the project id
   */
  public AddLoCDialog(Shell shell, Project project) {
    super(shell, project);
    super.setComponents(labels);
  }

  @Override
  protected void setData() {
    ProjectPlanSummary pps = Manager.getInstance().getProjectSummary(super.getProject().getProjectName());
    int oldLoc = (int) pps.get(Constants.KEY_PROGRAM_SIZE_IDX, Constants.KEY_SIZE_TOTAL_LOC, Constants.KEY_ACTUAL);
    spinner.setSelection(oldLoc);
  }

  @Override
  protected void configureShell(Shell newShell) {
    super.configureShell(newShell);
    newShell.setText("Add PIP Record to project : " + super.getProject().getProjectName());
  }

  @Override
  protected Point getInitialSize() {
    return new Point(450, 180);
  }

  @Override
  public void check() {
    long loc = spinner.getSelection();
    ProjectPlanSummary plan = Manager.getInstance().getProjectSummary(super.getProject().getProjectName());
    double oldLoc = plan.get(Constants.KEY_PROGRAM_SIZE_IDX, Constants.KEY_SIZE_TOTAL_LOC, Constants.KEY_ACTUAL);
    plan.put(Constants.KEY_PROGRAM_SIZE_IDX, Constants.KEY_SIZE_TOTAL_LOC, Constants.KEY_ACTUAL, loc);
    plan.updateValue(Constants.KEY_PROGRAM_SIZE_IDX, Constants.KEY_SIZE_TOTAL_LOC, Constants.KEY_TO_DATE, -oldLoc);
    plan.updateValue(Constants.KEY_PROGRAM_SIZE_IDX, Constants.KEY_SIZE_TOTAL_LOC, Constants.KEY_TO_DATE, loc);
    Calc.getInstance().updateSummary(plan, Constants.KEY_ACTUAL);
    Calc.getInstance().updateSummary(plan, Constants.KEY_TO_DATE);
    Manager.getInstance().update(plan);

    refresh();
    close();
  }

  @Override
  protected void addComponent(Composite container, int i) {
    switch (i) {
      case 0:
        spinner = new Spinner(container, SWT.BORDER);
        spinner.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
        spinner.setMaximum(Integer.MAX_VALUE);
        spinner.setTextLimit(10);
        break;
    }
  }
}
