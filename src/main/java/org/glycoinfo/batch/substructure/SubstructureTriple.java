package org.glycoinfo.batch.substructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.glycoinfo.batch.TripleBean;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

public class SubstructureTriple implements TripleBean {

	public static Logger logger = (Logger) LoggerFactory
			.getLogger("org.glycoinfo.batch.substructure.SubstructureTriple");

	String ident;
	String subNumber;
	String keyword;
	String supIri;
	Set<String> subIriList = new HashSet<String>();
	String select = "DISTINCT ?glycans ?AccessionNumber ?Seq";
	String where = "?glycans a glycan:glycan_motif .\n"
			+ "?glycans toucan:has_primary_id ?AccessionNumber .\n"
			+ "?glycans glycan:has_glycosequence ?gseq .\n"
			+ "?gseq glycan:has_sequence ?Seq .\n"
			+ "?gseq glycan:in_carbohydrate_format glycan:carbohydrate_format_kcf\n";

	public static final String prefix = "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n"
			+ "PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
			+ "PREFIX toucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>\n";

	public static final String from = "from <http://glytoucan.org/rdf/demo/0.7>\n"
			+ "from <http://glytoucan.org/rdf/demo/0.7/kcf> \n";

	public static String graph = "http://glytoucan.org/rdf/demo/0.7/motif";

	@Override
	public String getIdent() {
		return this.ident;
	}

	@Override
	public void setIdent(String ident) {
		this.ident = ident;
	}

	@Override
	public String getInsert() {
		// insert the substructure rdf
		// if it's a motif, add the has_motif rdf
		StringBuffer insert = new StringBuffer();
		for (String string : subIriList) {
			insert.append("<" + getSupIri() + "> glycan:has_motif <" + string + "> .\n");
		}
		return insert.toString();
	}

	@Override
	public void setInsert(String insert) {
		return;
	}

	@Override
	public String getFailInsert() {
		return "";
	}

	@Override
	public void setFailInsert(String failInsert) {
		return;
	}

	public String getSubNumber() {
		return subNumber;
	}

	public void setSubNumber(String subNumber) {
		this.subNumber = subNumber;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getSupIri() {
		return supIri;
	}

	public void setSupIri(String supIri) {
		this.supIri = supIri;
	}

	public Set<String> getSubIri() {
		return subIriList;
	}

	public void setSubIri(Set subIri) {
		this.subIriList = subIri;
	}

	@Override
	public String getPrefix() {
		return prefix;
	}

	@Override
	public void setPrefix(String prefix) {
	}

	@Override
	public void setOrderBy(String orderByStatement) {
		return;
	}

	@Override
	public String getSelect() {
		return "SELECT " + select + "\n";
	}

	@Override
	public void setSelect(String select) {
	}

	@Override
	public String getWhere() {
		return (null != where) ? " WHERE {" + where + "}\n" : "";
	}

	@Override
	public void setWhere(String where) {
	}

	@Override
	public String getFrom() {
		return from;
	}

	@Override
	public void setFrom(String from) {
	}

	public String getProcessQuery() {
		StringBuilder query = new StringBuilder();

		query.append(getPrefix());
		query.append(getSelect());
		query.append(getFrom());
		query.append(getWhere());
		// query.append(" RETURN ").append(returnStatement);
		// query.append(getOrderBy() != null ? getOrderBy() : "");

		String resultingQuery = query.toString();

		if (logger.isDebugEnabled()) {
			logger.debug(resultingQuery);
		}

		return resultingQuery;

	}

	/*
	 * 
	 * total list:
	 * 
	 * PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#> PREFIX toucan:
	 * <http://www.glytoucan.org/glyco/owl/glytoucan#> SELECT DISTINCT ?glycans
	 * ?AccessionNumber ?Seq from <http://glytoucan.org/rdf/demo/0.6> from
	 * <http://glytoucan.org/rdf/demo/0.6/rdf> WHERE { ?glycans a
	 * glycan:saccharide . ?glycans toucan:has_primary_id ?AccessionNumber .
	 * ?glycans glycan:has_glycosequence ?gseq . ?gseq glycan:has_sequence ?Seq
	 * . ?gseq glycan:in_carbohydrate_format glycan:carbohydrate_format_kcf }
	 * 
	 * 
	 * motifs to check:
	 * 
	 * PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#> PREFIX toucan:
	 * <http://www.glytoucan.org/glyco/owl/glytoucan#> SELECT DISTINCT ?glycans
	 * ?AccessionNumber ?Seq from <http://glytoucan.org/rdf/demo/0.6> from
	 * <http://glytoucan.org/rdf/demo/0.6/rdf> WHERE { ?glycans a
	 * glycan:glycan_motif . ?glycans toucan:has_primary_id ?AccessionNumber .
	 * ?glycans glycan:has_glycosequence ?gseq . ?gseq glycan:has_sequence ?Seq
	 * . ?gseq glycan:in_carbohydrate_format glycan:carbohydrate_format_kcf }
	 * 
	 * 
	 * PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#> PREFIX toucan:
	 * <http://www.glytoucan.org/glyco/owl/glytoucan#> SELECT DISTINCT ?glycans
	 * ?id ?Seq from <http://glytoucan.org/rdf/demo/0.6> from
	 * <http://bluetree.jp/test> WHERE { ?glycans a glycan:glycan_motif .
	 * ?glycans toucan:has_primary_id ?id . ?glycans glycan:has_glycosequence
	 * ?gseq . ?gseq glycan:has_sequence ?Seq . ?gseq
	 * glycan:in_carbohydrate_format glycan:carbohydrate_format_kcf }
	 * 
	 * glycan:has_motif
	 */

	// triple store batch process
	// conversion
	// retrieve a list of asc#
	// for each asc #
	// convert to kcf
	// insert into rdf

	// mass

	// motif/substructure
	// retrieve a list of asc#
	// for each asc# -
	// retrieve list of structures to find subs
	// search for substructure - kcam
	// insert substructure/motif
}