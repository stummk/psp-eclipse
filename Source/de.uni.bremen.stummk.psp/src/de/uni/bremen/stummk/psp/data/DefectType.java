package de.uni.bremen.stummk.psp.data;

import de.uni.bremen.stummk.psp.utility.Constants;

/**
 * Enum with defect types
 * 
 * @author Konstantin
 *
 */
public enum DefectType {

  DOCUMENTATION(Constants.DEFECT_TYPE_DOCUMENTATION), SYNTAX(Constants.DEFECT_TYPE_Syntax), BUILD(
      Constants.DEFECT_TYPE_Build), ASSIGNMENT(Constants.DEFECT_TYPE_Assignment), INTERFACE(
          Constants.DEFECT_TYPE_Interface), CHECKING(Constants.DEFECT_TYPE_CHECKING), DATA(Constants.DEFECT_TYPE_DATA), FUNCTION(
              Constants.DEFECT_TYPE_FUNCTION), SYSTEM(
                  Constants.DEFECT_TYPE_SYSTEM), ENVIRONMENT(Constants.DEFECT_TYPE_ENVIRONMENT);

  private String type;

  /**
   * @param type the defect Type <br>
   *        {@link Constants.DEFECT_TYPE_DOCUMENTATION} <br>
   *        {@link Constants.DEFECT_TYPE_Syntax} <br>
   *        {@link Constants.DEFECT_TYPE_Build} <br>
   *        {@link Constants.DEFECT_TYPE_Assignment}<br>
   *        {@link Constants.DEFECT_TYPE_Interface}<br>
   *        {@link Constants.DEFECT_TYPE_CHECKING}<br>
   *        {@link Constants.DEFECT_TYPE_DATA}<br>
   *        {@link Constants.DEFECT_TYPE_FUNCTION}<br>
   *        {@link Constants.DEFECT_TYPE_SYSTEM}<br>
   *        {@link Constants.DEFECT_TYPE_ENVIRONMENT}<br>
   */
  DefectType(String type) {
    this.type = type;
  }

  @Override
  public String toString() {
    switch (this) {
      case DOCUMENTATION:
        return "Documentation";
      case SYNTAX:
        return "Syntax";
      case BUILD:
        return "Build, Package";
      case ASSIGNMENT:
        return "Assignment";
      case INTERFACE:
        return "Interface";
      case CHECKING:
        return "Checking";
      case DATA:
        return "Data";
      case FUNCTION:
        return "Function";
      case SYSTEM:
        return "System";
      case ENVIRONMENT:
        return "Environment";
      default:
        return "";
    }
  }

  /**
   * @return the String Values of the defect types
   */
  public static String[] getValues() {
    return new String[] {DOCUMENTATION.type, SYNTAX.type, BUILD.type, ASSIGNMENT.type, INTERFACE.type, CHECKING.type,
        DATA.type, FUNCTION.type, SYSTEM.type, ENVIRONMENT.type};
  }

  /**
   * @param value the string value of an defect type
   * @return the index of the defect type
   */
  public static int getIndexOfValue(String value) {
    for (int i = 0; i < getValues().length; ++i) {
      if (getValues()[i].equals(value)) {
        return i;
      }
    }
    return -1;
  }

  /**
   * @param text the string value of an defect type
   * @return the DefectType of a string value
   */
  public static DefectType fromString(String text) {
    if (text != null) {
      for (DefectType dt : DefectType.values()) {
        if (text.equals(dt.type)) {
          return dt;
        }
      }
    }
    return null;
  }

}
