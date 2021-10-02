package biz.hirte.timesheet.composite;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Text;

import biz.hirte.timesheet.model.IPropertyDescriptor;
import biz.hirte.timesheet.model.ValueTypeName;

public class PropertyDescriptorComposite extends Composite {

	private DataBindingContext		m_bindingContext;

	private PropertyDescriptorInfo	propertyDescritorInfo;
	private Combo					cmbType;
	private Text					txtKey;

	/**
	 * Create the composite.
	 * @param parent
	 * @param style
	 */
	public PropertyDescriptorComposite(Composite parent, int style) {
		super(parent, style);
		setLayout(new FormLayout());

		CLabel lblKey = new CLabel(this, SWT.NONE);
		FormData fd_lblKey = new FormData();
		fd_lblKey.width = 80;
		fd_lblKey.top = new FormAttachment(0, 10);
		fd_lblKey.left = new FormAttachment(0, 10);
		lblKey.setLayoutData(fd_lblKey);
		lblKey.setText("Key");

		ComboViewer cmbvViewer = new ComboViewer(this, SWT.NONE);
		cmbType = cmbvViewer.getCombo();
		FormData fd_cmbType = new FormData();
		fd_cmbType.right = new FormAttachment(100, -10);
		cmbType.setLayoutData(fd_cmbType);

		CLabel lblType = new CLabel(this, SWT.NONE);
		fd_cmbType.top = new FormAttachment(lblType, 0, SWT.TOP);
		fd_cmbType.left = new FormAttachment(lblType, 10);
		FormData fd_lblType = new FormData();
		fd_lblType.width = 80;
		fd_lblType.top = new FormAttachment(lblKey, 10);
		fd_lblType.left = new FormAttachment(0, 10);
		lblType.setLayoutData(fd_lblType);
		lblType.setText("Type");

		Text text = new Text(this, SWT.BORDER);
		FormData fd_text = new FormData();
		fd_text.right = new FormAttachment(100, -10);
		fd_text.top = new FormAttachment(lblKey, 0, SWT.TOP);
		fd_text.left = new FormAttachment(lblKey, 10);
		text.setLayoutData(fd_text);
		txtKey = text;
		FormData fd_txtKey = new FormData();
		fd_txtKey.right = new FormAttachment(100, -10);
		fd_txtKey.top = new FormAttachment(0, 10);
		fd_txtKey.left = new FormAttachment(lblKey, 10);
		txtKey.setLayoutData(fd_txtKey);

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	public void setPropertyDescriptor(IPropertyDescriptor propertyDescriptor) {
		if (m_bindingContext != null) {
			m_bindingContext.dispose();
		}

		this.propertyDescritorInfo = new PropertyDescriptorInfo(propertyDescriptor.getKey(), propertyDescriptor.getValueTypeName());
		m_bindingContext = initDataBindings();
	}

	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeTextTxtKeyObserveWidget = WidgetProperties.text(SWT.Modify).observe(txtKey);
		IObservableValue keyPropertyDescritorObserveValue = PojoProperties.value("key").observe(propertyDescritorInfo);
		bindingContext.bindValue(observeTextTxtKeyObserveWidget, keyPropertyDescritorObserveValue, null, null);
		//
		IObservableValue observeTextCmbTypeObserveWidget = WidgetProperties.text().observe(cmbType);
		IObservableValue valueTypeNamePropertyDescritorObserveValue = PojoProperties.value("type").observe(propertyDescritorInfo);
		bindingContext.bindValue(observeTextCmbTypeObserveWidget, valueTypeNamePropertyDescritorObserveValue, null, null);
		//
		return bindingContext;
	}

	public PropertyDescriptorInfo getPropertyDescritorInfo() {
		return propertyDescritorInfo;
	}

	public static class PropertyDescriptorInfo {

		String			key;
		ValueTypeName	type;

		public PropertyDescriptorInfo(String key, ValueTypeName type) {
			super();
			this.key = key;
			this.type = type;
		}

		public String getKey() {
			return key;
		}

		public ValueTypeName getType() {
			return type;
		}

	}
}
