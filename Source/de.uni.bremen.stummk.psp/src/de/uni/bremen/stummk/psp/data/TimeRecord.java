package de.uni.bremen.stummk.psp.data;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import de.uni.bremen.stummk.psp.calculation.MathCalc;

/**
 * Represents an time record entry
 * 
 * @author Konstantin
 *
 */
@Entity
@Table(name = "time_record")
public class TimeRecord implements PersistenceItem {

  @ManyToOne
  private Project project;

  @ManyToOne(cascade = CascadeType.PERSIST)
  private Task task;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long ID;

  @JoinColumn
  private LocalDate date;

  @JoinColumn
  private LocalDate enddate;

  @JoinColumn
  private LocalTime starttime;

  @JoinColumn
  private LocalTime endtime;

  @JoinColumn
  private long interruptTime = 0;

  @JoinColumn
  private long deltaTime = 0;

  @JoinColumn
  @Enumerated(EnumType.STRING)
  protected Phase phase;

  @JoinColumn
  private String comment = "";

  /**
   * Constructor
   */
  public TimeRecord() {
    // empty for jpa purpose
  }

  /**
   * Constructor
   * 
   * @param date the start date of this time record
   * @param enddate the end date of this time record
   * @param starttime the start time of the time record
   * @param endtime the end time of the time record
   * @param interuptMin the interrupt time in minutes
   * @param phase the {@link Phase} of this time record
   * @param comment comment
   * @param project the project.the time record belongs to
   */
  public TimeRecord(LocalDate date, LocalDate enddate, LocalTime starttime, LocalTime endtime, int interuptMin,
      Phase phase, String comment, Project project) {
    this.date = date;
    this.enddate = enddate;
    this.starttime = starttime;
    this.endtime = endtime;
    this.interruptTime = MathCalc.getInstance().fromMinuteToSecond(interuptMin);
    this.phase = phase;
    this.comment = comment;
    this.project = project;
    this.deltaTime = MathCalc.getInstance().getSeconds(enddate, endtime, date, starttime) - interuptMin;
  }

  /**
   * Constructor
   * 
   * @param ID the id
   * @param date the start date of this time record
   * @param enddate the end date of this time record
   * @param starttime the start time of the time record
   * @param endtime the end time of the time record
   * @param interruptTime the interrupt time in minutes
   * @param deltaTime the delta time of ths record
   * @param phase the {@link Phase} of this time record
   * @param comment comment
   * @param project the project.the time record belongs to
   */
  public TimeRecord(long ID, LocalDate date, LocalDate enddate, LocalTime starttime, LocalTime endtime,
      int interruptTime, long deltaTime, Phase phase, String comment, Project project) {
    this.ID = ID;
    this.date = date;
    this.enddate = enddate;
    this.starttime = starttime;
    this.endtime = endtime;
    this.interruptTime = interruptTime;
    this.deltaTime = deltaTime;
    this.phase = phase;
    this.comment = comment;
    this.project = project;
  }

  /**
   * Constructor
   * 
   * @param startdate the start date of the time record
   * @param starttime the start time of the time record
   * @param project the project the time record belongs to
   * @param phase the {@link Phase} of the time record
   */
  public TimeRecord(LocalDate startdate, LocalTime starttime, Project project, Phase phase, Task task) {
    this.project = project;
    this.date = startdate;
    this.starttime = starttime;
    this.phase = phase;
    this.task = task;
  }

  /**
   * @return the phase of the entry
   */
  public Phase getPhase() {
    return this.phase;
  }

  /**
   * @return the project of the time record
   */
  public Project getProject() {
    return this.project;
  }

  /**
   * Sets the end time of the time record
   * 
   * @param enddate the end date of the time record
   * @param endtime the time, when entry has been stopped
   */
  public void setEndtime(LocalDate enddate, LocalTime endtime) {
    this.enddate = enddate;
    this.endtime = endtime;
    this.deltaTime =
        MathCalc.getInstance().getSeconds(this.enddate, endtime, this.date, this.starttime) - interruptTime;
  }

  /**
   * @return the start date
   */
  public LocalDate getDate() {
    return date;
  }

  /**
   * @return the start time
   */
  public LocalTime getStarttime() {
    return this.starttime;
  }

  /**
   * @return the end time
   */
  public LocalTime getEndtime() {
    return endtime;
  }

  /**
   * @return the delta Time in seconds
   */
  public long getDeltaTime() {
    return deltaTime;
  }

  /**
   * @return the comment
   */
  public String getComment() {
    return comment;
  }

  /**
   * @return the task of this record
   */
  public Task getTask() {
    return this.task;
  }

  /**
   * Sets one task to the record
   * 
   * @param task the task
   */
  public void setTask(Task task) {
    this.task = task;
  }

  /**
   * @return the interrupt time in seconds
   */
  public long getInterruptTime() {
    return this.interruptTime;
  }

  @Override
  public long getID() {
    return ID;
  }

  @Override
  public List<String> getElements() {
    List<String> elements = new ArrayList<>();
    elements.add(date.toString());
    elements.add(starttime.toString());
    elements.add(endTimeToString());
    elements.add("" + MathCalc.getInstance().fromSecondToMinute(interruptTime));
    elements.add("" + MathCalc.getInstance().fromSecondToMinute(deltaTime));
    elements.add(phase.toString());
    elements.add(comment);

    return elements;
  }

  /**
   * Refreshes the attributes of a time record
   * 
   * @param date the start date
   * @param enddate the end date
   * @param starttime the start time
   * @param endtime the end time
   * @param interuptMin interrupt time in minutes
   * @param phase the {@link Phase} of the time record
   * @param comment a comment
   */
  public void refreshItems(LocalDate date, LocalDate enddate, LocalTime starttime, LocalTime endtime, int interuptMin,
      Phase phase, String comment) {
    this.date = date;
    this.enddate = enddate;
    this.starttime = starttime;
    this.endtime = endtime;
    this.interruptTime = MathCalc.getInstance().fromMinuteToSecond(interuptMin);
    this.deltaTime = MathCalc.getInstance().getSeconds(enddate, endtime, date, starttime) - interuptMin;
    this.phase = phase;
    this.comment = comment;
  }

  /**
   * @return the end date of the time record
   */
  public LocalDate getEndDate() {
    return this.enddate;
  }

  private String endTimeToString() {
    return endtime != null ? endtime.toString() : "";
  }

  private String taskToString() {
    return task != null ? Long.toString(task.getID()) : "";
  }

  /**
   * Sets the interruption time of this time record
   * 
   * @param interruptTime the new interruption time value
   */
  public void setInterruptTime(long interruptTime) {
    this.interruptTime = interruptTime;
  }

  @Override
  public String toString() {
    return ID + "#" + date.toString() + "#" + enddate.toString() + "#" + starttime.toString() + "#" + endtime.toString()
        + "#" + interruptTime + "#" + deltaTime + "#" + phase.toString() + "#" + comment + "#" + taskToString();
  }

}
