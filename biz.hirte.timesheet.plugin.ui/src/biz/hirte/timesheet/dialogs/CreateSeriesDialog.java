package biz.hirte.timesheet.dialogs;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import biz.hirte.swt.controls.DatePicker;
import biz.hirte.timesheet.model.IPeriod;
import biz.hirte.timesheet.model.IProperty;

public class CreateSeriesDialog extends TitleAreaDialog {

	private Text				txtComment;
	private IPeriod				period;
	private Button				btnMo;
	private Button				btnThu;
	private Button				btnWed;
	private Button				btnTue;
	private Button				btnFri;
	private Button				btnSat;
	private Button				btnSun;

	private SeriesInformation	ret	= null;
	private DatePicker			dpFrom;
	private DatePicker			dpUntil;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public CreateSeriesDialog(Shell parentShell, IPeriod period) {
		super(parentShell);
		this.period = period;
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setMessage("Please provide from and until and a note for every day of the series.");
		setTitle("Create Period Series");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new FormLayout());
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		CLabel lblFrom = new CLabel(container, SWT.NONE);
		FormData fd_lblFrom = new FormData();
		fd_lblFrom.left = new FormAttachment(0, 10);
		lblFrom.setLayoutData(fd_lblFrom);
		lblFrom.setText("Begin:");

		dpFrom = new DatePicker(container, SWT.NONE);
		FormData fd_dpFrom = new FormData();
		fd_dpFrom.width = 120;
		fd_dpFrom.left = new FormAttachment(lblFrom, 6);
		fd_dpFrom.bottom = new FormAttachment(lblFrom, 0, SWT.BOTTOM);
		dpFrom.setLayoutData(fd_dpFrom);

		CLabel lblUntil = new CLabel(container, SWT.NONE);
		FormData fd_lblUntil = new FormData();
		fd_lblUntil.top = new FormAttachment(lblFrom, 0, SWT.TOP);
		lblUntil.setLayoutData(fd_lblUntil);
		lblUntil.setText("End:");

		dpUntil = new DatePicker(container, SWT.NONE);
		fd_lblUntil.right = new FormAttachment(dpUntil, -6);
		FormData fd_dpUntil = new FormData();
		fd_dpUntil.width = 120;
		fd_dpUntil.top = new FormAttachment(dpFrom, 0, SWT.TOP);
		dpUntil.setLayoutData(fd_dpUntil);

		Group grpWeekdays = new Group(container, SWT.NONE);
		fd_lblFrom.bottom = new FormAttachment(grpWeekdays, -6);
		fd_dpUntil.right = new FormAttachment(grpWeekdays, 0, SWT.RIGHT);
		grpWeekdays.setText("Weekdays");
		grpWeekdays.setLayout(new FormLayout());
		FormData fd_grpWeekdays = new FormData();
		fd_grpWeekdays.top = new FormAttachment(0, 42);
		fd_grpWeekdays.right = new FormAttachment(100, -10);
		fd_grpWeekdays.left = new FormAttachment(0, 10);
		grpWeekdays.setLayoutData(fd_grpWeekdays);

		btnMo = new Button(grpWeekdays, SWT.CHECK);
		btnMo.setSelection(true);
		FormData fd_btnMo = new FormData();
		fd_btnMo.top = new FormAttachment(0, 10);
		fd_btnMo.left = new FormAttachment(0, 10);
		btnMo.setLayoutData(fd_btnMo);
		btnMo.setText("Mon");

		btnThu = new Button(grpWeekdays, SWT.CHECK);
		btnThu.setSelection(true);
		FormData fd_btnThu = new FormData();
		fd_btnThu.bottom = new FormAttachment(btnMo, 0, SWT.BOTTOM);
		fd_btnThu.left = new FormAttachment(btnMo, 6);
		btnThu.setLayoutData(fd_btnThu);
		btnThu.setText("Tue");

		btnWed = new Button(grpWeekdays, SWT.CHECK);
		btnWed.setSelection(true);
		FormData fd_btnWed = new FormData();
		fd_btnWed.top = new FormAttachment(btnMo, 0, SWT.TOP);
		fd_btnWed.left = new FormAttachment(btnThu, 6);
		btnWed.setLayoutData(fd_btnWed);
		btnWed.setText("Wed");

		btnTue = new Button(grpWeekdays, SWT.CHECK);
		btnTue.setSelection(true);
		FormData fd_btnTue = new FormData();
		fd_btnTue.bottom = new FormAttachment(btnMo, 0, SWT.BOTTOM);
		fd_btnTue.left = new FormAttachment(btnWed, 6);
		btnTue.setLayoutData(fd_btnTue);
		btnTue.setText("Thr");

