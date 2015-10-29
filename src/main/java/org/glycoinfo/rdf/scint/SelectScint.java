package org.glycoinfo.rdf.scint;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SelectSparqlBean;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.UriProvider;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.schema.SchemaSparqlFormatter;
import org.springframework.stereotype.Component;

@Component
public class SelectScint extends SelectSparqlBean implements UriProvider {
	
	ClassHandler classHandler;
	
	public ClassHandler getClassHandler() {
		return classHandler;
	}

	public void setClassHandler(ClassHandler classHandler) {
		this.classHandler = classHandler;
	}

	public SelectScint() {
		super.limit = "10";
	}
	
	void init() {
		this.prefix = SchemaSparqlFormatter.getPrefixDefinition(getClassHandler());
		update();
	}	

	@Override
	public void setSparqlEntity(SparqlEntity sparqlentity) {
		super.setSparqlEntity(sparqlentity);
		init();
	}
	
	public void update() {
		
		this.select="?" + URI + " ";
		this.where = "";
		if (null != getSparqlEntity()) {
			if (null != getSparqlEntity().getValue(SelectSparql.PRIMARY_KEY))
				this.where = SchemaSparqlFormatter.getAClassWhere(SchemaSparqlFormatter.getUri(classHandler, getSparqlEntity().getValue(SelectSparql.PRIMARY_KEY)), classHandler) + " \n";
			

			Set<String> columns = getSparqlEntity().getColumns();

			List<String> domains = null;
			try {
				domains = classHandler.getDomains();
//				if (!domains.contains(columns)) {
//					logger.warn("not all columns contained in domains");
//				} doesnt work since columns may not be full uri
			} catch (SparqlException e) {
				e.printStackTrace();
			}

			for (String column: columns) {
				if (column.equals(SelectSparql.PRIMARY_KEY) || column.equals(SelectSparql.NO_DOMAINS))
					continue;
				// add any columns requested to the select line
				this.select += "?" + column + " ";
				// finding with primary key is very easy, simply where with primary + type
				if (null != getSparqlEntity().getValue(SelectSparql.PRIMARY_KEY)) {
					this.where += SchemaSparqlFormatter.getDomainWhere(SchemaSparqlFormatter.getUri(classHandler, getSparqlEntity().getValue(SelectSparql.PRIMARY_KEY)), classHandler, column) + " \n";
//							+ SchemaSparqlFormatter.getDomainWhere("?uri", classHandler, column) + " \n";
				} else {
					// otherwise construct the where itself, select ?column where ?a column "value" and ignore primary key
					if (StringUtils.isNotBlank(getSparqlEntity().getValue(column) )) {
						this.where += "?" + URI + " " + classHandler.getPrefix() + ":" + column +  " ?" + column +" .\n";
						if (getSparqlEntity().getObjectValue(column) instanceof UriProvider)
							this.where += "?" + URI + " " + classHandler.getPrefix() + ":" + column +  ((UriProvider)getSparqlEntity().getObjectValue(column)).getUri() + " .\n";
						else
							this.where += "?" + URI + " " + classHandler.getPrefix() + ":" + column +  " \"" + getSparqlEntity().getValue(column) + "\" .\n";
					} else {
						if (column.contains(":"))
							this.where += "?" + URI + " " + column +  " ?" + column +" .\n";
						else
							this.where += "?" + URI + " " + classHandler.getPrefix() + ":" + column +  " ?" + column +" .\n";
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
			if (!columns.contains(SelectSparql.NO_DOMAINS)) {
			for(String domain: domains) {
				this.select += "?" + domain + " \n";
				if (columns.contains(SelectSparql.PRIMARY_KEY)) {
					// if we have the primary key, uri should be primary key address.
					this.where += "OPTIONAL { " + getUri() + " " + classHandler.getPrefix() + ":" + domain +  " ?" + domain + " . } \n";
				} else {
					this.where += "OPTIONAL { ?" + URI + " " + classHandler.getPrefix() + ":" + domain +  " ?" + domain + " . } \n";
				}
			}
			}
		}
	}

	@Override
	public String getUri() {
//		if (null == getSparqlEntity() || null == getSparqlEntity().getValue(SelectSparql.PRIMARY_KEY))
//			throw new SparqlException("need at least sparqlentity or URI");
		return SchemaSparqlFormatter.getUri(classHandler, getSparqlEntity().getObjectValue(SelectSparql.PRIMARY_KEY));
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
		// this needs to be here...
	}
	
	public String getPrimaryKey() throws SparqlException {
		if (null == getSparqlEntity() || null == getSparqlEntity().getValue(SelectSparql.URI))
			throw new SparqlException("need at least sparqlentity or URI");

		return SchemaSparqlFormatter.getPrimaryKey(classHandler, getSparqlEntity().getValue(SelectSparql.URI));
	}
}