package biz.hirte.timesheet.view;

import java.awt.image.BufferedImage;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CLabel;
import org.eclipse.swt.events.MenuAdapter;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.PaintEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeColumn;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jfree.chart.JFreeChart;

import biz.hirte.timesheet.image.ImageConverter;
import biz.hirte.timesheet.model.IPeriod;
import biz.hirte.timesheet.model.IProject;
import biz.hirte.timesheet.model.util.Durations;
import biz.hirte.timesheet.service.IChartService;
import biz.hirte.timesheet.service.impl.ChartServiceImpl;

public class TimesheetView {

	private TreeViewer				tvProjectPeriods;
	private Composite				chartComposite;

	private IChartService			chartService	= new ChartServiceImpl();
	private CLabel					lblDuration;

	private Button					btnPeriodAtT_0;
	private Button					btnPeriodAtT_1;
	private Button					btnPeriodAtT_2;

	private TimesheetViewController	controller;
	private MenuItem				mntmProjectsDelete;
	private MenuItem				mntmProjectsExport;
	private MenuItem				mntmPeriodAdd;
	private MenuItem				mntmPeriodDelete;
	private MenuItem				mntmPeriodExportSelected;
	private MenuItem				mntmPeriodMerge;
	private MenuItem				mntmPeriodCreateSeries;
	private MenuItem				mntmEditPeriod;
	private MenuItem				mntmEditProject;
	private MenuItem				mntmAddProject;

	private JFreeChart				chart;

	public TimesheetView() {}

