package org.glycoinfo.batch.search;


public interface SearchSparql {

	String getWhere(String sequence);

	public void setGlycoSequenceUri(String strGlycoSeqVariable);

	public String getGlycoSequenceUri();

}