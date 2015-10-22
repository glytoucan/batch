package org.glycoinfo.rdf.service.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.glycan.Contributor;
import org.glycoinfo.rdf.glycan.ContributorInsertSparql;
import org.glycoinfo.rdf.glycan.ContributorNameSelectSparql;
import org.glycoinfo.rdf.glycan.LatestContributorIdSparql;
import org.glycoinfo.rdf.service.ContributorProcedure;
import org.springframework.beans.factory.annotation.Autowired;

public class ContributorProcedureRdf implements ContributorProcedure  {

	private static final Log logger = LogFactory
			.getLog(ContributorProcedureRdf.class);
	
	@Autowired
	SparqlDAO sparqlDAO;
	
	@Autowired
	ContributorInsertSparql contributorSparql;

	@Autowired
	ContributorNameSelectSparql contributorSelectSparql;
	
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
	public String addContributor(String name) throws SparqlException {
		SparqlEntity result = searchContributor(name);
		String id;
		if (result == null) {
			// retrieve the latest contributor id
			SelectSparql selectLatestContributorId = new LatestContributorIdSparql();
			List<SparqlEntity> personUIDResult = sparqlDAO.query(selectLatestContributorId);
			
			SparqlEntity idSE = personUIDResult.iterator().next();
			
			id = idSE.getValue("id");
			
			// insert the above data.
			SparqlEntity sparqlEntityPerson = new SparqlEntity(id);
			sparqlEntityPerson.setValue(ContributorInsertSparql.ContributorName, name);
			sparqlEntityPerson.setValue(ContributorInsertSparql.UserId, id);
			contributorSparql.setSparqlEntity(sparqlEntityPerson);
			
			sparqlDAO.insert(contributorSparql);		
		} else {
			id = result.getValue(Contributor.ID);
			logger.info("User id " + id + "already exists");
		}
		
		return id;
	}

	@Override
	public SparqlEntity searchContributor(String name) throws SparqlException {
		if (StringUtils.isBlank(name))
			throw new SparqlException("name cannot be blank");

		SparqlEntity se = new SparqlEntity();
		se.setValue(Contributor.NAME, name);
		contributorSelectSparql.setSparqlEntity(se);

		List<SparqlEntity> personUIDResult = sparqlDAO.query(contributorSelectSparql);
		
		if (personUIDResult.iterator().hasNext()) {
			SparqlEntity idSE = personUIDResult.iterator().next();
			return idSE;
		}
		return null;
	}
}