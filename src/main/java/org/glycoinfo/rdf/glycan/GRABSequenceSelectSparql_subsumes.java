package org.glycoinfo.rdf.glycan;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.rdf.SelectSparqlBean;
import org.glycoinfo.rdf.SparqlException;
import org.springframework.stereotype.Component;

/**
 * 
 *  GRAB graph selectSparql for subsumes 
 * 
 * @author tokunaga
 *
 */

@Component
public class GRABSequenceSelectSparql_subsumes extends SelectSparqlBean {
	
	public static final String SaccharideURI = Glycosidic_topology.URI;
	public static final String id = "id";

	public GRABSequenceSelectSparql_subsumes(String sparql) {
		super(sparql);
	}

	public GRABSequenceSelectSparql_subsumes() {
		super();
		this.prefix = "PREFIX glytoucan: <http://www.glytoucan.org/glyco/owl/glytoucan#>\n"
				+"PREFIX rocs: <http://www.glycoinfo.org/glyco/owl/relation#>\n"
				+"PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n";
		this.select = "DISTINCT ?id ?subsumes_id\n";
		this.from = "FROM <http://rdf.glytoucan.org/core>\nFROM <http://rdf.glytoucan.org/topology>\nFROM <http://rdf.glytoucan.org/composition>\nFROM <http://rdf.glytoucan.org/base_composition>\nFROM <http://rdf.glytoucan.org/sequence/iupac_extended>\n";
		this.orderby = "ORDER BY ?iupac \n";
	}

	public String getPrimaryId() {
		return "\"" + getSparqlEntity().getValue(Glycosidic_topology.PrimaryId_1) + "\"";
	}

	@Override
	public String getWhere() throws SparqlException {
		this.where = "VALUES ?id {" + getPrimaryId() + "}\n"
				+ "VALUES ?has_topology { rocs:has_topology }\n"
				+ "VALUES ?has_composition { rocs:has_composition }\n"
				+ "VALUES ?has_base_composition { rocs:has_base_composition }\n"
				+ "OPTIONAL {\n" 
				+ "?s glytoucan:has_primary_id ?id .\n"
				+ "?s ?has_topology ?ht .\n"
				+ "?ht glytoucan:has_primary_id ?subsumes_id .\n" 
				+ "?ht a rocs:Glycosidic_topology .\n"
				+ "?ht glycan:has_glycosequence ?seq .\n"
				+ "?seq glycan:has_sequence ?iupac .\n" + "}\n"
				+ "OPTIONAL {\n" 
				+ "?s glytoucan:has_primary_id ?id .\n"
				+ "?s ?has_composition ?hc .\n"
				+ "?hc glytoucan:has_primary_id ?subsumes_id .\n" 
				+ "?hc a rocs:Composition .\n"
				+ "?hc glycan:has_glycosequence ?seq .\n"
				+ "?seq glycan:has_sequence ?iupac .\n" + "}\n"
				+ "OPTIONAL {\n" 
				+ "?s glytoucan:has_primary_id ?id .\n"
				+ "?s ?has_base_composition ?hbc .\n"
				+ "?hbc glytoucan:has_primary_id ?subsumes_id .\n" 
				+ "?hbc a rocs:Base_Composition .\n"
				+ "?hbc glycan:has_glycosequence ?seq .\n"
				+ "?seq glycan:has_sequence ?iupac .\n" + "}\n"
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

}
