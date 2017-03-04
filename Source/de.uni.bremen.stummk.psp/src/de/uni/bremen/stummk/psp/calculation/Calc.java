package de.uni.bremen.stummk.psp.calculation;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import de.uni.bremen.stummk.psp.data.DefectRecord;
import de.uni.bremen.stummk.psp.data.PersistenceItem;
import de.uni.bremen.stummk.psp.data.Phase;
import de.uni.bremen.stummk.psp.data.Project;
import de.uni.bremen.stummk.psp.data.ProjectPlanSummary;
import de.uni.bremen.stummk.psp.data.ScheduleEntry;
import de.uni.bremen.stummk.psp.data.SummaryItem;
import de.uni.bremen.stummk.psp.data.Task;
import de.uni.bremen.stummk.psp.data.TimeRecord;
import de.uni.bremen.stummk.psp.utility.Constants;

/**
 * Contains calculation functions
 * 
 * @author Konstantin
 *
 */
public class Calc {

  private static Calc instance;

  private Calc() {
    // private constructor
  }

  /**
   * @return an instance of {@link Calc}, if no one exists
   */
  public static Calc getInstance() {
    if (instance == null) {
      instance = new Calc();
    }
    return instance;
  }

  /**
   * @param project the Project name of the tasks
   * @return the total planned hours from tasks of a given project
   */
  public long getTotalPlannedHours(String project) {
    return Manager.getInstance().getTaskPlanning(project).stream().mapToLong(v -> v.getPlanTime()).sum();
  }

  /**
   * Updates the planned value for each task of a given project
   * 
   * @param projectName the {@link Project}, of which the task will be updated
   */
  public void updatePlannedValues(String projectName) {
    long plHour = getTotalPlannedHours(projectName);
    List<Task> tasks = Manager.getInstance().getTaskPlanning(projectName);
    for (Task task : tasks) {
      double newValue = MathCalc.getInstance().divide(task.getPlanTime() * 100, plHour);
      task.setPlannedValue(newValue);
      if (task.isComplete()) {
        task.setEarnedValue(newValue);
      }
    }
    tasks.forEach(v -> Manager.getInstance().update(v));
    updateCumulativeValues(projectName);
  }

  /**
   * Updates the planned cumulative hours, values and the actual cumulative values of all tasks of
   * one project
   * 
   * @param projectName the {@link Project}, of which the task will be updated
   */
  public void updateCumulativeValues(String projectName) {
    long cumPlHour = 0;
    double cumPlValue = 0;
    double cumActValue = 0;
    List<Task> tasks = Manager.getInstance().getTaskPlanning(projectName);

    for (Task t : tasks) {
      cumPlHour += t.getPlanTime();
      cumPlValue += t.getPlanValue();
      cumActValue += t.getEarnedValue();

      t.setCumulativeValues(cumPlHour, cumPlValue, cumActValue);

      Manager.getInstance().update(t);
    }
  }

  /**
   * Updates values in the {@link ProjectPlanSummary} by adding a new value to the old
   * 
   * @param task the task, which values will be added
   * @param projectName the {@link Project}, the {@link ProjectPlanSummary} will be updated
   */
  public void addSummaryValues(Task task, String projectName) {
    ProjectPlanSummary pps = Manager.getInstance().getProjectSummary(projectName);

    pps = updateSummaryValueTask(pps, task, 1);

    updateSummary(pps, Constants.KEY_PLAN);
    Manager.getInstance().update(pps);
  }

  /**
   * Updates values in the {@link ProjectPlanSummary} by adding a new value to the old
   * 
   * @param defect the defect, which values will be added
   * @param projectName the {@link Project}, the {@link ProjectPlanSummary} will be updated
   */
  public void addSummaryValue(DefectRecord defect, String projectName) {
    ProjectPlanSummary pps = Manager.getInstance().getProjectSummary(projectName);
    pps = updateDefectInjected(pps, defect, 1);
    pps = updateDefectRemove(pps, defect.getProject(), defect, 1);
    pps = updateManualDefect(pps, defect, 1);

    updateToDatePer(pps);
    updateSummary(pps, Constants.KEY_ACTUAL);
    updateSummary(pps, Constants.KEY_TO_DATE);
    Manager.getInstance().update(pps);
  }

