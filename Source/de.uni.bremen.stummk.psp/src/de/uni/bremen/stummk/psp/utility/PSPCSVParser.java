package de.uni.bremen.stummk.psp.utility;

import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import de.uni.bremen.stummk.psp.data.DefectRecord;
import de.uni.bremen.stummk.psp.data.DefectType;
import de.uni.bremen.stummk.psp.data.DefectsInjected;
import de.uni.bremen.stummk.psp.data.DefectsRemoved;
import de.uni.bremen.stummk.psp.data.PIP;
import de.uni.bremen.stummk.psp.data.PSPProject;
import de.uni.bremen.stummk.psp.data.Phase;
import de.uni.bremen.stummk.psp.data.ProgramSize;
import de.uni.bremen.stummk.psp.data.Project;
import de.uni.bremen.stummk.psp.data.ProjectPlanSummary;
import de.uni.bremen.stummk.psp.data.ScheduleEntry;
import de.uni.bremen.stummk.psp.data.Summary;
import de.uni.bremen.stummk.psp.data.SummaryItem;
import de.uni.bremen.stummk.psp.data.Task;
import de.uni.bremen.stummk.psp.data.TestReport;
import de.uni.bremen.stummk.psp.data.TimeInPhase;
import de.uni.bremen.stummk.psp.data.TimeRecord;

/**
 * Parser class, which parses the data of the psp project to a csv file or parses a csv file to a
 * psp project
 * 
 * @author Konstantin
 *
 */
public class PSPCSVParser {

