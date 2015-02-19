package org.glycoinfo.batch.search.wurcs;

import java.util.LinkedList;

import org.glycoinfo.WURCSFramework.util.rdf.WURCSrdfSPARQLGLIPS_SSM_TBD;
import org.glycoinfo.batch.search.SearchSparql;

public class SearchSparqlBean extends WURCSrdfSPARQLGLIPS_SSM_TBD implements
		SearchSparql {
	String strGlycoSeqVariable;

	public String getWhere(String a_strWURCS) {
		LinkedList<String> t_aOption = new LinkedList<String>();
//		t_aOption.add("exact");
		t_aOption.add("uri");
		t_aOption.add("wurcs");
//		t_aOption.add("accession_number");
		t_aOption.add("whereonly");
		t_aOption.add("TEST_ID:" + "sparqlbean");
		return getSPARQL(a_strWURCS, t_aOption);
	}

	@Override
	public void setGlycoSequenceUri(String strGlycoSeqVariable) {
		this.strGlycoSeqVariable = strGlycoSeqVariable;
	}

	@Override
	public String getGlycoSequenceUri() {
		return strGlycoSeqVariable;
	}

}
