package org.glycoinfo.rdf.glycan.wurcs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.rdf.SelectSparqlBean;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.glycan.Saccharide;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 
 * SelectSparql for retrieving the Wurcs of 
 * The filter removes any existing sequences in the getTo() of the GlyConvert.
 * 
 * For instance: Retrieving of original glycoct by using
 * org.glycoinfo.conversion.wurcs.GlycoctToWurcsConverter.
 * 
 * @author aoki
 *
 */
@Component
public class GlycoSequenceSelectSparql extends SelectSparqlBean implements InitializingBean {
	public static final String SaccharideURI = Saccharide.URI;
	public static final String Sequence = "Sequence";
	public static final String GlycanSequenceURI = "GlycanSequenceURI";
	public static final String AccessionNumber = Saccharide.PrimaryId;

	private boolean whereset = false;
	public GlycoSequenceSelectSparql(String sparql) {
		super(sparql);
	}

	public GlycoSequenceSelectSparql() {
		super();
		this.prefix = "PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
				+ "PREFIX glytoucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>\n";
		this.select = "DISTINCT ?" + Sequence + "\n"
				+ "?" + AccessionNumber + "\n";
		this.from = "FROM <http://rdf.glytoucan.org>\n"
				+ "FROM <http://rdf.glytoucan.org/sequence/wurcs>\n";
		this.where = "?" + SaccharideURI + " a glycan:saccharide .\n"
				+ "?" + SaccharideURI + " glytoucan:has_primary_id ?" + AccessionNumber + " .\n"
				+ "?" + SaccharideURI + " glycan:has_glycosequence ?" + GlycanSequenceURI + " .\n"
				+ "?" + GlycanSequenceURI + " glycan:has_sequence ?" + Sequence + " .\n"
				+ "?" + GlycanSequenceURI + " glycan:in_carbohydrate_format glycan:carbohydrate_format_wurcs .\n"
//						+ getFilter()
				;
	}
	
	public String getPrimaryId() {
		return "\"" + getSparqlEntity().getValue(Saccharide.PrimaryId) + "\"";
	}


	@Override
	public String getWhere() throws SparqlException {
		String whereCopy = this.where;
		if (null != getSparqlEntity() && null != getSparqlEntity().getValue(Saccharide.PrimaryId))
			whereCopy += "?" + SaccharideURI + " glytoucan:has_primary_id " + getPrimaryId() + " .\n";
		
		return whereCopy;
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
	public String getFilter() {
		return "FILTER NOT EXISTS {\n"
				+ "?" + SaccharideURI + " glytoucan:has_derivatized_mass ?existingmass .\n}";
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.state(getPrefix() != null, "A ident is required");
		Assert.state(getSelect() != null, "A select is required");
	}
}