package de.uni.bremen.stummk.psp.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.TreeEvent;
import org.eclipse.swt.events.TreeListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.forms.widgets.FormToolkit;
import org.eclipse.ui.forms.widgets.TableWrapData;
import org.eclipse.ui.forms.widgets.TableWrapLayout;

import de.uni.bremen.stummk.psp.Activator;
import de.uni.bremen.stummk.psp.calculation.Filter;
import de.uni.bremen.stummk.psp.control.TaskOverview;
import de.uni.bremen.stummk.psp.data.PersistenceItem;
import de.uni.bremen.stummk.psp.data.Phase;
import de.uni.bremen.stummk.psp.data.Task;
import de.uni.bremen.stummk.psp.utility.Constants;
import swing2swt.layout.BorderLayout;

/**
 * Represents a table based on the {@link Grid}
 * 
 * @author Konstantin
 *
 */
public class Table {
  private Tree table;
  private List<? extends PersistenceItem> defaultItems = new ArrayList<PersistenceItem>();
  private IPreferenceStore store;
  private String sortCommand = "";
  private boolean sortable;
  private Task runningTask = null;


  /**
   * Constructor
   *
   * @param toolkit the toolkit
   * @param parent the parent composite of the table
   * @param columnHeader the column headers of the table
   * @param sortable true if this table should be sortable
   * @param sortCommand the preference stored in the preference store for this table
   */
  public Table(FormToolkit toolkit, Composite parent, Header[] columnHeader, boolean sortable, String sortCommand) {
    store = Activator.getDefault().getPreferenceStore();
    this.sortCommand = sortCommand;
    this.sortable = sortable;
    Layout layout = parent.getLayout();

    if (sortCommand.equals(Constants.COMMAND_SORT_TASK_OVERVIEW)) {
      table = new Tree(parent, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION | SWT.V_SCROLL | SWT.H_SCROLL);
      table.addTreeListener(new TreeListener() {

        @Override
        public void treeExpanded(TreeEvent e) {
          if (e.item instanceof TreeItem) {
            TreeItem item = (TreeItem) e.item;
            item.setExpanded(true);
            saveExpandedPrefs();
          }
        }

        @Override
        public void treeCollapsed(TreeEvent e) {
          if (e.item instanceof TreeItem) {
            TreeItem item = (TreeItem) e.item;
            item.setExpanded(false);
            saveExpandedPrefs();
          }
        }
      });
    } else if (columnHeader.equals(ColumnHeader.SUMMARY_HEADER) || columnHeader.equals(ColumnHeader.SIZE_HEADER)
        || columnHeader.equals(ColumnHeader.TIME_IN_PHASE_HEADER)
        || columnHeader.equals(ColumnHeader.DEFECT_INJECTED_HEADER)
        || columnHeader.equals(ColumnHeader.DEFECT_REMOVED_HEADER)) {
      table = new Tree(parent, SWT.BORDER);
    } else {
      table = new Tree(parent, SWT.BORDER | SWT.SINGLE | SWT.FULL_SELECTION);
    }

    if (layout instanceof BorderLayout) {
      table.setLayoutData(BorderLayout.CENTER);
    } else if (layout instanceof TableWrapLayout) {
      table.setLayoutData(new TableWrapData(TableWrapData.FILL_GRAB, TableWrapData.FILL_GRAB, 1, 1));
    } else if (layout instanceof GridLayout) {
      table.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    }

    table.setHeaderVisible(true);
    table.setVisible(true);
    table.setLinesVisible(true);

    toolkit.adapt(table);
    toolkit.paintBordersFor(table);

    createColumnHeader(columnHeader, table);

    if (sortable && !sortCommand.equals(Constants.COMMAND_SORT_TASK_OVERVIEW)) {
      // if sortable sort table
      sort();
    }
  }

