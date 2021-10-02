package biz.hirte.timesheet.events;

import biz.hirte.timesheet.model.IPeriod;

public class PeriodEvent {

	private IPeriod period;

	public PeriodEvent(IPeriod period) {
		super();
		this.period = period;
	}

	public IPeriod getPeriod() {
		return period;
	}

	public void setPeriod(IPeriod period) {
		this.period = period;
	}

}
