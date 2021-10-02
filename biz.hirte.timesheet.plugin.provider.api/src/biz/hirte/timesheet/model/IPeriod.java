package biz.hirte.timesheet.model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

/**
 * Interface that a client model has to implement.
 * 
 * @author hirte
 *
 */
public interface IPeriod {

	/** Getter / Setter for the DAY */
	public LocalDate getDay();

	// public void setDay(LocalDate day);

	/** Getter / Setter for the BEGIN */
	public LocalTime getBegin();

	// public void setBegin(LocalTime begin);

	/** Getter / Setter for the END */
	public LocalTime getEnd();

	// public void setEnd(LocalTime end);

	/** Getter / Setter for the COMMENT */
	public String getComment();

	// public void setComment(String comment);

	/** Getter / Setter for the UUID */
	public UUID getUuid();

	// public void setUuid(UUID uuid);

	/** Getter / Setter for the PROJECT */
	public IProject getProject();

	// public void setProject(IProject project);

	/** Getter / Setter for the DURATION */
	public Duration getDuration();

	/** Getter / Setter for the Break DURATION */
	public Duration getBreakDuration();

	@Override
	public String toString();

	@Override
	int hashCode();

	@Override
	boolean equals(Object obj);

	/** Creates a flat copy of this IPeriod object */
	IPeriod copy();

	<T extends IProperty> List<T> getProperties();
}
