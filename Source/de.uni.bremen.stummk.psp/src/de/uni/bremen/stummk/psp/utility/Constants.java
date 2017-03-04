package de.uni.bremen.stummk.psp.utility;

import org.eclipse.core.runtime.QualifiedName;

import de.uni.bremen.stummk.psp.Activator;

/**
 * Contains all constants
 * 
 * @author Konstantin
 *
 */
public class Constants {

  // STATUS

  /**
   * Status id, which shows, that no task is running
   */
  public static final int STATUS_NO_TASK = 1000;

  /**
   * Status id, which shows, that one task is running
   */
  public static final int STATUS_TASK_RUNNING = 2000;

  /**
   * Status id, which shows, that running task is interrupted
   */
  public static final int STATUS_TASK_INTERRUPT = 3000;

  /**
   * Status, which shows, that a task is unready
   */
  public static final String STATUS_UNREADY = "Unready";

  /**
   * Status, which shows, that a task is ready
   */
  public static final String STATUS_READY = "Ready";

  /**
   * Status, which shows, that a task is stopped
   */
  public static final String STATUS_STOPPED = "Stopped";

  /**
   * Status, which shows, that a task is running
   */
  public static final String STATUS_RUNNING = "Running";

  /**
   * Status, which shows, that a task is interrupted
   */
  public static final String STATUS_INTERRUPT = "Interrupt";


  // COMMANDS

  /**
   * Command, that shows plan vs. actual values diagram
   */
  public static final String COMMAND_PLAN_ACTUAL_DIAGRAM = "de.uni.bremen.stummk.psp.plan.actual.command";

  /**
   * Command, that synchronize data from db to file
   */
  public static final String COMMAND_SYNC = "de.uni.bremen.stummk.psp.sync.command";

  /**
   * Command, called to show chart with distributed injected defects per phases
   */
  public static final String COMMAND_DEFECT_INJECTED_PERCENTAGE =
      "de.uni.bremen.stummk.psp.defect.injected.percentage.command";

  /**
   * Command, called to show chart with distributed removed defects per phases
   */
  public static final String COMMAND_DEFECT_REMOVED_PERCENTAGE =
      "de.uni.bremen.stummk.psp.defect.removed.perscentage.command";

  /**
   * Command, called to show chart with distributed time per phases
   */
  public static final String COMMAND_TIME_IN_PHASE_PERCENTAGE = "de.uni.bremen.stummk.psp.time.perscentage.command";

  /**
   * Command, called to show chart with earned value tracking
   */
  public static final String COMMAND_EARNED_VALUE_TRACKING = "de.uni.bremen.stummk.psp.earned.value.tracking.command";

  /**
   * Command, called to show chart with time tracking
   */
  public static final String COMMAND_TIME_TRACKING = "de.uni.bremen.stummk.psp.time.tracking.command";

  /**
   * Command, that shows info about running task
   */
  public static final String COMMAND_INFO = "de.uni.bremen.stummk.psp.info.command";


  /**
   * Command, that starts the record of data of one task
   */
  public static final String COMMAND_START = "de.uni.bremen.stummk.psp.start.command";

  /**
   * Command, that interrupts recording data of running task
   */
  public static final String COMMAND_INTERRUPT = "de.uni.bremen.stummk.psp.interrupt.command";


  /**
   * Command, that stops recording data for a task
   */
  public static final String COMMAND_STOP = "de.uni.bremen.stummk.psp.stop.command";

  /**
   * Command, that marks a selected task as complete
   */
  public static final String COMMAND_COMPLETE_TASK = "de.uni.bremen.stummk.psp.complete.command";

  /**
   * Command, that open an dialog to add or edit something
   */
  public static final String COMMAND_ADD = "de.uni.bremen.stummk.psp.add.command";

  /**
   * Command, that open an dialog to add a new task
   */
  public static final String COMMAND_ADD_TASK = "de.uni.bremen.stummk.psp.addtask.command";

  /**
   * Command, that open an dialog to add a new defect record
   */
  public static final String COMMAND_ADD_DEFECT_RECORD = "de.uni.bremen.stummk.psp.adddefectrecord.command";

  /**
   * Command, that open an dialog to add a new time record
   */
  public static final String COMMAND_ADD_TIME_RECORD = "de.uni.bremen.stummk.psp.addtimerecord.command";

  /**
   * Command, that open an dialog to add a new pip
   */
  public static final String COMMAND_ADD_PIP = "de.uni.bremen.stummk.psp.addpip.command";

  /**
   * Command, that open an dialog to add a new number of locs
   */
  public static final String COMMAND_ADD_LOC = "de.uni.bremen.stummk.psp.addloc.command";

