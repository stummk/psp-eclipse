package de.uni.bremen.stummk.psp.calculation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.core.runtime.IPath;

import de.uni.bremen.stummk.psp.data.DefectRecord;
import de.uni.bremen.stummk.psp.data.PIP;
import de.uni.bremen.stummk.psp.data.PSPProject;
import de.uni.bremen.stummk.psp.data.PersistenceItem;
import de.uni.bremen.stummk.psp.data.Phase;
import de.uni.bremen.stummk.psp.data.Project;
import de.uni.bremen.stummk.psp.data.ProjectPlanSummary;
import de.uni.bremen.stummk.psp.data.ScheduleEntry;
import de.uni.bremen.stummk.psp.data.Task;
import de.uni.bremen.stummk.psp.data.TestReport;
import de.uni.bremen.stummk.psp.data.TimeRecord;
import de.uni.bremen.stummk.psp.utility.Constants;
import de.uni.bremen.stummk.psp.utility.DBConnection;
import de.uni.bremen.stummk.psp.utility.DatasetException;

/**
 * Manager, which execute operations on the database
 * 
 * @author Konstantin
 *
 */
public class Manager {

  private static Manager instance;

  private Manager() {
    // private constructor
  }

  /**
   * @return an instance of the manager, if no one exists
   */
  public static Manager getInstance() {
    if (instance == null) {
      instance = new Manager();
    }
    return instance;
  }

  /**
   * Saves an instance of {@link PersistenceItem} into the database
   * 
   * @param item the item, which should be stored into the database
   */
  public void saveToDB(PersistenceItem item) {
    try {
      DBConnection.getInstance().add(item);
    } catch (DatasetException e1) {
      e1.printStackTrace();
    }
  }

  /**
   * Updates an item of {@link PersistenceItem} in the database
   * 
   * @param item the item, which will be updated
   */
  public void update(PersistenceItem item) {
    try {
      DBConnection.getInstance().update(item);
    } catch (DatasetException e) {
      e.printStackTrace();
    }
  }

  /**
   * Deletes an instance of {@link PersistenceItem} from the database
   * 
   * @param item Instance, which will be delete from database
   */
  public void delete(PersistenceItem item) {
    try {
      DBConnection.getInstance().delete(item);
    } catch (DatasetException e) {
      e.printStackTrace();
    }
  }


  /**
   * Checks if database contains an project
   * 
   * @param projectID the name of the project
   * @return true, if database contains project
   */
  public boolean containsProject(String projectID) {
    try {
      return DBConnection.getInstance().containsProject(projectID);
    } catch (DatasetException e) {
      e.printStackTrace();
    }

    return false;
  }

  /**
   * Saves a backup from one project to the database
   * 
   * @param pspProject the backup
   */
  public void saveBackupProject(PSPProject pspProject) {
    try {
      // add project and Project plan summary
      DBConnection.getInstance().add(pspProject.getProject());
      DBConnection.getInstance().add(pspProject.getSummary());
      // saves all tasks
      pspProject.getTasks().stream().forEach(v -> {
        try {
          DBConnection.getInstance().add(v);
        } catch (DatasetException e) {
          e.printStackTrace();
        }
      });
      // saves all time records
      pspProject.getTimeRecords().stream().forEach(v -> {
        try {
          DBConnection.getInstance().add(v);
        } catch (DatasetException e) {
          e.printStackTrace();
        }
      });
      // saves the test report
      pspProject.getTestReport().stream().forEach(v -> {
        try {
          DBConnection.getInstance().add(v);
        } catch (DatasetException e) {
          e.printStackTrace();
        }
      });
      // saves the schedule plan
      pspProject.getScheduleEntries().stream().forEach(v -> {
        try {
          DBConnection.getInstance().add(v);
        } catch (DatasetException e) {
          e.printStackTrace();
        }
      });
      // saves the pip
      pspProject.getPIP().stream().forEach(v -> {
        try {
          DBConnection.getInstance().add(v);
        } catch (DatasetException e) {
          e.printStackTrace();
        }
      });
      // saves the defect records
      pspProject.getDefectRecords().stream().forEach(v -> {
        try {
          DBConnection.getInstance().add(v);
        } catch (DatasetException e) {
          e.printStackTrace();
        }
      });
    } catch (DatasetException e) {
      e.printStackTrace();
    }
  }

