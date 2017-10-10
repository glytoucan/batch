package org.glycoinfo.rdf;

import java.util.ArrayList;

import org.glycoinfo.rdf.dao.SparqlEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteInsertSparqlBean implements DeleteInsertSparql {

	protected Logger logger = LoggerFactory.getLogger(getClass());

	protected String prefix, delete, fromGraph, toGraph, insert, using, where;
	String graphbase;

	StringBuffer sparql;
	SparqlEntity sparqlEntity = new SparqlEntity();
	ArrayList<InsertSparql> related;
	
	public DeleteInsertSparqlBean(String sparql) {
		this.sparql = new StringBuffer(sparql);
	}

	public DeleteInsertSparqlBean() {
	}

	// PREFIX
	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	// DELETE
	@Override
	public String getDelete() throws SparqlException {
		return delete;
	}

	@Override
	public void setDelete(String delete) {
		this.delete = delete;
	}

	// INSERT
	@Override
	public String getInsert() throws SparqlException {
		return insert;
	}

	@Override
	public void setInsert(String insert) {
		this.insert = insert;
	}

	// From GRAPH 
	@Override
	public String getFromGraph() {
		return fromGraph;
	}

	@Override
	public void setFromGraph(String fromGraph) {
		this.fromGraph = fromGraph;
	}

	// To GRAPH
	@Override
	public String getToGraph() {
		return toGraph;
	}

	@Override
	public void setToGraph(String toGraph) {
		this.toGraph = toGraph;
	}

	// USING
	@Override
	public String getUsing() {
		return using;
	}

	@Override
	public void setUsing(String using) {
		this.using = using;
	}

	// WHERE
	@Override
	public String getWhere() {
		return where;
	}

	@Override
	public void setWhere(String where) {
		this.where = where;
	}

	// GRAPH BASE
	public String getGraphBase() {
		return graphbase;
	}

	/**
	 * graph base to set the graph to insert into. 
	 * example: http://glytoucan.org/rdf/demo/0.7
	 * @param base
	 */
	public void setGraphBase(String base) {
		this.graphbase = base;
	}

	@Override
	public String getFormat() {
		return InsertSparql.SPARQL;
	}

	// SPARQL Entity
	@Override
	public SparqlEntity getSparqlEntity() {
		return this.sparqlEntity;
	}

	@Override
	public void setSparqlEntity(SparqlEntity sparqlentity) {
		this.sparql = null;
		this.sparqlEntity = sparqlentity;
	}

	@Override
	public String getSparql() throws SparqlException {
    	StringBuffer sparqlbuf = new StringBuffer();
		if (this.sparql == null) {
			sparqlbuf.append(getPrefix() != null ? getPrefix() : "");
			if (getFormat().equals(DeleteInsertSparql.SPARQL)) {
				sparqlbuf.append("DELETE \n");
				sparqlbuf.append(getFromGraph() != null ? "{ GRAPH <" + getFromGraph() + ">\n" : "");
				sparqlbuf.append(" { \n" + getDelete() + "\n }\n");
				sparqlbuf.append(getFromGraph() != null ? "}\n" : "");
				sparqlbuf.append("INSERT \n");
				sparqlbuf.append(getToGraph() != null ? "{ GRAPH <" + getToGraph() + ">\n" : "");
				sparqlbuf.append(" { \n" + getInsert() + "\n }\n");
				sparqlbuf.append(getToGraph() != null ? "}\n" : "");
				sparqlbuf.append(getUsing() != null ? "USING <" + getUsing() + ">\n" : "");
				sparqlbuf.append(getWhere() != null ? "WHERE {\n" + getWhere() + "\n} \n" : "");
			} else {
				sparqlbuf.append(getDelete());
			}
			return sparqlbuf.toString();
		} else {
			logger.debug(this.sparql.toString());
			return this.sparql.toString();
		}
	}

	@Override
	public void setSparql(String sparql) {
		this.sparql = new StringBuffer(sparql);
	}

}


