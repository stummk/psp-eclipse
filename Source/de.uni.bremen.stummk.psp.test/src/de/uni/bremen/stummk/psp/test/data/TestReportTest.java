package de.uni.bremen.stummk.psp.test.data;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.uni.bremen.stummk.psp.data.Project;
import de.uni.bremen.stummk.psp.data.TestReport;

/**
 * Tests for the TestReport class
 * 
 * @author Konstantin
 *
 */
public class TestReportTest {
  private TestReport classUnderTest;
  private Project p;
  private static final String testName = "test";
  private static final String expected = "expected";
  private static final String actual = "actual";

  @Before
  public void setUp() {
    String projectName = "test";
    LocalDate date = LocalDate.now();
    p = new Project(projectName, date);
  }

  @Test
  public void testConstructor() {
    classUnderTest = new TestReport();
    assertEquals(null, classUnderTest.getProject());

    classUnderTest = new TestReport(p, testName, expected, actual);
    assertEquals(p, classUnderTest.getProject());
    assertEquals(testName, classUnderTest.getName());
    assertEquals(expected, classUnderTest.getExpectedResult());
    assertEquals(actual, classUnderTest.getActualResult());
  }

  @Test
  public void testRefresh() {
    classUnderTest = new TestReport(p, testName, expected, actual);
    classUnderTest.refreshItems(expected, "not actual");

    List<String> elements = new ArrayList<>();
    elements.add(testName);
    elements.add(expected);
    elements.add("not actual");

    assertEquals(elements, classUnderTest.getElements());

  }
}
