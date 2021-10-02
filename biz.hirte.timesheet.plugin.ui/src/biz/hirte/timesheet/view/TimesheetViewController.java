package biz.hirte.timesheet.view;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.eclipse.core.commands.ExecutionException;
import org.eclipse.core.commands.NotEnabledException;
import org.eclipse.core.commands.NotHandledException;
import org.eclipse.core.commands.common.NotDefinedException;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.e4.core.di.annotations.Creatable;
import org.eclipse.e4.core.di.annotations.Optional;
import org.eclipse.e4.ui.di.UIEventTopic;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.program.Program;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.IHandlerService;

import biz.hirte.timesheet.EventTopics;
import biz.hirte.timesheet.PluginKeys;
import biz.hirte.timesheet.dialogs.CreateSeriesDialog;
import biz.hirte.timesheet.dialogs.CreateSeriesDialog.SeriesInformation;
import biz.hirte.timesheet.dialogs.ExportDialog;
import biz.hirte.timesheet.dialogs.ExportDialog.ExportInformation;
import biz.hirte.timesheet.events.PeriodEvent;
import biz.hirte.timesheet.events.ProjectEvent;
import biz.hirte.timesheet.model.DateFilter;
import biz.hirte.timesheet.model.IPeriod;
import biz.hirte.timesheet.model.IProject;
import biz.hirte.timesheet.service.ITimesheetProviderService;

@Creatable
public class TimesheetViewController {

	private TimesheetView				view;
	private ITimesheetProviderService	service			= null;
	private IHandlerService				handlerService	= null;
	private IEclipseContext				context			= null;
	private List<YearMonth>				yearMonthFilter	= new ArrayList<YearMonth>();

	@Inject
	public TimesheetViewController(ITimesheetProviderService srv, IHandlerService handlerService, IEclipseContext ctx) {
		this.service = srv;
		this.handlerService = handlerService;
		this.context = ctx;
	}

	TimesheetView getView() {
		return view;
	}

	void setView(TimesheetView view) {
		this.view = view;
	}

	void onCreateSeries(Shell shell, IPeriod period) {

		CreateSeriesDialog dlg = new CreateSeriesDialog(shell, period);

		if (dlg.open() != IDialogConstants.OK_ID) {
			return;
		}

		SeriesInformation si = dlg.getSeriesInformation();
		LocalDate day = si.getStart();
		LocalDate end = si.getEnd();

		while (!day.isAfter(end)) {

			DayOfWeek dow = day.getDayOfWeek();

			if (si.getWeekDays().contains(dow)) {
				service.addPeriod(period.getProject(), day, period.getBegin(), period.getEnd(), (int) period.getBreakDuration().toMinutes(), si.getText(),
						si.getProps());
			}

			day = day.plusDays(1);
		}

		updateView();
	}

	void onExportSelectedProjectsClicked(Shell shell) {

		HashMap<IProject, List<IPeriod>> projectPeriods = new HashMap<IProject, List<IPeriod>>();

		IProject[] selectedProjects = getSelectedProjects();

		for (IProject p : selectedProjects) {
			projectPeriods.put(p, (List<IPeriod>) p.getPeriods());
		}

		YearMonth ym = YearMonth.now();
		LocalDate atEndOfMonth = ym.atEndOfMonth();
		LocalDate atStartOfMonth = ym.atDay(1);

		ExportDialog ed = new ExportDialog(shell, atStartOfMonth, atEndOfMonth);

		if (ed.open() == Window.OK) {

			ExportInformation ei = ed.getExportInformation();
			Map<IProject, String> exports = ei.getExporter().getExportService().export(projectPeriods, ei.getFrom(), ei.getTo());
			for (Map.Entry<IProject, String> exportPath : exports.entrySet()) {
				openExportedFile(shell, exportPath.getKey(), exportPath.getValue());
			}

		}

	}

	private void openExportedFile(Shell shell, IProject iProject, String exportFilePath) {
		StringBuilder sb = new StringBuilder();
		sb.append("The periods of project ").append(iProject.getName()).append(" have been exported successfully to:\n\n");
		sb.append(exportFilePath).append("\n\n");
		sb.append("Do you want to open the file right now?");
		boolean open = MessageDialog.openConfirm(shell, "Export successful", sb.toString());
		if (open) {
			Program.launch(exportFilePath);
		}
	}

