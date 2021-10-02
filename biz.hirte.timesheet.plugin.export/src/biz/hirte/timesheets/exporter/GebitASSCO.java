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

import biz.hirte.timesheet.export.ExportManager;
import biz.hirte.timesheet.export.ExportPeriodRow;
import biz.hirte.timesheet.model.IPeriod;
import biz.hirte.timesheet.model.IProject;
import biz.hirte.timesheet.service.ITimesheetExportService;

/**
 * Solcom/Gebit Exporter.
 * 
 * @author hirte
 *
 */
public class GebitASSCO implements ITimesheetExportService {

	private static final DateTimeFormatter	SDF				= DateTimeFormatter.ofPattern("dd.MM.yyyy");
	private static final DateTimeFormatter	SDF_TIME		= DateTimeFormatter.ofPattern("HH:mm");
	private static final String				FILE_TEMPLATE	= "/Zeiterfassung_Gebit.xlsx";
	private static final String				TMP_FILE_PREFIX	= "gebit-assco";

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

		writeProjects(projectPeriods, start, end, ret, directory);

		return ret;
	}

	void writeProjects(Map<IProject, List<IPeriod>> projectPeriods, LocalDate start, LocalDate end, Map<IProject, String> ret, File directory) {
		for (Map.Entry<IProject, List<IPeriod>> entry : projectPeriods.entrySet()) {

			writeOneProject(start, end, ret, directory, entry);

		}
	}

	void writeOneProject(LocalDate start, LocalDate end, Map<IProject, String> ret, File directory, Map.Entry<IProject, List<IPeriod>> entry) {

		ExportManager em = new ExportManager();
		Map<LocalDate, ExportPeriodRow> exportPeriodRows = em.toExportPeriodRows(entry.getKey(), entry.getValue());
		System.out.println(exportPeriodRows);

		try {

			/* create temporary file */
			File exportFile = File.createTempFile(TMP_FILE_PREFIX, ".xlsx", directory);

			/* load xlsx timesheet template */
			XSSFWorkbook wb = new XSSFWorkbook(getClass().getResourceAsStream(FILE_TEMPLATE));
			Sheet sheet = wb.getSheetAt(0);

			/* write the head line */
			Row row = sheet.getRow(0);
			Cell cell = row.getCell(0, Row.CREATE_NULL_AS_BLANK);
			cell.setCellValue("Zeitnachweis: " + entry.getKey().getName());

			/* first row in the sheet (rows are zero based) */
			int rowIdx = 2;

			LocalDate now = start;

			long minutes = 0L;
			while (!now.isAfter(end)) {

				ExportPeriodRow epr = exportPeriodRows.get(now);
				if (epr != null) {
					minutes += epr.getEffectiveDuration().toMinutes();
					fillRowWithPeriod(sheet, rowIdx, epr);
				}

				/* Schleifenzähler um 1 erhöhen */
				rowIdx++;
				// Datum um 1 erhöhen
				now = now.plusDays(1);
			}

			// Summe der Minuten schreiben
			sheet.getRow(33).getCell(6).setCellValue(String.format("%4.2f", minutes / 60D));

			wb.write(new FileOutputStream(exportFile));
			wb.close();

			ret.put(entry.getKey(), exportFile.getAbsolutePath());

		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void fillRowWithPeriod(Sheet sheet, int rowIdx, ExportPeriodRow period) {
		Row row = sheet.getRow(rowIdx);

		Cell cell = row.getCell(1, Row.CREATE_NULL_AS_BLANK);
		CellStyle cellStyle = cell.getCellStyle();
		setupCell(cellStyle);
		cell.setCellValue(SDF.format(period.getDay()));
		cell.setCellStyle(cellStyle);

		cell = row.getCell(2, Row.CREATE_NULL_AS_BLANK);
		cellStyle = cell.getCellStyle();
		cell.setCellValue(period.getDescription().toString());
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

		long minutes = period.getBreakTime().toMinutes();
		row.getCell(5, Row.CREATE_NULL_AS_BLANK).setCellValue(String.format("%4.2f", minutes / 60D));

		minutes = period.getEffectiveDuration().toMinutes();
		row.getCell(6, Row.CREATE_NULL_AS_BLANK).setCellValue(String.format("%4.2f", minutes / 60D));
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
