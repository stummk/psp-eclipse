package de.uni.bremen.stummk.psp.data;

import java.util.Collections;
import java.util.List;

/**
 * Represents a backup instance of the project for backup purpose
 * 
 * @author Konstantin
 *
 */
public class PSPProject {

  private Project projectInfo;

  private List<PIP> pip;

  private List<TestReport> testReports;

  private List<DefectRecord> defectRecord;

  private List<ScheduleEntry> schedulePlan;

  private List<TimeRecord> timeRecords;

  private List<Task> tasks;

  private ProjectPlanSummary projectSummary;

  /**
   * Constructor
   * 
   * @param projectData the project
   * @param summary the project plan summary
   * @param pip the pip entries
   * @param testReports the test reports
   * @param defectRecord the defect reports
   * @param schedulePlan the schedule plan
   * @param timeRecords the time records
   * @param tasks the tasks
   */
  public PSPProject(Project projectData, ProjectPlanSummary summary, List<PIP> pip, List<TestReport> testReports,
      List<DefectRecord> defectRecord, List<ScheduleEntry> schedulePlan, List<TimeRecord> timeRecords,
      List<Task> tasks) {
    this.projectInfo = projectData;
    this.projectSummary = summary;
    this.pip = pip;
    this.testReports = testReports;
    this.defectRecord = defectRecord;
    this.schedulePlan = schedulePlan;
    this.timeRecords = timeRecords;
    this.tasks = tasks;
  }

  /**
   * Constructor
   * 
   * @param projectData the project of this backup
   * @param summary the project plan summary of the project
   */
  public PSPProject(Project projectData, ProjectPlanSummary summary) {
    this.projectInfo = projectData;
    this.projectSummary = summary;
  }

  public PSPProject() {}

  /**
   * @return the project
   */
  public Project getProject() {
    return this.projectInfo;
  }

  /**
   * @return the test report entries
   */
  public List<TestReport> getTestReport() {
    return testReports != null ? testReports : Collections.emptyList();
  }

  /**
   * @return the pip entries
   */
  public List<PIP> getPIP() {
    return pip != null ? pip : Collections.emptyList();
  }

  /**
   * @return the defect record entries
   */
  public List<DefectRecord> getDefectRecords() {
    return defectRecord != null ? defectRecord : Collections.emptyList();
  }

  /**
   * @return the schedule entries
   */
  public List<ScheduleEntry> getScheduleEntries() {
    return schedulePlan != null ? schedulePlan : Collections.emptyList();
  }

  /**
   * @return the time records
   */
  public List<TimeRecord> getTimeRecords() {
    return timeRecords != null ? timeRecords : Collections.emptyList();
  }

  /**
   * @return the tasks of the project
   */
  public List<Task> getTasks() {
    return this.tasks != null ? tasks : Collections.emptyList();
  }

  /**
   * @return the project plan summary
   */
  public ProjectPlanSummary getSummary() {
    return this.projectSummary;
  }

  /**
   * Sets the project
   * 
   * @param project the project
   */
  public void setProject(Project project) {
    this.projectInfo = project;
  }

  /**
   * Sets the project plan summary
   * 
   * @param pps the project plan summary
   */
  public void setPPS(ProjectPlanSummary pps) {
    this.projectSummary = pps;
  }

  /**
   * Sets the time records
   * 
   * @param tr the time records
   */
  public void setTimeRec(List<TimeRecord> tr) {
    this.timeRecords = tr;
  }

  /**
   * Sets the defect records
   * 
   * @param dRec the defect records
   */
  public void setDefRec(List<DefectRecord> dRec) {
    this.defectRecord = dRec;
  }

  /**
   * Sets the pip
   * 
   * @param pip the pip
   */
  public void setPIP(List<PIP> pip) {
    this.pip = pip;
  }

  /**
   * Sets the test report
   * 
   * @param tRep the test report
   */
  public void setTestReport(List<TestReport> tRep) {
    this.testReports = tRep;
  }

  /**
   * Sets the tasks
   * 
   * @param t the tasks
   */
  public void setTask(List<Task> t) {
    this.tasks = t;
  }

  /**
   * Sets the schedule plan
   * 
   * @param se the schedule plan
   */
  public void setSchedule(List<ScheduleEntry> se) {
    this.schedulePlan = se;
  }



}
