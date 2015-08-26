package org.glycoinfo.rdf.service;

import org.glycoinfo.rdf.SparqlException;

public interface ContributorProcedure {
	String addContributor() throws SparqlException;
	
	String searchContributor() throws SparqlException;

	String getName();

	void setName(String name);
}