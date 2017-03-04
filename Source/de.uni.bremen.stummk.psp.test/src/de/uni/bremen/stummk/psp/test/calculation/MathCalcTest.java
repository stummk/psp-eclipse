package de.uni.bremen.stummk.psp.test.calculation;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.LocalTime;

import org.junit.BeforeClass;
import org.junit.Test;

import de.uni.bremen.stummk.psp.calculation.MathCalc;

/**
 * Test class, which contains the test for the MathCalc class
 * 
 * @author Konstantin
 *
 */
public class MathCalcTest {
  private static MathCalc classUnderTest;

  @BeforeClass
  public static void setUp() {
    classUnderTest = MathCalc.getInstance();
  }

  /**
   * Test the round method
   */
  @Test
  public void testRound() {
    double value = 1.12345;
    double result = 1.12;

    assertEquals(result, classUnderTest.round(value), 0.0);
  }

  /**
   * Test the divide Method
   */
  @Test
  public void testDivide() {
    double dividend = 5;
    double zero = 0;
    double divisor = 2;
    double result = 2.5;

    assertEquals(zero, classUnderTest.divide(dividend, zero), 0.0);
    assertEquals(result, classUnderTest.divide(dividend, divisor), 0.0);
  }

  /**
   * check if second and minutes will be returned right.
   */
  @Test
  public void testGetMinAndSec() {
    LocalDate start = LocalDate.of(2016, 9, 20);
    LocalDate end = LocalDate.of(2016, 9, 20);
    LocalTime startTime = LocalTime.of(10, 10);
    LocalTime endTime = LocalTime.of(10, 11);
    long sec = 60;
    long min = 1;

    assertEquals(min, classUnderTest.getMin(end, endTime, start, startTime));
    assertEquals(sec, classUnderTest.getSeconds(end, endTime, start, startTime));
  }

  /**
   * Tests if Minutes are converted to seconds right and if seconds are converted to minutes right.
   */
  @Test
  public void testFromSecToMinAndMinToSec() {
    long sec = 60;
    long min = 1;

    assertEquals(sec, classUnderTest.fromMinuteToSecond(min));
    assertEquals(min, classUnderTest.fromSecondToMinute(sec));
  }
}

