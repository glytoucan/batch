package org.glycoinfo.rdf.scint;

import java.util.ArrayList;
import java.util.List;

import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.glycoinfo.rdf.schema.DomainSchemaSelectSparql;

@Component
public class ClassHandler {

	public static Logger logger = (Logger) LoggerFactory
			.getLogger("org.glycoinfo.rdf.scint.Scint");
	
	@Autowired
	SparqlDAO schemaDAO;

	// domains and ranges are the properties and class names in a list.
	List<String> domains, ranges;

	private String prefix;

	private String prefixIri;

	private String className;
	
	public ClassHandler() {
	}
	
	public ClassHandler(String prefix, String prefixIri, String className) {
		this.prefix=prefix;
		this.prefixIri=prefixIri;
		this.className=className;
	}

	public List<String> getDomains() throws SparqlException {
		SelectSparql domainselect = new DomainSchemaSelectSparql(this);
		logger.debug(domainselect.getSparql());
		List<SparqlEntity> results = schemaDAO.query(domainselect);
		if (domains==null)
			domains = new ArrayList<String>();
		for (SparqlEntity sparqlEntity : results) {
			domains.add(sparqlEntity.getValue("result"));
		}
		return domains;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getPrefixIri() {
		return prefixIri;
	}

	public void setPrefixIri(String prefixIri) {
		this.prefixIri = prefixIri;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public void setSparqlDAO(SparqlDAO sparqlDAO) {
		this.schemaDAO = sparqlDAO;
	}
}