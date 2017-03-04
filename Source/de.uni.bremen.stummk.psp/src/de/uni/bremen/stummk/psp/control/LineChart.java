package de.uni.bremen.stummk.psp.control;

import java.awt.BorderLayout;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.NumberTickUnit;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.xy.XYDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import de.uni.bremen.stummk.psp.calculation.MathCalc;
import de.uni.bremen.stummk.psp.data.ScheduleEntry;
import de.uni.bremen.stummk.psp.utility.Constants;

/**
 * Class which represents a line chart which shows the tracking of earned values and spent time per
 * week in a project
 * 
 * @author Konstantin
 *
 */
public class LineChart {
  private int chartToShow = 0;
  private String title = "";
  private List<ScheduleEntry> schedulePlan = new ArrayList<ScheduleEntry>();
  private ChartPanel chartPanel;

  /**
   * Constructor
   * 
   * @param title the title of the chart
   * @param chartToShow the chart dat which wil be shown in the chart {@link Constants.CHART_TIME}
   *        or {@link Constants.CHART_VALUE}
   * @param schedulePlan the schedule plan which data will be shown in the chart
   */
  public LineChart(String title, int chartToShow, List<ScheduleEntry> schedulePlan) {
    this.title = title;
    this.chartToShow = chartToShow;
    this.schedulePlan = schedulePlan != null ? schedulePlan : Collections.emptyList();

    chartPanel = createChart();

    JFrame f = new JFrame(title);
    f.setTitle(title);
    f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    f.setLayout(new BorderLayout(0, 5));
    f.add(chartPanel, BorderLayout.CENTER);
    chartPanel.setMouseWheelEnabled(true);

    f.pack();
    f.setLocationRelativeTo(null);
    f.setVisible(true);
  }

  private ChartPanel createChart() {
    // create chart
    XYDataset data = createDataset();
    String xAxisLabel = "";
    String yAxisLabel = "";

    if (chartToShow == Constants.CHART_TIME) {
      xAxisLabel = "Weeks";
      yAxisLabel = "Cumulative Spent Time in Minutes";
    } else if (chartToShow == Constants.CHART_VALUE) {
      xAxisLabel = "Weeks";
      yAxisLabel = "Cumulative Earned Value";
    }

    JFreeChart chart = ChartFactory.createXYLineChart(title, xAxisLabel, yAxisLabel, data, PlotOrientation.VERTICAL,
        true, true, false);

    // customize plot
    final XYPlot plot = chart.getXYPlot();
    plot.setBackgroundPaint(Color.lightGray);
    plot.setDomainGridlinePaint(Color.white);
    plot.setRangeGridlinePaint(Color.white);
    plot.setNoDataMessage("No data to display");

    XYLineAndShapeRenderer renderer = (XYLineAndShapeRenderer) plot.getRenderer();
    renderer.setBaseShapesVisible(true);

    // Tick unit in x-Axis should be 1
    NumberAxis domain = (NumberAxis) plot.getDomainAxis();
    domain.setTickUnit(new NumberTickUnit(1.0));
    domain.setVerticalTickLabels(true);

    return new ChartPanel(chart);
  }

  private XYDataset createDataset() {
    // create dataset
    final XYSeries planSeries = new XYSeries("Plan");
    final XYSeries actualSeries = new XYSeries("Actual");

    for (ScheduleEntry e : schedulePlan) {
      if (chartToShow == Constants.CHART_TIME) {
        // get minutes of cumulative planned and actual hour
        double pTime = ((double) e.getCumulativePlannedHours());
        double aTime = ((double) e.getActualCumulativeHour() / 60);
        planSeries.add(e.getWeekNumber(), MathCalc.getInstance().round((pTime)));
        actualSeries.add(e.getWeekNumber(), MathCalc.getInstance().round(aTime));
      } else if (chartToShow == Constants.CHART_VALUE) {
        planSeries.add(e.getWeekNumber(), MathCalc.getInstance().round(e.getCumulativeEarnedValue()));
        actualSeries.add(e.getWeekNumber(), MathCalc.getInstance().round(e.getActualEarnedValue()));
      }
    }

    // create dataset
    final XYSeriesCollection dataset = new XYSeriesCollection();
    dataset.addSeries(planSeries);
    dataset.addSeries(actualSeries);

    return dataset;
  }
}
