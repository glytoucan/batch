package org.glycoinfo.batch.search.wurcs;

import org.glycoinfo.WURCSFramework.wurcs.WURCSFormatException;
import org.glycoinfo.batch.search.SearchSparql;
import org.glycoinfo.rdf.SelectSparqlBean;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.glycan.GlycoSequence;
import org.glycoinfo.rdf.glycan.Saccharide;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import ch.qos.logback.classic.Logger;

public class MotifSearchSparql extends SelectSparqlBean {

	public static Logger logger = (Logger) LoggerFactory
			.getLogger(MotifSearchSparql.class);

	@Autowired
	SearchSparql searchSparql;
	
	public SearchSparql getSearchSparql() {
		return searchSparql;
	}

	public void setSearchSparql(SearchSparql search) {
		this.searchSparql = search;
	}

	public MotifSearchSparql() {
		this.prefix = "PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
				+ "PREFIX toucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>";
		this.select = "DISTINCT ?" + Saccharide.URI + " ?" + Saccharide.PrimaryId; 
		this.from = "FROM <http://rdf.glytoucan.org>\n"
				+ "FROM <http://rdf.glytoucan.org/sequence/wurcs>\n"
				+ "FROM <http://www.glycoinfo.org/graph/wurcs/0.4>";
	}

	@Override
	public String getWhere() throws SparqlException {
		this.where = "?" + Saccharide.URI + " toucan:has_primary_id ?" + Saccharide.PrimaryId + " .\n"
				+ "GRAPH <http://www.glycoinfo.org/graph/wurcs/0.4> {"
				+ "?" + Saccharide.URI + " glycan:has_glycosequence ?" + GlycoSequence.URI + " .\n";
				
//				"?glycans a glycan:glycan_motif .\n"
//				+ "?glycans glycan:has_glycosequence ?gseq .\n"
//				+ "?gseq glycan:has_sequence ?Seq .\n"
//				+ "?gseq glycan:in_carbohydrate_format glycan:carbohydrate_format_glycoct\n";
		
		try {
			this.where += getSearchSparql().getExactWhere(getSparqlEntity().getValue(GlycoSequence.Sequence));
		} catch (WURCSFormatException e) {
			throw new SparqlException(e);
		}
		
		this.where += "}";
		return where;
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