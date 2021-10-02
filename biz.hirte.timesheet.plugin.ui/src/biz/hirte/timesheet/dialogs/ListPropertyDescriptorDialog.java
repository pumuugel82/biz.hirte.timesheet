package biz.hirte.timesheet.dialogs;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

import biz.hirte.timesheet.composite.ListPropertyDescriptorComposite;
import biz.hirte.timesheet.model.IPropertyDescriptor;
import biz.hirte.timesheet.service.ITimesheetProviderService;

public class ListPropertyDescriptorDialog extends Dialog {

	private ListPropertyDescriptorComposite	lpd;
	private ITimesheetProviderService		providerService;
	private IEclipseContext					context;
	private IPropertyDescriptor[]			selections	= new IPropertyDescriptor[0];

	/**
	 * Create the dialog.
	 * @param parentShell
	 */
	@Inject
	public ListPropertyDescriptorDialog(Shell parentShell, IEclipseContext context, ITimesheetProviderService providerService) {
		super(parentShell);
		this.context = context;
		this.providerService = providerService;
	}

	/**
	 * Create contents of the dialog.
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);

		container.setLayout(new FillLayout());

		IEclipseContext childCtx = context.createChild();
		childCtx.set(Composite.class, container);

		this.lpd = ContextInjectionFactory.make(ListPropertyDescriptorComposite.class, childCtx);
		this.lpd.setPropertyDescriptorList(providerService.getAllPropertyDescriptors());

		return container;
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
		IStructuredSelection selection = this.lpd.getSelection();
		selections = (IPropertyDescriptor[]) selection.toArray();
		super.okPressed();
	}

	@Override
	protected void cancelPressed() {
		selections = new IPropertyDescriptor[0];
		super.cancelPressed();
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(450, 376);
	}

	public IPropertyDescriptor[] getResult() {
		return selections;
	}

}
