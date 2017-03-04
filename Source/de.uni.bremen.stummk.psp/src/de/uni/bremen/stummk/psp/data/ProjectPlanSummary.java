package de.uni.bremen.stummk.psp.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import de.uni.bremen.stummk.psp.utility.Constants;

/**
 * Represents the project plan summary
 * 
 * @author Konstantin
 *
 */
@Entity
@Table(name = "project_plan_summary")
public class ProjectPlanSummary implements PersistenceItem {

  @OneToOne
  private Project project;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long ID;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  private List<Summary> summary;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  private List<ProgramSize> programSize;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  private List<TimeInPhase> timeInPhase;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  private List<DefectsInjected> defectsInjected;

  @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
  private List<DefectsRemoved> defectsRemoved;

  /**
   * Constructor
   */
  public ProjectPlanSummary() {
    initialize();
  }

  /**
   * Constructor
   * 
   * @param project the project, the project plan summary belongs to
   */
  public ProjectPlanSummary(Project project) {
    this.project = project;
    initialize();
  }

  /**
   * Constructor
   * 
   * @param ID the id of the project plan summary
   * @param project the project, the project plan summary belongs to
   */
  public ProjectPlanSummary(long ID, Project project) {
    this.ID = ID;
    this.project = project;
  }

  /**
   * @return the project of the project plan summary
   */
  public Project getProject() {
    return this.project;
  }

  private void initialize() {
    // initializes the project plan summary
    this.summary = new ArrayList<Summary>();
    this.programSize = new ArrayList<ProgramSize>();
    this.timeInPhase = new ArrayList<TimeInPhase>();
    this.defectsInjected = new ArrayList<DefectsInjected>();
    this.defectsRemoved = new ArrayList<DefectsRemoved>();

    summary.add(new Summary(Constants.KEY_SUM_LOC_PER_HOUR));
    summary.add(new Summary(Constants.KEY_SUM_TIME));
    summary.add(new Summary(Constants.KEY_SUM_DEFECTS_PER_LOC));
    summary.add(new Summary(Constants.KEY_SUM_YIELD));
    summary.add(new Summary(Constants.KEY_SUM_AF_RATIO));

    programSize.add(new ProgramSize(Constants.KEY_SIZE_TOTAL_LOC));

    timeInPhase.add(new TimeInPhase(Constants.KEY_PHASE_PLANNING));
    timeInPhase.add(new TimeInPhase(Constants.KEY_PHASE_DESIGN));
    timeInPhase.add(new TimeInPhase(Constants.KEY_PHASE_DESIGN_REVIEW));
    timeInPhase.add(new TimeInPhase(Constants.KEY_PHASE_CODE));
    timeInPhase.add(new TimeInPhase(Constants.KEY_PHASE_CODE_REVIEW));
    timeInPhase.add(new TimeInPhase(Constants.KEY_PHASE_COMPILE));
    timeInPhase.add(new TimeInPhase(Constants.KEY_PHASE_TEST));
    timeInPhase.add(new TimeInPhase(Constants.KEY_PHASE_POSTMORTEM));
    timeInPhase.add(new TimeInPhase(Constants.KEY_PHASE_TOTAL));

    defectsInjected.add(new DefectsInjected(Constants.KEY_PHASE_PLANNING));
    defectsInjected.add(new DefectsInjected(Constants.KEY_PHASE_DESIGN));
    defectsInjected.add(new DefectsInjected(Constants.KEY_PHASE_DESIGN_REVIEW));
    defectsInjected.add(new DefectsInjected(Constants.KEY_PHASE_CODE));
    defectsInjected.add(new DefectsInjected(Constants.KEY_PHASE_CODE_REVIEW));
    defectsInjected.add(new DefectsInjected(Constants.KEY_PHASE_COMPILE));
    defectsInjected.add(new DefectsInjected(Constants.KEY_PHASE_TEST));
    defectsInjected.add(new DefectsInjected(Constants.KEY_PHASE_POSTMORTEM));
    defectsInjected.add(new DefectsInjected(Constants.KEY_PHASE_TOTAL));

    defectsRemoved.add(new DefectsRemoved(Constants.KEY_PHASE_PLANNING));
    defectsRemoved.add(new DefectsRemoved(Constants.KEY_PHASE_DESIGN));
    defectsRemoved.add(new DefectsRemoved(Constants.KEY_PHASE_DESIGN_REVIEW));
    defectsRemoved.add(new DefectsRemoved(Constants.KEY_PHASE_CODE));
    defectsRemoved.add(new DefectsRemoved(Constants.KEY_PHASE_CODE_REVIEW));
    defectsRemoved.add(new DefectsRemoved(Constants.KEY_PHASE_COMPILE));
    defectsRemoved.add(new DefectsRemoved(Constants.KEY_PHASE_TEST));
    defectsRemoved.add(new DefectsRemoved(Constants.KEY_PHASE_POSTMORTEM));
    defectsRemoved.add(new DefectsRemoved(Constants.KEY_PHASE_TOTAL));
  }

