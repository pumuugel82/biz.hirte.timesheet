package biz.hirte.timesheet.export;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

public class ExportPeriodRow implements Comparable<ExportPeriodRow> {

	private final LocalDate	day;
	private final LocalTime	begin;
	private final LocalTime	end;
	private final Duration	effectiveDuration;
	private final Duration	breakTime;
	private final String	description;

	public ExportPeriodRow(LocalDate day, LocalTime begin, LocalTime end, Duration effectiveDuration, Duration breakTime, String description) {
		super();
		Objects.requireNonNull(day, "Day must not be null.");
		Objects.requireNonNull(begin, "Begin must not be null.");
		Objects.requireNonNull(end, "End must not be null.");

		this.day = day;
		this.begin = begin;
		this.end = end;
		this.effectiveDuration = effectiveDuration;
		this.breakTime = breakTime;
		this.description = description;
	}

	public LocalDate getDay() {
		return day;
	}

	public LocalTime getBegin() {
		return begin;
	}

	public LocalTime getEnd() {
		return end;
	}

	public Duration getEffectiveDuration() {
		return effectiveDuration;
	}

	public Duration getBreakTime() {
		return breakTime;
	}

	public String getDescription() {
		return description;
	}

	@Override
	public int compareTo(ExportPeriodRow o) {
		if (o == null) return -1;

		int ret = this.getDay().isBefore(o.getDay()) ? 0 : this.getDay().isAfter(o.getDay()) ? -1 : 0;
		if (ret == 0) {
			ret = this.getBegin().isBefore(o.getBegin()) ? 0 : this.getBegin().isAfter(o.getBegin()) ? -1 : 0;
		}
		return ret;
	}

	@Override
	public String toString() {
		return "ExportPeriodRow [day=" + day + ", begin=" + begin + ", end=" + end + ", effectiveDuration=" + effectiveDuration + ", breakTime=" + breakTime
				+ ", description=" + description + "]";
	}

}
