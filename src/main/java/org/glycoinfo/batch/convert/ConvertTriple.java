package org.glycoinfo.batch.convert;

import org.glycoinfo.batch.TripleBean;

/*
 <http://www.glycoinfo.org/rdf/glycan/G63838JW> glycan:has_glycosequence ?gseq .
 ?gseq glycan:has_sequence ?Seq .
 ?gseq glycan:in_carbohydrate_format ?type

 <http://www.glycoinfo.org/rdf/glycan/G63838JW/sequence/kcf>
 a                              glycan:glycosequence ;
 glycan:has_sequence            "RES\n1b:b-dglc-HEX-1:5\n2s:n-acetyl\n3b:b-dglc-HEX-1:5\n4s:n-acetyl\n5b:b-dman-HEX-1:5\n6b:a-dman-HEX-1:5\n7b:b-dglc-HEX-1:5\n8s:n-acetyl\n9b:b-dglc-HEX-1:5\n10s:n-acetyl\n11b:b-dgal-HEX-1:5\n12b:a-dman-HEX-1:5\n13b:a-dman-HEX-1:5\n14b:a-dman-HEX-1:5\nLIN\n1:1d(2+1)2n\n2:1o(4+1)3d\n3:3d(2+1)4n\n4:3o(4+1)5d\n5:5o(3+1)6d\n6:6o(2+1)7d\n7:7d(2+1)8n\n8:6o(4+1)9d\n9:9d(2+1)10n\n10:9o(4+1)11d\n11:5o(6+1)12d\n12:12o(3+1)13d\n13:12o(6+1)14d\n"^^xsd:string ;
 glycan:in_carbohydrate_format  glycan:carbohydrate_format_glycoct .

 //input this:
 <http://www.glycoinfo.org/rdf/glycan/G63838JW/sequence>
 a                              glycan:glycosequence ;
 glycan:has_sequence            "KCF"^^xsd:string ;
 glycan:in_carbohydrate_format  glycan:carbohydrate_format_kcf .


 @prefix rdfs: <http://www.w3.org/2000/01/rdf-schema#> .
 @prefix xsd: <http://www.w3.org/2001/XMLSchema#> .
 @prefix sio: <http://semanticscience.org/resource/> .
 @prefix glycan: <http://purl.jp/bio/12/glyco/glycan#> .
 @prefix glytoucan: <http://www.glytoucan.org/glyco/owl/glytoucan#> .

 <http://glycoinfo.org/rdf/glycan/G72943US> a glycan:saccharide ;
 glycan:has_glycosequence <http://glycoinfo.org/rdf/glycan/G72943US/sequence/kcf> .

 <http://glycoinfo.org/rdf/glycan/G72943US/sequence/kcf> a glycan:glycosequence ;
 rdfs:label "G72943US Glycosequence"^^xsd:string ;
 glycan:has_sequence "KCF%3d2.0%2f1%2c0%2f%5bo2h%5d"^^xsd:string ;
 glycan:in_carbohydrate_format glycan:carbohydrate_format_kcf ;
 //	sio:SIO_000300 "WURCS%3d2.0%2f1%2c0%2f%5bo2h%5d"@en ;
 glytoucan:is_glycosequence_of <http://glycoinfo.org/rdf/glycan/G72943US> ;
 //	glytoucan:is_carbohydrate_residue <http://glycoinfo.org/rdf/glycan/wurcs/WURCS%3d2.0%2f1%2c0%2f%5bo2h%5d/residue> .

 <http://glycoinfo.org/rdf/glycan/G72943US> a sio:SIO_010334 ;
 rdfs:label "G72943US carbohydrate residue"^^xsd:string ;
 glycan:has_sequence "WURCS%3d2.0%2f1%2c0%2f%5bo2h%5d"^^xsd:string ;
 glycan:in_carbohydrate_format glycan:carbohydrate_format_wurcs ;
 sio:SIO_000300 "WURCS%3d2.0%2f1%2c0%2f%5bo2h%5d"@en .

 */