  /**
   * Updates values in the {@link ProjectPlanSummary} by adding a new value to the old
   * 
   * @param time the time, which values will be added
   * @param projectName the {@link Project}, the {@link ProjectPlanSummary} will be updated
   */
  public void addSummaryValue(TimeRecord time, String projectName) {
    ProjectPlanSummary pps = Manager.getInstance().getProjectSummary(projectName);

    pps = updateSummaryValueTime(pps, time, time.getDeltaTime());

    updateToDatePer(pps);
    updateSummary(pps, Constants.KEY_ACTUAL);
    updateSummary(pps, Constants.KEY_TO_DATE);
    Manager.getInstance().update(pps);
  }

  /**
   * Updates values in the {@link ProjectPlanSummary} by subtract values from an existing
   * 
   * @param task the task, which values will be subtract
   * @param projectName the {@link Project}, the {@link ProjectPlanSummary} will be updated
   */
  public void subSummaryValue(Task task, String projectName) {
    ProjectPlanSummary pps = Manager.getInstance().getProjectSummary(projectName);

    pps = updateSummaryValueTask(pps, task, -1);

    updateSummary(pps, Constants.KEY_PLAN);
    Manager.getInstance().update(pps);
  }

  /**
   * Updates values in the {@link ProjectPlanSummary} by subtract values from an existing
   * 
   * @param defect the defect, which values will be subtract
   * @param projectName the {@link Project}, the {@link ProjectPlanSummary} will be updated
   */
  public void subSummaryValue(DefectRecord defect, String projectName) {
    ProjectPlanSummary pps = Manager.getInstance().getProjectSummary(projectName);

    pps = updateDefectInjected(pps, defect, -1);
    pps = updateDefectRemove(pps, defect.getProject(), defect, -1);
    pps = updateManualDefect(pps, defect, -1);

    updateToDatePer(pps);
    updateSummary(pps, Constants.KEY_ACTUAL);
    updateSummary(pps, Constants.KEY_TO_DATE);
    Manager.getInstance().update(pps);
  }

  /**
   * Updates values in the {@link ProjectPlanSummary} by subtract values from an existing
   * 
   * @param time the time, which values will be subtract
   * @param projectName the {@link Project}, the {@link ProjectPlanSummary} will be updated
   */
  public void subSummaryValue(TimeRecord time, String projectName) {
    ProjectPlanSummary pps = Manager.getInstance().getProjectSummary(projectName);

    pps = updateSummaryValueTime(pps, time, -time.getDeltaTime());

    updateToDatePer(pps);
    updateSummary(pps, Constants.KEY_ACTUAL);
    updateSummary(pps, Constants.KEY_TO_DATE);
    Manager.getInstance().update(pps);
  }

