package org.glycoinfo.batch.search;

import org.glycoinfo.rdf.SparqlException;


public interface SearchSparql {

	String getWhere(String sequence) throws SparqlException;

	public void setGlycoSequenceUri(String strGlycoSeqVariable);

	public String getGlycoSequenceUri();

}