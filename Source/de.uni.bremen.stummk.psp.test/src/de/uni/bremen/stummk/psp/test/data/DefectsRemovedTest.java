package de.uni.bremen.stummk.psp.test.data;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.uni.bremen.stummk.psp.calculation.MathCalc;
import de.uni.bremen.stummk.psp.data.DefectsRemoved;
import de.uni.bremen.stummk.psp.utility.Constants;

/**
 * Tests for the DefectsRemoved class
 * 
 * @author Konstantin
 *
 */
public class DefectsRemovedTest {
  private DefectsRemoved classUnderTest;

  @Test
  public void testConstructor() {
    classUnderTest = new DefectsRemoved();
    assertEquals(null, classUnderTest.getTitle());

    String title = "test";
    classUnderTest = new DefectsRemoved(title);
    assertEquals(title, classUnderTest.getTitle());
  }

  @Test
  public void testPutAndGet() {
    String title = "test";
    double plan = 2;
    double actual = 3;
    double toDate = 4;
    double toDatePer = 75.0;
    double defPerHour = 25.0;
    List<String> list = new ArrayList<>();
    list.add(title);
    list.add("" + MathCalc.getInstance().round(plan));
    list.add("" + MathCalc.getInstance().round(actual));
    list.add("" + MathCalc.getInstance().round(toDate));
    list.add("" + MathCalc.getInstance().round(toDatePer));
    list.add("" + MathCalc.getInstance().round(defPerHour));

    classUnderTest = new DefectsRemoved(title);
    classUnderTest.put(Constants.KEY_PLAN, plan);
    classUnderTest.put(Constants.KEY_ACTUAL, actual);
    classUnderTest.put(Constants.KEY_TO_DATE, toDate);
    classUnderTest.put(Constants.KEY_TO_DATE_PER, toDatePer);
    classUnderTest.put(Constants.KEY_DEFECT_PER_HOUR, defPerHour);

    assertEquals(plan, classUnderTest.get(Constants.KEY_PLAN), 0.0);
    assertEquals(actual, classUnderTest.get(Constants.KEY_ACTUAL), 0.0);
    assertEquals(toDate, classUnderTest.get(Constants.KEY_TO_DATE), 0.0);
    assertEquals(toDatePer, classUnderTest.get(Constants.KEY_TO_DATE_PER), 0.0);
    assertEquals(toDatePer, classUnderTest.get(Constants.KEY_DEFECT_PER_HOUR), 0.0);

    assertEquals(list, classUnderTest.getElements());
  }
}