  /**
   * Updates the Value in the summary section of the {@link ProjectPlanSummary}
   * 
   * @param pps the {@link ProjectPlanSummary}, which will be updated
   * @param key the key of the column in the table
   */
  public void updateSummary(ProjectPlanSummary pps, String key) {
    // loc/hour
    double loc = pps.get(Constants.KEY_PROGRAM_SIZE_IDX, Constants.KEY_SIZE_TOTAL_LOC, key);
    double hour = pps.get(Constants.KEY_TIME_IN_PHASE_IDX, Constants.KEY_PHASE_TOTAL, key);
    if (!key.equals(Constants.KEY_PLAN)) {
      hour = MathCalc.getInstance().fromSecondToMinute((long) hour);
    }
    double locPerHour = MathCalc.getInstance().divide(60 * loc, hour);
    pps.put(Constants.KEY_SUMMARY_IDX, Constants.KEY_SUM_LOC_PER_HOUR, key, locPerHour);

    // time
    double time = pps.get(Constants.KEY_TIME_IN_PHASE_IDX, Constants.KEY_PHASE_TOTAL, key);
    if (!key.equals(Constants.KEY_PLAN)) {
      time = MathCalc.getInstance().fromSecondToMinute((long) time);
    }
    pps.put(Constants.KEY_SUMMARY_IDX, Constants.KEY_SUM_TIME, key, time);

    // Def/Kloc
    double def = pps.get(Constants.KEY_DEFECTS_REMOVED_IDX, Constants.KEY_PHASE_TOTAL, key);
    double loc1 = pps.get(Constants.KEY_PROGRAM_SIZE_IDX, Constants.KEY_SIZE_TOTAL_LOC, key);
    double defPerKloc = MathCalc.getInstance().divide(1000 * def, loc1);
    pps.put(Constants.KEY_SUMMARY_IDX, Constants.KEY_SUM_DEFECTS_PER_LOC, key, defPerKloc);

    // Yield
    double defRem = 0;
    double defInj = 0;
    List<? extends SummaryItem> list = pps.getSectionContent(Constants.KEY_DEFECTS_REMOVED_IDX);
    List<? extends SummaryItem> list2 = pps.getSectionContent(Constants.KEY_DEFECTS_INJECTED_IDX);

    for (SummaryItem item : list) {
      if (item.getTitle().equals(Constants.KEY_PHASE_COMPILE)) {
        break;
      }
      defRem += item.get(key);
    }

    for (SummaryItem item : list2) {
      if (item.getTitle().equals(Constants.KEY_PHASE_COMPILE)) {
        break;
      }
      defInj += item.get(key);
    }
    double yield = MathCalc.getInstance().divide(100 * defRem, defInj);
    pps.put(Constants.KEY_SUMMARY_IDX, Constants.KEY_SUM_YIELD, key, yield);

    // A/F Ratio
    double timeInDesignReview = pps.get(Constants.KEY_TIME_IN_PHASE_IDX, Constants.KEY_PHASE_DESIGN_REVIEW, key);
    double timeInCodeReview = pps.get(Constants.KEY_TIME_IN_PHASE_IDX, Constants.KEY_PHASE_CODE_REVIEW, key);
    double timeInCompile = pps.get(Constants.KEY_TIME_IN_PHASE_IDX, Constants.KEY_PHASE_COMPILE, key);
    double timeInTest = pps.get(Constants.KEY_TIME_IN_PHASE_IDX, Constants.KEY_PHASE_TEST, key);

    if (!key.equals(Constants.KEY_PLAN)) {
      timeInCompile = MathCalc.getInstance().fromSecondToMinute((long) timeInCompile);
      timeInCodeReview = MathCalc.getInstance().fromSecondToMinute((long) timeInCodeReview);
      timeInDesignReview = MathCalc.getInstance().fromSecondToMinute((long) timeInDesignReview);
      timeInTest = MathCalc.getInstance().fromSecondToMinute((long) timeInTest);
    }

    double value = MathCalc.getInstance().divide((timeInDesignReview + timeInCodeReview), (timeInCompile + timeInTest));
    pps.put(Constants.KEY_SUMMARY_IDX, Constants.KEY_SUM_AF_RATIO, key, value);
  }


