package biz.hirte.timesheet.provider.xml.model;

import java.math.BigDecimal;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlIDREF;
import javax.xml.bind.annotation.XmlType;

import biz.hirte.timesheet.model.IProperty;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "property")
public class XProperty implements IProperty {

	@XmlAttribute
	private String				value;

	@XmlIDREF
	@XmlAttribute
	private XPropertyDescriptor	descriptor;

	@Override
	public XPropertyDescriptor getDescriptor() {
		return descriptor;
	}

	@Override
	public String getString() {
		return value;
	}

	@Override
	public Integer getInteger() {
		return Integer.parseInt(value);
	}

	@Override
	public BigDecimal getDecimal() {
		return new BigDecimal(value);
	}

	@Override
	public Boolean getBoolean() {
		return Boolean.valueOf(value);
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setDescriptor(XPropertyDescriptor descriptor) {
		this.descriptor = descriptor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((descriptor == null) ? 0 : descriptor.hashCode());
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		XProperty other = (XProperty) obj;
		if (descriptor == null) {
			if (other.descriptor != null) return false;
		} else if (!descriptor.equals(other.descriptor)) return false;
		if (value == null) {
			if (other.value != null) return false;
		} else if (!value.equals(other.value)) return false;
		return true;
	}

}
