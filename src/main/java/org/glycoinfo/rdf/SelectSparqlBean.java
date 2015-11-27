package org.glycoinfo.rdf;

import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.dao.SparqlEntityFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;
/**
 *  * Provides a convenient base class for creating a {@link SelectSparql}
 * instance. The implementation allows customization by overriding methods.
 * 
 * @author aoki
 *
 */
public class SelectSparqlBean implements SelectSparql, InitializingBean {

	protected String define, prefix, select, where, from, orderby, groupby, construct, having,
			limit, offset;
	StringBuffer sparql;
	SparqlEntity sparqlEntity = new SparqlEntity();

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
		SparqlEntityFactory.set(sparqlentity);
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
			sparqlbuf.append(getDefine() != null ? getDefine() + " " : " ");
			sparqlbuf.append(getPrefix() != null ? getPrefix() + " " : " ");
			sparqlbuf.append("SELECT " + getSelect() + " ");
			sparqlbuf.append(getFrom() != null ? getFrom() + " " : " ");
//			sparqlbuf.append(getFrom() != null ? " FROM <" + getFrom() + ">"
//					: "");
			// sparqlbuf.append(matchStatement != null ? " MATCH " + matchStatement
			// :
			// "");
			String where = getWhere();
			sparqlbuf.append(where != null ? "WHERE {\n" + where + "} "
					: " ");
			// sparqlbuf.append(" RETURN ").append(returnStatement);
			sparqlbuf.append(getOrderBy() != null ?  getOrderBy().contains("ORDER BY")? getOrderBy() : " ORDER BY " + getOrderBy()
					+ " " : " ");

			sparqlbuf.append(getLimit() != null ? " LIMIT " + getLimit() + " "
					: " ");
			sparqlbuf.append(getOffset() != null ? " OFFSET " + getOffset() + " "
					: " ");
//			logger.debug(sparqlbuf.toString());
			return sparqlbuf.toString();
		} else {
			logger.debug(this.sparql.toString());
			return this.sparql.toString();
		}
	}

	@Override
	public String getDefine() {
		return define;
	}

	@Override
	public void setDefine(String define) {
		this.define = define;
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.state(getSelect() != null, "A select is required");
	}
}