  /**
   * Updates the def/hour values in the {@link ProjectPlanSummary}
   * 
   * @param pps the project plan summary
   * @param projectName the project name
   * @param tableKey the section, which will be updated
   * @param rowKey the row, which will be updated
   */
  public void updateDefPerHour(ProjectPlanSummary pps, String projectName, int tableKey, String rowKey) {
    double def = pps.get(tableKey, rowKey, Constants.KEY_ACTUAL);
    double min = pps.get(Constants.KEY_TIME_IN_PHASE_IDX, rowKey, Constants.KEY_ACTUAL);
    min = MathCalc.getInstance().fromSecondToMinute((long) min);
    double value = MathCalc.getInstance().divide(60 * def, min);
    pps.put(tableKey, rowKey, Constants.KEY_DEFECT_PER_HOUR, value);

    double def_1 = pps.get(tableKey, Constants.KEY_PHASE_TOTAL, Constants.KEY_ACTUAL);
    double min_1 = pps.get(Constants.KEY_TIME_IN_PHASE_IDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_ACTUAL);
    min_1 = MathCalc.getInstance().fromSecondToMinute((long) min_1);
    double value_1 = MathCalc.getInstance().divide(60 * def_1, min_1);
    pps.put(tableKey, Constants.KEY_PHASE_TOTAL, Constants.KEY_DEFECT_PER_HOUR, value_1);

  }

  /**
   * Creates or updates a schedule plan entry
   * 
   * @param project the project of the schedule plan
   * @param task task, which data will be used to create the schedule plan entry
   */
  public void createOrUpdateSchedule(Project project, Task task) {
    createScheduleEntry(project, task, task.getPlannedMonday(), task.getPlanTime());
    updatePlanCumValue(project);
    updateActualCumValue(project);
  }

  /**
   * Creates or updates a schedule plan entry with actual data
   * 
   * @param project the project of the schedule plan
   * @param timerecord time record, which data will be used to create the schedule plan entry
   */
  public void updateScheduleActualHour(Project project, TimeRecord timerecord) {
    createScheduleEntry(project, timerecord, timerecord.getEndDate(), timerecord.getDeltaTime());
  }

  /**
   * Deletes plan hours from schedule entry
   * 
   * @param project the project to which the schedule entry belongs to
   * @param task the task, which plan hours will be deleted from schedule entry
   */
  public void deletePlanHoursFromSchedule(Project project, Task task) {
    deleteScheduleEntry(project, task.getPlannedMonday(), task, task.getPlanTime());
    updatePlanCumValue(project);
    updateActualCumValue(project);
  }

  /**
   * Deletes actual hours from schedule entry
   * 
   * @param project the project to which the schedule entry belongs to
   * @param record the time record, which hours will be deleted from schedule entry
   */
  public void deleteActualHoursFromSchedule(Project project, TimeRecord record) {
    deleteScheduleEntry(project, record.getDate(), record, record.getDeltaTime());
  }

  private void deleteHoursFromSchedule(Project project, DefectRecord defect) {
    deleteScheduleEntry(project, defect.getDate(), defect,
        MathCalc.getInstance().fromMinuteToSecond(defect.getFixTime()));
  }

  /**
   * Updates the cumulative hours in the schedule entry
   * 
   * @param project the project of the schedule entry
   */
  public void updateCumHours(Project project) {
    long cumPlHour = 0;
    long cumActualHour = 0;
    List<ScheduleEntry> entries = Manager.getInstance().getSchedulePlanning(project.getProjectName());

    if (entries != null) {
      for (ScheduleEntry e : entries) {
        cumPlHour += e.getPlannedHours();
        cumActualHour += e.getActualDirectHours();
        e.setCumulativePlannedHours(cumPlHour);
        e.setCumulativeActualHours(cumActualHour);

        Manager.getInstance().update(e);
      }
    }
  }

  /**
   * Updates the cumulative plan value in the schedule entry
   * 
   * @param project the project of the schedule entry
   */
  public void updatePlanCumValue(Project project) {
    List<ScheduleEntry> entries = Manager.getInstance().getSchedulePlanning(project.getProjectName());

    if (entries != null) {
      for (ScheduleEntry e : entries) {
        long planCumHour = e.getCumulativePlannedHours();
        Task task = Manager.getInstance().getMinMaxCumHourTask(project.getProjectName(), planCumHour);

        if (task != null) {
          e.setCumulativePlannedValue(task.getPlannedCumulativeValues());
          Manager.getInstance().update(e);
        }
      }
    }
  }