  private void createColumnHeader(Header[] columnHeader, Tree table) {
    // create headers
    for (Header h : columnHeader) {
      if (h instanceof Column) {
        TreeColumn tableColumn = new TreeColumn(table, SWT.NONE);
        tableColumn.setText(h.getTitle());
        tableColumn.setWidth(((Column<?>) h).getSize());
        tableColumn.setResizable(true);
        tableColumn.setData(((Column<?>) h).getType().getSimpleName());

        // set click listener to columns to sort them
        if (sortable) {
          tableColumn.addSelectionListener(new SelectionAdapter() {
            @Override
            public void widgetSelected(SelectionEvent e) {
              if (!(tableColumn.getText().equals(ColumnHeader.HEADER_TO_PHASE)
                  && sortCommand.equals(Constants.COMMAND_SORT_TASK_OVERVIEW))) {
                sort(tableColumn, table.indexOf(tableColumn));
              }
              super.widgetSelected(e);
            }
          });
        }
      }
    }
  }

  /**
   * @return the selection of the table
   */
  public PersistenceItem getSelection() {
    if (table.getSelection().length > 0) {
      return table.getSelection()[0].getData() instanceof PersistenceItem
          ? (PersistenceItem) table.getSelection()[0].getData() : null;
    }
    return null;
  }

  /**
   * @return the phase of one selection in the task Overview
   */
  public Phase getPhaseOfSelection() {
    if (table.getSelection().length > 0) {
      if (table.getSelection()[0].getData() instanceof Task) {
        return ((Task) table.getSelection()[0].getData()).getPhase();
      }

      if (table.getSelection()[0].getData() instanceof String) {
        return Phase.fromString(((String) table.getSelection()[0].getData()));
      }
    }
    return null;
  }

  /**
   * Selects one Task in the {@link TaskOverview}
   * 
   * @param task the task which will be selected
   */
  public void select(Task task) {
    TreeItem[] items = table.getItems();
    for (TreeItem item : items) {
      for (TreeItem it : item.getItems()) {
        if (it.getData() instanceof Task && ((Task) it.getData()).getID() == task.getID()) {
          table.select(it);
          if (it.getParentItem() != null && it.getParentItem().getItemCount() > 0) {
            it.getParentItem().setExpanded(true);
            saveExpandedPrefs();
          }
        }
      }
    }
  }

  /**
   * Sets the running task to be drawn in the tabe of the taskoverview
   *
   * @param task the running task
   */
  public void setRunningTask(Task task) {
    this.runningTask = task;
  }

  /**
   * @return the Grid of the table
   */
  public Tree getTable() {
    return this.table;
  }

  /**
   * @return if the table belongs to the {@link TaskOverview}
   */
  public boolean isTaskOverview() {
    return sortCommand.equals(Constants.COMMAND_SORT_TASK_OVERVIEW);
  }

  /**
   * clear all data of the table
   */
  public void clear() {
    if (!this.table.isDisposed()) {
      this.table.removeAll();
    }
  }

  /**
   * Sorts the Table
   */
  public void sort() {
    if (sortCommand.equals(Constants.COMMAND_SORT_TASK_OVERVIEW)) {
      drawTaskOverview(defaultItems);
    } else {
      draw(defaultItems);
    }

    if (sortable) {
      // if sortable table, sort it by saved column and sort-style
      int columnIndex = -1;
      int sortStyle = SWT.NONE;

      if (!sortCommand.isEmpty()) {
        if (sortCommand.equals(Constants.COMMAND_SORT_TASK_OVERVIEW)) {
          columnIndex = getColumnIndex(store.getString(sortCommand).split("#")[0]);
        } else {
          columnIndex = Integer.valueOf(store.getString(sortCommand).split("#")[0]);
        }
        sortStyle = Integer.valueOf(store.getString(sortCommand).split("#")[1]);

        if (columnIndex != -1) {
          if (sortStyle == SWT.NONE) {
            table.setSortColumn(table.getColumn(columnIndex));
            table.setSortDirection(SWT.UP);
          } else if (sortStyle == SWT.DOWN) {
            table.setSortColumn(table.getColumn(columnIndex));
            table.setSortDirection(SWT.NONE);
          } else if (sortStyle == SWT.UP) {
            table.setSortColumn(table.getColumn(columnIndex));
            table.setSortDirection(SWT.DOWN);
          }
          sort(table.getColumn(columnIndex), columnIndex);
        }
      }
    }
  }

