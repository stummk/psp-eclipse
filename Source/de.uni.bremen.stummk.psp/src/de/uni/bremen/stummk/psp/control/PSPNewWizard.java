package de.uni.bremen.stummk.psp.control;

import java.lang.reflect.InvocationTargetException;
import java.time.LocalDate;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.WorkbenchException;
import org.eclipse.ui.ide.IDE;

import de.uni.bremen.stummk.psp.Activator;
import de.uni.bremen.stummk.psp.calculation.Manager;
import de.uni.bremen.stummk.psp.data.PSPProject;
import de.uni.bremen.stummk.psp.data.Project;
import de.uni.bremen.stummk.psp.data.ProjectPlanSummary;
import de.uni.bremen.stummk.psp.utility.Constants;
import de.uni.bremen.stummk.psp.utility.DataIO;

/**
 * Represents the wizard to create a new psp file in a project
 * 
 * @author Konstantin
 *
 */
public class PSPNewWizard extends Wizard implements INewWizard {

  private PSPNewWizardPage page;
  private ISelection selection;

  /**
   * Constructor
   */
  public PSPNewWizard() {
    super();
    setNeedsProgressMonitor(true);
  }

  @Override
  public void init(IWorkbench workbench, IStructuredSelection selection) {
    this.selection = selection;
  }

  @Override
  public void addPages() {
    page = new PSPNewWizardPage(selection);
    addPage(page);
  }

  @Override
  public boolean performFinish() {
    String projectName = page.getProjectName();
    String[] selectedProjects = page.getSelectedProjects();

    // create psp file when finish button is presssed
    IRunnableWithProgress op = new IRunnableWithProgress() {
      public void run(IProgressMonitor monitor) throws InvocationTargetException {
        try {
          doFinish(projectName, selectedProjects, monitor);
        } catch (CoreException e) {
          throw new InvocationTargetException(e);
        } finally {
          monitor.done();
        }
      }
    };

    try {
      getContainer().run(true, false, op);
    } catch (InterruptedException e) {
      return false;
    } catch (InvocationTargetException e) {
      e.printStackTrace();
      Throwable realException = e.getTargetException();
      MessageDialog.openError(getShell(), "Error", realException.getMessage());
      return false;
    }
    return true;
  }

  private void doFinish(String projectName, String[] selectedProjects, IProgressMonitor monitor) throws CoreException {
    // create a psp file
    monitor.beginTask("Converting to PSP Project", 2);

    IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
    IResource resource = root.findMember(new Path(projectName));

    if (!resource.exists()) {
      throwCoreException("Container \"" + projectName + "\" does not exist.");
    }

    // delete Project, if is in database
    if (Manager.getInstance().containsProject(projectName)) {
      Manager.getInstance().deleteCompleteProject(projectName);
    }

    // create project and initial project plan summary and save to db and file

    Project projectDate =
        new Project(projectName, LocalDate.ofEpochDay(TimeUnit.MILLISECONDS.toDays(resource.getLocalTimeStamp())));
    Manager.getInstance().saveToDB(projectDate);

    Project p = Manager.getInstance().getProjectByName(projectName);
    ProjectPlanSummary pps = new ProjectPlanSummary(p);

    if (selectedProjects.length > 0) {
      pps = Manager.getInstance().getToDateValues(pps, selectedProjects);
    }

    Manager.getInstance().saveToDB(pps);

    PSPProject project = new PSPProject(projectDate, pps);

    DataIO.saveToFile(projectName, project, monitor);
    monitor.worked(1);

    // open psp editor with project data and task overview
    monitor.setTaskName("Opening file...");
    getShell().getDisplay().asyncExec(new Runnable() {
      public void run() {
        IWorkbenchPage page = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage();
        try {
          IDE.openEditor(page, (IFile) ((IProject) ResourcesPlugin.getWorkspace().getRoot().getProject(projectName))
              .getFile(new Path("psp.csv")), true);
          openPerspective(Constants.PERSPECTIVE_ID);
        } catch (PartInitException e) {
          e.printStackTrace();
        }
      }
    });
    monitor.worked(1);
  }

  private void throwCoreException(String message) throws CoreException {
    IStatus status = new Status(IStatus.ERROR, "editor", IStatus.OK, message, null);
    throw new CoreException(status);
  }

  private void openPerspective(String perspectiveID) {
    // opens perspective
    IWorkbenchWindow window = Activator.getDefault().getWorkbench().getActiveWorkbenchWindow();
    try {
      IWorkbenchPage page = window.getActivePage();
      IPerspectiveDescriptor perspective = page.getPerspective();
      int returnCode = 0;
      MessageDialogWithToggle msg = null;

      if (!perspective.getId().equals(Constants.PERSPECTIVE_ID)) {
        IPreferenceStore store = Activator.getDefault().getPreferenceStore();
        returnCode = store.getInt(Constants.PROPERTY_OPEN_PERSPECTIVE);
        if (returnCode == 0) {
          msg = MessageDialogWithToggle.openYesNoQuestion(getShell(), "Open Associated Perspective?",
              "This file is associated with the PSP-Perspective.\n Do you want to open the perspective now?",
              "remember my decision", false, store, Constants.PROPERTY_OPEN_PERSPECTIVE);
          returnCode = msg.getReturnCode();

          if (msg.getToggleState()) {
            store.setValue(Constants.PROPERTY_OPEN_PERSPECTIVE, returnCode);
          }
        }

        if (returnCode == 2) {
          PlatformUI.getWorkbench().showPerspective(perspectiveID, window);
        }
      }
    } catch (

    WorkbenchException e) {
      MessageDialog.openError(window.getShell(), "Error Opening Perspective",
          "Could not open Perspective with ID: " + perspectiveID);
    }
  }

}
