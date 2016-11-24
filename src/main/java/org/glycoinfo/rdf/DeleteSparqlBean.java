package org.glycoinfo.rdf;

import java.util.ArrayList;

import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.dao.SparqlEntityFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DeleteSparqlBean implements DeleteSparql {

  protected Logger logger = LoggerFactory.getLogger(getClass());

	protected String delete, prefix, where, graph, using;
	String graphbase;

	StringBuffer sparql;
	SparqlEntity sparqlEntity = new SparqlEntity();
	ArrayList<InsertSparql> related;

	public DeleteSparqlBean(String sparql) {
		this.sparql = new StringBuffer(sparql);
	}

	public DeleteSparqlBean() {
	}

	public String getDelete() throws SparqlException {
		return delete;
	}

	public void setDelete(String delete) {
		this.delete = delete;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}

	public String getGraph() {
		return graph;
	}

	public void setGraph(String graph) {
		this.graph = graph;
	}

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

	
	public void setSparql(String sparql) {
		this.sparql = new StringBuffer(sparql);
	}

	@Override
	public void setSparqlEntity(SparqlEntity sparqlentity) {
		this.sparql = null;
		this.sparqlEntity = sparqlentity;
		
//		SparqlEntityFactory.set(sparqlentity);
	}

	@Override
	public SparqlEntity getSparqlEntity() {
		return this.sparqlEntity;
	}

	public String getUsing() {
		return using;
	}

	public void setUsing(String using) {
		this.using = using;
	}

	/**
	 * 
	 * prefix + "\n" + "INSERT\n" + "{ GRAPH <"+graph+"> {\n" + insert + "  }\n"
	 * + "}\n" + using + "WHERE {\n" + where + "}"
	 * @throws SparqlException 
	 * 
	 * @see org.glycoinfo.rdf.InsertSparql#getSparql()
	 */
	@Override
	public String getSparql() throws SparqlException {
    StringBuffer sparqlbuf = new StringBuffer();
		if (this.sparql == null) {
			sparqlbuf.append(getPrefix() != null ? getPrefix() : "");
			if (getFormat().equals(InsertSparql.SPARQL) || getFormat().equals(DELETEWHERE)) {
  			if (getFormat().equals(DELETEWHERE)) {
          sparqlbuf.append("DELETE WHERE\n");
  			} else
          sparqlbuf.append("DELETE DATA\n");

      sparqlbuf.append(getGraph() != null ? "{ GRAPH <" + getGraph()
					+ ">\n" : "");
			// sparqlbuf.append(getUsing());
			sparqlbuf.append("{ " + getDelete() + " }\n");
			sparqlbuf.append(getGraph() != null ? "}\n" : "");
			sparqlbuf.append(getWhere() != null ? "WHERE " + getWhere() + "\n" : "");
			} else {
				sparqlbuf.append(getDelete());
			}
//			logger.debug(sparqlbuf.toString());
			return sparqlbuf.toString();
		} else {
			logger.debug(this.sparql.toString());
			return this.sparql.toString();
		}
	}

	@Override
	public String toString() {
		try {
			return getSparql();
		} catch (SparqlException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}

	@Override
	public String getFormat() {
		return InsertSparql.SPARQL;
	}
}