  /**
   * Deletes the given project
   * 
   * @param projectID the name of the project
   */
  public void deleteCompleteProject(String projectID) {
    if (containsProject(projectID)) {
      try {
        DBConnection.getInstance().deleteCompleteProject(projectID);
      } catch (DatasetException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Loads all entries of one given project from database
   * 
   * @param projectID the name of the project, which will be backuped
   * @return an instance of {@link PSPProject}
   */
  public PSPProject loadBackupProject(String projectID) {
    try {
      return DBConnection.getInstance().loadBackupProject(projectID);
    } catch (DatasetException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * @param projectID the name of the project
   * @return all pip entries of a project
   */
  public List<PIP> getPip(String projectID) {
    try {
      return DBConnection.getInstance().getPIPByProject(projectID);
    } catch (DatasetException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * @param projectID the name of the project
   * @return all time records of a project
   */
  public List<TimeRecord> getTimeRecord(String projectID) {
    try {
      return DBConnection.getInstance().getTimeRecordByProject(projectID);
    } catch (DatasetException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * @param projectID the name of the project
   * @return all defect records of a project
   */
  public List<DefectRecord> getDefectRecord(String projectName) {
    try {
      return DBConnection.getInstance().getDefectRecordByProject(projectName);
    } catch (DatasetException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * @param projectID the name of the project
   * @return all test report entries of a project
   */
  public List<TestReport> getTestReport(String projectName) {
    try {
      return DBConnection.getInstance().getTestReportByProject(projectName);
    } catch (DatasetException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * @param projectID the name of the project
   * @return all tasks of a project
   */
  public List<Task> getTaskPlanning(String projectName) {
    try {
      return DBConnection.getInstance().getTasksByProject(projectName);
    } catch (DatasetException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * @param projectID the name of the project
   * @return all schedule entries of a project
   */
  public List<ScheduleEntry> getSchedulePlanning(String projectName) {
    try {
      return DBConnection.getInstance().getScheduleByProject(projectName);
    } catch (DatasetException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * @param projectID the name of the project
   * @param weekNumber the number of week
   * @return all schedule entries of a project at one given week
   */
  public List<ScheduleEntry> getSchedulePlanByWeek(String projectName, long weekNumber) {
    try {
      return DBConnection.getInstance().getScheduleByWeek(projectName, weekNumber);
    } catch (DatasetException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * @param projectID the name of the project
   * @return the project plan summary of a project
   */
  public ProjectPlanSummary getProjectSummary(String projectName) {
    try {
      return DBConnection.getInstance().getSummaryByProject(projectName);
    } catch (DatasetException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Checks if one time record overlaps with existing time records
   * 
   * @param id the id of the time record
   * @param date the start date of a time record
   * @param starttime the start time of a time record
   * @param enddate the end date of a time record
   * @param endtime the end time of a time record
   * @param projectName the project name, the time record belongs to
   * @return true if time record of a project overlapps, else false
   */
  public boolean timeRecordOverlapps(long id, LocalDate date, LocalTime starttime, LocalDate enddate, LocalTime endtime,
      String projectName) {
    List<TimeRecord> records = getTimeRecordOverlapps(id, date, starttime, enddate, endtime, projectName);
    return records.size() > 0;
  }

  /**
   * return overlapping time records
   * 
   * @param date the start date of a time record
   * @param starttime the start time of a time record
   * @param enddate the end date of a time record
   * @param endtime the end time of a time record
   * @param projectName the project name, the time record belongs to
   * @return true if time record of a project exist, else false
   */
  public List<TimeRecord> getTimeRecordOverlapps(long id, LocalDate date, LocalTime starttime, LocalDate enddate,
      LocalTime endtime, String projectName) {
    List<TimeRecord> records = new ArrayList<TimeRecord>();
    try {
      records.addAll(DBConnection.getInstance().getTimeRecordByProject(projectName));
    } catch (DatasetException e) {
      e.printStackTrace();
    }

    if (!records.isEmpty()) {
      return records.stream().filter(v -> getInTimeRange(v, id, date, starttime, enddate, endtime))
          .collect(Collectors.toList());
    }

    return records;
  }

  private boolean getInTimeRange(TimeRecord v, long id, LocalDate date, LocalTime starttime, LocalDate enddate,
      LocalTime endtime) {

    LocalDateTime a_start = LocalDateTime.of(date, starttime);
    LocalDateTime a_end = LocalDateTime.of(enddate, endtime);

    LocalDateTime b_start = LocalDateTime.of(v.getDate(), v.getStarttime());
    LocalDateTime b_end = LocalDateTime.of(v.getEndDate(), v.getEndtime());

    if (v.getID() == id) {
      return false;
    }

    if ((a_start.isBefore(b_start) && a_end.isBefore(b_start)) || (a_start.isAfter(b_end))) {
      return false;
    }

    return true;
  }

  /**
   * Checks if one task with given name exist in a given project
   * 
   * @param txtName the task name
   * @param projectName the project name, the task belongs to
   * @return true if task of a project exist, else false
   */
  public boolean taskExist(String txtName, String projectName) {
    try {
      return DBConnection.getInstance().taskExist(txtName, projectName);
    } catch (DatasetException e) {
      e.printStackTrace();
    }
    return false;
  }

  /**
   * @param projectName the project which tasks will be searched
   * @param planCumHour the planned cumulative Hour of one week in the schedule plan
   * @return the task, with the maximal planned cumulative hours, which are <= the planned
   *         cumulative hour of one week
   */
  public Task getMinMaxCumHourTask(String projectName, long planCumHour) {
    List<Task> tasks = new ArrayList<Task>();
    long maxCumHour = Long.MIN_VALUE;
    Task actualTask = null;
    try {
      tasks = DBConnection.getInstance().getTasksByProject(projectName);
    } catch (DatasetException e) {
      e.printStackTrace();
    }

    if (tasks != null) {
      for (Task task : tasks) {
        if (task.getPlannedCumulativeHours() <= planCumHour && task.getPlannedCumulativeHours() > maxCumHour) {
          maxCumHour = task.getPlannedCumulativeHours();
          actualTask = task;
        }
      }
    }

    return actualTask;
  }

  /**
   * @param projectName the project name the tasks belongs to
   * @return all completed {@link Task} of one project
   */
  public List<Task> getCompletedTasks(String projectName) {
    try {
      return DBConnection.getInstance().getCompletedTasks(projectName);
    } catch (DatasetException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Calculates the to date values for a given {@link ProjectPLanSummary}
   * 
   * @param pps the {@link ProjectPlanSummary} which to Date values will be updated
   * @param selectedProjects the Projects, which will be used to calculate the to date values
   * @return the {@link ProjectPlanSummary}, with updated to date values
   */
  public ProjectPlanSummary getToDateValues(ProjectPlanSummary pps, String[] selectedProjects) {
    List<ProjectPlanSummary> ppsList = new ArrayList<ProjectPlanSummary>();

    for (String projectID : selectedProjects) {
      try {
        ppsList.add(DBConnection.getInstance().getSummaryByProject(projectID));
      } catch (DatasetException e) {
        e.printStackTrace();
      }
    }

    for (ProjectPlanSummary summary : ppsList) {
      // Program size
      double size = summary.get(Constants.KEY_PROGRAM_SIZE_IDX, Constants.KEY_SIZE_TOTAL_LOC, Constants.KEY_ACTUAL);
      pps.updateValue(Constants.KEY_PROGRAM_SIZE_IDX, Constants.KEY_SIZE_TOTAL_LOC, Constants.KEY_TO_DATE, size);

      // Time In Phase, Defects Injected and defects removed toDate Values
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

        double plan = summary.get(tableIDX, Constants.KEY_PHASE_PLANNING, Constants.KEY_ACTUAL);
        pps.updateValue(tableIDX, Constants.KEY_PHASE_PLANNING, Constants.KEY_TO_DATE, plan);

        double design = summary.get(tableIDX, Constants.KEY_PHASE_DESIGN, Constants.KEY_ACTUAL);
        pps.updateValue(tableIDX, Constants.KEY_PHASE_DESIGN, Constants.KEY_TO_DATE, design);

        double designReview = summary.get(tableIDX, Constants.KEY_PHASE_DESIGN_REVIEW, Constants.KEY_ACTUAL);
        pps.updateValue(tableIDX, Constants.KEY_PHASE_DESIGN_REVIEW, Constants.KEY_TO_DATE, designReview);

        double code = summary.get(tableIDX, Constants.KEY_PHASE_CODE, Constants.KEY_ACTUAL);
        pps.updateValue(tableIDX, Constants.KEY_PHASE_CODE, Constants.KEY_TO_DATE, code);

        double codeReview = summary.get(tableIDX, Constants.KEY_PHASE_CODE_REVIEW, Constants.KEY_ACTUAL);
        pps.updateValue(tableIDX, Constants.KEY_PHASE_CODE_REVIEW, Constants.KEY_TO_DATE, codeReview);

        double compile = summary.get(tableIDX, Constants.KEY_PHASE_COMPILE, Constants.KEY_ACTUAL);
        pps.updateValue(tableIDX, Constants.KEY_PHASE_COMPILE, Constants.KEY_TO_DATE, compile);

        double test = summary.get(tableIDX, Constants.KEY_PHASE_TEST, Constants.KEY_ACTUAL);
        pps.updateValue(tableIDX, Constants.KEY_PHASE_TEST, Constants.KEY_TO_DATE, test);

        double postmortem = summary.get(tableIDX, Constants.KEY_PHASE_POSTMORTEM, Constants.KEY_ACTUAL);
        pps.updateValue(tableIDX, Constants.KEY_PHASE_POSTMORTEM, Constants.KEY_TO_DATE, postmortem);

        double total = summary.get(tableIDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_ACTUAL);
        pps.updateValue(tableIDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_TO_DATE, total);
      }
    }

    // update to date per
    Calc.getInstance().updateToDatePer(pps);

    // update summary
    Calc.getInstance().updateSummary(pps, Constants.KEY_TO_DATE);

    return pps;
  }

  /**
   * @param string the project name
   * @param testname the test case name
   * @return A {@link TestReport} - Entry
   */
  public TestReport getTestReportEntry(String string, String testname) {
    try {
      return DBConnection.getInstance().getTestReportEntry(string, testname);
    } catch (DatasetException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * @param projectName the project name
   * @return An instance of {@link Project}
   */
  public Project getProjectByName(String projectName) {
    try {
      return DBConnection.getInstance().getProjectByName(projectName);
    } catch (DatasetException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * @param projectName the project name
   * @param id the number of the test
   * @return the defect record of given project name and number or null
   */
  public DefectRecord getDefectRecord(String projectName, int id) {
    try {
      return DBConnection.getInstance().getDefectRecord(projectName, id);
    } catch (DatasetException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 
   * @param projectName the project name
   * @param id the id of the linked {@link Task}
   * @return one defect record of given project and the task id
   */
  public DefectRecord getDefectRecord(String projectName, long id) {
    try {
      return DBConnection.getInstance().getDefectRecord(projectName, id);
    } catch (DatasetException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 
   * @param projectName the project name
   * @param taskID task id
   * @return a list of time records of a project and a task
   */
  public List<TimeRecord> getTimeRecord(String projectName, long taskID) {
    try {
      return DBConnection.getInstance().getTimeRecord(projectName, taskID);
    } catch (DatasetException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * @param name the project name
   * @param projectRelativePath the project relative path of the source, containing the defect
   * @param phase the phase of the defect
   * @param key {@link Constants.KEY_DEFECTS_INJECTED_IDX} or
   *        {@link Constants.KEY_DEFECTS_REMOVED_IDX}
   * @return a list of defect records of one resource in one project
   */
  public List<DefectRecord> getDefectRecord(String name, IPath projectRelativePath, Phase phase, int key) {
    try {
      List<DefectRecord> rec = null;

      if (projectRelativePath.toString().isEmpty()) {
        rec = DBConnection.getInstance().getDefectRecordByProject(name);
      } else {
        rec = DBConnection.getInstance().getDefectRecord(name, projectRelativePath.toString());
      }
      if (rec != null) {
        if (key == Constants.KEY_DEFECTS_INJECTED_IDX) {
          return rec.stream().filter(v -> v.getInjectPhase() == phase).collect(Collectors.toList());
        }

        if (key == Constants.KEY_DEFECTS_REMOVED_IDX) {
          return rec.stream().filter(v -> v.getRemovePhase() == phase).collect(Collectors.toList());
        }
      }
    } catch (DatasetException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 
   * @param name project name
   * @param projectRelativePath the project relative path of the resource
   * @return a list of defect records of one resource
   */
  public List<DefectRecord> getDefectRecord(String name, IPath projectRelativePath) {
    try {
      List<DefectRecord> rec = null;

      if (projectRelativePath.toString().isEmpty()) {
        rec = DBConnection.getInstance().getDefectRecordByProject(name);
      } else {
        rec = DBConnection.getInstance().getDefectRecord(name, projectRelativePath.toString());
      }

      return rec;
    } catch (DatasetException e) {
      e.printStackTrace();
    }
    return null;
  }
}
