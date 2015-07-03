package org.glycoinfo.rdf.search;

import java.util.LinkedList;

import javax.xml.bind.annotation.XmlRootElement;

import org.glycoinfo.WURCSFramework.util.array.WURCSFormatException;
import org.glycoinfo.WURCSFramework.util.array.WURCSImporter;
import org.glycoinfo.WURCSFramework.util.exchange.WURCSArrayToSequence;
import org.glycoinfo.WURCSFramework.wurcs.array.WURCSArray;
import org.glycoinfo.WURCSFramework.wurcs.sequence.WURCSSequence;
import org.glycoinfo.batch.search.SearchSparql;
import org.glycoinfo.rdf.SelectSparqlBean;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.glycan.Saccharide;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.wordnik.swagger.annotations.ApiModel;

@ApiModel (value="Search Substructure SPARQL", description="SPARQL query generated to find substructures.")
@XmlRootElement (name="select-search-sparql")
public class SearchSparqlBean extends
		SelectSparqlBean implements SearchSparql {
	String strGlycoSeqVariable;

//	WURCSSequenceExporterSPARQL exporter = new WURCSSequenceExporterSPARQL();
	
	public SearchSparqlBean() {
		super();
		this.prefix = "PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
				+ "PREFIX toucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>\n"
				+ "PREFIX wurcs: <http://www.glycoinfo.org/glyco/owl/wurcs#>";
		this.select = "DISTINCT ?" + Saccharide.URI;
//				+ " ?" + Saccharide.PrimaryId; 
		this.from = "FROM <http://rdf.glytoucan.org>\n"
				+ "FROM <http://rdf.glytoucan.org/sequence/wurcs>\n"
				+ "FROM <http://rdf.glycoinfo.org/wurcs/0.5.0>\n"
				+ "FROM <http://rdf.glycoinfo.org/wurcs/0.5.0/ms>";
	}

	@JsonProperty(value="where")
	public String getWhere(String a_strWURCS) throws SparqlException {
		LinkedList<String> t_aOption = new LinkedList<String>();
//		t_aOption.add("exact");
		t_aOption.add("uri");
//		t_aOption.add("wurcs");
//		t_aOption.add("accession_number");
		t_aOption.add("whereonly");
		t_aOption.add("TEST_ID:" + "sparqlbean");
		WURCSImporter t_oImport = new WURCSImporter();
		WURCSArray t_oWURCS;
		try {
			t_oWURCS = t_oImport.extractWURCSArray(a_strWURCS);
		} catch (WURCSFormatException e) {
			e.printStackTrace();
			throw new SparqlException(e);
		}
		WURCSArrayToSequence t_oA2S = new WURCSArrayToSequence();
		t_oA2S.start(t_oWURCS);
		WURCSSequence t_oSeq = t_oA2S.getSequence();
		
//		return exporter.getMainQuery(t_oSeq);
		return null;
	}

	@Override
	public void setGlycoSequenceUri(String strGlycoSeqVariable) {
		this.strGlycoSeqVariable = strGlycoSeqVariable;
	}

	@Override
	@JsonProperty(value="glycosequence")
	public String getGlycoSequenceUri() {
		return strGlycoSeqVariable;
	}

}
