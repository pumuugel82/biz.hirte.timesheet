package biz.hirte.timesheet.model;

import java.time.LocalDate;

/**
 * A DateFilter is used to specify a period with a concrete start and end date.
 * 
 * @author hirte
 *
 */
public class DateFilter {

	private final LocalDate	from;
	private final LocalDate	until;

	public DateFilter(LocalDate from, LocalDate until) {
		this.from = from;
		this.until = until;
	}

	public LocalDate getFrom() {
		return from;
	}

	public LocalDate getUntil() {
		return until;
	}

}
