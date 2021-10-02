package biz.hirte.timesheet.provider.xml.service;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.namespace.QName;

import org.xml.sax.SAXException;

import biz.hirte.timesheet.provider.xml.Activator;
import biz.hirte.timesheet.provider.xml.model.XPeriod;
import biz.hirte.timesheet.provider.xml.model.XProject;
import biz.hirte.timesheet.provider.xml.model.XProperty;
import biz.hirte.timesheet.provider.xml.model.XPropertyDescriptor;
import biz.hirte.timesheet.provider.xml.model.XTimesheets;

/**
 * Cares about persisting the Model with JAXB.
 * 
 * @author hirte
 *
 */
public class TimesheetPersister {

	private static final Logger			LOGGER							= Logger.getLogger(TimesheetPersister.class.getCanonicalName());

	private static TimesheetPersister	INSTANCE;

	private static final String			TIMESHEETS_XML_DATAFILE_NAME	= "timesheets.xml";

	private final String				DATA_FILE_NAME;

	private static final String			TIMESHEETS_XML_DATAFILE_FOLDER	= ".eclipse";

	private JAXBElement<XTimesheets>	dataStore;

	public TimesheetPersister(String dataFileName) {
		this.DATA_FILE_NAME = dataFileName;
	}

	public static final TimesheetPersister getInstance(String dataFileName) {
		if (INSTANCE == null) {
			INSTANCE = new TimesheetPersister(dataFileName == null ? TIMESHEETS_XML_DATAFILE_NAME : dataFileName);
		}
		return INSTANCE;
	}

	public static final TimesheetPersister getInstance() {
		return getInstance(null);
	}

	private File prepareDataLocation() throws Exception {

		LOGGER.entering("TimesheetPersister", "prepareDataLocation()");

		String dataDicLoc = null;
		if (Activator.getDefault() != null) {
			dataDicLoc = System.getProperty("user.home") + File.separator + TIMESHEETS_XML_DATAFILE_FOLDER + File.separator
					+ Activator.getDefault().getBundleName();
		} else {
			dataDicLoc = System.getProperty("user.home") + File.separator + TIMESHEETS_XML_DATAFILE_FOLDER + File.separator + "test";
		}

		File dic = new File(dataDicLoc);

		if (dic.exists() && dic.isDirectory()) {

			return dic;

		} else {

			if (dic.mkdir()) {
				return dic;
			}

		}

		return null;
	}

	JAXBElement<XTimesheets> loadDataStore() throws Exception {

		LOGGER.entering("TimesheetPersister", "loadDataStore()");

		File directory = prepareDataLocation();
		File file = new File(directory, DATA_FILE_NAME);

		JAXBElement<XTimesheets> dstore;

		if (file.exists()) {
			LOGGER.info("TIMESHEETS_XML_DATAFILE exists: " + file.getAbsolutePath());
			dstore = readDataFile(file);

		} else {

			dstore = createEmptyDataStore();
			saveDataFile(file, dstore);
		}

		return dstore;

	}

	public void saveDataStore() throws Exception {

		File directory = prepareDataLocation();

		File file = new File(directory, DATA_FILE_NAME);

		if (!file.exists()) {
			file.createNewFile();
		}

		saveDataFile(file, dataStore);

	}

	XTimesheets getTimesheets() {

		if (this.dataStore == null) {
			try {
				this.dataStore = loadDataStore();
			}
			catch (Exception e) {
				this.dataStore = createEmptyDataStore();
				e.printStackTrace();
			}
		}

		return this.dataStore == null ? null : this.dataStore.getValue();
	}

	private synchronized JAXBElement<XTimesheets> readDataFile(final File datafile) {

		LOGGER.entering("TimesheetPersister", "readDataFile()");

		JAXBElement<XTimesheets> mp = null;
		JAXBContext ctx;
		try {
			ctx = JAXBContext.newInstance(XProject.class, XTimesheets.class, XPeriod.class, XProperty.class, XPropertyDescriptor.class);
			Unmarshaller unms = ctx.createUnmarshaller();

			mp = createDataStore((XTimesheets) unms.unmarshal(datafile));
		}
		catch (JAXBException e) {
			LOGGER.logp(Level.SEVERE, "TimesheetPersister", "readDataFile()", "Cannot unmarshall datastore", e);
		}

		return mp;
	}

	private synchronized void saveDataFile(final File datafile, final JAXBElement<XTimesheets> data) throws SAXException {

		if (data == null) {
			throw new IllegalArgumentException("Data must not be null.");
		}

		JAXBContext ctx;
		try {
			ctx = JAXBContext.newInstance(XProject.class, XTimesheets.class, XPeriod.class);
			Marshaller ms = ctx.createMarshaller();
			ms.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
			ms.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

			ms.marshal(data, datafile);
		}
		catch (JAXBException e) {
			LOGGER.logp(Level.SEVERE, "TimesheetPersister", "readDataFile()", "Cannot marshall datastore", e);
		}

	}

	private JAXBElement<XTimesheets> createEmptyDataStore() {
		return createDataStore(new XTimesheets());
	}

	private JAXBElement<XTimesheets> createDataStore(XTimesheets timesheets) {

		QName qname = new QName("http://timesheets.hirte.biz", "timesheets");
		JAXBElement<XTimesheets> jaxbelement = new JAXBElement<XTimesheets>(qname, XTimesheets.class, timesheets);

		return jaxbelement;
	}

}
