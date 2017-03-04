package de.uni.bremen.stummk.psp.data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * Represents an test report entry
 * 
 * @author Konstantin
 *
 */
@Entity
@Table(name = "test_report")
public class TestReport implements PersistenceItem {

  @ManyToOne
  private Project project;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long ID;

  @JoinColumn
  private String name;

  @JoinColumn
  private String expectedResult;

  @JoinColumn
  private String actualResult;

  /**
   * Constructor
   */
  public TestReport() {
    // empty for jpa purpose
  }

  /**
   * Constructor
   * 
   * @param project the project, the Test report belongs to
   * @param name the name of the test
   * @param expectedResult the expected Result
   * @param actualResult the actual result
   */
  public TestReport(Project project, String name, String expectedResult, String actualResult) {
    this.project = project;
    this.name = name;
    this.expectedResult = expectedResult;
    this.actualResult = actualResult;
  }

  /**
   * Constructor
   * 
   * @param ID the id
   * @param name the name of the test
   * @param expectedResult the expected Result
   * @param actualResult the actual result
   * @param project the project, the Test report belongs to
   */
  public TestReport(long ID, String name, String expectedResult, String actualResult, Project project) {
    this.ID = ID;
    this.project = project;
    this.name = name;
    this.expectedResult = expectedResult;
    this.actualResult = actualResult;
  }

  /**
   * @return the project of the test report entry
   */
  public Project getProject() {
    return this.project;
  }

  /**
   * @return the name of the test
   */
  public String getName() {
    return this.name;
  }

  /**
   * @return the expected result
   */
  public String getExpectedResult() {
    return this.expectedResult;
  }

  /**
   * @return the actual result
   */
  public String getActualResult() {
    return this.actualResult;
  }

  @Override
  public long getID() {
    return this.ID;
  }

  @Override
  public List<String> getElements() {
    List<String> elements = new ArrayList<>();
    elements.add(name);
    elements.add(expectedResult);
    elements.add(actualResult);

    return elements;
  }

  /**
   * Updates the attributes of the entry
   * 
   * @param expected the new expected result
   * @param actual the new actual result
   */
  public void refreshItems(String expected, String actual) {
    this.expectedResult = expected;
    this.actualResult = actual;
  }

  @Override
  public String toString() {
    return String.valueOf(this.ID) + "#" + name + "#" + expectedResult + "#" + actualResult;
  }

}
