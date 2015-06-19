package org.glycoinfo.rdf.schema;

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
		return getPrefixClassName(classHandler) + " " + classHandler.getPrefix() + ":" + domain + " ?" + domain + " .";
	}

	// schema:Person schema:domainName ?domainName
	public static String getInsert(ClassHandler classHandler, String domain, String data) {
		return getPrefixClassName(classHandler) + " " + classHandler.getPrefix() + ":" + domain + " \"" + data + "\" .";
	}
}