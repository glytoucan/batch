package org.glycoinfo.batch.glyconvert;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.convert.GlyConvert;
import org.glycoinfo.rdf.SelectSparqlBean;
import org.glycoinfo.rdf.glycan.Saccharide;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
public class ConvertSelectSparql extends SelectSparqlBean implements
		GlyConvertSparql, InitializingBean {
	public static final String SaccharideURI = Saccharide.URI;
	public static final String Sequence = "Sequence";
	public static final String GlycanSequenceURI = "GlycanSequenceURI";
	public static final String AccessionNumber = Saccharide.PrimaryId;
	
  @Autowired(required = true)
  @Qualifier("org.glycoinfo.batch.glyconvert")
	GlyConvert glyConvert;

	public ConvertSelectSparql(String sparql) {
		super(sparql);
	}

	public ConvertSelectSparql() {
		super();
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
		String format = getGlyConvert().getFromFormat();
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
	  String where = "?" + SaccharideURI + " a glycan:saccharide .\n" + "?"
        + SaccharideURI
        + " glytoucan:has_primary_id ?" + AccessionNumber + " .\n" + "?"
        + SaccharideURI + " glycan:has_glycosequence ?"
        + GlycanSequenceURI + " .\n" + "?" + GlycanSequenceURI
        + " glycan:has_sequence ?Sequence .\n" + "?"
        + GlycanSequenceURI + " glycan:in_carbohydrate_format "
        + getFormat()+"\n";
	  if (StringUtils.isNotBlank(getSparqlEntity().getValue(GlyConvertSparql.DoNotFilter)))
	    return where;
	  else 
	    return where + getFilter();
	}

	/**
	 * 
	 * the filter removes any sequences that already have a sequence in the
	 * GlyConvert.getTo() format.
	 * 
	 * @return
	 */
	public String getFilter() {
		/* FILTER NOT EXISTS {
?SaccharideURI glycan:has_glycosequence ?existingseq .
?existingseq glycan:has_sequence ?someSequence .
?existingseq glycan:in_carbohydrate_format glycan:carbohydrate_format_wurcs .
}
*/
		return "FILTER NOT EXISTS {\n"
				+ "?" + SaccharideURI + " glycan:has_glycosequence ?existingseq .\n"
				+ "?existingseq glycan:has_sequence ?someSequence .\n"
				+ "?existingseq glycan:in_carbohydrate_format glycan:carbohydrate_format_" + getGlyConvert().getToFormat() + "\n" + "}";
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.state(getPrefix() != null, "A ident is required");
		Assert.state(getSelect() != null, "A select is required");
		Assert.state(getGlyConvert() != null,
				"A conversion is required - is one configured for autowire?");
	}
	
	@Override
	public String getFrom() {
    return "FROM <http://rdf.glytoucan.org/core>\n"
        + "FROM <http://rdf.glytoucan.org/sequence/wurcs>\n"
        + "FROM <http://rdf.glytoucan.org/sequence/" + glyConvert.getToFormat() + ">";
	}
}