  private void sort(TreeColumn column, int columnIndex) {
    // sorts the item, depending on the sort-style
    List<TreeItem> items = Arrays.asList(table.getItems());
    List<PersistenceItem> pItems = new ArrayList<PersistenceItem>();
    // add items to be sorted
    if (sortCommand.equals(Constants.COMMAND_SORT_TASK_OVERVIEW)) {
      pItems.addAll(getChildrenOfPhases(items));
    } else {
      items.forEach(v -> pItems.add((PersistenceItem) v.getData()));
    }
    int sortStyle = SWT.NONE;
    if (column.equals(column.getParent().getSortColumn())) {
      sortStyle = column.getParent().getSortDirection();
    }
    column.getParent().getSortDirection();
    sort(sortStyle, pItems, column, columnIndex);
  }

  private void sort(int sortStyle, List<PersistenceItem> pItems, TreeColumn column, int columnIndex) {
    // clean sorted column and sorts the table
    clearColumnSort();
    switch (sortStyle) {
      // if actual no sort sort down
      case SWT.NONE:
        sortNone(pItems, column, columnIndex);
        break;
      // if sort actual sort down, sort up
      case SWT.DOWN:
        sortDown(pItems, column, columnIndex);
        break;
      // if actual sort up, sort none
      case SWT.UP:
        sortUp(pItems, column, columnIndex);
        break;
    }
  }

  private void sortUp(List<PersistenceItem> pItems, TreeColumn column, int columnIndex) {
    // sets the default values and set sort to none, if column is sorted up
    pItems.clear();
    pItems.addAll(defaultItems);
    if (sortCommand.equals(Constants.COMMAND_SORT_TASK_OVERVIEW)) {
      drawTaskOverview(pItems);
      store.setValue(sortCommand, table.getColumn(columnIndex).getText() + "#" + SWT.NONE);
    } else {
      draw(pItems);
      store.setValue(sortCommand, columnIndex + "#" + SWT.NONE);
    }
    table.setSortColumn(column);
    table.setSortDirection(SWT.NONE);
  }

  private void sortDown(List<PersistenceItem> pItems, TreeColumn column, int columnIndex) {
    // sorts up and set sort to up, if column is sorted down
    if (sortCommand.equals(Constants.COMMAND_SORT_TASK_OVERVIEW)) {
      pItems.sort((v1, v2) -> valueOf(getFilteredColumnData(((Task) v1).getOverviewElements()).get(columnIndex),
          getFilteredColumnData(((Task) v2).getOverviewElements()).get(columnIndex), (String) column.getData()));
      drawTaskOverview(pItems);
      store.setValue(sortCommand, table.getColumn(columnIndex).getText() + "#" + SWT.UP);
    } else {
      pItems.sort((v1, v2) -> valueOf(v1.getElements().get((columnIndex)), v2.getElements().get((columnIndex)),
          (String) column.getData()));
      draw(pItems);
      store.setValue(sortCommand, columnIndex + "#" + SWT.UP);
    }
    table.setSortColumn(column);
    table.setSortDirection(SWT.UP);
  }

  private void sortNone(List<PersistenceItem> pItems, TreeColumn column, int columnIndex) {
    // sorts down and set sort to down, if column is not sorted
    if (sortCommand.equals(Constants.COMMAND_SORT_TASK_OVERVIEW)) {
      pItems.sort((v1, v2) -> valueOf(getFilteredColumnData(((Task) v2).getOverviewElements()).get(columnIndex),
          getFilteredColumnData(((Task) v1).getOverviewElements()).get(columnIndex), (String) column.getData()));
      drawTaskOverview(pItems);
      store.setValue(sortCommand, table.getColumn(columnIndex).getText() + "#" + SWT.DOWN);
    } else {
      pItems.sort((v1, v2) -> valueOf(v2.getElements().get(columnIndex), v1.getElements().get(columnIndex),
          (String) column.getData()));
      draw(pItems);
      store.setValue(sortCommand, columnIndex + "#" + SWT.DOWN);
    }
    table.setSortColumn(column);
    table.setSortDirection(SWT.DOWN);
  }

  /**
   * Sets the default items without sorting of this table;
   *
   * @param defaltItems the default items of the table
   */
  public void setDefaultItems(List<? extends PersistenceItem> defaultItems) {
    this.defaultItems = defaultItems;
  }

