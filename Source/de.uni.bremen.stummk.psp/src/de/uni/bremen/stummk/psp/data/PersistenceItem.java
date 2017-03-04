package de.uni.bremen.stummk.psp.data;

import java.util.List;

/**
 * Interface for items, whcih will be stored in database
 * 
 * @author Konstantin
 *
 */
public interface PersistenceItem {

  /**
   * @return the database id of one persistence entity
   */
  long getID();

  /**
   * @return items for the table
   */
  List<String> getElements();

  /**
   * @return the project of the item
   */
  Project getProject();

}
