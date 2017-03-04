package de.uni.bremen.stummk.psp.test.control;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.uni.bremen.stummk.psp.calculation.Manager;
import de.uni.bremen.stummk.psp.control.AddDefectDialog;
import de.uni.bremen.stummk.psp.control.AddLoCDialog;
import de.uni.bremen.stummk.psp.control.AddPIPDialog;
import de.uni.bremen.stummk.psp.control.AddTaskDialog;
import de.uni.bremen.stummk.psp.control.AddTimeRecordDialog;
import de.uni.bremen.stummk.psp.control.WindowDialog;
import de.uni.bremen.stummk.psp.data.DefectRecord;
import de.uni.bremen.stummk.psp.data.DefectType;
import de.uni.bremen.stummk.psp.data.PIP;
import de.uni.bremen.stummk.psp.data.PSPProject;
import de.uni.bremen.stummk.psp.data.Phase;
import de.uni.bremen.stummk.psp.data.Project;
import de.uni.bremen.stummk.psp.data.Task;
import de.uni.bremen.stummk.psp.data.TimeRecord;
import de.uni.bremen.stummk.psp.utility.Constants;
import de.uni.bremen.stummk.psp.utility.DBConnection;
import de.uni.bremen.stummk.psp.utility.PSPCSVParser;

/**
 * Tests the Add Dialogs, should be run as JUNIT-PLUGIN TEST
 * 
 * @author Konstantin
 *
 */
public class AddDialogTest {
  private static WindowDialog classUnderTest;
  private static final String projectName = "TESTPROJECT";
  private Project p;
  private static final String taskName = "TASK_NAME";
  private static final Phase phase = Phase.POSTMORTEM;
  private static final DefectType type = DefectType.DATA;
  private static final long timeValue = 600;
  private static final long one = 1;
  private static final LocalDate date = LocalDate.now();
  private static final LocalTime time = LocalTime.now();
  private static final LocalTime endTime = time.plusSeconds(timeValue);

  @BeforeClass
  public static void startUp() {
    // set up db
    DBConnection.init();

    if (Manager.getInstance().containsProject(projectName)) {
      Manager.getInstance().deleteCompleteProject(projectName);
    }
  }

  @Before
  public void setUp() {
    // Load Project from file and save it to db
    PSPProject psp = loadTestData();
    Manager.getInstance().saveBackupProject(psp);

    p = psp.getProject();
  }

  /**
   * Tests the AddPIPDialog class
   */
  @Test
  public void testPIPDialog() {
    try {
      // test constructor
      String problem = "problem";
      String proposal = "proposal";
      classUnderTest = new AddPIPDialog(null, p);
      assertNull(classUnderTest.getSelection());

      PIP pip = new PIP(problem, proposal, "", p);
      classUnderTest = new AddPIPDialog(null, p, pip);

      assertEquals(problem, ((PIP) classUnderTest.getSelection()).getProblemDescription());
      assertEquals(proposal, ((PIP) classUnderTest.getSelection()).getProposalDescription());

      // test the check method, if error are checked
      pip.refreshItems("", "", "");
      classUnderTest.create();
      classUnderTest.check();
      assertEquals("The problem or purposal should be descripted", classUnderTest.getErrLbl().getText());
      classUnderTest.close();
    } catch (NoClassDefFoundError e) {
    }
  }

  /**
   * Tests the AddDefectRecord Dialog
   */
  @Test
  public void testDefectDialog() {
    try {
      // test constructor
      classUnderTest = new AddDefectDialog(null, p);
      assertNull(classUnderTest.getSelection());

      DefectRecord defectRec = new DefectRecord(1, date, type, phase, phase, 0, "", p, "");
      classUnderTest = new AddDefectDialog(null, p, defectRec);

      assertEquals(defectRec.getDate(), ((DefectRecord) classUnderTest.getSelection()).getDate());
      assertEquals(defectRec.getInjectPhase(), ((DefectRecord) classUnderTest.getSelection()).getInjectPhase());
      assertEquals(defectRec.getRemovePhase(), ((DefectRecord) classUnderTest.getSelection()).getRemovePhase());
      assertEquals(defectRec.getType(), ((DefectRecord) classUnderTest.getSelection()).getType());

      // test the check method, if error are checked
      defectRec.refreshItems(p.getTimestamp().minusDays(1), type, phase, phase, 0, "");
      classUnderTest.create();
      classUnderTest.check();
      assertEquals("Date can not be before Project start date", classUnderTest.getErrLbl().getText());
      classUnderTest.close();
    } catch (NoClassDefFoundError e) {
    }
  }

