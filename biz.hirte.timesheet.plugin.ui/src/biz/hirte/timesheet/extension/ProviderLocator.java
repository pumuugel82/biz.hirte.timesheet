package biz.hirte.timesheet.extension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.RegistryFactory;

import biz.hirte.timesheet.Activator;
import biz.hirte.timesheet.service.ITimesheetProviderService;

/**
 * Locator for Provider. It reads the the Extensions provided by other plugins
 * for the Provider - ExtensionPoint.
 * 
 * @author hirtech
 *
 */
public class ProviderLocator {

	public static final String PROVIDER_EXTENSION_POINT = Activator.PLUGIN_ID + ".provider";

	public static final ProviderLocator INSTANCE = new ProviderLocator();

	/**
	 * 
	 * @return
	 */
	public List<ProviderDescriptor> getProviderDefinitions() {

		List<ProviderDescriptor> ret = new ArrayList<ProviderDescriptor>();

		IExtensionRegistry extensionRegistry = RegistryFactory.getRegistry();
		if (extensionRegistry == null) {
			extensionRegistry = RegistryFactory.createRegistry(null, null, null);
		}

		IExtensionPoint exporterEP = extensionRegistry.getExtensionPoint(PROVIDER_EXTENSION_POINT);
		if (exporterEP == null) {
			throw new IllegalStateException(String.format("ExtensionPoint %s was not found.", PROVIDER_EXTENSION_POINT));
		}

		IExtension[] extensions = exporterEP.getExtensions();
		if (extensions == null) {
			return Collections.emptyList();
		}

		for (IExtension iExtension : extensions) {
			try {
				ret.addAll(getProviderDefinitions(iExtension));
			}
			catch (CoreException e) {
				e.printStackTrace();
			}
		}

		return ret;
	}

	/**
	 * 
	 * @param iExtension
	 * @return
	 * @throws CoreException
	 */
	private List<ProviderDescriptor> getProviderDefinitions(IExtension iExtension) throws CoreException {

		List<ProviderDescriptor> ret = new ArrayList<ProviderDescriptor>();

		IConfigurationElement[] configurationElements = iExtension.getConfigurationElements();
		for (IConfigurationElement iConfigurationElement : configurationElements) {

			String displayName = iConfigurationElement.getAttribute("displayName");
			String description = iConfigurationElement.getAttribute("description");

			/*
			 * No explicit instanceof test needed IMHO.
			 */
			Object srvRef = iConfigurationElement.createExecutableExtension("clazz");
			ret.add(new ProviderDescriptor(displayName, description, (ITimesheetProviderService) srvRef));

		}

		return ret;
	}
}
