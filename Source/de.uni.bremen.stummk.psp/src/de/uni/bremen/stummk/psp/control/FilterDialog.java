package de.uni.bremen.stummk.psp.control;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Shell;

import de.uni.bremen.stummk.psp.Activator;
import de.uni.bremen.stummk.psp.calculation.ToolbarController;
import de.uni.bremen.stummk.psp.ui.FilterDialogComposite;
import de.uni.bremen.stummk.psp.utility.Constants;

/**
 * Dialog, which shows the filter preferences
 * 
 * @author Konstantin
 *
 */
public class FilterDialog extends Dialog {
  FilterDialogComposite composite;
  ToolbarController controller;

  /**
   * Constructor
   * 
   * @param parentShell the shell
   * @param controller the toolbar controller calling the dialog
   */
  public FilterDialog(Shell parentShell, ToolbarController controller) {
    super(parentShell);
    setShellStyle(getShellStyle() | SWT.SHELL_TRIM);
    this.controller = controller;
  }

  @Override
  protected void configureShell(Shell newShell) {
    super.configureShell(newShell);
    newShell.setText("Choose filter settings");
  }

  @Override
  protected Control createDialogArea(Composite parent) {
    Composite content = (Composite) super.createDialogArea(parent);
    content.setLayout(new FillLayout());
    composite = new FilterDialogComposite(content);
    initializeFilter();
    return parent;
  }

  @Override
  protected void createButtonsForButtonBar(Composite parent) {
    createButton(parent, IDialogConstants.FINISH_ID, IDialogConstants.FINISH_LABEL, true)
        .addSelectionListener(new SelectionAdapter() {

          @Override
          public void widgetSelected(SelectionEvent se) {
            setFilter();
            controller.refresh(null);
            close();
          }
        });
    createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
  }

  @Override
  protected Point getInitialSize() {
    return new Point(500, 400);
  }

  public void initializeFilter() {
    // Initialize the preferences from the preference store
    IPreferenceStore store = Activator.getDefault().getPreferenceStore();
    composite.getBtnHideReady().setSelection(store.getBoolean(Constants.COMMAND_FILTER_READY));

    boolean allPhases = store.getBoolean(Constants.COMMAND_FILTER_ALL_PHASES);

    if (allPhases) {
      composite.getBtnShowAll().setSelection(allPhases);
      composite.getBtnShowAll().notifyListeners(SWT.Selection, new Event());
    } else {
      composite.getBtnShowAll().setSelection(allPhases);
      composite.getBtnShowPlanning().setSelection(store.getBoolean(Constants.COMMAND_FILTER_PLANNING));
      composite.getBtnShowDesign().setSelection(store.getBoolean(Constants.COMMAND_FILTER_DESIGN));
      composite.getBtnShowDesignReview().setSelection(store.getBoolean(Constants.COMMAND_FILTER_DESIGN_REVIEW));
      composite.getBtnShowCode().setSelection(store.getBoolean(Constants.COMMAND_FILTER_CODE));
      composite.getBtnShowCodeReview().setSelection(store.getBoolean(Constants.COMMAND_FILTER_CODE_REVIEW));
      composite.getBtnShowCompile().setSelection(store.getBoolean(Constants.COMMAND_FILTER_COMPILE));
      composite.getBtnShowTest().setSelection(store.getBoolean(Constants.COMMAND_FILTER_TEST));
      composite.getBtnShowPostmortem().setSelection(store.getBoolean(Constants.COMMAND_FILTER_POSTMORTEM));
    }

    composite.getBtnShowPriority().setSelection(store.getBoolean(Constants.COMMAND_FILTER_COLUMN_PRIORITY));
    composite.getBtnShowTime().setSelection(store.getBoolean(Constants.COMMAND_FILTER_COLUMN_TIME));
    composite.getBtnShowValue().setSelection(store.getBoolean(Constants.COMMAND_FILTER_COLUMN_VALUE));
    composite.getBtnShowStatus().setSelection(store.getBoolean(Constants.COMMAND_FILTER_COLUMN_STATUS));
    composite.getBtnShowDate().setSelection(store.getBoolean(Constants.COMMAND_FILTER_COLUMN_CHANGE_DATE));
  }

  private void setFilter() {
    // sets the filter
    IPreferenceStore store = Activator.getDefault().getPreferenceStore();
    store.setValue(Constants.COMMAND_FILTER_READY, composite.getBtnHideReady().getSelection());
    store.setValue(Constants.COMMAND_FILTER_ALL_PHASES, composite.getBtnShowAll().getSelection());
    store.setValue(Constants.COMMAND_FILTER_PLANNING, composite.getBtnShowPlanning().getSelection());
    store.setValue(Constants.COMMAND_FILTER_DESIGN, composite.getBtnShowDesign().getSelection());
    store.setValue(Constants.COMMAND_FILTER_DESIGN_REVIEW, composite.getBtnShowDesignReview().getSelection());
    store.setValue(Constants.COMMAND_FILTER_CODE, composite.getBtnShowCode().getSelection());
    store.setValue(Constants.COMMAND_FILTER_CODE_REVIEW, composite.getBtnShowCodeReview().getSelection());
    store.setValue(Constants.COMMAND_FILTER_COMPILE, composite.getBtnShowCompile().getSelection());
    store.setValue(Constants.COMMAND_FILTER_TEST, composite.getBtnShowTest().getSelection());
    store.setValue(Constants.COMMAND_FILTER_POSTMORTEM, composite.getBtnShowPostmortem().getSelection());
    store.setValue(Constants.COMMAND_FILTER_COLUMN_PRIORITY, composite.getBtnShowPriority().getSelection());
    store.setValue(Constants.COMMAND_FILTER_COLUMN_TIME, composite.getBtnShowTime().getSelection());
    store.setValue(Constants.COMMAND_FILTER_COLUMN_VALUE, composite.getBtnShowValue().getSelection());
    store.setValue(Constants.COMMAND_FILTER_COLUMN_STATUS, composite.getBtnShowStatus().getSelection());
    store.setValue(Constants.COMMAND_FILTER_COLUMN_CHANGE_DATE, composite.getBtnShowDate().getSelection());
  }

}