  @Override
  public long getID() {
    return ID;
  }

  @Override
  public List<String> getElements() {
    return Collections.emptyList();
  }

  /**
   * Returns the elements of a section in the project plan summary
   * 
   * @param index the index of the section <br>
   *        {@link Constants.KEY_SUMMARY_IDX} <br>
   *        {@link Constants.KEY_PROGRAM_SIZE_IDX} <br>
   *        {@link Constants.KEY_TIME_IN_PHASE_IDX} <br>
   *        {@link Constants.KEY_DEFECTS_INJECTED_IDX} <br>
   *        {@link Constants.KEY_DEFECTS_REMOVED_IDX} <br>
   * 
   * @return the elements of the given section
   */
  public List<? extends SummaryItem> getSectionContent(int index) {
    switch (index) {
      case Constants.KEY_SUMMARY_IDX:
        return this.summary;
      case Constants.KEY_PROGRAM_SIZE_IDX:
        return this.programSize;
      case Constants.KEY_TIME_IN_PHASE_IDX:
        return this.timeInPhase;
      case Constants.KEY_DEFECTS_INJECTED_IDX:
        return this.defectsInjected;
      case Constants.KEY_DEFECTS_REMOVED_IDX:
        return this.defectsRemoved;
    }
    return Collections.emptyList();
  }

  /**
   * Updates the values of the {@link ProjectPlanSummary} by summing the actual value with the new
   * one. The following keys can be used in the sections: Not all keys are available in all
   * sections. <br>
   * section: <br>
   * {@link Constants.KEY_SUMMARY_IDX} <br>
   * row keys : <br>
   * {@link Constants.KEY_SUM_LOC_PER_HOUR} <br>
   * {@link Constants.KEY_SUM_TIME} <br>
   * {@link Constants.KEY_SUM_DEFECTS_PER_LOC} <br>
   * {@link Constants.KEY_SUM_YIELD} <br>
   * {@link Constants.KEY_SUM_AF_RATIO } <br>
   * column keys: <br>
   * {@link Constants.KEY_PLAN} <br>
   * {@link Constants.KEY_ACTUAL} <br>
   * {@link Constants.KEY_TO_DATE} <br>
   * <br>
   * section: <br>
   * {@link Constants.KEY_PROGRAM_SIZE_IDX} <br>
   * row keys : <br>
   * {@link Constants.KEY_SIZE_TOTAL_LOC} <br>
   * column keys: <br>
   * {@link Constants.KEY_PLAN} <br>
   * {@link Constants.KEY_ACTUAL} <br>
   * {@link Constants.KEY_TO_DATE} <br>
   * <br>
   * section: <br>
   * {@link Constants.KEY_TIME_IN_PHASE_IDX} <br>
   * row keys : <br>
   * {@link Constants.KEY_PHASE_PLANNING} <br>
   * {@link Constants.KEY_PHASE_DESIGN} <br>
   * {@link Constants.KEY_PHASE_DESIGN_REVIEW} <br>
   * {@link Constants.KEY_PHASE_CODE} <br>
   * {@link Constants.KEY_PHASE_CODE_REVIEW} <br>
   * {@link Constants.KEY_PHASE_COMPILE} <br>
   * {@link Constants.KEY_PHASE_TEST} <br>
   * {@link Constants.KEY_PHASE_POSTMORTEM} <br>
   * {@link Constants.KEY_PHASE_TOTAL} <br>
   * column keys: <br>
   * {@link Constants.KEY_PLAN} <br>
   * {@link Constants.KEY_ACTUAL} <br>
   * {@link Constants.KEY_TO_DATE} <br>
   * {@link Constants.KEY_TO_DATE_PER} <br>
   * <br>
   * section: <br>
   * {@link Constants.KEY_DEFECTS_INJECTED_IDX} <br>
   * row keys : <br>
   * {@link Constants.KEY_PHASE_PLANNING} <br>
   * {@link Constants.KEY_PHASE_DESIGN} <br>
   * {@link Constants.KEY_PHASE_DESIGN_REVIEW} <br>
   * {@link Constants.KEY_PHASE_CODE} <br>
   * {@link Constants.KEY_PHASE_CODE_REVIEW} <br>
   * {@link Constants.KEY_PHASE_COMPILE} <br>
   * {@link Constants.KEY_PHASE_TEST} <br>
   * {@link Constants.KEY_PHASE_POSTMORTEM} <br>
   * {@link Constants.KEY_PHASE_TOTAL} <br>
   * column keys: <br>
   * {@link Constants.KEY_PLAN} <br>
   * {@link Constants.KEY_ACTUAL} <br>
   * {@link Constants.KEY_TO_DATE} <br>
   * {@link Constants.KEY_TO_DATE_PER} <br>
   * {@link Constants.KEY_DEFECT_PER_HOUR} <br>
   * <br>
   * section: <br>
   * {@link Constants.KEY_DEFECTS_REMOVED_IDX} <br>
   * row keys : <br>
   * {@link Constants.KEY_PHASE_PLANNING} <br>
   * {@link Constants.KEY_PHASE_DESIGN} <br>
   * {@link Constants.KEY_PHASE_DESIGN_REVIEW} <br>
   * {@link Constants.KEY_PHASE_CODE} <br>
   * {@link Constants.KEY_PHASE_CODE_REVIEW} <br>
   * {@link Constants.KEY_PHASE_COMPILE} <br>
   * {@link Constants.KEY_PHASE_TEST} <br>
   * {@link Constants.KEY_PHASE_POSTMORTEM} <br>
   * {@link Constants.KEY_PHASE_TOTAL} <br>
   * column keys: <br>
   * {@link Constants.KEY_PLAN} <br>
   * {@link Constants.KEY_ACTUAL} <br>
   * {@link Constants.KEY_TO_DATE} <br>
   * {@link Constants.KEY_TO_DATE_PER} <br>
   * {@link Constants.KEY_DEFECT_PER_HOUR} <br>
   * 
   * @param tableKey the key of the section
   * @param rowKey the key of the row
   * 
   * @param columnKey the key of the column
   * @param value the new value
   */
  public void updateValue(int tableKey, String rowKey, String columnKey, double value) {
    List<? extends SummaryItem> list = getSectionContent(tableKey);
    SummaryItem si = null;

    for (SummaryItem item : list) {
      if (item.getTitle().equals(rowKey)) {
        si = item;
      }
    }

    double v = si.get(columnKey) + value;

    if (v < 0) {
      v = 0;
    }

    si.put(columnKey, v);
  }

