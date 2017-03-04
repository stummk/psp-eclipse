package de.uni.bremen.stummk.psp.calculation;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jface.preference.IPreferenceStore;

import de.uni.bremen.stummk.psp.Activator;
import de.uni.bremen.stummk.psp.data.Phase;
import de.uni.bremen.stummk.psp.data.Task;
import de.uni.bremen.stummk.psp.ui.ColumnHeader;
import de.uni.bremen.stummk.psp.ui.Header;
import de.uni.bremen.stummk.psp.utility.Constants;

/**
 * Contains filter and sort functions
 * 
 * @author Konstantin
 *
 */
public class Filter {

  /**
   * Filter a list of {@link Task}. Return only tasks of the phases, which should be shown in the
   * TaskOverview
   * 
   * @param dataTofilter tasks to be filtered
   * @return filtered list of tasks with only phases, which will be shown in the task overview
   */
  public static List<Task> filter(List<Task> dataTofilter) {
    List<Task> filtered = dataTofilter;
    IPreferenceStore store = Activator.getDefault().getPreferenceStore();
    boolean hideReady = store.getBoolean(Constants.COMMAND_FILTER_READY);
    if (hideReady) {
      filtered = filtered.stream().filter(v -> !v.isComplete()).collect(Collectors.toList());
    }

    filtered =
        filtered.stream().filter(v -> getPhasesToShow().contains(v.getPhase().toString())).collect(Collectors.toList());

    return filtered;
  }

  /**
   * Filter a list of phases
   * 
   * @param phasesToFilter phases to be filtered
   * @return list of phases, which are shown in the task overview
   */
  public static List<String> filterPhases(String[] phasesToFilter) {
    List<String> filtered = new ArrayList<>();
    for (int i = 0; i < phasesToFilter.length; ++i) {
      if (getPhasesToShow().contains(phasesToFilter[i])) {
        filtered.add(phasesToFilter[i]);
      }
    }
    return filtered;
  }

  /**
   * Filter headers of the column header in the task overview
   * 
   * @param columns the column header
   * @return the filtered header
   */
  public static Header[] filterColumns(Header[] columns) {
    List<Header> filtered = new ArrayList<Header>();
    List<Header> columnsToShow = getColumnsToShow();
    for (Header header : columns) {
      if (columnsToShow.contains(header)) {
        filtered.add(header);
      }
    }

    Header[] filteredHeader = new Header[filtered.size()];
    filteredHeader = filtered.toArray(filteredHeader);
    return filteredHeader;
  }

  private static List<String> getPhasesToShow() {
    // selects the phases, which will be shown in the task overview
    List<String> phasesToShow = new ArrayList<>();
    IPreferenceStore store = Activator.getDefault().getPreferenceStore();
    if (store.getBoolean(Constants.COMMAND_FILTER_PLANNING)) {
      phasesToShow.add(Phase.getValue(Phase.PLANNING));
    }
    if (store.getBoolean(Constants.COMMAND_FILTER_DESIGN)) {
      phasesToShow.add(Phase.getValue(Phase.DESIGN));
    }
    if (store.getBoolean(Constants.COMMAND_FILTER_DESIGN_REVIEW)) {
      phasesToShow.add(Phase.getValue(Phase.DESIGNREVIEW));
    }
    if (store.getBoolean(Constants.COMMAND_FILTER_CODE)) {
      phasesToShow.add(Phase.getValue(Phase.CODE));
    }
    if (store.getBoolean(Constants.COMMAND_FILTER_CODE_REVIEW)) {
      phasesToShow.add(Phase.getValue(Phase.CODEREVIEW));
    }
    if (store.getBoolean(Constants.COMMAND_FILTER_COMPILE)) {
      phasesToShow.add(Phase.getValue(Phase.COMPILE));
    }
    if (store.getBoolean(Constants.COMMAND_FILTER_TEST)) {
      phasesToShow.add(Phase.getValue(Phase.TEST));
    }
    if (store.getBoolean(Constants.COMMAND_FILTER_POSTMORTEM)) {
      phasesToShow.add(Phase.getValue(Phase.POSTMORTEM));
    }

    return phasesToShow;
  }

  private static List<Header> getColumnsToShow() {
    // selects the column to be shown in the task overview
    List<Header> columnsToShow = new ArrayList<>();
    IPreferenceStore store = Activator.getDefault().getPreferenceStore();
    for (int i = 0; i < ColumnHeader.TASK_OVERVIEW_HEADER.length; ++i) {
      switch (i) {
        case 0:
          columnsToShow.add(ColumnHeader.TASK_OVERVIEW_HEADER[0]);
          break;
        case 1:
          columnsToShow.add(ColumnHeader.TASK_OVERVIEW_HEADER[1]);
          break;
        case 2:
          if (store.getBoolean(Constants.COMMAND_FILTER_COLUMN_PRIORITY)) {
            columnsToShow.add(ColumnHeader.TASK_OVERVIEW_HEADER[2]);
          }
          break;
        case 3:
          if (store.getBoolean(Constants.COMMAND_FILTER_COLUMN_TIME)) {
            columnsToShow.add(ColumnHeader.TASK_OVERVIEW_HEADER[3]);
            columnsToShow.add(ColumnHeader.TASK_OVERVIEW_HEADER[4]);
          }
          break;
        case 4:
          if (store.getBoolean(Constants.COMMAND_FILTER_COLUMN_VALUE)) {
            columnsToShow.add(ColumnHeader.TASK_OVERVIEW_HEADER[5]);
            columnsToShow.add(ColumnHeader.TASK_OVERVIEW_HEADER[6]);
          }
          break;
        case 5:
          if (store.getBoolean(Constants.COMMAND_FILTER_COLUMN_STATUS)) {
            columnsToShow.add(ColumnHeader.TASK_OVERVIEW_HEADER[7]);
          }
          break;
        case 6:
          if (store.getBoolean(Constants.COMMAND_FILTER_COLUMN_CHANGE_DATE)) {
            columnsToShow.add(ColumnHeader.TASK_OVERVIEW_HEADER[8]);
          }
          break;
      }
    }

    return columnsToShow;
  }

}
