package de.uni.bremen.stummk.psp.utility;

import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;

/**
 * This class contains operations on checking the workspace
 * 
 * @author Konstantin
 *
 */
public class CheckOperation {

  /**
   * Checks if project of given resource conatins a psp file
   * 
   * @param resource The Resource which project will be searched
   * @return true if porject conatins psp file, else false
   */
  public static boolean projectContainFile(IResource resource) {
    return getProjectFile(resource) != null ? true : false;
  }

  /**
   * Checks if the project of the given resource contains a psp file
   * 
   * @param resource the resource of project, which will be checked
   * @return the psp file if the project contains one psp file, null else
   */
  public static IFile getProjectFile(IResource resource) {
    try {
      if (resource.getProject().isOpen()) {
        for (IResource r : resource.getProject().members()) {
          if (r.getType() == IResource.FILE && r.getName().equals("psp.csv")) {
            return (IFile) r;
          }
        }
      }
    } catch (CoreException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Returns the Project of one selection
   * 
   * @param selection the selection of the resource in the package explorer
   * @return the project of one selection
   */
  public static IProject getProjectBySelection(ISelection selection) {
    if (selection instanceof IStructuredSelection) {
      Object firstElement = ((IStructuredSelection) selection).getFirstElement();
      IResource res = null;

      if (firstElement instanceof IAdaptable) {
        res = (IResource) (((IAdaptable) firstElement).getAdapter(IResource.class));
      } else if (firstElement instanceof IResource) {
        res = (IResource) firstElement;
      }

      return res != null ? res.getProject() : null;
    }
    return null;
  }

  /**
   * Gets all members of one resource
   * 
   * @param resourcesList the resources, which are members of a given resource
   * @param location the resource location, which will be searched for members
   * @param root the workspace root
   */
  public static void findMembers(List<IResource> resourcesList, IPath location, IWorkspaceRoot root) {
    IContainer container = root.getContainerForLocation(location);
    if (!container.exists()) {
      return;
    }

    // Checks if container is in resource list
    if (!contains(container, resourcesList)) {
      resourcesList.add(container);
    }
    try {
      if (container != null) {
        IResource[] resources;
        resources = container.members();
        for (IResource res : resources) {
          if (res.getType() == IResource.FOLDER) {
            // f resource is folder search the folder recursiv for mambers
            resourcesList.add(res);
            IPath tempPath = res.getLocation();
            findMembers(resourcesList, tempPath, root);
          } else {
            // add resource to resource list
            if (!contains(res, resourcesList)) {
              resourcesList.add(res);
            }
          }
        }
      }
    } catch (CoreException e) {
      e.printStackTrace();
    }
  }

  private static boolean contains(IResource res, List<IResource> resourcesList) {
    // checks if one file is in the resource list
    for (IResource resource : resourcesList) {
      if (resource.getName().equals(res.getName())) {
        return true;
      }
    }
    return false;
  }
}
