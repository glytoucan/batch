package org.glycoinfo.rdf.scint;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SelectSparqlBean;
import org.glycoinfo.rdf.SparqlBean;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.UriProvider;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.schema.SchemaSparqlFormatter;
import org.springframework.stereotype.Component;

@Component
public class SelectScint extends ScintillateParent implements UriProvider {

	SelectSparqlBean sparqlBean;
	

	public SelectScint() throws SparqlException {
		this.sparqlBean = new SelectSparqlBean();
		this.sparqlBean.setLimit("10");
	}
	
	public SelectScint(String prefix, String prefixIri, String className) throws SparqlException {
		super(prefix, prefixIri, className);
		this.sparqlBean = new SelectSparqlBean();
	}
	
	void init() throws SparqlException {
		update();
	}

	public void update() throws SparqlException {
		this.sparqlBean.setPrefix(SchemaSparqlFormatter.getPrefixDefinition(this));
		
		this.sparqlBean.setSelect("?" + SelectSparql.URI + " ");
		this.sparqlBean.setWhere("");
		if (null != getSparqlEntity()) {
			if (null != getSparqlEntity().getValue(SelectSparql.PRIMARY_KEY))
				this.sparqlBean.setWhere(SchemaSparqlFormatter.getAClassWhere(SchemaSparqlFormatter.getUri(this, getSparqlEntity().getValue(SelectSparql.PRIMARY_KEY)), this) + " \n");
			else
			  this.sparqlBean.setWhere(SchemaSparqlFormatter.getAClassWhere("?" + SelectSparql.URI, this) + " \n");
			
			Set<String> columns = getSparqlEntity().getColumns();

			List<String> domains = null;
			try {
				domains = getDomains();
//				if (!domains.contains(columns)) {
//					logger.warn("not all columns contained in domains");
//				} doesnt work since columns may not be full uri
			} catch (SparqlException e) {
				e.printStackTrace();
			}

			for (String column: columns) {
				if (column.equals(SelectSparql.PRIMARY_KEY) || column.equals(NO_DOMAINS))
					continue;
				// add any columns requested to the select line
				this.sparqlBean.setSelect(this.sparqlBean.getSelect() + "?" + column + " ");
				// finding with primary key is very easy, simply where with primary + type
				if (null != getSparqlEntity().getValue(SelectSparql.PRIMARY_KEY)) {
					this.sparqlBean.setWhere(this.sparqlBean.getWhere() + SchemaSparqlFormatter.getDomainWhere(SchemaSparqlFormatter.getUri(this, getSparqlEntity().getValue(SelectSparql.PRIMARY_KEY)), this, column) + " \n");
//							+ SchemaSparqlFormatter.getDomainWhere("?uri", classHandler, column) + " \n";
				} else {
					// otherwise construct the where itself, select ?column where ?a column "value" and ignore primary key
					if (StringUtils.isNotBlank(getSparqlEntity().getValue(column) )) {
						this.sparqlBean.setWhere(this.sparqlBean.getWhere() + "?" + SelectSparql.URI + " " + getPrefix() + ":" + column +  " ?" + column +" .\n");
						if (getSparqlEntity().getObjectValue(column) instanceof UriProvider)
							this.sparqlBean.setWhere(this.sparqlBean.getWhere() + "?" + SelectSparql.URI + " " + getPrefix() + ":" + column +  " " + ((UriProvider)getSparqlEntity().getObjectValue(column)).getUri() + " .\n");
						else
							this.sparqlBean.setWhere(this.sparqlBean.getWhere() + SchemaSparqlFormatter.getInsert("?" + SelectSparql.URI , this, column, getSparqlEntity().getObjectValue(column)));
//							this.sparqlBean.setWhere(this.sparqlBean.getWhere() + "?" + SelectSparql.URI + " " + getPrefix() + ":" + column +  " \"" + getSparqlEntity().getValue(column) + "\" .\n");
						
					} else {
						if (column.contains(":"))
							this.sparqlBean.setWhere(this.sparqlBean.getWhere() + "?" + SelectSparql.URI + " " + column +  " ?" + column +" .\n");
						else
							this.sparqlBean.setWhere(this.sparqlBean.getWhere() + "?" + SelectSparql.URI + " " + getPrefix() + ":" + column +  " ?" + column +" .\n");
					}
				}
			}
			
			/*
			 * prefix schema: <http://schema.org/>
prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#>
prefix rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
SELECT ?result 
FROM <http://schema.org> 
WHERE {
# schema:Person rdf:type owl:Class . 
?result rdfs:domain [
      rdf:type owl:Class ;
      owl:unionOf (
          schema:ContactPoint
          schema:Organization
          schema:Person
        ) 
    ]  .
}
			 */
			if (!columns.contains(NO_DOMAINS)) {
			for(String domain: domains) {
				if (StringUtils.isNotBlank(getSparqlEntity().getValue(domain))) {
					continue;
				}
				this.sparqlBean.setSelect(this.sparqlBean.getSelect() + "?" + domain + " \n");
				if (columns.contains(SelectSparql.PRIMARY_KEY)) {
					// if we have the primary key, uri should be primary key address.
					this.sparqlBean.setWhere(this.sparqlBean.getWhere() + "OPTIONAL { " + getUri() + " " + getPrefix() + ":" + domain +  " ?" + domain + " . } \n");
				} else {
					this.sparqlBean.setWhere(this.sparqlBean.getWhere() + "OPTIONAL { ?" + SelectSparql.URI + " " + getPrefix() + ":" + domain +  " ?" + domain + " . } \n");
				}
			}
			}
		}
	}

	public SparqlEntity getSparqlEntity() {
		return sparqlBean.getSparqlEntity();
	}
	
	@Override
	public String getUri() throws SparqlException {
		if (null == getSparqlEntity() && null == getSparqlEntity().getValue(SelectSparql.PRIMARY_KEY) && null == getSparqlEntity().getValue(SelectSparql.URI))
			throw new SparqlException("need at least sparqlentity or URI");
		if (null != getSparqlEntity().getValue(SelectSparql.URI))
			return "<" + getSparqlEntity().getValue(SelectSparql.URI) + ">";
		return SchemaSparqlFormatter.getUri(this, getSparqlEntity().getObjectValue(SelectSparql.PRIMARY_KEY));
	}
	
	public String getPrimaryKey() throws SparqlException {
		if (null == getSparqlEntity() || null == getSparqlEntity().getValue(SelectSparql.URI))
			throw new SparqlException("need at least sparqlentity or URI");

		return SchemaSparqlFormatter.getPrimaryKey(this, getSparqlEntity().getValue(SelectSparql.URI));
	}

	@Override
	public SelectSparql getSparqlBean() {
		return sparqlBean;
	}

	@Override
	public void setSparqlBean(SparqlBean sparql) {
		this.sparqlBean = (SelectSparqlBean) sparql;
	}
}