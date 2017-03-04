package de.uni.bremen.stummk.psp.utility;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.IWizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.wizards.IWizardDescriptor;

import de.uni.bremen.stummk.psp.calculation.Manager;
import de.uni.bremen.stummk.psp.control.AddDefectDialog;
import de.uni.bremen.stummk.psp.control.AddLoCDialog;
import de.uni.bremen.stummk.psp.control.AddPIPDialog;
import de.uni.bremen.stummk.psp.control.AddTaskDialog;
import de.uni.bremen.stummk.psp.control.AddTimeRecordDialog;
import de.uni.bremen.stummk.psp.data.Project;

/**
 * Handler,which opens an Add dialog depending on the command,which is executed
 * 
 * @author Konstantin
 *
 */
public class OpenDialogHandler extends AbstractHandler {
  private String selectionPath = "";

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    if (event.getCommand().getId().equals(Constants.COMMAND_CONVERT)) {
      openWizard(Constants.WIZARD_ID);
    } else {

      Shell shell = HandlerUtil.getActiveShell(event);
      ISelection selection = HandlerUtil.getActiveMenuSelection(event);

      IProject project = CheckOperation.getProjectBySelection(selection);

      if (project != null && CheckOperation.projectContainFile(project)) {
        if (selection instanceof IStructuredSelection) {
          Object firstElement = ((IStructuredSelection) selection).getFirstElement();
          IResource res = null;

          if (firstElement instanceof IAdaptable) {
            res = (IResource) (((IAdaptable) firstElement).getAdapter(IResource.class));
          } else if (firstElement instanceof IResource) {
            res = (IResource) firstElement;
          }

          if (res != null) {
            selectionPath = res.getProjectRelativePath().toString();
          }
        }

        Project p = Manager.getInstance().getProjectByName(project.getName());
        openDialog(shell, event.getCommand().getId(), p);
      }
    }

    return null;
  }

  private void openDialog(Shell shell, String id, Project project) {
    switch (id) {
      case Constants.COMMAND_ADD_TASK:
        new AddTaskDialog(shell, project).open();
        break;
      case Constants.COMMAND_ADD_TIME_RECORD:
        new AddTimeRecordDialog(shell, project).open();
        break;
      case Constants.COMMAND_ADD_DEFECT_RECORD:
        new AddDefectDialog(shell, project, selectionPath).open();
        break;
      case Constants.COMMAND_ADD_PIP:
        new AddPIPDialog(shell, project).open();
        break;
      case Constants.COMMAND_ADD_LOC:
        new AddLoCDialog(shell, project).open();
        break;
    }
  }

  private void openWizard(String id) {
    // First see if this is a "new wizard".
    IWizardDescriptor descriptor = PlatformUI.getWorkbench().getNewWizardRegistry().findWizard(id);
    // If not check if it is an "import wizard".
    if (descriptor == null) {
      descriptor = PlatformUI.getWorkbench().getImportWizardRegistry().findWizard(id);
    }
    // Or maybe an export wizard
    if (descriptor == null) {
      descriptor = PlatformUI.getWorkbench().getExportWizardRegistry().findWizard(id);
    }
    try {
      // Then if we have a wizard, open it.
      if (descriptor != null) {
        IWizard wizard = descriptor.createWizard();

        WizardDialog wd = new WizardDialog(Display.getDefault().getActiveShell(), wizard);
        wd.setTitle(wizard.getWindowTitle());
        wd.open();
      }
    } catch (CoreException e) {
      e.printStackTrace();
    }
  }



}
