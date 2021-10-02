package biz.hirte.timesheet.model.util;

import java.util.List;

import biz.hirte.timesheet.model.IPeriod;
import biz.hirte.timesheet.model.IProject;

public class Durations {

	public static final double calculateOverallProjectsDuration(List<? extends IProject> projects) {
		double sum = 0D;

		for (IProject project : projects) {
			sum += calculateOverallPeriodsDuration(project.getPeriods());
		}

		return sum;
	}

	public static final double calculateOverallPeriodsDuration(List<? extends IPeriod> periods) {

		double sum = 0D;
		for (IPeriod period : periods) {
			sum += period.getDuration().toMinutes();
		}
		return sum;
	}

	public static final double calculatePeriodDuration(IPeriod period) {
		return period.getDuration().toMinutes();
	}

	public static final int calculatePeriodDuration(int startHour, int startMinutes, int endHour, int endMinutes) {

		if (startHour <= endHour) {
			if (startMinutes < endMinutes) {
				return ((endHour - startHour) * 60) + endMinutes - startMinutes;
			} else {
				return ((60 - startMinutes) + endMinutes) + (endHour - (startHour + 1)) * 60;
			}
		}

		return 0;
	}
}
