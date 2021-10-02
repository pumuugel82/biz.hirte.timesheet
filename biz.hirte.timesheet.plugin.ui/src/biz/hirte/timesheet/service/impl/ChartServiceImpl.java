package biz.hirte.timesheet.service.impl;

import java.util.List;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.general.PieDataset;

import biz.hirte.timesheet.model.IProject;
import biz.hirte.timesheet.model.util.Durations;
import biz.hirte.timesheet.service.IChartService;

/**
 * Implementation of {@link IChartService}.
 * 
 * @author hirte
 *
 */
public class ChartServiceImpl implements IChartService {

	@Override
	public JFreeChart createChart(List<? extends IProject> projects) {

		PieDataset dataset = this.createDataset(projects);

		final JFreeChart chart = ChartFactory.createPieChart(null, // chart-title
				dataset, // data
				true, // include legend
				true, false);

		chart.setBorderVisible(false);
		chart.setAntiAlias(true);

		final PiePlot plot = (PiePlot) chart.getPlot();
		plot.setInteriorGap(0.0);
		plot.setBackgroundAlpha(0F);
		plot.setLabelGenerator(null);
		return chart;

	}

	public PieDataset createDataset(List<? extends IProject> projects) {

		final DefaultPieDataset dataset = new DefaultPieDataset();

		for (IProject project : projects) {
			Double amount = Durations.calculateOverallPeriodsDuration(project.getPeriods());
			dataset.setValue(project.getName(), amount);
		}

		return dataset;
	}

}
