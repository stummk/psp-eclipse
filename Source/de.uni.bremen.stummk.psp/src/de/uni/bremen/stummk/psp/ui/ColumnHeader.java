package de.uni.bremen.stummk.psp.ui;

import de.uni.bremen.stummk.psp.utility.Constants;

/**
 * Includes all column headers of the columns for the grid
 * 
 * @author Konstantin
 *
 */
public class ColumnHeader {

  private static final int NUM_COLUMN_WIDTH = 80;
  private static final int NUM_COLUMN_BIG_WIDTH = 100;
  private static final int MEDIUM_COLUMN_WIDTH = 110;
  private static final int LARGE_COLUMN_WIDTH = 120;
  private static final int TXT_COLUMN_WIDTH = 150;
  private static final int MEDIUM_TXT_COLUMN_WIDTH = 200;
  private static final int BIG_TXT_COLUMN_WIDTH = 300;

  /**
   * Header name of the Task Overview Table for Column "Phase"
   */
  public static final String HEADER_TO_PHASE = "Phase";

  /**
   * Header name of the Task Overview Table for Column "Task Name"
   */
  public static final String HEADER_TO_NAME = "Task Name";

  /**
   * Header name of the Task Overview Table for Column "Priority"
   */
  public static final String HEADER_TO_PRIORITY = "Priority";

  /**
   * Header name of the Task Overview Table for Column "Plan Min."
   */
  public static final String HEADER_TO_PLAN_MIN = "Plan Min.";

  /**
   * Header name of the Task Overview Table for Column "Actual Min."
   */
  public static final String HEADER_TO_ACTUAL_MIN = "Actual Min.";

  /**
   * Header name of the Task Overview Table for Column "Plan Earned Value"
   */
  public static final String HEADER_TO_PLAN_VALUE = "Plan Earned Value";

  /**
   * Header name of the Task Overview Table for Column "Actual Earned Value"
   */
  public static final String HEADER_TO_ACTUAL_VALUE = "Actual Earned Value";

  /**
   * Header name of the Task Overview Table for Column "Status"
   */
  public static final String HEADER_TO_STATUS = "Status";

  /**
   * Header name of the Task Overview Table for Column "Last Change Date"
   */
  public static final String HEADER_TO_CHANGE_DATE = "Last Change Date";

  /**
   * Column-Headers for task overview
   */
  public static final Header[] TASK_OVERVIEW_HEADER =
      {new Column<String>(HEADER_TO_PHASE, NUM_COLUMN_BIG_WIDTH, true, String.class),
          new Column<String>(HEADER_TO_NAME, TXT_COLUMN_WIDTH, String.class),
          new Column<Integer>(HEADER_TO_PRIORITY, NUM_COLUMN_WIDTH, Integer.class),
          new Column<Long>(HEADER_TO_PLAN_MIN, NUM_COLUMN_WIDTH, Long.class),
          new Column<Long>(HEADER_TO_ACTUAL_MIN, NUM_COLUMN_WIDTH, Long.class),
          new Column<Double>(HEADER_TO_PLAN_VALUE, MEDIUM_COLUMN_WIDTH, Double.class),
          new Column<Double>(HEADER_TO_ACTUAL_VALUE, LARGE_COLUMN_WIDTH, Double.class),
          new Column<String>(HEADER_TO_STATUS, NUM_COLUMN_WIDTH, String.class),
          new Column<String>(HEADER_TO_CHANGE_DATE, TXT_COLUMN_WIDTH, String.class)};

  /**
   * Column-Headers for pip
   */
  public static final Header[] PIP_HEADER = {new Column<Long>("PIP Number", NUM_COLUMN_WIDTH, Long.class),
      new Column<String>("Problem Description", TXT_COLUMN_WIDTH, String.class),
      new Column<String>("Proposal Description", TXT_COLUMN_WIDTH, String.class),
      new Column<String>("Description", BIG_TXT_COLUMN_WIDTH, String.class)};

  /**
   * Column-Headers for the time recording log
   */
  public static final Header[] TIME_RECORDING_HEADER = {new Column<String>("Date", NUM_COLUMN_BIG_WIDTH, String.class),
      new Column<String>("StartTime", NUM_COLUMN_WIDTH, String.class),
      new Column<String>("Stoptime", NUM_COLUMN_WIDTH, String.class),
      new Column<Long>("Interrupt Min.", NUM_COLUMN_BIG_WIDTH, Long.class),
      new Column<Long>("Delta Time (min.)", MEDIUM_COLUMN_WIDTH, Long.class),
      new Column<String>("Phase", NUM_COLUMN_BIG_WIDTH, String.class),
      new Column<String>("Comments", TXT_COLUMN_WIDTH, String.class)};

  /**
   * Column-Headers for the test report
   */
  public static final Header[] TEST_REPORT_HEADER = {new Column<String>("Test Name", TXT_COLUMN_WIDTH, String.class),
      new Column<String>("Expected Result", TXT_COLUMN_WIDTH, String.class),
      new Column<String>("Actual Result", TXT_COLUMN_WIDTH, String.class)};

  /**
   * Column-Headers for the task planning template
   */
  public static final Header[] TASK_PLANNING_HEADER = {new Column<String>("Task Name", TXT_COLUMN_WIDTH, String.class),
      new Column<String>("Plan Hours", NUM_COLUMN_BIG_WIDTH, String.class),
      new Column<Double>("Plan Value", NUM_COLUMN_WIDTH, Double.class),
      new Column<String>("Plan Cumulative Hours", TXT_COLUMN_WIDTH, String.class),
      new Column<Double>("Plan Cumulative Value", TXT_COLUMN_WIDTH, Double.class),
      new Column<String>("Plan Finish Date", NUM_COLUMN_BIG_WIDTH, String.class),
      new Column<String>("Actual Finish Date", LARGE_COLUMN_WIDTH, String.class),
      new Column<Double>("Actual Earned Value", LARGE_COLUMN_WIDTH, Double.class),
      new Column<Double>("Actual Cumulative Earned Value", MEDIUM_TXT_COLUMN_WIDTH, Double.class)};

