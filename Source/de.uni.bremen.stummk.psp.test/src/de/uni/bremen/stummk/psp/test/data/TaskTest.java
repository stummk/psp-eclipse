package de.uni.bremen.stummk.psp.test.data;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.uni.bremen.stummk.psp.data.Phase;
import de.uni.bremen.stummk.psp.data.Project;
import de.uni.bremen.stummk.psp.data.Task;
import de.uni.bremen.stummk.psp.utility.Constants;

/**
 * Tests for the Task class
 * 
 * @author Konstantin
 *
 */
public class TaskTest {
  private Task classUnderTest;
  private Project p;
  private String name = "test";
  private Phase phase = Phase.CODE;
  private long plan = 12;
  private LocalDate date = LocalDate.now();
  private long planLoc = 123;
  private long planDefI = 1;
  private long planDefR = 1;


  @Before
  public void setUp() {
    String projectName = "test";
    LocalDate date = LocalDate.now();
    p = new Project(projectName, date);
  }

  @Test
  public void testConstructor() {
    classUnderTest = new Task();
    assertEquals(null, classUnderTest.getProject());

    classUnderTest = new Task(name, 0, phase, plan, date, planLoc, planDefI, planDefR, p, Constants.TASK_TYPE_TASK);
    assertEquals(p, classUnderTest.getProject());
    assertEquals(name, classUnderTest.getName());
    assertEquals(plan, classUnderTest.getPlanTime());
    assertEquals(date, classUnderTest.getPlannedMonday());
    assertEquals(planLoc, classUnderTest.getPlanLoc());
    assertEquals(planDefI, classUnderTest.getPlanDefInjected());
    assertEquals(planDefR, classUnderTest.getPlanDefRemoved());
  }

  @Test
  public void testRefresh() {
    classUnderTest = new Task(name, 0, phase, plan, date, planLoc, planDefI, planDefR, p, Constants.TASK_TYPE_TASK);
    classUnderTest.refreshItems("new name", 0, phase, plan, date, planLoc, planDefI, planDefR);

    List<String> elements = new ArrayList<>();
    elements.add("new name");
    elements.add("0h : 12min");
    elements.add("" + 0.0);
    elements.add("0h : 0min");
    elements.add("" + 0.0);
    elements.add(date.toString());
    elements.add("");
    elements.add("" + 0.0);
    elements.add("" + 0.0);

    assertEquals(elements, classUnderTest.getElements());

    elements = new ArrayList<>();
    elements.add("");
    elements.add("new name");
    elements.add("" + 0);
    elements.add("" + plan);
    elements.add("" + 0);
    elements.add("" + 0.0);
    elements.add("" + 0.0);
    elements.add("Unready");
    elements.add("");

    assertEquals(elements, classUnderTest.getOverviewElements());

  }

  @Test
  public void testCompleteTask() {
    classUnderTest = new Task();
    classUnderTest.markAsComplete(date);

    assertEquals(date, classUnderTest.getCompleteDate());
    assertEquals("Ready", classUnderTest.getStatus());
  }
}
