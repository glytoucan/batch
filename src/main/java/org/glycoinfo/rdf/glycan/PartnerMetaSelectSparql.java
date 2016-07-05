package org.glycoinfo.rdf.glycan;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.rdf.SelectSparqlBean;
import org.glycoinfo.rdf.SparqlException;
import org.springframework.stereotype.Component;

/*
 * SelectSparql for retrieving a partner base.
 * 
 * @author shinmachi
 * 
 */
@Component
public class PartnerMetaSelectSparql extends SelectSparqlBean implements Partner {
	
	public PartnerMetaSelectSparql(String sparql) {
		super(sparql);
	}
	
	public PartnerMetaSelectSparql() {
		super();
		this.prefix = "\n\n"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
				+ "\n"
				+ "PREFIX dcterms: <http://purl.org/dc/terms/>"
				+ "\n"
				+ "PREFIX glytoucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>"
				+ "\n" ;

		this.select = "DISTINCT * "
				+ "\n";

//		this.from = "FROM <http://test>"
//				+ "\n";
	}
	
	@Override
	public String getWhere() throws SparqlException {
		String where = "\n"
				+ "?s a glytoucan:Partner ."
				+ "\n"
				+ "?s ?p ?o ."
				+ "\n";
		return where;
	}
	
	protected Log logger = LogFactory.getLog(getClass());
}
