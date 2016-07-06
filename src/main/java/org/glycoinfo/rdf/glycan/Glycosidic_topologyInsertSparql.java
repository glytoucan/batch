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
 * @prefix rocs: <http://www.glycoinfo.org/glyco/owl/relation#> .
 * 
 * <http://rdf.glycoinfo.org/glycan/{Accession number 1}>
 *	rocs:has_topology <http://rdf.glycoinfo.org/glycan/{Accession number 2}> .
 *
 * <http://rdf.glycoinfo.org/glycan/{Accession number 2}>
 *	a rocs:Glycosidic_topology ;
 *
 * @author tokunaga
 *
 */
public class Glycosidic_topologyInsertSparql extends InsertSparqlBean implements Glycosidic_topology, UriProvider {

	String level_type = "a rocs:Glycosidic_topology";
	String has_toplogy = "rocs:has_topology";
	private String m_PrimaryId = null;

	void init() {
		this.prefix="PREFIX rocs: <http://www.glycoinfo.org/glyco/owl/relation#>\n";
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
					+ " rocs:has_topology <http://rdf.glycoinfo.org/glycan/"+ getSparqlEntity().getValue(PrimaryId_2) +"> .\n"
					+ "<http://rdf.glycoinfo.org/glycan/" + getSparqlEntity().getValue(PrimaryId_2) +">"
					+ " a rocs:Glycosidic_topology .\n";
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