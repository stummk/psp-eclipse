package de.uni.bremen.stummk.psp.calculation;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;

import de.uni.bremen.stummk.psp.utility.Constants;

/**
 * Represents a counter of line of codes
 * 
 * @author Konstantin
 *
 */
public class LoCCounter {

  /**
   * Counts line of codes in the source files of a project
   * 
   * @param projectName the project name, the line of codes will be counted
   * @return number of line of codes
   */
  public static int count(String projectName) {
    int loc = 0;
    List<IResource> compileFiles = new ArrayList<IResource>();
    IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();
    IProject project = root.getProject(projectName);
    if (project != null) {

      findCompileFiles(compileFiles, project.getLocation(), root);

      for (IResource res : compileFiles) {
        loc += countLoC((IFile) res);
      }
    }

    return loc;

  }

  /**
   * Counts line of codes of one resource
   * 
   * @param resource the resource of which the line of code will be counted
   * @return number of line of codes
   */
  public static int count(IResource resource) {
    int loc = 0;
    List<IResource> compileFiles = new ArrayList<IResource>();
    IWorkspaceRoot root = ResourcesPlugin.getWorkspace().getRoot();

    if (isSourceFile(resource)) {
      return countLoC((IFile) resource);
    } else {
      findCompileFiles(compileFiles, resource.getLocation(), root);

      for (IResource res : compileFiles) {
        loc += countLoC((IFile) res);
      }
    }
    return loc;
  }

  private static int countLoC(IFile res) {
    // count the locs
    BufferedReader reader = null;
    try {
      reader = new BufferedReader(new FileReader(res.getLocation().toFile()));
      int lines = 0;
      String line;
      boolean isRubyComment = false;
      while ((line = reader.readLine()) != null) {
        // if ruby comment, skip counting until comment finished
        if (findSourceLanguage(res) == Constants.FILE_RUBY && line.trim().startsWith("=begin")) {
          isRubyComment = true;
          continue;
        }
        if (findSourceLanguage(res) == Constants.FILE_RUBY && line.trim().startsWith("=end")) {
          isRubyComment = false;
          continue;
        }
        if (findSourceLanguage(res) == Constants.FILE_RUBY && isRubyComment == true) {
          continue;
        }
        // increment lines
        if (isSourceLine(line, findSourceLanguage(res))) {
          lines++;
        }
      }
      return lines;
    } catch (Exception ex) {
      return -1;
    } finally {
      try {
        if (reader != null) {
          reader.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  private static boolean isSourceLine(String line, int fileType) {
    // exclude lines, such as comments, empty line etc.
    switch (fileType) {
      case Constants.FILE_JAVA:
        return !("".equals(line.trim()) || line.trim().startsWith("import") || line.trim().startsWith("package")
            || line.trim().startsWith("@") || line.trim().startsWith("//") || line.trim().startsWith("/*")
            || line.trim().startsWith("*/") || line.trim().startsWith("*") || line.trim().equals(");")
            || line.trim().equals("{") || line.trim().equals("}") || line.trim().equals("});"));
      case Constants.FILE_C:
        return !("".equals(line.trim()) || line.trim().startsWith("#include") || line.trim().startsWith("//")
            || line.trim().startsWith("/*") || line.trim().startsWith("*/") || line.trim().startsWith("*")
            || line.trim().equals(");") || line.trim().equals("{") || line.trim().equals("}")
            || line.trim().equals("});"));
      case Constants.FILE_RUBY:
        return !("".equals(line.trim()) || line.trim().startsWith("#") || line.trim().startsWith("@")
            || line.trim().startsWith("=begin") || line.trim().startsWith("=end"));
    }
    return false;
  }

  private static void findCompileFiles(List<IResource> compileFiles, IPath path, IWorkspaceRoot root) {
    // Searches the project in the workspace to find source files
    IContainer container = root.getContainerForLocation(path);
    if (!container.exists()) {
      return;
    }

    try {
      if (container != null) {
        IResource[] resources;
        resources = container.members();
        for (IResource res : resources) {
          if (isSourceFile(res)) {
            if (!contains(res, compileFiles)) {
              compileFiles.add(res);
            }
          }
          if (res.getType() == IResource.FOLDER) {
            IPath tempPath = res.getLocation();
            findCompileFiles(compileFiles, tempPath, root);
          }
        }
      }
    } catch (CoreException e) {
      e.printStackTrace();
    }
  }

  private static boolean contains(IResource res, List<IResource> compileFiles) {
    // checks if one file has been counted yet
    for (IResource resource : compileFiles) {
      if (resource.getName().equals(res.getName())) {
        return true;
      }
    }
    return false;
  }

  private static boolean isSourceFile(IResource resource) {
    // Checksif one file is a source file
    int sourceLanguage = findSourceLanguage(resource);
    return sourceLanguage == Constants.FILE_JAVA || sourceLanguage == Constants.FILE_C || sourceLanguage == Constants.FILE_RUBY
        || sourceLanguage == Constants.FILE_COMPILE;
  }

  private static int findSourceLanguage(IResource resource) {
    // checks which type of sorce one file is
    if ("java".equalsIgnoreCase(resource.getFileExtension())) {
      return Constants.FILE_JAVA;
    }

    if ("c".equalsIgnoreCase(resource.getFileExtension()) || "cc".equalsIgnoreCase(resource.getFileExtension())
        || "cpp".equalsIgnoreCase(resource.getFileExtension()) || "h".equalsIgnoreCase(resource.getFileExtension())
        || "hpp".equalsIgnoreCase(resource.getFileExtension())) {
      return Constants.FILE_C;
    }

    if ("rb".equalsIgnoreCase(resource.getFileExtension()) || "rbw".equalsIgnoreCase(resource.getFileExtension())) {
      return Constants.FILE_RUBY;
    }

    return -1;
  }
}
