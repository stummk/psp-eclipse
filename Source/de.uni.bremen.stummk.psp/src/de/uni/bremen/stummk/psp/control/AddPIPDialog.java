package de.uni.bremen.stummk.psp.control;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.TableWrapData;

import de.uni.bremen.stummk.psp.calculation.Manager;
import de.uni.bremen.stummk.psp.data.PIP;
import de.uni.bremen.stummk.psp.data.Project;

/**
 * This class represents an dialog, with which a pip entry can be added
 *
 * @author Konstantin
 *
 */
public class AddPIPDialog extends WindowDialog {

  private Text txtDescription, text, text_1;
  private String[] labels = {"Problem", "Proposal", "Description"};

  /**
   * Constructor
   * 
   * @param shell the calling shell
   * @param project the project id
   */
  public AddPIPDialog(Shell shell, Project project) {
    super(shell, project);
    super.setComponents(labels);
  }

  /**
   * Constructor
   * 
   * @param shell the parent shell
   * @param projectID the project id
   * @param selection the selected {@link PIP}
   */
  public AddPIPDialog(Shell shell, Project projectID, PIP selection) {
    super(shell, projectID, selection);
    super.setComponents(labels);
  }

  @Override
  protected void setData() {
    PIP pip = (PIP) super.getSelection();
    text.setText(pip.getProblemDescription());
    text_1.setText(pip.getProposalDescription());
    txtDescription.setText(pip.getNotes());
  }

  @Override
  protected void configureShell(Shell newShell) {
    super.configureShell(newShell);
    String text = " PIP Record to project : " + super.getProject().getProjectName();
    if (super.getSelection() instanceof PIP) {
      newShell.setText("Edit" + text);
    } else {
      newShell.setText("Add" + text);
    }
  }

  @Override
  protected Point getInitialSize() {
    return new Point(450, 180);
  }

  @Override
  public void check() {
    if (text.getText().isEmpty() && text_1.getText().isEmpty()) {
      super.getErrLbl().setText("The problem or purposal should be descripted");
    } else {
      // add changes to db
      if (super.getSelection() != null) {
        PIP pip = (PIP) super.getSelection();
        pip.refreshItems(text.getText(), text_1.getText(), txtDescription.getText());
        Manager.getInstance().update(pip);
      } else {
        super.setSelection(new PIP(text.getText(), text_1.getText(), txtDescription.getText(), super.getProject()));
        Manager.getInstance().saveToDB(super.getSelection());
      }

      refresh();
      close();
    }
  }

  @Override
  protected void addComponent(Composite container, int i) {
    switch (i) {
      case 0:
        text = new Text(container, SWT.NONE);
        text.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
        text.setText("");
        break;
      case 1:
        text_1 = new Text(container, SWT.NONE);
        text_1.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
        text_1.setText("");
        break;
      case 2:
        txtDescription = new Text(container, SWT.WRAP | SWT.H_SCROLL);
        txtDescription.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
        txtDescription.setText("");
        break;
    }
  }
}
