package de.uni.bremen.stummk.psp.test.data;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.util.Collections;

import org.junit.Test;

import de.uni.bremen.stummk.psp.data.Project;

/**
 * Tests for the Project class
 * 
 * @author Konstantin
 *
 */
public class ProjectTest {

  private Project classUnderTest;

  @Test
  public void testConstructor() {
    classUnderTest = new Project();
    assertEquals(null, classUnderTest.getProjectName());

    String projectName = "test";
    LocalDate date = LocalDate.now();
    classUnderTest = new Project(projectName, date);
    assertEquals(projectName, classUnderTest.getProjectName());
    assertEquals(date, classUnderTest.getTimestamp());

    assertEquals(Collections.emptyList(), classUnderTest.getElements());

  }

}
