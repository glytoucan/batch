package org.glycoinfo.rdf.glycan;

import java.util.ArrayList;

import org.apache.commons.lang.StringUtils;
import org.glycoinfo.rdf.InsertSparql;
import org.glycoinfo.rdf.InsertSparqlBean;
import org.glycoinfo.rdf.UriProvider;

import scala.annotation.meta.setter;

/**
 * 
 * Insert Subsumption data of a glycosidic topology.  
 * 
 * @prefix glycan: <http://purl.jp/bio/12/glyco/glycan#> .
 * @prefix rocs: <http://www.glycoinfo.org/glyco/owl/relation#> .
 * 
 * <http://rdf.glycoinfo.org/glycan/{Accession number 1}>
 *	a glycan:Saccharide ;
 *	rocs:subsumes <http://rdf.glycoinfo.org/glycan/{Accession number 2}> ;
 *	rocs:has_topology <http://rdf.glycoinfo.org/glycan/{Accession number 2}> .
 *
 * <http://rdf.glycoinfo.org/glycan/{Accession number 2}>
 *	a glycan:Saccharide, rocs:Glycosidic_topology ;
 *	rocs:subsumed_by <http://rdf.glycoinfo.org/glycan/{Accession number 1}> .
 *
 * @author tokunaga
 *
 */
public class Glycosidic_topologyInsertSparql extends InsertSparqlBean implements Glycosidic_topology, UriProvider {

	String level1_type = "a glycan:saccharide";
	String subsumes = "rocs:subsumes";
	String has_toplogy = "rocs:has_topology";
	String level2_type = "a glycan:saccharide, rocs:Glycosidic_topology";
	String subsumed_by = "rocs:subsumed_by";
	private String m_PrimaryId = null;

	void init() {
		this.prefix="prefix glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
				+ "Prefix rocs: <http://www.glycoinfo.org/glyco/owl/relation#>\n";
	}
	public Glycosidic_topologyInsertSparql() {
		init();
	}
	
	public Glycosidic_topologyInsertSparql(GlycoSequenceInsertSparql sequence ) {
		init();
		ArrayList<InsertSparql> list = new ArrayList<InsertSparql>();
		list.add(sequence);
		addRelated(list);
	}
	
	public String getInsert() {
		if (StringUtils.isNotBlank(getSparqlEntity().getValue(PrimaryId_1)) && StringUtils.isNotBlank(getSparqlEntity().getValue(PrimaryId_2))) {
			this.insert = "<http://rdf.glycoinfo.org/glycan/" + getSparqlEntity().getValue(PrimaryId_1) +">"
					+ " rocs:subsumes <http://rdf.glycoinfo.org/glycan/"+ getSparqlEntity().getValue(PrimaryId_2) +"> ;\n"
					+ " rocs:has_topology <http://rdf.glycoinfo.org/glycan/"+ getSparqlEntity().getValue(PrimaryId_2) +"> .\n"
					+ "<http://rdf.glycoinfo.org/glycan/" + getSparqlEntity().getValue(PrimaryId_2) +">"
					+ " a rocs:Glycosidic_topology ;\n"
					+ "rocs:subsumed_by <http://rdf.glycoinfo.org/glycan/"+ getSparqlEntity().getValue(PrimaryId_1) +"> .\n";
		}
		return this.insert;
	}

	public String getSaccharideURI() {
		return "<" + getUri() + ">";
	}

	@Override
	public String getUri() {
		return SaccharideUtil.getURI(m_PrimaryId); 
	}
	
}