		btnFri = new Button(grpWeekdays, SWT.CHECK);
		btnFri.setSelection(true);
		FormData fd_btnFri = new FormData();
		fd_btnFri.top = new FormAttachment(btnMo, 0, SWT.TOP);
		fd_btnFri.left = new FormAttachment(btnTue, 6);
		btnFri.setLayoutData(fd_btnFri);
		btnFri.setText("Fri");

		btnSat = new Button(grpWeekdays, SWT.CHECK);
		FormData fd_btnSat = new FormData();
		fd_btnSat.top = new FormAttachment(btnMo, 0, SWT.TOP);
		fd_btnSat.left = new FormAttachment(btnFri, 6);
		btnSat.setLayoutData(fd_btnSat);
		btnSat.setText("Sat");

		btnSun = new Button(grpWeekdays, SWT.CHECK);
		FormData fd_btnSun = new FormData();
		fd_btnSun.bottom = new FormAttachment(btnMo, 0, SWT.BOTTOM);
		fd_btnSun.left = new FormAttachment(btnSat, 6);
		btnSun.setLayoutData(fd_btnSun);
		btnSun.setText("Sun");

		Group grpNote = new Group(container, SWT.NONE);
		fd_grpWeekdays.bottom = new FormAttachment(grpNote, -6);
		grpNote.setText("Note");
		grpNote.setLayout(new FormLayout());
		FormData fd_grpNote = new FormData();
		fd_grpNote.right = new FormAttachment(100, -10);
		fd_grpNote.left = new FormAttachment(0, 10);
		fd_grpNote.bottom = new FormAttachment(100, -10);
		fd_grpNote.top = new FormAttachment(0, 116);
		grpNote.setLayoutData(fd_grpNote);

		txtComment = new Text(grpNote, SWT.BORDER | SWT.MULTI);
		FormData fd_txtComment = new FormData();
		fd_txtComment.bottom = new FormAttachment(100, -10);
		fd_txtComment.right = new FormAttachment(100, -10);
		fd_txtComment.top = new FormAttachment(0, 10);
		fd_txtComment.left = new FormAttachment(0, 10);
		txtComment.setLayoutData(fd_txtComment);

		initControls();

		return container;
	}

	private void initControls() {
		this.txtComment.setText(this.period.getComment());
		LocalDate day = this.period.getDay().plusDays(1);
		this.dpFrom.setDate(day);
		this.dpUntil.setDate(day.withDayOfMonth(day.lengthOfMonth()));
	}

	/**
	 * Create contents of the button bar.
	 * 
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected void buttonPressed(int buttonId) {

		if (buttonId == IDialogConstants.OK_ID) {
			this.ret = createSeriesInformation();
			// ret == null will indicate wrong series information.
			if (ret != null) {
				close();
			}
		} else if (buttonId == IDialogConstants.CANCEL_ID) {
			close();
		}

	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(390, 424);
	}

	private SeriesInformation createSeriesInformation() {

		SeriesInformation st = new SeriesInformation();

		st.start = dpFrom.getDate();
		st.end = dpUntil.getDate();

		if (st.start.isAfter(st.end)) {
			return null;
		}

		if (btnMo.getSelection()) {
			st.weekDays.add(DayOfWeek.MONDAY);
		}

		if (btnTue.getSelection()) {
			st.weekDays.add(DayOfWeek.TUESDAY);
		}

		if (btnWed.getSelection()) {
			st.weekDays.add(DayOfWeek.WEDNESDAY);
		}

		if (btnThu.getSelection()) {
			st.weekDays.add(DayOfWeek.THURSDAY);
		}

		if (btnFri.getSelection()) {
			st.weekDays.add(DayOfWeek.FRIDAY);
		}

		if (btnSat.getSelection()) {
			st.weekDays.add(DayOfWeek.SATURDAY);
		}

		if (btnSun.getSelection()) {
			st.weekDays.add(DayOfWeek.SUNDAY);
		}

		st.text = this.txtComment.getText();

		return st;

	}

	public SeriesInformation getSeriesInformation() {
		return ret;
	}

	public class SeriesInformation {

		LocalDate		start		= LocalDate.now();
		LocalDate		end			= LocalDate.now();
		Set<DayOfWeek>	weekDays	= new HashSet<DayOfWeek>();
		String			text		= "";
		List<IProperty>	props		= new ArrayList<IProperty>();

		public LocalDate getStart() {
			return start;
		}

		public LocalDate getEnd() {
			return end;
		}

		public Set<DayOfWeek> getWeekDays() {
			return weekDays;
		}

		public String getText() {
			return text;
		}

		public List<IProperty> getProps() {
			return props;
		}

	}
}
