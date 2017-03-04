package de.uni.bremen.stummk.psp.data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

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
import de.uni.bremen.stummk.psp.control.TaskOverview;
import de.uni.bremen.stummk.psp.utility.Constants;

/**
 * Represents a task
 * 
 * @author Konstantin
 *
 */
@Entity
@Table(name = "task")
public class Task implements PersistenceItem {

  @ManyToOne
  private Project project;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long ID;

  @JoinColumn
  private String name;

  @JoinColumn
  @Enumerated(EnumType.STRING)
  private Phase phase;

  @JoinColumn
  private long plannedTime = 0;

  @JoinColumn
  private double plannedValue = 0;

  @JoinColumn
  private long plannedCumulativeHours = 0;

  @JoinColumn
  private double plannedCumulativeValues = 0;

  @JoinColumn
  private LocalDate plannedFinishDate;

  @JoinColumn
  private LocalDate actualDate;

  @JoinColumn
  private double actualEarnedValue = 0;

  @JoinColumn
  private double actualCumulativeEarnedValue = 0;

  @JoinColumn
  private long estimatedDefectInjected = 0;

  @JoinColumn
  private long estimatedDefectRemoved = 0;

  @JoinColumn
  private long actualTime = 0;

  @JoinColumn
  private boolean isComplete = false;

  @JoinColumn
  private long planLoc = 0;

  @JoinColumn
  private String status = "";

  @JoinColumn
  private LocalDateTime lastChange;

  @JoinColumn
  private int priority;

  @JoinColumn
  private String type = "";


  /**
   * Constructor
   */
  public Task() {
    // empty for jpa purpose
  }

  /**
   * Constructor
   * 
   * @param name the name of the task
   * @param priority the priority of the Task
   * @param phase the {@link Phase} of the task
   * @param planTime the planned time for the task in minute
   * @param date the planned monday, when task will be completed
   * @param planLoc the planned line of codes of the task
   * @param planDefInj the planned injected defects by this task
   * @param planDefRmd the planned removed defects by this task
   * @param project the project the task belongs to
   * @param type the TYpe of the task {@link Constants.TASK_TYPE_TASK} or
   *        {@link Constants.TASK_TYPE_DEFECT_FIX}
   */
  public Task(String name, int priority, Phase phase, long planTime, LocalDate date, long planLoc, long planDefInj,
      long planDefRmd, Project project, String type) {
    this.name = name;
    this.phase = phase;
    this.plannedTime = planTime;
    this.plannedFinishDate = date;
    this.planLoc = planLoc;
    this.estimatedDefectInjected = planDefInj;
    this.estimatedDefectRemoved = planDefRmd;
    this.project = project;
    this.lastChange = LocalDateTime.MIN;
    this.status = Constants.STATUS_UNREADY;
    this.priority = priority;
    this.type = type;
  }

  /**
   * Constructor
   * 
   * @param ID the id
   * @param name the name of the task
   * @param phase the {@link Phase} of the task
   * @param planTime the planned time for the task in minute
   * @param plannedValue the planned value
   * @param plannedCumulativeHours the planned cumulative hours
   * @param plannedCumulativeValues the the planned cumulative values
   * @param planMonday the planned monday, when task will be completed
   * @param planLoc the planned line of codes of the task
   * @param planDefInj the planned injected defects by this task
   * @param planDefRmd the planned removed defects by this task
   * @param actualDate the actual date
   * @param actualEarnedValue the actual earned value
   * @param actualCumulativeEarnedValue the actual cumulative earned value
   * @param actualTime the actual time
   * @param isComplete is this task complete
   * @param status the status
   * @param lastChange the last change date
   * @param priority the priority of the task
   * @param type type the TYpe of the task {@link Constants.TASK_TYPE_TASK} or
   *        {@link Constants.TASK_TYPE_DEFECT_FIX}
   * @param project the project the task belongs to
   */
  public Task(long ID, String name, Phase phase, long planTime, double plannedValue, long plannedCumulativeHours,
      double plannedCumulativeValues, LocalDate planMonday, long planLoc, long planDefInj, long planDefRmd,
      String actualDate, double actualEarnedValue, double actualCumulativeEarnedValue, long actualTime,
      boolean isComplete, String status, String lastChange, int priority, String type, Project project) {
    this.ID = ID;
    this.name = name;
    this.phase = phase;
    this.plannedTime = planTime;
    this.plannedValue = plannedValue;
    this.plannedCumulativeHours = plannedCumulativeHours;
    this.plannedCumulativeValues = plannedCumulativeValues;
    this.plannedFinishDate = planMonday;
    this.planLoc = planLoc;
    this.estimatedDefectInjected = planDefInj;
    this.estimatedDefectRemoved = planDefRmd;
    this.actualDate = actualDate.isEmpty() ? null : LocalDate.parse(actualDate);
    this.actualEarnedValue = actualEarnedValue;
    this.actualCumulativeEarnedValue = actualCumulativeEarnedValue;
    this.actualTime = actualTime;
    this.isComplete = isComplete;
    this.project = project;
    this.status = status;
    this.lastChange = lastChange.isEmpty() ? LocalDateTime.MIN : LocalDateTime.parse(lastChange);
    this.priority = priority;
    this.type = type;
  }

