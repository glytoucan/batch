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

	// GRAPH
	@Override
	public String getGraph() {
		return graph;
	}

	@Override
	public void setGraph(String graph) {
		this.graph = graph;
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
				sparqlbuf.append(getGraph() != null ? "{ GRAPH <" + getGraph() + ">\n" : "");
				sparqlbuf.append("{ " + getDelete() + " }\n");
				sparqlbuf.append(getGraph() != null ? "}\n" : "");
				sparqlbuf.append("INSERT \n");
				sparqlbuf.append(getGraph() != null ? "{ GRAPH <" + getGraph() + ">\n" : "");
				sparqlbuf.append("{ " + getDelete() + " }\n");
				sparqlbuf.append(getGraph() != null ? "}\n" : "");
				sparqlbuf.append(getUsing() != null ? "USING <" + getUsing() + ">\n" : "");
				sparqlbuf.append(getWhere() != null ? "WHERE " + getWhere() + "\n" : "");
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