  /**
   * Updates the cumulative actual value in the schedule entry
   * 
   * @param project the project of the schedule entry
   */
  public void updateActualCumValue(Project project) {
    List<ScheduleEntry> entries = Manager.getInstance().getSchedulePlanning(project.getProjectName());
    LocalDate projectStartDate = project.getTimestamp();
    List<Task> completedTasks = Manager.getInstance().getCompletedTasks(project.getProjectName());

    entries.forEach(v -> v.setCumulativeValue(0));

    for (Task task : completedTasks) {
      long weeks = ChronoUnit.WEEKS.between(projectStartDate, task.getCompleteDate());
      ScheduleEntry entry = getScheduleOnWeek(weeks, entries);
      if (entry != null) {
        entry.setCumulativeValue(entry.getActualEarnedValue() + task.getEarnedValue());
      }
    }

    double cumValue = 0;
    if (entries != null) {
      for (ScheduleEntry e : entries) {
        cumValue += e.getActualEarnedValue();
        e.setCumulativeValue(cumValue);
        Manager.getInstance().update(e);
      }
    }
  }

  /**
   * Updates the to date percentage values in a given {@link ProjectPlanSummary}
   * 
   * @param pps the {@link ProjectPlanSummary}
   */
  public void updateToDatePer(ProjectPlanSummary pps) {
    for (int i = 0; i < 3; ++i) {
      int tableIDX = 0;
      switch (i) {
        case 0:
          tableIDX = Constants.KEY_TIME_IN_PHASE_IDX;
          break;
        case 1:
          tableIDX = Constants.KEY_DEFECTS_INJECTED_IDX;
          break;
        case 2:
          tableIDX = Constants.KEY_DEFECTS_REMOVED_IDX;
      }


      double total = pps.get(tableIDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_TO_DATE);

      if (tableIDX == Constants.KEY_TIME_IN_PHASE_IDX) {
        total = MathCalc.getInstance().fromSecondToMinute((long) total);
      }

      double plan = pps.get(tableIDX, Constants.KEY_PHASE_PLANNING, Constants.KEY_TO_DATE);
      if (tableIDX == Constants.KEY_TIME_IN_PHASE_IDX) {
        plan = MathCalc.getInstance().fromSecondToMinute((long) plan);
      }
      pps.put(tableIDX, Constants.KEY_PHASE_PLANNING, Constants.KEY_TO_DATE_PER,
          MathCalc.getInstance().divide(100 * plan, total));

      double design = pps.get(tableIDX, Constants.KEY_PHASE_DESIGN, Constants.KEY_TO_DATE);
      if (tableIDX == Constants.KEY_TIME_IN_PHASE_IDX) {
        design = MathCalc.getInstance().fromSecondToMinute((long) design);
      }
      pps.put(tableIDX, Constants.KEY_PHASE_DESIGN, Constants.KEY_TO_DATE_PER,
          MathCalc.getInstance().divide(100 * design, total));

      double designReview = pps.get(tableIDX, Constants.KEY_PHASE_DESIGN_REVIEW, Constants.KEY_TO_DATE);
      if (tableIDX == Constants.KEY_TIME_IN_PHASE_IDX) {
        designReview = MathCalc.getInstance().fromSecondToMinute((long) designReview);
      }
      pps.put(tableIDX, Constants.KEY_PHASE_DESIGN_REVIEW, Constants.KEY_TO_DATE_PER,
          MathCalc.getInstance().divide(100 * designReview, total));

      double code = pps.get(tableIDX, Constants.KEY_PHASE_CODE, Constants.KEY_TO_DATE);
      if (tableIDX == Constants.KEY_TIME_IN_PHASE_IDX) {
        code = MathCalc.getInstance().fromSecondToMinute((long) code);
      }
      pps.put(tableIDX, Constants.KEY_PHASE_CODE, Constants.KEY_TO_DATE_PER,
          MathCalc.getInstance().divide(100 * code, total));

      double codeReview = pps.get(tableIDX, Constants.KEY_PHASE_CODE_REVIEW, Constants.KEY_TO_DATE);
      if (tableIDX == Constants.KEY_TIME_IN_PHASE_IDX) {
        codeReview = MathCalc.getInstance().fromSecondToMinute((long) codeReview);
      }
      pps.put(tableIDX, Constants.KEY_PHASE_CODE_REVIEW, Constants.KEY_TO_DATE_PER,
          MathCalc.getInstance().divide(100 * codeReview, total));

      double compile = pps.get(tableIDX, Constants.KEY_PHASE_COMPILE, Constants.KEY_TO_DATE);
      if (tableIDX == Constants.KEY_TIME_IN_PHASE_IDX) {
        compile = MathCalc.getInstance().fromSecondToMinute((long) compile);
      }
      pps.put(tableIDX, Constants.KEY_PHASE_COMPILE, Constants.KEY_TO_DATE_PER,
          MathCalc.getInstance().divide(100 * compile, total));

      double test = pps.get(tableIDX, Constants.KEY_PHASE_TEST, Constants.KEY_TO_DATE);
      if (tableIDX == Constants.KEY_TIME_IN_PHASE_IDX) {
        test = MathCalc.getInstance().fromSecondToMinute((long) test);
      }
      pps.put(tableIDX, Constants.KEY_PHASE_TEST, Constants.KEY_TO_DATE_PER,
          MathCalc.getInstance().divide(100 * test, total));

      double postmortem = pps.get(tableIDX, Constants.KEY_PHASE_POSTMORTEM, Constants.KEY_TO_DATE);
      if (tableIDX == Constants.KEY_TIME_IN_PHASE_IDX) {
        postmortem = MathCalc.getInstance().fromSecondToMinute((long) postmortem);
      }
      pps.put(tableIDX, Constants.KEY_PHASE_POSTMORTEM, Constants.KEY_TO_DATE_PER,
          MathCalc.getInstance().divide(100 * postmortem, total));

      double total1 = pps.get(tableIDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_TO_DATE);
      if (tableIDX == Constants.KEY_TIME_IN_PHASE_IDX) {
        total1 = MathCalc.getInstance().fromSecondToMinute((long) total1);
      }
      pps.put(tableIDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_TO_DATE_PER,
          MathCalc.getInstance().divide(100 * total1, total));
    }
  }

