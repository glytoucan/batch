package org.glycoinfo.rdf.dao;

import java.util.List;

import org.glycoinfo.rdf.InsertSparql;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.utils.TripleStoreProperties;

public interface SparqlDAO {

	List<SparqlEntity> query(SelectSparql select) throws SparqlException;

	void insert(InsertSparql insert) throws SparqlException;

	void delete(String string) throws SparqlException;

	void execute(String string) throws SparqlException;
	
	int load(String file) throws SparqlException;

}