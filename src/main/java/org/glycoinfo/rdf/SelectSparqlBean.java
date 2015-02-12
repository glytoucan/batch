package org.glycoinfo.rdf;

import org.glycoinfo.rdf.dao.SparqlEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SelectSparqlBean implements SelectSparql {

	protected String prefix, select, where, from, orderby, groupby, construct, having,
			limit, offset;
	StringBuffer sparql;
	SparqlEntity sparqlEntity;

	protected Logger logger = LoggerFactory.getLogger(getClass());

	public SelectSparqlBean(String sparql) {
		this.sparql = new StringBuffer(sparql);
	}

	public SelectSparqlBean() {
	}

	@Override
	public String getPrefix() {
		return prefix;
	}

	@Override
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	@Override
	public String getSelect() {
		return select;
	}

	@Override
	public void setSelect(String select) {
		this.select = select;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.glycoinfo.rdf.SelectSparql#getWhere()
	 */
	@Override
	public String getWhere() throws SparqlException {
		return where;
	}

	@Override
	public void setWhere(String where) {
		this.where = where;
	}

	@Override
	public String getFrom() {
		return from;
	}

	@Override
	public void setFrom(String from) {
		this.from = from;
	}

	@Override
	public String getOrderBy() {
		return orderby;
	}

	@Override
	public void setOrderBy(String orderByStatement) {
		this.orderby = orderByStatement;
	}

	@Override
	public String getGroupBy() {
		return groupby;
	}

	@Override
	public void setGroupBy(String groupByStatement) {
		this.groupby = groupByStatement;
	}

	@Override
	public String getHaving() {
		return having;
	}

	@Override
	public void setHaving(String havingStatement) {
		this.having = havingStatement;
	}

	@Override
	public String getConstruct() {
		return construct;
	}

	@Override
	public void setConstruct(String construct) {
		this.construct = construct;
	}

	@Override
	public void setSparql(String sparql) {
		this.sparql = new StringBuffer(sparql);
	}

	@Override
	public void setSparqlEntity(SparqlEntity sparqlentity) {
		this.sparqlEntity = sparqlentity;
	}

	@Override
	public SparqlEntity getSparqlEntity() {
		return sparqlEntity;
	}

	@Override
	public String getLimit() {
		return limit;
	}

	@Override
	public void setLimit(String limit) {
		this.limit = limit;
	}

	@Override
	public String getOffset() {
		return offset;
	}

	@Override
	public void setOffset(String offset) {
		this.offset = offset;
	}

	@Override
	public String getSparql() throws SparqlException {
		if (null == this.sparql) {
			StringBuffer sparqlbuf = new StringBuffer();
			sparqlbuf.append(getPrefix() + "\n");
			sparqlbuf.append("SELECT " + getSelect() + "\n");
			sparqlbuf.append(getFrom() != null ? getFrom() + "\n" : "\n");
//			sparqlbuf.append(getFrom() != null ? " FROM <" + getFrom() + ">"
//					: "");
			// sparqlbuf.append(matchStatement != null ? " MATCH " + matchStatement
			// :
			// "");
			sparqlbuf.append(getWhere() != null ? "WHERE {" + getWhere() + "}\n"
					: "");
			// sparqlbuf.append(" RETURN ").append(returnStatement);
			sparqlbuf.append(getOrderBy() != null ? " ORDER BY " + getOrderBy()
					+ "\n" : "");

			sparqlbuf.append(getLimit() != null ? " LIMIT " + getLimit() + "\n"
					: "");
			sparqlbuf.append(getOffset() != null ? " OFFSET " + getOffset() + "\n"
					: "");
			logger.debug(sparqlbuf.toString());
			return sparqlbuf.toString();
		} else {
			logger.debug(this.sparql.toString());
			return this.sparql.toString();
		}
	}
}