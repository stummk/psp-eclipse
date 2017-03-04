package de.uni.bremen.stummk.psp.control;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.ContainerSelectionDialog;

import de.uni.bremen.stummk.psp.ui.PSPNewWizardComposite;
import de.uni.bremen.stummk.psp.utility.CheckOperation;

/**
 * Represents the wizard page of the creating psp file wizard
 * 
 * @author Konstantin
 *
 */
public class PSPNewWizardPage extends WizardPage {

  private ISelection selection;
  private PSPNewWizardComposite container;

  /**
   * Constructor
   * 
   * @param selection the project which is selected
   */
  public PSPNewWizardPage(ISelection selection) {
    super("wizardPage");
    setTitle("Create PSP");
    setDescription("This wizard creates an new psp file.");
    if (selection != null) {
      this.selection = selection;
    } else {
      IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
      if (window != null) {
        this.selection = (IStructuredSelection) window.getSelectionService().getSelection();
      }
    }
  }

  @Override
  public void createControl(Composite parent) {
    this.container = new PSPNewWizardComposite(parent, SWT.NULL);

    // browse button function
    container.getBrowseButton().addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        handleBrowse();
      }
    });

    // text field listens to changes
    container.getTextField().addModifyListener(new ModifyListener() {
      public void modifyText(ModifyEvent e) {
        dialogChanged();
      }
    });

    IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
    for (IProject project : projects) {
      if (CheckOperation.projectContainFile(project)) {
        container.getMultiSelection().add(project.getName());
      }
    }

    init();
    dialogChanged();
    setControl(container);
  }

  private void handleBrowse() {
    // open project to select one Project
    IWorkspaceRoot workspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
    ContainerSelectionDialog dialog =
        new ContainerSelectionDialog(getShell(), workspaceRoot, false, "Select one project");

    if (dialog.open() == ContainerSelectionDialog.OK) {
      Object[] result = dialog.getResult();
      if (result.length == 1) {
        IPath path = (IPath) result[0];
        container.getTextField().setText(workspaceRoot.findMember(path, false).getProject().getName());
      }
    }
  }

  private void init() {
    // Check if one project is selected and set the name into the project-textfield
    if (!selection.isEmpty() && selection instanceof IStructuredSelection) {
      IProject project = CheckOperation.getProjectBySelection(selection);
      if (project != null) {
        container.getTextField().setText(project.getName());
      }
    }
  }

  private void dialogChanged() {
    // checks text in textfield if changed
    String containerText = container.getTextField().getText();
    IResource resource = ResourcesPlugin.getWorkspace().getRoot().findMember(new Path(containerText));

    if (containerText.isEmpty()) {
      // textfield is empty
      updateStatus("Choose a project");
      return;
    }

    if (resource == null || (resource.getType() & (IResource.PROJECT)) == 0) {
      // project does not exist
      updateStatus("Project does not exists");
      return;
    }

    if (!resource.isAccessible()) {
      // project is not writeable
      updateStatus("Project must be writable");
      return;
    }

    if (CheckOperation.projectContainFile(resource)) {
      // project contains a psp file
      updateStatus("Project already has a PSP file");
      return;
    }
    updateStatus(null);
  }

  private void updateStatus(String message) {
    setErrorMessage(message);
    setPageComplete(message == null);
  }

  /**
   * @return the project id entered in the textfield
   */
  public String getProjectName() {
    return container.getTextField().getText();
  }

  /**
   * @return the selected Projects, which data will be used to calculate the to date values
   */
  public String[] getSelectedProjects() {
    return container.getMultiSelection().getSelection();
  }
}
