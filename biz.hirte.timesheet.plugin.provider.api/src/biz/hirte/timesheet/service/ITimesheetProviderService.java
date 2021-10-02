package biz.hirte.timesheet.service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import biz.hirte.timesheet.model.DateFilter;
import biz.hirte.timesheet.model.IPeriod;
import biz.hirte.timesheet.model.IProject;
import biz.hirte.timesheet.model.IProperty;
import biz.hirte.timesheet.model.IPropertyDescriptor;
import biz.hirte.timesheet.model.ValueTypeName;

/**
 * Interface that contributor plugins have to implement when providing a
 * timesheet service.
 * 
 * @author hirte
 *
 */
public interface ITimesheetProviderService {

	/**
	 * Adds a new timesheet entry to the project specified via the projectName.
	 * ProjectName may be a name for a not existing project. In this case the
	 * implementation may either create a new project in the persistent store or
	 * throw a RuntimeException dynamic creation of projects is not supported.
	 * 
	 * @param projectName
	 *            name of the project / may be a new name
	 * @param day
	 *            Day of the time entry
	 * @param begin
	 *            time of the begin
	 * @param end
	 *            time of the end
	 * @param description
	 *            note for the time entry
	 * @return the created period entry
	 */
	IPeriod addPeriod(IProject project, LocalDate day, LocalTime begin, LocalTime end, int breakInMinutes, String description, List<IProperty> properties);

	/**
	 * Tries to remove the period from the project.
	 * 
	 * @param p
	 *            project that contains the period
	 * @param period
	 *            the period to be removed from the project
	 */
	void removePeriod(IProject project, IPeriod period);

	/**
	 * Updates the original IPeriod with the given prameters.
	 * 
	 * @param original
	 *            may not be null.
	 * @param day
	 * @param begin
	 * @param end
	 * @param description
	 * @return
	 */
	IPeriod updatePeriod(IPeriod original, LocalDate day, LocalTime begin, LocalTime end, int breakInMinutes, String description, List<IProperty> properties);

	/**
	 * Returns a List of all periods from the given project that are in between
	 * of begin and end.
	 * 
	 * @param project
	 * @param dateFilter
	 * @return
	 */
	List<? extends IPeriod> getAllPeriods(IProject project, List<DateFilter> dateFilter);

	/**
	 * Returns a list of all projects and all periods for each project.
	 * 
	 * @return
	 */
	List<? extends IProject> getAllProjects();

	/**
	 * Returns a list of projects that have periods between begin and end. Each
	 * project then contains only periods that are between begin and end.
	 * 
	 * @param dateFilter
	 * @return
	 */
	List<? extends IProject> getProjectsPeriods(List<DateFilter> dateFilter);

	/**
	 * Adds a project to the persistent store.
	 * 
	 * @param p
	 */
	IProject addProject(String name, String description);

	/**
	 * 
	 * @param original
	 * @param name
	 * @param description
	 * @return
	 */
	IProject updateProject(IProject original, String name, String description);

	/**
	 * Removes a project from the persistent store.
	 * 
	 * @param p
	 */
	void removeProject(IProject p);

	/**
	 * Adds a PropertyDescriptor to the list of known PropertyDescriptors in ITimesheets.
	 * 
	 * @param name
	 * @param valueTypeName
	 */
	void addPropertyDescriptor(String name, ValueTypeName valueTypeName);

	IPropertyDescriptor createEmptyPropertyDescriptor();

	void updatePropertyDescriptor(IPropertyDescriptor descriptor, String key, ValueTypeName type);

	List<? extends IPropertyDescriptor> getAllPropertyDescriptors();
}
