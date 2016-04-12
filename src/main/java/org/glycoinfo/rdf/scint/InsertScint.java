package org.glycoinfo.rdf.scint;


import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.rdf.InsertSparqlBean;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.UriProvider;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.schema.SchemaSparqlFormatter;
import org.springframework.stereotype.Component;

@Component
public class InsertScint extends Scintillate implements UriProvider {

	private static final Log logger = LogFactory.getLog(InsertScint.class);
	
	InsertSparqlBean sparqlBean;
	
	public InsertScint() {
		sparqlBean=new InsertSparqlBean();
	}
	
	public InsertScint(String prefix, String prefixIri, String className) throws SparqlException {
		super(prefix, prefixIri, className);
		this.sparqlBean = new InsertSparqlBean();
		setPrefix();
	}
	
	public void setPrefix() {
		this.sparqlBean.setPrefix(SchemaSparqlFormatter.getPrefixDefinition(this) + "\n");
	}

	public void updateSparql() throws SparqlException {
		if (getSparqlEntity() != null) {
			this.sparqlBean.setInsert(SchemaSparqlFormatter.getAInsert(getUri(), this));
				List<String> domains;
				try {
				Set<String> columns = getSparqlEntity().getColumns();
				domains = getDomains();
	
				for (String column : columns) {
					if (column.equals(SelectSparql.PRIMARY_KEY) || column.equals(Scintillate.NO_DOMAINS))
						continue;
					if (!domains.contains(column)) {
						// TODO: this doesn't take into consideration subClass relationships
						logger.warn("field:>" + column + "< is not a predicate of this class>" + getClassName() + "<");
					}
					this.sparqlBean.setInsert(this.sparqlBean.getInsert() + SchemaSparqlFormatter.getInsert(getUri(), this, column, getSparqlEntity().getObjectValue(column)));
				}
				} catch (SparqlException e) {
					e.printStackTrace();
				}
		} else
			logger.warn("no SparqlEntity.  Needed for primary key and columns.");
	}

	private SparqlEntity getSparqlEntity() {
		return this.sparqlBean.getSparqlEntity();
	}

	@Override
	public String getUri() throws SparqlException {
		if (null == getSparqlEntity() || null == getSparqlEntity().getValue(SelectSparql.PRIMARY_KEY) && null == getSparqlEntity().getValue(SelectSparql.URI))
			throw new SparqlException("need at least sparqlentity or URI");
		if (null != getSparqlEntity().getValue(SelectSparql.URI))
			return "<" + getSparqlEntity().getValue(SelectSparql.URI) + ">";
		return SchemaSparqlFormatter.getUri(this, getSparqlEntity().getObjectValue(SelectSparql.PRIMARY_KEY));
	}
	
	public InsertSparqlBean getSparqlBean() throws SparqlException {
		return sparqlBean;
	}

}