package biz.hirte.timesheets.exporter;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import biz.hirte.timesheet.model.IPeriod;
import biz.hirte.timesheet.model.IProject;
import biz.hirte.timesheet.model.PeriodComparator;
import biz.hirte.timesheet.service.ITimesheetExportService;

public class BundesbankSHSDB4 implements ITimesheetExportService {

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
				exportFile = File.createTempFile("pass_shsdb4", ".xlsx", directory);

				/* load xlsx timesheet template */
				XSSFWorkbook wb = new XSSFWorkbook(getClass().getResourceAsStream("/Stundenzettel_SHSDB4.xlsx"));
				Sheet sheet = wb.getSheetAt(0);

				/* first row in the sheet (rows are zero based) */
				int rowIdx = 2;

				LocalDate now = start;

				/* list index pointing the current period in the list of periods. */
				int lidx = 0;
				long minutes = 0L;
				while (!now.isAfter(end)) {

					if (lidx < periods.size()) {
						IPeriod period = periods.get(lidx);
						if (now.isEqual(period.getDay())) {
							minutes += period.getDuration().toMinutes();
							fillRow(SDF, SDF_TIME, sheet, rowIdx, period);
							lidx++;
						}
					}
					rowIdx++;

					/* Schleifenzähler um 1 erhöhen */
					now = now.plusDays(1);
				}

				sheet.getRow(33).getCell(5).setCellValue(String.format("%4.2f", minutes / 60D));

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

	private void fillRow(DateTimeFormatter SDF, DateTimeFormatter SDF_TIME, Sheet sheet, int rowIdx, IPeriod period) {
		Row row = sheet.getRow(rowIdx);

		Cell cell = row.getCell(1, Row.CREATE_NULL_AS_BLANK);
		CellStyle cellStyle = cell.getCellStyle();
		setupCell(cellStyle);
		cell.setCellValue(SDF.format(period.getDay()));
		cell.setCellStyle(cellStyle);

		cell = row.getCell(2, Row.CREATE_NULL_AS_BLANK);
		cellStyle = cell.getCellStyle();
		cell.setCellValue(period.getComment());
		cell.setCellStyle(cellStyle);

		cell = row.getCell(3, Row.CREATE_NULL_AS_BLANK);
		cellStyle = cell.getCellStyle();
		cell.setCellValue(SDF_TIME.format(period.getBegin()));
		cell.setCellStyle(cellStyle);

		cell = row.getCell(4, Row.CREATE_NULL_AS_BLANK);
		cellStyle = cell.getCellStyle();
		cellStyle.setBorderBottom(CellStyle.BORDER_THIN);
		cell.setCellValue(SDF_TIME.format(period.getEnd()));
		cell.setCellStyle(cellStyle);

		long minutes = period.getDuration().toMinutes();
		row.getCell(5, Row.CREATE_NULL_AS_BLANK).setCellValue(String.format("%4.2f", minutes / 60D));
	}

	private void setupCell(CellStyle cs) {
		cs.setBorderBottom(CellStyle.BORDER_MEDIUM);
		cs.setBorderLeft(CellStyle.BORDER_MEDIUM);
		cs.setBorderRight(CellStyle.BORDER_MEDIUM);
		cs.setBorderTop(CellStyle.BORDER_MEDIUM);
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
