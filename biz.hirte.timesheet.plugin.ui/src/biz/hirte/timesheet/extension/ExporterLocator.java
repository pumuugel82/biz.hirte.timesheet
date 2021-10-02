package biz.hirte.timesheet.extension;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.IExtensionRegistry;
import org.eclipse.core.runtime.RegistryFactory;

import biz.hirte.timesheet.Activator;
import biz.hirte.timesheet.service.ITimesheetExportService;

/**
 * Locator for Exporter. It reads the the Extensions provided by other plugins
 * for the Exporter - ExtensionPoint.
 * 
 * @author hirtech
 *
 */
public class ExporterLocator {

	/**
	 * Singleton - ready to use.
	 */
	public static final ExporterLocator INSTANCE = new ExporterLocator();

	/**
	 * Full qualified name of the "exporter" - Extension Point
	 */
	public static final String EXPORTER_EXTENSION_POINT = Activator.PLUGIN_ID + ".exporter";

	/**
	 * Attribute name for the provides class in the extension point.
	 */
	private static final String EXT_ATTR_CLAZZ = "clazz";

	/**
	 * Attribute name for the description in the extension point.
	 */
	private static final String EXT_ATTR_DESCRIPTION2 = "description";

	/**
	 * Attribute name for the displayName in the extension point.
	 */
	private static final String EXT_ATTR_DISPLAY_NAME = "displayName";

	/**
	 * Looks for Extension for the Exporter Extension Point and returns each of
	 * them as {@link ExporterDescriptor}.
	 * 
	 * @return
	 */
	public List<ExporterDescriptor> getExporterDefinitions() {

		/* List containing the result */
		List<ExporterDescriptor> ret = new ArrayList<ExporterDescriptor>();

		/* ExtensionRegistry that maintains all Extensions */
		IExtensionRegistry extensionRegistry = RegistryFactory.getRegistry();
		if (extensionRegistry == null) {
			extensionRegistry = RegistryFactory.createRegistry(null, null, null);
		}

		/* Get the ExtensionPoint from the Registry */
		IExtensionPoint exporterEP = extensionRegistry.getExtensionPoint(EXPORTER_EXTENSION_POINT);

		/* Prevent NullPointerExceptions */
		if (exporterEP == null) {
			throw new IllegalStateException(String.format("ExtensionPoint %s was not found.", EXPORTER_EXTENSION_POINT));
		}

		/* Check if any Extension exists at all */
		IExtension[] extensions = exporterEP.getExtensions();
		if (extensions == null) {
			/* No extensions found. Return empty List */
			return ret;
		}

		/*
		 * Each of the found Extension must be converted into the
		 * ExporterDescriptor format.
		 */
		for (IExtension iExtension : extensions) {
			try {
				ret.addAll(toExportDefinitions(iExtension));
			}
			catch (CoreException e) {
				e.printStackTrace();
			}
		}

		return ret;
	}

	/**
	 * Converts one Extension into an {@link ExporterDescriptor}. Since the
	 * Extension may provide several Exporter, the converter method returns a
	 * list of ExporterDescriptor for one Extension.
	 * 
	 * @param iExtension
	 * @return
	 * @throws CoreException
	 */
	private List<ExporterDescriptor> toExportDefinitions(IExtension iExtension) throws CoreException {

		/* List containing the result */
		List<ExporterDescriptor> ret = new ArrayList<ExporterDescriptor>();

		/* Get all the configured elements */
		IConfigurationElement[] configurationElements = iExtension.getConfigurationElements();

		/* Check that no mis-configuration causes NullPointerException */
		if (configurationElements == null) {
			return ret;
		}

		/* Each IConfigurationElement is expected to be a configured Exporter */
		for (IConfigurationElement iConfigurationElement : configurationElements) {

			String displayName = iConfigurationElement.getAttribute(EXT_ATTR_DISPLAY_NAME);
			String description = iConfigurationElement.getAttribute(EXT_ATTR_DESCRIPTION2);

			/*
			 * No explicit instanceof test needed IMHO.
			 */
			Object srvRef = iConfigurationElement.createExecutableExtension(EXT_ATTR_CLAZZ);

			/* Throw Exception as early as possible. */
			if (srvRef == null) {
				/*
				 * But may not ever happen since createExecutableExtension will
				 * fail in this case before
				 */
				throw new IllegalStateException(String.format("No service could be instantiated for %s.", EXPORTER_EXTENSION_POINT));
			}

			ret.add(new ExporterDescriptor(displayName, description, (ITimesheetExportService) srvRef));

		}

		return ret;
	}
}
