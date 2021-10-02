package biz.hirte.timesheet.provider.xml.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

import biz.hirte.timesheet.model.IPeriod;
import biz.hirte.timesheet.model.IProject;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "project")
public class XProject implements IProject {

	@XmlAttribute
	private UUID			uuid;

	@XmlAttribute
	private String			name;

	@XmlElement
	private String			description;

	@XmlElement(name = "period")
	private List<XPeriod>	periods					= new ArrayList<XPeriod>();

	@XmlElement(name = "property")
	private List<XProperty>	properties				= new ArrayList<XProperty>();

	@XmlElement(name = "defaultPeriodProperty")
	private List<XProperty>	defaultPeriodProperties	= new ArrayList<XProperty>();

	public XProject() {}

	public XProject(String name) {
		super();
		this.name = name;
	}

	public XProject(String name, String description) {
		super();
		this.name = name;
		this.description = description;
		this.uuid = UUID.randomUUID();
	}

	private XProject(String name, String description, UUID uuid) {
		super();
		this.name = name;
		this.description = description;
		this.uuid = uuid;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	public boolean add(XPeriod e) {
		e.setProject(this);
		return periods.add(e);
	}

	public boolean addAll(Collection<? extends XPeriod> c) {
		return periods.addAll(c);
	}

	public boolean remove(Object o) {
		if (periods.remove(o)) {
			((XPeriod) o).setProject(null);
			return true;
		}
		return false;
	}

	@Override
	public List<? extends IPeriod> getPeriods() {
		return periods;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((uuid == null) ? 0 : uuid.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) return true;
		if (obj == null) return false;
		if (getClass() != obj.getClass()) return false;
		XProject other = (XProject) obj;
		if (uuid == null) {
			if (other.uuid != null) return false;
		} else if (!uuid.equals(other.uuid)) return false;
		return true;
	}

	/**
	 * Will create a copy of the original object without referenced objects.
	 * 
	 * @return
	 */
	@Override
	public XProject copy() {
		return new XProject(name, description, uuid);
	}

	@Override
	public List<XProperty> getProperties() {
		return this.properties;
	}

	@Override
	public List<XProperty> getDefaultPeriodProperties() {
		return this.defaultPeriodProperties;
	}

}
