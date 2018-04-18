package org.glycoinfo.rdf.schema;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.UriProvider;
import org.glycoinfo.rdf.scint.ClassHandler;

public class SchemaSparqlFormatter {
	
	public static final Log logger = LogFactory.getLog(SchemaSparqlFormatter.class);

	public static String getPrefixClassName(ClassHandler classHandler) {
		return classHandler.getPrefix() + ":" + classHandler.getClassName();
	}
	
  public static String getPrefixDefinition(ClassHandler classHandler) {
    if (classHandler.getPrefixIri().contains("<"))
      return "prefix " + classHandler.getPrefix() + ": <"
          + classHandler.getPrefixIri().substring(1, classHandler.getPrefixIri().length() - 1) + ">";
    else
      return "prefix " + classHandler.getPrefix() + ": <" + classHandler.getPrefixIri() + ">";
  }
	
	public static String getDomainName(ClassHandler classHandler, String uri) {
		// uri = http://schema.org/additionalName
		// prefix = http://schema.org/
		if (!uri.contains(classHandler.getPrefixIri())) {
			logger.debug("uri:>" + uri + "< not :>" + classHandler.getPrefixIri() + "<");
			return null;
		}
		return uri.substring(classHandler.getPrefixIri().length(), uri.length());
		// additionalName
	}

	public static String getCommonClassWhere(ClassHandler classHandler) {
		return getPrefixClassName(classHandler) + " rdf:type owl:Class .";
	}
	
	// schema:Person schema:domainName ?domainName
	public static String getDomainWhere(ClassHandler classHandler, String domain) {
		return getPrefixClassName(classHandler) + " " + classHandler.getPrefix() + ":" + domain + " ?" + domain + " .\n";
	}
	
	public static String getDomainWhere(String uri, ClassHandler classHandler, String domain) {
		return uri + " " + classHandler.getPrefix() + ":" + domain + " ?" + domain + " .\n";
	}

	// schema:Person schema:domainName ?domainName
	public static String getInsert(ClassHandler classHandler, String domain, String data) {
		return getPrefixClassName(classHandler) + " " + classHandler.getPrefix() + ":" + domain + " \"" + data + "\" .\n";
	}
	
	public static String getInsert(ClassHandler classHandler, String domain, Object data) throws SparqlException {
		if (data instanceof String)
			return getInsert(classHandler, domain, (String)data);
		if (data instanceof UriProvider) {
			UriProvider uriData = (UriProvider)data;
			return getPrefixClassName(classHandler) + " " + classHandler.getPrefix() + ":" + domain + " " + uriData.getUri() + " .\n";
		}
		return getPrefixClassName(classHandler) + " " + classHandler.getPrefix() + ":" + domain + " " + data + " .\n";
	}

	public static String getInsert(String uri, ClassHandler classHandler, String domain, String data) {
		return uri + " " + classHandler.getPrefix() + ":" + domain + " \"" + data + "\" .\n";
	}
	
	public static String getInsert(String uri, ClassHandler classHandler, String domain, Object data) throws SparqlException {
		if (data instanceof String)
			return getInsert(uri, classHandler, domain, (String)data);
		if (data instanceof UriProvider) {
			UriProvider uriData = (UriProvider)data;
			return uri + " " + classHandler.getPrefix() + ":" + domain + " " + uriData.getUri() + " .\n";
		}
		if (data instanceof Date) {
			String nowAsISO = getDateFormat().format(data);
			return uri + " " + classHandler.getPrefix() + ":" + domain + " \"" + nowAsISO + "\"^^xsd:dateTime .\n";
		}
		if (data instanceof Enum) {
			Enum dataEnum = (Enum)data;
			return uri + " " + classHandler.getPrefix() + ":" + domain + " "  + classHandler.getPrefix() + ":" + dataEnum.toString() + " .\n";
		}

		return uri + " " + classHandler.getPrefix() + ":" + domain + " " + data + " .\n";
	}

	public static String getAClassWhere(String uri, ClassHandler classHandler) {
		return uri + " a " + getPrefixClassName(classHandler) + " .";
	}

	public static String getUri(ClassHandler classHandler, Object value) {
		return "<" + classHandler.getPrefixIri() + classHandler.getClassName() + "#" + getPrimaryKey(value) + ">";
	}
	
	public static String getPrimaryKey(Object value) {
		if (value instanceof Date) {
			String nowAsISO = getDateFormat().format(value);
			return nowAsISO;
		}
		return value.toString();
	}

	public static String getPrimaryKey(ClassHandler classhandler, String value) {
		return value.replace(classhandler.getPrefixIri(), "").replace(classhandler.getClassName() + "#", "");
	}

	
	public static String getAInsert(String uri, ClassHandler classHandler) {
		return uri + " a " + getPrefixClassName(classHandler) + " . \n";
	}
	
	public static DateFormat getDateFormat() {
		TimeZone tz = TimeZone.getTimeZone("UTC");
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
		df.setTimeZone(tz);
		return df;
	}
}