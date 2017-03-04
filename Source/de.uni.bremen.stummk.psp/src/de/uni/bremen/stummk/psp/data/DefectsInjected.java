package de.uni.bremen.stummk.psp.data;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import de.uni.bremen.stummk.psp.calculation.MathCalc;
import de.uni.bremen.stummk.psp.utility.Constants;

/**
 * Represents a row in the defects injected table in the {@link ProjectPlanSummary}
 * 
 * @author Konstantin
 *
 */
@Entity
@Table(name = "defects_injected")
public class DefectsInjected implements PersistenceItem, SummaryItem {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long ID;

  @JoinColumn
  private String title;

  @JoinColumn
  private double plan, actual, toDate, toDatePercentage, defPerHour;

  /**
   * Constructor
   */
  public DefectsInjected() {
    // empty for jpa purpose
  }

  /**
   * Constructor
   * 
   * @param title the title of the row
   */
  public DefectsInjected(String title) {
    this.title = title;
    this.plan = 0;
    this.actual = 0;
    this.toDate = 0;
    this.toDatePercentage = 0;
    this.defPerHour = 0;
  }

  /**
   * Constructor
   * 
   * @param ID the id
   * @param title the title of the row
   * @param plan the plan value
   * @param actual the actual value
   * @param toDate the to date value
   * @param toDatePercentage the to date percentage value
   * @param defPerHour the defect per hour value
   * 
   */
  public DefectsInjected(long ID, String title, double plan, double actual, double toDate, double toDatePercentage,
      double defPerHour) {
    this.ID = ID;
    this.title = title;
    this.plan = plan;
    this.actual = actual;
    this.toDate = toDate;
    this.toDatePercentage = toDatePercentage;
    this.defPerHour = defPerHour;
  }

  @Override
  public void put(String key, double value) {
    switch (key) {
      case Constants.KEY_PLAN:
        this.plan = value;
        break;
      case Constants.KEY_ACTUAL:
        this.actual = value;
        break;
      case Constants.KEY_TO_DATE:
        this.toDate = value;
        break;
      case Constants.KEY_TO_DATE_PER:
        this.toDatePercentage = value;
        break;
      case Constants.KEY_DEFECT_PER_HOUR:
        this.defPerHour = value;
    }
  }

  @Override
  public double get(String key) {
    switch (key) {
      case Constants.KEY_PLAN:
        return this.plan;
      case Constants.KEY_ACTUAL:
        return this.actual;
      case Constants.KEY_TO_DATE:
        return this.toDate;
      case Constants.KEY_TO_DATE_PER:
        return this.toDatePercentage;
      case Constants.KEY_DEFECT_PER_HOUR:
        return this.toDatePercentage;
    }
    return 0;
  }

  @Override
  public String getTitle() {
    return this.title;
  }

  @Override
  public long getID() {
    return this.ID;
  }

  @Override
  public List<String> getElements() {
    List<String> list = new ArrayList<>();
    list.add(this.title);
    list.add("" + MathCalc.getInstance().round(this.plan));
    list.add("" + MathCalc.getInstance().round(this.actual));
    list.add("" + MathCalc.getInstance().round(this.toDate));
    list.add("" + MathCalc.getInstance().round(this.toDatePercentage));
    list.add("" + MathCalc.getInstance().round(this.defPerHour));

    return list;
  }

  @Override
  public String toString() {
    return ID + "#" + title + "#" + plan + "#" + actual + "#" + toDate + "#" + toDatePercentage + "#" + defPerHour;
  }

  @Override
  public Project getProject() {
    return null;
  }

}
