package biz.hirte.timesheet.events;

import biz.hirte.timesheet.model.IProject;

public class ProjectEvent {

	private IProject updatedProject = null;

	public ProjectEvent(IProject updatedProject) {
		this.updatedProject = updatedProject;
	}

	public IProject getUpdatedProject() {
		return updatedProject;
	}

	public void setUpdatedProject(IProject updatedProject) {
		this.updatedProject = updatedProject;
	}

}
