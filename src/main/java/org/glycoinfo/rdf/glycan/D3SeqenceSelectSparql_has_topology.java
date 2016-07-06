package org.glycoinfo.rdf.glycan;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.rdf.SelectSparqlBean;
import org.glycoinfo.rdf.SparqlException;
import org.springframework.stereotype.Component;

/**
 * 
 * SelectSparql for Glycosidic_topology
 * 
 * @author tokunaga
 *
 */

@Component
public class D3SeqenceSelectSparql_has_topology extends SelectSparqlBean {

	// public static final String SaccharideURI = Saccharide.URI;
	// public static final String id = "id";
	// public static final String GlycanSequenceURI = "GlycanSequenceURI";
	// public static final String AccessionNumber = Saccharide.PrimaryId;
	
	public static final String SaccharideURI = Glycosidic_topology.URI;
	public static final String id = "id";

	public D3SeqenceSelectSparql_has_topology(String sparql) {
		super(sparql);
	}

	public D3SeqenceSelectSparql_has_topology() {
		super();
		this.prefix = "PREFIX glytoucan: <http://www.glytoucan.org/glyco/owl/glytoucan#>\n"
				+"PREFIX rocs: <http://www.glycoinfo.org/glyco/owl/relation#>\n";
		this.select = "DISTINCT ?id ?has_topology ?topology_id\n";
		this.from = "FROM <http://rdf.glytoucan.org/core>\nFROM <http://rdf.glytoucan.org/topology>\n";
//		this.from = "FROM <http://rdf.glycoinfo.org/glycan/browser/demo>";
	}

	public String getPrimaryId() {
		return "\"" + getSparqlEntity().getValue(Glycosidic_topology.PrimaryId_1) + "\"";
	}

	@Override
	public String getWhere() throws SparqlException {
		this.where = "VALUES ?id {" + getPrimaryId() + "}\n"
				+ "VALUES ?has_topology { rocs:has_topology }\n"
				+ "OPTIONAL {\n" 
				+ "?s glytoucan:has_primary_id ?id .\n"
				+ "?s ?has_topology ?ht .\n"
				+ "?ht glytoucan:has_primary_id ?topology_id .\n" 
				+ "?ht a rocs:Glycosidic_topology .\n" + "}\n"
		;
		return where;
	}

	protected Log logger = LogFactory.getLog(getClass());

	String glycanUri;

	/**
	 * 
	 * the filter removes any sequences that already have a sequence in the
	 * GlyConvert.getTo() format.
	 * 
	 * @return
	 */
	// public String getFilter() {
	// return "FILTER NOT EXISTS {\n" + "?" + SaccharideURI
	// + " glytoucan:has_derivatized_mass ?existingmass .\n}";
	// }

	// @Override
	// public void afterPropertiesSet() throws Exception {
	// Assert.state(getPrefix() != null, "A ident is required");
	// Assert.state(getSelect() != null, "A select is required");
	// }

}
