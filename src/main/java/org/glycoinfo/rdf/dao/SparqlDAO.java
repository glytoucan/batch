package org.glycoinfo.rdf.dao;

import java.util.List;

import org.glycoinfo.rdf.SparqlBean;
import org.glycoinfo.rdf.SparqlException;

public interface SparqlDAO {

	List<SparqlEntity> query(SparqlBean select) throws SparqlException;

	void insert(SparqlBean insert) throws SparqlException;

	void delete(SparqlBean string) throws SparqlException;

	void execute(SparqlBean string) throws SparqlException;
	
	/**
	 * 
	 * The archive functionality accepts any type of Sparql class , Insert, Delete SparqlBeans.
	 * 
	 * @param something
	 * @throws SparqlException
	 */
	void archive(SparqlBean something) throws SparqlException;
	
	int load(String file) throws SparqlException;

}