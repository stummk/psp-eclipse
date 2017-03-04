package de.uni.bremen.stummk.psp.control;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;
import org.eclipse.ui.menus.IMenuService;

import de.uni.bremen.stummk.psp.Activator;
import de.uni.bremen.stummk.psp.calculation.AddAction;
import de.uni.bremen.stummk.psp.calculation.Calc;
import de.uni.bremen.stummk.psp.calculation.DeleteAction;
import de.uni.bremen.stummk.psp.calculation.Manager;
import de.uni.bremen.stummk.psp.data.DefectRecord;
import de.uni.bremen.stummk.psp.data.PersistenceItem;
import de.uni.bremen.stummk.psp.data.Project;
import de.uni.bremen.stummk.psp.data.Task;
import de.uni.bremen.stummk.psp.data.TimeRecord;
import de.uni.bremen.stummk.psp.ui.AddButton;
import de.uni.bremen.stummk.psp.ui.ColumnHeader;
import de.uni.bremen.stummk.psp.ui.DeleteButton;
import de.uni.bremen.stummk.psp.ui.Table;
import de.uni.bremen.stummk.psp.ui.Header;
import de.uni.bremen.stummk.psp.ui.SearchField;
import de.uni.bremen.stummk.psp.utility.CheckOperation;
import de.uni.bremen.stummk.psp.utility.Constants;
import swing2swt.layout.BorderLayout;

/**
 * Represents an FormPage of the editor
 * 
 * @author Konstantin
 *
 */
public class FormPage extends org.eclipse.ui.forms.editor.FormPage {

  private Project projectID;
  private Table table;
  private ScrolledForm form;
  private FormToolkit toolkit;
  private SearchField searchField;
  private Composite composite;
  private Label lblHours, lblValues;
  private long hour = 0;
  private double value = 0;
  private FormPage page;
  private ISelectionListener listener;


  /**
   * Create the form page.
   *
   * @param editor the editor the page belongs to
   * @param id the unique id of the page
   * @param title the title of the page
   * @param projectID the project, the page belongs to
   */
  public FormPage(FormEditor editor, String id, String title, Project projectID) {
    super(editor, id, title);
    this.projectID = projectID;
    page = this;

    if (id.equals(Constants.ID_DEFECT_RECORD_FORM)) {
      listener = new ISelectionListener() {
        public void selectionChanged(IWorkbenchPart part, ISelection sel) {
          loadSelectedDefects(sel);
        }
      };
    }
  }

  /**
   * Create contents of the form.
   *
   * @param managedForm
   */
  @Override
  protected void createFormContent(IManagedForm managedForm) {
    toolkit = managedForm.getToolkit();
    form = managedForm.getForm();
    Composite body = form.getBody();
    toolkit.decorateFormHeading(form.getForm());
    toolkit.paintBordersFor(body);
    toolkit.decorateFormHeading(form.getForm());
    toolkit.paintBordersFor(body);
    managedForm.getForm().getBody().setLayout(new BorderLayout(4, 10));

    composite = new Composite(managedForm.getForm().getBody(), SWT.NONE);
    composite.setLayoutData(BorderLayout.NORTH);
    managedForm.getToolkit().adapt(composite);
    managedForm.getToolkit().paintBordersFor(composite);
    GridLayout gl_composite = new GridLayout(3, false);
    gl_composite.marginHeight = 0;
    gl_composite.verticalSpacing = 0;
    composite.setLayout(gl_composite);


    if (getId().equals(Constants.ID_TASK_PLANNING_FORM)) {
      Composite composite_1 = new Composite(managedForm.getForm().getBody(), SWT.NONE);
      composite_1.setLayoutData(BorderLayout.SOUTH);
      managedForm.getToolkit().adapt(composite_1);
      managedForm.getToolkit().paintBordersFor(composite_1);
      {
        TableWrapLayout twl_composite_1 = new TableWrapLayout();
        twl_composite_1.numColumns = 2;
        composite_1.setLayout(twl_composite_1);
      }

      Label lblTotalTime = managedForm.getToolkit().createLabel(composite_1, "Total Planned Hours:", SWT.NONE);
      lblTotalTime.setBounds(0, 0, 55, 15);

      lblHours = managedForm.getToolkit().createLabel(composite_1, "0", SWT.NONE);
      lblHours.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));

      managedForm.getToolkit().createLabel(composite_1, "Total Planned Value:", SWT.NONE);

