package org.glycoinfo.rdf.glycan;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.rdf.SelectSparqlBean;
import org.glycoinfo.rdf.SparqlException;
import org.springframework.stereotype.Component;

/**
 * 
 * SelectSparql for retrieving a saccharide based on identifier.
 * The filter removes any existing sequences in the getTo() of the GlyConvert.
 * 
 * For instance: Retrieving of original glycoct by using
 * org.glycoinfo.conversion.wurcs.GlycoctToWurcsConverter.
 * 
 * @author aoki
 *
 */
@Component
public class SaccharideSelectSparql extends SelectSparqlBean {
	public static final String SaccharideURI = Saccharide.URI;
	public static final String Sequence = "Sequence";
	public static final String GlycanSequenceURI = "GlycanSequenceURI";
	public static final String AccessionNumber = Saccharide.PrimaryId;

	public SaccharideSelectSparql(String sparql) {
		super(sparql);
	}

	public SaccharideSelectSparql() {
		super();
		this.prefix = "PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
				+ "PREFIX glytoucan: <http://www.glytoucan.org/glyco/owl/glytoucan#>\n";
		this.select = "DISTINCT ?" + SaccharideURI;
//		this.from = "FROM <http://rdf.glytoucan.org>\n";
	}
	
	public String getPrimaryId() {
		return "\"" + getSparqlEntity().getValue(Saccharide.PrimaryId) + "\"^^xsd:string";
	}


	@Override
	public String getWhere() throws SparqlException {
		this.where = "?" + SaccharideURI + " a glycan:saccharide .\n"
				+ "?" + SaccharideURI + " glytoucan:has_primary_id " + getPrimaryId() + " .\n";
		return where;
	}

	protected Log logger = LogFactory.getLog(getClass());
}