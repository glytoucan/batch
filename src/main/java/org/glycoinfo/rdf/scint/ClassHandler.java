package org.glycoinfo.rdf.scint;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.schema.DomainSchemaSelectSparql;
import org.glycoinfo.rdf.schema.SchemaSparqlFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ClassHandler {

  private static final Log logger = LogFactory.getLog(ClassHandler.class);
	
	@Autowired
	SparqlDAO sparqlDAO;

	// domains and ranges are the properties and class names in a list.
	List<String> domains, ranges;

	protected String prefix;

	protected String prefixIri;

	protected String className;
	
	private String uriBase;
	
	public ClassHandler() {
	}
	
	public ClassHandler(String prefix, String prefixIri, String className) {
		this.prefix=prefix;
		this.prefixIri=prefixIri;
		this.className=className;
	}

	public List<String> getDomains() throws SparqlException {
		// TODO: this doesnt take into consideration subClass relationships
		SelectSparql domainselect = new DomainSchemaSelectSparql(this);
		logger.debug(domainselect.getSparql());
		List<SparqlEntity> results = sparqlDAO.query(domainselect);
		if (domains==null)
			domains = new ArrayList<String>();
		else
			domains.clear();
		for (SparqlEntity sparqlEntity : results) {

			String result = SchemaSparqlFormatter.getDomainName(this, sparqlEntity.getValue("result"));
			logger.debug("result:>" + result + "<");
			if (null == result)
				continue;

			domains.add(result);
		}
		return domains;
	}

	/**
	 *
	 * Short prefix name of this class.  Used when defining the short name of the prefix.   
	 * 
	 * @return String short prefix name.
	 *
	 */
	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * 
	 * Return the Prefix IRI of this class.  Used when defining the Ontology uri, such as http://purl.jp/bio/12/glyco/glycan#.
	 * 
	 * @return String name of class.
	 * 
	 */
	public String getPrefixIri() {
		return prefixIri;
	}

	public void setPrefixIri(String prefixIri) {
		this.prefixIri = prefixIri;
	}

	/**
	 * 
	 * Return the name of this class.  Useful for shortening references such as in schema:Person.
	 * 
	 * @return String name of class.
	 * 
	 */
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	
	/**
	 * 
	 * Return the base Uri to be used for this class.  Used when definining the URI for an instance of this class.
	 * 
	 * @return String name of class.
	 * 
	 */
	public String getUriBase() {
		return uriBase;
	}

	public void setUriBase(String uriBase) {
		this.uriBase = uriBase;
	}

	public void setSparqlDAO(SparqlDAO sparqlDAO) {
		this.sparqlDAO = sparqlDAO;
	}
}