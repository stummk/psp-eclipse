package de.uni.bremen.stummk.psp.test.calculation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.uni.bremen.stummk.psp.calculation.Calc;
import de.uni.bremen.stummk.psp.calculation.Manager;
import de.uni.bremen.stummk.psp.calculation.MathCalc;
import de.uni.bremen.stummk.psp.data.DefectRecord;
import de.uni.bremen.stummk.psp.data.DefectType;
import de.uni.bremen.stummk.psp.data.PSPProject;
import de.uni.bremen.stummk.psp.data.Phase;
import de.uni.bremen.stummk.psp.data.Project;
import de.uni.bremen.stummk.psp.data.ProjectPlanSummary;
import de.uni.bremen.stummk.psp.data.ScheduleEntry;
import de.uni.bremen.stummk.psp.data.Task;
import de.uni.bremen.stummk.psp.data.TimeRecord;
import de.uni.bremen.stummk.psp.utility.Constants;
import de.uni.bremen.stummk.psp.utility.DBConnection;
import de.uni.bremen.stummk.psp.utility.DatasetException;
import de.uni.bremen.stummk.psp.utility.PSPCSVParser;

/**
 * Class, that tests the Calculation operations
 * 
 * @author Konstantin
 *
 */
public class CalcTest {
  private static Calc classUnderTest;
  private static final String projectName = "TESTPROJECT";
  private static final long totalPlannedHours = 0;
  private static final String taskName = "TASK_NAME";
  private static final Phase phase = Phase.POSTMORTEM;
  private static final DefectType type = DefectType.DATA;
  private static final long timeValue = 600;
  private static final long one = 1;
  private static final LocalDate date = LocalDate.now();
  private static final LocalTime time = LocalTime.now();
  private static final LocalTime endTime = time.plusSeconds(timeValue);


  @BeforeClass
  public static void setUp() {
    // initialize the database
    classUnderTest = Calc.getInstance();
    assertNotNull(classUnderTest);

    DBConnection.init();

    if (Manager.getInstance().containsProject(projectName)) {
      Manager.getInstance().deleteCompleteProject(projectName);
    }
  }

  @Before
  public void init() {
    // load project from test file and save to db
    PSPProject psp = loadTestData();
    Manager.getInstance().saveBackupProject(psp);
    assertEquals(psp.getProject().getProjectName(),
        Manager.getInstance().loadBackupProject(projectName).getProject().getProjectName());
  }

  /**
   * Tests the total planned hours method of the Calc class
   * 
   * @throws DatasetException
   */
  @Test
  public void testGetTotalPlannedHour() throws DatasetException {
    assertEquals(totalPlannedHours, classUnderTest.getTotalPlannedHours(projectName));
  }

  /**
   * Test-Case: Update the planned values in the summary
   * 
   * @throws DatasetException
   */
  @Test
  public void testUpdatePlanValues() throws DatasetException {
    try {
      classUnderTest.updatePlannedValues(projectName);
      int count = 0;
      for (Task task : Manager.getInstance().getTaskPlanning(projectName)) {
        assertEquals(MathCalc.getInstance().divide(100 * task.getPlanTime(), totalPlannedHours), task.getPlanValue(),
            0.0);
        assertEquals(count + task.getPlanTime(), task.getPlannedCumulativeHours());
        count += task.getPlanTime();
      }
    } catch (IllegalStateException e) {

    }
  }

