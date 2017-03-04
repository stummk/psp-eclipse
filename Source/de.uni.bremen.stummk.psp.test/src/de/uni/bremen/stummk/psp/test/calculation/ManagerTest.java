package de.uni.bremen.stummk.psp.test.calculation;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.uni.bremen.stummk.psp.calculation.Manager;
import de.uni.bremen.stummk.psp.data.DefectRecord;
import de.uni.bremen.stummk.psp.data.DefectType;
import de.uni.bremen.stummk.psp.data.PSPProject;
import de.uni.bremen.stummk.psp.data.Phase;
import de.uni.bremen.stummk.psp.data.Project;
import de.uni.bremen.stummk.psp.data.ProjectPlanSummary;
import de.uni.bremen.stummk.psp.data.Task;
import de.uni.bremen.stummk.psp.data.TestReport;
import de.uni.bremen.stummk.psp.data.TimeRecord;
import de.uni.bremen.stummk.psp.utility.Constants;
import de.uni.bremen.stummk.psp.utility.DBConnection;
import de.uni.bremen.stummk.psp.utility.PSPCSVParser;

/**
 * Test classs which test the Manager.class. Only functions will be tested, which are not used by
 * the CalcTes
 * 
 * @author Konstantin
 *
 */
public class ManagerTest {
  private static Manager classUnderTest;
  private static final String projectName = "TESTPROJECT";
  private static final String notProjectName = "NOTAPROJECT";
  private static final String taskName = "TASK_NAME";
  private static final Phase phase = Phase.POSTMORTEM;
  private static final DefectType type = DefectType.DATA;
  private static final long timeValue = 600;
  private static final long one = 1;
  private static final int plusMinute = 10;
  private static final LocalDate date = LocalDate.now();

  @BeforeClass
  public static void setUp() {
    // Setting up the database
    try {
      classUnderTest = Manager.getInstance();
      assertNotNull(classUnderTest);

      DBConnection.init();

      if (classUnderTest.containsProject(projectName)) {
        Manager.getInstance().deleteCompleteProject(projectName);
      }
    } catch (NoClassDefFoundError e) {

    }
  }

  @Before
  public void init() {
    // load project from file and save to database
    PSPProject psp = loadTestData();
    classUnderTest.saveBackupProject(psp);
    assertEquals(psp.getProject().getProjectName(),
        classUnderTest.loadBackupProject(projectName).getProject().getProjectName());
  }

  /**
   * Test the get Methods of the Manager by searching a null Project name and a project name, that
   * is not in the database
   */
  @Test
  public void testNullAndFalseProjectName() {
    assertNull(classUnderTest.getCompletedTasks(null));
    assertNull(classUnderTest.getDefectRecord(null));
    assertNull(classUnderTest.getPip(null));
    assertNull(classUnderTest.getProjectByName(null));
    assertNull(classUnderTest.getSchedulePlanning(null));
    assertNull(classUnderTest.getTaskPlanning(null));
    assertNull(classUnderTest.getTestReport(null));
    assertNull(classUnderTest.getTimeRecord(null));
    assertNull(classUnderTest.getProjectSummary(null));

    assertEquals(Collections.EMPTY_LIST, classUnderTest.getCompletedTasks(notProjectName));
    assertEquals(Collections.EMPTY_LIST, classUnderTest.getDefectRecord(notProjectName));
    assertEquals(Collections.EMPTY_LIST, classUnderTest.getPip(notProjectName));
    assertEquals(null, classUnderTest.getProjectByName(notProjectName));
    assertEquals(Collections.EMPTY_LIST, classUnderTest.getSchedulePlanning(notProjectName));
    assertEquals(Collections.EMPTY_LIST, classUnderTest.getTestReport(notProjectName));
    assertEquals(Collections.EMPTY_LIST, classUnderTest.getTimeRecord(notProjectName));
    assertEquals(Collections.EMPTY_LIST, classUnderTest.getTaskPlanning(notProjectName));
  }

  /**
   * Tests the update method of the Manager class by updating an item and check if it has been
   * updated in the database
   */
  @Test
  public void testUpdateItem() {
    try {
      Project p = Manager.getInstance().getProjectByName(projectName);
      Task task = new Task(taskName, 0, phase, timeValue, date, 0, 0, one, p, Constants.TASK_TYPE_TASK);
      classUnderTest.saveToDB(task);
      assertTrue(classUnderTest.taskExist(taskName, projectName));
      assertFalse(classUnderTest.taskExist(taskName, notProjectName));

      task = classUnderTest.getTaskPlanning(projectName).get(0);
      task.setActualTime(timeValue);
      classUnderTest.update(task);
      assertEquals(task.getActualTime(), classUnderTest.getTaskPlanning(projectName).get(0).getActualTime());
    } catch (IllegalStateException e) {

    }
  }

  /**
   * Saves one defect record and test report record to the database and checks if it has been saved
   * to database
   */
  @Test
  public void testGetDefectRecordAndTestReportEntry() {
    try {
      Project p = Manager.getInstance().getProjectByName(projectName);
      DefectRecord dr = new DefectRecord(plusMinute, date, type, phase, phase, one, "", p, "");
      TestReport tr = new TestReport(p, taskName, "expected", "actual");
      classUnderTest.saveToDB(dr);
      classUnderTest.saveToDB(tr);

      assertEquals(dr.getType(), classUnderTest.getDefectRecord(projectName, plusMinute).getType());
      assertEquals(tr.getName(), classUnderTest.getTestReportEntry(projectName, taskName).getName());
      assertEquals(tr.getExpectedResult(),
          classUnderTest.getTestReportEntry(projectName, taskName).getExpectedResult());
      assertEquals(tr.getActualResult(), classUnderTest.getTestReportEntry(projectName, taskName).getActualResult());
    } catch (IllegalStateException e) {

    }
  }

