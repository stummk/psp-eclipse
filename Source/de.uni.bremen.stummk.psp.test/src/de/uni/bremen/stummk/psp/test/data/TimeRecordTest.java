package de.uni.bremen.stummk.psp.test.data;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.uni.bremen.stummk.psp.data.Phase;
import de.uni.bremen.stummk.psp.data.Project;
import de.uni.bremen.stummk.psp.data.TimeRecord;

/**
 * Tests for the TimeRecord class
 * 
 * @author Konstantin
 *
 */
public class TimeRecordTest {
  private TimeRecord classUnderTest;
  private Project p;
  private static final LocalDate start_end = LocalDate.now();
  private static final LocalTime startTime = LocalTime.of(12, 12, 12);
  private static final LocalTime endTime = LocalTime.of(12, 13, 12);
  private static final long interruptMin = 0;
  private static final Phase phase = Phase.CODE;
  private static final String comment = "comment";
  private static final LocalTime interruptStartTime = LocalTime.of(12, 12, 14);
  private static final LocalTime interruptEndTime = LocalTime.of(12, 12, 20);

  @Before
  public void setUp() {
    String projectName = "test";
    LocalDate date = LocalDate.now();
    p = new Project(projectName, date);
  }

  @Test
  public void testConstructor() {
    classUnderTest = new TimeRecord();
    assertEquals(null, classUnderTest.getProject());

    classUnderTest = new TimeRecord(start_end, start_end, startTime, endTime, (int) interruptMin, phase, comment, p);
    assertEquals(start_end, classUnderTest.getDate());
    assertEquals(start_end, classUnderTest.getEndDate());
    assertEquals(startTime, classUnderTest.getStarttime());
    assertEquals(endTime, classUnderTest.getEndtime());
    assertEquals(interruptMin, classUnderTest.getInterruptTime());
    assertEquals(phase, classUnderTest.getPhase());
    assertEquals(comment, classUnderTest.getComment());
    assertEquals(60, classUnderTest.getDeltaTime());
    assertEquals(p, classUnderTest.getProject());

    classUnderTest = new TimeRecord(start_end, startTime, p, phase, null);
    assertEquals(start_end, classUnderTest.getDate());
    assertEquals(startTime, classUnderTest.getStarttime());
    assertEquals(phase, classUnderTest.getPhase());
  }

  @Test
  public void testRefresh() {
    classUnderTest = new TimeRecord(start_end, startTime, p, phase, null);
    classUnderTest.refreshItems(start_end, start_end, startTime, LocalTime.of(12, 13), 0, phase, "");

    List<String> list = new ArrayList<>();
    list.add(start_end.toString());
    list.add(startTime.toString());
    list.add(LocalTime.of(12, 13).toString());
    list.add("" + 0);
    list.add("" + 0);
    list.add(phase.toString());
    list.add("");

    assertEquals(list, classUnderTest.getElements());

  }

  @Test
  public void testEndTime() {
    classUnderTest = new TimeRecord(start_end, startTime, p, phase, null);

    classUnderTest.setInterruptTime(6);

    classUnderTest.setEndtime(start_end, endTime);

    assertEquals(54, classUnderTest.getDeltaTime());
    assertEquals(6, classUnderTest.getInterruptTime());
  }
}