  /**
   * Tests the AddTimeRecordDialog
   */
  @Test
  public void testTimeDialog() {
    try {
      // test constructor
      classUnderTest = new AddTimeRecordDialog(null, p);
      assertNull(classUnderTest.getSelection());

      TimeRecord timeRec = new TimeRecord(date, date, time, endTime, 0, phase, "", p);
      Manager.getInstance().saveToDB(timeRec);
      classUnderTest = new AddTimeRecordDialog(null, p, timeRec);

      assertEquals(timeRec.getDate(), ((TimeRecord) classUnderTest.getSelection()).getDate());
      assertEquals(timeRec.getDeltaTime(), ((TimeRecord) classUnderTest.getSelection()).getDeltaTime());
      assertEquals(timeRec.getEndDate(), ((TimeRecord) classUnderTest.getSelection()).getEndDate());
      assertEquals(timeRec.getEndtime(), ((TimeRecord) classUnderTest.getSelection()).getEndtime());
      assertEquals(timeRec.getInterruptTime(), ((TimeRecord) classUnderTest.getSelection()).getInterruptTime());
      assertEquals(timeRec.getPhase(), ((TimeRecord) classUnderTest.getSelection()).getPhase());
      assertEquals(timeRec.getStarttime(), ((TimeRecord) classUnderTest.getSelection()).getStarttime());

      // test the check method, if error are checked
      timeRec.refreshItems(p.getTimestamp().minusDays(1), date, time, endTime, 0, phase, "");
      classUnderTest.create();
      classUnderTest.check();
      assertEquals("Startdate can not be before Project start date", classUnderTest.getErrLbl().getText());
      classUnderTest.close();

      timeRec.refreshItems(p.getTimestamp(), p.getTimestamp().minusDays(1), time, endTime, 0, phase, "");
      classUnderTest.create();
      classUnderTest.check();
      assertEquals("Enddate can not be before startdate", classUnderTest.getErrLbl().getText());
      classUnderTest.close();

      timeRec.refreshItems(date, date, endTime, time, 0, phase, "");
      classUnderTest.create();
      classUnderTest.check();
      assertEquals("Starttime must be before Stoptime", classUnderTest.getErrLbl().getText());
      classUnderTest.close();

      timeRec.refreshItems(date, date, time, endTime, (int) timeValue, phase, "");
      classUnderTest.create();
      classUnderTest.check();
      assertEquals("Interrupt Time can not be greater than worked time", classUnderTest.getErrLbl().getText());
      classUnderTest.close();
    } catch (NoClassDefFoundError | IllegalStateException e) {
    }
  }

  /**
   * Tests the AddTaskDialog
   */
  @Test
  public void testTaskDialog() {
    try {
      // test constructor
      classUnderTest = new AddTaskDialog(null, p);
      assertNull(classUnderTest.getSelection());

      Task task = new Task(taskName, 0, phase, timeValue, date, 0, 0, one, p, Constants.TASK_TYPE_TASK);
      classUnderTest = new AddTaskDialog(null, p, task);

      assertEquals(task.getName(), ((Task) classUnderTest.getSelection()).getName());
      assertEquals(task.getPhase(), ((Task) classUnderTest.getSelection()).getPhase());
      assertEquals(task.getPlanDefInjected(), ((Task) classUnderTest.getSelection()).getPlanDefInjected());
      assertEquals(task.getPlanDefRemoved(), ((Task) classUnderTest.getSelection()).getPlanDefRemoved());
      assertEquals(task.getPlannedMonday(), ((Task) classUnderTest.getSelection()).getPlannedMonday());
      assertEquals(task.getPlanTime(), ((Task) classUnderTest.getSelection()).getPlanTime());

      // test the check method, if error are checked
      task.refreshItems("", 0, phase, timeValue, date, 0, 0, one);
      classUnderTest.create();
      classUnderTest.check();
      assertEquals("Task name can not be empty", classUnderTest.getErrLbl().getText());
      classUnderTest.close();

      task.refreshItems(taskName, 0, phase, timeValue, p.getTimestamp().minusDays(1), 0, 0, one);
      classUnderTest.create();
      classUnderTest.check();
      assertEquals("Planned Finish Date cannot be before Project startdate", classUnderTest.getErrLbl().getText());
      classUnderTest.close();

      task.refreshItems(taskName, 0, phase, timeValue, date, 0, 0, one);
      Manager.getInstance().saveToDB(task);
      classUnderTest.create();
      classUnderTest.setSelection(null);
      classUnderTest.check();
      assertEquals("Task with Name already exists", classUnderTest.getErrLbl().getText());
      classUnderTest.close();
    } catch (NoClassDefFoundError e) {
    }
  }

  /**
   * Tests the AddLoCDialog class
   */
  @Test
  public void testLocDialog() {
    // test constructor
    classUnderTest = new AddLoCDialog(null, p);
    assertNull(classUnderTest.getSelection());
  }

  @After
  public void cleanUp() {
    // cleaning up the db
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