  /**
   * Add one defect to the removed phase in the project plan summary
   * 
   * @param project the project, the defect is added
   * @param defect the defect, which is added
   */
  public void updateRemoveValues(Project project, DefectRecord defect) {
    ProjectPlanSummary pps = Manager.getInstance().getProjectSummary(project.getProjectName());

    pps = updateDefectRemove(pps, project, defect, 1);

    updateToDatePer(pps);
    updateSummary(pps, Constants.KEY_ACTUAL);
    updateSummary(pps, Constants.KEY_TO_DATE);
    Manager.getInstance().update(pps);
  }

  private ProjectPlanSummary updateSummaryValueTime(ProjectPlanSummary pps, TimeRecord time, long value) {
    // updates the time values in the project plan summary
    pps.updateValue(Constants.KEY_TIME_IN_PHASE_IDX, Phase.getValue(time.getPhase()), Constants.KEY_ACTUAL, value);
    pps.updateValue(Constants.KEY_TIME_IN_PHASE_IDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_ACTUAL, value);
    updateDefPerHour(pps, time.getProject().getProjectName(), Constants.KEY_DEFECTS_INJECTED_IDX,
        Phase.getValue(time.getPhase()));
    updateDefPerHour(pps, time.getProject().getProjectName(), Constants.KEY_DEFECTS_REMOVED_IDX,
        Phase.getValue(time.getPhase()));
    pps.updateValue(Constants.KEY_TIME_IN_PHASE_IDX, Phase.getValue(time.getPhase()), Constants.KEY_TO_DATE, value);
    pps.updateValue(Constants.KEY_TIME_IN_PHASE_IDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_TO_DATE, value);
    return pps;
  }

