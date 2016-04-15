package org.glycoinfo.rdf.scint;

import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.rdf.DeleteSparql;
import org.glycoinfo.rdf.DeleteSparqlBean;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SparqlBean;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.UriProvider;
import org.glycoinfo.rdf.schema.SchemaSparqlFormatter;
import org.springframework.stereotype.Component;

@Component
public class DeleteScint extends ScintillateParent implements UriProvider {

	private static final Log logger = LogFactory.getLog(DeleteScint.class);
	
	DeleteSparqlBean sparqlBean;

	public DeleteScint() {
	}
	
	public DeleteScint(String graph, String prefix, String prefixIri, String className) throws SparqlException {
		super(prefix, prefixIri, className);
		setSparqlBean(new DeleteSparqlBean());
		this.sparqlBean.setGraph(graph);
		setPrefix();
	}
	
	public void setPrefix() {
		this.sparqlBean.setPrefix(SchemaSparqlFormatter.getPrefixDefinition(this) + "\n");
	}

	public void init() {
		this.prefix = SchemaSparqlFormatter.getPrefixDefinition(this) + "\n";
	}

	@Override
	public void update() {
		if (sparqlBean.getSparqlEntity() != null) {
			sparqlBean.setDelete(SchemaSparqlFormatter.getAInsert(getUri(), this));
			List<String> domains;
			try {
			Set<String> columns = sparqlBean.getSparqlEntity().getColumns();
			domains = getDomains();

			for (String column : columns) {
				if (column.equals(SelectSparql.PRIMARY_KEY) || column.equals(Scintillate.NO_DOMAINS))
					continue;
				if (!domains.contains(column)) {
					// TODO: this doesnt take into consideration subClass relationships
					logger.warn("field:>" + column + "< is not a predicate of this class>" + getClassName() + "<");
				}
				sparqlBean.setDelete(sparqlBean.getDelete() + SchemaSparqlFormatter.getInsert(getUri(), this, column, sparqlBean.getSparqlEntity().getObjectValue(column)));
			}
			} catch (SparqlException e) {
				e.printStackTrace();
			}
		} else
			logger.warn("no SparqlEntity.  Needed for primary key and columns.");
	}

	@Override
	public String getUri() {
		return SchemaSparqlFormatter.getUri(this, sparqlBean.getSparqlEntity().getObjectValue(SelectSparql.PRIMARY_KEY));
	}

	@Override
	public DeleteSparql getSparqlBean() {
		return sparqlBean;
	}

	@Override
	public void setSparqlBean(SparqlBean sparql) {
		this.sparqlBean = (DeleteSparqlBean) sparql;
	}
}