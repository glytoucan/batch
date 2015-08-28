package org.glycoinfo.rdf.glycan.wurcs;

import org.glycoinfo.WURCSFramework.util.array.WURCSFormatException;
import org.glycoinfo.WURCSFramework.util.array.WURCSImporter;
import org.glycoinfo.WURCSFramework.util.exchange.WURCSArrayToSequence;
import org.glycoinfo.WURCSFramework.util.rdf.WURCSSequenceExporterSPARQLOld;
import org.glycoinfo.WURCSFramework.wurcs.array.WURCSArray;
import org.glycoinfo.WURCSFramework.wurcs.sequence.WURCSSequence;
import org.glycoinfo.rdf.SelectSparqlBean;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.glycan.GlycoSequence;

public class WurcsRDFSelectSparql extends SelectSparqlBean {

	@Override
	public String getSparql() throws SparqlException {
		WURCSImporter t_oImport = new WURCSImporter();
		WURCSArray t_oWURCS;
		try {
			t_oWURCS = t_oImport.extractWURCSArray(getSparqlEntity().getValue(GlycoSequence.Sequence));
		} catch (WURCSFormatException e) {
			throw new SparqlException(e);
		}
		WURCSArrayToSequence t_oA2S = new WURCSArrayToSequence();
		t_oA2S.start(t_oWURCS);
		WURCSSequence t_oSeq = t_oA2S.getSequence();

		WURCSSequenceExporterSPARQLOld t_oExport = new WURCSSequenceExporterSPARQLOld();

		// Set option for SPARQL query generator
		//t_oExport.setCountOption(true); // True for result count
		//t_oExport.addTergetGraphURI("<http://rdf.glycoinfo.org/wurcs/seq>"); // Add your terget graph
		//t_oExport.setMSGraphURI("<http://rdf.glycoinfo.org/wurcs/0.5.1/ms>"); // Set your monosaccharide graph

		t_oExport.start(t_oSeq);
		String t_strSPARQL = t_oExport.getQuery();
		
String sparql = "DEFINE sql:select-option \"order\"\n"
+ "PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
+ "PREFIX wurcs: <http://www.glycoinfo.org/glyco/owl/wurcs#>\n"
+ "SELECT DISTINCT ?glycan (str ( ?wurcs ) AS ?WURCS)\n"
+ "FROM <http://rdf.glycoinfo.org/wurcs/seq/0.1>\n"
+ "FROM <http://rdf.glycoinfo.org/wurcs/seq/0.1/pos>\n"
+ "FROM NAMED <http://rdf.glycoinfo.org/wurcs/0.5.1/ms>\n"
+ "WHERE {\n"
+ "?glycan glycan:has_glycosequence ?gseq .\n"
+ "?gseq glycan:has_sequence ?wurcs .\n"
+ "# GLIN1\n"
+ "## GRES1\n"
+ "?gseq wurcs:has_GRES ?GRES1 .\n"
+ "?GRES1 wurcs:is_monosaccharide ?MS1 .\n"
+ "GRAPH <http://rdf.glycoinfo.org/wurcs/0.5.1/ms> {\n"
+ "<http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/u2122h> wurcs:subsumes ?MS1 .\n"
+ "}\n"
+ "?GRES1 wurcs:is_acceptor_of ?GLIN1 .\n"
+ "?GLIN1 wurcs:has_acceptor_position 4 .\n"
+ "?GLIN1 wurcs:has_donor_position 1 .\n"
+ "?GLIN1 wurcs:has_MAP .\n"
+ "## GRES2\n"
+ "?gseq wurcs:has_GRES ?GRES2 .\n"
+ "?GRES2 wurcs:is_monosaccharide ?MS2 .\n"
+ "GRAPH <http://rdf.glycoinfo.org/wurcs/0.5.1/ms> {\n"
+ "<http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/a2112h-1b_1-5> wurcs:subsumes ?MS2 .\n"
+ "}\n"
+ "?GRES2 wurcs:is_donor_of ?GLIN1 .\n"
+ "# GLIN2\n"
+ "?GRES2 wurcs:is_acceptor_of ?GLIN2 .\n"
+ "?GLIN2 wurcs:has_acceptor_position 3 .\n"
+ "?GLIN2 wurcs:has_donor_position 1 .\n"
+ "?GLIN2 wurcs:has_MAP .\n"
+ "## GRES3\n"
+ "?gseq wurcs:has_GRES ?GRES3 .\n"
+ "?GRES3 wurcs:is_monosaccharide ?MS3 .\n"
+ "GRAPH <http://rdf.glycoinfo.org/wurcs/0.5.1/ms> {\n"
+ "<http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/a2112h-1a_1-5> wurcs:subsumes ?MS3 .\n"
+ "}\n"
+ "?GRES3 wurcs:is_donor_of ?GLIN2 .\n"
+ "# GLIN3\n"
+ "?GRES3 wurcs:is_acceptor_of ?GLIN3 .\n"
+ "?GLIN3 wurcs:has_acceptor_position 3 .\n"
+ "?GLIN3 wurcs:has_donor_position 1 .\n"
+ "?GLIN3 wurcs:has_MAP  .\n"
+ "## GRES4\n"
+ "?gseq wurcs:has_GRES ?GRES4 .\n"
+ "?GRES4 wurcs:is_monosaccharide ?MS4 .\n"
+ "GRAPH <http://rdf.glycoinfo.org/wurcs/0.5.1/ms> {\n"
+ "<http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/a2112h-1b_1-5_2*NCC%2F3%3DO> wurcs:subsumes ?MS4 .\n"
+ "}\n"
+ "?GRES4 wurcs:is_donor_of ?GLIN3 .\n"
+ "}\n"
+ "ORDER BY ?glycan\n";
return sparql;
//		return t_strSPARQL;
	}
	
}
