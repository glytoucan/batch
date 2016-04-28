package org.glycoinfo.rdf.service;

import java.util.List;

import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlEntity;

public interface ContributorProcedure {
	
	/**
	 * 
	 * Add a contributor with String name.
	 * First executes searchContributor to confirm it doesn't exist.
	 * If it exists, returns the id of the existing user, otherwise the id of the new user.
	 * 
	 * @param name
	 * @return
	 * @throws SparqlException
	 */
	String addContributor(String name) throws SparqlException;
	
	SparqlEntity searchContributor(String name) throws SparqlException;

	List<SparqlEntity> selectDatabaseByContributor(String contributorId) throws SparqlException;
	List<SparqlEntity> insertResourceEntry(List<SparqlEntity> entries, String id) throws SparqlException;
}