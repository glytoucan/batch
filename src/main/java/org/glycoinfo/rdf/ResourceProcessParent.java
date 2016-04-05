package org.glycoinfo.rdf;

import org.glycoinfo.rdf.dao.SparqlDAO;
import org.springframework.beans.factory.annotation.Autowired;

public class ResourceProcessParent implements ResourceProcess { 
	@Autowired
	SparqlDAO sparqlDAO;

	public SparqlDAO getSparqlDAO() {
		return sparqlDAO;
	}

	public void setSparqlDAO(SparqlDAO sparqlDAO) {
		this.sparqlDAO = sparqlDAO;
	}
}