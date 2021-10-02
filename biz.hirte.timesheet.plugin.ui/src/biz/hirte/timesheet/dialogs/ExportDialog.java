package biz.hirte.timesheet.dialogs;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import biz.hirte.swt.controls.DatePicker;
import biz.hirte.timesheet.extension.ExporterDescriptor;
import biz.hirte.timesheet.extension.ExporterLocator;

public class ExportDialog extends TitleAreaDialog {

	private ListViewer					listViewer;

	private LocalDate					from;
	private LocalDate					to;

	private static DateTimeFormatter	dtf					= DateTimeFormatter.ofPattern("dd.MM.yyyy");
	private CLabel						lblFrom;

	private ExportInformation			exportInformation	= null;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public ExportDialog(Shell parentShell, LocalDate from, LocalDate to) {
		super(parentShell);
		this.from = from;
		this.to = to;
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setMessage("Select the exporter which will export the timesheet");
		setTitle("Export timesheet");
		Composite area = (Composite) super.createDialogArea(parent);
		Composite container = new Composite(area, SWT.NONE);
		container.setLayout(new FormLayout());
		container.setLayoutData(new GridData(GridData.FILL_BOTH));

		listViewer = new ListViewer(container, SWT.BORDER | SWT.V_SCROLL);
		List list = listViewer.getList();
		FormData fd_list = new FormData();
		fd_list.left = new FormAttachment(0, 6);
		fd_list.right = new FormAttachment(100, -6);
		fd_list.bottom = new FormAttachment(100, -6);
		list.setLayoutData(fd_list);

		lblFrom = new CLabel(container, SWT.NONE);
		fd_list.top = new FormAttachment(lblFrom, 6);
		FormData fd_lblFrom = new FormData();
		fd_lblFrom.width = 40;
		fd_lblFrom.top = new FormAttachment(0, 6);
		fd_lblFrom.left = new FormAttachment(0, 6);
		lblFrom.setLayoutData(fd_lblFrom);
		lblFrom.setText("From:");

		CLabel lblUntil = new CLabel(container, SWT.NONE);
		FormData fd_lblUntil = new FormData();
		fd_lblUntil.bottom = new FormAttachment(list, -6);
		fd_lblUntil.width = 40;
		lblUntil.setLayoutData(fd_lblUntil);
		lblUntil.setText("Until:");

		DatePicker dpStart = new DatePicker(container, SWT.NONE);
		dpStart.setDate(from);
		FormData fd_dpStart = new FormData();
		fd_dpStart.width = 120;
		fd_dpStart.top = new FormAttachment(lblFrom, 0, SWT.TOP);
		fd_dpStart.left = new FormAttachment(lblFrom, 6);
		dpStart.setLayoutData(fd_dpStart);

		DatePicker dpEnd = new DatePicker(container, SWT.NONE);
		dpEnd.setDate(this.to);
		fd_lblUntil.right = new FormAttachment(dpEnd, -6);
		FormData fd_dpEnd = new FormData();
		fd_dpEnd.width = 120;
		fd_dpEnd.bottom = new FormAttachment(list, -6);
		fd_dpEnd.right = new FormAttachment(100, -10);
		dpEnd.setLayoutData(fd_dpEnd);

		listViewer.setContentProvider(ArrayContentProvider.getInstance());
		listViewer.setLabelProvider(new ILabelProvider() {

			@Override
			public void addListener(ILabelProviderListener listener) {}

			@Override
			public void dispose() {}

			@Override
			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			@Override
			public void removeListener(ILabelProviderListener listener) {}

			@Override
			public Image getImage(Object element) {
				return null;
			}

			@Override
			public String getText(Object element) {
				if (element instanceof ExporterDescriptor) {
					ExporterDescriptor ed = (ExporterDescriptor) element;
					return String.format("%s - %s", ed.getName(), ed.getDescription());
				}
				return element.getClass().getName();
			}

		});

		dpStart.addValueChangeListener(ld -> {
			this.from = ld;
		});
		dpEnd.addValueChangeListener(ld -> {
			this.to = ld;
		});

		updateInputs();
		updateTextFields();

		return area;
	}

	private void updateTextFields() {}

	private void updateInputs() {
		listViewer.setInput(ExporterLocator.INSTANCE.getExporterDefinitions());
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

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(428, 454);
	}

	@Override
	protected void buttonPressed(int buttonId) {

		if (buttonId == Dialog.OK) {

			if (collectExportInformation()) {
				super.buttonPressed(buttonId);
			}

		} else {

			exportInformation = null;
			super.buttonPressed(buttonId);

		}
	}

	private boolean collectExportInformation() {

		IStructuredSelection selection = (IStructuredSelection) this.listViewer.getSelection();
		if (selection.isEmpty()) {
			return false;
		}

		exportInformation = new ExportInformation();
		exportInformation.from = this.from;
		exportInformation.to = this.to;
		exportInformation.exporter = (ExporterDescriptor) selection.getFirstElement();

		return true;
	}

	public ExportInformation getExportInformation() {
		return exportInformation;
	}

	public class ExportInformation {

		LocalDate			from;
		LocalDate			to;
		ExporterDescriptor	exporter;

		public LocalDate getFrom() {
			return from;
		}

		public LocalDate getTo() {
			return to;
		}

		public ExporterDescriptor getExporter() {
			return exporter;
		}

	}
}
