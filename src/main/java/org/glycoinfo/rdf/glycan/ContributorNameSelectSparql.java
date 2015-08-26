package org.glycoinfo.rdf.glycan;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.rdf.SelectSparqlBean;
import org.glycoinfo.rdf.SparqlException;

public class ContributorNameSelectSparql extends SelectSparqlBean implements Contributor {

	public static Log logger = (Log) LogFactory.getLog(ContributorNameSelectSparql.class);

	/**
	 * PREFIX glytoucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>
SELECT ?id
FROM <http://rdf.glytoucan.org>
where {
?o a foaf:Person .
?o dcterms:identifier ?id .
}
	 * @param string 
	 * 
	 */
	public ContributorNameSelectSparql() {
		this.prefix = "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "PREFIX dcterms: <http://purl.org/dc/terms/>\n"
				+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/#>\n";
		this.select = "?" + Contributor.Id + "\n";
		this.where 
			= "?o a foaf:Person .\n"
			+ "BIND (STRAFTER(STR(?o), \"http://rdf.glycoinfo.org/glytoucan/contributor/userId/\") AS ?" + Contributor.Id + ") .\n";
	}

	@Override
	public String getWhere() throws SparqlException {
		
		return this.where + "?o foaf:name \"" + getSparqlEntity().getValue(Contributor.Name) + "\"^^xsd:string . \n";
	}
}