package org.glycoinfo.rdf.schema;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.UriProvider;
import org.glycoinfo.rdf.scint.ClassHandler;

public class SchemaSparqlFormatter {

	public static String getPrefixClassName(ClassHandler classHandler) {
		return classHandler.getPrefix() + ":" + classHandler.getClassName();
	}
	
	public static String getPrefixDefinition(ClassHandler classHandler) {
		return "prefix " + classHandler.getPrefix() + ": <" + classHandler.getPrefixIri() + ">";
	}
	
	public static String getDomainName(ClassHandler classHandler, String uri) {
		// uri = http://schema.org/additionalName
		// prefix = http://schema.org/
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
	
	public static String getInsert(ClassHandler classHandler, String domain, Object data) {
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
	
	public static String getInsert(String uri, ClassHandler classHandler, String domain, Object data) {
		if (data instanceof String)
			return getInsert(uri, classHandler, domain, (String)data);
		if (data instanceof UriProvider) {
			UriProvider uriData = (UriProvider)data;
			return uri + " " + classHandler.getPrefix() + ":" + domain + " " + uriData.getUri() + " .\n";
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
			TimeZone tz = TimeZone.getTimeZone("UTC");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
			df.setTimeZone(tz);
			String nowAsISO = df.format(value);

			return nowAsISO;
		}
		return value.toString();
	}

	public static String getAInsert(String uri, ClassHandler classHandler) {
		return uri + " a " + getPrefixClassName(classHandler) + " . \n";
	}
}