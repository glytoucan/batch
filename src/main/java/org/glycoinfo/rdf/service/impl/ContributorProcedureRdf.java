package org.glycoinfo.rdf.service.impl;

import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.rdf.InsertSparql;
import org.glycoinfo.rdf.InsertSparqlBean;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.glycan.Contributor;
import org.glycoinfo.rdf.glycan.ContributorDatabaseInsertSparql;
import org.glycoinfo.rdf.glycan.ContributorInsertSparql;
import org.glycoinfo.rdf.glycan.ContributorNameSelectSparql;
import org.glycoinfo.rdf.glycan.DatabaseSelectSparql;
import org.glycoinfo.rdf.glycan.LatestContributorIdSparql;
import org.glycoinfo.rdf.glycan.ResourceEntry;
import org.glycoinfo.rdf.service.ContributorProcedure;
import org.glycoinfo.rdf.service.exception.ContributorException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.transaction.annotation.Transactional;

public class ContributorProcedureRdf implements ContributorProcedure  {

	private static final Log logger = LogFactory
			.getLog(ContributorProcedureRdf.class);
	
	@Autowired
	SparqlDAO sparqlDAO;
	
	@Autowired
	ContributorInsertSparql contributorSparql;

	@Autowired
	ContributorNameSelectSparql contributorNameSelectSparql;
	
	@Autowired
	@Qualifier("DatabaseSelectSparql")
	SelectSparql databaseSelectSparql;
	
	@Autowired
	@Qualifier("ResourceEntryInsert")
	InsertSparql resourceEntryInsertSparql;
	
	/**
	 * adds a Contributor class (foaf:Person).
	 * 
     * <http://rdf.glycoinfo.org/glytoucan/contributor/1>
     * a    foaf:Person ;
     * dcterms:identifier    "1" ;
     * foaf:name "Administrator" .
     * 
	 * @throws SparqlException
	 */
  @Transactional
	public String addContributor(String name) throws ContributorException {
		SparqlEntity result = searchContributor(name);
		String id;
		if (result == null) {
			// retrieve the latest contributor id
			SelectSparql selectLatestContributorId = new LatestContributorIdSparql();
			List<SparqlEntity> personUIDResult;
			try {
				personUIDResult = sparqlDAO.query(selectLatestContributorId);
			} catch (SparqlException e) {
				throw new ContributorException(e);
			}
			
			SparqlEntity idSE = personUIDResult.iterator().next();
			
			id = idSE.getValue("id");
			
			// insert the above data.
			SparqlEntity sparqlEntityPerson = new SparqlEntity(id);
			sparqlEntityPerson.setValue(ContributorInsertSparql.ContributorName, name);
			sparqlEntityPerson.setValue(ContributorInsertSparql.UserId, id);
			contributorSparql.setSparqlEntity(sparqlEntityPerson);

		try {
			sparqlDAO.insert(contributorSparql);		
		} catch (SparqlException e) {
			throw new ContributorException(e);
		}

		} else {
			id = result.getValue(Contributor.ID);
			logger.info("User id " + id + "already exists");
		}
		
		return id;
	}

	@Override
  @Transactional
	public SparqlEntity searchContributor(String name) throws ContributorException {
		if (StringUtils.isBlank(name))
			throw new ContributorException("given name cannot be blank.  Please fix account information or login with google+.");

		SparqlEntity se = new SparqlEntity();
		se.setValue(Contributor.NAME, name);
		contributorNameSelectSparql.setSparqlEntity(se);

		List<SparqlEntity> personUIDResult;
		try {
			personUIDResult = sparqlDAO.query(contributorNameSelectSparql);
		} catch (SparqlException e) {
			throw new ContributorException(e);
		}
		
		if (personUIDResult.iterator().hasNext()) {
			SparqlEntity idSE = personUIDResult.iterator().next();
			return idSE;
		}
		return null;
	}
	
	@Override
  @Transactional
	public SparqlEntity selectDatabaseByContributor(String contributorId) throws ContributorException {
		if (StringUtils.isBlank(contributorId))
			throw new ContributorException("contributorId cannot be blank");

		SparqlEntity se = new SparqlEntity();
		se.setValue(ResourceEntry.ContributorId, contributorId);
		databaseSelectSparql.setSparqlEntity(se);
		List<SparqlEntity> results = null;
		try {
			results = sparqlDAO.query(databaseSelectSparql);
		} catch (SparqlException e) {
			throw new ContributorException(e);
		}
		if (!results.iterator().hasNext())
			throw new ContributorException("contributor >" + contributorId + "< is not a member of a database");
		SparqlEntity database = results.iterator().next();
		// need the urltemplate
//		String databaseLiteral = database.getValue(ResourceEntry.GlycanDatabaseLiteral);
//		if (StringUtils.isBlank(databaseLiteral))
//			throw new ContributorException("databaseLiteral does not exist");
		
		return database;
	}

	@Override
  @Transactional
	public List<SparqlEntity> insertResourceEntry(List<SparqlEntity> entries, String id) throws ContributorException {
		if (null == entries)
			throw new ContributorException("entries cannot be blank");

		if (entries.iterator().hasNext()) {
			SparqlEntity databaseresult = entries.iterator().next();
			databaseresult.setValue(ResourceEntry.Identifier, id);
			databaseresult.setValue(ResourceEntry.DataSubmittedDate, new Date());
			resourceEntryInsertSparql.setSparqlEntity(databaseresult);
			try {
				sparqlDAO.insert(resourceEntryInsertSparql);
			} catch (SparqlException e) {
				throw new ContributorException(e);
			}
		}
		
		return entries;
	}

	@Override
  @Transactional
	public void memberDb(String contributorId, String dbAbbreviation) throws ContributorException {
//		 insert 				?user foaf:member ?db .
//		 for the ?db where glycan:has_abbreviation ?dbAbbreviation
//		String insert = 
//				+ "INSERT {\n"
//				+ "<http://rdf.glycoinfo.org/glytoucan/contributor/userId/" + contributorId + "> foaf:member ?db .\n"
//						+ "} WHERE {\n"
//						+ "?db glycan:has_abbreviation \"" + dbAbbreviation + "\"^^<http://www.w3.org/2001/XMLSchema#string> . \n"
//								+ "}";
		
		SparqlEntity dbSE = new SparqlEntity();
		dbSE.setValue(Contributor.ID, contributorId);
		dbSE.setValue(ResourceEntry.Database_Abbreviation, dbAbbreviation);
		InsertSparql ins = new ContributorDatabaseInsertSparql();
		ins.setSparqlEntity(dbSE);
		try {
			sparqlDAO.insert(ins);
		} catch (SparqlException e) {
			throw new ContributorException(e);
		}
	}
	
//	@Override
//	public SparqlEntity insertContributorLog(String id, String message) throws SparqlException {
//		if (StringUtils.isBlank(id))
//			throw new SparqlException("id cannot be blank");
//
//		SparqlEntity se = new SparqlEntity();
//		se.setValue(Contributor.NAME, name);
//		contributorSelectSparql.setSparqlEntity(se);
//
//		List<SparqlEntity> personUIDResult = sparqlDAO.query(contributorSelectSparql);
//		
//		if (personUIDResult.iterator().hasNext()) {
//			SparqlEntity idSE = personUIDResult.iterator().next();
//			return idSE;
//		}
//		return null;
//	}
	
	
}