package org.glycoinfo.rdf.schema.org;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.glycoinfo.rdf.SelectSparqlBean;
import org.glycoinfo.rdf.UriProvider;

public class DateTime implements UriProvider {

	public Date dateValue;
	
	public Date getDateValue() {
		return dateValue;
	}

	public void setDateValue(Date dateValue) {
		this.dateValue = dateValue;
	}

	@Override
	public String getUri() {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
		df.setTimeZone(tz);
		String nowAsISO = df.format(getDateValue());
		return "<http://schema.org/DateTime/" + nowAsISO + ">";
	}
}
