package de.uni.bremen.stummk.psp.utility;

/**
 * Class contains the Header for the CSV-File
 * 
 * @author Konstantin
 *
 */
public class CSVHeader {

  // CSV FILE HEADER AND TABLE NAMES

  /**
   * Table name in csv file : project
   */
  public static final Object[] CSV_TABLE_PROJECT = {"Project"};

  /**
   * Table name in csv file : project plan summary
   */
  public static final Object[] CSV_TABLE_PROJECT_PLAN_SUMMARY = {"Project Plan Summary"};

  /**
   * Table name in csv file : pps summary
   */
  public static final Object[] CSV_TABLE_PPS_SUMMARY = {"PPS Summary"};

  /**
   * Table name in csv file : pps size
   */
  public static final Object[] CSV_TABLE_PPS_SIZE = {"PPS Program Size"};

  /**
   * Table name in csv file : pps time
   */
  public static final Object[] CSV_TABLE_PPS_TIME = {"PPS Time in Phase"};

  /**
   * Table name in csv file : pps defect injected
   */
  public static final Object[] CSV_TABLE_PPS_DEFECT_INJECTED = {"PPS Defects Injected"};

  /**
   * Table name in csv file : defect removed
   */
  public static final Object[] CSV_TABLE_PPS_DEFECT_REMOVED = {"PPS Defects Removed"};

  /**
   * Table name in csv file : pip
   */
  public static final Object[] CSV_TABLE_PIP = {"PIP"};

  /**
   * Table name in csv file : test report
   */
  public static final Object[] CSV_TABLE_TEST_REPORT = {"Test Report"};

  /**
   * Table name in csv file : defect records
   */
  public static final Object[] CSV_TABLE_DEFECT_RECORD = {"Defect Records"};

  /**
   * Table name in csv file : time records
   */
  public static final Object[] CSV_TABLE_TIME_RECORD = {"Time Records"};

  /**
   * Table name in csv file : tasks
   */
  public static final Object[] CSV_TABLE_TASK = {"Tasks"};

  /**
   * Table name in csv file : schedule plan
   */
  public static final Object[] CSV_TABLE_SCHEDULE_PLAN = {"Schedule Plan"};

  /**
   * Header of the project csv row
   */
  public static final Object[] CSV_COLUMN_HEADER_PROJECT = {"ID", "projectName", "timestamp"};

  /**
   * Header of the pps summary csv row
   */
  public static final Object[] CSV_COLUMN_HEADER_PPS_SUMMARY = {"ID", "title", "plan", "actual", "toDate"};

  /**
   * Header of the pps size csv row
   */
  public static final Object[] CSV_COLUMN_HEADER_PPS_SIZE = {"ID", "title", "plan", "actual", "toDate"};

  /**
   * Header of the pps time csv row
   */
  public static final Object[] CSV_COLUMN_HEADER_PPS_TIME =
      {"ID", "title", "plan", "actual", "toDate", "toDatePercentage", "taskID"};

  /**
   * Header of the pps defect injected csv row
   */
  public static final Object[] CSV_COLUMN_HEADER_PPS_DEFECT_INJECTED =
      {"ID", "title", "plan", "actual", "toDate", "toDatePercentage", "defPerHour"};

  /**
   * Header of the pps defect removed csv row
   */
  public static final Object[] CSV_COLUMN_HEADER_PPS_DEFECT_REMOVED =
      {"ID", "title", "plan", "actual", "toDate", "toDatePercentage", "defPerHour"};

  /**
   * Header of the pip csv row
   */
  public static final Object[] CSV_COLUMN_HEADER_PIP = {"ID", "problemDescription", "proposalDescription", "notes"};

  /**
   * Header of the test report csv row
   */
  public static final Object[] CSV_COLUMN_HEADER_TEST_REPORT = {"ID", "name", "expectedResult", "actualResult"};

  /**
   * Header of the defect record csv row
   */
  public static final Object[] CSV_COLUMN_HEADER_DEFECT_RECORD = {"ID", "number", "date", "type", "injectPhase",
      "removePhase", "fixTime", "description", "fixTimeManual", "taskID", "filePath"};

  /**
   * Header of the time record csv row
   */
  public static final Object[] CSV_COLUMN_HEADER_TIME_RECORD =
      {"ID", "date", "enddate", "starttime", "endtime", "interruptTime", "deltaTime", "phase", "comment"};

  /**
   * Header of the task csv row
   */
  public static final Object[] CSV_COLUMN_HEADER_TASK = {"ID", "name", "phase", "plannedTime", "plannedValue",
      "plannedCumulativeHours", "plannedCumulativeValues", "plannedFinishDate", "planLoc", "estimatedDefectInjected",
      "estimatedDefectRemoved", "actualDate", "actualEarnedValue", "actualCumulativeEarnedValue", "actualTime",
      "isComplete", "status", "lastChange", "priority", "type"};

  /**
   * Header of the schedule plan csv row
   */
  public static final Object[] CSV_COLUMN_HEADER_SCHEDULE_PLAN =
      {"ID", "weekNumber", "dateMonday", "plannedHours", "cumulativePlannedHours", "cumulativeEarnedValue",
          "actualDirectHours", "actualCumulativeHour", "actualEarnedValue"};


}