  private ProjectPlanSummary updateSummaryValueTask(ProjectPlanSummary pps, Task task, long value) {
    // updates the plan values in the project plan summary
    pps.updateValue(Constants.KEY_TIME_IN_PHASE_IDX, Phase.getValue(task.getPhase()), Constants.KEY_PLAN,
        (value * task.getPlanTime()));
    pps.updateValue(Constants.KEY_TIME_IN_PHASE_IDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_PLAN,
        (value * task.getPlanTime()));
    pps.updateValue(Constants.KEY_PROGRAM_SIZE_IDX, Constants.KEY_SIZE_TOTAL_LOC, Constants.KEY_PLAN,
        (value * task.getPlanLoc()));
    pps.updateValue(Constants.KEY_DEFECTS_INJECTED_IDX, Phase.getValue(task.getPhase()), Constants.KEY_PLAN,
        (value * task.getPlanDefInjected()));
    pps.updateValue(Constants.KEY_DEFECTS_INJECTED_IDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_PLAN,
        (value * task.getPlanDefInjected()));
    pps.updateValue(Constants.KEY_DEFECTS_REMOVED_IDX, Phase.getValue(task.getPhase()), Constants.KEY_PLAN,
        (value * task.getPlanDefRemoved()));
    pps.updateValue(Constants.KEY_DEFECTS_REMOVED_IDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_PLAN,
        (value * task.getPlanDefRemoved()));
    return pps;
  }

  private ProjectPlanSummary updateDefectRemove(ProjectPlanSummary pps, Project project, DefectRecord defect,
      long value) {
    // updates the defect removed values in the project plan summary
    if (defect.getRemovePhase() != null) {
      pps.updateValue(Constants.KEY_DEFECTS_REMOVED_IDX, Phase.getValue(defect.getRemovePhase()), Constants.KEY_ACTUAL,
          value);
      pps.updateValue(Constants.KEY_DEFECTS_REMOVED_IDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_ACTUAL, value);
      updateDefPerHour(pps, project.getProjectName(), Constants.KEY_DEFECTS_REMOVED_IDX,
          Phase.getValue(defect.getRemovePhase()));
      pps.updateValue(Constants.KEY_DEFECTS_REMOVED_IDX, Phase.getValue(defect.getRemovePhase()), Constants.KEY_TO_DATE,
          value);
      pps.updateValue(Constants.KEY_DEFECTS_REMOVED_IDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_TO_DATE, value);
    }

    return pps;
  }

  private ProjectPlanSummary updateDefectInjected(ProjectPlanSummary pps, DefectRecord defect, long value) {
    // updates the defect injected values in the project plan summary
    pps.updateValue(Constants.KEY_DEFECTS_INJECTED_IDX, Phase.getValue(defect.getInjectPhase()), Constants.KEY_ACTUAL,
        value);
    pps.updateValue(Constants.KEY_DEFECTS_INJECTED_IDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_ACTUAL, value);
    updateDefPerHour(pps, defect.getProject().getProjectName(), Constants.KEY_DEFECTS_INJECTED_IDX,
        Phase.getValue(defect.getInjectPhase()));
    pps.updateValue(Constants.KEY_DEFECTS_INJECTED_IDX, Phase.getValue(defect.getInjectPhase()), Constants.KEY_TO_DATE,
        value);
    pps.updateValue(Constants.KEY_DEFECTS_INJECTED_IDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_TO_DATE, value);

    return pps;
  }

