package biz.hirte.timesheet.service.contextfunction;

import java.util.List;

import org.eclipse.e4.core.contexts.ContextFunction;
import org.eclipse.e4.core.contexts.IEclipseContext;

import biz.hirte.timesheet.extension.ProviderDescriptor;
import biz.hirte.timesheet.extension.ProviderLocator;
import biz.hirte.timesheet.service.ITimesheetProviderService;

public class TimesheetProviderContextFunction extends ContextFunction {

	@Override
	public Object compute(IEclipseContext context, String contextKey) {

		List<ProviderDescriptor> providerDefinitions = ProviderLocator.INSTANCE.getProviderDefinitions();

		if (providerDefinitions.isEmpty()) {
			throw new IllegalStateException(String.format("No provider for timesheet found. At least one provider has to be present at the extension point %s",
					ProviderLocator.PROVIDER_EXTENSION_POINT));
		}

		// HIER DEN RICHTIGEN SERVICE AUSWAEHLEN, ANHAND DER KONFIGURATION IN ?
		context.set(ITimesheetProviderService.class, providerDefinitions.get(0).getService());

		return providerDefinitions.get(0).getService();
	}

}
