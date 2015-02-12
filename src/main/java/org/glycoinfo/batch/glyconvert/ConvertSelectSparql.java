package org.glycoinfo.batch.glyconvert;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.conversion.GlyConvert;
import org.glycoinfo.rdf.SelectSparqlBean;
import org.glycoinfo.rdf.glycan.Saccharide;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

/**
 * 
 * An abstract class used to retrieve the glycan sequences, based on the
 * conversion format specified in the getFrom() of the configured GlyConvert.
 * The filter removes any existing sequences in the getTo() of the GlyConvert.
 * 
 * For instance: Retrieving of original glycoct by using
 * org.glycoinfo.conversion.wurcs.GlycoctToWurcsConverter.
 * 
 * @author aoki
 *
 */
@Component
public class ConvertSelectSparql extends SelectSparqlBean implements
		GlyConvertSparql, InitializingBean {
	public static final String SaccharideURI = Saccharide.URI;
	public static final String Sequence = "Sequence";
	public static final String GlycanSequenceURI = "GlycanSequenceURI";
	public static final String AccessionNumber = Saccharide.PrimaryId;

	@Autowired
	GlyConvert glyConvert;

	public ConvertSelectSparql(String sparql) {
		super(sparql);
	}

	public ConvertSelectSparql() {
		super();
		this.from = "FROM <http://rdf.glytoucan.org>";
	}

	protected Log logger = LogFactory.getLog(getClass());

	String glycanUri;

	public GlyConvert getGlyConvert() {
		return glyConvert;
	}

	public void setGlyConvert(GlyConvert glyconvert) {
		this.glyConvert = glyconvert;
	}

	public String getFormat() {
		// TODO fix this workaround
		String format = getGlyConvert().getFromFormat();
		if (format.equals("glycoct_condensed"))
			format = "glycoct";

		return "glycan:carbohydrate_format_" + format;
	}

	@Override
	public String getSelect() {
		return "DISTINCT ?" + SaccharideURI + " ?" + AccessionNumber
				+ " ?" + Sequence + " ?" + GlycanSequenceURI;
	}

	@Override
	public String getPrefix() {
		return "PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
				+ "PREFIX glytoucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.glycoinfo.rdf.SelectSparqlBean#getWhere()
	 */
	public String getWhere() {
		return "?" + SaccharideURI + " a glycan:saccharide .\n" + "?"
				+ SaccharideURI
				+ " glytoucan:has_primary_id ?" + AccessionNumber + " .\n" + "?"
				+ SaccharideURI + " glycan:has_glycosequence ?"
				+ GlycanSequenceURI + " .\n" + "?" + GlycanSequenceURI
				+ " glycan:has_sequence ?Sequence .\n" + "?"
				+ GlycanSequenceURI + " glycan:carbohydrate_format_glycoct_condensed "
				+ getFormat() + "\n";
	}

	/**
	 * 
	 * the filter removes any sequences that already have a sequence in the
	 * GlyConvert.getTo() format.
	 * 
	 * @return
	 */
	public String getFilter() {
		return "FILTER NOT EXISTS {\n"
				+ "?"
				+ SaccharideURI
				+ " glycan:has_glycosequence ?kseq .\n"
				+ "?kseq glycan:has_sequence ?kSeq .\n"
				+ "?kseq glycan:in_carbohydrate_format glycan:carbohydrate_format_"
				+ getGlyConvert().getToFormat() + "\n" + "}";
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.state(getPrefix() != null, "A ident is required");
		Assert.state(getSelect() != null, "A select is required");
		Assert.state(getGlyConvert() != null,
				"A conversion is required - is one configured for autowire?");
	}
}