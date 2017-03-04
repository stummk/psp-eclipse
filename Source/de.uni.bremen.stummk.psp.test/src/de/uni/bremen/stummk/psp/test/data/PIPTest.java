package de.uni.bremen.stummk.psp.test.data;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;

import org.junit.Test;

import de.uni.bremen.stummk.psp.data.PIP;
import de.uni.bremen.stummk.psp.data.Project;

/**
 * Tests for the PIP class
 * 
 * @author Konstantin
 *
 */
public class PIPTest {
  private PIP classUnderTest;

  @Test
  public void testConstructor() {
    classUnderTest = new PIP();
    assertEquals(null, classUnderTest.getNotes());

    String projectName = "test";
    LocalDate date = LocalDate.now();
    Project p = new Project(projectName, date);

    String desc = "description";
    String prop = "proposal";
    String prob = "problem";

    classUnderTest = new PIP(prob, prop, desc, p);
    assertEquals(desc, classUnderTest.getNotes());
    assertEquals(prob, classUnderTest.getProblemDescription());
    assertEquals(prop, classUnderTest.getProposalDescription());

    String newDesc = "new description";

    classUnderTest.refreshItems(prob, prop, newDesc);
    assertEquals(newDesc, classUnderTest.getNotes());
    assertEquals(prob, classUnderTest.getProblemDescription());
    assertEquals(prop, classUnderTest.getProposalDescription());
  }
}
