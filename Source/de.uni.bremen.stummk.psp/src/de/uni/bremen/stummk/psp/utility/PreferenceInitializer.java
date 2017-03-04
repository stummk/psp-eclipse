package de.uni.bremen.stummk.psp.utility;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;

import de.uni.bremen.stummk.psp.Activator;

/**
 * Class which initialize the default preference values of this plugin
 * 
 * @author Konstantin
 *
 */
public class PreferenceInitializer extends AbstractPreferenceInitializer {
  IPreferenceStore store;

  /**
   * Constructor
   */
  public PreferenceInitializer() {
    store = Activator.getDefault().getPreferenceStore();
  }

  @Override
  public void initializeDefaultPreferences() {
    store.setDefault(Constants.COMMAND_FILTER_READY, false);
    store.setDefault(Constants.COMMAND_FILTER_ALL_PHASES, true);
    store.setDefault(Constants.COMMAND_FILTER_PLANNING, true);
    store.setDefault(Constants.COMMAND_FILTER_DESIGN, true);
    store.setDefault(Constants.COMMAND_FILTER_DESIGN_REVIEW, true);
    store.setDefault(Constants.COMMAND_FILTER_CODE, true);
    store.setDefault(Constants.COMMAND_FILTER_CODE_REVIEW, true);
    store.setDefault(Constants.COMMAND_FILTER_COMPILE, true);
    store.setDefault(Constants.COMMAND_FILTER_TEST, true);
    store.setDefault(Constants.COMMAND_FILTER_POSTMORTEM, true);
    store.setDefault(Constants.COMMAND_FILTER_COLUMN_PRIORITY, false);
    store.setDefault(Constants.COMMAND_FILTER_COLUMN_TIME, true);
    store.setDefault(Constants.COMMAND_FILTER_COLUMN_VALUE, true);
    store.setDefault(Constants.COMMAND_FILTER_COLUMN_STATUS, true);
    store.setDefault(Constants.COMMAND_FILTER_COLUMN_CHANGE_DATE, false);
    store.setDefault(Constants.COMMAND_SORT_TASK_OVERVIEW, "-1#" + SWT.NONE);
    store.setDefault(Constants.COMMAND_SORT_TIME_RECORD, "-1#" + SWT.NONE);
    store.setDefault(Constants.COMMAND_SORT_DEFECT_RECORD, "-1#" + SWT.NONE);
    store.setDefault(Constants.COMMAND_SORT_TEST_REPORT, "-1#" + SWT.NONE);
    store.setDefault(Constants.COMMAND_SORT_PIP, "-1#" + SWT.NONE);
    store.setDefault(Constants.COMMAND_SORT_TASK_TEMPLATE, "-1#" + SWT.NONE);
    store.setDefault(Constants.COMMAND_SORT_SCHEDULE_TEMPLATE, "-1#" + SWT.NONE);
    store.setDefault(Constants.PREF_PLANNING_EXP, false);
    store.setDefault(Constants.PREF_DESIGN_EXP, false);
    store.setDefault(Constants.PREF_DESIGN_REVIEW_EXP, false);
    store.setDefault(Constants.PREF_CODE_EXP, false);
    store.setDefault(Constants.PREF_CODE_REVIEW_EXP, false);
    store.setDefault(Constants.PREF_COMPILE_EXP, false);
    store.setDefault(Constants.PREF_TEST_EXP, false);
    store.setDefault(Constants.PREF_POSTMORTEM_EXP, false);
  }

}