  /**
   * Set the values to the table, in order they have been saved without any sorting
   */
  public void clearColumnSort() {
    table.setSortColumn(null);
  }

  private List<PersistenceItem> getChildrenOfPhases(List<TreeItem> items) {
    // return all children of one parent tree item
    List<PersistenceItem> pItems = new ArrayList<>();
    for (TreeItem item : items) {

      if (!(item.getData() instanceof Task)) {
        setExpanded(item.getText(), item.getExpanded());
      }

      for (TreeItem it : item.getItems()) {
        if (it.getData() instanceof Task) {
          pItems.add((Task) it.getData());
        }
      }
    }
    return pItems;
  }

  private void draw(List<? extends PersistenceItem> list) {
    // draw the items to the table
    clear();
    for (PersistenceItem item : list) {
      TreeItem gItem = new TreeItem(getTable(), SWT.NONE);
      gItem.setData(item);
      for (int i = 0; i < table.getColumnCount(); ++i) {
        gItem.setText(i, item.getElements().get(i));
      }
    }
  }

  private void drawTaskOverview(List<? extends PersistenceItem> items) {
    // Sets the data to the table
    clear();
    drawRunningTask();

    // draw the phases, to be shown
    List<String> filteredPhases = Filter.filterPhases(Phase.getValues());
    for (String phase : filteredPhases) {
      TreeItem phaseItem = new TreeItem(table, SWT.NONE);
      phaseItem.setText(phase);
      phaseItem.setData(phase);
      phaseItem.setExpanded(true);
    }

    // sets the grid items
    for (PersistenceItem item : items) {
      if (runningTask == null || ((Task) item).getID() != runningTask.getID()) {
        TreeItem parent = getPhaseItem(((Task) item).getPhase());
        if (parent != null) {
          TreeItem gItem = new TreeItem(parent, SWT.NONE);
          gItem.setData(item);
          for (int i = 0; i < table.getColumnCount(); ++i) {
            gItem.setText(i, getFilteredColumnData(((Task) item).getOverviewElements()).get(i));
          }

          if (((Task) item).getType().equals(Constants.TASK_TYPE_DEFECT_FIX)) {
            gItem.setBackground(new Color(gItem.getDisplay(), 255, 228, 225));
          }
        }
      }
    }

    for (TreeItem item : table.getItems()) {
      if (!(item.getData() instanceof Task)) {
        item.setExpanded(getExpanded((String) item.getData()));
      }
    }
  }

  private void drawRunningTask() {
    // draw the running task in the first row of the table
    if (runningTask != null) {
      TreeItem runningItem = new TreeItem(table, SWT.NONE);
      runningItem.setData(runningTask);
      List<String> list = getFilteredColumnData(runningTask.getOverviewElements());
      for (int i = 0; i < table.getColumnCount(); ++i) {
        runningItem.setText(i, list.get(i));
      }
      runningItem.setBackground(new Color(runningItem.getDisplay(), 192, 255, 62));
    }
  }

  /**
   * redraw the columns of the task overview
   */
  public void drawTaskOverviewColumns() {
    Header[] header = Filter.filterColumns(ColumnHeader.TASK_OVERVIEW_HEADER);
    TreeColumn[] columns = table.getColumns();
    for (TreeColumn column : columns) {
      column.dispose();
    }
    createColumnHeader(header, table);
  }

  private int valueOf(String v1, String v2, String type) {
    // compares to values, converting the strings to the vaues
    if (type.equals("String")) {
      return v1.compareToIgnoreCase(v2);
    } else if (type.equals("Integer")) {
      return Integer.valueOf(v1).compareTo(Integer.valueOf(v2));
    } else if (type.equals("Double")) {
      return Double.valueOf(v1).compareTo(Double.valueOf(v2));
    } else if (type.equals("Long")) {
      return Long.valueOf(v1).compareTo(Long.valueOf(v2));
    }
    return -100;
  }

  private int getColumnIndex(String string) {
    // get the column index by name for the task overview
    TreeColumn[] columns = table.getColumns();
    for (TreeColumn column : columns) {
      if (column.getText().equals(string)) {
        return table.indexOf(column);
      }
    }
    return -1;
  }