	protected void deleteSelectedProjects(Shell parent) {
		IProject[] selectedProjects = getSelectedProjects();

		for (IProject p : selectedProjects) {
			boolean deleteConfirmed = MessageDialog.openConfirm(parent, "Delete Period", String.format("Do you really want to delete:\n\n %s?", p));
			if (deleteConfirmed) {
				service.removeProject(p);
			}
		}

		updateView();
	}

	protected void onDeleteSelectedPeriods(Shell parent) {

		IPeriod[] selectedPeriods = getSelectedPeriods();
		for (IPeriod p : selectedPeriods) {
			boolean deleteConfirmed = MessageDialog.openConfirm(parent, "Delete Period", String.format("Do you really want to delete:\n\n %s?", p));
			if (deleteConfirmed) {
				service.removePeriod(p.getProject(), p);
			}
		}

		updateView();
	}

	protected void onExportSelectedPeriods(Shell shell) {

		HashMap<IProject, List<IPeriod>> projectPeriods = new HashMap<IProject, List<IPeriod>>();

		IPeriod[] selectedPeriods = getSelectedPeriods();
		for (IPeriod p : selectedPeriods) {
			if (projectPeriods.containsKey(p.getProject())) {
				projectPeriods.get(p.getProject()).add(p);
			} else {
				projectPeriods.put(p.getProject(), new ArrayList<IPeriod>());
			}
		}

		YearMonth ym = YearMonth.now();
		LocalDate atEndOfMonth = ym.atEndOfMonth();
		LocalDate atBeginOfMonth = ym.atDay(1);

		ExportDialog ed = new ExportDialog(shell, atBeginOfMonth, atEndOfMonth);

		if (ed.open() == Window.OK) {

			ExportInformation ei = ed.getExportInformation();

			Map<IProject, String> exports = ei.getExporter().getExportService().export(projectPeriods, ei.getFrom(), ei.getTo());

			for (Map.Entry<IProject, String> export : exports.entrySet()) {
				openExportedFile(shell, export.getKey(), export.getValue());
			}
		}
	}

	void updateView() {
		List<DateFilter> dateFilter = yearMonthFilter.stream().map((ym -> new DateFilter(ym.atDay(1), ym.atEndOfMonth()))).collect(Collectors.toList());
		List<? extends IProject> projectsPeriods = service.getProjectsPeriods(dateFilter);
		view.updateInputs(projectsPeriods);
	}

	@Inject
	@Optional
	private void subscribeTopicPeriodAdd(@UIEventTopic(EventTopics.TOPIC_PERIOD) Period data) {

		if (data != null) {
			updateView();
		}
	}

	public boolean addYearMonthFilter(YearMonth e) {
		return yearMonthFilter.add(e);
	}

	public boolean removeYearMonthFilter(Object o) {
		return yearMonthFilter.remove(o);
	}

	@Inject
	@Optional
	public void onPeriodChange(@UIEventTopic(EventTopics.TOPIC_PERIOD) PeriodEvent data) {
		updateView();
	}

	@Inject
	@Optional
	public void onProjectChange(@UIEventTopic(EventTopics.TOPIC_PROJECT) ProjectEvent data) {
		updateView();
	}

	void onAddPeriodClicked() {

		IProject p = null;

		IProject[] selectedProjects = this.getSelectedProjects();
		IPeriod[] selectedPeriods = this.getSelectedPeriods();

		if (selectedPeriods != null && selectedPeriods.length > 0) {
			p = selectedPeriods[0].getProject();
		} else if (selectedProjects != null && selectedProjects.length > 0) {
			p = selectedProjects[0];
		}

		try {
			context.set(IPeriod.class, null);
			context.set(IProject.class, p);
			handlerService.executeCommand(PluginKeys.COMMAND_ID_CHANGE_PERIOD, null);
		}
		catch (ExecutionException | NotDefinedException | NotEnabledException | NotHandledException e) {
			e.printStackTrace();
		}
	}

