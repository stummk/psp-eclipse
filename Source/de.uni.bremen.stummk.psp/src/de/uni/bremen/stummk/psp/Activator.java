package de.uni.bremen.stummk.psp;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import de.uni.bremen.stummk.psp.calculation.Manager;
import de.uni.bremen.stummk.psp.utility.CheckOperation;
import de.uni.bremen.stummk.psp.utility.DBConnection;
import de.uni.bremen.stummk.psp.utility.DataIO;
import de.uni.bremen.stummk.psp.utility.FileHash;
import de.uni.bremen.stummk.psp.utility.Constants;

/**
 * The activator class controls the plug-in life cycle
 * 
 * @author Konstantin
 */
public class Activator extends AbstractUIPlugin {

  // The plug-in ID
  public static final String PLUGIN_ID = "de.uni.bremen.stummk.psp";

  // The shared instance
  private static Activator plugin;

  /**
   * The constructor
   */
  public Activator() {
    // empty
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.BundleContext)
   */
  @Override
  public void start(BundleContext context) throws Exception {
    super.start(context);
    plugin = this;
    DBConnection.init();
    loadProjectData();
  }

  /*
   * (non-Javadoc)
   * 
   * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.BundleContext)
   */
  @Override
  public void stop(BundleContext context) throws Exception {
    saveProjectData();
    DBConnection.close();
    plugin = null;
    super.stop(context);
  }

  /**
   * Returns the shared instance
   *
   * @return the shared instance
   */
  public static Activator getDefault() {
    return plugin;
  }

  /**
   * Returns an image descriptor for the image file at the given plug-in relative path
   *
   * @param path the path
   * @return the image descriptor
   */
  public static ImageDescriptor getImageDescriptor(String path) {
    return imageDescriptorFromPlugin(PLUGIN_ID, path);
  }

  private void saveProjectData() throws CoreException, IOException {
    // Saves all made changes to psp files
    IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
    for (IProject p : projects) {
      p.refreshLocal(IResource.DEPTH_INFINITE, null);
      if (CheckOperation.getProjectFile(p) == null && Manager.getInstance().containsProject(p.getName())) {
        Manager.getInstance().deleteCompleteProject(p.getName());
      }
    }
  }

  private void loadProjectData() throws CoreException, FileNotFoundException {
    // Loads the PSP projects into workspace
    IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
    for (IProject p : projects) {
      p.refreshLocal(IResource.DEPTH_INFINITE, null);
      if (p.isOpen() && !CheckOperation.projectContainFile(p) && Manager.getInstance().containsProject(p.getName())) {
        Manager.getInstance().deleteCompleteProject(p.getProject().getName());
      }

      // if project is open check if file has been changed and load data into db if file has been
      // changed
      if (p.isOpen() && CheckOperation.projectContainFile(p)) {
        if (!Manager.getInstance().containsProject(p.getName())) {
          saveAndHash(p);
        } else {
          IFile file = CheckOperation.getProjectFile(p);
          String hash = FileHash.hash(file);
          if (file.getPersistentProperty(Constants.PROPERTY_HASH) == null
              || !file.getPersistentProperty(Constants.PROPERTY_HASH).equals(hash)) {
            saveAndHash(p);
          }
        }
      }
    }
  }

  private void saveAndHash(IProject project) {
    // saves data from file to db
    if (Manager.getInstance().containsProject(project.getName())) {
      Manager.getInstance().deleteCompleteProject(project.getName());
    }
    Manager.getInstance().saveBackupProject(DataIO.loadFromFile(project.getName()));
    IFile file = CheckOperation.getProjectFile(project);
    String hash = FileHash.hash(file);
    try {
      file.setPersistentProperty(Constants.PROPERTY_HASH, hash);
    } catch (CoreException e) {
      e.printStackTrace();
    }
  }
}