  /**
   * Column-Headers for the schedule planning template
   */
  public static final Header[] SCHEDULE_PLANNING_HEADER =
      {new Column<Long>("Week Number", NUM_COLUMN_BIG_WIDTH, Long.class),
          new Column<String>("Monday of Week", MEDIUM_COLUMN_WIDTH, String.class),
          new Column<String>("Plan Direct Hours", MEDIUM_COLUMN_WIDTH, String.class),
          new Column<String>("Plan Cumulative Hours", TXT_COLUMN_WIDTH, String.class),
          new Column<Double>("Plan Cumulative Value", TXT_COLUMN_WIDTH, Double.class),
          new Column<String>("Actual Direct Hours", LARGE_COLUMN_WIDTH, String.class),
          new Column<String>("Actual Cumulative Hours", TXT_COLUMN_WIDTH, String.class),
          new Column<Double>("Actual Cumulative Earned Value", MEDIUM_TXT_COLUMN_WIDTH, Double.class)};

  /**
   * Column-Headers for the defect recording log
   */
  public static final Header[] DEFECT_RECORDING_HEADER =
      {new Column<String>("Date", NUM_COLUMN_BIG_WIDTH, String.class),
          new Column<Integer>("Number", NUM_COLUMN_WIDTH, Integer.class),
          new Column<String>("Type", NUM_COLUMN_BIG_WIDTH, String.class),
          new Column<String>("Inject", NUM_COLUMN_BIG_WIDTH, String.class),
          new Column<String>("Remove", NUM_COLUMN_BIG_WIDTH, String.class),
          new Column<Long>("Fix Time", NUM_COLUMN_WIDTH, Long.class),
          new Column<String>("Description", BIG_TXT_COLUMN_WIDTH, String.class)};

  /**
   * Column-Headers for the summary part of the project plan summary
   */
  public static final Header[] SUMMARY_HEADER =
      {new Column<String>(Constants.KEY_SUMMARY, TXT_COLUMN_WIDTH, String.class),
          new Column<Double>(Constants.KEY_PLAN, NUM_COLUMN_WIDTH, Double.class),
          new Column<Double>(Constants.KEY_ACTUAL, NUM_COLUMN_WIDTH, Double.class),
          new Column<Double>(Constants.KEY_TO_DATE, NUM_COLUMN_WIDTH, Double.class)};

  /**
   * Column-Headers for the program size part of the project plan summary
   */
  public static final Header[] SIZE_HEADER =
      {new Column<String>(Constants.KEY_PROGRAM_SIZE, TXT_COLUMN_WIDTH, String.class),
          new Column<Double>(Constants.KEY_PLAN, NUM_COLUMN_WIDTH, Double.class),
          new Column<Double>(Constants.KEY_ACTUAL, NUM_COLUMN_WIDTH, Double.class),
          new Column<Double>(Constants.KEY_TO_DATE, NUM_COLUMN_WIDTH, Double.class)};

  /**
   * Column-Headers for the time in phase part of the project plan summary
   */
  public static final Header[] TIME_IN_PHASE_HEADER =
      {new Column<String>(Constants.KEY_TIME_IN_PHASE, TXT_COLUMN_WIDTH, String.class),
          new Column<Double>(Constants.KEY_PLAN, NUM_COLUMN_WIDTH, Double.class),
          new Column<Double>(Constants.KEY_ACTUAL, NUM_COLUMN_WIDTH, Double.class),
          new Column<Double>(Constants.KEY_TO_DATE, NUM_COLUMN_WIDTH, Double.class),
          new Column<Double>(Constants.KEY_TO_DATE_PER, NUM_COLUMN_WIDTH, Double.class)};

  /**
   * Column-Headers for the defect injected part of the project plan summary
   */
  public static final Header[] DEFECT_INJECTED_HEADER =
      {new Column<String>(Constants.KEY_DEFECTS_INJECTED, TXT_COLUMN_WIDTH, String.class),
          new Column<Double>(Constants.KEY_PLAN, NUM_COLUMN_WIDTH, Double.class),
          new Column<Double>(Constants.KEY_ACTUAL, NUM_COLUMN_WIDTH, Double.class),
          new Column<Double>(Constants.KEY_TO_DATE, NUM_COLUMN_WIDTH, Double.class),
          new Column<Double>(Constants.KEY_TO_DATE_PER, NUM_COLUMN_WIDTH, Double.class),
          new Column<Double>(Constants.KEY_DEFECT_PER_HOUR, NUM_COLUMN_WIDTH, Double.class)};

  /**
   * Column-Headers for the defect removed part of the project plan summary
   */
  public static final Header[] DEFECT_REMOVED_HEADER =
      {new Column<String>(Constants.KEY_DEFECTS_REMOVED, TXT_COLUMN_WIDTH, String.class),
          new Column<Double>(Constants.KEY_PLAN, NUM_COLUMN_WIDTH, Double.class),
          new Column<Double>(Constants.KEY_ACTUAL, NUM_COLUMN_WIDTH, Double.class),
          new Column<Double>(Constants.KEY_TO_DATE, NUM_COLUMN_WIDTH, Double.class),
          new Column<Double>(Constants.KEY_TO_DATE_PER, NUM_COLUMN_WIDTH, Double.class),
          new Column<Double>(Constants.KEY_DEFECT_PER_HOUR, NUM_COLUMN_WIDTH, Double.class)};
}
