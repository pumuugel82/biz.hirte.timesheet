package biz.hirte.timesheet.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import biz.hirte.timesheet.model.IPeriod;
import biz.hirte.timesheet.model.IProject;

/**
 * Definition of the ExportService that contributer plugins have to implement.
 * 
 * @author hirte
 *
 */
public interface ITimesheetExportService {

	/**
	 * Implementations should export the project list into the specified
	 * directory. The returned String is expected to be the filename of the
	 * created export file within the specified directory.
	 * 
	 * @param directory
	 *            the destination directory in which the export should be
	 *            stored.
	 * @param projects
	 *            list of projects with subset of periods that fit in between
	 *            parameters start and end.
	 * @param start
	 *            first day of the report period
	 * @param end
	 *            last day of the report period
	 * @return filename in which the export is stored.
	 */
	Map<IProject, String> export(Map<IProject, List<IPeriod>> projects, LocalDate start, LocalDate end);
}