  /**
   * @return the project of the task
   */
  public Project getProject() {
    return this.project;
  }

  @Override
  public long getID() {
    return this.ID;
  }

  /**
   * @return the status of the task
   */
  public String getStatus() {
    return this.status;
  }

  /**
   * @return the date, when task was last modified
   */
  public LocalDateTime getLastChange() {
    return lastChange;
  }

  @Override
  public List<String> getElements() {
    List<String> elements = new ArrayList<>();
    elements.add(name);
    elements.add(getHourFromMinString(this.plannedTime));
    elements.add("" + MathCalc.getInstance().round(plannedValue));
    elements.add(getHourFromMinString(this.plannedCumulativeHours));
    elements.add("" + MathCalc.getInstance().round(plannedCumulativeValues));
    elements.add(plannedFinishDate.toString());
    elements.add(getActualDateToString());
    elements.add("" + MathCalc.getInstance().round(actualEarnedValue));
    elements.add("" + MathCalc.getInstance().round(actualCumulativeEarnedValue));

    return elements;
  }

  /**
   * @return the elements for the table of the {@link TaskOverview}
   */
  public List<String> getOverviewElements() {
    List<String> elements = new ArrayList<>();
    elements.add("");
    elements.add(name);
    elements.add("" + priority);
    elements.add("" + plannedTime);
    elements.add("" + MathCalc.getInstance().fromSecondToMinute(actualTime));
    elements.add("" + MathCalc.getInstance().round(plannedValue));
    elements.add("" + MathCalc.getInstance().round(actualEarnedValue));
    elements.add(getStatus());
    elements.add(lastChange.isEqual(LocalDateTime.MIN) ? ""
        : lastChange.toLocalDate().toString() + " " + lastChange.toLocalTime().toString());
    return elements;
  }

  /**
   * @return the task name
   */
  public String getName() {
    return this.name;
  }

  /**
   * @return the phase of the task
   */
  public Phase getPhase() {
    return this.phase;
  }

  /**
   * @return the planned time in min
   */
  public long getPlanTime() {
    return this.plannedTime;
  }

  /**
   * @return the planned value
   */
  public double getPlanValue() {
    return this.plannedValue;
  }

  /**
   * @return the planned monday
   */
  public LocalDate getPlannedMonday() {
    return this.plannedFinishDate;
  }

  /**
   * @return the planned line of codes
   */
  public long getPlanLoc() {
    return this.planLoc;
  }

  /**
   * @return the planned injected defects
   */
  public long getPlanDefInjected() {
    return this.estimatedDefectInjected;
  }

  /**
   * @return the priority of the task
   */
  public int getPriority() {
    return this.priority;
  }

  /**
   * @return the planned removed defects
   */
  public long getPlanDefRemoved() {
    return this.estimatedDefectRemoved;
  }

  /**
   * Refreshes the attributes of the task
   * 
   * @param name the name of the task
   * @param phase the {@link Phase} of the task
   * @param planTime the planned time for the task in min
   * @param date the planned monday, when task will be completed
   * @param planLoc the planned line of codes of the task
   * @param planDefInj the planned injected defects by this task
   * @param planDefRmd the planned removed defects by this task
   */
  public void refreshItems(String name, int priority, Phase phase, long planTime, LocalDate date, long planLoc,
      long planDefInj, long planDefRmd) {
    this.name = name;
    this.priority = priority;
    this.phase = phase;
    this.plannedTime = planTime;
    this.plannedFinishDate = date;
    this.planLoc = planLoc;
    this.estimatedDefectInjected = planDefInj;
    this.estimatedDefectRemoved = planDefRmd;
  }