  private void setExpanded(String text, boolean expanded) {
    IPreferenceStore store = Activator.getDefault().getPreferenceStore();
    switch (text) {
      case "Planning":
        store.setValue(Constants.PREF_PLANNING_EXP, expanded);
      case "Design":
        store.setValue(Constants.PREF_DESIGN_EXP, expanded);
      case "Design Review":
        store.setValue(Constants.PREF_DESIGN_REVIEW_EXP, expanded);
      case "Code":
        store.setValue(Constants.PREF_CODE_EXP, expanded);
      case "Code Review":
        store.setValue(Constants.PREF_CODE_REVIEW_EXP, expanded);
      case "Compile":
        store.setValue(Constants.PREF_COMPILE_EXP, expanded);
      case "Test":
        store.setValue(Constants.PREF_TEST_EXP, expanded);
      case "Postmortem":
        store.setValue(Constants.PREF_POSTMORTEM_EXP, expanded);
    }
  }

  private boolean getExpanded(String phase) {
    IPreferenceStore store = Activator.getDefault().getPreferenceStore();
    switch (phase) {
      case "Planning":
        return store.getBoolean(Constants.PREF_PLANNING_EXP);
      case "Design":
        return store.getBoolean(Constants.PREF_DESIGN_EXP);
      case "Design Review":
        return store.getBoolean(Constants.PREF_DESIGN_REVIEW_EXP);
      case "Code":
        return store.getBoolean(Constants.PREF_CODE_EXP);
      case "Code Review":
        return store.getBoolean(Constants.PREF_CODE_REVIEW_EXP);
      case "Compile":
        return store.getBoolean(Constants.PREF_COMPILE_EXP);
      case "Test":
        return store.getBoolean(Constants.PREF_TEST_EXP);
      case "Postmortem":
        return store.getBoolean(Constants.PREF_POSTMORTEM_EXP);
    }
    return false;
  }

  /**
   * saves the expanded items state
   */
  public void saveExpandedPrefs() {
    if (sortCommand.equals(Constants.COMMAND_SORT_TASK_OVERVIEW)) {
      List<TreeItem> items = Arrays.asList(table.getItems());
      for (TreeItem item : items) {
        if (!(item.getData() instanceof Task)) {
          setExpanded(item.getText(), item.getExpanded());
        }
      }
    }
  }

  private List<String> getFilteredColumnData(List<String> overviewElements) {
    // return the data of the task, of the shown columns
    List<String> list = new ArrayList<String>();
    TreeColumn[] header = table.getColumns();
    for (int i = 0; i < header.length; ++i) {
      if (header[i].getText().equals(ColumnHeader.HEADER_TO_PHASE)) {
        list.add(overviewElements.get(0));
      }
      if (header[i].getText().equals(ColumnHeader.HEADER_TO_NAME)) {
        list.add(overviewElements.get(1));
      }
      if (header[i].getText().equals(ColumnHeader.HEADER_TO_PRIORITY)) {
        list.add(overviewElements.get(2));
      }
      if (header[i].getText().equals(ColumnHeader.HEADER_TO_PLAN_MIN)) {
        list.add(overviewElements.get(3));
      }
      if (header[i].getText().equals(ColumnHeader.HEADER_TO_ACTUAL_MIN)) {
        list.add(overviewElements.get(4));
      }
      if (header[i].getText().equals(ColumnHeader.HEADER_TO_PLAN_VALUE)) {
        list.add(overviewElements.get(5));
      }
      if (header[i].getText().equals(ColumnHeader.HEADER_TO_ACTUAL_VALUE)) {
        list.add(overviewElements.get(6));
      }
      if (header[i].getText().equals(ColumnHeader.HEADER_TO_STATUS)) {
        list.add(overviewElements.get(7));
      }
      if (header[i].getText().equals(ColumnHeader.HEADER_TO_CHANGE_DATE)) {
        list.add(overviewElements.get(8));
      }
    }
    return list;
  }

  private TreeItem getPhaseItem(Phase phase) {
    // get the phase grid item
    TreeItem[] items = table.getItems();
    for (TreeItem i : items) {
      if (i.getText(0).equals(phase.toString())) {
        return i;
      }
    }
    return null;
  }

}
