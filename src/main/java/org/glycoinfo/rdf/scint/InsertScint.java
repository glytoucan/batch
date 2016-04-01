package org.glycoinfo.rdf.scint;

import java.util.List;
import java.util.Set;

import org.glycoinfo.rdf.InsertSparqlBean;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.UriProvider;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.schema.SchemaSparqlFormatter;
import org.springframework.stereotype.Component;

@Component
public class InsertScint extends InsertSparqlBean implements UriProvider {

	private ClassHandler classHandler;

	public ClassHandler getClassHandler() {
		return classHandler;
	}

	public void setClassHandler(ClassHandler classHandler) {
		this.classHandler = classHandler;
		init();
	}

	public InsertScint() {
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
			if (classHandler != null) {
				
				this.insert=SchemaSparqlFormatter.getAInsert(getUri(), classHandler);
				List<String> domains;
				try {
				Set<String> columns = getSparqlEntity().getColumns();
				domains = classHandler.getDomains();
	
				for (String column : columns) {
					if (column.equals(SelectSparql.PRIMARY_KEY) || column.equals(Scintillate.NO_DOMAINS))
						continue;
					if (!domains.contains(column)) {
						// TODO: this doesnt take into consideration subClass relationships
						logger.warn("field:>" + column + "< is not a predicate of this class>" + classHandler.getClassName() + "<");
					}
					this.insert += SchemaSparqlFormatter.getInsert(getUri(), classHandler, column, getSparqlEntity().getObjectValue(column));
				}
				} catch (SparqlException e) {
					e.printStackTrace();
				}
			} else
				logger.warn("please set classHandler to autofill sparql");
		} else
			logger.warn("no SparqlEntity.  Needed for primary key and columns.");
	}

	@Override
	public String getUri() {
		return SchemaSparqlFormatter.getUri(classHandler, getSparqlEntity().getObjectValue(SelectSparql.PRIMARY_KEY));
	}
}