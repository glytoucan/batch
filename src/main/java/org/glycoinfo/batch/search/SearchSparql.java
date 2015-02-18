package org.glycoinfo.batch.search;

import org.glycoinfo.WURCSFramework.wurcs.WURCSFormatException;

public interface SearchSparql {

	String getExactWhere(String sequence) throws WURCSFormatException;

	public void setGlycoSequenceUri(String strGlycoSeqVariable);

	public String getGlycoSequenceUri();

}