  /**
   * Returns a value from given section, row and column in the {@link ProjectPlanSummary}. The
   * following keys can be used in the sections: Not all keys are available in all sections. <br>
   * section: <br>
   * {@link Constants.KEY_SUMMARY_IDX} <br>
   * row keys : <br>
   * {@link Constants.KEY_SUM_LOC_PER_HOUR} <br>
   * {@link Constants.KEY_SUM_TIME} <br>
   * {@link Constants.KEY_SUM_DEFECTS_PER_LOC} <br>
   * {@link Constants.KEY_SUM_YIELD} <br>
   * {@link Constants.KEY_SUM_AF_RATIO } <br>
   * column keys: <br>
   * {@link Constants.KEY_PLAN} <br>
   * {@link Constants.KEY_ACTUAL} <br>
   * {@link Constants.KEY_TO_DATE} <br>
   * <br>
   * section: <br>
   * {@link Constants.KEY_PROGRAM_SIZE_IDX} <br>
   * row keys : <br>
   * {@link Constants.KEY_SIZE_TOTAL_LOC} <br>
   * column keys: <br>
   * {@link Constants.KEY_PLAN} <br>
   * {@link Constants.KEY_ACTUAL} <br>
   * {@link Constants.KEY_TO_DATE} <br>
   * <br>
   * section: <br>
   * {@link Constants.KEY_TIME_IN_PHASE_IDX} <br>
   * row keys : <br>
   * {@link Constants.KEY_PHASE_PLANNING} <br>
   * {@link Constants.KEY_PHASE_DESIGN} <br>
   * {@link Constants.KEY_PHASE_DESIGN_REVIEW} <br>
   * {@link Constants.KEY_PHASE_CODE} <br>
   * {@link Constants.KEY_PHASE_CODE_REVIEW} <br>
   * {@link Constants.KEY_PHASE_COMPILE} <br>
   * {@link Constants.KEY_PHASE_TEST} <br>
   * {@link Constants.KEY_PHASE_POSTMORTEM} <br>
   * {@link Constants.KEY_PHASE_TOTAL} <br>
   * column keys: <br>
   * {@link Constants.KEY_PLAN} <br>
   * {@link Constants.KEY_ACTUAL} <br>
   * {@link Constants.KEY_TO_DATE} <br>
   * {@link Constants.KEY_TO_DATE_PER} <br>
   * <br>
   * section: <br>
   * {@link Constants.KEY_DEFECTS_INJECTED_IDX} <br>
   * row keys : <br>
   * {@link Constants.KEY_PHASE_PLANNING} <br>
   * {@link Constants.KEY_PHASE_DESIGN} <br>
   * {@link Constants.KEY_PHASE_DESIGN_REVIEW} <br>
   * {@link Constants.KEY_PHASE_CODE} <br>
   * {@link Constants.KEY_PHASE_CODE_REVIEW} <br>
   * {@link Constants.KEY_PHASE_COMPILE} <br>
   * {@link Constants.KEY_PHASE_TEST} <br>
   * {@link Constants.KEY_PHASE_POSTMORTEM} <br>
   * {@link Constants.KEY_PHASE_TOTAL} <br>
   * column keys: <br>
   * {@link Constants.KEY_PLAN} <br>
   * {@link Constants.KEY_ACTUAL} <br>
   * {@link Constants.KEY_TO_DATE} <br>
   * {@link Constants.KEY_TO_DATE_PER} <br>
   * {@link Constants.KEY_DEFECT_PER_HOUR} <br>
   * <br>
   * section: <br>
   * {@link Constants.KEY_DEFECTS_REMOVED_IDX} <br>
   * row keys : <br>
   * {@link Constants.KEY_PHASE_PLANNING} <br>
   * {@link Constants.KEY_PHASE_DESIGN} <br>
   * {@link Constants.KEY_PHASE_DESIGN_REVIEW} <br>
   * {@link Constants.KEY_PHASE_CODE} <br>
   * {@link Constants.KEY_PHASE_CODE_REVIEW} <br>
   * {@link Constants.KEY_PHASE_COMPILE} <br>
   * {@link Constants.KEY_PHASE_TEST} <br>
   * {@link Constants.KEY_PHASE_POSTMORTEM} <br>
   * {@link Constants.KEY_PHASE_TOTAL} <br>
   * column keys: <br>
   * {@link Constants.KEY_PLAN} <br>
   * {@link Constants.KEY_ACTUAL} <br>
   * {@link Constants.KEY_TO_DATE} <br>
   * {@link Constants.KEY_TO_DATE_PER} <br>
   * {@link Constants.KEY_DEFECT_PER_HOUR} <br>
   * 
   * @param tableKey the section key <br>
   * 
   * @param rowKey the row key
   * @param columnKey the column key. Not all Keys are available in all section <br>
   * @return the value of a given key
   */
  public double get(int tableKey, String rowKey, String columnKey) {
    List<? extends SummaryItem> list = getSectionContent(tableKey);
    SummaryItem si = null;

    for (SummaryItem item : list) {
      if (item.getTitle().equals(rowKey)) {
        si = item;
      }
    }

    return si.get(columnKey);
  }

