package biz.hirte.timesheet.dialogs;

import java.util.List;

import javax.inject.Inject;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.databinding.beans.PojoProperties;
import org.eclipse.core.databinding.observable.value.IObservableValue;
import org.eclipse.e4.core.contexts.IEclipseContext;
import org.eclipse.jface.databinding.swt.WidgetProperties;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.custom.CTabItem;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

import biz.hirte.timesheet.composite.ListPropertiesComposite;
import biz.hirte.timesheet.model.IProject;
import biz.hirte.timesheet.model.IProperty;

public class ChangeProjectDialog extends TitleAreaDialog {

	private DataBindingContext		m_bindingContext;

	private Text					txtDescription;
	private Text					txtName;
	private final ProjectInfo		pi;

	private CTabItem				ctab1;
	private CTabItem				ctab2;
	private CTabItem				ctab3;

	private ListPropertiesComposite	projectPropertiesComp;
	private ListPropertiesComposite	periodDefaultPropertiesComp;

	private IEclipseContext			ctx;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	@Inject
	public ChangeProjectDialog(Shell parentShell, IProject project, IEclipseContext ctx) {
		super(parentShell);
		this.ctx = ctx;
		if (project == null) {
			this.pi = new ProjectInfo();
		} else {
			this.pi = new ProjectInfo(project);
		}
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		setMessage("Please provide the project specific information.");
		setTitle("Change Project");
		Composite area = (Composite) super.createDialogArea(parent);

		CTabFolder tabFolder = new CTabFolder(area, SWT.NONE);
		tabFolder.setLayoutData(new GridData(GridData.FILL_BOTH));
		tabFolder.setLayout(new FillLayout());

		ctab1 = new CTabItem(tabFolder, SWT.NONE);
		ctab1.setText("Basic");
		Composite container = new Composite(tabFolder, SWT.NONE);
		ctab1.setControl(container);

		container.setLayout(new FormLayout());

		CLabel lblName = new CLabel(container, SWT.NONE);
		FormData fd_lblName = new FormData();
		fd_lblName.top = new FormAttachment(0, 10);
		fd_lblName.left = new FormAttachment(0, 10);
		lblName.setLayoutData(fd_lblName);
		lblName.setText("Name");

		CLabel lblDescriptioin = new CLabel(container, SWT.NONE);
		fd_lblName.right = new FormAttachment(lblDescriptioin, 0, SWT.RIGHT);
		FormData fd_lblDescriptioin = new FormData();
		fd_lblDescriptioin.top = new FormAttachment(lblName, 6);
		fd_lblDescriptioin.left = new FormAttachment(0, 10);
		lblDescriptioin.setLayoutData(fd_lblDescriptioin);
		lblDescriptioin.setText("Description");

		txtDescription = new Text(container, SWT.BORDER | SWT.MULTI);
		FormData fd_txtDescription = new FormData();
		fd_txtDescription.left = new FormAttachment(lblDescriptioin, 16);
		fd_txtDescription.right = new FormAttachment(100, -10);
		fd_txtDescription.bottom = new FormAttachment(100, -6);
		fd_txtDescription.top = new FormAttachment(lblDescriptioin, -21);
		txtDescription.setLayoutData(fd_txtDescription);

		txtName = new Text(container, SWT.BORDER);
		FormData fd_txtName = new FormData();
		fd_txtName.top = new FormAttachment(0, 10);
		fd_txtName.left = new FormAttachment(lblName, 16);
		fd_txtName.right = new FormAttachment(100, -10);
		txtName.setLayoutData(fd_txtName);

		ctab2 = new CTabItem(tabFolder, SWT.NONE);
		ctab2.setText("Properties");
		projectPropertiesComp = new ListPropertiesComposite(tabFolder, SWT.NONE, ctx);
		ctab2.setControl(projectPropertiesComp);

		ctab3 = new CTabItem(tabFolder, SWT.NONE);
		ctab3.setText("Period Properties");
		periodDefaultPropertiesComp = new ListPropertiesComposite(tabFolder, SWT.NONE, ctx);
		ctab3.setControl(periodDefaultPropertiesComp);

		return area;
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
		m_bindingContext = initDataBindings();
	}

	/**
	 * Return the initial size of the dialog.
	 */
	@Override
	protected Point getInitialSize() {
		return new Point(485, 459);
	}

	public ProjectInfo getProjectInfo() {
		return this.pi;
	}

	public class ProjectInfo {

		String			name;
		String			description;
		List<IProperty>	projectProperties;
		List<IProperty>	periodDefaultProperties;

		ProjectInfo(IProject project) {
			this.name = project.getName();
			this.description = project.getDescription();
			this.projectProperties = (List<IProperty>) project.getProperties();
			this.periodDefaultProperties = (List<IProperty>) project.getDefaultPeriodProperties();
		}

		ProjectInfo() {}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getDescription() {
			return description;
		}

		public void setDescription(String description) {
			this.description = description;
		}

	}

	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		IObservableValue observeTextTxtDescriptionObserveWidget = WidgetProperties.text(SWT.Modify).observe(txtDescription);
		IObservableValue descriptionPiObserveValue = PojoProperties.value("description").observe(pi);
		bindingContext.bindValue(observeTextTxtDescriptionObserveWidget, descriptionPiObserveValue, null, null);
		//
		IObservableValue observeTextTxtNameObserveWidget = WidgetProperties.text(SWT.Modify).observe(txtName);
		IObservableValue namePiObserveValue = PojoProperties.value("name").observe(pi);
		bindingContext.bindValue(observeTextTxtNameObserveWidget, namePiObserveValue, null, null);
		//
		this.periodDefaultPropertiesComp.setProperties(pi.periodDefaultProperties);
		this.projectPropertiesComp.setProperties(pi.projectProperties);
		//
		return bindingContext;
	}
}
