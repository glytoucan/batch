package org.glycoinfo.rdf.scint;

import java.util.List;
import java.util.Set;

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
		update();
		init();
	}
	
	public void update() {
		this.select="";
		this.where = SchemaSparqlFormatter.getAClassWhere(SchemaSparqlFormatter.getUri(classHandler, getSparqlEntity().getValue(SelectSparql.PRIMARY_KEY)), classHandler) + " \n";
		if (null != getSparqlEntity()) {
			Set<String> columns = getSparqlEntity().getColumns();

			List<String> domains;
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
				this.select += "?" + column + " ";
				this.where  += SchemaSparqlFormatter.getDomainWhere(SchemaSparqlFormatter.getUri(classHandler, getSparqlEntity().getValue(SelectSparql.PRIMARY_KEY)), classHandler, column) + " \n";
			}
		}
	}

	@Override
	public String getUri() {
		return SchemaSparqlFormatter.getUri(classHandler, getSparqlEntity().getObjectValue(SelectSparql.PRIMARY_KEY));
	}
}