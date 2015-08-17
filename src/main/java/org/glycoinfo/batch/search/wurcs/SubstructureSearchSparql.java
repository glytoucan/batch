package org.glycoinfo.batch.search.wurcs;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.glycoinfo.WURCSFramework.util.array.WURCSFormatException;
import org.glycoinfo.WURCSFramework.util.array.WURCSImporter;
import org.glycoinfo.WURCSFramework.util.exchange.WURCSArrayToSequence2;
import org.glycoinfo.WURCSFramework.util.rdf.WURCSSequence2ExporterSPARQL;
import org.glycoinfo.WURCSFramework.wurcs.array.WURCSArray;
import org.glycoinfo.WURCSFramework.wurcs.sequence2.WURCSSequence2;
import org.glycoinfo.rdf.SelectSparqlBean;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.glycan.GlycoSequence;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SubstructureSearchSparql extends SelectSparqlBean {

	public static final String SubstructureSearchSaccharideURI = "glycan";
	
	public static Logger logger = (Logger) LoggerFactory
			.getLogger(SubstructureSearchSparql.class);

//	WURCSSequenceExporterSPARQL exporter = new WURCSSequenceExporterSPARQL();

	public SubstructureSearchSparql() {
		this.define = "DEFINE sql:select-option \"order\"";
		this.prefix = "PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
				+ "PREFIX wurcs: <http://www.glycoinfo.org/glyco/owl/wurcs#>";
		this.select = "DISTINCT ?" + SubstructureSearchSaccharideURI;
//				+ " ?" + Saccharide.PrimaryId; 
		this.from = "FROM <http://rdf.glycoinfo.org/wurcs/seq/0.3>\n"
				+ "FROM <http://rdf.glycoinfo.org/wurcs/seq/0.3/pos>\n"
				+ "FROM <http://rdf.glycoinfo.org/wurcs/0.5.1/ms>\n";
	}

	@Override
	public String getWhere() throws SparqlException {
		this.where = "";
//	"?" + Saccharide.URI + " toucan:has_primary_id ?" + Saccharide.PrimaryId + " .\n"
//				+ "GRAPH <http://www.glycoinfo.org/wurcs> {"
//				+ "SELECT *\n"
//				+ "WHERE {\n"
//				+ "?" + Saccharide.URI + " glycan:has_glycosequence ?gseq .\n";
				
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
//		WURCSArrayToSequence t_oA2S = new WURCSArrayToSequence();
//		t_oA2S.start(t_oWURCS);

		WURCSArrayToSequence2 t_oA2S = new WURCSArrayToSequence2();
		t_oA2S.start(t_oWURCS);
		WURCSSequence2 t_oSeq = t_oA2S.getSequence();

		WURCSSequence2ExporterSPARQL t_oExport = new WURCSSequence2ExporterSPARQL();

		// Set option for SPARQL query generator
		t_oExport.setCountOption(true); // True: Count result
		//t_oExport.addTergetGraphURI("<http://rdf.glycoinfo.org/wurcs/seq>"); // Add your terget graph
		//t_oExport.setMSGraphURI("<http://rdf.glycoinfo.org/wurcs/0.5.1/ms>"); // Set your monosaccharide graph
		t_oExport.hideComments(true); // Hide all comments in query
//		t_oExport.setSearchSupersumption(true); // Search supersumption of monosaccharide

		t_oExport.start(t_oSeq);
		String t_strSPARQL = t_oExport.getWhere();

		logger.debug("WHERE of substructure:>" +  t_strSPARQL + "<");

		
			this.where += t_strSPARQL;
		
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