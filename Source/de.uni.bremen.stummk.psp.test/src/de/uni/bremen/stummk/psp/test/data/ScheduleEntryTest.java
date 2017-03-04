package de.uni.bremen.stummk.psp.test.data;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.uni.bremen.stummk.psp.calculation.MathCalc;
import de.uni.bremen.stummk.psp.data.Phase;
import de.uni.bremen.stummk.psp.data.Project;
import de.uni.bremen.stummk.psp.data.ScheduleEntry;
import de.uni.bremen.stummk.psp.data.Task;
import de.uni.bremen.stummk.psp.data.TimeRecord;
import de.uni.bremen.stummk.psp.utility.Constants;

/**
 * Tests for the Schedule Entry class
 * 
 * @author Konstantin
 *
 */
public class ScheduleEntryTest {
  private ScheduleEntry classUnderTest;
  private Project p;
  private static final LocalDate date = LocalDate.now();
  private static final int weeknumber = 1;
  private static final long hoursToSet = 25;
  private static final long SetHoursMultiWith2 = 50;


  @Before
  public void setUp() {
    String projectName = "test";
    LocalDate date = LocalDate.now();
    p = new Project(projectName, date);
  }

  @Test
  public void testConstructor() {
    classUnderTest = new ScheduleEntry();
    assertEquals(null, classUnderTest.getProject());

    Task t = new Task("name", 0, Phase.CODE, 0, date, 0, 0, 0, p, Constants.TASK_TYPE_TASK);
    TimeRecord tr = new TimeRecord(date, date, LocalTime.now(), LocalTime.now(), 0, Phase.CODE, "", p);

    classUnderTest = new ScheduleEntry(p, weeknumber, t);
    assertEquals(p, classUnderTest.getProject());
    assertEquals(weeknumber, classUnderTest.getWeekNumber());
    assertEquals(0, classUnderTest.getActualCumulativeHour());
    assertEquals(0, classUnderTest.getActualDirectHours());
    assertEquals(0.0, classUnderTest.getActualEarnedValue(), 0.0);
    assertEquals(0.0, classUnderTest.getCumulativeEarnedValue(), 0.0);
    assertEquals(0, classUnderTest.getCumulativePlannedHours());
    assertEquals(date, classUnderTest.getDateMonday());
    assertEquals(0, classUnderTest.getPlannedHours());

    classUnderTest = new ScheduleEntry(p, weeknumber, tr);
    assertEquals(p, classUnderTest.getProject());
    assertEquals(weeknumber, classUnderTest.getWeekNumber());
  }

  @Test
  public void testGetElements() {
    Task t = new Task("name", 0, Phase.CODE, 0, date, 0, 0, 0, p, Constants.TASK_TYPE_TASK);

    List<String> elements = new ArrayList<>();
    elements.add("" + weeknumber);
    elements.add(date.toString());
    elements.add("0h : 0min");
    elements.add("0h : 0min");
    elements.add("" + MathCalc.getInstance().round(0.0));
    elements.add("0h : 0min");
    elements.add("0h : 0min");
    elements.add("" + MathCalc.getInstance().round(0.0));

    classUnderTest = new ScheduleEntry(p, weeknumber, t);
    assertEquals(elements, classUnderTest.getElements());
  }

  @Test
  public void testUpdate() {
    Task t = new Task("name", 0, Phase.CODE, 0, date, 0, 0, 0, p, Constants.TASK_TYPE_TASK);
    classUnderTest = new ScheduleEntry(p, weeknumber, t);

    classUnderTest.setCumulativeActualHours(hoursToSet);
    assertEquals(hoursToSet, classUnderTest.getActualCumulativeHour());

    classUnderTest.setCumulativePlannedHours(hoursToSet);
    assertEquals(hoursToSet, classUnderTest.getCumulativePlannedHours());

    classUnderTest.updateActualHour(hoursToSet);
    classUnderTest.updateActualHour(hoursToSet);
    assertEquals(SetHoursMultiWith2, classUnderTest.getActualDirectHours());

    classUnderTest.updateActualHour(-2 * hoursToSet);
    assertEquals(0, classUnderTest.getActualDirectHours());

    classUnderTest.updatePlannedValues(hoursToSet);
    classUnderTest.updatePlannedValues(hoursToSet);
    assertEquals(SetHoursMultiWith2, classUnderTest.getPlannedHours());

    classUnderTest.updatePlannedValues(-2 * hoursToSet);
    assertEquals(0, classUnderTest.getPlannedHours());
  }
}
