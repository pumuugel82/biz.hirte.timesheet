package biz.hirte.timesheet.provider.xml.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlID;
import javax.xml.bind.annotation.XmlType;

import biz.hirte.timesheet.model.IPropertyDescriptor;
import biz.hirte.timesheet.model.ValueTypeName;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "propertyDescriptor")
public class XPropertyDescriptor implements IPropertyDescriptor {

	@XmlID
	@XmlAttribute(name = "key")
	private String			key				= "";

	@XmlAttribute(name = "valueType")
	private ValueTypeName	valueTypeName	= ValueTypeName.STRING;

	@Override
	public ValueTypeName getValueTypeName() {
		return valueTypeName;
	}

	@Override
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public void setValueTypeName(ValueTypeName valueTypeName) {
		this.valueTypeName = valueTypeName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((key == null) ? 0 : key.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		XPropertyDescriptor other = (XPropertyDescriptor) obj;
		if (key == null) {
			if (other.key != null) return false;
		} else if (!key.equals(other.key)) return false;
		return true;
	}

}
