package de.uni.bremen.stummk.psp.data;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

/**
 * This class holds the general project info
 * 
 * @author Konstantin
 *
 */
@Entity
@Table(name = "project_info")
public class Project implements PersistenceItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long ID;

  @JoinColumn
  private String projectName;

  @JoinColumn
  private LocalDate timestamp;

  /**
   * Constructor
   */
  public Project() {
    // for jpa purpose
  }

  /**
   * Constructor
   * 
   * @param projectName the project id
   * @param timestamp the date, when the project was created
   */
  public Project(String projectName, LocalDate timestamp) {
    this.projectName = projectName;
    this.timestamp = timestamp;
  }

  /**
   * Constructor
   * 
   * @param ID the id of the project
   * @param projectName the project id
   * @param timestamp the date, when the project was created
   */
  public Project(long ID, String projectName, LocalDate timestamp) {
    this.ID = ID;
    this.projectName = projectName;
    this.timestamp = timestamp;
  }

  /**
   * @return the project name
   */
  public String getProjectName() {
    return projectName;
  }

  /**
   * @return the creation date of the project
   */
  public LocalDate getTimestamp() {
    return timestamp;
  }

  @Override
  public long getID() {
    return this.ID;
  }

  @Override
  public List<String> getElements() {
    return Collections.emptyList();
  }

  @Override
  public String toString() {
    return String.valueOf(this.ID) + "#" + this.projectName + "#" + timestamp.toString();
  }

  @Override
  public Project getProject() {
    return this;
  }

}
