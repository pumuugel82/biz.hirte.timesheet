package biz.hirte.timesheet.dialogs;

import java.time.LocalDate;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * Dialog that renders a small calendar (date picker). This Dialog doens't has
 * any border. So pretty much look like only the Calendar.
 * 
 * @version 1.0
 * @author christoph.hirte@gmail.com
 *
 */
public class DatePickerDialog extends Dialog {

	private LocalDate			date;
	protected Shell				shell;
	private DateTime			dateTime;
	private Point				openingLocation;

	private static final int	SHELL_WIDTH		= 187;
	private static final int	SHELL_HEIGHT	= 192;

	/**
	 * Create the dialog.
	 * 
	 * @param parent
	 * @param style
	 */
	public DatePickerDialog(Shell parent, int style, LocalDate date) {
		super(parent, style);
		setText("SWT Dialog");
		this.date = date;
	}

	/**
	 * Open the dialog and returns the selected LocalDate.
	 * 
	 * @return the result
	 */
	public LocalDate open() {
		createContents();
		shell.open();
		shell.layout();
		if (openingLocation != null) {
			shell.setLocation(openingLocation.x - (SHELL_WIDTH / 2), openingLocation.y - (SHELL_HEIGHT / 2));
		}

		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return date;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), SWT.NONE);
		shell.setSize(252, 276);
		shell.setText(getText());
		shell.setLayout(new GridLayout(1, true));

		dateTime = new DateTime(shell, SWT.CALENDAR);
		dateTime.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, false, false, 2, 1));

		Composite composite = new Composite(shell, SWT.NONE);
		composite.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		RowLayout rl_composite = new RowLayout(SWT.HORIZONTAL);
		rl_composite.justify = true;
		rl_composite.center = true;
		rl_composite.fill = true;
		composite.setLayout(rl_composite);

		Button btnOk = new Button(composite, SWT.NONE);
		btnOk.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				date = LocalDate.of(dateTime.getYear(), dateTime.getMonth() + 1, dateTime.getDay());
				DatePickerDialog.this.shell.close();
			}
		});
		btnOk.setLayoutData(new RowData(70, SWT.DEFAULT));
		btnOk.setText("Ok");

		Button btnCancel = new Button(composite, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				DatePickerDialog.this.shell.close();
			}
		});
		btnCancel.setLayoutData(new RowData(70, SWT.DEFAULT));
		btnCancel.setText("Cancel");

		this.dateTime.setYear(date.getYear());
		this.dateTime.setMonth(date.getMonthValue() - 1);
		this.dateTime.setDay(date.getDayOfMonth());

		shell.pack();

	}

	public void centerAround(Point p) {
		this.openingLocation = p;
	}
}
