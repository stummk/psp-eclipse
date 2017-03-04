package de.uni.bremen.stummk.psp.ui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

/**
 * Composite of the filter dialog
 * 
 * @author Konstantin
 *
 */
public class FilterDialogComposite {

  private Button btnHideReady, btnShowAll, btnShowPlanning, btnShowDesign, btnShowDesignReview, btnShowCode,
      btnShowCodeReview, btnShowCompile, btnShowTest, btnShowPostmortem, btnShowTime, btnShowValue, btnShowStatus,
      btnShowPriority, btnShowDate;
  private Composite composite;
  private Composite container_1;
  private Composite composite_1;

  /**
   * Constructor
   * 
   * @param parent the parent composite
   */
  public FilterDialogComposite(Composite parent) {
    createArea(parent);
  }

  private void createArea(Composite parent) {
    // create the ui area and set ui-data to it
    ScrolledComposite sc = new ScrolledComposite(parent, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);

    container_1 = new Composite(sc, SWT.NONE);
    {
      TableWrapLayout twl_container_1 = new TableWrapLayout();
      container_1.setLayout(twl_container_1);
    }

    setData(container_1);

    sc.setContent(container_1);
    sc.setExpandHorizontal(true);
    sc.setExpandVertical(true);
    sc.setMinSize(container_1.computeSize(SWT.DEFAULT, SWT.DEFAULT));
  }

