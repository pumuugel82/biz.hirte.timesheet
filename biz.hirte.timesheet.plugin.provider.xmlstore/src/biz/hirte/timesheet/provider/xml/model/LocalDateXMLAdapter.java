package biz.hirte.timesheet.provider.xml.model;

import java.time.LocalDate;

import javax.xml.bind.annotation.adapters.XmlAdapter;

//FIXME hirtech Use DateTimeFormatter instead!
public class LocalDateXMLAdapter extends XmlAdapter<String, LocalDate> {

	@Override
	public LocalDate unmarshal(String v) throws Exception {
		return LocalDate.parse(v);
	}

	@Override
	public String marshal(LocalDate v) throws Exception {
		return v == null ? "" : v.toString();
	}

}
