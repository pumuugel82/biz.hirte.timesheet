package biz.hirte.timesheet.extension;

import biz.hirte.timesheet.service.ITimesheetProviderService;

/**
 * A Descriptor around an instantiated Provider for the Provider -
 * ExtensionPoint. The service object provided by the ExtensionPoint can be
 * accessed via {@link ProviderDescriptor#getService()}.
 * 
 * @author hirte
 *
 */
public class ProviderDescriptor {

	/** Name for this provider in a human readable fashion */
	private String				displayName;

	/** Description for this provider */
	private String				description;

	/** Instance of this service */
	private ITimesheetProviderService	service;

	/**
	 * Standard constructor for this class.
	 * 
	 * @param displayName
	 *            human readable name
	 * @param description
	 *            description for this provider
	 * @param srvRef
	 *            instance of the service object
	 */
	public ProviderDescriptor(String displayName, String description, ITimesheetProviderService srvRef) {
		this.displayName = displayName;
		this.description = description;
		this.service = srvRef;
	}

	public ITimesheetProviderService getService() {
		return service;
	}

	public void setService(ITimesheetProviderService service) {
		this.service = service;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
