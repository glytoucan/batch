package org.glycoinfo.rdf.scint;

import org.glycoinfo.rdf.SparqlBean;
import org.glycoinfo.rdf.SparqlException;

public interface Scintillate {
	public final static String NO_DOMAINS = "no_domains";
	public void update() throws SparqlException;
	
	public SparqlBean getSparqlBean();
	public void setSparqlBean(SparqlBean sparql);
}