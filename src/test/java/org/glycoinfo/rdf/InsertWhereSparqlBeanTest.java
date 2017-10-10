package org.glycoinfo.rdf;

import org.junit.Test;

public class InsertWhereSparqlBeanTest {

	@Test
	public void test() throws SparqlException {
		InsertWhereSparqlBean insertWhereSparqlBean = new InsertWhereSparqlBean();
		insertWhereSparqlBean.setToGraph("http://test.org/graph1");
		insertWhereSparqlBean.setInsert(" ?s ?p ?o.");
		insertWhereSparqlBean.setUsing("http://test.org/graph2");
		insertWhereSparqlBean.setWhere("?s ?p ?o.");
		String sparql = insertWhereSparqlBean.getSparql();
		System.out.println(sparql);
	}

}
