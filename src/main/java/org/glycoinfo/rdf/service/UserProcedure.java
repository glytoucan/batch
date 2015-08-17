package org.glycoinfo.rdf.service;

import java.util.List;

import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlEntity;

public interface UserProcedure {
	public void addUser() throws SparqlException;
	
	public void setSparqlEntity(SparqlEntity s);
	
	public SparqlEntity getSparqlEntity();

	List<SparqlEntity> getUser(String email) throws SparqlException;
}