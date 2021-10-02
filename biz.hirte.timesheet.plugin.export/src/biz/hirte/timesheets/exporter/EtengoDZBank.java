package biz.hirte.timesheets.exporter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import biz.hirte.timesheet.model.IPeriod;
import biz.hirte.timesheet.model.IProject;
import biz.hirte.timesheet.model.PeriodComparator;
import biz.hirte.timesheet.service.ITimesheetExportService;

public class EtengoDZBank implements ITimesheetExportService {

	@Override
	public Map<IProject, String> export(Map<IProject, List<IPeriod>> projectPeriods, LocalDate start, LocalDate end) {

		Map<IProject, String> ret = new HashMap<IProject, String>();

		File directory = new File(System.getProperty("user.home") + File.separator + "eclipse.timesheet");

		try {
			prepareDataLocation(directory);
		}
		catch (Exception e1) {
			return null;
		}

		File exportFile = null;

		DateTimeFormatter SDF = DateTimeFormatter.ofPattern("dd.MM.yyyy");
		DateTimeFormatter SDF_TIME = DateTimeFormatter.ofPattern("HH:mm");

		for (Map.Entry<IProject, List<IPeriod>> entry : projectPeriods.entrySet()) {

			List<? extends IPeriod> periods = entry.getValue();
			periods.sort(new PeriodComparator(false));

			try {

				/* create temporary file */
				exportFile = File.createTempFile("etengo_dz", ".xlsx", directory);

				/* load xlsx timesheet template */
				XSSFWorkbook wb = new XSSFWorkbook(getClass().getResourceAsStream("/Zeiterfassung_Portal_DZBank.xlsx"));
				Sheet sheet = wb.getSheetAt(0);

				/* first row in the sheet (rows are zero based) */
				int rowIdx = 1;

				LocalDate now = start;

				/* list index pointing the current period in the list of periods. */
				int lidx = 0;

				while (!now.isAfter(end)) {

					if (lidx < periods.size()) {
						IPeriod period = periods.get(lidx);
						if (now.isEqual(period.getDay())) {
							fillRow(SDF, SDF_TIME, sheet, rowIdx++, period);
							lidx++;
						} else {
							fillRow(SDF, sheet, rowIdx++, now);
						}
					} else {
						fillRow(SDF, sheet, rowIdx++, now);
					}

					/* Schleifenzähler um 1 erhöhen */
					now = now.plusDays(1);
				}

				wb.write(new FileOutputStream(exportFile));
				wb.close();
			}
			catch (IOException e) {
				e.printStackTrace();
			}

			if (exportFile != null) {
				ret.put(entry.getKey(), exportFile.getAbsolutePath());
			}

		}

		return ret;
	}

	private void fillRow(DateTimeFormatter sDF, Sheet sheet, int rowIdx, LocalDate now) {
		Row row = sheet.createRow(rowIdx);
		row.createCell(0).setCellValue(sDF.format(now));
		row.createCell(1).setCellValue("");
		row.createCell(2).setCellValue("");
		row.createCell(3).setCellValue("");

	}

	private void fillRow(DateTimeFormatter SDF, DateTimeFormatter SDF_TIME, Sheet sheet, int rowIdx, IPeriod period) {
		Row row = sheet.createRow(rowIdx);
		row.createCell(0).setCellValue(SDF.format(period.getDay()));
		row.createCell(1).setCellValue(SDF_TIME.format(period.getBegin()));
		row.createCell(2).setCellValue(SDF_TIME.format(period.getEnd()));
		row.createCell(3).setCellValue(period.getComment());
	}

	private void prepareDataLocation(File directory) throws Exception {

		if (directory.exists() && directory.isDirectory()) {

			return;

		} else {

			if (directory.mkdir()) {
				return;
			}

		}

	}

}
