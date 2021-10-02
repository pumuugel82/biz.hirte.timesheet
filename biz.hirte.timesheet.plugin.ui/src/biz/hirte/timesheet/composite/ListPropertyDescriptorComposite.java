package biz.hirte.timesheet.composite;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.e4.core.contexts.ContextInjectionFactory;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;

import biz.hirte.timesheet.dialogs.ChangePropertyDescriptorDialog;
import biz.hirte.timesheet.model.IPropertyDescriptor;
import biz.hirte.timesheet.service.ITimesheetProviderService;

/**
 * Lays out a table with two columns to present PropertyDescriptor with key and value.
 * Has some Button on the right side to manage the entries in the table.
 * 
 * SWT-BUILDER CREATED THIS COMPOSITE.
 * 
 * @author hirte
 *
 */
public class ListPropertyDescriptorComposite extends Composite {

	private IEclipseContext	context;
	private Table			table;
	private TableViewer		tvDescriptors;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	@Inject
	public ListPropertyDescriptorComposite(Composite parent, IEclipseContext context) {
		super(parent, SWT.NONE);
		this.context = context;
		setLayout(new FormLayout());

		tvDescriptors = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION);
		table = tvDescriptors.getTable();
		FormData fd_table = new FormData();
		fd_table.bottom = new FormAttachment(100, -10);
		fd_table.top = new FormAttachment(0, 10);
		fd_table.left = new FormAttachment(0, 10);
		table.setLayoutData(fd_table);

		Button btnAdd = new Button(this, SWT.NONE);
		fd_table.right = new FormAttachment(btnAdd, -10);
		FormData fd_btnAdd = new FormData();
		fd_btnAdd.width = 80;
		fd_btnAdd.top = new FormAttachment(0, 10);
		fd_btnAdd.right = new FormAttachment(100, -10);
		btnAdd.setLayoutData(fd_btnAdd);
		btnAdd.setText("Add");

		Button btnDelete = new Button(this, SWT.NONE);
		FormData fd_btnDelete = new FormData();
		fd_btnDelete.top = new FormAttachment(btnAdd, 10);
		fd_btnDelete.width = 80;
		fd_btnDelete.right = new FormAttachment(btnAdd, 0, SWT.RIGHT);
		btnDelete.setLayoutData(fd_btnDelete);
		btnDelete.setText("Delete");

		tvDescriptors.setContentProvider(ArrayContentProvider.getInstance());
		tvDescriptors.setLabelProvider(new ITableLabelProvider() {

			@Override
			public void removeListener(ILabelProviderListener listener) {}

			@Override
			public boolean isLabelProperty(Object element, String property) {
				return false;
			}

			@Override
			public void dispose() {}

			@Override
			public void addListener(ILabelProviderListener listener) {}

			@Override
			public String getColumnText(Object element, int columnIndex) {

				IPropertyDescriptor desc = (IPropertyDescriptor) element;

				if (columnIndex == 0) {
					return desc.getKey();
				}

				if (columnIndex == 1) {
					return desc.getValueTypeName().name();
				}

				return null;
			}

			@Override
			public Image getColumnImage(Object element, int columnIndex) {
				return null;
			}
		});

		btnAdd.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				btnAddClicked();
			}

		});

		btnDelete.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				btnDeleteClicked();
			}

		});

	}

	private void btnAddClicked() {

		ChangePropertyDescriptorDialog dlg = ContextInjectionFactory.make(ChangePropertyDescriptorDialog.class, context);
		if (dlg.open() == Window.OK) {
			reloadInput();
		}
	}

	private void reloadInput() {
		ITimesheetProviderService iTimesheetProviderService = context.get(ITimesheetProviderService.class);
		List<? extends IPropertyDescriptor> descriptors = iTimesheetProviderService.getAllPropertyDescriptors();
	}

	private void btnDeleteClicked() {

	}

	public void setPropertyDescriptorList(List<? extends IPropertyDescriptor> descriptorList) {
		this.tvDescriptors.setInput(descriptorList);
	}

	public IStructuredSelection getSelection() {
		return this.tvDescriptors.getStructuredSelection();
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
