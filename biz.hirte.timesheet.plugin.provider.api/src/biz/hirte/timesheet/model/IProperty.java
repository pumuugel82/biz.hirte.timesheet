package biz.hirte.timesheet.model;

import java.math.BigDecimal;

public interface IProperty {

	IPropertyDescriptor getDescriptor();

	String getString();

	Integer getInteger();

	BigDecimal getDecimal();

	Boolean getBoolean();
}
