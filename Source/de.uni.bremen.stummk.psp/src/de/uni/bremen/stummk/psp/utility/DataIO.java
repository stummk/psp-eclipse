package de.uni.bremen.stummk.psp.utility;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;

import de.uni.bremen.stummk.psp.data.PSPProject;

/**
 * Handles input and output operations
 * 
 * @author Konstantin
 *
 */
public class DataIO {
  private static final String NEW_LINE = System.getProperty("line.separator");

  /**
   * Save PSP-Project data to a file
   *
   * @param project the Project name where the data will be saved
   * @param PSPProject The PSP-Project data
   * @param monitor The progress monitor, if one is desired or null
   */
  public static void saveToFile(String project, PSPProject PSPProject, IProgressMonitor monitor) {
    IProject p = ResourcesPlugin.getWorkspace().getRoot().getProject(project);
    IFile file = null;
    CSVPrinter csvPrinter = null;
    StringWriter csvStringWriter = new StringWriter();

    file = p.getFile(new Path("psp.csv"));
    CSVFormat csvFileFormat = CSVFormat.RFC4180.withRecordSeparator(NEW_LINE);
    try {
      csvPrinter = new CSVPrinter(csvStringWriter, csvFileFormat);
      PSPCSVParser.write(csvPrinter, PSPProject);
      InputStream stream = new ByteArrayInputStream(csvStringWriter.getBuffer().toString().getBytes());

      // create or update existing file
      if (file.exists()) {
        file.setContents(stream, true, true, monitor);
      } else {
        file.create(stream, true, monitor);
      }
      stream.close();
      csvStringWriter.flush();
      csvStringWriter.close();
      csvPrinter.close();
    } catch (IOException | CoreException e) {
      e.printStackTrace();
    }
  }

  /**
   * Load PSP-Project data from a file
   *
   * @param project the project name which data will be loaded
   * @return the psp project data
   */
  public static PSPProject loadFromFile(String project) {
    IProject p = ResourcesPlugin.getWorkspace().getRoot().getProject(project);
    final IFile file = p.getFile(new Path("psp.csv"));
    PSPProject pspProject = null;

    try {
      // read data from file and generate the PSP Project data
      InputStream stream = null;
      if (file.exists()) {
        stream = file.getContents();

        byte[] b = new byte[stream.available()];
        stream.read(b);
        pspProject = PSPCSVParser.read(new StringReader(new String(b)));
      }
      stream.close();
    } catch (IOException | CoreException e) {
      e.printStackTrace();
    }

    return pspProject;
  }
}
