package biz.hirte.timesheet.provider.xml.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import biz.hirte.timesheet.model.DateFilter;
import biz.hirte.timesheet.model.IPeriod;
import biz.hirte.timesheet.model.IProject;
import biz.hirte.timesheet.model.IProperty;
import biz.hirte.timesheet.model.IPropertyDescriptor;
import biz.hirte.timesheet.model.PeriodComparator;
import biz.hirte.timesheet.model.ValueTypeName;
import biz.hirte.timesheet.provider.xml.model.XPeriod;
import biz.hirte.timesheet.provider.xml.model.XProject;
import biz.hirte.timesheet.provider.xml.model.XPropertyDescriptor;
import biz.hirte.timesheet.provider.xml.model.XTimesheets;
import biz.hirte.timesheet.service.ITimesheetProviderService;

public class TimesheetServiceImpl implements ITimesheetProviderService {

	/**
	 * Comparator that is used to order the Periods of a Projects in descanding
	 * order right after insertion.
	 */
	private static final PeriodComparator PERIOD_COMPARATOR = new PeriodComparator(true);

	/**
	 * The Persister that cares about loading, caching and permanently write
	 * back of the the timesheet to the XML storage.
	 */

	public TimesheetServiceImpl() {}

	@Override
	public IPeriod addPeriod(IProject iproject, LocalDate localDate, LocalTime begin, LocalTime end, int breakInMinutes, String description,
			List<IProperty> properties) {

		if (iproject == null) {
			throw new IllegalArgumentException("Projet must not be null.");
		}

		XTimesheets timesheets = TimesheetPersister.getInstance().getTimesheets();

		XProject project = timesheets.findProject((XProject) iproject);

		if (project == null) {
			throw new IllegalStateException("Project could not be found in the repository.");
		}

		XPeriod xperiod = new XPeriod(localDate, begin, end, breakInMinutes, description);
		project.add(xperiod);

		Collections.sort(project.getPeriods(), PERIOD_COMPARATOR);

		return xperiod;
	}

	@Override
	public List<IPeriod> getAllPeriods(IProject project, List<DateFilter> dateFilters) {

		XTimesheets timesheets = TimesheetPersister.getInstance().getTimesheets();

		List<IPeriod> ret = new ArrayList<IPeriod>();

		for (DateFilter dateFilter : dateFilters) {

			LocalDate lowerBound = dateFilter.getFrom().minusDays(1);
			LocalDate upperBound = dateFilter.getUntil().plusDays(1);

			if (timesheets.contains(project)) {

				ret.addAll(project.getPeriods().stream().filter(p -> p.getDay().isAfter(lowerBound) && p.getDay().isBefore(upperBound))
						.collect(Collectors.toList()));

			} else {

				throw new IllegalStateException("Project is unknown.");

			}
		}

		return ret.stream().distinct().collect(Collectors.toList());
	}

	@Override
	public List<? extends IProject> getAllProjects() {
		XTimesheets timesheets = TimesheetPersister.getInstance().getTimesheets();
		return timesheets.getProjects();
	}

	@Override
	public IProject addProject(String name, String description) {

		XTimesheets timesheets = TimesheetPersister.getInstance().getTimesheets();

		XProject p = new XProject(name, description);

		if (!timesheets.contains(p)) {
			timesheets.add(p);
		}

		return p;
	}

	@Override
	public void removePeriod(IProject project, IPeriod period) {

		if (project == null || period == null) {
			throw new IllegalArgumentException("Project and period must not be null.");
		}

		XTimesheets timesheets = TimesheetPersister.getInstance().getTimesheets();

		XProject xproject = timesheets.findProject((XProject) project);
		if (xproject != null) {

			if (!xproject.remove(period)) {
				throw new IllegalStateException("Cannot delete period. Project doesn't contain the period.");
			}

			return;
		}

		throw new IllegalStateException("Cannot delete period. Project cannot be found.");

	}

	@Override
	public IPeriod updatePeriod(IPeriod original, LocalDate day, LocalTime begin, LocalTime end, int breakInMinutes, String description,
			List<IProperty> properties) {

		XPeriod xp = (XPeriod) original;
		xp.setComment(description);
		xp.setBegin(begin);
		xp.setEnd(end);
		xp.setDay(day);
		xp.setBreakInMinutes(breakInMinutes);

		return xp;
	}

	@Override
	public void removeProject(IProject project) {

		if (project == null) {
			throw new IllegalArgumentException("Project must not be null.");
		}

		XTimesheets timesheets = TimesheetPersister.getInstance().getTimesheets();

		if (!timesheets.remove(project)) {
			throw new IllegalStateException("Cannot delete project. Not found.");
		}

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<IProject> getProjectsPeriods(List<DateFilter> dateFilters) {

		if (dateFilters == null) {
			throw new IllegalArgumentException("DateFilters must not be null.");
		}

		List<IProject> ret = new ArrayList<IProject>();

		List<? extends IProject> xprojects = TimesheetPersister.getInstance().getTimesheets().getProjects();

		for (IProject xProject : xprojects) {

			/*
			 * Make a flat copy
			 */
			IProject xcopy = xProject.copy();

			List<IPeriod> allPeriods = this.getAllPeriods(xProject, dateFilters);

			/*
			 * Only if there are period entries for the peroject, add it to the
			 * result
			 */
			if (allPeriods.size() > 0) {
				((XProject) xcopy).addAll((Collection<? extends XPeriod>) allPeriods);
				ret.add(xcopy);
			}
		}

		return ret;
	}

	@Override
	public IProject updateProject(IProject original, String name, String description) {

		XTimesheets timesheets = TimesheetPersister.getInstance().getTimesheets();

		XProject xp = timesheets.findProject((XProject) original);
		if (xp == null) {
			throw new IllegalStateException("Project could not be found in the repository.");
		}

		xp.setDescription(description);
		xp.setName(name);

		return xp;
	}

	@Override
	public void addPropertyDescriptor(String name, ValueTypeName valueTypeName) {
		XTimesheets timesheets = TimesheetPersister.getInstance().getTimesheets();
		XPropertyDescriptor epd = new XPropertyDescriptor();
		epd.setKey(name);
		epd.setValueTypeName(valueTypeName);
		timesheets.add(epd);
	}

	@Override
	public IPropertyDescriptor createEmptyPropertyDescriptor() {
		return new XPropertyDescriptor();
	}

	@Override
	public void updatePropertyDescriptor(IPropertyDescriptor descriptor, String key, ValueTypeName type) {
		XPropertyDescriptor xdesc = (XPropertyDescriptor) descriptor;
		xdesc.setKey(key);
		xdesc.setValueTypeName(type);

		XTimesheets timesheets = TimesheetPersister.getInstance().getTimesheets();

		Optional<XPropertyDescriptor> oneDescriptor = timesheets.getDescriptors().stream().filter(desc -> desc == xdesc).findFirst();
		if (!oneDescriptor.isPresent()) {
			timesheets.add(xdesc);
		}
	}

	@Override
	public List<? extends IPropertyDescriptor> getAllPropertyDescriptors() {
		XTimesheets timesheets = TimesheetPersister.getInstance().getTimesheets();
		return timesheets.getDescriptors();
	}

}
