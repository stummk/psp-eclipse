package de.uni.bremen.stummk.psp.control;


import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

import de.uni.bremen.stummk.psp.calculation.RepeatingJob;
import de.uni.bremen.stummk.psp.calculation.ToolbarController;
import de.uni.bremen.stummk.psp.utility.Constants;

/**
 * Represents an info Dialog of the {@link TaskOverview} Toolbar
 * 
 * @author Konstantin
 *
 */
public class InfoDialog extends Dialog {

  private String projectName, taskName, planMin, message;
  private long actualMin;
  private Label lblNewLabel_3;
  private RepeatingJob job = null;
  private ToolbarController controller;

  /**
   * Constructor, if task is running
   * 
   * @param parentShell the parent shell, where the dialog is called
   * @param projectName the name of the project of the running task
   * @param taskName the name of the running task
   * @param planMin the plan minutes of the running task
   * @param actualSec the actual running time of the running task in seconds
   */
  public InfoDialog(Shell parentShell, String projectName, String taskName, String planMin, long actualSec,
      ToolbarController contoller) {
    super(parentShell);
    this.projectName = projectName;
    this.taskName = taskName;
    this.planMin = planMin;
    this.actualMin = actualSec;
    this.controller = contoller;
  }

  /**
   * Constructor, if task is not running
   * 
   * @param parentShell the parent shell, where the dialog is called
   * @param message the message, which will be shown
   */
  public InfoDialog(Shell parentShell, String message) {
    super(parentShell);
    this.message = message;
  }

  @Override
  protected void configureShell(Shell newShell) {
    super.configureShell(newShell);
    newShell.setText("Info about current running Task");
  }

  @Override
  protected Control createDialogArea(Composite parent) {
    Composite content = (Composite) super.createDialogArea(parent);
    content.setLayout(new FillLayout());

    ScrolledComposite sc = new ScrolledComposite(content, SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);

    Composite container = new Composite(sc, SWT.BORDER);
    {
      TableWrapLayout twl_container = new TableWrapLayout();
      twl_container.numColumns = 2;
      container.setLayout(twl_container);
    }

    // Sets Data or show Message
    if (message != null) {
      Label lblMessage = new Label(container, SWT.BORDER);
      lblMessage.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
      lblMessage.setText(message);
    } else {
      setData(container);
    }

    sc.setContent(container);
    sc.setExpandHorizontal(true);
    sc.setExpandVertical(true);
    sc.setMinSize(container.computeSize(SWT.DEFAULT, SWT.DEFAULT));

    return parent;
  }

  @Override
  protected void createButtonsForButtonBar(Composite parent) {
    createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, false)
        .addSelectionListener(new SelectionAdapter() {

          @Override
          public void widgetSelected(SelectionEvent e) {
            if (job != null) {
              job.cancel();
            }
            super.widgetSelected(e);
          }
        });
  }

  @Override
  protected Point getInitialSize() {
    return new Point(200, 250);
  }

  private void setData(Composite container) {
    Label lblCurrentTask = new Label(container, SWT.NONE);
    lblCurrentTask.setText("Current Task:");

    Label lblNewLabel = new Label(container, SWT.NONE);
    lblNewLabel.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.TOP, 1, 1));
    lblNewLabel.setText(taskName);

    Label lblProject = new Label(container, SWT.NONE);
    lblProject.setText("Project:");

    Label lblNewLabel_1 = new Label(container, SWT.NONE);
    lblNewLabel_1.setText(projectName);

    Label lblPlannedTime = new Label(container, SWT.NONE);
    lblPlannedTime.setText("Planned Time (min):");

    Label lblNewLabel_2 = new Label(container, SWT.NONE);
    lblNewLabel_2.setText(planMin);

    Label lblActualTime = new Label(container, SWT.NONE);
    lblActualTime.setText("Actual Time:");

    lblNewLabel_3 = new Label(container, SWT.NONE);
    lblNewLabel_3.setText("000" + " : " + "00" + " : " + "00");
    drawCounting();

    job = new RepeatingJob("timer", 1000) {
      protected IStatus run(IProgressMonitor monitor) {

        Display.getDefault().syncExec(new Runnable() {

          @Override
          public void run() {
            if (controller != null && controller.getStatus() == Constants.STATUS_TASK_RUNNING) {
              actualMin++;
            }
            drawCounting();
          }
        });


        schedule(1000);
        return org.eclipse.core.runtime.Status.OK_STATUS;
      }
    };

    job.schedule();

  }


  /**
   * @return the message
   */
  public String getMessage() {
    return message;
  }

  /**
   * @return the project name
   */
  public String getProjectName() {
    return projectName;
  }


  /**
   * @return the task name
   */
  public String getTask() {
    return taskName;
  }


  /**
   * @return the plan time
   */
  public String getPlanTime() {
    return planMin;
  }


  /**
   * @return the actual minutes
   */
  public long getActualTime() {
    return actualMin;
  }

  private void drawCounting() {
    if (!lblNewLabel_3.isDisposed()) {
      lblNewLabel_3.setText(convertSecToString());
      lblNewLabel_3.pack();
    }
  }

  private String convertSecToString() {
    return TimeUnit.SECONDS.toHours(actualMin) + " : " + TimeUnit.SECONDS.toMinutes(actualMin) + " : "
        + (actualMin % 60);
  }


}