  /**
   * Tests the update methods for the Project Plan Summary of the Calc class
   */
  @Test
  public void testUpdateSummary() {
    try {
      ProjectPlanSummary pps = Manager.getInstance().getProjectSummary(projectName);
      double planTime = pps.get(Constants.KEY_TIME_IN_PHASE_IDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_PLAN);
      double totalTime = pps.get(Constants.KEY_TIME_IN_PHASE_IDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_ACTUAL);
      double postmortemTime =
          pps.get(Constants.KEY_TIME_IN_PHASE_IDX, Constants.KEY_PHASE_POSTMORTEM, Constants.KEY_ACTUAL);
      double defInj = pps.get(Constants.KEY_DEFECTS_INJECTED_IDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_ACTUAL);
      double defInjPostmortem =
          pps.get(Constants.KEY_DEFECTS_INJECTED_IDX, Constants.KEY_PHASE_POSTMORTEM, Constants.KEY_ACTUAL);
      double defRmd = pps.get(Constants.KEY_DEFECTS_REMOVED_IDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_ACTUAL);
      double defRmdPostmortem =
          pps.get(Constants.KEY_DEFECTS_REMOVED_IDX, Constants.KEY_PHASE_POSTMORTEM, Constants.KEY_ACTUAL);
      ProjectPlanSummary result = null;

      Project p = pps.getProject();
      Task task = new Task(taskName, 0, phase, timeValue, date, 0, 0, one, p, Constants.TASK_TYPE_TASK);
      TimeRecord timeRec = new TimeRecord(date, date, time, endTime, 0, phase, "", p);
      DefectRecord defectRec = new DefectRecord(1, date, type, phase, phase, 0, "", p, "");

      Manager.getInstance().saveToDB(task);
      classUnderTest.addSummaryValues(task, projectName);
      Manager.getInstance().saveToDB(timeRec);
      classUnderTest.addSummaryValue(timeRec, projectName);
      Manager.getInstance().saveToDB(defectRec);
      classUnderTest.addSummaryValue(defectRec, projectName);

      // test by adding new values to the summary
      result = Manager.getInstance().getProjectSummary(projectName);
      assertEquals(planTime + timeValue,
          result.get(Constants.KEY_TIME_IN_PHASE_IDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_PLAN), 0.0);
      assertEquals(totalTime + timeValue,
          result.get(Constants.KEY_TIME_IN_PHASE_IDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_ACTUAL), 0.0);
      assertEquals(postmortemTime + timeValue,
          result.get(Constants.KEY_TIME_IN_PHASE_IDX, Constants.KEY_PHASE_POSTMORTEM, Constants.KEY_ACTUAL), 0.0);
      assertEquals(defInj + one,
          result.get(Constants.KEY_DEFECTS_INJECTED_IDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_ACTUAL), 0.0);
      assertEquals(defInjPostmortem + one,
          result.get(Constants.KEY_DEFECTS_INJECTED_IDX, Constants.KEY_PHASE_POSTMORTEM, Constants.KEY_ACTUAL), 0.0);
      assertEquals(defRmd + one,
          result.get(Constants.KEY_DEFECTS_REMOVED_IDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_ACTUAL), 0.0);
      assertEquals(defRmdPostmortem + one,
          result.get(Constants.KEY_DEFECTS_REMOVED_IDX, Constants.KEY_PHASE_POSTMORTEM, Constants.KEY_ACTUAL), 0.0);

      // test by subtract values from the summary
      classUnderTest.subSummaryValue(defectRec, projectName);
      Manager.getInstance().delete(defectRec);
      classUnderTest.subSummaryValue(task, projectName);
      Manager.getInstance().delete(task);
      classUnderTest.subSummaryValue(timeRec, projectName);
      Manager.getInstance().delete(timeRec);

      result = Manager.getInstance().getProjectSummary(projectName);
      assertEquals(planTime, result.get(Constants.KEY_TIME_IN_PHASE_IDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_PLAN),
          0.0);
      assertEquals(totalTime,
          result.get(Constants.KEY_TIME_IN_PHASE_IDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_ACTUAL), 0.0);
      assertEquals(postmortemTime,
          result.get(Constants.KEY_TIME_IN_PHASE_IDX, Constants.KEY_PHASE_POSTMORTEM, Constants.KEY_ACTUAL), 0.0);
      assertEquals(defInj,
          result.get(Constants.KEY_DEFECTS_INJECTED_IDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_ACTUAL), 0.0);
      assertEquals(defInjPostmortem,
          result.get(Constants.KEY_DEFECTS_INJECTED_IDX, Constants.KEY_PHASE_POSTMORTEM, Constants.KEY_ACTUAL), 0.0);
      assertEquals(defRmd,
          result.get(Constants.KEY_DEFECTS_REMOVED_IDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_ACTUAL), 0.0);
      assertEquals(defRmdPostmortem,
          result.get(Constants.KEY_DEFECTS_REMOVED_IDX, Constants.KEY_PHASE_POSTMORTEM, Constants.KEY_ACTUAL), 0.0);
    } catch (IllegalStateException e) {

    }
  }

  /**
   * Tests the update method of the schedule entry in the Calc class
   */
  @Test
  public void testScheduleUpdate() {
    try {
      Project p = Manager.getInstance().getProjectByName(projectName);
      long weeks = ChronoUnit.WEEKS.between(p.getTimestamp(), p.getTimestamp());
      List<ScheduleEntry> se = Manager.getInstance().getSchedulePlanByWeek(projectName, weeks);
      ScheduleEntry entry = null;
      if (se.size() == 1) {
        entry = se.get(0);
      }

      long planHour = entry.getPlannedHours();
      long actualHour = entry.getActualDirectHours();
      Task task = new Task(taskName, 0, phase, timeValue, p.getTimestamp(), 0, 0, one, p, Constants.TASK_TYPE_TASK);
      TimeRecord timeRec = new TimeRecord(p.getTimestamp(), p.getTimestamp(), time, endTime, 0, phase, "", p);

      // add new values
      Manager.getInstance().saveToDB(task);
      classUnderTest.createOrUpdateSchedule(p, task);
      Manager.getInstance().saveToDB(timeRec);
      classUnderTest.updateScheduleActualHour(p, timeRec);

      List<ScheduleEntry> result = Manager.getInstance().getSchedulePlanByWeek(projectName, weeks);
      ScheduleEntry entryResult = null;
      if (result.size() == 1) {
        entryResult = result.get(0);
      }

      assertEquals(actualHour + timeValue, entryResult.getActualDirectHours());
      assertEquals(timeValue, entryResult.getPlannedHours());

      // delete values from schedule plan
      classUnderTest.deletePlanHoursFromSchedule(p, task);
      classUnderTest.deleteActualHoursFromSchedule(p, timeRec);
      Manager.getInstance().delete(task);
      Manager.getInstance().delete(timeRec);
      result = Manager.getInstance().getSchedulePlanByWeek(projectName, weeks);
      entryResult = null;
      if (result.size() == 1) {
        entryResult = result.get(0);
      }

      assertEquals(actualHour, entryResult.getActualDirectHours());
      assertEquals(planHour, entryResult.getPlannedHours());
    } catch (IllegalStateException e) {

    }

  }

  @After
  public void cleanUp() {
    // delete all tables from project from db
    Manager.getInstance().deleteCompleteProject(projectName);
  }

  private static PSPProject loadTestData() {
    File file = new File("psp.csv");
    PSPProject psp = null;

    try {
      // read data from file and generate the PSP Project data
      InputStream stream = new FileInputStream(file);

      byte[] b = new byte[stream.available()];
      stream.read(b);
      psp = PSPCSVParser.read(new StringReader(new String(b)));
      stream.close();
    } catch (IOException e) {
      e.printStackTrace();
    }
    return psp;
  }

}
