package org.glycoinfo.batch.convert;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.util.Assert;

/**
 * 
 * An abstract class used to contain all generic conversion logic. For instance:
 * retrieving of original glycoct data will be duplicated for all conversions.
 * 
 * @author aoki
 *
 */
public abstract class ConvertReadSparql implements ConvertReaderSparqlInterface,
		InitializingBean {

	protected Log logger = LogFactory.getLog(getClass());

	String glycanUri;
	String from = "from <http://glytoucan.org/rdf/demo/0.2>\n";

	public String getFrom() {
		String queryFrom = from + "from <" + getGraph() + ">\n";
		return queryFrom;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	@Override
	public String getInsert() {
		String sequence = getSequenceFormatted();
		String rdf = getGlycanUri() + " glycan:has_glycosequence "
				+ getGlycanSequenceUri() + " .\n" + getGlycanSequenceUri()
				+ " glycan:in_carbohydrate_format " + getFormatUri() + " .\n"
				+ getGlycanSequenceUri() + " glytoucan:is_glycosequence_of "
				+ getGlycanUri() + " .\n";

		if (null != getSequence() && !getSequence().contains("ERROR")) {
			return rdf += getGlycanSequenceUri() + " glycan:has_sequence \""
					+ sequence + "\"^^xsd:string .\n";
		} else {
			return getFailInsert();
		}
	}

	@Override
	public void setInsert(String insert) {
	}

	@Override
	public String getFailInsert() {
		String rdf = getGlycanUri() + " glycan:has_glycosequence "
				+ getGlycanSequenceUri() + " .\n" + getGlycanSequenceUri()
				+ " glycan:in_carbohydrate_format " + getFormatUri() + " .\n"
				+ getGlycanSequenceUri() + " glytoucan:is_glycosequence_of "
				+ getGlycanUri() + " .\n";
		if (null == getSequence()) {
			return rdf += getGlycanSequenceUri()
					+ " rdfs:label \" ERROR IN CONVERSION: " + getFormat()
					+ "\"^^xsd:string .\n";
		} else {
			try {
				return rdf += getGlycanSequenceUri() + " rdfs:label \""
						+ URLEncoder.encode(getSequence(), "UTF-8") + "\"^^xsd:string .\n";
			} catch (UnsupportedEncodingException e) {
				return rdf += getGlycanSequenceUri() + " rdfs:label \""
						+ getSequence() + "\"^^xsd:string .\n";
			}
		}
	}

	@Override
	public void setFailInsert(String failInsert) {
	}

	@Override
	public String getGraph() {
		return graphbase + getFormat();
	}

	@Override
	public void setGraph(String graph) {
		logger.debug("cannot overwrite graphbase:>" + graphbase + "< with >"
				+ graph + "<");
	}

	@Override
	public String getFormatUri() {
		return "glycan:carbohydrate_format_" + getFormat();
	}

	@Override
	public void setFormatUri(String formatUri) {
		// TODO del
	}

	@Override
	public String getPrefix() {
		return "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
				+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
				+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n"
				+ "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n"
				+ "PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
				+ "PREFIX glytoucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>\n";
	}

	@Override
	public void setPrefix(String prefix) {
		// TODO Auto-generated method stub
	}

	public String getWhere() {
		return "{ ?s a glycan:saccharide . \n"
				+ "?s glytoucan:has_primary_id ?AccessionNumber . \n"
				+ "?s glycan:has_glycosequence ?gseq . \n"
				+ "?gseq glycan:has_sequence ?Seq . \n"
				+ "?gseq glycan:in_carbohydrate_format glycan:carbohydrate_format_glycoct \n"
				+ getFilter() + " }\n";
	}

	public void setWhere() {
	}

	@Override
	public String getSelectRdf() {
		StringBuilder query = new StringBuilder();
		query.append(getPrefix());
		query.append(getSelect());
		query.append(getFrom());
		// query.append(matchStatement != null ? " MATCH " + matchStatement :
		// "");
		query.append(getWhere() != null ? " WHERE " + getWhere() : "");
		// query.append(" RETURN ").append(returnStatement);
		query.append(getOrderBy() != null ? getOrderBy() : "");

		String resultingQuery = query.toString();

		if (logger.isDebugEnabled()) {
			logger.debug(resultingQuery);
		}

		return resultingQuery;
	}

	@Override
	public String getSelect() {
		return "SELECT DISTINCT ?s ?AccessionNumber ?Seq\n";
	}

	@Override
	public void setSelect(String select) {
	}

	@Override
	public String getGlycanUri() {
		if (this.glycanUri == null)
			return "<http://www.glycoinfo.org/rdf/glycan/" + getIdent() + ">";
		else
			return this.glycanUri;
	}

	@Override
	public void setGlycanUri(String glycanUri) {
		this.glycanUri = glycanUri;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.state(getPrefix() != null, "A ident is required");
		Assert.state(getSelect() != null, "A select is required");
		Assert.state(getFrom() != null, "A from is required");
	}

	public String getFilter() {
		return "FILTER NOT EXISTS {\n"
				+ "?s glycan:has_glycosequence ?kseq .\n"
				+ "?kseq glycan:has_sequence ?kSeq .\n"
				+ "?kseq glycan:in_carbohydrate_format glycan:carbohydrate_format_"
				+ getFormat() + "\n" + "}";
	}

	@Override
	public void setWhere(String where) {
		// TODO Auto-generated method stub

	}
}