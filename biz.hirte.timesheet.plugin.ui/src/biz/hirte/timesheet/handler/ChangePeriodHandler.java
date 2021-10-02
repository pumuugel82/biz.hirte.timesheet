
package biz.hirte.timesheet.handler;

import java.util.List;

import org.eclipse.e4.core.di.annotations.Execute;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.core.services.events.IEventBroker;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.widgets.Shell;

import biz.hirte.timesheet.EventTopics;
import biz.hirte.timesheet.dialogs.ChangePeriodDialog;
import biz.hirte.timesheet.dialogs.ChangePeriodDialog.PeriodInfo;
import biz.hirte.timesheet.events.PeriodEvent;
import biz.hirte.timesheet.model.IPeriod;
import biz.hirte.timesheet.model.IProject;
import biz.hirte.timesheet.service.ITimesheetProviderService;

/**
 * Handler that brings up the {@link ChangePeriodDialog}. This handly may get
 * called by menu / toolbar items in the menu / toolbar.
 * 
 * @author hirte
 *
 */
public class ChangePeriodHandler {

	@Execute
	public void execute(Shell shell, IEventBroker eventBroker, ITimesheetProviderService service, @Optional IPeriod period, @Optional IProject project) {

		@SuppressWarnings("unchecked")
		List<IProject> allProjects = (List<IProject>) service.getAllProjects();
		ChangePeriodDialog apd = new ChangePeriodDialog(shell, allProjects, period, project);

		/* Check which button was pressed OK | CANCEL */
		if (apd.open() == Window.OK) {

			/*
			 * Notify every listening components about the new Period. The
			 * period may have been persisted from within the dialog already,
			 * synchronously.
			 */

			PeriodInfo pi = apd.getPeriodInfo();

			IPeriod updatedPeriod = null;
			if (period == null) {
				updatedPeriod = service.addPeriod(pi.getProject(), pi.getDay(), pi.getStart(), pi.getEnd(), (int) pi.getBreakTime().toMinutes(),
						pi.getComment(), pi.getProps());
			} else {
				updatedPeriod = service.updatePeriod(period, pi.getDay(), pi.getStart(), pi.getEnd(), (int) pi.getBreakTime().toMinutes(), pi.getComment(),
						pi.getProps());
			}

			eventBroker.post(EventTopics.TOPIC_PERIOD, new PeriodEvent(updatedPeriod));
		}

	}

}