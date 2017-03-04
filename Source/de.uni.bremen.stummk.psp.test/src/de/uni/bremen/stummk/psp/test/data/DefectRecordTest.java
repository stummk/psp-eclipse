package de.uni.bremen.stummk.psp.test.data;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.uni.bremen.stummk.psp.data.DefectRecord;
import de.uni.bremen.stummk.psp.data.DefectType;
import de.uni.bremen.stummk.psp.data.Phase;
import de.uni.bremen.stummk.psp.data.Project;

/**
 * BlackBox tests for the DefectRecord class
 * 
 * @author Konstantin
 *
 */
public class DefectRecordTest {
  private DefectRecord classUnderTest;
  private Project p;
  private static final int number = 12;
  private static final LocalDate d = LocalDate.now();
  private static final DefectType type = DefectType.ASSIGNMENT;
  private static final Phase phase = Phase.CODE;
  private static final long fixTime = 1;
  private static final String description = "description";

  @Before
  public void setUp() {
    String projectName = "test";
    LocalDate date = LocalDate.now();
    p = new Project(projectName, date);
  }

  @Test
  public void testConstructor() {
    classUnderTest = new DefectRecord();
    assertEquals(null, classUnderTest.getProject());

    classUnderTest = new DefectRecord(number, d, type, phase, phase, fixTime, description, p, "");
    assertEquals(d, classUnderTest.getDate());
    assertEquals(type, classUnderTest.getType());
    assertEquals(phase, classUnderTest.getInjectPhase());
    assertEquals(phase, classUnderTest.getRemovePhase());
    assertEquals(fixTime, classUnderTest.getFixTime());
    assertEquals(description, classUnderTest.getDescription());
    assertEquals(p, classUnderTest.getProject());

    classUnderTest = new DefectRecord(0, d, type, phase, phase, fixTime, description, p, "");
    assertEquals(type, classUnderTest.getType());
    assertEquals(phase, classUnderTest.getInjectPhase());
    assertEquals(phase, classUnderTest.getRemovePhase());
    assertEquals(fixTime, classUnderTest.getFixTime());
    assertEquals(description, classUnderTest.getDescription());
    assertEquals(p, classUnderTest.getProject());
  }

  @Test
  public void testRefresh() {
    classUnderTest = new DefectRecord(number, d, type, phase, phase, fixTime, description, p, "");
    classUnderTest.refreshItems(d, type, phase, phase, 2, "");

    List<String> elements = new ArrayList<>();
    elements.add(d.toString());
    elements.add("" + classUnderTest.getID());
    elements.add(type.toString());
    elements.add(phase.toString());
    elements.add(phase.toString());
    elements.add("" + 2);
    elements.add("");

    assertEquals(elements, classUnderTest.getElements());

  }
}
