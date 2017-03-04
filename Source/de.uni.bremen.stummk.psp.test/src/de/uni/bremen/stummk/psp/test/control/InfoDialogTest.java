package de.uni.bremen.stummk.psp.test.control;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;

import de.uni.bremen.stummk.psp.control.InfoDialog;
import de.uni.bremen.stummk.psp.data.Phase;
import de.uni.bremen.stummk.psp.data.Project;
import de.uni.bremen.stummk.psp.data.Task;
import de.uni.bremen.stummk.psp.utility.Constants;

/**
 * Tests the Info Dialog
 * 
 * @author Konstantin
 *
 */
public class InfoDialogTest {
  private InfoDialog classUnderTest;
  private Project p;
  private static final String taskName = "TASK_NAME";
  private static final Phase phase = Phase.POSTMORTEM;
  private static final long timeValue = 600;
  private static final long one = 1;
  private static final LocalDate date = LocalDate.now();

  @Before
  public void setUp() {
    // Setting up project
    String projectName = "test";
    LocalDate date = LocalDate.now();
    p = new Project(projectName, date);
  }

  /**
   * Tests the constructor of the InfoDialog
   */
  @Test
  public void testConstructor() {
    classUnderTest = new InfoDialog(null, "message");
    assertEquals("message", classUnderTest.getMessage());

    Task task = new Task(taskName, 0, phase, timeValue, date, 0, 0, one, p, Constants.TASK_TYPE_TASK);
    task.setActualTime(timeValue);
    classUnderTest =
        new InfoDialog(null, p.getProjectName(), task.getName(), "" + task.getPlanTime(), task.getActualTime(), null);

    assertEquals(p.getProjectName(), classUnderTest.getProjectName());
    assertEquals(task.getActualTime(), classUnderTest.getActualTime());
    assertEquals(task.getName(), classUnderTest.getTask());
    assertEquals("" + task.getPlanTime(), classUnderTest.getPlanTime());
  }

}
