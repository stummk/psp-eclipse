package de.uni.bremen.stummk.psp.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.forms.widgets.FormToolkit;

import de.uni.bremen.stummk.psp.data.PersistenceItem;

/**
 * Represents the search field
 * 
 * @author Konstantin
 *
 */
public class SearchField {

  Text txtSearch;

  /**
   * Constructor
   * 
   * @param parent the parent composite
   * @param toolkit the toolkit
   */
  public SearchField(Composite parent, FormToolkit toolkit) {
    txtSearch = new Text(parent, SWT.BORDER);
    txtSearch.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
    toolkit.adapt(txtSearch, true, true);
    txtSearch.setMessage("Search...");
  }

  /**
   * @return the search field
   */
  public Text getSearchField() {
    return txtSearch;
  }

  /**
   * Searches in table to entered text in the search field
   * 
   * @param table the table, which data will be searched
   */
  public void search(Table table) {
    List<TreeItem> searchList = new ArrayList<TreeItem>();
    if (table.isTaskOverview()) {
      for (TreeItem it : table.getTable().getItems()) {
        searchList.addAll(Arrays.asList(it.getItems()));
      }
    } else {
      TreeItem[] item = table.getTable().getItems();
      searchList.addAll(Arrays.asList(item));
    }

    List<PersistenceItem> result = new ArrayList<PersistenceItem>();
    for (TreeItem i : searchList) {
      String[] strings = txtSearch.getText().split("\\s+");
      for (int j = 0; j < i.getParent().getColumnCount(); ++j) {
        for (String s : strings) {
          if (i.getText(j).matches(".*" + s + ".*")) {
            if (!result.contains((PersistenceItem) i.getData())) {
              result.add((PersistenceItem) i.getData());
            }
            break;
          }
        }

        if (result.contains(i)) {
          break;
        }
      }
    }

    table.setDefaultItems(result);
    table.sort();
  }
}
