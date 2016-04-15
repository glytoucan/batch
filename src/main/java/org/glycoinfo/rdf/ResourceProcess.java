package org.glycoinfo.rdf;

import org.glycoinfo.rdf.dao.SparqlDAO;

public interface ResourceProcess {
	public SparqlDAO getSparqlDAO();

	public void setSparqlDAO(SparqlDAO sparqlDAO);
	
	public String getGraph();
}