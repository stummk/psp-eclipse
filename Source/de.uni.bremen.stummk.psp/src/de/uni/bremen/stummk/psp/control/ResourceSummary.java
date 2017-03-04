package de.uni.bremen.stummk.psp.control;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.part.ViewPart;

import de.uni.bremen.stummk.psp.calculation.DefectCounter;
import de.uni.bremen.stummk.psp.calculation.LoCCounter;
import de.uni.bremen.stummk.psp.calculation.Manager;
import de.uni.bremen.stummk.psp.data.Phase;
import de.uni.bremen.stummk.psp.ui.ResourceSummaryComposite;
import de.uni.bremen.stummk.psp.utility.CheckOperation;
import de.uni.bremen.stummk.psp.utility.Constants;

/**
 * View, which show information of one seleted resource
 * 
 * @author Konstantin
 *
 */
public class ResourceSummary extends ViewPart {
  private final String newLine = System.getProperty("line.separator");
  private ResourceSummaryComposite container;
  private ISelectionListener listener;

  /**
   * Cosntructor
   */
  public ResourceSummary() {
    listener = new ISelectionListener() {
      public void selectionChanged(IWorkbenchPart part, ISelection sel) {
        if (!(sel instanceof IStructuredSelection)) {
          return;
        }

        // get selection in the project explorer
        IProject p = CheckOperation.getProjectBySelection(sel);
        if (p != null && CheckOperation.projectContainFile(p)) {
          Manager.getInstance().getProjectByName(p.getName());


          if (sel instanceof IStructuredSelection) {
            Object firstElement = ((IStructuredSelection) sel).getFirstElement();
            IResource res = null;

            if (firstElement instanceof IAdaptable) {
              res = (IResource) (((IAdaptable) firstElement).getAdapter(IResource.class));
            } else if (firstElement instanceof IResource) {
              res = (IResource) firstElement;
            }

            if (res != null) {
              container.getInfoLabel().setText(setText(res));
              container.getInfoLabel().pack();

              // container.getFormToolkit().createLabel(container.getContainer(), setText(res));
            }
          }
        }
      }
    };
  }

  @Override
  public void createPartControl(Composite parent) {
    this.container = new ResourceSummaryComposite(parent, SWT.BORDER);
    getSite().getPage().addSelectionListener(listener);
  }

  /**
   * Sets the information of one resource to the view
   * 
   * @param res the resource
   * @return the information of one resource
   */
  public String setText(IResource res) {
    StringBuffer buf = new StringBuffer();
    buf.append("Resource : ");
    buf.append(res.getProject().getName() + "/");
    buf.append(res.getProjectRelativePath().toString() + newLine);
    buf.append("Defects Injected : ");
    buf.append("Planning - " + DefectCounter.count(res, Phase.PLANNING, Constants.KEY_DEFECTS_INJECTED_IDX) + ", ");
    buf.append("Design - " + DefectCounter.count(res, Phase.DESIGN, Constants.KEY_DEFECTS_INJECTED_IDX) + ", ");
    buf.append(
        "Design Review - " + DefectCounter.count(res, Phase.DESIGNREVIEW, Constants.KEY_DEFECTS_INJECTED_IDX) + ", ");
    buf.append("Code - " + DefectCounter.count(res, Phase.CODE, Constants.KEY_DEFECTS_INJECTED_IDX) + ", ");
    buf.append(
        "Code Review - " + DefectCounter.count(res, Phase.CODEREVIEW, Constants.KEY_DEFECTS_INJECTED_IDX) + ", ");
    buf.append("Compile - " + DefectCounter.count(res, Phase.COMPILE, Constants.KEY_DEFECTS_INJECTED_IDX) + ", ");
    buf.append("Test - " + DefectCounter.count(res, Phase.TEST, Constants.KEY_DEFECTS_INJECTED_IDX) + ", ");
    buf.append("Postmortem - " + DefectCounter.count(res, Phase.POSTMORTEM, Constants.KEY_DEFECTS_INJECTED_IDX) + ", ");
    buf.append("Total - " + DefectCounter.count(res, null, Constants.KEY_DEFECTS_INJECTED_IDX) + newLine);
    buf.append("Defects Removed : ");
    buf.append("Planning - " + DefectCounter.count(res, Phase.PLANNING, Constants.KEY_DEFECTS_REMOVED_IDX) + ", ");
    buf.append("Design - " + DefectCounter.count(res, Phase.DESIGN, Constants.KEY_DEFECTS_REMOVED_IDX) + ", ");
    buf.append(
        "Design Review - " + DefectCounter.count(res, Phase.DESIGNREVIEW, Constants.KEY_DEFECTS_REMOVED_IDX) + ", ");
    buf.append("Code - " + DefectCounter.count(res, Phase.CODE, Constants.KEY_DEFECTS_REMOVED_IDX) + ", ");
    buf.append("Code Review - " + DefectCounter.count(res, Phase.CODEREVIEW, Constants.KEY_DEFECTS_REMOVED_IDX) + ", ");
    buf.append("Compile - " + DefectCounter.count(res, Phase.COMPILE, Constants.KEY_DEFECTS_REMOVED_IDX) + ", ");
    buf.append("Test - " + DefectCounter.count(res, Phase.TEST, Constants.KEY_DEFECTS_REMOVED_IDX) + ", ");
    buf.append("Postmortem - " + DefectCounter.count(res, Phase.POSTMORTEM, Constants.KEY_DEFECTS_REMOVED_IDX) + ", ");
    buf.append("Total - " + DefectCounter.count(res, null, Constants.KEY_DEFECTS_REMOVED_IDX) + newLine);
    buf.append("Lines of Code - " + countLoc(res) + newLine);
    return buf.toString();
  }

  private long countLoc(IResource resource) {
    return LoCCounter.count(resource);
  }

  @Override
  public void setFocus() {}

  @Override
  public void dispose() {
    getSite().getPage().removeSelectionListener(listener);
    super.dispose();
  }

}
