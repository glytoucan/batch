package org.glycoinfo.rdf.glycan;

import java.util.ArrayList;

import org.glycoinfo.rdf.InsertSparql;
import org.glycoinfo.rdf.InsertSparqlBean;

public class SaccharideInsertSparql extends InsertSparqlBean {

	String type = "a glycan:saccharide";
	String hasPrimaryId = "glytoucan:has_primary_id";
	
	public SaccharideInsertSparql() {
	}
	
	public SaccharideInsertSparql(GlycoSequenceInsertSparql sequence) {
		ArrayList<InsertSparql> list = new ArrayList<InsertSparql>();
		list.add(sequence);
		addRelated(list);
	}
	
	public String getInsert() {
		String saccharideURI = getSaccharideURI();
		String rdf = saccharideURI + " a " + getSparqlEntity().getValue(GlycoSequence.URI) + " .\n" + 
				saccharideURI + " glytoucan:has_primary_id " + getSparqlEntity().getValue(Saccharide.PrimaryId) + " .\n";
		return rdf;
	}
	
	public String getSaccharideURI() {
		return getSparqlEntity().getValue(Saccharide.URI);
	}
	/*
	String hasImage = "glycan:has_image";
	<http://www.glytoucan.org/glyspace/service/glycans/G00054MO/image?style=extended&format=png&notation=uoxf-color> ,
	<http://www.glytoucan.org/glyspace/service/glycans/G00054MO/image?style=extended&format=png&notation=iupac> , 
	<http://www.glytoucan.org/glyspace/service/glycans/G00054MO/image?style=extended&format=png&notation=cfgbw> , 
	<http://www.glytoucan.org/glyspace/service/glycans/G00054MO/image?style=extended&format=png&notation=cfg-uoxf> ,
	<http://www.glytoucan.org/glyspace/service/glycans/G00054MO/image?style=extended&format=png&notation=cfg> ,
	<http://www.glytoucan.org/glyspace/service/glycans/G00054MO/image?style=extended&format=png&notation=uoxf> .
	*/
}