package de.uni.bremen.stummk.psp.control;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.forms.IManagedForm;
import org.eclipse.ui.forms.editor.FormEditor;
import org.eclipse.ui.forms.editor.FormPage;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.ScrolledForm;

import de.uni.bremen.stummk.psp.calculation.Manager;
import de.uni.bremen.stummk.psp.data.Project;
import de.uni.bremen.stummk.psp.data.ProjectPlanSummary;
import de.uni.bremen.stummk.psp.data.SummaryItem;
import de.uni.bremen.stummk.psp.ui.ColumnHeader;
import de.uni.bremen.stummk.psp.ui.Table;
import swing2swt.layout.BorderLayout;

/**
 * Represents the<b>Project Plan Summary</b>of a project
 * 
 * @author Konstantin
 *
 */
public class ProjectSummaryForm extends FormPage {

  private ProjectPlanSummary pps;
  private Project projectID;
  private Label lblProjectID, lblDate;
  private Table tblSummary, tblSize, tblTime, tblDefInject, tblDefRemoved;
  private List<Table> tables = new ArrayList<>();
  private ScrolledForm form;
  private FormToolkit toolkit;
  private Composite composite_1;

  /**
   * Create the form page.
   *
   * @param editor Editor in with this page is created
   * @param id page identifier
   * @param title page title
   * @param projectID the project id
   */
  public ProjectSummaryForm(FormEditor editor, String id, String title, Project projectID) {
    super(editor, id, title);
    this.projectID = projectID;
  }

  @Override
  protected void createFormContent(IManagedForm managedForm) {
    toolkit = managedForm.getToolkit();
    form = managedForm.getForm();
    form.setText("Project Plan Summary");
    managedForm.getForm().getBody().setLayout(new BorderLayout(4, 10));

    Composite composite = new Composite(managedForm.getForm().getBody(), SWT.NONE);
    composite.setLayoutData(BorderLayout.NORTH);
    managedForm.getToolkit().adapt(composite);
    managedForm.getToolkit().paintBordersFor(composite);
    composite.setLayout(new GridLayout(2, false));

    Label lblProject = new Label(composite, SWT.NONE);
    managedForm.getToolkit().adapt(lblProject, true, true);
    lblProject.setText("Project:");

    lblProjectID = new Label(composite, SWT.NONE);
    managedForm.getToolkit().adapt(lblProjectID, true, true);
    lblProjectID.setText("projectID");

    Label lblCreationDate = new Label(composite, SWT.NONE);
    managedForm.getToolkit().adapt(lblCreationDate, true, true);
    lblCreationDate.setText("Creation Date:");

    lblDate = new Label(composite, SWT.NONE);
    managedForm.getToolkit().adapt(lblDate, true, true);
    lblDate.setText("date");

    lblProjectID.setText(projectID.getProjectName());
    lblDate.setText(projectID.getTimestamp().toString());

    composite_1 = new Composite(managedForm.getForm().getBody(), SWT.NONE);
    composite_1.setLayoutData(BorderLayout.CENTER);
    managedForm.getToolkit().adapt(composite_1);
    managedForm.getToolkit().paintBordersFor(composite_1);
    GridLayout gl = new GridLayout();
    gl.numColumns = 1;
    composite_1.setLayout(gl);

    tblSummary = new Table(toolkit, composite_1, ColumnHeader.SUMMARY_HEADER, false, "");
    tblSize = new Table(toolkit, composite_1, ColumnHeader.SIZE_HEADER, false, "");
    tblTime = new Table(toolkit, composite_1, ColumnHeader.TIME_IN_PHASE_HEADER, false, "");
    tblDefInject = new Table(toolkit, composite_1, ColumnHeader.DEFECT_INJECTED_HEADER, false, "");
    tblDefRemoved = new Table(toolkit, composite_1, ColumnHeader.DEFECT_REMOVED_HEADER, false, "");

    tables.add(tblSummary);
    tables.add(tblSize);
    tables.add(tblTime);
    tables.add(tblDefInject);
    tables.add(tblDefRemoved);

    refresh();
    managedForm.reflow(true);
  }

  /**
   * Refreshes this page
   */
  public void refresh() {
    tables.stream().forEach(v -> v.clear());
    setData();
  }

  private void setData() {
    pps = Manager.getInstance().getProjectSummary(projectID.getProjectName());
    for (int i = 1; i <= tables.size(); ++i) {
      fillWithData(tables.get(i - 1), pps.getSectionContent(i * 100));
    }
  }

  private void fillWithData(Table table, List<? extends SummaryItem> data) {
    for (SummaryItem si : data) {
      TreeItem item = new TreeItem(table.getTable(), SWT.NONE);
      for (int i = 0; i < si.getElements().size(); ++i) {
        item.setText(i, si.getElements().get(i));
      }
    }
  }

  /**
   * @return {@link ProjectPlanSummary} of the {@link ProjectSummaryForm}
   */
  public ProjectPlanSummary getProjectPlanSummary() {
    return this.pps;
  }

}
