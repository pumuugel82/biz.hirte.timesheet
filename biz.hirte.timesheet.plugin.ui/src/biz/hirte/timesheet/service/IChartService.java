package biz.hirte.timesheet.service;

import java.util.List;

import org.jfree.chart.JFreeChart;

import biz.hirte.timesheet.model.IProject;

/**
 * IChartService that is able to create JFreeCharts from a List of Projects.
 * Charts can then be rendered as is on any supported media.
 * 
 * @author hirte
 *
 */
public interface IChartService {

	/**
	 * Will create a Pie Chart with ledgend from the given List<Project>.
	 * 
	 * @param projects
	 * @return
	 */
	JFreeChart createChart(List<? extends IProject> projects);
}
