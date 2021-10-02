package biz.hirte.timesheet.provider.xml.model;

import java.time.LocalTime;

import javax.xml.bind.annotation.adapters.XmlAdapter;

//FIXME hirtech Use DateTimeFormatter instead!
public class LocalTimeXMLAdapter extends XmlAdapter<String, LocalTime> {

	@Override
	public LocalTime unmarshal(String v) throws Exception {
		return LocalTime.parse(v);
	}

	@Override
	public String marshal(LocalTime v) throws Exception {
		return v.toString();
	}

}
