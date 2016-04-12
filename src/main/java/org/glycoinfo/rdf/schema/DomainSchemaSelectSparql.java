package org.glycoinfo.rdf.schema;

import org.glycoinfo.rdf.scint.ClassHandler;

public class DomainSchemaSelectSparql extends SchemaSelectSparql {

	/*
	 * Prefix schema: <http://schema.org/> Prefix rdfs:
	 * <http://www.w3.org/2000/01/rdf-schema#> Prefix rdf:
	 * <http://www.w3.org/1999/02/22-rdf-syntax-ns#> Select ?domain Where {
	 * schema:Person rdf:type owl:Class . ?domain rdfs:domain schema:Person . }
	 */
	public DomainSchemaSelectSparql(ClassHandler classHandler) {
		super(classHandler);
		super.where = SchemaSparqlFormatter.getCommonClassWhere(classHandler) + " \n" + "{ ?class ^(rdfs:subClassOf)+ "
				+ SchemaSparqlFormatter.getPrefixClassName(classHandler) + " .\n" + "?result rdfs:domain ?class .\n"
				+ " } UNION { ?result rdfs:domain " + SchemaSparqlFormatter.getPrefixClassName(classHandler) + " .\n }"
				+ "UNION { ?result " + classHandler.getPrefix() + ":domainIncludes " + SchemaSparqlFormatter.getPrefixClassName(classHandler)
				+ " .\n }" + "UNION { ?class ^(rdfs:subClassOf)+ "
				+ SchemaSparqlFormatter.getPrefixClassName(classHandler) + " .\n" + "?result " + classHandler.getPrefix() + ":domainIncludes ?class .\n }";
	}
}