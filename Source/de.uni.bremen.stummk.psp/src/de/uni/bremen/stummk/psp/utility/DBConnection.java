package de.uni.bremen.stummk.psp.utility;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import de.uni.bremen.stummk.psp.data.DefectRecord;
import de.uni.bremen.stummk.psp.data.PIP;
import de.uni.bremen.stummk.psp.data.PSPProject;
import de.uni.bremen.stummk.psp.data.PersistenceItem;
import de.uni.bremen.stummk.psp.data.Project;
import de.uni.bremen.stummk.psp.data.ProjectPlanSummary;
import de.uni.bremen.stummk.psp.data.ScheduleEntry;
import de.uni.bremen.stummk.psp.data.Task;
import de.uni.bremen.stummk.psp.data.TestReport;
import de.uni.bremen.stummk.psp.data.TimeRecord;

/**
 * Represents access operations to the database
 * 
 * @author Konstantin
 *
 */
public class DBConnection {

  private static DBConnection instance;

  /**
   * Entity-Manager
   */
  private static EntityManager entityManager;

  private DBConnection() {
    // empty for purpose
  }

  /**
   * @return the instance of {@link DBConnection}
   */
  public static DBConnection getInstance() {
    if (instance == null) {
      instance = new DBConnection();
    }
    return instance;
  }

  /**
   * Closes the database connection
   */
  public static void close() {
    if (entityManager != null) {
      entityManager.getEntityManagerFactory().close();
    }
  }

  /**
   * Initializes the database
   * 
   * @throws PersistenceException If initializes failed
   */
  public static void init() throws PersistenceException {
    try {
      EntityManagerFactory factory = Persistence.createEntityManagerFactory("pspdb");
      entityManager = factory.createEntityManager();
    } catch (Exception e) {
      throw new PersistenceException("Could not initialize persistence component: " + e.getMessage());
    }
  }

  /**
   * @return the EntityManager
   */
  public EntityManager getEntityManager() {
    return entityManager;
  }

  /**
   * Persists {@link PersistenceItem} into the database
   * 
   * @param object item, which will be persist
   * @throws DatasetException
   */
  public void add(PersistenceItem object) throws DatasetException {
    if (object == null) {
      throw new IllegalArgumentException("Parameter is null");
    }
    try {
      EntityTransaction transaction = getEntityManager().getTransaction();
      transaction.begin();
      entityManager.persist(object);
      transaction.commit();
    } catch (Exception e) {
      throw new DatasetException("Error while adding: " + e.getMessage());
    }
  }

  /**
   * Select an Project item from database by project name
   * 
   * @param projectName the project name
   * @return instance of {@link Project}
   * @throws DatasetException when something went wrong
   */
  @SuppressWarnings("unchecked")
  public Project getProjectByName(String projectName) throws DatasetException {
    if (projectName == null) {
      return null;
    }
    try {
      final Query query = getEntityManager().createQuery("Select e FROM Project e WHERE e.projectName =?1");
      query.setParameter(1, projectName);
      List<Project> rs = query.getResultList();
      if (rs.size() == 1) {
        return rs.get(0);
      }
      return null;
    } catch (Exception e) {
      throw new DatasetException("Error while checking if Project is in Database: " + e.getMessage());
    }
  }

  /**
   * Checks if database contains a project
   * 
   * @param projectName the name of the project
   * @return true if project exist in db, else false
   * @throws DatasetException if something went wrong
   */
  public boolean containsProject(String projectName) throws DatasetException {
    if (projectName == null) {
      return false;
    }

    Project project = getProjectByName(projectName);

    if (project != null) {
      return true;
    }

    return false;
  }

  /**
   * Updates an element in the database
   * 
   * @param item Element, which will be updated
   * @throws DatasetException if something went wrong
   */
  public void update(PersistenceItem item) throws DatasetException {
    try {
      EntityTransaction transaction = getEntityManager().getTransaction();
      transaction.begin();
      entityManager.merge(item);
      transaction.commit();
    } catch (Exception e) {
      throw new DatasetException("Error while updating item: " + e.getMessage());
    }
  }

  /**
   * Deletes an element in the database
   * 
   * @param item Element which will be deleted
   * @throws DatasetException if something went wrong
   */
  public void delete(PersistenceItem item) throws DatasetException {
    try {
      EntityTransaction transaction = getEntityManager().getTransaction();
      transaction.begin();
      entityManager.remove(item);
      transaction.commit();
    } catch (Exception e) {
      throw new DatasetException("Error while deleting item: " + e.getMessage());
    }
  }

