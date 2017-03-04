package de.uni.bremen.stummk.psp.utility;

/**
 * Exception which is thrown by the persistence
 * 
 */
public class DatasetException extends Exception {

  /**
   * Serial ID
   */
  private static final long serialVersionUID = -7499912411622348365L;

  /**
   * Constructor
   * 
   * @param message Message of the Exception
   */
  public DatasetException(final String message) {
    super(message);
  }

  /**
   * Constructor
   * 
   * @param message message of the exception
   * @param e the thrown exception
   */
  public DatasetException(final String message, Exception e) {
    super(message, e);
  }

}