  /**
   * Command, that convert a project to psp project
   */
  public static final String COMMAND_CONVERT = "de.uni.bremen.stummk.psp.convert.command";

  /**
   * Filter command
   */
  public static final String COMMAND_FILTER = "de.uni.bremen.stummk.psp.filter.command";

  /**
   * Filter: hide ready tasks
   */
  public static final String COMMAND_FILTER_READY = "de.uni.bremen.stummk.psp.filter.ready.command";

  /**
   * Filter: show all phases
   */
  public static final String COMMAND_FILTER_ALL_PHASES = "de.uni.bremen.stummk.psp.filter.phases.command";

  /**
   * Filter: show planning phase
   */
  public static final String COMMAND_FILTER_PLANNING = "de.uni.bremen.stummk.psp.filter.planning.command";

  /**
   * Filter: show design phase
   */
  public static final String COMMAND_FILTER_DESIGN = "de.uni.bremen.stummk.psp.filter.design.command";

  /**
   * Filter: show design review phase
   */
  public static final String COMMAND_FILTER_DESIGN_REVIEW = "de.uni.bremen.stummk.psp.filter.design.review.command";

  /**
   * Filter: show code phase
   */
  public static final String COMMAND_FILTER_CODE = "de.uni.bremen.stummk.psp.filter.code.command";

  /**
   * Filter: show code review phase
   */
  public static final String COMMAND_FILTER_CODE_REVIEW = "de.uni.bremen.stummk.psp.filter.code.review.command";

  /**
   * Filter: show compile phase
   */
  public static final String COMMAND_FILTER_COMPILE = "de.uni.bremen.stummk.psp.filter.compile.command";

  /**
   * Filter: show test phase
   */
  public static final String COMMAND_FILTER_TEST = "de.uni.bremen.stummk.psp.filter.test.command";

  /**
   * Filter: show postmortem phase
   */
  public static final String COMMAND_FILTER_POSTMORTEM = "de.uni.bremen.stummk.psp.filter.postmortem.command";

  /**
   * Filter: show priority column
   */
  public static final String COMMAND_FILTER_COLUMN_PRIORITY = "de.uni.bremen.stummk.psp.filter.column.priority.command";

  /**
   * Filter: show time column
   */
  public static final String COMMAND_FILTER_COLUMN_TIME = "de.uni.bremen.stummk.psp.filter.column.time.command";

  /**
   * Filter: show value column
   */
  public static final String COMMAND_FILTER_COLUMN_VALUE = "de.uni.bremen.stummk.psp.filter.column.value.command";

  /**
   * Filter: show status column
   */
  public static final String COMMAND_FILTER_COLUMN_STATUS = "de.uni.bremen.stummk.psp.filter.column.status.command";

  /**
   * Filter: show change date column
   */
  public static final String COMMAND_FILTER_COLUMN_CHANGE_DATE = "de.uni.bremen.stummk.psp.filter.column.date.command";

  /**
   * sort preference command sorting task overview
   */
  public static final String COMMAND_SORT_TASK_OVERVIEW = "de.uni.bremen.stummk.psp.sort.task.overview.command";

  /**
   * sort preference command sorting time record
   */
  public static final String COMMAND_SORT_TIME_RECORD = "de.uni.bremen.stummk.psp.sort.time.record.command";

  /**
   * sort preference command sorting defect records
   */
  public static final String COMMAND_SORT_DEFECT_RECORD = "de.uni.bremen.stummk.psp.sort.defect.record.command";

  /**
   * sort preference command sorting test report
   */
  public static final String COMMAND_SORT_TEST_REPORT = "de.uni.bremen.stummk.psp.sort.test.report.command";

  /**
   * sort preference command sorting pip
   */
  public static final String COMMAND_SORT_PIP = "de.uni.bremen.stummk.psp.sort.pip.command";

  /**
   * sort preference command sorting task template
   */
  public static final String COMMAND_SORT_TASK_TEMPLATE = "de.uni.bremen.stummk.psp.sort.task.template.command";

  /**
   * sort preference command sorting schedule template
   */
  public static final String COMMAND_SORT_SCHEDULE_TEMPLATE = "de.uni.bremen.stummk.psp.sort.schedule.template.command";

  /**
   * Command ID delete an item from table
   */
  public static final String COMMAND_DELETE = "de.uni.bremen.stummk.psp.delete.command";

  /**
   * Preference planning phase expanded
   */
  public static final String PREF_PLANNING_EXP = "de.uni.bremen.stummk.psp.plan.pref.exp";

