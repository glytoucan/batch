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
		this.select="";
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
				if (column.equals(SelectSparql.PRIMARY_KEY))
					continue;
				// add any columns requested to the select line
				this.select += "?" + column + " ";
				// finding with primary key is very easy, simply where with primary + type
				if (null != getSparqlEntity().getValue(SelectSparql.PRIMARY_KEY))
					this.where += SchemaSparqlFormatter.getDomainWhere(SchemaSparqlFormatter.getUri(classHandler, getSparqlEntity().getValue(SelectSparql.PRIMARY_KEY)), classHandler, column) + " \n";
				else {
					// otherwise construct the where itself, select ?column where ?a column "value" and ignore primary key
					if (StringUtils.isNotBlank(getSparqlEntity().getValue(column) )) {
						this.where += "?uri " + classHandler.getPrefix() + ":" + column +  " ?" + column +" .\n";
						this.where += "?uri " + classHandler.getPrefix() + ":" + column +  " \"" + getSparqlEntity().getValue(column) + "\" .\n";
					} else {
						if (column.contains(":"))
							this.where += "?uri " + column +  " ?" + column +" .\n";
						else
							this.where += "?uri " + classHandler.getPrefix() + ":" + column +  " ?" + column +" .\n";
					}
				}
			}
			
			for(String domain: domains) {
				this.select += "?" + domain + " \n";
				this.where += "OPTIONAL { ?uri " + classHandler.getPrefix() + ":" + domain +  " ?" + domain + " . } \n";
			}
		}
	}

	@Override
	public String getUri() {
		return SchemaSparqlFormatter.getUri(classHandler, getSparqlEntity().getObjectValue(SelectSparql.PRIMARY_KEY));
	}
	
	@Override
	public void afterPropertiesSet() throws Exception {
	}
}