  private ProjectPlanSummary updateManualDefect(ProjectPlanSummary pps, DefectRecord defect, long value) {
    // updates the actual time values in the project plan summary, if defect is manual fixed
    if (defect.isFixManual()) {
      pps.updateValue(Constants.KEY_TIME_IN_PHASE_IDX, Phase.getValue(defect.getRemovePhase()), Constants.KEY_ACTUAL,
          (value * MathCalc.getInstance().fromMinuteToSecond(defect.getFixTime())));
      pps.updateValue(Constants.KEY_TIME_IN_PHASE_IDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_ACTUAL,
          (value * MathCalc.getInstance().fromMinuteToSecond(defect.getFixTime())));
      updateDefPerHour(pps, defect.getProject().getProjectName(), Constants.KEY_DEFECTS_INJECTED_IDX,
          Phase.getValue(defect.getRemovePhase()));
      updateDefPerHour(pps, defect.getProject().getProjectName(), Constants.KEY_DEFECTS_REMOVED_IDX,
          Phase.getValue(defect.getRemovePhase()));
      pps.updateValue(Constants.KEY_TIME_IN_PHASE_IDX, Phase.getValue(defect.getRemovePhase()), Constants.KEY_TO_DATE,
          (value * MathCalc.getInstance().fromMinuteToSecond(defect.getFixTime())));
      pps.updateValue(Constants.KEY_TIME_IN_PHASE_IDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_TO_DATE,
          (value * MathCalc.getInstance().fromMinuteToSecond(defect.getFixTime())));

      if (value == -1) {
        deleteHoursFromSchedule(defect.getProject(), defect);
      } else if (value == 1) {
        updateScheduleBYDefect(defect.getProject(), defect);
      }
    }

    return pps;
  }

  private void updateScheduleBYDefect(Project project, DefectRecord defect) {
    createScheduleEntry(project, defect, defect.getDate(),
        MathCalc.getInstance().fromMinuteToSecond(defect.getFixTime()));
  }

  private void createScheduleEntry(Project project, PersistenceItem item, LocalDate date, long time) {
    // create or update entry in schedule entry
    LocalDate projectStartDate = project.getTimestamp();
    long weeks = ChronoUnit.WEEKS.between(projectStartDate, date);

    List<ScheduleEntry> entries = Manager.getInstance().getSchedulePlanByWeek(project.getProjectName(), weeks);
    if (entries.size() == 0) {
      ScheduleEntry entry = new ScheduleEntry(project, weeks, item);
      Manager.getInstance().saveToDB(entry);
    } else if (entries.size() == 1) {
      ScheduleEntry entry = entries.get(0);
      if (item instanceof TimeRecord || item instanceof DefectRecord) {
        entry.updateActualHour(time);
      }

      if (item instanceof Task) {
        entry.updatePlannedValues(time);
      }

      Manager.getInstance().update(entry);
    }

    updateCumHours(project);
  }

  private void deleteScheduleEntry(Project project, LocalDate date, PersistenceItem item, long time) {
    // substract a value from existing schedule plan or delete the entry of the schedule plan
    LocalDate projectStartDate = project.getTimestamp();
    long weeks = ChronoUnit.WEEKS.between(projectStartDate, date);

    List<ScheduleEntry> entries = Manager.getInstance().getSchedulePlanByWeek(project.getProjectName(), weeks);
    if (entries.size() == 1) {
      ScheduleEntry entry = entries.get(0);

      if (item instanceof TimeRecord || item instanceof DefectRecord) {
        entry.updateActualHour(-time);
      }

      if (item instanceof Task) {
        entry.updatePlannedValues(-time);
      }

      if (entry.getPlannedHours() == 0 && entry.getActualDirectHours() == 0) {
        Manager.getInstance().delete(entry);
      } else {
        Manager.getInstance().update(entry);
      }
    }

    updateCumHours(project);
  }

  private ScheduleEntry getScheduleOnWeek(long week, List<ScheduleEntry> entries) {
    // returns the schedule entry of one week
    for (ScheduleEntry entry : entries) {
      if (entry.getWeekNumber() == week) {
        return entry;
      }
    }
    return null;
  }

}
