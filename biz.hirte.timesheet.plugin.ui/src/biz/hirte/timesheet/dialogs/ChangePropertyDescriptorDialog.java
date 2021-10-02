package biz.hirte.timesheet.dialogs;

import javax.inject.Inject;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import biz.hirte.timesheet.composite.PropertyDescriptorComposite;
import biz.hirte.timesheet.composite.PropertyDescriptorComposite.PropertyDescriptorInfo;
import biz.hirte.timesheet.model.IPropertyDescriptor;
import biz.hirte.timesheet.service.ITimesheetProviderService;

public class ChangePropertyDescriptorDialog extends Dialog {

	private PropertyDescriptorComposite	pdc;

	private IPropertyDescriptor			descriptor;
	private ITimesheetProviderService	providerService;

	private String						dialogTitle	= "";

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	@Inject
	public ChangePropertyDescriptorDialog(Shell parentShell, ITimesheetProviderService providerService) {
		super(parentShell);
		this.providerService = providerService;
		this.descriptor = providerService.createEmptyPropertyDescriptor();
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		dialogTitle = "Create Descriptor";
		Composite container = (Composite) super.createDialogArea(parent);

		container.setLayout(new FillLayout());
		pdc = new PropertyDescriptorComposite(container, SWT.NONE);
		pdc.setPropertyDescriptor(descriptor);

		return container;
	}

	@Override
	protected void configureShell(Shell newShell) {
		newShell.setText(dialogTitle);
		super.configureShell(newShell);
	}

	public void setDescriptor(IPropertyDescriptor descriptor) {
		this.descriptor = descriptor;
		this.dialogTitle = "Update Descriptor";
	}

	/**
	 * Create contents of the button bar.
	 * @param parent
	 */
	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected void okPressed() {
		PropertyDescriptorInfo info = pdc.getPropertyDescritorInfo();
		providerService.updatePropertyDescriptor(this.descriptor, info.getKey(), info.getType());
		super.okPressed();
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 300);
	}

	public PropertyDescriptorComposite.PropertyDescriptorInfo getResult() {
		return this.pdc.getPropertyDescritorInfo();
	}
}
