package biz.hirte.timesheet.provider.xml.service;

import biz.hirte.timesheet.provider.xml.model.XProject;
import biz.hirte.timesheet.provider.xml.model.XTimesheets;

public class TimesheetPersisterTest {

	public static void main(String[] args) throws Exception {
		TimesheetPersister instance = TimesheetPersister.getInstance("test.xml");
		XTimesheets value = instance.getTimesheets();

		XProject p = new XProject("Test", "Beschreibung");
		value.add(p);
		instance.saveDataStore();
	}
}
