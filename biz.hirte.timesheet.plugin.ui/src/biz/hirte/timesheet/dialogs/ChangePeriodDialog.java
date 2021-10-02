package biz.hirte.timesheet.dialogs;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.databinding.viewers.ViewerProperties;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;

import biz.hirte.swt.controls.DatePicker;
import biz.hirte.timesheet.Activator;
import biz.hirte.timesheet.PreferenceKeys;
import biz.hirte.timesheet.model.IPeriod;
import biz.hirte.timesheet.model.IProject;
import biz.hirte.timesheet.model.IProperty;

public class ChangePeriodDialog extends TitleAreaDialog {

	private static final int	PAD_H	= 6;
	private static final int	PAD_V	= 6;
	private Combo				cmbProjects;
	private CLabel				lblDuration;
	private Text				txtNote;
	private DateTime			dtStart;
	private DateTime			dtEnd;
	private ComboViewer			cmbvProjects;
	private DatePicker			dpDate;

	// ==== Model
	private final PeriodInfo	pi;
	private List<IProject>		projects;
	private final IProject		defaultProject;
	private Text				txtBreak;

	/**
	 * Create the dialog.
	 * 
	 * @wbp.parser.constructor
	 * @param parentShell
	 */
	public ChangePeriodDialog(Shell parentShell, List<IProject> projects, IPeriod period, IProject project) {
		super(parentShell);
		this.projects = projects;
		this.defaultProject = project;
		if (period == null) {
			this.pi = new PeriodInfo();
		} else {
			this.pi = new PeriodInfo(period);
		}
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {

		setTitle("Add Effort");
		setMessage("Please provide at least project, start and end");

		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		container.setLayout(new FormLayout());
		GridData gd_container = new GridData(GridData.FILL_BOTH);
		gd_container.heightHint = 200;
		container.setLayoutData(gd_container);

		CLabel lblProject = new CLabel(container, SWT.NONE);
		lblProject.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
		FormData fd_lblProject = new FormData();
		fd_lblProject.left = new FormAttachment(0, PAD_H);
		fd_lblProject.top = new FormAttachment(0, PAD_V);
		fd_lblProject.width = 50;
		lblProject.setLayoutData(fd_lblProject);
		lblProject.setText("Project");

		cmbvProjects = new ComboViewer(container, SWT.NONE);
		cmbProjects = cmbvProjects.getCombo();
		fd_lblProject.right = new FormAttachment(cmbProjects, -6);
		FormData fd_cmbProjects = new FormData();
		fd_cmbProjects.left = new FormAttachment(0, 71);
		fd_cmbProjects.right = new FormAttachment(100, -6);
		fd_cmbProjects.top = new FormAttachment(0, PAD_V);
		cmbProjects.setLayoutData(fd_cmbProjects);

		CLabel lblStart = new CLabel(container, SWT.NONE);
		lblStart.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
		lblStart.setText("Start");
		FormData fd_lblStart = new FormData();
		fd_lblStart.top = new FormAttachment(lblProject, 6);
		fd_lblStart.width = 50;
		lblStart.setLayoutData(fd_lblStart);

		dtStart = new DateTime(container, SWT.BORDER | SWT.TIME | SWT.SHORT);
		dtStart.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (dtStart.getHours() > dtEnd.getHours()) {
					dtEnd.setHours(dtStart.getHours());
				} else if (dtStart.getHours() == dtEnd.getHours()) {
					if (dtStart.getMinutes() > dtEnd.getMinutes()) {
						dtEnd.setMinutes(dtStart.getMinutes());
					}
				}
				updateLabelDuration();
			}
		});
		dtStart.setHours(8);
		dtStart.setMinutes(0);
		dtStart.setSeconds(0);
		FormData fd_dtStart = new FormData();
		fd_dtStart.width = 80;
		fd_dtStart.left = new FormAttachment(lblStart, 14);
		fd_dtStart.top = new FormAttachment(cmbProjects, 6);
		dtStart.setLayoutData(fd_dtStart);

		CLabel lblEnd = new CLabel(container, SWT.NONE);
		fd_lblStart.right = new FormAttachment(lblEnd, 0, SWT.RIGHT);
		lblEnd.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
		FormData fd_lblEnd = new FormData();
		fd_lblEnd.left = new FormAttachment(lblProject, 0, SWT.LEFT);
		fd_lblEnd.width = 50;
		lblEnd.setLayoutData(fd_lblEnd);
		lblEnd.setText("End");

		dtEnd = new DateTime(container, SWT.BORDER | SWT.TIME | SWT.SHORT);
		fd_lblEnd.top = new FormAttachment(dtEnd, 0, SWT.TOP);
		dtEnd.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				if (dtStart.getHours() > dtEnd.getHours()) {
					dtStart.setHours(dtEnd.getHours());
				} else if (dtStart.getHours() == dtEnd.getHours()) {
					if (dtStart.getMinutes() > dtEnd.getMinutes()) {
						dtStart.setMinutes(dtEnd.getMinutes());
					}
				}
				updateLabelDuration();
			}
		});
		dtEnd.setMinutes(0);
		dtEnd.setHours(16);
		dtEnd.setSeconds(0);
		FormData fd_dtEnd = new FormData();
		fd_dtEnd.width = 80;
		fd_dtEnd.left = new FormAttachment(lblEnd, 14);
		fd_dtEnd.top = new FormAttachment(dtStart, 6);
		dtEnd.setLayoutData(fd_dtEnd);

		CLabel lblNote = new CLabel(container, SWT.NONE);
		lblNote.setFont(SWTResourceManager.getFont("Segoe UI", 9, SWT.NORMAL));
		lblNote.setText("Note");
		FormData fd_lblNote = new FormData();
		fd_lblNote.left = new FormAttachment(lblProject, 0, SWT.LEFT);
		fd_lblNote.width = 50;
		lblNote.setLayoutData(fd_lblNote);

		txtNote = new Text(container, SWT.BORDER | SWT.MULTI);
		fd_lblNote.top = new FormAttachment(txtNote, 0, SWT.TOP);
		FormData fd_txtNote = new FormData();
		fd_txtNote.left = new FormAttachment(cmbProjects, 0, SWT.LEFT);
		fd_txtNote.top = new FormAttachment(dtEnd, 6);
		fd_txtNote.bottom = new FormAttachment(100, -PAD_H);
		fd_txtNote.right = new FormAttachment(100, -PAD_H);
		txtNote.setLayoutData(fd_txtNote);

		lblDuration = new CLabel(container, SWT.NONE);
		lblDuration.setAlignment(SWT.CENTER);
		FormData fd_lblDuration = new FormData();
		fd_lblDuration.width = 60;
		fd_lblDuration.top = new FormAttachment(dtEnd, 0, SWT.TOP);
		fd_lblDuration.right = new FormAttachment(100, -6);
		lblDuration.setLayoutData(fd_lblDuration);
		lblDuration.setText("08:00  hh:mm");

		dpDate = new DatePicker(container, SWT.NONE);
		FormData fd_dpDate = new FormData();
		fd_dpDate.bottom = new FormAttachment(dtStart, 0, SWT.BOTTOM);
		fd_dpDate.top = new FormAttachment(dtStart, 0, SWT.TOP);
		fd_dpDate.left = new FormAttachment(dtStart, 5);
		fd_dpDate.right = new FormAttachment(cmbProjects, 0, SWT.RIGHT);
		dpDate.setLayoutData(fd_dpDate);

		CLabel lblBreak = new CLabel(container, SWT.NONE);
		FormData fd_lblBreak = new FormData();
		fd_lblBreak.top = new FormAttachment(lblEnd, 0, SWT.TOP);
		fd_lblBreak.left = new FormAttachment(dtEnd, 6);
		lblBreak.setLayoutData(fd_lblBreak);
		lblBreak.setText("Break");

		txtBreak = new Text(container, SWT.BORDER);
		txtBreak.setText("0,00");
		FormData fd_txtBreak = new FormData();
		fd_txtBreak.top = new FormAttachment(lblEnd, 0, SWT.TOP);
		fd_txtBreak.left = new FormAttachment(lblBreak, 6);
		fd_txtBreak.width = 30;
		txtBreak.setLayoutData(fd_txtBreak);

		this.cmbvProjects.setContentProvider(ArrayContentProvider.getInstance());
		this.cmbvProjects.setLabelProvider(new LabelProvider() {

			@Override
			public String getText(Object element) {
				IProject p = (IProject) element;
				return p.getName();
			}
		});

		restoreDialogSettings();
		updateInputs();
		updateLabelDuration();
		initDataBindings();

		return area;
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(336, 440);
	}

	private void updateInputs() {
		this.cmbvProjects.setInput(projects);
		this.cmbProjects.setText(defaultProject == null ? "" : defaultProject.getName());
		dtStart.setHours(pi.start.getHour());
		dtStart.setMinutes(pi.start.getMinute());
		dtEnd.setHours(pi.end.getHour());
		dtEnd.setMinutes(pi.end.getMinute());
		dpDate.setDate(pi.day);
		txtBreak.setText(pi.breakTime.toMinutes() + "");
	}

	private void updateLabelDuration() {

		LocalTime ltStart = LocalTime.of(dtStart.getHours(), dtStart.getMinutes());
		LocalTime ltEnd = LocalTime.of(dtEnd.getHours(), dtEnd.getMinutes());
		Duration dur = Duration.between(ltStart, ltEnd);

		lblDuration.setText(String.format("%4.2f hrs", dur.toMinutes() / 60f));

	}

	@Override
	protected void buttonPressed(int buttonId) {
		if (buttonId == IDialogConstants.OK_ID && this.buttonOkClicked()) {
			super.buttonPressed(buttonId);
		}

		if (buttonId == IDialogConstants.CANCEL_ID) {
			super.buttonPressed(buttonId);
		}
	}

	private boolean buttonOkClicked() {
		if (!txtBreak.getText().matches("\\d+")) {
			MessageDialog.openError(getShell(), "Break duration", "Break duration must be a decimal.");
			txtBreak.setFocus();
			return false;
		}

		LocalTime start = LocalTime.of(dtStart.getHours(), dtStart.getMinutes());
		LocalTime end = LocalTime.of(dtEnd.getHours(), dtEnd.getMinutes());

		if (end.isBefore(start) || start.equals(end)) {
			MessageDialog.openError(getShell(), "Start and End time", "Start has to be earlier than End.");
			dtEnd.setFocus();
			return false;
		}

		if (this.pi.project == null) {
			MessageDialog.openError(getShell(), "Project missing", "A project must be selected.");
			this.cmbProjects.setFocus();
			return false;
		}

		this.pi.start = LocalTime.of(dtStart.getHours(), dtStart.getMinutes());
		this.pi.end = LocalTime.of(dtEnd.getHours(), dtEnd.getMinutes());
		this.pi.day = dpDate.getDate();
		this.pi.breakTime = Duration.ofMinutes(Long.valueOf(txtBreak.getText()));
		storeDialogSettings();
		return true;
	}

	private void storeDialogSettings() {
		IPreferenceStore prefStore = Activator.getDefault().getPreferenceStore();
		prefStore.putValue(PreferenceKeys.CHANGE_PERIOD_DIALOG_LAST_PROJECT, cmbProjects.getText());
		prefStore.putValue(PreferenceKeys.CHANGE_PERIOD_DIALOG_LAST_FROM_DATE_HRS, "" + dtStart.getHours());
		prefStore.putValue(PreferenceKeys.CHANGE_PERIOD_DIALOG_LAST_FROM_DATE_MIN, "" + dtStart.getMinutes());
		prefStore.putValue(PreferenceKeys.CHANGE_PERIOD_DIALOG_LAST_TO_DATE_HRS, "" + dtEnd.getHours());
		prefStore.putValue(PreferenceKeys.CHANGE_PERIOD_DIALOG_LAST_TO_DATE_MIN, "" + dtEnd.getMinutes());
	}

	private void restoreDialogSettings() {
		IPreferenceStore prefStore = Activator.getDefault().getPreferenceStore();
		cmbProjects.setText(prefStore.getString(PreferenceKeys.CHANGE_PERIOD_DIALOG_LAST_PROJECT));
		if (pi.isNew()) {
			pi.start = LocalTime.of(prefStore.getInt(PreferenceKeys.CHANGE_PERIOD_DIALOG_LAST_FROM_DATE_HRS),
					prefStore.getInt(PreferenceKeys.CHANGE_PERIOD_DIALOG_LAST_FROM_DATE_MIN));
			pi.end = LocalTime.of(prefStore.getInt(PreferenceKeys.CHANGE_PERIOD_DIALOG_LAST_TO_DATE_HRS),
					prefStore.getInt(PreferenceKeys.CHANGE_PERIOD_DIALOG_LAST_TO_DATE_MIN));
		}
	}

	public PeriodInfo getPeriodInfo() {
		return this.pi;
	}

	public class PeriodInfo {

		IProject		project		= null;
		LocalDate		day			= LocalDate.now();
		LocalTime		start		= LocalTime.of(8, 0);
		LocalTime		end			= LocalTime.of(16, 0);
		Duration		breakTime	= Duration.ZERO;
		String			comment		= "";
		List<IProperty>	props		= new ArrayList<IProperty>();

		PeriodInfo(IPeriod p) {
			this.day = p.getDay();
			this.start = p.getBegin();
			this.end = p.getEnd();
			this.comment = p.getComment();
			this.project = p.getProject();
			this.breakTime = p.getBreakDuration();
			this.props.addAll(p.getProperties());

		}

		PeriodInfo() {}

		boolean isNew() {
			return project == null;
		}

		public LocalDate getDay() {
			return day;
		}

		public void setDay(LocalDate day) {
			this.day = day;
		}

		public LocalTime getStart() {
			return start;
		}

		public void setStart(LocalTime start) {
			this.start = start;
		}

		public LocalTime getEnd() {
			return end;
		}

		public void setEnd(LocalTime end) {
			this.end = end;
		}

		public IProject getProject() {
			return project;
		}

		public void setProject(IProject project) {
			this.project = project;
		}

		public String getComment() {
			return comment;
		}

		public void setComment(String comment) {
			this.comment = comment;
		}

		public List<IProperty> getProps() {
			return props;
		}

		public Duration getBreakTime() {
			return breakTime;
		}

		public void setBreakTime(Duration breakTime) {
			this.breakTime = breakTime;
		}

	}

	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeTextTxtNoteObserveWidget = WidgetProperties.text(SWT.Modify).observe(txtNote);
		IObservableValue commentPiObserveValue = PojoProperties.value("comment").observe(pi);
		bindingContext.bindValue(observeTextTxtNoteObserveWidget, commentPiObserveValue, null, null);
		//
		IObservableValue observeSingleSelectionCmbvProjects = ViewerProperties.singleSelection().observeDelayed(10, cmbvProjects);
		IObservableValue projectPiObserveValue = PojoProperties.value("project").observe(pi);
		bindingContext.bindValue(observeSingleSelectionCmbvProjects, projectPiObserveValue, null, null);
		//
		return bindingContext;
	}
}
