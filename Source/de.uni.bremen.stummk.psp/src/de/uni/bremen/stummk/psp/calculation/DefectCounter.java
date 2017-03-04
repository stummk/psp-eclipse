package de.uni.bremen.stummk.psp.calculation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;

import de.uni.bremen.stummk.psp.data.DefectRecord;
import de.uni.bremen.stummk.psp.data.Phase;
import de.uni.bremen.stummk.psp.utility.CheckOperation;
import de.uni.bremen.stummk.psp.utility.Constants;

/**
 * Class implements function to count defects added to one resource
 * 
 * @author Konstantin
 *
 */
public class DefectCounter {

  /**
   * Counts the defects of one resource and a given phase
   * 
   * @param resource the resource of which the defects will be counted
   * @param phase the phase of the defect
   * @param phaseIndex if injected defects,than {@link Constants.KEY_DEFECTS_INJECTED_IDX}. if removed
   *        defects than {@link Constants.KEY_DEFECTS_REMOVED_IDX}
   * @return the sum of defects
   */
  public static long count(IResource resource, Phase phase, int phaseIndex) {
    long count = 0;
    List<IResource> resources = new ArrayList<IResource>();
    IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

    if (resource.getType() == IResource.FILE) {
      return countPhase(resource, phase, phaseIndex);
    } else if (resource.getType() == IResource.PROJECT) {
      return countPhase(resource, phase, phaseIndex);
    } else {

      CheckOperation.findMembers(resources, resource.getLocation(), root);

      for (IResource res : resources) {
        count += countPhase(res, phase, phaseIndex);
      }
    }

    return count;
  }

  private static long countPhase(IResource resource, Phase phase, int phaseIndex) {
    switch (phaseIndex) {
      // counts the defects of one resource
      case Constants.KEY_DEFECTS_INJECTED_IDX:
        // if no phase count total number of defects
        if (phase == null) {
          List<DefectRecord> rec =
              Manager.getInstance().getDefectRecord(resource.getProject().getName(), resource.getProjectRelativePath());
          if (rec != null) {
            return rec.stream().count();
          }
        } else {
          // get defects of the phase and count them
          List<DefectRecord> rec = Manager.getInstance().getDefectRecord(resource.getProject().getName(),
              resource.getProjectRelativePath(), phase, Constants.KEY_DEFECTS_INJECTED_IDX);
          if (rec != null) {
            return rec.stream().count();
          }
        }
        break;
      case Constants.KEY_DEFECTS_REMOVED_IDX:
        if (phase == null) {
          List<DefectRecord> rec =
              Manager.getInstance().getDefectRecord(resource.getProject().getName(), resource.getProjectRelativePath());
          if (rec != null) {
            rec = rec.stream().filter(v -> v.getRemovePhase() != null).collect(Collectors.toList());
            return rec.stream().count();
          }
        } else {
          List<DefectRecord> rec = Manager.getInstance().getDefectRecord(resource.getProject().getName(),
              resource.getProjectRelativePath(), phase, Constants.KEY_DEFECTS_REMOVED_IDX);
          if (rec != null) {
            return rec.stream().count();
          }
        }
        break;
    }

    return 0;
  }
}
