package biz.hirte.timesheet.provider.xml;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

import biz.hirte.timesheet.provider.xml.service.TimesheetPersister;

public class Activator extends AbstractUIPlugin {

	// The shared instance
	private static Activator plugin;

	/**
	 * The constructor
	 */
	public Activator() {}

	private static ScheduledExecutorService ES;

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
		ES = Executors.newScheduledThreadPool(1);
		ES.scheduleWithFixedDelay(new Runnable() {

			@Override
			public void run() {
				try {
					TimesheetPersister.getInstance().saveDataStore();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 1, 5, TimeUnit.MINUTES);

		// Shutdown the executor service so at shutdown 
		Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {

			@Override
			public void run() {
				ES.shutdown();
			}
		}));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#stop(org.osgi.framework.
	 * BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		TimesheetPersister.getInstance().saveDataStore();

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

	public String getBundleName() {
		return plugin.getBundle().getSymbolicName();
	}
}