/*
 * search this:
 PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#> 
 PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> 
 PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> 
 PREFIX sio: <http://semanticscience.org/resource/> 
 PREFIX glytoucan: <http://www.glytoucan.org/glyco/owl/glytoucan#> 
 SELECT DISTINCT ?s ?AccessionNumber ?Seq ?type
 FROM <http://glytoucan.org/rdf/demo/0.5>
 FROM <nobutest>
 WHERE {
 ?s a glycan:saccharide .
 ?s glytoucan:has_primary_id ?AccessionNumber .
 ?s glycan:has_glycosequence ?gseq .
 ?gseq glycan:has_sequence ?Seq .
 ?gseq glycan:in_carbohydrate_format ?type
 }

 PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#> 
 PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> 
 PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> 
 PREFIX sio: <http://semanticscience.org/resource/> 
 PREFIX glytoucan: <http://www.glytoucan.org/glyco/owl/glytoucan#> 
 SELECT DISTINCT ?s ?AccessionNumber ?Seq 
 FROM <http://glytoucan.org/rdf/demo/0.5>
 FROM <nobutest>
 WHERE {
 ?s a glycan:saccharide .
 ?s glytoucan:has_primary_id ?AccessionNumber .
 ?s glycan:has_glycosequence ?gseq .
 ?gseq glycan:has_sequence ?Seq .
 ?gseq glycan:in_carbohydrate_format <glycan:in_carbohydrate_format glycan:carbohydrate_format_kcf>
 }

 PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#> 
 PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> 
 PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> 
 PREFIX sio: <http://semanticscience.org/resource/> 
 PREFIX glytoucan: <http://www.glytoucan.org/glyco/owl/glytoucan#> 
 SELECT DISTINCT ?gseq
 FROM <http://glytoucan.org/rdf/demo/0.5>
 FROM <nobutest>
 WHERE {
 ?gseq glycan:in_carbohydrate_format glycan:carbohydrate_format_kcf
 }

 PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
 PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
 PREFIX owl: <http://www.w3.org/2002/07/owl#>
 PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
 PREFIX dc: <http://purl.org/dc/elements/1.1/>
 PREFIX dcterms: <http://purl.org/dc/terms/>
 PREFIX dbpedia2: <http://dbpedia.org/property/>
 PREFIX dbpedia: <http://dbpedia.org/>
 PREFIX foaf: <http://xmlns.com/foaf/0.1/>
 PREFIX skos: <http://www.w3.org/2004/02/skos/core#>
 PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>
 PREFIX glytoucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>
 SELECT DISTINCT ?s ?AccessionNumber ?Seq ?type
 from <http://glytoucan.org/rdf/demo/0.5>
 from <http://glytoucan.org/rdf/demo/msdb/7>
 from <http://purl.jp/bio/12/glyco/glycan/ontology/0.18>
 from <http://www.glytoucan.org/glyco/owl/glytoucan>
 WHERE { ?s a glycan:saccharide . ?s glytoucan:has_primary_id ?AccessionNumber . ?s glycan:has_glycosequence ?gseq . ?gseq glycan:has_sequence ?Seq . ?gseq glycan:in_carbohydrate_format glycan:carbohydrate_format_glycoct }
 OFFSET 10 LIMIT 10

 */
public class ConvertTriple implements TripleBean {

	String ident;
	String sequence;
	String subjectLocation;
	String format = "glycan:carbohydrate_format_kcf";

	public static String glycanLocation = "<http://www.glycoinfo.org/rdf/glycan/%ID%/sequence>";
	public static String prefix = "@prefix glycan: <http://purl.jp/bio/12/glyco/glycan#> .\n";
	public static String graph = "http://glytoucan.org/rdf/demo/0.7/kcf";
	
	public String getIdent() {
		return ident;
	}

