package org.glycoinfo.rdf;

import java.util.ArrayList;

import org.glycoinfo.rdf.dao.SparqlEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteInsertSparqlBean implements DeleteInsertSparql {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected String prefix, delete, graph, insert, using, where;
	String graphbase;

	StringBuffer sparql;
	SparqlEntity sparqlEntity = new SparqlEntity();
	ArrayList<InsertSparql> related;
	
	public DeleteInsertSparqlBean(String sparql) {
		this.sparql = new StringBuffer(sparql);
	}

	public DeleteInsertSparqlBean() {
	}

	@Override
	public String getDelete() throws SparqlException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setDelete(String insert) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getInsert() throws SparqlException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setInsert(String insert) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getGraph() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setGraph(String graph) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getUsing() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setUsing(String using) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getFormat() {
		// TODO Auto-generated method stub
		return null;
	}

}
