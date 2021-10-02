package biz.hirte.timesheet.extension;

import biz.hirte.timesheet.service.ITimesheetExportService;

/**
 * A Descriptor around an instantiated Exporter for the Exporter -
 * ExtensionPoint. The service object provided by the ExtensionPoint can be
 * accessed via {@link ProviderDescriptor#getService()}.
 * 
 * @author hirte
 *
 */
public class ExporterDescriptor {

	/** Name of this Exporter. */
	private String					name;

	/** Description for this Exporter. */
	private String					description;

	/** An instance of the Exporter */
	private ITimesheetExportService	exportService;

	/**
	 * Standard constructor for ths class.
	 * 
	 * @param name
	 * @param description
	 * @param exportService
	 */
	public ExporterDescriptor(String name, String description, ITimesheetExportService exportService) {
		super();
		this.name = name;
		this.description = description;
		this.exportService = exportService;
	}

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

	public ITimesheetExportService getExportService() {
		return exportService;
	}

	public void setExportService(ITimesheetExportService exportService) {
		this.exportService = exportService;
	}

}
