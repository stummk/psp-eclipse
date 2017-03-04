package de.uni.bremen.stummk.psp.control;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.part.FileEditorInput;

import de.uni.bremen.stummk.psp.calculation.Manager;
import de.uni.bremen.stummk.psp.data.PSPProject;
import de.uni.bremen.stummk.psp.data.Project;
import de.uni.bremen.stummk.psp.utility.CheckOperation;
import de.uni.bremen.stummk.psp.utility.Constants;
import de.uni.bremen.stummk.psp.utility.DataIO;
import de.uni.bremen.stummk.psp.utility.FileHash;

/***
 * The Editor holds all forms of the PSP Project data
 * 
 * @author Konstantin
 *
 */
public class Editor extends FormEditor implements IResourceChangeListener {

  private Project project;

  private ProjectSummaryForm summaryForm;
  private List<FormPage> pages = new ArrayList<>();

  /**
   * Constructor
   */
  public Editor() {
    super();
    ResourcesPlugin.getWorkspace().addResourceChangeListener(this);
  }


  @Override
  public void init(final IEditorSite site, final IEditorInput input) throws PartInitException {
    if (!(input instanceof IFileEditorInput)) {
      throw new PartInitException("Invalid Input: Must be IFileEditorInput");
    }
    super.init(site, input);
    String fileProjectName = getFile().getProject().getName();
    try {
      getFile().getProject().refreshLocal(IResource.DEPTH_INFINITE, null);
    } catch (CoreException e) {
      e.printStackTrace();
    }

    // loads project from database. If Project not exists, load project from file and save to db
    if (Manager.getInstance().containsProject(fileProjectName)) {
      if (!CheckOperation.projectContainFile(getFile())) {
        dispose();
        return;
      }

      // checks hash
      checkHash(getFile(), fileProjectName);

      project = Manager.getInstance().getProjectByName(fileProjectName);
    } else {
      if (CheckOperation.projectContainFile(getFile())) {
        PSPProject psp = DataIO.loadFromFile(getFile().getProject().getName());
        Manager.getInstance().saveBackupProject(psp);
        project = psp.getProject();
      } else {
        throw new PartInitException("No Input Found for this Editor");
      }
    }
    setPartName("PSP Project Data - " + project.getProjectName());
  }

  @Override
  protected void addPages() {
    try {
      this.summaryForm =
          new ProjectSummaryForm(this, Constants.ID_PROJECT_SUMMARY_FORM, "Project Plan Summary", project);
      pages.add(new FormPage(this, Constants.ID_TIME_RECORD_FORM, "Time Recording Log", project));
      pages.add(new FormPage(this, Constants.ID_DEFECT_RECORD_FORM, "Defect Recording Log", project));
      pages.add(new FormPage(this, Constants.ID_PIP_FORM, "Process Improvement Proposal", project));
      pages.add(new FormPage(this, Constants.ID_TEST_REPROT_FORM, "Test Report", project));
      pages.add(new FormPage(this, Constants.ID_TASK_PLANNING_FORM, "Task Planing", project));
      pages.add(new FormPage(this, Constants.ID_SCHEDULE_PLANNING_FORM, "Schedule Planing", project));

      addPage(summaryForm);
      pages.stream().forEach(v -> {
        try {
          addPage(v);
        } catch (PartInitException e) {
          e.printStackTrace();
        }
      });
    } catch (PartInitException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void doSave(IProgressMonitor monitor) {
    // is not supported
  }

  @Override
  public void doSaveAs() {
    // Do nothing
  }

  @Override
  public boolean isSaveAsAllowed() {
    return false;
  }

  @Override
  public void dispose() {
    // deletes project from database, if file is deleted
    if (!CheckOperation.projectContainFile(getFile()) && project != null
        && Manager.getInstance().containsProject(project.getProjectName())) {
      Manager.getInstance().deleteCompleteProject(project.getProjectName());
    }

    ResourcesPlugin.getWorkspace().removeResourceChangeListener(this);
    super.dispose();
  }

  @Override
  public void resourceChanged(IResourceChangeEvent event) {
    // If ressources changed close editor
    if (event.getType() == IResourceChangeEvent.PRE_CLOSE || event.getType() == IResourceChangeEvent.PRE_DELETE) {
      Display.getDefault().asyncExec(new Runnable() {
        public void run() {
          IWorkbenchPage[] pages = getSite().getWorkbenchWindow().getPages();
          for (int i = 0; i < pages.length; i++) {
            if (((FileEditorInput) getEditorInput()).getFile().getProject().equals(event.getResource())) {
              IEditorPart editorPart = pages[i].findEditor(getEditorInput());
              pages[i].closeEditor(editorPart, true);
            }
          }
        }
      });
    } else {
      if (CheckOperation.projectContainFile(getFile())) {
        Display.getDefault().asyncExec(new Runnable() {
          public void run() {
            checkHash(getFile(), getFile().getProject().getName());
            refreshPages();
          }
        });
      }
    }
  }

  /**
   * Refreshes the page of the project plan summary
   */
  public void refreshSummaryPage() {
    if (this.summaryForm.isActive()) {
      this.summaryForm.refresh();
    }
  }

  @Override
  public void setFocus() {
    refreshPages();
    super.setFocus();
  }

  /**
   * Refreshes all pages of this editor
   */
  public void refreshPages() {
    refreshSummaryPage();

    for (FormPage page : pages) {
      if (page.isActive()) {
        page.refresh(page.getId());
      }
    }
  }

  @Override
  protected void pageChange(int newPageIndex) {
    super.pageChange(newPageIndex);
    switch (newPageIndex) {
      case 0:
        this.summaryForm.refresh();
        break;
      case 1:
        this.pages.get((newPageIndex - 1)).refresh(Constants.ID_TIME_RECORD_FORM);
        break;
      case 2:
        this.pages.get((newPageIndex - 1)).refresh(Constants.ID_DEFECT_RECORD_FORM);
        break;
      case 3:
        this.pages.get((newPageIndex - 1)).refresh(Constants.ID_PIP_FORM);
        break;
      case 4:
        this.pages.get((newPageIndex - 1)).refresh(Constants.ID_TEST_REPROT_FORM);
        break;
      case 5:
        this.pages.get((newPageIndex - 1)).refresh(Constants.ID_TASK_PLANNING_FORM);
        break;
      case 6:
        this.pages.get((newPageIndex - 1)).refresh(Constants.ID_SCHEDULE_PLANNING_FORM);
        break;
    }
  }


  /**
   * @return the {@link Project} of this editor
   */
  public Project getProject() {
    return this.project;
  }

  /**
   * @return the {@link ProjectSummaryForm} of the editor
   */
  public ProjectSummaryForm getProjectSummaryForm() {
    return this.summaryForm;
  }

  private void checkHash(IFile file, String projectName) {
    // Checks the hash of the file
    String hash = FileHash.hash(file);
    try {
      if (file.getPersistentProperty(Constants.PROPERTY_HASH) != null
          && !file.getPersistentProperty(Constants.PROPERTY_HASH).equals(hash)) {
        if (Manager.getInstance().containsProject(projectName)) {
          Manager.getInstance().deleteCompleteProject(projectName);
        }
        Manager.getInstance().saveBackupProject(DataIO.loadFromFile(projectName));
      }
    } catch (CoreException e) {
      e.printStackTrace();
    }
  }

  private IFile getFile() {
    // returns File, from which the editor is called
    final IEditorInput input = getEditorInput();
    if (input instanceof FileEditorInput) {
      return ((FileEditorInput) input).getFile();
    }
    return null;
  }


}
