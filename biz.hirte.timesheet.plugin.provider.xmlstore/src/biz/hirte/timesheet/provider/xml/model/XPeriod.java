package biz.hirte.timesheet.provider.xml.model;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

import biz.hirte.timesheet.model.IPeriod;
import biz.hirte.timesheet.model.IProject;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "period")
public class XPeriod implements biz.hirte.timesheet.model.IPeriod {

	@XmlAttribute
	private UUID			uuid;

	@XmlAttribute
	@XmlJavaTypeAdapter(value = LocalDateXMLAdapter.class)
	private LocalDate		day;

	@XmlAttribute
	@XmlJavaTypeAdapter(value = LocalTimeXMLAdapter.class)
	private LocalTime		begin;

	@XmlAttribute
	@XmlJavaTypeAdapter(value = LocalTimeXMLAdapter.class)
	private LocalTime		end;

	@XmlAttribute(required = false)
	private int				breakInMinutes;

	@XmlAttribute
	private String			comment;

	@XmlElement(name = "descriptors")
	private List<XProperty>	properties	= new ArrayList<XProperty>();

	@XmlTransient
	private IProject		project;

	public XPeriod() {}

	public XPeriod(LocalDate day, LocalTime begin, LocalTime end, int breakInMinutes, String comment) {
		super();
		this.day = day;
		this.begin = begin;
		this.end = end;
		this.breakInMinutes = breakInMinutes;
		this.comment = comment;
		this.uuid = UUID.randomUUID();
	}

	@Override
	public LocalDate getDay() {
		return day;
	}

	public void setDay(LocalDate day) {
		this.day = day;
	}

	@Override
	public LocalTime getBegin() {
		return begin;
	}

	public void setBegin(LocalTime begin) {
		this.begin = begin;
	}

	@Override
	public LocalTime getEnd() {
		return end;
	}

	public void setEnd(LocalTime end) {
		this.end = end;
	}

	@Override
	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	@Override
	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

	@Override
	public String toString() {
		return "Period [day=" + day + ", begin=" + begin + ", end=" + end + ", comment=" + comment + "]";
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
		XPeriod other = (XPeriod) obj;
		if (uuid == null) {
			if (other.uuid != null) return false;
		} else if (!uuid.equals(other.uuid)) return false;
		return true;
	}

	public void afterUnmarshal(Unmarshaller u, Object parent) {
		this.project = (IProject) parent;
	}

	@Override
	public IProject getProject() {
		return project;
	}

	public void setProject(IProject project) {
		this.project = project;
	}

	@Override
	public Duration getDuration() {
		return Duration.between(begin, end);
	}

	@Override
	public IPeriod copy() {
		return new XPeriod(day, begin, end, breakInMinutes, comment);
	}

	@Override
	public List<XProperty> getProperties() {
		return this.properties;
	}

	@Override
	public Duration getBreakDuration() {
		return Duration.ofMinutes(breakInMinutes);
	}

	public void setBreakInMinutes(int breakInMinutes) {
		this.breakInMinutes = breakInMinutes;
	}

	public int getBreakInMinutes() {
		return breakInMinutes;
	}

}