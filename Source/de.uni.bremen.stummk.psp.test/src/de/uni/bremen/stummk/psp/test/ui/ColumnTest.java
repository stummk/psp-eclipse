package de.uni.bremen.stummk.psp.test.ui;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import de.uni.bremen.stummk.psp.ui.Column;

/**
 * Tests Column
 * 
 * @author Konstantin
 *
 */
public class ColumnTest {
  private Column<?> classUnderTest;

  /**
   * Test Constructor
   */
  @Test
  public void testConstructor() {
    String title = "test";
    int size = 2;
    classUnderTest = new Column<Integer>(title, size, Integer.class);

    assertEquals(title, classUnderTest.getTitle());
    assertEquals(size, classUnderTest.getSize());
  }
}
