package de.uni.bremen.stummk.psp.control;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorReference;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

import de.uni.bremen.stummk.psp.data.DefectRecord;
import de.uni.bremen.stummk.psp.data.PersistenceItem;
import de.uni.bremen.stummk.psp.data.Phase;
import de.uni.bremen.stummk.psp.data.Project;
import de.uni.bremen.stummk.psp.data.Task;
import de.uni.bremen.stummk.psp.utility.Constants;

/**
 * This class describes the general structure of a dialog
 * 
 * @author Konstantin
 *
 */
public abstract class WindowDialog extends Dialog {

  private Project project;
  private PersistenceItem selection;
  private String[] components;
  private Label lblError;
  private TaskOverview view;
  private final int fixManualPosition = 4;
  private final int fixTimePosition = 5;

  /**
   * Constructor
   * 
   * @param parentShell the parent shell, where the dialog is called
   * @param project the project the dialog belongs to
   */
  public WindowDialog(Shell parentShell, Project project) {
    super(parentShell);
    setShellStyle(getShellStyle() | SWT.SHELL_TRIM);
    this.project = project;
  }

  /**
   * Constructor
   * 
   * @param parentShell the parent shell, where the dialog is called
   * @param project the project the dialog belongs to
   * @param selection the selected item in a table
   */
  public WindowDialog(Shell parentShell, Project projectID, PersistenceItem selection) {
    super(parentShell);
    setShellStyle(getShellStyle() | SWT.SHELL_TRIM);
    this.project = projectID;
    this.selection = selection;
  }

  @Override
  protected void createButtonsForButtonBar(Composite parent) {
    createButton(parent, IDialogConstants.FINISH_ID, IDialogConstants.FINISH_LABEL, true)
        .addSelectionListener(new SelectionAdapter() {

          @Override
          public void widgetSelected(SelectionEvent se) {
            check();
          }
        });
    createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
  }

  /**
   * Refreshes the {@link TaskOverview}, all {@link FormPage} of the {@link Editor}
   */
  protected void refresh() {
    Display.getDefault().asyncExec(new Runnable() {
      @Override
      public void run() {
        IWorkbenchWindow[] iw = PlatformUI.getWorkbench().getWorkbenchWindows();
        for (IWorkbenchWindow w : iw) {
          IWorkbenchPage[] ip = w.getPages();
          for (IWorkbenchPage p : ip) {
            IViewReference[] vp = p.getViewReferences();
            for (IViewReference r : vp) {
              if (r.getId().equals(Constants.ID_TASK_OVERVIEW)) {
                view = (TaskOverview) r.getView(true);
              }
            }
          }
        }
        if (view != null) {
          if (selection instanceof Task) {
            view.refresh((Task) selection);
          } else {
            view.refresh(null);
          }
        }
      }
    });

    IEditorReference[] editorReference =
        PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getEditorReferences();
    for (IEditorReference reference : editorReference) {
      if (reference.getEditor(false) instanceof Editor) {
        Editor edit = ((Editor) reference.getEditor(false));
        if (edit.getProject().getProjectName().equals(project.getProjectName())) {
          edit.refreshPages();
        }
      }
    }
  }

  /**
   * @return the selection
   */
  public PersistenceItem getSelection() {
    return this.selection;
  }

  /**
   * Sets the selection of this dialog
   * 
   * @param item the new selection, to be set
   */
  public void setSelection(PersistenceItem item) {
    this.selection = item;
  }

  /**
   * @return the project, the dialog belongs to
   */
  protected Project getProject() {
    return this.project;
  }

  /**
   * Sets the labels of components, which will be includeed in the dialog
   * 
   * @param componentLabel the labels of the components
   */
  protected void setComponents(String[] componentLabel) {
    this.components = componentLabel;
  }

  @Override
  protected Control createDialogArea(Composite parent) {
    Composite content = (Composite) super.createDialogArea(parent);
    content.setLayout(new FillLayout());

    ScrolledComposite sc = new ScrolledComposite(content, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);

    Composite container = new Composite(sc, SWT.NONE);
    {
      TableWrapLayout twl_container = new TableWrapLayout();
      twl_container.numColumns = 2;
      container.setLayout(twl_container);
    }

    // Error label
    Label lblNew = new Label(container, SWT.NONE);
    lblNew.setLayoutData(new TableWrapData(TableWrapData.CENTER, TableWrapData.MIDDLE, 1, 1));
    lblError = new Label(container, SWT.NONE);
    lblError.setLayoutData(new TableWrapData(TableWrapData.FILL, TableWrapData.TOP, 1, 1));
    lblError.setForeground(new Color(getShell().getDisplay(), 204, 0, 0));

    // creates labels
    for (int i = 0; i < components.length; ++i) {
      if (selection instanceof DefectRecord && !((DefectRecord) selection).isFixManual()) {
        if (i == fixManualPosition || i == fixTimePosition) {
          continue;
        }
      }

      Label lblNewLabel = new Label(container, SWT.NONE);
      lblNewLabel.setLayoutData(new TableWrapData(TableWrapData.CENTER, TableWrapData.MIDDLE, 1, 1));
      lblNewLabel.setText(components[i]);

      addComponent(container, i);
    }

    if (selection != null) {
      setData();
    }

    sc.setContent(container);
    sc.setExpandHorizontal(true);
    sc.setExpandVertical(true);
    sc.setMinSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));

    return parent;
  }

  /**
   * @return the error label
   */
  public Label getErrLbl() {
    return this.lblError;
  }

  /**
   * @return the index of the phase of the running task
   */
  public int getRunningPhase() {
    TaskOverview view = (TaskOverview) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
        .findView(Constants.ID_TASK_OVERVIEW);
    if (view != null && view.getToolbarController().taskIsRunning()) {
      return Phase.getIndexOfValue(Phase.getValue(view.getToolbarController().getRunningTask().getPhase()));
    }
    return 0;
  }

  /**
   * Sets the data to the components, when a existing selection will be updated
   */
  protected abstract void setData();

  /**
   * Adds the components to the dialog depending on the labels of the label set in
   * {@link #setComponents(String[])}
   * 
   * @param container the container coposite of the components
   * @param i the index of the component
   */
  protected abstract void addComponent(Composite container, int i);

  /**
   * Checks the entered data. and saves the {@link PersistenceItem}, or show error if not valid data
   * is entered
   */
  public abstract void check();



}