  /**
   * Deletes a complete project from database and all elements that belongs to the project
   * 
   * @param projectID the project name to be deleted
   * @throws DatasetException
   */
  public void deleteCompleteProject(String projectID) throws DatasetException {
    getDefectRecordByProject(projectID).stream().forEach(v -> {
      try {
        delete(v);
      } catch (DatasetException e) {
        e.printStackTrace();
      }
    });
    getPIPByProject(projectID).stream().forEach(v -> {
      try {
        delete(v);
      } catch (DatasetException e) {
        e.printStackTrace();
      }
    });
    getScheduleByProject(projectID).stream().forEach(v -> {
      try {
        delete(v);
      } catch (DatasetException e) {
        e.printStackTrace();
      }
    });
    getTasksByProject(projectID).stream().forEach(v -> {
      try {
        delete(v);
      } catch (DatasetException e) {
        e.printStackTrace();
      }
    });
    getTestReportByProject(projectID).stream().forEach(v -> {
      try {
        delete(v);
      } catch (DatasetException e) {
        e.printStackTrace();
      }
    });
    getTimeRecordByProject(projectID).stream().forEach(v -> {
      try {
        delete(v);
      } catch (DatasetException e) {
        e.printStackTrace();
      }
    });

    ProjectPlanSummary pps = getSummaryByProject(projectID);
    if (pps != null) {
      delete(pps);
    }

    Project p = getProjectByName(projectID);
    if (p != null) {
      delete(p);
    }
  }

  /**
   * Loads all data from a project to be backuped in a file
   * 
   * @param projectID the project to be backuped
   * @return an instance of {@link PSPProject}
   * @throws DatasetException
   */
  public PSPProject loadBackupProject(String projectID) throws DatasetException {
    PSPProject psp = new PSPProject(getProjectByName(projectID), getSummaryByProject(projectID),
        getPIPByProject(projectID), getTestReportByProject(projectID), getDefectRecordByProject(projectID),
        getScheduleByProject(projectID), getTimeRecordByProject(projectID), getTasksByProject(projectID));
    return psp;
  }

  /**
   * Loads all tasks of a project from db
   * 
   * @param projectID the project name
   * @return all tasks of the project
   * @throws DatasetException
   */
  @SuppressWarnings("unchecked")
  public List<Task> getTasksByProject(String projectID) throws DatasetException {
    if (projectID == null) {
      return null;
    }
    try {
      final Query query = getEntityManager().createQuery("Select e FROM Task e WHERE e.project.projectName =?1");
      query.setParameter(1, projectID);
      List<Task> rs = query.getResultList();
      return rs;
    } catch (Exception e) {
      throw new DatasetException("Error while getting item from db: " + e.getMessage());
    }
  }

  /**
   * Loads all defect records of a project from db
   * 
   * @param projectID the project name
   * @return all defect records of the project
   * @throws DatasetException
   */
  @SuppressWarnings("unchecked")
  public List<DefectRecord> getDefectRecordByProject(String projectID) throws DatasetException {
    if (projectID == null) {
      return null;
    }
    try {
      final Query query = getEntityManager()
          .createQuery("Select e FROM DefectRecord e WHERE e.project.projectName =?1 ORDER BY e.date, e.ID");
      query.setParameter(1, projectID);
      List<DefectRecord> rs = query.getResultList();
      return rs;
    } catch (Exception e) {
      throw new DatasetException("Error while getting item from db: " + e.getMessage());
    }
  }

  /**
   * Loads all pip entries of a project from db
   * 
   * @param projectID the project name
   * @return all pip entries of the project
   * @throws DatasetException
   */
  @SuppressWarnings("unchecked")
  public List<PIP> getPIPByProject(String projectID) throws DatasetException {
    if (projectID == null) {
      return null;
    }
    try {
      final Query query = getEntityManager().createQuery("Select e FROM PIP e WHERE e.project.projectName =?1");
      query.setParameter(1, projectID);
      List<PIP> rs = query.getResultList();
      return rs;
    } catch (Exception e) {
      throw new DatasetException("Error while getting item from db: " + e.getMessage());
    }
  }

  /**
   * Loads the project plan summary of a project from db
   * 
   * @param projectID the project name
   * @return the project plan summary of the project
   * @throws DatasetException
   */
  @SuppressWarnings("unchecked")
  public ProjectPlanSummary getSummaryByProject(String projectID) throws DatasetException {
    if (projectID == null) {
      return null;
    }
    try {
      final Query query =
          getEntityManager().createQuery("Select e FROM ProjectPlanSummary e WHERE e.project.projectName =?1");
      query.setParameter(1, projectID);
      List<ProjectPlanSummary> rs = query.getResultList();
      if (rs.size() == 1) {
        return rs.get(0);
      }
      return null;
    } catch (Exception e) {
      throw new DatasetException("Error while getting item from db: " + e.getMessage());
    }
  }