  /**
   * Mark the task as complete
   * 
   * @param date the date when the task is completed
   */
  public void markAsComplete(LocalDate date) {
    this.actualDate = date;
    this.actualEarnedValue = this.plannedValue;
    this.isComplete = true;
    this.status = Constants.STATUS_READY;
  }

  /**
   * Sets the modification date of the task
   * 
   * @param lastChange the modification date
   */
  public void setLastChange(LocalDateTime lastChange) {
    this.lastChange = lastChange;
  }

  /**
   * @return if task is complete
   */
  public boolean isComplete() {
    return this.isComplete;
  }

  /**
   * Sets the planned value to the task
   * 
   * @param plannedValue the planned value
   */
  public void setPlannedValue(double plannedValue) {
    this.plannedValue = plannedValue;
  }

  /**
   * Sets the actual time of the task in seconds
   * 
   * @param deltaTime
   */
  public void setActualTime(long deltaTime) {
    this.actualTime += deltaTime;
  }

  /**
   * @return the actual earned value
   */
  public double getEarnedValue() {
    return this.actualEarnedValue;
  }

  /**
   * Set the new value of the actual earned value
   * 
   * @param value the new value
   */
  public void setEarnedValue(double value) {
    this.actualEarnedValue = value;
  }


  /**
   * @return the planned Cumulative Hours
   */
  public long getPlannedCumulativeHours() {
    return plannedCumulativeHours;
  }

  /**
   * @return the planned Cumulative Values
   */
  public double getPlannedCumulativeValues() {
    return plannedCumulativeValues;
  }

  /**
   * @return the actualTime in seconds
   */
  public long getActualTime() {
    return actualTime;
  }

  /**
   * @return the date, when the task was completed
   */
  public LocalDate getCompleteDate() {
    return this.actualDate;
  }

  /**
   * Sets new values to the cumulative values
   * 
   * @param cumPlHour the cumulative planned hour
   * @param cumPlValue the cumulative planned value
   * @param cumActValue the cumulative actual value
   */
  public void setCumulativeValues(long cumPlHour, double cumPlValue, double cumActValue) {
    this.plannedCumulativeHours = cumPlHour;
    this.plannedCumulativeValues = cumPlValue;
    this.actualCumulativeEarnedValue = cumActValue;
  }

  private String getActualDateToString() {
    return isComplete() && actualDate != null ? actualDate.toString() : "";
  }

  private String getHourFromMinString(long minutes) {
    return "" + TimeUnit.MINUTES.toHours(minutes) + "h : " + minutes % 60 + "min";
  }

  /**
   * @return the type of the task {@link Constants.TASK_TYPE_TASK} or
   *         {@link Constants.TASK_TYPE_DEFECT_FIX}
   */
  public String getType() {
    return type;
  }

  @Override
  public String toString() {
    return ID + "#" + name + "#" + phase.toString() + "#" + plannedTime + "#" + plannedValue + "#"
        + plannedCumulativeHours + "#" + plannedCumulativeValues + "#" + plannedFinishDate.toString() + "#" + planLoc
        + "#" + estimatedDefectInjected + "#" + estimatedDefectRemoved + "#" + getActualDateToString() + "#"
        + actualEarnedValue + "#" + actualCumulativeEarnedValue + "#" + actualTime + "#" + isComplete + "#" + status
        + "#" + lastChange.toString() + "#" + priority + "#" + type;
  }

  /**
   * Sets the status of the task
   * 
   * @param status {@link Constants.STATUS_UNREADY} <br>
   *        {@link Constants.STATUS_RUNNING} <br>
   *        {@link Constants.STATUS_INTERRUPT} <br>
   *        {@link Constants.STATUS_STOPPED} <br>
   *        {@link Constants.STATUS_READY} <br>
   * 
   */
  public void setStatus(String status) {
    this.status = status;
  }

  /**
   * updates the actual hours
   * 
   * @param value the value to be updated
   */
  public void update(long value) {
    this.actualTime += value;
    if (this.actualTime < 0) {
      actualTime = 0;
    }
  }

  /**
   * Sets a new name to the task
   * 
   * @param string the new name
   */
  public void setName(String string) {
    this.name = string;
  }
}
