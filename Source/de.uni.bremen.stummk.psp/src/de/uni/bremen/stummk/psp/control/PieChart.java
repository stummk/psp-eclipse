package de.uni.bremen.stummk.psp.control;

import java.awt.BorderLayout;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.PieSectionLabelGenerator;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot3D;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.Rotation;

import de.uni.bremen.stummk.psp.calculation.Filter;
import de.uni.bremen.stummk.psp.calculation.MathCalc;
import de.uni.bremen.stummk.psp.data.Phase;
import de.uni.bremen.stummk.psp.data.ProjectPlanSummary;
import de.uni.bremen.stummk.psp.utility.Constants;

/**
 * Represents a pie chart for the distribution of values per phase
 * 
 * @author Konstantin
 *
 */
public class PieChart {
  private String title = "";
  private int tableIDX;
  private ChartPanel chartPanel;
  private ProjectPlanSummary pps;

  /**
   * Constructor
   * 
   * @param pps the {@link ProjectPlanSummary} which values will be used in the chart
   * @param tableIDX the section of the {@link ProjectPlanSummary}, which data will be used, like
   *        <br>
   *        {@link Constants.KEY_TIME_IN_PHASE_IDX} <br>
   *        {@link Constants.KEY_DEFECTS_INJECTED_IDX} <br>
   *        {@link Constants.KEY_DEFECTS_REMOVED_IDX}
   * @param title the title of the chart
   */
  public PieChart(ProjectPlanSummary pps, int tableIDX, String title) {
    this.tableIDX = tableIDX;
    this.title = title;
    this.pps = pps;

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
    DefaultPieDataset data = createDataset();
    JFreeChart chart = ChartFactory.createPieChart3D(title, data, true, true, false);

    final PiePlot3D plot = (PiePlot3D) chart.getPlot();
    plot.setStartAngle(290);
    plot.setDirection(Rotation.CLOCKWISE);
    plot.setForegroundAlpha(0.5f);
    plot.setNoDataMessage("No data to display");

    PieSectionLabelGenerator labelGenerator = new StandardPieSectionLabelGenerator("{0} = {2}");
    plot.setLabelGenerator(labelGenerator);

    return new ChartPanel(chart);
  }

  private DefaultPieDataset createDataset() {
    // create the dataSet
    Map<String, Double> map = new HashMap<>();
    double plan = this.pps.get(tableIDX, Constants.KEY_PHASE_PLANNING, Constants.KEY_ACTUAL);
    double design = this.pps.get(tableIDX, Constants.KEY_PHASE_DESIGN, Constants.KEY_ACTUAL);
    double designReview = this.pps.get(tableIDX, Constants.KEY_PHASE_DESIGN_REVIEW, Constants.KEY_ACTUAL);
    double code = this.pps.get(tableIDX, Constants.KEY_PHASE_CODE, Constants.KEY_ACTUAL);
    double codeReview = this.pps.get(tableIDX, Constants.KEY_PHASE_CODE_REVIEW, Constants.KEY_ACTUAL);
    double compile = this.pps.get(tableIDX, Constants.KEY_PHASE_COMPILE, Constants.KEY_ACTUAL);
    double test = this.pps.get(tableIDX, Constants.KEY_PHASE_TEST, Constants.KEY_ACTUAL);
    double postmortem = this.pps.get(tableIDX, Constants.KEY_PHASE_POSTMORTEM, Constants.KEY_ACTUAL);

    if (tableIDX == Constants.KEY_TIME_IN_PHASE_IDX) {
      plan = MathCalc.getInstance().fromSecondToMinute((long) plan);
      design = MathCalc.getInstance().fromSecondToMinute((long) design);
      designReview = MathCalc.getInstance().fromSecondToMinute((long) designReview);
      code = MathCalc.getInstance().fromSecondToMinute((long) code);
      codeReview = MathCalc.getInstance().fromSecondToMinute((long) codeReview);
      compile = MathCalc.getInstance().fromSecondToMinute((long) compile);
      test = MathCalc.getInstance().fromSecondToMinute((long) test);
      postmortem = MathCalc.getInstance().fromSecondToMinute((long) postmortem);
    }

    map.put(Constants.KEY_PHASE_PLANNING, new Double(MathCalc.getInstance().round(plan)));
    map.put(Constants.KEY_PHASE_DESIGN, new Double(MathCalc.getInstance().round(design)));
    map.put(Constants.KEY_PHASE_DESIGN_REVIEW, new Double(MathCalc.getInstance().round(designReview)));
    map.put(Constants.KEY_PHASE_CODE, new Double(MathCalc.getInstance().round(code)));
    map.put(Constants.KEY_PHASE_CODE_REVIEW, new Double(MathCalc.getInstance().round(codeReview)));
    map.put(Constants.KEY_PHASE_COMPILE, new Double(MathCalc.getInstance().round(compile)));
    map.put(Constants.KEY_PHASE_TEST, new Double(MathCalc.getInstance().round(test)));
    map.put(Constants.KEY_PHASE_POSTMORTEM, new Double(MathCalc.getInstance().round(postmortem)));

    final DefaultPieDataset dataset = new DefaultPieDataset();
    setData(dataset, map);

    return dataset;
  }

  private void setData(DefaultPieDataset dataset, Map<String, Double> map) {
    List<String> filteredPhases = Filter.filterPhases(Phase.getValues());

    if (filteredPhases.contains(Constants.KEY_PHASE_PLANNING)) {
      dataset.setValue(Constants.KEY_PHASE_PLANNING, map.get(Constants.KEY_PHASE_PLANNING));
    }

    if (filteredPhases.contains(Constants.KEY_PHASE_DESIGN)) {
      dataset.setValue(Constants.KEY_PHASE_DESIGN, map.get(Constants.KEY_PHASE_DESIGN));
    }

    if (filteredPhases.contains(Constants.KEY_PHASE_DESIGN_REVIEW)) {
      dataset.setValue(Constants.KEY_PHASE_DESIGN_REVIEW, map.get(Constants.KEY_PHASE_DESIGN_REVIEW));
    }

    if (filteredPhases.contains(Constants.KEY_PHASE_CODE)) {
      dataset.setValue(Constants.KEY_PHASE_CODE, map.get(Constants.KEY_PHASE_CODE));
    }

    if (filteredPhases.contains(Constants.KEY_PHASE_CODE_REVIEW)) {
      dataset.setValue(Constants.KEY_PHASE_CODE_REVIEW, map.get(Constants.KEY_PHASE_CODE_REVIEW));
    }

    if (filteredPhases.contains(Constants.KEY_PHASE_COMPILE)) {
      dataset.setValue(Constants.KEY_PHASE_COMPILE, map.get(Constants.KEY_PHASE_COMPILE));
    }

    if (filteredPhases.contains(Constants.KEY_PHASE_TEST)) {
      dataset.setValue(Constants.KEY_PHASE_TEST, map.get(Constants.KEY_PHASE_TEST));
    }

    if (filteredPhases.contains(Constants.KEY_PHASE_POSTMORTEM)) {
      dataset.setValue(Constants.KEY_PHASE_POSTMORTEM, map.get(Constants.KEY_PHASE_POSTMORTEM));
    }
  }
}
