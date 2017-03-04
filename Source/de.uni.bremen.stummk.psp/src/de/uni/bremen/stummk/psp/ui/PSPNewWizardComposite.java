package de.uni.bremen.stummk.psp.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

import swing2swt.layout.BorderLayout;

/**
 * Represents the graphical user interface of the wizard page
 * 
 * @author Konstantin
 *
 */
public class PSPNewWizardComposite extends Composite {
  private Text text;
  private Button btnBrowse;
  private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());
  private List multi;

  /**
   * Create the composite.
   * 
   * @param parent the parent composite
   * @param style the Style of this composite
   */
  public PSPNewWizardComposite(Composite parent, int style) {
    super(parent, SWT.NONE);
    setLayout(new BorderLayout(0, 0));

    Composite composite = new Composite(this, SWT.NONE);
    composite.setLayoutData(BorderLayout.NORTH);
    formToolkit.adapt(composite);
    formToolkit.paintBordersFor(composite);
    {
      TableWrapLayout twl_composite = new TableWrapLayout();
      twl_composite.numColumns = 3;
      composite.setLayout(twl_composite);
    }

    Label lblProjectName = new Label(composite, SWT.NONE);
    lblProjectName.setLayoutData(new TableWrapData(TableWrapData.CENTER, TableWrapData.MIDDLE, 1, 1));
    lblProjectName.setText("Project:");

    text = new Text(composite, SWT.BORDER);
    text.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));

    btnBrowse = new Button(composite, SWT.NONE);
    btnBrowse.setText("Browse ...");

    Composite composite_1 = formToolkit.createComposite(this, SWT.NONE);
    composite_1.setLayoutData(BorderLayout.CENTER);
    formToolkit.paintBordersFor(composite_1);
    {
      composite_1.setLayout(new TableWrapLayout());
    }

    Label lblNewLabel = new Label(composite_1, SWT.NONE);
    lblNewLabel.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
    formToolkit.adapt(lblNewLabel, true, true);
    lblNewLabel.setText("Select the projects,you want to use to calculate the To Date values");

    multi = new List(composite_1, SWT.BORDER | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL);
    TableWrapData twd_multi = new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.FILL_GRAB, 1, 1);
    twd_multi.heightHint = 152;
    multi.setLayoutData(twd_multi);
    formToolkit.adapt(multi, true, true);

  }

  /**
   * @return the selected project
   */
  public Text getTextField() {
    return this.text;
  }

  /**
   * @return browse button
   */
  public Button getBrowseButton() {
    return this.btnBrowse;
  }

  /**
   * @return the multi selection list of this composite
   */
  public List getMultiSelection() {
    return this.multi;
  }
}
