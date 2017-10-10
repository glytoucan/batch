package org.glycoinfo.rdf;

import org.junit.Test;

public class DeleteInsertSparqlBeanTest {

	@Test
	public void test() throws SparqlException {
		DeleteInsertSparqlBean deleteInsertSparqlBean = new DeleteInsertSparqlBean();
		deleteInsertSparqlBean.setFromGraph("http://test.org/graph");
		deleteInsertSparqlBean.setDelete(" ?Saccharide glycan:has_motif ?GlycanMotif.");
		deleteInsertSparqlBean.setToGraph("http://test.org/graph");
		deleteInsertSparqlBean.setInsert(" ?Saccharide glycan:has_motif ?GlycanMotif.");
		deleteInsertSparqlBean.setUsing("http://test.org/graph");
		deleteInsertSparqlBean.setWhere("?Saccharide glycan:has_motif ?GlycanMotif.");
		String sparql = deleteInsertSparqlBean.getSparql();
		System.out.println(sparql);
	}
}
