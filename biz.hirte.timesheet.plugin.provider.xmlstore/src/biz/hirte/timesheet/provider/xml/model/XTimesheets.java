package biz.hirte.timesheet.provider.xml.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import biz.hirte.timesheet.model.IProject;
import biz.hirte.timesheet.model.ITimesheets;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(namespace = "http://timesheets.hirte.biz", name = "timesheets")
@XmlType(name = "timesheet")
public class XTimesheets implements ITimesheets {

	@XmlElement(name = "project")
	private List<XProject>				projects	= new ArrayList<XProject>();

	@XmlElement(name = "descriptor")
	private List<XPropertyDescriptor>	descriptors	= new ArrayList<XPropertyDescriptor>();

	public XTimesheets() {}

	public boolean contains(Object o) {
		return projects.contains(o);
	}

	public XProject findProject(XProject indicator) {
		int indexOf = projects.indexOf(indicator);

		if (indexOf < 0) {
			return null;
		}

		return projects.get(indexOf);
	}

	public XProject add(XProject e) {
		projects.add(e);
		return e;
	}

	@Override
	public List<? extends IProject> getProjects() {
		return projects;
	}

	public void setProjects(List<XProject> projects) {
		this.projects = projects;
	}

	public boolean remove(IProject o) {
		return projects.remove(o);
	}

	@Override
	public List<XPropertyDescriptor> getDescriptors() {
		return descriptors;
	}

	public boolean add(XPropertyDescriptor e) {
		return descriptors.add(e);
	}

	public boolean addAll(Collection<XPropertyDescriptor> c) {
		return descriptors.addAll(c);
	}

}
