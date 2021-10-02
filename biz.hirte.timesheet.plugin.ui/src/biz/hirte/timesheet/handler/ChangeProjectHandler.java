package biz.hirte.timesheet.handler;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import biz.hirte.timesheet.EventTopics;
import biz.hirte.timesheet.dialogs.ChangeProjectDialog;
import biz.hirte.timesheet.dialogs.ChangeProjectDialog.ProjectInfo;
import biz.hirte.timesheet.events.ProjectEvent;
import biz.hirte.timesheet.model.IProject;
import biz.hirte.timesheet.service.ITimesheetProviderService;

public class ChangeProjectHandler {

	@Execute
	public void execute(Shell shell, IEventBroker eventBroker, ITimesheetProviderService service, IEclipseContext ctx, @Optional IProject project) {

		ChangeProjectDialog cpd = new ChangeProjectDialog(shell, project, ctx);

		if (cpd.open() == Window.OK) {

			IProject updatedProject = null;

			if (project == null) {
				/*
				 * project == null bedeutet, dass IProject muss neu erstellt
				 * werden.
				 */
				ProjectInfo pi = cpd.getProjectInfo();
				updatedProject = service.addProject(pi.getName(), pi.getDescription());

			} else {
				/*
				 * project != null bedeutet, dass IProject muss aktualisiert
				 * werden
				 */
				ProjectInfo pi = cpd.getProjectInfo();
				updatedProject = service.updateProject(project, pi.getName(), pi.getDescription());

			}

			eventBroker.post(EventTopics.TOPIC_PERIOD, new ProjectEvent(updatedProject));
		}
	}
}
