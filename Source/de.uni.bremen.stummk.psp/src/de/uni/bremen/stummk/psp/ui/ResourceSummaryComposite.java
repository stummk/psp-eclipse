package de.uni.bremen.stummk.psp.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.FormToolkit;

/**
 * Composite of the resource summary view
 * 
 * @author Konstantin
 *
 */
public class ResourceSummaryComposite {
  private Label lblInfo;
  private Composite composite;
  private final FormToolkit formToolkit = new FormToolkit(Display.getDefault());

  /**
   * Constructor
   * 
   * @param parent the parent composite
   * @param style the style of this composite
   */
  public ResourceSummaryComposite(Composite parent, int style) {
    parent.setLayout(new FillLayout());

    ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
    formToolkit.adapt(scrolledComposite);
    formToolkit.paintBordersFor(scrolledComposite);
    scrolledComposite.setExpandHorizontal(true);
    scrolledComposite.setExpandVertical(true);

    composite = new Composite(scrolledComposite, SWT.BORDER);
    formToolkit.adapt(composite);
    formToolkit.paintBordersFor(composite);
    composite.setLayout(new FillLayout(SWT.HORIZONTAL));

    lblInfo = new Label(composite, SWT.NONE);
    lblInfo.setText(
        "Resource:       \nDefects Injected: Planning - 0, Design - 0, Design Review - 0, Code - 0, Code Review - 0, Compile - 0, Test - 0, Postmortem - 0, Total - 0     \nDefects Removed: Planning - 0, Design - 0, Design Review - 0, Code - 0, Code Review - 0, Compile - 0, Test - 0, Postmortem - 0, Total - 0      \nLines of Code - 0     \n");
    formToolkit.adapt(lblInfo, true, true);

    scrolledComposite.setContent(composite);
    scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
  }

  /**
   * @return the label of the composite
   */
  public Label getInfoLabel() {
    return this.lblInfo;
  }

  public FormToolkit getFormToolkit() {
    return this.formToolkit;
  }

  public Composite getContainer() {
    return this.composite;
  }
}