  /**
   * Add a value to the given key of the {@link ProjectPlanSummary} by replacing the old value by
   * the new one.
   * 
   * The following keys can be used in the sections: Not all keys are available in all sections.
   * <br>
   * section: <br>
   * {@link Constants.KEY_SUMMARY_IDX} <br>
   * row keys : <br>
   * {@link Constants.KEY_SUM_LOC_PER_HOUR} <br>
   * {@link Constants.KEY_SUM_TIME} <br>
   * {@link Constants.KEY_SUM_DEFECTS_PER_LOC} <br>
   * {@link Constants.KEY_SUM_YIELD} <br>
   * {@link Constants.KEY_SUM_AF_RATIO } <br>
   * column keys: <br>
   * {@link Constants.KEY_PLAN} <br>
   * {@link Constants.KEY_ACTUAL} <br>
   * {@link Constants.KEY_TO_DATE} <br>
   * <br>
   * section: <br>
   * {@link Constants.KEY_PROGRAM_SIZE_IDX} <br>
   * row keys : <br>
   * {@link Constants.KEY_SIZE_TOTAL_LOC} <br>
   * column keys: <br>
   * {@link Constants.KEY_PLAN} <br>
   * {@link Constants.KEY_ACTUAL} <br>
   * {@link Constants.KEY_TO_DATE} <br>
   * <br>
   * section: <br>
   * {@link Constants.KEY_TIME_IN_PHASE_IDX} <br>
   * row keys : <br>
   * {@link Constants.KEY_PHASE_PLANNING} <br>
   * {@link Constants.KEY_PHASE_DESIGN} <br>
   * {@link Constants.KEY_PHASE_DESIGN_REVIEW} <br>
   * {@link Constants.KEY_PHASE_CODE} <br>
   * {@link Constants.KEY_PHASE_CODE_REVIEW} <br>
   * {@link Constants.KEY_PHASE_COMPILE} <br>
   * {@link Constants.KEY_PHASE_TEST} <br>
   * {@link Constants.KEY_PHASE_POSTMORTEM} <br>
   * {@link Constants.KEY_PHASE_TOTAL} <br>
   * column keys: <br>
   * {@link Constants.KEY_PLAN} <br>
   * {@link Constants.KEY_ACTUAL} <br>
   * {@link Constants.KEY_TO_DATE} <br>
   * {@link Constants.KEY_TO_DATE_PER} <br>
   * <br>
   * section: <br>
   * {@link Constants.KEY_DEFECTS_INJECTED_IDX} <br>
   * row keys : <br>
   * {@link Constants.KEY_PHASE_PLANNING} <br>
   * {@link Constants.KEY_PHASE_DESIGN} <br>
   * {@link Constants.KEY_PHASE_DESIGN_REVIEW} <br>
   * {@link Constants.KEY_PHASE_CODE} <br>
   * {@link Constants.KEY_PHASE_CODE_REVIEW} <br>
   * {@link Constants.KEY_PHASE_COMPILE} <br>
   * {@link Constants.KEY_PHASE_TEST} <br>
   * {@link Constants.KEY_PHASE_POSTMORTEM} <br>
   * {@link Constants.KEY_PHASE_TOTAL} <br>
   * column keys: <br>
   * {@link Constants.KEY_PLAN} <br>
   * {@link Constants.KEY_ACTUAL} <br>
   * {@link Constants.KEY_TO_DATE} <br>
   * {@link Constants.KEY_TO_DATE_PER} <br>
   * {@link Constants.KEY_DEFECT_PER_HOUR} <br>
   * <br>
   * section: <br>
   * {@link Constants.KEY_DEFECTS_REMOVED_IDX} <br>
   * row keys : <br>
   * {@link Constants.KEY_PHASE_PLANNING} <br>
   * {@link Constants.KEY_PHASE_DESIGN} <br>
   * {@link Constants.KEY_PHASE_DESIGN_REVIEW} <br>
   * {@link Constants.KEY_PHASE_CODE} <br>
   * {@link Constants.KEY_PHASE_CODE_REVIEW} <br>
   * {@link Constants.KEY_PHASE_COMPILE} <br>
   * {@link Constants.KEY_PHASE_TEST} <br>
   * {@link Constants.KEY_PHASE_POSTMORTEM} <br>
   * {@link Constants.KEY_PHASE_TOTAL} <br>
   * column keys: <br>
   * {@link Constants.KEY_PLAN} <br>
   * {@link Constants.KEY_ACTUAL} <br>
   * {@link Constants.KEY_TO_DATE} <br>
   * {@link Constants.KEY_TO_DATE_PER} <br>
   * {@link Constants.KEY_DEFECT_PER_HOUR} <br>
   * 
   * @param tableKey the key of the section<br>
   * @param rowKey the key of the row
   * @param columnKey the key of the column
   * @param value the new value
   */
  public void put(int tableKey, String rowKey, String columnKey, double value) {
    List<? extends SummaryItem> list = getSectionContent(tableKey);
    SummaryItem si = null;

    for (SummaryItem item : list) {
      if (item.getTitle().equals(rowKey)) {
        si = item;
      }
    }

    si.put(columnKey, value);
  }

  /**
   * Sets the summary section
   * 
   * @param summaries the summary section
   */
  public void setSummaries(List<Summary> summaries) {
    this.summary = summaries;
  }

  /**
   * Sets the size section
   * 
   * @param pSize the size section
   */
  public void setSize(List<ProgramSize> pSize) {
    this.programSize = pSize;
  }

  /**
   * Sets the time in phase section
   * 
   * @param tip the time in phase section
   */
  public void setTimeInPhase(List<TimeInPhase> tip) {
    this.timeInPhase = tip;

  }

  /**
   * Sets the defect injected section
   * 
   * @param di the defect injected section
   */
  public void setDefInj(List<DefectsInjected> di) {
    this.defectsInjected = di;
  }

  /**
   * Sets the defect removed section
   * 
   * @param dr the defect removed section
   */
  public void setDefRmd(List<DefectsRemoved> dr) {
    this.defectsRemoved = dr;
  }


}
