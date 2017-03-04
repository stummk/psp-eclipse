package de.uni.bremen.stummk.psp.data;

import de.uni.bremen.stummk.psp.utility.Constants;

/**
 * Enum of phases in one project
 * 
 * @author Konstantin
 *
 */
public enum Phase {

  PLANNING(Constants.KEY_PHASE_PLANNING), DESIGN(Constants.KEY_PHASE_DESIGN), DESIGNREVIEW(
      Constants.KEY_PHASE_DESIGN_REVIEW), CODE(Constants.KEY_PHASE_CODE), CODEREVIEW(Constants.KEY_PHASE_CODE_REVIEW), COMPILE(
          Constants.KEY_PHASE_COMPILE), TEST(Constants.KEY_PHASE_TEST), POSTMORTEM(Constants.KEY_PHASE_POSTMORTEM);
  private String phase;

  /**
   * @param type the defect Type <br>
   *        {@link Constants.KEY_PHASE_PLANNING} <br>
   *        {@link Constants.KEY_PHASE_DESIGN} <br>
   *        {@link Constants.KEY_PHASE_DESIGN_REVIEW} <br>
   *        {@link Constants.KEY_PHASE_CODE} <br>
   *        {@link Constants.KEY_PHASE_CODE_REVIEW} <br>
   *        {@link Constants.KEY_PHASE_COMPILE} <br>
   *        {@link Constants.KEY_PHASE_TEST} <br>
   *        {@link Constants.KEY_PHASE_POSTMORTEM} <br>
   */
  Phase(String phase) {
    this.phase = phase;
  }

  @Override
  public String toString() {
    switch (this) {
      case PLANNING:
        return "Planning";
      case DESIGN:
        return "Design";
      case DESIGNREVIEW:
        return "Design Review";
      case CODE:
        return "Code";
      case CODEREVIEW:
        return "Code Review";
      case COMPILE:
        return "Compile";
      case TEST:
        return "Test";
      case POSTMORTEM:
        return "Postmortem";
      default:
        return "";
    }
  }

  /**
   * @return the String Values of the phase enums
   */
  public static String[] getValues() {
    return new String[] {PLANNING.phase, DESIGN.phase, DESIGNREVIEW.phase, CODE.phase, CODEREVIEW.phase, COMPILE.phase,
        TEST.phase, POSTMORTEM.phase};
  }

  /**
   * @param value the string value of a phase
   * @return the index of a phase
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
   * @param text the string value of a phase
   * @return the Phase of a string value
   */
  public static Phase fromString(String text) {
    if (text != null) {
      for (Phase p : Phase.values()) {
        if (text.equals(p.phase)) {
          return p;
        }
      }
    }
    return null;
  }

  /**
   * @param phase Phase
   * @return the String value of a Phase
   */
  public static String getValue(Phase phase) {
    if (phase == null) {
      return "";
    }
    switch (phase) {
      case PLANNING:
        return Constants.KEY_PHASE_PLANNING;
      case DESIGN:
        return Constants.KEY_PHASE_DESIGN;
      case DESIGNREVIEW:
        return Constants.KEY_PHASE_DESIGN_REVIEW;
      case CODE:
        return Constants.KEY_PHASE_CODE;
      case CODEREVIEW:
        return Constants.KEY_PHASE_CODE_REVIEW;
      case COMPILE:
        return Constants.KEY_PHASE_COMPILE;
      case TEST:
        return Constants.KEY_PHASE_TEST;
      case POSTMORTEM:
        return Constants.KEY_PHASE_POSTMORTEM;
    }
    return "";
  }
}
