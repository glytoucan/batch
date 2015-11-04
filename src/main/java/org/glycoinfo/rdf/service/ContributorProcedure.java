package org.glycoinfo.rdf.service;

import java.util.List;

import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlEntity;

public interface ContributorProcedure {
	String addContributor(String name) throws SparqlException;
	
	SparqlEntity searchContributor(String name) throws SparqlException;

	List<SparqlEntity> selectDatabaseByContributor(String contributorId) throws SparqlException;
	List<SparqlEntity> insertResourceEntry(List<SparqlEntity> entries, String id) throws SparqlException;
}