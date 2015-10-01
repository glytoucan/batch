package org.glycoinfo.rdf.service;

import org.glycoinfo.rdf.SparqlException;

public interface ContributorProcedure {
	String addContributor() throws SparqlException;
	
	String searchContributor() throws SparqlException;

	String getId();

	void setId(String id);

	void setName(String name);
	
	String getName();

//	String searchContributor(String email);
}