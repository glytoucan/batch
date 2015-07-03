package org.glycoinfo.rdf.scint;

import java.util.List;
import java.util.Set;

import org.glycoinfo.rdf.InsertSparqlBean;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SelectSparqlBean;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.UriProvider;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.schema.SchemaSparqlFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

//@Component
public class InsertScint extends InsertSparqlBean implements UriProvider {
	
	private ClassHandler classHandler;

	public ClassHandler getClassHandler() {
		return classHandler;
	}

	public void setClassHandler(ClassHandler classHandler) {
		this.classHandler = classHandler;
		init();
	}

	public InsertScint(String graph) {
		this.graph = graph;
	}
	
	public void init() {
		this.prefix = SchemaSparqlFormatter.getPrefixDefinition(getClassHandler()) + "\n";
	}

	@Override
	public void setSparqlEntity(SparqlEntity sparqlentity) {
		super.setSparqlEntity(sparqlentity);
		update();
	}
	
	public void update() {
		if (getSparqlEntity() != null) {
			this.insert=SchemaSparqlFormatter.getAInsert(getUri(), classHandler);
			Set<String> columns = getSparqlEntity().getColumns();
	
			List<String> domains;
			try {
				domains = classHandler.getDomains();
				if (!domains.contains(columns)) {
					logger.warn("not all columns contained in actual domains");
				}
			} catch (SparqlException e) {
				e.printStackTrace();
			}

			for (String column : columns) {
				if (column.equals(SelectSparql.PRIMARY_KEY))
					continue;
				this.insert += SchemaSparqlFormatter.getInsert(getUri(), classHandler, column, getSparqlEntity().getObjectValue(column));
			}
		}
	}

	@Override
	public String getUri() {
		return SchemaSparqlFormatter.getUri(classHandler, getSparqlEntity().getObjectValue(SelectSparql.PRIMARY_KEY));
	}
}