package de.uni.bremen.stummk.psp.calculation;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * this class contains math calculations
 * 
 * @author Konstantin
 *
 */
public class MathCalc {
  private static MathCalc instance;

  private MathCalc() {
    // private constructor
  }

  /**
   * @return an instance of {@link MathCalc}, if no one exists
   */
  public static MathCalc getInstance() {
    if (instance == null) {
      instance = new MathCalc();
    }
    return instance;
  }

  /**
   * @param number the double value, which will be rounded to two decimal places
   * @return the double value with two decimal places
   */
  public double round(double number) {
    return Math.round(number * 100) / 100.0;
  }

  /**
   * @param enddate the end date
   * @param endTime the end time
   * @param date the start date
   * @param startTime the start time
   * @return the delta minutes from given start date and time and an end date and time
   */
  public long getMin(LocalDate enddate, LocalTime endTime, LocalDate date, LocalTime startTime) {
    LocalDateTime start = LocalDateTime.of(date, startTime);
    LocalDateTime end = LocalDateTime.of(enddate, endTime);
    return ChronoUnit.MINUTES.between(start, end);
  }

  /**
   * @param seconds the minutes value in seconds
   * @return seconds in minutes
   */
  public long fromSecondToMinute(long seconds) {
    return TimeUnit.SECONDS.toMinutes(seconds);
  }

  /**
   * @param minutes the seconds value in minutes
   * @return minutes in seconds
   */
  public long fromMinuteToSecond(long minutes) {
    return TimeUnit.MINUTES.toSeconds(minutes);
  }

  /**
   * @param enddate the end date
   * @param endTime the end time
   * @param date the start date
   * @param startTime the start time
   * @return the delta minutes in milliseconds from given start date and time and an end date and
   *         time
   */
  public long getSeconds(LocalDate enddate, LocalTime endTime, LocalDate date, LocalTime startTime) {
    LocalDateTime start = LocalDateTime.of(date, startTime);
    LocalDateTime end = LocalDateTime.of(enddate, endTime);
    return ChronoUnit.SECONDS.between(start, end);
  }

  /**
   * @param dividend the dividend
   * @param divisor the divisor
   * 
   * @return the division of two number or 0 if divisor is 0
   */
  public double divide(double dividend, double divisor) {
    return divisor > 0.0 ? dividend / divisor : 0.0;
  }

  /**
   * @param date one date
   * @return the Monday of the week of the date
   */
  public LocalDate getMonday(LocalDate date) {
    return date.minusDays(((date.getDayOfWeek().getValue() % 8) - 1));
  }

  /**
   * @param startTime list with start times
   * @param endTime list with end times
   * @return the seconds of two lists with start and end times
   */
  public long calcSeconds(List<LocalDateTime> startTime, List<LocalDateTime> endTime) {
    long time = 0;
    for (int i = 0; i < endTime.size(); ++i) {
      time += MathCalc.getInstance().getSeconds(endTime.get(i).toLocalDate(), endTime.get(i).toLocalTime(),
          startTime.get(i).toLocalDate(), startTime.get(i).toLocalTime());
    }

    return time;
  }
}