  /**
   * Loads all schedule entries of a project from db
   * 
   * @param projectID the project name
   * @return all schedule entiries of the project
   * @throws DatasetException
   */
  @SuppressWarnings("unchecked")
  public List<ScheduleEntry> getScheduleByProject(String projectID) throws DatasetException {
    if (projectID == null) {
      return null;
    }
    try {
      final Query query = getEntityManager().createQuery(
          "Select e FROM ScheduleEntry e WHERE e.project.projectName =?1 ORDER BY e.weekNumber, e.dateMonday");
      query.setParameter(1, projectID);
      List<ScheduleEntry> rs = query.getResultList();
      return rs;
    } catch (Exception e) {
      throw new DatasetException("Error while getting item from db: " + e.getMessage());
    }
  }

  /**
   * Loads all test report entries from db of a project
   * 
   * @param projectID the project name
   * @return all test report entries of the project
   * @throws DatasetException
   */
  @SuppressWarnings("unchecked")
  public List<TestReport> getTestReportByProject(String projectID) throws DatasetException {
    if (projectID == null) {
      return null;
    }
    try {
      final Query query = getEntityManager().createQuery("Select e FROM TestReport e WHERE e.project.projectName =?1");
      query.setParameter(1, projectID);
      List<TestReport> rs = query.getResultList();
      return rs;
    } catch (Exception e) {
      throw new DatasetException("Error while getting item from db: " + e.getMessage());
    }
  }

  /**
   * Loads all time records from db of a project
   * 
   * @param projectID the project name
   * @return all time records of the project
   * @throws DatasetException
   */
  @SuppressWarnings("unchecked")
  public List<TimeRecord> getTimeRecordByProject(String projectID) throws DatasetException {
    if (projectID == null) {
      return null;
    }
    try {
      final Query query = getEntityManager().createQuery("Select e FROM TimeRecord e WHERE e.project.projectName =?1");
      query.setParameter(1, projectID);
      List<TimeRecord> rs = query.getResultList();
      rs.sort(new Comparator<TimeRecord>() {

        @Override
        public int compare(TimeRecord o1, TimeRecord o2) {
          LocalDateTime start1 = LocalDateTime.of(o1.getDate(), o1.getStarttime());
          LocalDateTime start2 = LocalDateTime.of(o2.getDate(), o2.getStarttime());
          return start1.compareTo(start2);
        }
      });

      return rs;
    } catch (Exception e) {
      throw new DatasetException("Error while getting item from db: " + e.getMessage());
    }
  }

  /**
   * Checks if a task exist for a given project
   * 
   * @param txtName the name of the task
   * @param projectName the project name
   * @return true if exist
   * @throws DatasetException
   */
  @SuppressWarnings("unchecked")
  public boolean taskExist(String txtName, String projectName) throws DatasetException {
    if (projectName != null && txtName != null) {
      List<Task> tasks = null;

      try {
        final Query query =
            entityManager.createQuery("Select e FROM Task e WHERE e.project.projectName =?1 AND e.name =?2 ");
        query.setParameter(1, projectName);
        query.setParameter(2, txtName);
        tasks = query.getResultList();
        return (tasks != null && tasks.size() > 0);
      } catch (Exception e) {
        throw new DatasetException("Error while checking if containing task: " + e.getMessage());
      }
    }
    return false;
  }

  /**
   * Loads all schedule entries of a project from db
   * 
   * @param projectID the project name
   * @param weekNumber the number of week
   * @return all schedule entries of the project
   * @throws DatasetException
   */
  @SuppressWarnings("unchecked")
  public List<ScheduleEntry> getScheduleByWeek(String projectName, long weekNumber) throws DatasetException {
    if (projectName == null) {
      return null;
    }
    try {
      final Query query = getEntityManager().createQuery(
          "Select e FROM ScheduleEntry e WHERE e.project.projectName =?1 AND e.weekNumber =?2 ORDER BY e.weekNumber");
      query.setParameter(1, projectName);
      query.setParameter(2, weekNumber);
      List<ScheduleEntry> rs = query.getResultList();
      return rs;
    } catch (Exception e) {
      throw new DatasetException("Error while getting item from db: " + e.getMessage());
    }
  }

