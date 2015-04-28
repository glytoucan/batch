package org.glycoinfo.batch.search.wurcs;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.glycoinfo.WURCSFramework.util.WURCSFormatException;
import org.glycoinfo.WURCSFramework.util.WURCSImporter;
import org.glycoinfo.WURCSFramework.util.exchange.WURCSArrayToSequence;
import org.glycoinfo.WURCSFramework.util.rdf.WURCSSequenceExporterSPARQL;
import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;
import org.glycoinfo.WURCSFramework.wurcs.sequence.WURCSSequence;
import org.glycoinfo.batch.search.SearchSparql;
import org.glycoinfo.rdf.SelectSparqlBean;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.glycan.GlycoSequence;
import org.glycoinfo.rdf.glycan.Saccharide;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class SubstructureSearchSparql extends SelectSparqlBean {

	public static Logger logger = (Logger) LoggerFactory
			.getLogger(SubstructureSearchSparql.class);

	WURCSSequenceExporterSPARQL exporter = new WURCSSequenceExporterSPARQL();

	public SubstructureSearchSparql() {
		this.define = "DEFINE sql:select-option \"order\"";
		this.prefix = "PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
				+ "PREFIX wurcs: <http://www.glycoinfo.org/glyco/owl/wurcs#>";
		this.select = "DISTINCT ?" + Saccharide.URI;
//				+ " ?" + Saccharide.PrimaryId; 
		this.from = "FROM <http://rdf.glycoinfo.org/wurcs/seq/0.1>\n"
				+ "FROM <http://rdf.glycoinfo.org/wurcs/seq/0.1/pos>\n"
				+ "FROM <http://rdf.glycoinfo.org/wurcs/0.5.1/ms>\n";
	}

	@Override
	public String getWhere() throws SparqlException {
		this.where = ""
				
//	"?" + Saccharide.URI + " toucan:has_primary_id ?" + Saccharide.PrimaryId + " .\n"
//				+ "GRAPH <http://www.glycoinfo.org/wurcs> {"
//				+ "SELECT *\n"
//				+ "WHERE {\n"
				+ "?" + Saccharide.URI + " glycan:has_glycosequence ?gseq .\n";
				
//				"?glycans a glycan:glycan_motif .\n"
//				+ "?glycans glycan:has_glycosequence ?gseq .\n"
//				+ "?gseq glycan:has_sequence ?Seq .\n"
//				+ "?gseq glycan:in_carbohydrate_format glycan:carbohydrate_format_glycoct\n";
		WURCSImporter t_oImport = new WURCSImporter();
		WURCSArray t_oWURCS;
		try {
			t_oWURCS = t_oImport.extractWURCSArray(URLDecoder.decode(getSparqlEntity().getValue(GlycoSequence.Sequence), "UTF-8"));
		} catch (UnsupportedEncodingException | WURCSFormatException e) {
			e.printStackTrace();
			throw new SparqlException(e);
		}
		WURCSArrayToSequence t_oA2S = new WURCSArrayToSequence();
		t_oA2S.start(t_oWURCS);
		WURCSSequence t_oSeq = t_oA2S.getSequence();
		
//		try {
			this.where += exporter.getMainQuery(t_oSeq, false);
//		} catch (WURCSFormatException e) {
//			throw new SparqlException(e);
//		}
		
//		this.where += "}";
//		this.where += "}";
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
	 * PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#> PREFIX toucan:getSearchSparql
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