  /**
   * Parses a {@link PSPProject} to a csv string
   * 
   * @param csvPrinter {@link CSVPrinter} which parses data to csv
   * @param project the {@link PSPProject} to be parsed
   */
  public static void write(CSVPrinter csvPrinter, PSPProject project) {
    try {
      // PROJECT
      csvPrinter.printRecord(CSVHeader.CSV_TABLE_PROJECT);
      csvPrinter.printRecord(CSVHeader.CSV_COLUMN_HEADER_PROJECT);
      csvPrinter.printRecord((Object[]) project.getProject().toString().split("#", -1));
      csvPrinter.println();

      // PROJECT PLAN SUMMARY
      csvPrinter.printRecord(CSVHeader.CSV_TABLE_PROJECT_PLAN_SUMMARY);
      csvPrinter.printRecord(project.getSummary().getID());
      csvPrinter.println();

      csvPrinter.printRecord(CSVHeader.CSV_TABLE_PPS_SUMMARY);
      csvPrinter.printRecord(CSVHeader.CSV_COLUMN_HEADER_PPS_SUMMARY);
      for (SummaryItem sum : project.getSummary().getSectionContent(Constants.KEY_SUMMARY_IDX)) {
        csvPrinter.printRecord((Object[]) sum.toString().split("#", -1));
      }
      csvPrinter.println();

      csvPrinter.printRecord(CSVHeader.CSV_TABLE_PPS_SIZE);
      csvPrinter.printRecord(CSVHeader.CSV_COLUMN_HEADER_PPS_SIZE);
      for (SummaryItem sum : project.getSummary().getSectionContent(Constants.KEY_PROGRAM_SIZE_IDX)) {
        csvPrinter.printRecord((Object[]) sum.toString().split("#", -1));
      }
      csvPrinter.println();

      csvPrinter.printRecord(CSVHeader.CSV_TABLE_PPS_TIME);
      csvPrinter.printRecord(CSVHeader.CSV_COLUMN_HEADER_PPS_TIME);
      for (SummaryItem sum : project.getSummary().getSectionContent(Constants.KEY_TIME_IN_PHASE_IDX)) {
        csvPrinter.printRecord((Object[]) sum.toString().split("#", -1));
      }
      csvPrinter.println();

      csvPrinter.printRecord(CSVHeader.CSV_TABLE_PPS_DEFECT_INJECTED);
      csvPrinter.printRecord(CSVHeader.CSV_COLUMN_HEADER_PPS_DEFECT_INJECTED);
      for (SummaryItem sum : project.getSummary().getSectionContent(Constants.KEY_DEFECTS_INJECTED_IDX)) {
        csvPrinter.printRecord((Object[]) sum.toString().split("#", -1));
      }
      csvPrinter.println();

      csvPrinter.printRecord(CSVHeader.CSV_TABLE_PPS_DEFECT_REMOVED);
      csvPrinter.printRecord(CSVHeader.CSV_COLUMN_HEADER_PPS_DEFECT_REMOVED);
      for (SummaryItem sum : project.getSummary().getSectionContent(Constants.KEY_DEFECTS_REMOVED_IDX)) {
        csvPrinter.printRecord((Object[]) sum.toString().split("#", -1));
      }
      csvPrinter.println();

      // TIME RECORDS
      csvPrinter.printRecord(CSVHeader.CSV_TABLE_TIME_RECORD);
      csvPrinter.printRecord(CSVHeader.CSV_COLUMN_HEADER_TIME_RECORD);
      for (TimeRecord rec : project.getTimeRecords()) {
        csvPrinter.printRecord((Object[]) rec.toString().split("#", -1));
      }
      csvPrinter.println();

      // DEFECT RECORDS
      csvPrinter.printRecord(CSVHeader.CSV_TABLE_DEFECT_RECORD);
      csvPrinter.printRecord(CSVHeader.CSV_COLUMN_HEADER_DEFECT_RECORD);
      for (DefectRecord rec : project.getDefectRecords()) {
        csvPrinter.printRecord((Object[]) rec.toString().split("#", -1));
      }
      csvPrinter.println();

      // PIP
      csvPrinter.printRecord(CSVHeader.CSV_TABLE_PIP);
      csvPrinter.printRecord(CSVHeader.CSV_COLUMN_HEADER_PIP);
      for (PIP rec : project.getPIP()) {
        csvPrinter.printRecord((Object[]) rec.toString().split("#", -1));
      }
      csvPrinter.println();

      // TEST REPORT
      csvPrinter.printRecord(CSVHeader.CSV_TABLE_TEST_REPORT);
      csvPrinter.printRecord(CSVHeader.CSV_COLUMN_HEADER_TEST_REPORT);
      for (TestReport rec : project.getTestReport()) {
        csvPrinter.printRecord((Object[]) rec.toString().split("#", -1));
      }
      csvPrinter.println();

      // TASKS
      csvPrinter.printRecord(CSVHeader.CSV_TABLE_TASK);
      csvPrinter.printRecord(CSVHeader.CSV_COLUMN_HEADER_TASK);
      for (Task rec : project.getTasks()) {
        csvPrinter.printRecord((Object[]) rec.toString().split("#", -1));
      }
      csvPrinter.println();

      // SCHEDULE PLAN
      csvPrinter.printRecord(CSVHeader.CSV_TABLE_SCHEDULE_PLAN);
      csvPrinter.printRecord(CSVHeader.CSV_COLUMN_HEADER_SCHEDULE_PLAN);
      for (ScheduleEntry rec : project.getScheduleEntries()) {
        csvPrinter.printRecord((Object[]) rec.toString().split("#", -1));
      }
      csvPrinter.println();

    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  /**
   * Parses a csv file to {@link PSPProject}
   * 
   * @param reader the String Reader, which reads the file into a string
   * @return parsed {@link PSPProject}
   */
  public static PSPProject read(StringReader reader) {
    CSVFormat csvFormat = CSVFormat.RFC4180;
    CSVParser csvParser = null;
    PSPProject pspProject = new PSPProject();
    Project project = null;
    ProjectPlanSummary pps = null;
    List<Summary> summaries = new ArrayList<>();
    List<ProgramSize> pSize = new ArrayList<>();
    List<TimeInPhase> tip = new ArrayList<>();
    List<DefectsInjected> di = new ArrayList<>();
    List<DefectsRemoved> dr = new ArrayList<>();
    Map<Long, List<Long>> taskToTime = new HashMap<>();
    List<TimeRecord> tr = new ArrayList<>();
    Map<Long, Long> taskToDefect = new HashMap<>();
    List<DefectRecord> dRec = new ArrayList<>();
    List<PIP> pip = new ArrayList<>();
    List<TestReport> tRep = new ArrayList<>();
    List<Task> t = new ArrayList<>();
    List<ScheduleEntry> se = new ArrayList<>();
    int count = 0;

    try {
      csvParser = new CSVParser(reader, csvFormat);
      List<CSVRecord> records = csvParser.getRecords();
      for (CSVRecord record : records) {
        if (isHeader(record)) {
          // if line is a header
          continue;
        }

        if (record.size() == 1 && record.get(0).isEmpty()) {
          // if empty line
          ++count;
        } else {
          switch (count) {
            case 0:
              // project
              project = new Project(Long.parseLong(record.get(0)), record.get(1), LocalDate.parse(record.get(2)));
              break;
            case 1:
              // projectplansummary
              pps = new ProjectPlanSummary(Long.parseLong(record.get(0)), project);
              break;
            case 2:
              summaries.add(new Summary(Long.parseLong(record.get(0)), record.get(1), Double.parseDouble(record.get(2)),
                  Double.parseDouble(record.get(3)), Double.parseDouble(record.get(4))));
              break;
            case 3:
              pSize.add(new ProgramSize(Long.parseLong(record.get(0)), record.get(1), Double.parseDouble(record.get(2)),
                  Double.parseDouble(record.get(3)), Double.parseDouble(record.get(4))));
              break;
            case 4:
              tip.add(new TimeInPhase(Long.parseLong(record.get(0)), record.get(1), Double.parseDouble(record.get(2)),
                  Double.parseDouble(record.get(3)), Double.parseDouble(record.get(4)),
                  Double.parseDouble(record.get(5))));
              break;
            case 5:
              di.add(
                  new DefectsInjected(Long.parseLong(record.get(0)), record.get(1), Double.parseDouble(record.get(2)),
                      Double.parseDouble(record.get(3)), Double.parseDouble(record.get(4)),
                      Double.parseDouble(record.get(5)), Double.parseDouble(record.get(6))));
              break;
            case 6:
              dr.add(new DefectsRemoved(Long.parseLong(record.get(0)), record.get(1), Double.parseDouble(record.get(2)),
                  Double.parseDouble(record.get(3)), Double.parseDouble(record.get(4)),
                  Double.parseDouble(record.get(5)), Double.parseDouble(record.get(6))));
              break;
            case 7:
              // time record
              tr.add(new TimeRecord(Long.parseLong(record.get(0)), LocalDate.parse(record.get(1)),
                  LocalDate.parse(record.get(2)), LocalTime.parse(record.get(3)), LocalTime.parse(record.get(4)),
                  Integer.parseInt(record.get(5)), Long.parseLong(record.get(6)), Phase.fromString(record.get(7)),
                  record.get(8), project));
              addToMap(taskToTime, Long.parseLong(record.get(0)), record.get(9));
              break;
            case 8:
              // defect records
              dRec.add(new DefectRecord(Long.parseLong(record.get(0)), Integer.parseInt(record.get(1)),
                  LocalDate.parse(record.get(2)), DefectType.fromString(record.get(3)), Phase.fromString(record.get(4)),
                  Phase.fromString(record.get(5)), Long.parseLong(record.get(6)), record.get(7), project,
                  Boolean.parseBoolean(record.get(8)), record.get(10)));
              addDefectToMap(taskToDefect, Long.parseLong(record.get(0)), record.get(9));
              break;
            case 9:
              // pip
              pip.add(new PIP(Long.parseLong(record.get(0)), record.get(1), record.get(2), record.get(3), project));
              break;
            case 10:
              // test report
              tRep.add(
                  new TestReport(Long.parseLong(record.get(0)), record.get(1), record.get(2), record.get(3), project));
              break;
            case 11:
              // tasks
              Task task = new Task(Long.parseLong(record.get(0)), record.get(1), Phase.fromString(record.get(2)),
                  Long.parseLong(record.get(3)), Double.parseDouble(record.get(4)), Long.parseLong(record.get(5)),
                  Double.parseDouble(record.get(6)), LocalDate.parse(record.get(7)), Long.parseLong(record.get(8)),
                  Long.parseLong(record.get(9)), Long.parseLong(record.get(10)), record.get(11),
                  Double.parseDouble(record.get(12)), Double.parseDouble(record.get(13)),
                  Long.parseLong(record.get(14)), Boolean.parseBoolean(record.get(15)), record.get(16), record.get(17),
                  Integer.parseInt(record.get(18)), record.get(19), project);
              t.add(task);
              putTaskToTimeRecord(task, taskToTime, tr);
              putTaskToDefectRecord(task, taskToDefect, dRec);
              break;
            case 12:
              // schedule plan
              se.add(new ScheduleEntry(Long.parseLong(record.get(0)), Integer.parseInt(record.get(1)),
                  LocalDate.parse(record.get(2)), Long.parseLong(record.get(3)), Long.parseLong(record.get(4)),
                  Double.parseDouble(record.get(5)), Long.parseLong(record.get(6)), Long.parseLong(record.get(7)),
                  Double.parseDouble(record.get(8)), project));
              break;
          }
        }
      }

      // add data to project plan summary
      pps.setSummaries(summaries);
      pps.setSize(pSize);
      pps.setTimeInPhase(tip);
      pps.setDefInj(di);
      pps.setDefRmd(dr);

      // add data to psp project
      pspProject.setProject(project);
      pspProject.setPPS(pps);
      pspProject.setTimeRec(tr);
      pspProject.setDefRec(dRec);
      pspProject.setPIP(pip);
      pspProject.setTestReport(tRep);
      pspProject.setTask(t);
      pspProject.setSchedule(se);

      reader.close();
      csvParser.close();
    } catch (IOException e) {
      e.printStackTrace();
    }

    return pspProject;
  }

  private static void putTaskToDefectRecord(Task task, Map<Long, Long> taskToDefect, List<DefectRecord> dRec) {
    // add tasks to defect records by id
    if (taskToDefect.containsKey(task.getID())) {
      for (DefectRecord rec : dRec) {
        if (rec.getID() == taskToDefect.get(task.getID())) {
          rec.setTask(task);
        }
      }
    }
  }

  private static void addDefectToMap(Map<Long, Long> taskToDefect, long parseLong, String string) {
    // adds defect record to map with task id as key
    if (!string.isEmpty()) {
      if (!taskToDefect.containsKey(Long.parseLong(string))) {
        taskToDefect.put(Long.parseLong(string), parseLong);
      }
    }
  }

  private static void putTaskToTimeRecord(Task t, Map<Long, List<Long>> taskToTime, List<TimeRecord> tr) {
    // add task to time records depending on task id as key
    if (taskToTime.containsKey(t.getID())) {
      List<Long> list = taskToTime.get(t.getID());
      for (TimeRecord rec : tr) {
        if (list.contains(rec.getID())) {
          rec.setTask(t);
        }
      }
    }
  }

  private static void addToMap(Map<Long, List<Long>> taskToTime, long parseLong, String string) {
    // add time records to map with task id as key
    if (!string.isEmpty()) {
      if (taskToTime.containsKey(Long.parseLong(string))) {
        taskToTime.get(Long.parseLong(string)).add(parseLong);
      } else {
        ArrayList<Long> list = new ArrayList<>();
        list.add(parseLong);
        taskToTime.put(Long.parseLong(string), list);
      }
    }
  }

  private static boolean isHeader(CSVRecord record) {
    // checks if line is a header
    String[] s = new String[record.size()];
    for (int i = 0; i < record.size(); ++i) {
      s[i] = record.get(i);
    }

    return Arrays.equals(s, CSVHeader.CSV_TABLE_DEFECT_RECORD) || Arrays.equals(s, CSVHeader.CSV_TABLE_PIP)
        || Arrays.equals(s, CSVHeader.CSV_TABLE_PPS_DEFECT_INJECTED)
        || Arrays.equals(s, CSVHeader.CSV_TABLE_PPS_DEFECT_REMOVED) || Arrays.equals(s, CSVHeader.CSV_TABLE_PPS_SIZE)
        || Arrays.equals(s, CSVHeader.CSV_TABLE_PPS_SUMMARY) || Arrays.equals(s, CSVHeader.CSV_TABLE_PPS_TIME)
        || Arrays.equals(s, CSVHeader.CSV_TABLE_PROJECT) || Arrays.equals(s, CSVHeader.CSV_TABLE_PROJECT_PLAN_SUMMARY)
        || Arrays.equals(s, CSVHeader.CSV_TABLE_SCHEDULE_PLAN) || Arrays.equals(s, CSVHeader.CSV_TABLE_TASK)
        || Arrays.equals(s, CSVHeader.CSV_TABLE_TEST_REPORT) || Arrays.equals(s, CSVHeader.CSV_TABLE_TIME_RECORD)
        || Arrays.equals(s, CSVHeader.CSV_COLUMN_HEADER_DEFECT_RECORD)
        || Arrays.equals(s, CSVHeader.CSV_COLUMN_HEADER_PIP)
        || Arrays.equals(s, CSVHeader.CSV_COLUMN_HEADER_PPS_DEFECT_INJECTED)
        || Arrays.equals(s, CSVHeader.CSV_COLUMN_HEADER_PPS_DEFECT_REMOVED)
        || Arrays.equals(s, CSVHeader.CSV_COLUMN_HEADER_PPS_SIZE)
        || Arrays.equals(s, CSVHeader.CSV_COLUMN_HEADER_PPS_SUMMARY)
        || Arrays.equals(s, CSVHeader.CSV_COLUMN_HEADER_PPS_TIME)
        || Arrays.equals(s, CSVHeader.CSV_COLUMN_HEADER_PROJECT)
        || Arrays.equals(s, CSVHeader.CSV_COLUMN_HEADER_SCHEDULE_PLAN)
        || Arrays.equals(s, CSVHeader.CSV_COLUMN_HEADER_TASK)
        || Arrays.equals(s, CSVHeader.CSV_COLUMN_HEADER_TEST_REPORT)
        || Arrays.equals(s, CSVHeader.CSV_COLUMN_HEADER_TIME_RECORD);
  }

}