  /**
   * Preference design phase expanded
   */
  public static final String PREF_DESIGN_EXP = "de.uni.bremen.stummk.psp.design.pref.exp";

  /**
   * Preference design review phase expanded
   */
  public static final String PREF_DESIGN_REVIEW_EXP = "de.uni.bremen.stummk.psp.design.review.pref.exp";

  /**
   * Preference code phase expanded
   */
  public static final String PREF_CODE_EXP = "de.uni.bremen.stummk.psp.code.pref.exp";

  /**
   * Preference code review phase expanded
   */
  public static final String PREF_CODE_REVIEW_EXP = "de.uni.bremen.stummk.psp.code.review.pref.exp";

  /**
   * Preference compile phase expanded
   */
  public static final String PREF_COMPILE_EXP = "de.uni.bremen.stummk.psp.compile.exp";

  /**
   * Preference test phase expanded
   */
  public static final String PREF_TEST_EXP = "de.uni.bremen.stummk.psp.test.exp";

  /**
   * Preference postmortem phase expanded
   */
  public static final String PREF_POSTMORTEM_EXP = "de.uni.bremen.stummk.psp.postmortem.pref.exp";


  // IDs of editor pages, view etc.

  /**
   * Editor ID
   */
  public static final String EDITOR_ID = "de.uni.bremen.stummk.psp.editor";

  /**
   * Wizard ID
   */
  public static final String WIZARD_ID = "de.uni.bremen.stummk.psp.wizard";

  /**
   * Perspective ID
   */
  public static final String PERSPECTIVE_ID = "de.uni.bremen.stummk.psp.perspective";

  /**
   * Resource summary view ID
   */
  public static final String RESOURCE_SUMMARY_ID = "de.uni.bremen.stummk.psp.resource.summary";

  /**
   * ID of the project summary form
   */
  public static final String ID_PROJECT_SUMMARY_FORM = "de.uni.bremen.stummk.psp.projectsummaryform";

  /**
   * ID of the project time record form
   */
  public static final String ID_TIME_RECORD_FORM = "de.uni.bremen.stummk.psp.timerecordinglogform";

  /**
   * ID of the project defect record form
   */
  public static final String ID_DEFECT_RECORD_FORM = "de.uni.bremen.stummk.psp.defectrecordinglogform";

  /**
   * ID of the project pip form
   */
  public static final String ID_PIP_FORM = "de.uni.bremen.stummk.psp.pipform";

  /**
   * ID of the project task planning form
   */
  public static final String ID_TASK_PLANNING_FORM = "de.uni.bremen.stummk.psp.taskplaningform";

  /**
   * ID of the project schedule planning form
   */
  public static final String ID_SCHEDULE_PLANNING_FORM = "de.uni.bremen.stummk.psp.scheduleplaningform";

  /**
   * ID of the project test report form
   */
  public static final String ID_TEST_REPROT_FORM = "de.uni.bremen.stummk.psp.testreportform";

  /**
   * ID of the project task overview
   */
  public static final String ID_TASK_OVERVIEW = "de.uni.bremen.stummk.psp.task.overview";


  // Project Plan Summary Keys

  // SUMMARY- Section KEYS

  /**
   * Key summary of the project plan summary
   */
  public static final int KEY_SUMMARY_IDX = 100;
  /**
   * Key summary of the project plan summary
   */
  public static final String KEY_SUMMARY = "Summary";

  /**
   * Key loc per hour of summary of the project plan summary
   */
  public static final String KEY_SUM_LOC_PER_HOUR = "LoC/Hour";

  /**
   * Key time of summary of the project plan summary
   */
  public static final String KEY_SUM_TIME = "Total Time";

  /**
   * Key defects per kloc of summary of the project plan summary
   */
  public static final String KEY_SUM_DEFECTS_PER_LOC = "Defects/KLOC";

  /**
   * Key yield of summary of the project plan summary
   */
  public static final String KEY_SUM_YIELD = "Yield";

  /**
   * Key af ratio of summary of the project plan summary
   */
  public static final String KEY_SUM_AF_RATIO = "A/F Ratio";

  // SECTION KEYS

  /**
   * Key program size of the project plan summary
   */
  public static final int KEY_PROGRAM_SIZE_IDX = 200;

  /**
   * Key program size of the project plan summary
   */
  public static final String KEY_PROGRAM_SIZE = "Program Size (LoC)";

  /**
   * Key total loc of size of the project plan summary
   */
  public static final String KEY_SIZE_TOTAL_LOC = "Total LoC";

  /**
   * Key time in phase of the project plan summary
   */
  public static final int KEY_TIME_IN_PHASE_IDX = 300;

