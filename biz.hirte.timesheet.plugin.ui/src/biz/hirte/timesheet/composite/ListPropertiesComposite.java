package biz.hirte.timesheet.composite;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
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
import org.eclipse.swt.widgets.TableColumn;

import biz.hirte.timesheet.model.IProperty;

/**
 * Lays out a table with two columns to present Properties with key and value.
 * Has some Button on the right side to manage the entries in the table.
 * 
 * SWT-BUILDER CREATED THIS COMPOSITE.
 * 
 * @author hirte
 */
public class ListPropertiesComposite extends Composite {

	private Table			table;

	private IEclipseContext	ctx;
	private List<IProperty>	properties	= new ArrayList<>();

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public ListPropertiesComposite(Composite parent, int style, IEclipseContext ctx) {
		super(parent, style);
		setLayout(new FormLayout());
		this.ctx = ctx;

		TableViewer tableViewer = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION);
		table = tableViewer.getTable();
		FormData fd_table = new FormData();
		fd_table.bottom = new FormAttachment(100, -10);
		fd_table.top = new FormAttachment(0, 10);
		fd_table.left = new FormAttachment(0, 10);
		table.setLayoutData(fd_table);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);

		TableColumn tblclmnKey = new TableColumn(table, SWT.NONE);
		tblclmnKey.setWidth(200);
		tblclmnKey.setText("Key");

		TableColumn tblclmnValue = new TableColumn(table, SWT.NONE);
		tblclmnValue.setWidth(150);
		tblclmnValue.setText("Value");

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
		fd_btnDelete.width = 80;
		fd_btnDelete.top = new FormAttachment(btnAdd, 10);
		fd_btnDelete.right = new FormAttachment(100, -10);
		btnDelete.setLayoutData(fd_btnDelete);
		btnDelete.setText("Delete");

		tableViewer.setContentProvider(ArrayContentProvider.getInstance());
		tableViewer.setLabelProvider(new PropertiesTableLabelProvider());

		btnAdd.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {

				super.widgetSelected(e);
			}

		});

	}

	public List<IProperty> getProperties() {
		return properties;
	}

	public void setProperties(List<IProperty> properties) {
		this.properties = properties;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	/**
	 * LabelProvider for the Properties TableViewer.
	 * 
	 * @author hirte
	 *
	 */
	private static final class PropertiesTableLabelProvider implements ITableLabelProvider {

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

			IProperty prop = (IProperty) element;

			if (columnIndex == 0) {
				return prop.getDescriptor().getKey();
			}

			if (columnIndex == 1) {
				return prop.getString();
			}

			return null;
		}

		@Override
		public Image getColumnImage(Object element, int columnIndex) {
			return null;
		}
	}
}
