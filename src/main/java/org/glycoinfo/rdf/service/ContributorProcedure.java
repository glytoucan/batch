package org.glycoinfo.rdf.service;

import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlEntity;

public interface ContributorProcedure {
	String addContributor(String name) throws SparqlException;
	
	SparqlEntity searchContributor(String name) throws SparqlException;
}