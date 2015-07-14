package org.glycoinfo.rdf.service;

import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlEntity;

public interface UserProcedure {
	public void addUser() throws SparqlException;
	
	public void setSparqlEntity(SparqlEntity s);
	
	public SparqlEntity getSparqlEntity();
}