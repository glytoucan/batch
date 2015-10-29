package org.glycoinfo.rdf.service;

import java.util.List;

import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlEntity;

public interface UserProcedure {
	public static final String GIVEN_NAME = "givenName";
	public static final String ID = "id";
	public static final String FAMILY_NAME = "familyName";
	public static final String GENDER = "gender";
	public static final String PICTURE = "picture";
	public static final String EMAIL ="email", VERIFIED_EMAIL="verifiedEmail";
	public static final String CONTRIBUTOR_ID = "alternateName"; // shortcut to map contributor id into Person;
	public static final String PROGRAM_NAME = "programName";
	public static final String GLYTOUCAN_PROGRAM = "glytoucanPartnerProgram";
	public static final String GLYTOUCAN_PROGRAM_TITLE = "Glytoucan Partner";
	public static final String MEMBERSHIP_NUMBER = "membershipNumber";
	public static final String MEMBER = "member";
	public static final String MEMBER_OF = "memberOf";
	
	public void add(SparqlEntity userSparqlEntity) throws SparqlException; // with PRIMARY_ID 
	
	public SparqlEntity getById(String primaryId) throws SparqlException;
	
	public String getIdByEmail(String email) throws SparqlException;
	
	public String generateHash(String primaryId) throws SparqlException;

	public List<SparqlEntity> getAll() throws SparqlException;

	public List<SparqlEntity> getByContributorId(String username) throws SparqlException;

	boolean checkApiKey(String username, String hash) throws SparqlException;
}