  /**
   * Testing the time record overlapping function in the Manager class.
   */
  @Test
  public void testTimeRecordOverlapps() {
    try {
      LocalTime startTime1 = LocalTime.of(21, 21, 21);
      LocalTime endTime1 = LocalTime.of(22, 21, 21);
      LocalTime startTime2 = LocalTime.of(22, 22, 22);
      LocalTime endTime2 = LocalTime.of(22, 45, 21);
      LocalTime startTime3 = LocalTime.of(21, 19, 13);
      LocalTime endTime3 = LocalTime.of(21, 45, 21);
      Project p = Manager.getInstance().getProjectByName(projectName);
      TimeRecord oneTimeRecord = new TimeRecord(date, date, startTime1, endTime1, 0, phase, "", p);
      TimeRecord secondTimeRecordNotOverlapping = new TimeRecord(date, date, startTime2, endTime2, 0, phase, "", p);
      TimeRecord thirdOverlapping = new TimeRecord(date, date, startTime3, endTime3, 0, phase, "", p);
      TimeRecord fourOverlapping = new TimeRecord(date, date, endTime1, endTime2, 0, phase, "", p);
      TimeRecord fivedOverlapping = new TimeRecord(date, date, endTime1, startTime2, 0, phase, "", p);

      classUnderTest.saveToDB(oneTimeRecord);

      // time record does not overlap
      assertFalse(classUnderTest.timeRecordOverlapps(secondTimeRecordNotOverlapping.getID(),
          secondTimeRecordNotOverlapping.getDate(), secondTimeRecordNotOverlapping.getStarttime(),
          secondTimeRecordNotOverlapping.getEndDate(), secondTimeRecordNotOverlapping.getEndtime(), projectName));

      // time record overlaps
      assertTrue(classUnderTest.timeRecordOverlapps(thirdOverlapping.getID(), thirdOverlapping.getDate(),
          thirdOverlapping.getStarttime(), thirdOverlapping.getEndDate(), thirdOverlapping.getEndtime(), projectName));
      assertTrue(classUnderTest.timeRecordOverlapps(fourOverlapping.getID(), fourOverlapping.getDate(),
          fourOverlapping.getStarttime(), fourOverlapping.getEndDate(), fourOverlapping.getEndtime(), projectName));
      assertTrue(classUnderTest.timeRecordOverlapps(fivedOverlapping.getID(), fivedOverlapping.getDate(),
          fivedOverlapping.getStarttime(), fivedOverlapping.getEndDate(), fivedOverlapping.getEndtime(), projectName));

      // time record does not overlap
      assertFalse(classUnderTest.timeRecordOverlapps(oneTimeRecord.getID(), oneTimeRecord.getDate(),
          oneTimeRecord.getStarttime(), oneTimeRecord.getEndDate(), oneTimeRecord.getEndtime(), projectName));
    } catch (IllegalStateException e) {

    }
  }

  /**
   * Test the get to date values method in the Manager class.
   */
  @Test
  public void testGetToDateValues() {
    String[] sum = {projectName};
    ProjectPlanSummary ps = classUnderTest.getProjectSummary(projectName);
    double time = ps.get(Constants.KEY_TIME_IN_PHASE_IDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_TO_DATE);
    double defInj = ps.get(Constants.KEY_DEFECTS_INJECTED_IDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_TO_DATE);
    double defRmd = ps.get(Constants.KEY_DEFECTS_REMOVED_IDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_TO_DATE);
    double size = ps.get(Constants.KEY_PROGRAM_SIZE_IDX, Constants.KEY_SIZE_TOTAL_LOC, Constants.KEY_TO_DATE);
    double perDefRmd = ps.get(Constants.KEY_DEFECTS_REMOVED_IDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_TO_DATE_PER);
    ProjectPlanSummary pps = classUnderTest.getToDateValues(ps, sum);
    double time1 = pps.get(Constants.KEY_TIME_IN_PHASE_IDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_TO_DATE);
    double defInj1 = pps.get(Constants.KEY_DEFECTS_INJECTED_IDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_TO_DATE);
    double defRmd1 = pps.get(Constants.KEY_DEFECTS_REMOVED_IDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_TO_DATE);
    double size1 = pps.get(Constants.KEY_PROGRAM_SIZE_IDX, Constants.KEY_SIZE_TOTAL_LOC, Constants.KEY_TO_DATE);
    double perDefRmd1 =
        pps.get(Constants.KEY_DEFECTS_REMOVED_IDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_TO_DATE_PER);

    assertEquals(time * 2, time1, 0.0);
    assertEquals(defInj * 2, defInj1, 0.0);
    assertEquals(defRmd * 2, defRmd1, 0.0);
    assertEquals(size * 2, size1, 0.0);
    assertEquals(perDefRmd, perDefRmd1, 0.0);

  }

  @After
  public void cleanUp() {
    // cleans up the database
    classUnderTest.deleteCompleteProject(projectName);
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
