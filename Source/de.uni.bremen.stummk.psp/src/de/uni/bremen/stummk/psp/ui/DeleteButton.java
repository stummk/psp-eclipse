package de.uni.bremen.stummk.psp.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.forms.widgets.FormToolkit;

import de.uni.bremen.stummk.psp.Activator;
import de.uni.bremen.stummk.psp.calculation.DeleteAction;
import de.uni.bremen.stummk.psp.control.FormPage;
import de.uni.bremen.stummk.psp.utility.Constants;

/**
 * Represenst the delete button in the forms
 * 
 * @author Konstantin
 *
 */
public class DeleteButton {

  private Button btnDelete;
  private FormPage page;
  private Table table;

  /**
   * Constructor
   * 
   * @param parent the parent composite
   * @param toolkit the toolkit
   * @param id the id of the page of the delete button
   * @param page the page containing the button
   */
  public DeleteButton(Composite parent, FormToolkit toolkit, String id, FormPage page) {
    this.btnDelete = new Button(parent, SWT.NONE);
    this.page = page;
    toolkit.adapt(btnDelete, true, true);
    btnDelete.setText("Delete");
    delete();
  }

  /**
   * Constructor
   * 
   * @param parent the parent composite
   * @param toolkit the toolkit
   * @param id id of the page of the delete button
   */
  public DeleteButton(Composite parent, FormToolkit toolkit, String id) {
    this.btnDelete = new Button(parent, SWT.NONE);
    toolkit.adapt(btnDelete, true, true);
    btnDelete.setText("Delete");
    delete();
  }

  /**
   * Activate the button
   */
  public void activate() {
    this.btnDelete.setEnabled(true);
  }

  /**
   * Deactivate the button
   */
  public void deactivate() {
    this.btnDelete.setEnabled(false);
  }

  public void setTable(Table table) {
    this.table = table;
  }

  /**
   * Deletes an entry of a table
   * 
   * @param table the table, which holds the data, which can be deleted
   */
  public void delete() {
    this.btnDelete.addSelectionListener(new SelectionAdapter() {

      @Override
      public void widgetSelected(SelectionEvent e) {
        if (page instanceof FormPage) {
          table = page.getTable();
        }

        new DeleteAction("Delete Selection", Constants.COMMAND_DELETE, table, page,
            Activator.getImageDescriptor("resources/icons/delete.png")).run();
      }
    });
  }

}
