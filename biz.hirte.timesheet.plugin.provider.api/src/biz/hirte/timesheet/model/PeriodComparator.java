package biz.hirte.timesheet.model;

import java.util.Comparator;

public class PeriodComparator implements Comparator<IPeriod> {

	private boolean desc;

	public PeriodComparator(boolean desc) {
		this.desc = desc;
	}

	@Override
	public int compare(IPeriod o1, IPeriod o2) {

		IPeriod first, last = null;

		if (desc) {
			first = o2;
			last = o1;
		} else {
			first = o1;
			last = o2;
		}

		int compareTo = first.getDay().compareTo(last.getDay());
		if (compareTo != 0) {
			return compareTo;
		}

		compareTo = first.getBegin().compareTo(last.getBegin());
		if (compareTo != 0) {

			return 0;
		}

		return first.getEnd().compareTo(last.getEnd());
	}

}