	public void setIdent(String ident) {
		this.ident = ident;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getSubjectLocation() {
		return "<http://www.glycoinfo.org/rdf/glycan/" + getIdent()
				+ "/sequence>";
	}

	public void setSubjectLocation(String subjectLocation) {
		this.subjectLocation = subjectLocation;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	@Override
	public String toString() {
		return getInsert();
	}

	@Override
	public String getInsert() {
		// return "<http://glycoinfo.org/rdf/glycan/" + getIdent() +
		// "> a glycan:saccharide ;\n" +
		// "glycan:has_glycosequence <http://glycoinfo.org/rdf/glycan/" +
		// getIdent() + "/sequence/kcf>\n" +
		// "<http://glycoinfo.org/rdf/glycan/G72943US/sequence/kcf> a glycan:glycosequence ;\n"
		// +
		// "rdfs:label \"" + getIdent() + " KCF\"^^xsd:string ;\n" +
		// "glycan:has_sequence \"" + getSequence() + "\"^^xsd:string ;\n" +
		// "glycan:in_carbohydrate_format glycan:carbohydrate_format_kcf ;\n" +
		// "glytoucan:is_glycosequence_of <http://glycoinfo.org/rdf/glycan/" +
		// getIdent() + "> ;\n";
		if (null != getSequence()) {
			return "<http://www.glycoinfo.org/rdf/glycan/"
					+ getIdent()
					+ "> glycan:has_glycosequence <http://www.glycoinfo.org/rdf/glycan/"
					+ getIdent()
					+ "/sequence/kcf> .\n"
					+ "<http://www.glycoinfo.org/rdf/glycan/"
					+ getIdent()
					+ "/sequence/kcf> rdfs:label \""
					+ getIdent()
					+ " KCF\"^^xsd:string .\n"
					+ "<http://www.glycoinfo.org/rdf/glycan/"
					+ getIdent()
					+ "/sequence/kcf> glycan:has_sequence \""
					+ getSequence().replace("\n", "\\n").replace("CT-1", getIdent())
					+ "\"^^xsd:string .\n"
					+ "<http://www.glycoinfo.org/rdf/glycan/"
					+ getIdent()
					+ "/sequence/kcf> glycan:in_carbohydrate_format glycan:carbohydrate_format_kcf .\n"
					+ "<http://www.glycoinfo.org/rdf/glycan/"
					+ getIdent()
					+ "/sequence/kcf> glytoucan:is_glycosequence_of <http://www.glycoinfo.org/rdf/glycan/"
					+ getIdent() + "> .\n";
		} else {
			return "<http://www.glycoinfo.org/rdf/glycan/"
					+ getIdent()
					+ "> glycan:has_glycosequence <http://www.glycoinfo.org/rdf/glycan/"
					+ getIdent()
					+ "/sequence/kcf> .\n"
					+ "<http://www.glycoinfo.org/rdf/glycan/"
					+ getIdent()
					+ "/sequence/kcf> rdfs:label \" ERROR IN CONVERSION: "
					+ getIdent()
					+ " KCF\"^^xsd:string .\n"
					// + "<http://www.glycoinfo.org/rdf/glycan/" + getIdent() +
					// "/sequence/kcf> glycan:has_sequence \"" +
					// getSequence().replace("\n", "\\n") + "\"^^xsd:string .\n"
					+ "<http://www.glycoinfo.org/rdf/glycan/"
					+ getIdent()
					+ "/sequence/kcf> glycan:in_carbohydrate_format glycan:carbohydrate_format_kcf .\n"
					+ "<http://www.glycoinfo.org/rdf/glycan/"
					+ getIdent()
					+ "/sequence/kcf> glytoucan:is_glycosequence_of <http://www.glycoinfo.org/rdf/glycan/"
					+ getIdent() + "> .\n";
		}
	}

	@Override
	public void setInsert(String insert) {
	}

	@Override
	public String getFailInsert() {
		return "<http://www.glycoinfo.org/rdf/glycan/"
				+ getIdent()
				+ "> glycan:has_glycosequence <http://www.glycoinfo.org/rdf/glycan/"
				+ getIdent()
				+ "/sequence/kcf> .\n"
				+ "<http://www.glycoinfo.org/rdf/glycan/"
				+ getIdent()
				+ "/sequence/kcf> rdfs:label \" ERROR IN CONVERSION: "
				+ getIdent()
				+ " KCF\"^^xsd:string .\n"
				// + "<http://www.glycoinfo.org/rdf/glycan/" + getIdent() +
				// "/sequence/kcf> glycan:has_sequence \"" +
				// getSequence().replace("\n", "\\n") + "\"^^xsd:string .\n"
				+ "<http://www.glycoinfo.org/rdf/glycan/"
				+ getIdent()
				+ "/sequence/kcf> glycan:in_carbohydrate_format glycan:carbohydrate_format_kcf .\n"
				+ "<http://www.glycoinfo.org/rdf/glycan/"
				+ getIdent()
				+ "/sequence/kcf> glytoucan:is_glycosequence_of <http://www.glycoinfo.org/rdf/glycan/"
				+ getIdent() + "> .\n";
	}

	@Override
	public void setFailInsert(String failInsert) {
	}

	@Override
	public String getPrefix() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPrefix(String prefix) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setOrderBy(String orderByStatement) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getSelect() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setSelect(String select) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getWhere() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setWhere(String where) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getFrom() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setFrom(String from) {
		// TODO Auto-generated method stub
		
	}

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
	// retrieve all structures
	// convert to kcf
	// search for substructure - kcam

}