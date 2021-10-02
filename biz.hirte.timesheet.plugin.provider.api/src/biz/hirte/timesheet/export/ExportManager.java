package biz.hirte.timesheet.export;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import biz.hirte.timesheet.model.IPeriod;
import biz.hirte.timesheet.model.IProject;

public class ExportManager {

	public <T extends IPeriod> Map<LocalDate, ExportPeriodRow> toExportPeriodRows(IProject project, List<T> periods) {

		List<ExportPeriodRow> exports = periods.stream()
				.map(p -> new ExportPeriodRow(p.getDay(), p.getBegin(), p.getEnd(), Duration.between(p.getBegin(), p.getEnd()), p.getBreakDuration(),
						p.getComment())) //
				.collect(Collectors.toList());

		Map<LocalDate, ExportPeriodRow> rows = new HashMap<>();

		for (ExportPeriodRow eprL : exports) {
			LocalDate k = eprL.getDay();
			ExportPeriodRow cr = rows.get(k);

			ExportPeriodRow earlierEPR = cr == null ? eprL : (cr.getBegin().isBefore(eprL.getBegin()) ? cr : eprL);
			ExportPeriodRow laterEPR = earlierEPR.equals(eprL) ? cr : eprL;

			if (laterEPR != null) {

				Duration effectiveDuration = laterEPR.getEffectiveDuration().plus(earlierEPR.getEffectiveDuration());
				Duration breakDuration = Duration.between(earlierEPR.getEnd(), laterEPR.getBegin()).plus(earlierEPR.getBreakTime())
						.plus(laterEPR.getBreakTime());
				String description = laterEPR.getDescription().equals(earlierEPR.getDescription()) ? laterEPR.getDescription()
						: new StringBuilder(cr.getDescription()).append(" + ").append(eprL.getDescription()).toString();
				ExportPeriodRow epr = new ExportPeriodRow(k, earlierEPR.getBegin(), laterEPR.getEnd(), effectiveDuration, breakDuration, description);
				rows.put(k, epr);
			} else {
				rows.put(k, eprL);
			}
		}

		return rows;
	}

}
