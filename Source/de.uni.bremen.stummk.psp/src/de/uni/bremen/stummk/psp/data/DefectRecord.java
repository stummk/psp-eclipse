package de.uni.bremen.stummk.psp.data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Represents an defect record
 * 
 * @author Konstantin
 *
 */
@Entity
@Table(name = "defect_record")
public class DefectRecord implements PersistenceItem {


  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long ID;

  @JoinColumn
  private int number = 0;

  @ManyToOne
  private Project project;

  @OneToOne
  private Task task;

  @JoinColumn
  private LocalDate date;

  @JoinColumn
  @Enumerated(EnumType.STRING)
  protected DefectType type;

  @JoinColumn
  @Enumerated(EnumType.STRING)
  private Phase injectPhase;

  @JoinColumn
  @Enumerated(EnumType.STRING)
  private Phase removePhase;

  @JoinColumn
  private long fixTime;

  @JoinColumn
  private String description;

  @JoinColumn
  private boolean fixTimeManual;

  @JoinColumn
  private String filePath = "";

  /**
   * Constructor
   */
  public DefectRecord() {
    // for jpa purpose
  }

  /**
   * Constructor
   * 
   * @param number the number of the defect
   * @param date the date the record is created
   * @param type the type of the {@link DefectType}
   * @param injectPhase the {@link Phase} the defect is injected
   * @param removePhase the {@link Phase} the defect is removed
   * @param fixTime the fix time in minutes
   * @param description the description of the defect
   * @param project the project, the defect belongs to
   * @param filePath the project relative path of the resource the defect belongs to or an empty
   *        string
   */
  public DefectRecord(int number, LocalDate date, DefectType type, Phase injectPhase, Phase removePhase, long fixTime,
      String description, Project project, String filePath) {
    this.number = number;
    this.date = date;
    this.type = type;
    this.injectPhase = injectPhase;
    this.removePhase = removePhase;
    this.fixTime = fixTime;
    this.description = description;
    this.project = project;
    this.fixTimeManual = false;
    this.filePath = filePath.isEmpty() ? project.getProjectName() : filePath;
  }

  /**
   * Constructor
   * 
   * @param date the date the record is created
   * @param type the type of the {@link DefectType}
   * @param injectPhase the {@link Phase} the defect is injected
   * @param removePhase the {@link Phase} the defect is removed
   * @param fixTime the fix time in minutes
   * @param description the description of the defect
   * @param project the project, the defect belongs to
   * @param fixTimeManual was the defect fixed already
   * @param filePath the project relative path of the resource the defect belongs to or an empty
   *        string
   */
  public DefectRecord(LocalDate date, DefectType type, Phase injectPhase, Phase removePhase, long fixTime,
      String description, Project project, boolean fixTimeManual, String filePath) {
    this.date = date;
    this.type = type;
    this.injectPhase = injectPhase;
    this.removePhase = removePhase;
    this.fixTime = fixTime;
    this.description = description;
    this.project = project;
    this.fixTimeManual = fixTimeManual;
    this.filePath = filePath.isEmpty() ? project.getProjectName() : filePath;
  }

  /**
   * Constructor
   * 
   * @param ID the id of the defect record
   * @param number the number of the defect
   * @param date the date the record is created
   * @param type the type of the {@link DefectType}
   * @param injectPhase the {@link Phase} the defect is injected
   * @param removePhase the {@link Phase} the defect is removed
   * @param fixTime the fix time in minutes
   * @param description the description of the defect
   * @param project the project, the defect belongs to
   * @param fixTimeManual was the defect fixed already
   * @param filePath the project relative path of the resource the defect belongs to or an empty
   *        string
   */
  public DefectRecord(long ID, int number, LocalDate date, DefectType type, Phase injectPhase, Phase removePhase,
      long fixTime, String description, Project project, boolean fixTimeManual, String filePath) {
    this.ID = ID;
    this.number = number;
    this.date = date;
    this.type = type;
    this.injectPhase = injectPhase;
    this.removePhase = removePhase;
    this.fixTime = fixTime;
    this.description = description;
    this.project = project;
    this.fixTimeManual = fixTimeManual;
    this.filePath = filePath;
  }

  /**
   * @return the date
   */
  public LocalDate getDate() {
    return date;
  }


  /**
   * @return the injectPhase
   */
  public Phase getInjectPhase() {
    return injectPhase;
  }


  /**
   * @return the removePhase
   */
  public Phase getRemovePhase() {
    return removePhase;
  }


  /**
   * @return the fixTime
   */
  public long getFixTime() {
    return fixTime;
  }

  /**
   * @return true is defect was fixed manual
   */
  public boolean isFixManual() {
    return fixTimeManual;
  }

  /**
   * @return the project relative path of the resource the defect belongs to
   */
  public String getFilePath() {
    return filePath;
  }


  /**
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * @return the task of the defect record
   */
  public Task getTask() {
    return this.task;
  }

  /**
   * Sets the task of this defect
   * 
   * @param task the task
   */
  public void setTask(Task task) {
    this.task = task;
  }

  /**
   * @return the defect type
   */
  public DefectType getType() {
    return type;
  }

  /**
   * @return the {@link Project} of the defect record
   */
  public Project getProject() {
    return this.project;
  }

  @Override
  public long getID() {
    return this.ID;
  }

  @Override
  public List<String> getElements() {
    List<String> elements = new ArrayList<>();
    elements.add(date.toString());
    elements.add("" + ID);
    elements.add(type.toString());
    elements.add(injectPhase.toString());
    elements.add(removePhase != null ? removePhase.toString() : "");
    elements.add("" + fixTime);
    elements.add(description);

    return elements;
  }

  /**
   * Refreshes the data of the defect record
   * 
   * @param date the date the record is created
   * @param type the type of the {@link DefectType}
   * @param injectPhase the {@link Phase} the defect is injected
   * @param removePhase the {@link Phase} the defect is removed
   * @param fixTime the fix time in minutes
   * @param description the description of the defect
   */
  public void refreshItems(LocalDate date, DefectType type, Phase injectPhase, Phase removePhase, long fixTime,
      String description) {
    this.date = date;
    this.type = type;
    this.injectPhase = injectPhase;
    this.removePhase = removePhase;
    this.fixTime = fixTime;
    this.description = description;
  }

  private String removePhaseToString() {
    return removePhase != null ? removePhase.toString() : "";
  }

  private String taskToString() {
    return task != null ? Long.toString(task.getID()) : "";
  }

  @Override
  public String toString() {
    return ID + "#" + number + "#" + date.toString() + "#" + type + "#" + injectPhase.toString() + "#"
        + removePhaseToString() + "#" + fixTime + "#" + description + "#" + fixTimeManual + "#" + taskToString() + "#"
        + filePath;
  }

  /**
   * updates the fix time of this record
   * 
   * @param fixTime the fix time
   */
  public void updateFixTime(long fixTime) {
    this.fixTime += fixTime;

    if (fixTime < 0) {
      fixTime = 0;
    }
  }

  /**
   * Sets the remove phase of the defect
   * 
   * @param phase the remove phase
   */
  public void setRemovePhase(Phase phase) {
    this.removePhase = phase;
  }

}