  /**
   * Loads all completed task of one project from database
   * 
   * @param projectName the project name
   * @return the completed tasks of a project
   * @throws DatasetException
   */
  @SuppressWarnings("unchecked")
  public List<Task> getCompletedTasks(String projectName) throws DatasetException {
    if (projectName == null) {
      return null;
    }
    try {
      final Query query =
          getEntityManager().createQuery("Select e FROM Task e WHERE e.project.projectName =?1 AND e.isComplete =?2");
      query.setParameter(1, projectName);
      query.setParameter(2, true);
      List<Task> rs = query.getResultList();
      return rs;
    } catch (Exception e) {
      throw new DatasetException("Error while getting item from db: " + e.getMessage());
    }
  }

  /**
   * Loads a test report form given project name and test name
   * 
   * @param string the project name
   * @param testname the test anme
   * @return the test of given project name and testname or null
   * @throws DatasetException
   */
  @SuppressWarnings("unchecked")
  public TestReport getTestReportEntry(String string, String testname) throws DatasetException {
    if (string == null) {
      return null;
    }
    try {
      final Query query =
          getEntityManager().createQuery("Select e FROM TestReport e WHERE e.project.projectName =?1 AND e.name =?2");
      query.setParameter(1, string);
      query.setParameter(2, testname);
      List<TestReport> rs = query.getResultList();
      if (rs.size() == 1) {
        return rs.get(0);
      }
      return null;
    } catch (Exception e) {
      throw new DatasetException("Error while getting item from db: " + e.getMessage());
    }
  }

  /**
   * Loads a defect record from database
   * 
   * @param projectName the name of the project name
   * @param id the number of the defect record
   * @return defect record if data base contains or null
   * @throws DatasetException
   */
  @SuppressWarnings("unchecked")
  public DefectRecord getDefectRecord(String projectName, int id) throws DatasetException {
    if (projectName == null) {
      return null;
    }
    try {
      final Query query = getEntityManager()
          .createQuery("Select e FROM DefectRecord e WHERE e.project.projectName =?1 AND e.number =?2");
      query.setParameter(1, projectName);
      query.setParameter(2, id);
      List<DefectRecord> rs = query.getResultList();
      if (rs.size() == 1) {
        return rs.get(0);
      }
      return null;
    } catch (Exception e) {
      throw new DatasetException("Error while getting item from db: " + e.getMessage());
    }
  }

  /**
   * @param projectName the name of the project
   * @param taskID the id of the task in that project
   * @return a list of time records, which are linked to the task with given id
   * @throws DatasetException
   */
  @SuppressWarnings("unchecked")
  public List<TimeRecord> getTimeRecord(String projectName, long taskID) throws DatasetException {
    if (projectName == null) {
      return null;
    }
    try {
      final Query query = getEntityManager()
          .createQuery("Select e FROM TimeRecord e WHERE e.project.projectName =?1 AND e.task.ID =?2");
      query.setParameter(1, projectName);
      query.setParameter(2, taskID);
      List<TimeRecord> rs = query.getResultList();
      return rs;
    } catch (Exception e) {
      throw new DatasetException("Error while getting item from db: " + e.getMessage());
    }
  }

  /**
   * 
   * @param projectName the name of the project
   * @param id the id of the task, which is linked to the defect
   * @return one defect record of a given project and linked task
   * @throws DatasetException
   */
  @SuppressWarnings("unchecked")
  public DefectRecord getDefectRecord(String projectName, long id) throws DatasetException {
    if (projectName == null) {
      return null;
    }
    try {
      final Query query = getEntityManager()
          .createQuery("Select e FROM DefectRecord e WHERE e.project.projectName =?1 AND e.task.ID =?2");
      query.setParameter(1, projectName);
      query.setParameter(2, id);
      List<DefectRecord> rs = query.getResultList();
      if (rs.size() == 1) {
        return rs.get(0);
      }
      return null;
    } catch (Exception e) {
      throw new DatasetException("Error while getting item from db: " + e.getMessage());
    }
  }

  /**
   * @param name the project name
   * @param string the project relative path of one resource
   * @return a list of defect records linked to one resource
   * @throws DatasetException
   */
  @SuppressWarnings("unchecked")
  public List<DefectRecord> getDefectRecord(String name, String string) throws DatasetException {
    if (name == null) {
      return null;
    }
    try {
      final Query query = getEntityManager()
          .createQuery("Select e FROM DefectRecord e WHERE e.project.projectName =?1 AND e.filePath = ?2");
      query.setParameter(1, name);
      query.setParameter(2, string);
      List<DefectRecord> rs = query.getResultList();
      return rs;
    } catch (Exception e) {
      throw new DatasetException("Error while getting item from db: " + e.getMessage());
    }
  }
}
