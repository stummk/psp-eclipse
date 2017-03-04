package de.uni.bremen.stummk.psp.test.data;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import de.uni.bremen.stummk.psp.data.Project;
import de.uni.bremen.stummk.psp.data.ProjectPlanSummary;
import de.uni.bremen.stummk.psp.utility.Constants;

/**
 * Tests for the ProjectPlanSummary class
 * 
 * @author Konstantin
 *
 */
public class ProjectPlanSummaryTest {
  private ProjectPlanSummary classUnderTest;
  private Project p;
  private static final double zeroValue = 0;
  private static final double value = 23;
  private static final double result = 46;
  private static final int notAnIndex = -1;

  @Before
  public void setUp() {
    String projectName = "test";
    LocalDate date = LocalDate.now();
    p = new Project(projectName, date);
  }

  @Test
  public void testConstructor() {
    classUnderTest = new ProjectPlanSummary();
    assertEquals(null, classUnderTest.getProject());

    classUnderTest = new ProjectPlanSummary(p);
    assertEquals(p, classUnderTest.getProject());
    assertEquals(Collections.emptyList(), classUnderTest.getElements());
  }

  @Test
  public void testUpdateValue() {
    classUnderTest = new ProjectPlanSummary(p);
    classUnderTest.put(Constants.KEY_DEFECTS_INJECTED_IDX, Constants.KEY_PHASE_COMPILE, Constants.KEY_ACTUAL, value);
    assertEquals(value,
        classUnderTest.get(Constants.KEY_DEFECTS_INJECTED_IDX, Constants.KEY_PHASE_COMPILE, Constants.KEY_ACTUAL), 0.0);

    classUnderTest.updateValue(Constants.KEY_DEFECTS_INJECTED_IDX, Constants.KEY_PHASE_COMPILE, Constants.KEY_ACTUAL, value);
    assertEquals(result,
        classUnderTest.get(Constants.KEY_DEFECTS_INJECTED_IDX, Constants.KEY_PHASE_COMPILE, Constants.KEY_ACTUAL), 0.0);
  }

  @Test
  public void testGetSection() {
    classUnderTest = new ProjectPlanSummary(p);
    assertEquals(Collections.emptyList(), classUnderTest.getSectionContent(notAnIndex));
    assertEquals(zeroValue,
        classUnderTest.get(Constants.KEY_DEFECTS_INJECTED_IDX, Constants.KEY_PHASE_COMPILE, Constants.KEY_ACTUAL), 0.0);
    assertEquals(zeroValue,
        classUnderTest.get(Constants.KEY_DEFECTS_REMOVED_IDX, Constants.KEY_PHASE_COMPILE, Constants.KEY_ACTUAL), 0.0);
    assertEquals(zeroValue,
        classUnderTest.get(Constants.KEY_PROGRAM_SIZE_IDX, Constants.KEY_SIZE_TOTAL_LOC, Constants.KEY_ACTUAL), 0.0);
    assertEquals(zeroValue,
        classUnderTest.get(Constants.KEY_TIME_IN_PHASE_IDX, Constants.KEY_PHASE_COMPILE, Constants.KEY_ACTUAL), 0.0);
    assertEquals(zeroValue, classUnderTest.get(Constants.KEY_SUMMARY_IDX, Constants.KEY_SUM_TIME, Constants.KEY_ACTUAL), 0.0);
  }
}