	void onContextMenuBeforeShow() {

		IProject[] selectedProjects = this.getSelectedProjects();
		IPeriod[] selectedPeriods = this.getSelectedPeriods();

		if (selectedPeriods.length == 0) {
			view.enableEditPeriod(false);
			view.enableCreateSeries(false);
			view.enableDeletePeriods(false);
			view.enableExportPeriods(false);
			view.enableMergePeriods(false);
		} else if (selectedPeriods.length == 1) {
			view.enableEditPeriod(true);
			view.enableCreateSeries(true);
			view.enableDeletePeriods(true);
			view.enableExportPeriods(true);
			view.enableMergePeriods(false);
		} else if (selectedPeriods.length == 2 && selectedProjects.length == 0 && selectedPeriods[0].getProject().equals(selectedPeriods[1].getProject())) {
			view.enableEditPeriod(false);
			view.enableCreateSeries(true);
			view.enableDeletePeriods(true);
			view.enableExportPeriods(true);
			view.enableMergePeriods(true);
		} else { // > 2
			view.enableEditPeriod(false);
			view.enableCreateSeries(false);
			view.enableDeletePeriods(true);
			view.enableExportPeriods(true);
			view.enableMergePeriods(false);
		}

		if (selectedProjects.length == 1 && selectedPeriods.length == 0) {
			view.enableAddProject(true);
			view.enableEditProject(true);
			view.enableDeleteProjects(true);
			view.enableExportProjects(true);
		} else {
			view.enableAddProject(true);
			view.enableEditProject(false);
			view.enableDeleteProjects(false);
			view.enableExportProjects(false);
		}
	}

	private IPeriod[] getSelectedPeriods() {
		Object[] selectedItems = view.getTreeSelections();
		ArrayList<IPeriod> periods = new ArrayList<IPeriod>(selectedItems.length);

		for (Object item : selectedItems) {
			if (item instanceof IPeriod) {
				IPeriod p = (IPeriod) item;
				periods.add(p);
			}
		}

		return periods.toArray(new IPeriod[0]);
	}

	private IProject[] getSelectedProjects() {

		Object[] selectedItems = view.getTreeSelections();
		ArrayList<IProject> projects = new ArrayList<IProject>(selectedItems.length);

		for (Object item : selectedItems) {
			if (item instanceof IProject) {
				IProject p = (IProject) item;
				projects.add(p);
			}
		}

		return projects.toArray(new IProject[0]);
	}

	void onTreeDoubleClicked() {

		IPeriod[] selectedPeriods = getSelectedPeriods();
		if (selectedPeriods.length == 1) {
			onEditSelectedPeriod();
			return;
		}

		IProject[] selectedProjects = getSelectedProjects();
		if (selectedProjects.length == 1) {
			onEditSelectedProjectClicked();
			return;
		}

	}

	void onEditSelectedPeriod() {
		IPeriod[] selectedPeriods = getSelectedPeriods();
		if (selectedPeriods.length == 1) {
			context.set(IPeriod.class, selectedPeriods[0]);
			try {
				handlerService.executeCommand(PluginKeys.COMMAND_ID_CHANGE_PERIOD, null);
			}
			catch (ExecutionException | NotDefinedException | NotEnabledException | NotHandledException e) {
				e.printStackTrace();
			}
		}
	}

	void onEditSelectedProjectClicked() {
		IProject[] selectedProjects = getSelectedProjects();
		if (selectedProjects.length == 1) {

			context.set(IProject.class, selectedProjects[0]);
			try {
				handlerService.executeCommand(PluginKeys.COMMAND_ID_CHANGE_PROJECT, null);
			}
			catch (ExecutionException | NotDefinedException | NotEnabledException | NotHandledException e) {
				e.printStackTrace();
			}
			return;
		}
	}

	void onAddProjectClicked() {
		context.set(IProject.class, null);
		try {
			handlerService.executeCommand(PluginKeys.COMMAND_ID_CHANGE_PROJECT, null);
		}
		catch (ExecutionException | NotDefinedException | NotEnabledException | NotHandledException e) {
			e.printStackTrace();
		}
	}

}
