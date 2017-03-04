package de.uni.bremen.stummk.psp.utility;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.CoreException;

/**
 * creates a hash of one file
 * 
 * @author Konstantin
 *
 */
public class FileHash {

  /**
   * Hashes the content of a given file
   * 
   * @param file the file ,which content will be hashed
   * @return hash of conetnt of file
   */
  public static String hash(IFile file) {
    StringBuffer hexString = null;
    try {
      // read data from file and generate the PSP Project data
      InputStream stream = null;
      MessageDigest md = MessageDigest.getInstance("SHA-256");
      if (file.exists()) {
        stream = file.getContents();
        byte[] b = new byte[stream.available()];
        stream.read(b);
        md.update(b);
        byte[] mdbytes = md.digest();

        hexString = new StringBuffer();
        for (int i = 0; i < mdbytes.length; i++) {
          hexString.append(Integer.toHexString(0xFF & mdbytes[i]));
        }
      }
      stream.close();
    } catch (IOException | CoreException | NoSuchAlgorithmException e) {
      e.printStackTrace();
    }

    return hexString.toString();
  }
}
