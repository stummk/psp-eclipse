package de.uni.bremen.stummk.psp.data;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import de.uni.bremen.stummk.psp.calculation.MathCalc;

/**
 * Represents an entry of the schedule plan
 * 
 * @author Konstantin
 *
 */
@Entity
@Table(name = "schedule_plan")
public class ScheduleEntry implements PersistenceItem {

  @ManyToOne
  private Project project;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long ID;

  @JoinColumn
  private long weekNumber;

  @JoinColumn
  private LocalDate dateMonday;

  @JoinColumn
  private long plannedHours;

  @JoinColumn
  private long cumulativePlannedHours;

  @JoinColumn
  private double cumulativeEarnedValue;

  @JoinColumn
  private long actualDirectHours;

  @JoinColumn
  private long actualCumulativeHour;

  @JoinColumn
  private double actualEarnedValue;

  /**
   * Constructor
   */
  public ScheduleEntry() {
    // empty for jpa purpose
  }

  /**
   * Constructor
   * 
   * @param project the project the schedule entry belongs to
   * @param weekNumber the week number of the schedule entry
   * @param item the item, which creates the schedule entry <br>
   *        {@link Task}, <br>
   *        {@link TimeRecord}, <br>
   *        {@link DefectRecord}, <br>
   */
  public ScheduleEntry(Project project, long weekNumber, PersistenceItem item) {
    this.project = project;
    this.weekNumber = weekNumber;

    if (item instanceof Task) {
      this.dateMonday = ((Task) item).getPlannedMonday();
      this.plannedHours = ((Task) item).getPlanTime();
      this.actualDirectHours = 0;
    }

    if (item instanceof TimeRecord) {
      this.dateMonday = MathCalc.getInstance().getMonday(((TimeRecord) item).getEndDate());
      this.plannedHours = 0;
      this.actualDirectHours = ((TimeRecord) item).getDeltaTime();
    }

    if (item instanceof DefectRecord) {
      this.dateMonday = MathCalc.getInstance().getMonday(((DefectRecord) item).getDate());
      this.plannedHours = 0;
      this.actualDirectHours = MathCalc.getInstance().fromMinuteToSecond(((DefectRecord) item).getFixTime());
    }

    this.cumulativePlannedHours = 0;
    this.cumulativeEarnedValue = 0;
    this.actualCumulativeHour = 0;
    this.actualEarnedValue = 0;
  }

  /**
   * Constructor
   * 
   * @param ID the id
   * @param weekNumber the number of week of the schedule entry
   * @param monday the monday of the entry
   * @param planHours the plan hours of the entry
   * @param cumPlanHours the cumulative planned hours of the entry
   * @param cumEarnedValue the palnned cumulative earned value of the entry
   * @param directHours the direct hours of the entry
   * @param cumDirectHours the cumulative direct hours of the entry
   * @param actualEarnedValue the actual cumulative earned value of the entry
   * @param project the Project the schedule entry belongs to
   */
  public ScheduleEntry(long ID, int weekNumber, LocalDate monday, long planHours, long cumPlanHours,
      double cumEarnedValue, long directHours, long cumDirectHours, double actualEarnedValue, Project project) {
    this.ID = ID;
    this.weekNumber = weekNumber;
    this.dateMonday = monday;
    this.plannedHours = planHours;
    this.cumulativePlannedHours = cumPlanHours;
    this.cumulativeEarnedValue = cumEarnedValue;
    this.actualDirectHours = directHours;
    this.actualCumulativeHour = cumDirectHours;
    this.actualEarnedValue = actualEarnedValue;
    this.project = project;
  }

  /**
   * @return the week Number
   */
  public long getWeekNumber() {
    return weekNumber;
  }

  /**
   * @return the date Monday
   */
  public LocalDate getDateMonday() {
    return dateMonday;
  }

  /**
   * @return the planned Hours
   */
  public long getPlannedHours() {
    return plannedHours;
  }

  /**
   * @return the cumulative Planned Hours
   */
  public long getCumulativePlannedHours() {
    return cumulativePlannedHours;
  }

  /**
   * @return the planned cumulative Earned Value
   */
  public double getCumulativeEarnedValue() {
    return cumulativeEarnedValue;
  }

  /**
   * @return the actual Direct Hours in seconds
   */
  public long getActualDirectHours() {
    return actualDirectHours;
  }

  /**
   * @return the actual Cumulative Hour in seconds
   */
  public long getActualCumulativeHour() {
    return actualCumulativeHour;
  }

  /**
   * @return the actual cummulative Earned Value
   */
  public double getActualEarnedValue() {
    return actualEarnedValue;
  }

  /**
   * @return the project of the schedule entry
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
    elements.add("" + weekNumber);
    elements.add(dateMonday.toString());
    elements.add(getHourFromMinString(plannedHours));
    elements.add(getHourFromMinString(cumulativePlannedHours));
    elements.add("" + MathCalc.getInstance().round(cumulativeEarnedValue));
    elements.add(getHourString(actualDirectHours));
    elements.add(getHourString(actualCumulativeHour));
    elements.add("" + MathCalc.getInstance().round(actualEarnedValue));

    return elements;
  }

  /**
   * Updates the planned hours
   * 
   * @param plannedHours the planned hours in minutes, which should be added
   */
  public void updatePlannedValues(long plannedHours) {
    this.plannedHours += plannedHours;

    if (this.plannedHours < 0) {
      this.plannedHours = 0;
    }
  }

  /**
   * Updates the actual direct hours
   * 
   * @param actualTime the actual hours in seconds, which should be added
   */
  public void updateActualHour(long actualTime) {
    this.actualDirectHours += actualTime;

    if (this.actualDirectHours < 0) {
      this.actualDirectHours = 0;
    }
  }

  /**
   * Sets a new value to the planned cumulative hours
   * 
   * @param hours the hours in minutes
   */
  public void setCumulativePlannedHours(long hours) {
    this.cumulativePlannedHours = hours;
  }

  /**
   * Sets a new value to the actual cumulative hours
   * 
   * @param hours the hours in seconds
   */
  public void setCumulativeActualHours(long cumActualHour) {
    this.actualCumulativeHour = cumActualHour;
  }

  /**
   * @param minutes minutes
   * @return a String in format "h : min" of given minutes
   */
  private String getHourString(long minutes) {
    return "" + TimeUnit.SECONDS.toHours(minutes) + "h : " + TimeUnit.SECONDS.toMinutes(minutes) % 60 + "min";
  }

  /**
   * @param minutes minutes
   * @return a String in format "h : min" of given minutes
   */
  private String getHourFromMinString(long minutes) {
    return "" + TimeUnit.MINUTES.toHours(minutes) + "h : " + minutes % 60 + "min";
  }

  /**
   * Set a new value to the cumulative planned value
   * 
   * @param plannedCumulativeValues the new value of the cumulative planned value
   */
  public void setCumulativePlannedValue(double plannedCumulativeValues) {
    this.cumulativeEarnedValue = plannedCumulativeValues;
  }

  /**
   * Sets a new value to the cumulative actual value
   * 
   * @param cumulativeEarnedValue the new value of the cumulative actual value
   */
  public void setCumulativeValue(double cumulativeEarnedValue) {
    this.actualEarnedValue = cumulativeEarnedValue;
  }

  @Override
  public String toString() {
    return ID + "#" + weekNumber + "#" + dateMonday.toString() + "#" + plannedHours + "#" + cumulativePlannedHours + "#"
        + cumulativeEarnedValue + "#" + actualDirectHours + "#" + actualCumulativeHour + "#" + actualEarnedValue;
  }

}
