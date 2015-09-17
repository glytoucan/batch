package org.glycoinfo.rdf.service;

import java.util.List;

import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlEntity;

public interface UserProcedure {
	public static final String givenName = "givenName";
	public static final String id = "id";
	public static final String familyName = "familyName";
	public static final String gender = "gender";
	public static final String picture= "picture";
	public static final String email="email", verifiedEmail="verifiedEmail";
	
	
	public void addUser() throws SparqlException;
	
	public void setSparqlEntity(SparqlEntity s);
	
	public SparqlEntity getSparqlEntity();

	List<SparqlEntity> getUser(String email) throws SparqlException;
	
	public boolean isSendEmail();

	public void setSendEmail(boolean sendEmail);

}