package de.uni.bremen.stummk.psp.ui;

/**
 * Represents a column in the grid
 * 
 * @author Konstantin
 * @param <T> the column type
 *
 */
public class Column<T> implements Header {

  private String title;

  private int size;

  private boolean isTree;

  private Class<T> type;

  /**
   * Constructor
   * 
   * @param title the title of the column
   * @param size size of the column width
   * @param type the type of data in this column
   */
  public Column(String title, int size, Class<T> type) {
    this.title = title;
    this.size = size;
    this.isTree = false;
    this.type = type;
  }

  /**
   * Constructor
   * 
   * @param title the title of the column
   * @param size size of the column width
   * @param isTree if this column is a tree
   * @param type the type of data in this column
   */
  public Column(String title, int size, boolean isTree, Class<T> type) {
    this.title = title;
    this.size = size;
    this.isTree = isTree;
    this.type = type;
  }

  @Override
  public String getTitle() {
    return this.title;
  }

  /**
   * @return the column width
   */
  public int getSize() {
    return this.size;
  }

  /**
   * @return true if column has children items
   */
  public boolean isTree() {
    return this.isTree;
  }

  /**
   * return the type of the data store in this column
   * 
   * @return
   */
  public Class<T> getType() {
    return this.type;
  }

}