      lblValues = managedForm.getToolkit().createLabel(composite_1, "0", SWT.NONE);
      lblValues.setLayoutData(new TableWrapData(TableWrapData.FILL, TableWrapData.TOP, 1, 1));
    }

    generateViewElements(composite);
    managedForm.reflow(true);
  }

  private void generateViewElements(Composite parent) {
    // generates view elements, depending on the page id
    switch (getId()) {
      case Constants.ID_TIME_RECORD_FORM:
        createTable(ColumnHeader.TIME_RECORDING_HEADER, Constants.COMMAND_SORT_TIME_RECORD);
        createSearchField(parent);
        createAddButton(parent);
        createDeleteButton(parent);
        refresh(getId());
        break;
      case Constants.ID_DEFECT_RECORD_FORM:
        createTable(ColumnHeader.DEFECT_RECORDING_HEADER, Constants.COMMAND_SORT_DEFECT_RECORD);
        createSearchField(parent);
        createAddButton(parent);
        createDeleteButton(parent);
        getSite().getPage().addSelectionListener(listener);
        refresh(getId());
        break;
      case Constants.ID_TEST_REPROT_FORM:
        createTable(ColumnHeader.TEST_REPORT_HEADER, Constants.COMMAND_SORT_TEST_REPORT);
        createSearchField(parent);
        refresh(getId());
        break;
      case Constants.ID_TASK_PLANNING_FORM:
        createTable(ColumnHeader.TASK_PLANNING_HEADER, Constants.COMMAND_SORT_TASK_TEMPLATE);
        createSearchField(parent);
        createAddButton(parent);
        createDeleteButton(parent);
        refresh(getId());
        break;
      case Constants.ID_SCHEDULE_PLANNING_FORM:
        createTable(ColumnHeader.SCHEDULE_PLANNING_HEADER, Constants.COMMAND_SORT_SCHEDULE_TEMPLATE);
        createSearchField(parent);
        refresh(getId());
        break;
      case Constants.ID_PIP_FORM:
        createTable(ColumnHeader.PIP_HEADER, Constants.COMMAND_SORT_PIP);
        createSearchField(parent);
        createAddButton(parent);
        createDeleteButton(parent);
        refresh(getId());
        break;
    }
  }

  private void createTable(Header[] columnHeader, String sortPreference) {
    table = new Table(toolkit, form.getBody(), columnHeader, true, sortPreference);
    table.getTable().addMouseListener(new MouseAdapter() {

      @Override
      public void mouseDown(MouseEvent e) {
        if (!(getId().equals(Constants.ID_SCHEDULE_PLANNING_FORM) || getId().equals(Constants.ID_TEST_REPROT_FORM))) {
          IMenuService mSvc = (IMenuService) getSite().getWorkbenchWindow().getService(IMenuService.class);
          MenuManager mgr = new MenuManager();
          mSvc.populateContributionManager(mgr, "popup:de.uni.bremen.stummk.psp.menuContribution");
          table.getTable().setMenu(null);

          if (e.button == 3) {
            mgr.add(new AddAction(form.getShell(), getId(), projectID, null, Constants.COMMAND_ADD, "Add",
                Activator.getImageDescriptor("resources/icons/add.png")));
          }

          if (e.button == 3 && table.getSelection() instanceof PersistenceItem) {
            mgr.add(new AddAction(form.getShell(), getId(), projectID, table.getSelection(), Constants.COMMAND_ADD,
                "Edit", Activator.getImageDescriptor("resources/icons/edit.png")));
            mgr.add(new DeleteAction("Delete", Constants.COMMAND_DELETE, table, page,
                Activator.getImageDescriptor("resources/icons/delete.png")));
          }

          table.getTable().setMenu(mgr.createContextMenu(table.getTable()));
        }

        super.mouseDown(e);
      }

      @Override
      public void mouseDoubleClick(MouseEvent e) {
        if (table.getSelection() instanceof PersistenceItem) {
          new AddAction(form.getShell(), getId(), projectID, table.getSelection(), Constants.COMMAND_ADD, "",
              Activator.getImageDescriptor("resources/icons/edit.png")).run();
        }
        super.mouseDoubleClick(e);
      }
    });
  }

  private void createSearchField(Composite parent) {
    searchField = new SearchField(parent, toolkit);
    searchField.getSearchField().addModifyListener(new ModifyListener() {

      @Override
      public void modifyText(ModifyEvent e) {
        if (searchField.getSearchField().getText().equals("")) {
          refresh(getId());
        } else {
          refresh(getId());
          searchField.search(table);
        }
      }
    });
  }

  private void createAddButton(Composite parent) {
    new AddButton(parent, toolkit, getId(), projectID);
  }

  private void createDeleteButton(Composite parent) {
    new DeleteButton(parent, toolkit, getId(), page);
  }

  /**
   * Refreshes the data of the page
   * 
   * @param pageID the unique id of the page
   */
  public void refresh(String pageID) {
    table.clear();
    List<? extends PersistenceItem> list = new ArrayList<PersistenceItem>();
    switch (pageID) {
      case Constants.ID_TIME_RECORD_FORM:
        list = Manager.getInstance().getTimeRecord(projectID.getProjectName());
        table.setDefaultItems(list);
        table.sort();
        if (!searchField.getSearchField().getText().isEmpty()) {
          searchField.search(table);
        }
        markSelectedTask();
        break;
      case Constants.ID_DEFECT_RECORD_FORM:
        list = Manager.getInstance().getDefectRecord(projectID.getProjectName());
        table.setDefaultItems(list);
        table.sort();
        if (!searchField.getSearchField().getText().isEmpty()) {
          searchField.search(table);
        }
        markSelectionDefects();
        break;
      case Constants.ID_TEST_REPROT_FORM:
        list = Manager.getInstance().getTestReport(projectID.getProjectName());
        table.setDefaultItems(list);
        table.sort();
        if (!searchField.getSearchField().getText().isEmpty()) {
          searchField.search(table);
        }
        break;
      case Constants.ID_TASK_PLANNING_FORM:
        hour = Calc.getInstance().getTotalPlannedHours(projectID.getProjectName());
        value = Manager.getInstance().getTaskPlanning(projectID.getProjectName()).stream()
            .mapToDouble(v -> v.getPlanValue()).sum();
        lblHours.setText("" + TimeUnit.MINUTES.toHours(hour) + "h : " + hour % 60 + " min");
        lblValues.setText("" + value);
        list = Manager.getInstance().getTaskPlanning(projectID.getProjectName());
        table.setDefaultItems(list);
        table.sort();
        if (!searchField.getSearchField().getText().isEmpty()) {
          searchField.search(table);
        }
        break;
      case Constants.ID_SCHEDULE_PLANNING_FORM:
        list = Manager.getInstance().getSchedulePlanning(projectID.getProjectName());
        table.setDefaultItems(list);
        table.sort();
        if (!searchField.getSearchField().getText().isEmpty()) {
          searchField.search(table);
        }
        break;
      case Constants.ID_PIP_FORM:
        list = Manager.getInstance().getPip(projectID.getProjectName());
        table.setDefaultItems(list);
        table.sort();
        if (!searchField.getSearchField().getText().isEmpty()) {
          searchField.search(table);
        }
        break;
    }
  }

  /**
   * @return the table of the page
   */
  public Table getTable() {
    return this.table;
  }

  /**
   * @return the Project of the page
   */
  public Project getProject() {
    return this.projectID;
  }

  private void markSelectedTask() {
    // marks the selected task in the task overview
    TaskOverview view = (TaskOverview) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
        .findView(Constants.ID_TASK_OVERVIEW);
    if (view != null && view.getProject() != null
        && view.getProject().getProjectName().equals(projectID.getProjectName())) {
      PersistenceItem item = view.getTable().getSelection();
      if (item instanceof Task) {
        TreeItem[] gItems = page.getTable().getTable().getItems();
        for (TreeItem i : gItems) {
          if (i.getData() instanceof TimeRecord && ((TimeRecord) i.getData()).getTask() != null
              && ((TimeRecord) i.getData()).getTask().getID() == ((Task) item).getID()) {
            i.setBackground(new Color(i.getDisplay(), 132, 112, 255));
          }
        }
      }
    }
  }

  private void markSelectionDefects() {
    // select the defects of one selection
    ISelectionService service = getSite().getWorkbenchWindow().getSelectionService();
    IStructuredSelection sel = (IStructuredSelection) service.getSelection("org.eclipse.jdt.ui.PackageExplorer");

    loadSelectedDefects(sel);
  }

  /**
   * Loads the defects of one selection in the package explorer
   * 
   * @param sel the selection in the package explorer
   */
  protected void loadSelectedDefects(ISelection sel) {
    if (!(sel instanceof IStructuredSelection)) {
      return;
    }

    IProject p = CheckOperation.getProjectBySelection(sel);
    if (p != null && CheckOperation.projectContainFile(p) && p.getName().equals(projectID.getProjectName())) {
      if (sel instanceof IStructuredSelection) {
        Object firstElement = ((IStructuredSelection) sel).getFirstElement();
        IResource res = null;

        if (firstElement instanceof IAdaptable) {
          res = (IResource) (((IAdaptable) firstElement).getAdapter(IResource.class));
        } else if (firstElement instanceof IResource) {
          res = (IResource) firstElement;
        }

        if (res != null) {
          List<DefectRecord> recs = getDefectRecs(res);
          if (recs != null) {
            if (!getTable().getTable().isDisposed()) {
              getTable().clear();
              getTable().setDefaultItems(recs);
              getTable().sort();
            }
          }
        }
      }
    }
  }

  private List<DefectRecord> getDefectRecs(IResource res) {
    // Gets defects hierarchical, of file, folder or project
    List<DefectRecord> recs = new ArrayList<>();
    if (res.getType() == IResource.FILE) {
      return Manager.getInstance().getDefectRecord(projectID.getProjectName(), res.getProjectRelativePath());
    } else if (res.getType() == IResource.PROJECT) {
      return Manager.getInstance().getDefectRecord(projectID.getProjectName(), res.getProjectRelativePath());
    } else {
      List<IResource> resources = new ArrayList<IResource>();
      CheckOperation.findMembers(resources, res.getLocation(), ResourcesPlugin.getWorkspace().getRoot());

      for (IResource r : resources) {
        recs.addAll(Manager.getInstance().getDefectRecord(projectID.getProjectName(), r.getProjectRelativePath()));
      }
    }

    return recs;
  }
}