  private void setData(Composite container) {
    // adds ui elements to the container
    Label lblFilter = new Label(container, SWT.NONE);
    lblFilter.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
    lblFilter.setText("Choose Filter for the Table in the Task Overview:");

    Label lblTask = new Label(container, SWT.NONE);
    lblTask.setText("Tasks:");

    btnHideReady = new Button(container, SWT.CHECK);
    btnHideReady.setBounds(29, 0, 93, 16);
    btnHideReady.setText("Hide Ready Tasks");

    Label lblPhases = new Label(container, SWT.NONE);
    lblPhases.setText("Phases:");

    btnShowAll = new Button(container, SWT.CHECK);
    btnShowAll.setBounds(29, 0, 93, 16);
    btnShowAll.setText("Show all phases");
    btnShowAll.addSelectionListener(new SelectionAdapter() {
      @Override
      public void widgetSelected(SelectionEvent se) {
        if (btnShowAll.getSelection()) {
          selectAndActivate(true);
        } else {
          selectAndActivate(false);
        }
      }
    });

    composite = new Composite(container_1, SWT.NONE);
    {
      TableWrapLayout twl_composite = new TableWrapLayout();
      twl_composite.numColumns = 4;
      composite.setLayout(twl_composite);
    }
    composite.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));

    btnShowPlanning = new Button(composite, SWT.CHECK);
    btnShowPlanning.setBounds(29, 0, 100, 16);
    btnShowPlanning.setText("Show Planning");

    btnShowDesign = new Button(composite, SWT.CHECK);
    btnShowDesign.setBounds(29, 0, 93, 16);
    btnShowDesign.setText("Show Design");

    btnShowDesignReview = new Button(composite, SWT.CHECK);
    btnShowDesignReview.setBounds(29, 0, 93, 16);
    btnShowDesignReview.setText("Show Design Review");

    btnShowCode = new Button(composite, SWT.CHECK);
    btnShowCode.setBounds(29, 0, 93, 16);
    btnShowCode.setText("Show Code");

    btnShowCodeReview = new Button(composite, SWT.CHECK);
    btnShowCodeReview.setBounds(29, 0, 93, 16);
    btnShowCodeReview.setText("Show Code Review");

    btnShowCompile = new Button(composite, SWT.CHECK);
    btnShowCompile.setBounds(29, 0, 93, 16);
    btnShowCompile.setText("Show Compile");

    btnShowTest = new Button(composite, SWT.CHECK);
    btnShowTest.setBounds(29, 0, 93, 16);
    btnShowTest.setText("Show Test");

    btnShowPostmortem = new Button(composite, SWT.CHECK);
    btnShowPostmortem.setBounds(29, 0, 93, 16);
    btnShowPostmortem.setText("Show Postmortem");

    Label lblColumns = new Label(container, SWT.NONE);
    lblColumns.setText("Columns to show:");

    composite_1 = new Composite(container_1, SWT.NONE);
    {
      TableWrapLayout twl_composite = new TableWrapLayout();
      twl_composite.numColumns = 4;
      composite_1.setLayout(twl_composite);
    }
    composite_1.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.FILL, 1, 1));

    btnShowPriority = new Button(composite_1, SWT.CHECK);
    btnShowPriority.setBounds(29, 0, 93, 16);
    btnShowPriority.setText("Show Pritotity");

    btnShowTime = new Button(composite_1, SWT.CHECK);
    btnShowTime.setBounds(29, 0, 93, 16);
    btnShowTime.setText("Show Time");

    btnShowValue = new Button(composite_1, SWT.CHECK);
    btnShowValue.setBounds(29, 0, 93, 16);
    btnShowValue.setText("Show Value");

    btnShowStatus = new Button(composite_1, SWT.CHECK);
    btnShowStatus.setBounds(29, 0, 93, 16);
    btnShowStatus.setText("Show Status");

    btnShowDate = new Button(composite_1, SWT.CHECK);
    btnShowDate.setBounds(29, 0, 93, 16);
    btnShowDate.setText("Show Change Date");
  }

  /**
   * @return the btnHideReady
   */
  public Button getBtnHideReady() {
    return btnHideReady;
  }

  /**
   * @return the btnShowAll
   */
  public Button getBtnShowAll() {
    return btnShowAll;
  }

  /**
   * @return the btnShowPlanning
   */
  public Button getBtnShowPlanning() {
    return btnShowPlanning;
  }

  /**
   * @return the btnShowDesign
   */
  public Button getBtnShowDesign() {
    return btnShowDesign;
  }

  /**
   * @return the btnShowDesignReview
   */
  public Button getBtnShowDesignReview() {
    return btnShowDesignReview;
  }

  /**
   * @return the btnShowCode
   */
  public Button getBtnShowCode() {
    return btnShowCode;
  }

  /**
   * @return the btnShowCodeReview
   */
  public Button getBtnShowCodeReview() {
    return btnShowCodeReview;
  }

  /**
   * @return the btnShowCompile
   */
  public Button getBtnShowCompile() {
    return btnShowCompile;
  }

  /**
   * @return the btnShowTest
   */
  public Button getBtnShowTest() {
    return btnShowTest;
  }

  /**
   * @return the btnShowPostmortem
   */
  public Button getBtnShowPostmortem() {
    return btnShowPostmortem;
  }

  /**
   * @return the btnShowTime
   */
  public Button getBtnShowTime() {
    return btnShowTime;
  }

  /**
   * @return the btnShowValue
   */
  public Button getBtnShowValue() {
    return btnShowValue;
  }

  /**
   * @return the btnShowStatus
   */
  public Button getBtnShowStatus() {
    return btnShowStatus;
  }

  /**
   * @return the btnShowPriority
   */
  public Button getBtnShowPriority() {
    return btnShowPriority;
  }

  /**
   * @return the btnShowDate
   */
  public Button getBtnShowDate() {
    return btnShowDate;
  }

  /**
   * Selects and activates buttons
   * 
   * @param selected true if check buttons of the phases should be selected and activated
   */
  public void selectAndActivate(boolean selected) {
    this.btnShowCode.setSelection(selected);
    this.btnShowCodeReview.setSelection(selected);
    this.btnShowCompile.setSelection(selected);
    this.btnShowDesign.setSelection(selected);
    this.btnShowDesignReview.setSelection(selected);
    this.btnShowPlanning.setSelection(selected);
    this.btnShowPostmortem.setSelection(selected);
    this.btnShowTest.setSelection(selected);

    this.btnShowCode.setEnabled(!selected);
    this.btnShowCodeReview.setEnabled(!selected);
    this.btnShowCompile.setEnabled(!selected);
    this.btnShowDesign.setEnabled(!selected);
    this.btnShowDesignReview.setEnabled(!selected);
    this.btnShowPlanning.setEnabled(!selected);
    this.btnShowPostmortem.setEnabled(!selected);
    this.btnShowTest.setEnabled(!selected);
  }
}
