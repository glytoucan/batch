package org.glycoinfo.rdf.glycan;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.rdf.SelectSparqlBean;
import org.glycoinfo.rdf.SparqlException;
import org.springframework.stereotype.Component;

/**
 * 
 *  GRAB graph selectSparql for subsumed by
 *  order by iupac literal
 * 
 * @author tokunaga
 *
 */

@Component
public class GRABSequenceSelectSparql_subsumedby extends SelectSparqlBean {
	
	public static final String SaccharideURI = Glycosidic_topology.URI;
	public static final String id = "id";

	public GRABSequenceSelectSparql_subsumedby(String sparql) {
		super(sparql);
	}

	public GRABSequenceSelectSparql_subsumedby() {
		super();
		this.prefix = "PREFIX glytoucan: <http://www.glytoucan.org/glyco/owl/glytoucan#>\n"
				+"PREFIX rocs: <http://www.glycoinfo.org/glyco/owl/relation#>\n"
				+ "PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n";
		this.select = "DISTINCT ?id ?subsumedby_id\n";
		this.from = "FROM <http://rdf.glytoucan.org/core>\nFROM <http://rdf.glytoucan.org/topology>\nFROM <http://rdf.glytoucan.org/composition>\nFROM <http://rdf.glytoucan.org/base_composition>\nFROM <http://rdf.glytoucan.org/sequence/iupac_extended>\n";
		//this.orderby = "ORDER BY ?iupac \n";
	}

	public String getPrimaryId() {
		return "\"" + getSparqlEntity().getValue(Glycosidic_topology.PrimaryId_2) + "\"";
	}

	@Override
	public String getWhere() throws SparqlException {
		this.where = "VALUES ?subsumedby_id {" + getPrimaryId() + "}\n"
				+ "VALUES ?has_topology { rocs:has_topology }\n"
				+ "VALUES ?has_composition { rocs:has_composition }\n"
				+ "VALUES ?has_base_composition { rocs:has_base_composition }\n"
				+ "OPTIONAL {\n" 
				+ "?s glytoucan:has_primary_id ?id .\n"
				+ "?s ?has_topology ?ht .\n"
				+ "?ht glytoucan:has_primary_id ?subsumedby_id .\n" 
				+ "?ht a rocs:Glycosidic_topology .\n" 
				+ "?s glycan:has_glycosequence ?seq .\n"
				+ "?seq glycan:has_sequence ?iupac .\n" + "}\n"
				+ "OPTIONAL {\n" 
				+ "?s glytoucan:has_primary_id ?id .\n"
				+ "?s ?has_composition ?hc .\n"
				+ "?hc glytoucan:has_primary_id ?subsumedby_id .\n" 
				+ "?hc a rocs:Composition .\n"
				+ "?s glycan:has_glycosequence ?seq .\n"
				+ "?seq glycan:has_sequence ?iupac .\n" + "}\n"
				+ "OPTIONAL {\n" 
				+ "?s glytoucan:has_primary_id ?id .\n"
				+ "?s ?has_base_composition ?hbc .\n"
				+ "?hbc glytoucan:has_primary_id ?subsumedby_id .\n" 
				+ "?hbc a rocs:Base_Composition .\n"
				+ "?s glycan:has_glycosequence ?seq .\n"
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
