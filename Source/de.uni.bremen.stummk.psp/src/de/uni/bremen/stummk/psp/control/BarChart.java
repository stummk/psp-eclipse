package de.uni.bremen.stummk.psp.control;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.CategoryDataset;
import org.jfree.data.category.DefaultCategoryDataset;

import de.uni.bremen.stummk.psp.calculation.MathCalc;
import de.uni.bremen.stummk.psp.data.ProjectPlanSummary;
import de.uni.bremen.stummk.psp.utility.Constants;

/**
 * Represents a bar chart for the comparison of the plan and actual values
 * 
 * @author Konstantin
 *
 */
public class BarChart {
  private String title = "";
  private ChartPanel chartPanel;
  private ProjectPlanSummary pps;

  /**
   * Constructor
   * 
   * @param pps the {@link ProjectPlanSummary} which holds the values
   * @param title the title of the chart
   */
  public BarChart(ProjectPlanSummary pps, String title) {
    this.pps = pps;
    this.title = title;
    chartPanel = createChart();
    JFrame f = new JFrame(title);
    f.setTitle(title);
    f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
    f.setLayout(new BorderLayout(0, 5));
    f.add(chartPanel, BorderLayout.CENTER);
    chartPanel.setMouseWheelEnabled(true);
    chartPanel.setHorizontalAxisTrace(true);
    chartPanel.setVerticalAxisTrace(true);
    chartPanel.restoreAutoBounds();

    f.pack();
    f.setLocationRelativeTo(null);
    f.setVisible(true);
  }

  private ChartPanel createChart() {
    // creates the chart
    CategoryDataset data = createDataset();
    JFreeChart chart = ChartFactory.createBarChart(title, "Categorie", "Absolute Value", data);
    CategoryPlot plot = chart.getCategoryPlot();
    BarRenderer renderer = (BarRenderer) plot.getRenderer();
    renderer.setBaseSeriesVisible(true);
    return new ChartPanel(chart);
  }

  private CategoryDataset createDataset() {

    // row keys...
    final String plan = Constants.KEY_PLAN;
    final String actual = Constants.KEY_ACTUAL;

    // column keys...
    final String sizeLoC = Constants.KEY_PROGRAM_SIZE + "(number of lines)";
    final String timeInPhase = Constants.KEY_TIME_IN_PHASE;
    final String defectsInjected = Constants.KEY_DEFECTS_INJECTED + "(number of defects)";
    final String defectsRemoved = Constants.KEY_DEFECTS_REMOVED + "(number of defects)";

    // create the dataset...
    final DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    // Plan Values
    dataset.addValue(pps.get(Constants.KEY_PROGRAM_SIZE_IDX, Constants.KEY_SIZE_TOTAL_LOC, Constants.KEY_PLAN), plan,
        sizeLoC);
    dataset.addValue(pps.get(Constants.KEY_TIME_IN_PHASE_IDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_PLAN), plan,
        timeInPhase);
    dataset.addValue(pps.get(Constants.KEY_DEFECTS_INJECTED_IDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_PLAN), plan,
        defectsInjected);
    dataset.addValue(pps.get(Constants.KEY_DEFECTS_REMOVED_IDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_PLAN), plan,
        defectsRemoved);

    // Actual Values
    dataset.addValue(pps.get(Constants.KEY_PROGRAM_SIZE_IDX, Constants.KEY_SIZE_TOTAL_LOC, Constants.KEY_ACTUAL),
        actual, sizeLoC);
    dataset.addValue(
        MathCalc.getInstance().fromSecondToMinute(
            (long) pps.get(Constants.KEY_TIME_IN_PHASE_IDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_ACTUAL)),
        actual, timeInPhase);
    dataset.addValue(pps.get(Constants.KEY_DEFECTS_INJECTED_IDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_ACTUAL),
        actual, defectsInjected);
    dataset.addValue(pps.get(Constants.KEY_DEFECTS_REMOVED_IDX, Constants.KEY_PHASE_TOTAL, Constants.KEY_ACTUAL),
        actual, defectsRemoved);

    return dataset;

  }


}
