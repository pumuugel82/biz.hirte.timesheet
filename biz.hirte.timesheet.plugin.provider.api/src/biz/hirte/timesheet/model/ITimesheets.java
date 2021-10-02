package biz.hirte.timesheet.model;

import java.util.List;

/**
 * Interface that client model has to implement.
 * 
 * @author hirte
 *
 */
public interface ITimesheets {

	List<? extends IProject> getProjects();

	List<? extends IPropertyDescriptor> getDescriptors();

}