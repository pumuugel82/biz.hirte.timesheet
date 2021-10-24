package biz.hirte.timesheet;

import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class Activator extends AbstractUIPlugin {

	// The plug-in ID
	public static final String PLUGIN_ID = "biz.hirte.timesheet.plugin.ui"; //$NON-NLS-1$

	// The shared instance
	private static Activator plugin;

	/**
	 * The constructor
	 */
	public Activator() {}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#start(org.osgi.framework.
	 * BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.
	 * BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 *
	 * @return the shared instance
	 */
	public static Activator getDefault() {
		return plugin;
	}

	@Override
	protected void initializeDefaultPluginPreferences() {
		IPreferenceStore prefStore = getPreferenceStore();
		prefStore.setDefault(PreferenceKeys.CHANGE_PERIOD_DIALOG_LAST_PROJECT, "");
		prefStore.setDefault(PreferenceKeys.CHANGE_PERIOD_DIALOG_LAST_FROM_DATE_HRS, 9);
		prefStore.setDefault(PreferenceKeys.CHANGE_PERIOD_DIALOG_LAST_FROM_DATE_MIN, 0);
		prefStore.setDefault(PreferenceKeys.CHANGE_PERIOD_DIALOG_LAST_TO_DATE_HRS, 17);
		prefStore.setDefault(PreferenceKeys.CHANGE_PERIOD_DIALOG_LAST_TO_DATE_MIN, 0);
	}

}