  /**
   * Key time in phase of the project plan summary
   */
  public static final String KEY_TIME_IN_PHASE = "Time in Phase (min.)";

  /**
   * Key defects injected of the project plan summary
   */
  public static final int KEY_DEFECTS_INJECTED_IDX = 400;

  /**
   * Key defects injected of the project plan summary
   */
  public static final String KEY_DEFECTS_INJECTED = "Defects Injected";

  /**
   * Key defect removed of the project plan summary
   */
  public static final int KEY_DEFECTS_REMOVED_IDX = 500;

  /**
   * Key defect removed of the project plan summary
   */
  public static final String KEY_DEFECTS_REMOVED = "Defects Removed";

  // PHASE KEYS

  /**
   * Key phase planning of the project plan summary
   */
  public static final String KEY_PHASE_PLANNING = "Planning";

  /**
   * Key phase design of the project plan summary
   */
  public static final String KEY_PHASE_DESIGN = "Design";

  /**
   * Key phase design review of the project plan summary
   */
  public static final String KEY_PHASE_DESIGN_REVIEW = "Design Review";

  /**
   * Key phase code of the project plan summary
   */
  public static final String KEY_PHASE_CODE = "Code";

  /**
   * Key phase code review of the project plan summary
   */
  public static final String KEY_PHASE_CODE_REVIEW = "Code Review";

  /**
   * Key summary of the project plan summary
   */
  public static final String KEY_PHASE_COMPILE = "Compile";

  /**
   * Key phase test of the project plan summary
   */
  public static final String KEY_PHASE_TEST = "Test";

  /**
   * Key phase potmortem of the project plan summary
   */
  public static final String KEY_PHASE_POSTMORTEM = "Postmortem";

  /**
   * Key phase total of the project plan summary
   */
  public static final String KEY_PHASE_TOTAL = "Total";

  // COLUMN KEYS

  /**
   * Key plan of the project plan summary
   */
  public static final String KEY_PLAN = "Plan";

  /**
   * Key actual of the project plan summary
   */
  public static final String KEY_ACTUAL = "Actual";

  /**
   * Key to date of the project plan summary
   */
  public static final String KEY_TO_DATE = "To Date";

  /**
   * Key to date percent of the project plan summary
   */
  public static final String KEY_TO_DATE_PER = "To Date %";

  /**
   * Key defect per hour of the project plan summary
   */
  public static final String KEY_DEFECT_PER_HOUR = "Def./Hour";

  // DEFECT TYPES

  /**
   * defect type documentation
   */
  public static final String DEFECT_TYPE_DOCUMENTATION = "Documentation";

  /**
   * defect type syntax
   */
  public static final String DEFECT_TYPE_Syntax = "Syntax";

  /**
   * defect type build
   */
  public static final String DEFECT_TYPE_Build = "Build, Package";

  /**
   * defect type assignment
   */
  public static final String DEFECT_TYPE_Assignment = "Assignment";

  /**
   * defect type interface
   */
  public static final String DEFECT_TYPE_Interface = "Interface";

  /**
   * defect type checking
   */
  public static final String DEFECT_TYPE_CHECKING = "Checking";

  /**
   * defect type data
   */
  public static final String DEFECT_TYPE_DATA = "Data";

  /**
   * defect type function
   */
  public static final String DEFECT_TYPE_FUNCTION = "Function";

  /**
   * defect type system
   */
  public static final String DEFECT_TYPE_SYSTEM = "System";

  /**
   * defect type environment
   */
  public static final String DEFECT_TYPE_ENVIRONMENT = "Environment";

  // File extensions
  /**
   * JAVA File extension
   */
  public static final int FILE_JAVA = 10000;

  /**
   * c file extension
   */
  public static final int FILE_C = 11000;

  /**
   * ruby file extension
   */
  public static final int FILE_RUBY = 13000;

  /**
   * if source file
   */
  public static final int FILE_COMPILE = 11111;

  // Properties

  /**
   * File hash property
   */
  public static final QualifiedName PROPERTY_HASH = new QualifiedName(Activator.PLUGIN_ID, "PSP_FILE_HASH");

  /**
   * Perspective open property
   */
  public static final String PROPERTY_OPEN_PERSPECTIVE = "PSP_OPEN_PERSPECTVIE_STATE";

  // TASK TYPES

  /**
   * Task type task
   */
  public static final String TASK_TYPE_TASK = "Task";

  /**
   * Task type defect fix
   */
  public static final String TASK_TYPE_DEFECT_FIX = "Defect Fix";

  /**
   * line Chart time
   */
  public static final int CHART_TIME = 20000;

  /**
   * line chart value
   */
  public static final int CHART_VALUE = 21000;
}
