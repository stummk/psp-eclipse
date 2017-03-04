package de.uni.bremen.stummk.psp.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.forms.widgets.FormToolkit;

import de.uni.bremen.stummk.psp.calculation.Filter;
import de.uni.bremen.stummk.psp.control.TaskOverview;
import de.uni.bremen.stummk.psp.data.Project;
import de.uni.bremen.stummk.psp.utility.Constants;
import swing2swt.layout.BorderLayout;

/**
 * Represents the task overview
 * 
 * @author Konstantin
 *
 */
public class TaskOverviewComposite extends Composite {

  private final FormToolkit toolkit = new FormToolkit(Display.getCurrent());
  private Table table;
  private SearchField txtSearch;
  private AddButton btnNew;
  private DeleteButton btnDelete;

  /**
   * Create the composite.
   *
   * @param parent the parent composite
   * @param style the style
   * @param projectID the id of the project tasks, the overview is shown
   */
  public TaskOverviewComposite(Composite parent, int style, Project projectID) {
    super(parent, style);
    addDisposeListener(new DisposeListener() {
      public void widgetDisposed(DisposeEvent e) {
        toolkit.dispose();
      }
    });
    toolkit.adapt(this);
    toolkit.paintBordersFor(this);
    setLayout(new BorderLayout(4, 10));

    Composite composite = new Composite(this, SWT.NONE);
    composite.setLayoutData(BorderLayout.NORTH);
    toolkit.adapt(composite);
    toolkit.paintBordersFor(composite);
    GridLayout gl_composite = new GridLayout(3, false);
    gl_composite.marginHeight = 0;
    gl_composite.verticalSpacing = 0;
    composite.setLayout(gl_composite);

    // components
    txtSearch = new SearchField(composite, toolkit);
    table = new Table(toolkit, this, Filter.filterColumns(ColumnHeader.TASK_OVERVIEW_HEADER), true,
        Constants.COMMAND_SORT_TASK_OVERVIEW);
    btnNew = new AddButton(composite, toolkit, Constants.ID_TASK_OVERVIEW, projectID);
    btnDelete = new DeleteButton(composite, toolkit, Constants.ID_TASK_OVERVIEW);

  }

  /**
   * @return the {@link Table} of the {@link TaskOverview}
   */
  public Table getTable() {
    return this.table;
  }

  /**
   * @return the {@link SearchField} of the {@link TaskOverview}
   */
  public SearchField getSearchField() {
    return this.txtSearch;
  }

  /**
   * @return the {@link AddButton} of the {@link TaskOverview}
   */
  public AddButton getAddButton() {
    return this.btnNew;
  }

  /**
   * @return the {@link DeleteButton} of the {@link TaskOverview}
   */
  public DeleteButton getDeleteButton() {
    return this.btnDelete;
  }

}