	/**
	 * Create contents of the view part.
	 */
	@PostConstruct
	public void createControls(Composite parent, TimesheetViewController ctr) {

		controller = ctr;
		controller.setView(this);

		Composite cmp = new Composite(parent, SWT.NONE);
		cmp.setBounds(0, 0, 851, 298);
		cmp.setLayout(new FormLayout());

		Composite cmpLeft = new Composite(cmp, SWT.NONE);
		cmpLeft.setLayout(new FormLayout());
		FormData fd_cmpLeft = new FormData();
		fd_cmpLeft.width = 160;
		fd_cmpLeft.bottom = new FormAttachment(100);
		fd_cmpLeft.top = new FormAttachment(0);
		fd_cmpLeft.left = new FormAttachment(0);
		cmpLeft.setLayoutData(fd_cmpLeft);

		Composite cmpTop = new Composite(cmp, SWT.NONE);
		cmpTop.setLayout(new FormLayout());
		FormData fd_cmpTop = new FormData();
		fd_cmpTop.left = new FormAttachment(cmpLeft, 0);
		fd_cmpTop.right = new FormAttachment(100, 0);
		fd_cmpTop.height = 30;
		cmpTop.setLayoutData(fd_cmpTop);

		tvProjectPeriods = new TreeViewer(cmp, SWT.BORDER | SWT.MULTI | SWT.FULL_SELECTION);
		Tree tree = tvProjectPeriods.getTree();
		tree.addMouseListener(new MouseAdapter() {

			@Override
			public void mouseDoubleClick(MouseEvent e) {
				controller.onTreeDoubleClicked();
			}
		});
		tree.setLinesVisible(true);
		tree.setHeaderVisible(true);
		FormData fd_tree = new FormData();
		fd_tree.top = new FormAttachment(cmpTop, 0);

		btnPeriodAtT_0 = new Button(cmpTop, SWT.TOGGLE);
		FormData fd_btnNewButton = new FormData();
		fd_btnNewButton.height = 20;
		fd_btnNewButton.width = 80;
		fd_btnNewButton.top = new FormAttachment(0, 5);
		fd_btnNewButton.left = new FormAttachment(0);
		btnPeriodAtT_0.setLayoutData(fd_btnNewButton);

		btnPeriodAtT_0.setText("0");

		btnPeriodAtT_1 = new Button(cmpTop, SWT.TOGGLE);
		FormData fd_btnNewButton_1 = new FormData();
		fd_btnNewButton_1.width = 80;
		fd_btnNewButton_1.height = 20;
		fd_btnNewButton_1.top = new FormAttachment(btnPeriodAtT_0, 0, SWT.TOP);
		fd_btnNewButton_1.left = new FormAttachment(btnPeriodAtT_0, 6);
		btnPeriodAtT_1.setLayoutData(fd_btnNewButton_1);
		btnPeriodAtT_1.setText("-1");

		btnPeriodAtT_2 = new Button(cmpTop, SWT.TOGGLE);
		FormData fd_btnNewButton_2 = new FormData();
		fd_btnNewButton_2.width = 80;
		fd_btnNewButton_2.height = 20;
		fd_btnNewButton_2.top = new FormAttachment(btnPeriodAtT_0, 0, SWT.TOP);
		fd_btnNewButton_2.left = new FormAttachment(btnPeriodAtT_1, 6);
		btnPeriodAtT_2.setLayoutData(fd_btnNewButton_2);
		btnPeriodAtT_2.setText("-2");
		fd_tree.left = new FormAttachment(cmpLeft, 0);

		lblDuration = new CLabel(cmpLeft, SWT.NONE);
		lblDuration.setText("Sum: 160 hrs 45 mins");
		FormData fd_lblDuration = new FormData();
		fd_lblDuration.top = new FormAttachment(0, 6);
		fd_lblDuration.left = new FormAttachment(0, 6);
		fd_lblDuration.right = new FormAttachment(100, -6);
		fd_lblDuration.height = 20;
		lblDuration.setLayoutData(fd_lblDuration);

		chartComposite = new Composite(cmpLeft, SWT.NONE);
		chartComposite.setBackground(SWTResourceManager.getColor(SWT.COLOR_WIDGET_BACKGROUND));
		FormData fd_chartComposite = new FormData();
		fd_chartComposite.top = new FormAttachment(0, 30);
		fd_chartComposite.left = new FormAttachment(0, 5);
		fd_chartComposite.bottom = new FormAttachment(100, -6);
		fd_chartComposite.right = new FormAttachment(0, 155);
		chartComposite.setLayoutData(fd_chartComposite);
		chartComposite.addPaintListener(this::paintChart);

		fd_tree.right = new FormAttachment(100, -6);
		fd_tree.bottom = new FormAttachment(100, -6);

		tree.setLayoutData(fd_tree);

		TreeColumn trclmnProject = new TreeColumn(tree, SWT.NONE);
		trclmnProject.setWidth(220);
		trclmnProject.setText("Project");

		TreeColumn trclmnDate = new TreeColumn(tree, SWT.NONE);
		trclmnDate.setWidth(80);
		trclmnDate.setText("Date");

		TreeColumn trclmnFrom = new TreeColumn(tree, SWT.NONE);
		trclmnFrom.setWidth(60);
		trclmnFrom.setText("Begin");

		TreeColumn trclmnUnti = new TreeColumn(tree, SWT.NONE);
		trclmnUnti.setWidth(60);
		trclmnUnti.setText("End");

		TreeColumn trclmnDuration = new TreeColumn(tree, SWT.NONE);
		trclmnDuration.setWidth(60);
		trclmnDuration.setText("Duration");

		TreeColumn trclmnEffectiveBreak = new TreeColumn(tree, SWT.NONE);
		trclmnEffectiveBreak.setWidth(60);
		trclmnEffectiveBreak.setText("Break");

		TreeColumn trclmnEffectiveDuration = new TreeColumn(tree, SWT.NONE);
		trclmnEffectiveDuration.setToolTipText("Effevtive Duration = Duration - Break");
		trclmnEffectiveDuration.setWidth(60);
		trclmnEffectiveDuration.setText("Effective Duration");

		TreeColumn trclmnComment = new TreeColumn(tree, SWT.NONE);
		trclmnComment.setWidth(350);
		trclmnComment.setText("Comment");

		Menu menu = new Menu(tree);
		menu.addMenuListener(new MenuAdapter() {

			@Override
			public void menuShown(MenuEvent e) {
				controller.onContextMenuBeforeShow();
			}
		});
		tree.setMenu(menu);

		MenuItem mntmProjects = new MenuItem(menu, SWT.CASCADE);
		mntmProjects.setText("Projects");

		Menu submenuProjects = new Menu(mntmProjects);
		mntmProjects.setMenu(submenuProjects);

		mntmAddProject = new MenuItem(submenuProjects, SWT.NONE);
		mntmAddProject.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				controller.onAddProjectClicked();
			}
		});
		mntmAddProject.setText("Add Project...");

		mntmEditProject = new MenuItem(submenuProjects, SWT.NONE);
		mntmEditProject.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				controller.onEditSelectedProjectClicked();
			}
		});
		mntmEditProject.setText("Edit Project...");

		mntmProjectsDelete = new MenuItem(submenuProjects, SWT.NONE);
		mntmProjectsDelete.setEnabled(false);
		mntmProjectsDelete.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				TimesheetView.this.controller.deleteSelectedProjects(parent.getShell());
			}
		});
		mntmProjectsDelete.setText("Delete selected");

		mntmProjectsExport = new MenuItem(submenuProjects, SWT.NONE);
		mntmProjectsExport.setEnabled(false);
		mntmProjectsExport.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				controller.onExportSelectedProjectsClicked(e.display.getActiveShell());
			}
		});
		mntmProjectsExport.setText("Export selected");

		MenuItem menuItem = new MenuItem(menu, SWT.SEPARATOR);
		menuItem.setText("Projects");

		MenuItem mntmPeriods = new MenuItem(menu, SWT.CASCADE);
		mntmPeriods.setText("Periods");

		Menu submenuPeriods = new Menu(mntmPeriods);
		mntmPeriods.setMenu(submenuPeriods);

		mntmPeriodAdd = new MenuItem(submenuPeriods, SWT.NONE);
		mntmPeriodAdd.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				controller.onAddPeriodClicked();
			}
		});
		mntmPeriodAdd.setText("Add Period...");

		mntmPeriodCreateSeries = new MenuItem(submenuPeriods, SWT.NONE);
		mntmPeriodCreateSeries.setEnabled(false);
		mntmPeriodCreateSeries.setText("Create Series...");
		mntmPeriodCreateSeries.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				IStructuredSelection selection = (IStructuredSelection) tvProjectPeriods.getSelection();
				if (!selection.isEmpty() && selection.getFirstElement() instanceof IPeriod) {
					TimesheetView.this.controller.onCreateSeries(parent.getShell(), (IPeriod) selection.getFirstElement());
				}
			}

		});

		mntmEditPeriod = new MenuItem(submenuPeriods, SWT.NONE);
		mntmEditPeriod.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				controller.onEditSelectedPeriod();
			}

		});
		mntmEditPeriod.setText("Edit Period...");

		mntmPeriodMerge = new MenuItem(submenuPeriods, SWT.NONE);
		mntmPeriodMerge.setText("Merge selected...");
		mntmPeriodMerge.setEnabled(false);

		mntmPeriodDelete = new MenuItem(submenuPeriods, SWT.NONE);
		mntmPeriodDelete.setEnabled(false);
		mntmPeriodDelete.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				TimesheetView.this.controller.onDeleteSelectedPeriods(parent.getShell());
			}
		});
		mntmPeriodDelete.setText("Delete selected");

		mntmPeriodExportSelected = new MenuItem(submenuPeriods, SWT.NONE);
		mntmPeriodExportSelected.setEnabled(false);
		mntmPeriodExportSelected.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				TimesheetView.this.controller.onExportSelectedPeriods(parent.getShell());
			}
		});
		mntmPeriodExportSelected.setText("Export selected...");

		tvProjectPeriods.setContentProvider(new ProjectPeriodTreeContentProvider());
		tvProjectPeriods.setLabelProvider(new ProjectPeriodTreeLabelProvider());

		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MMMM");
		YearMonth yearMonths_0 = YearMonth.now();

		this.btnPeriodAtT_0.setText(yearMonths_0.format(dtf));
		this.btnPeriodAtT_0.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);

				if (btnPeriodAtT_0.getSelection()) {
					controller.addYearMonthFilter(yearMonths_0);
				} else {
					controller.removeYearMonthFilter(yearMonths_0);
				}

				controller.updateView();
			}

		});

		YearMonth yearMonths_1 = yearMonths_0.minusMonths(1);
		this.btnPeriodAtT_1.setText(yearMonths_1.format(dtf));
		this.btnPeriodAtT_1.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);

				if (btnPeriodAtT_1.getSelection()) {
					controller.addYearMonthFilter(yearMonths_1);
				} else {
					controller.removeYearMonthFilter(yearMonths_1);
				}

				controller.updateView();
			}

		});

		YearMonth yearMonths_2 = yearMonths_1.minusMonths(1);
		this.btnPeriodAtT_2.setText(yearMonths_2.format(dtf));
		this.btnPeriodAtT_2.addSelectionListener(new SelectionAdapter() {

			@Override
			public void widgetSelected(SelectionEvent e) {
				super.widgetSelected(e);

				if (btnPeriodAtT_2.getSelection()) {
					controller.addYearMonthFilter(yearMonths_2);
				} else {
					controller.removeYearMonthFilter(yearMonths_2);
				}

				controller.updateView();
			}

		});

		controller.addYearMonthFilter(yearMonths_0);
		this.btnPeriodAtT_0.setSelection(true);

		this.mntmPeriodAdd.setEnabled(true);

		controller.updateView();
	}

	void updateInputs(List<? extends IProject> allProjects) {
		Object[] expandedElements = tvProjectPeriods.getExpandedElements();
		tvProjectPeriods.setInput(allProjects);
		tvProjectPeriods.setExpandedElements(expandedElements);
		updateChart(allProjects);
		updateTotalDuration(allProjects);
		updateFastSelectionButton();
	}

	private void updateFastSelectionButton() {

	}

	private void updateTotalDuration(List<? extends IProject> allProjects) {
		int totalDuration = (int) Durations.calculateOverallProjectsDuration(allProjects);
		int hrs = (totalDuration / 60);
		int mins = totalDuration - (hrs * 60);
		lblDuration.setText(String.format("Total: %d hrs %d mins", hrs, mins));
	}

	private void updateChart(List<? extends IProject> projects) {
		chart = this.chartService.createChart(projects);
		this.chartComposite.update();
		this.chartComposite.redraw();
		this.chartComposite.setFocus();
	}

	private void paintChart(PaintEvent e) {
		GC gc = e.gc;
		Rectangle bounds = this.chartComposite.getBounds();
		BufferedImage bufferedImage = this.chart.createBufferedImage(bounds.width, (int) (bounds.width * 1.6f));
		ImageData swtImageData = ImageConverter.convertToSWT(bufferedImage);
		gc.drawImage(new Image(e.display, swtImageData), 0, 0);
	}

	// === Project related

	void enableAddProject(boolean enable) {
		this.mntmAddProject.setEnabled(enable);
	}

	void enableEditProject(boolean enable) {
		this.mntmEditProject.setEnabled(enable);
	}

	void enableDeleteProjects(boolean enable) {
		this.mntmProjectsDelete.setEnabled(enable);
	}

	void enableExportProjects(boolean enable) {
		this.mntmProjectsExport.setEnabled(enable);
	}

	// === Period related

	void enableAddPeriod(boolean enable) {
		this.mntmPeriodAdd.setEnabled(enable);
	}

	void enableEditPeriod(boolean enable) {
		this.mntmEditPeriod.setEnabled(enable);
	}

	void enableDeletePeriods(boolean enable) {
		this.mntmPeriodDelete.setEnabled(enable);
	}

	void enableMergePeriods(boolean enable) {
		this.mntmPeriodMerge.setEnabled(enable);
	}

	void enableCreateSeries(boolean enable) {
		this.mntmPeriodCreateSeries.setEnabled(enable);
	}

	void enableExportPeriods(boolean enable) {
		this.mntmPeriodExportSelected.setEnabled(enable);
	}

	Object[] getTreeSelections() {
		IStructuredSelection selection = (IStructuredSelection) this.tvProjectPeriods.getSelection();
		return selection.toArray();
	}

	@PreDestroy
	public void dispose() {}

	@Focus
	public void setFocus() {}
}
