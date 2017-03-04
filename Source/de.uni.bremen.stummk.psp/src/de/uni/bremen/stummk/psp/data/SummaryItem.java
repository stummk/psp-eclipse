package de.uni.bremen.stummk.psp.data;

/**
 * Defines operations of a table row in the {@link ProjectPlanSummary}
 * 
 * @author Konstantin
 *
 */
public interface SummaryItem extends PersistenceItem {

  /**
   * Puts a new value the given key
   * 
   * @param key the key of the value
   * @param value the new value
   */
  void put(String key, double value);

  /**
   * @param key key of the value
   * @return the value of the given key
   */
  double get(String key);

  /**
   * @return the title
   */
  String getTitle();

  /**
   * @return A String containing all entries splitted by a #
   */
  @Override
  String toString();
}
