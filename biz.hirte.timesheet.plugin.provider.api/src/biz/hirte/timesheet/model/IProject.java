package biz.hirte.timesheet.model;

import java.util.List;

/**
 * Interface that client model has to implement.
 * 
 * @author hirte
 *
 */
public interface IProject {

	/**
	 * Getter / Setter for NAME
	 */
	String getName();

	// void setName(String name);

	/**
	 * Getter / Setter for DESCRIPTION
	 */
	String getDescription();

	// void setDescription(String description);

	/**
	 * Getter for all Periods.
	 * 
	 * @return
	 */
	List<? extends IPeriod> getPeriods();

	@Override
	int hashCode();

	@Override
	boolean equals(Object obj);

	/**
	 * Will create a flat copy of the original object without the list of
	 * IPeriods.
	 * 
	 * @return
	 */
	IProject copy();

	/**
	 * Will return a list of properties.
	 * @return
	 */
	List<? extends IProperty> getProperties();

	List<? extends IProperty> getDefaultPeriodProperties();
}