package org.glycoinfo.rdf.glycan;

import java.util.ArrayList;

import org.glycoinfo.rdf.InsertSparql;
import org.glycoinfo.rdf.InsertSparqlBean;
//
public class DerivatizedMassInsertSparql extends InsertSparqlBean {

	String type = "a glycan:saccharide";
	String hasPrimaryId = "glytoucan:has_primary_id";
	public static String Mass = "Mass";

	public DerivatizedMassInsertSparql() {
		this.prefix="PREFIX glytoucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>\n";
	}
	
	public String getInsert() {
// <http://rdf.glycoinfo.org/glycan/G00054MO> glytoucan:has_derivatized_mass <http://rdf.glycoinfo.org/derivatization_type_none/820.2960859799999> .
// <http://rdf.glycoinfo.org/derivatization_type_none/820.2960859799999> a	glytoucan:derivatized_mass .
// <http://rdf.glycoinfo.org/derivatization_type_none/820.2960859799999> glytoucan:has_derivatization_type	glytoucan:derivatization_type_none ;
// <http://rdf.glycoinfo.org/derivatization_type_none/820.2960859799999> glytoucan:has_mass "820.2960859799999"^^xsd:double .
							
		String saccharideURI = getSaccharideURI();
		String rdf = saccharideURI + " glytoucan:has_derivatized_mass " + getURI() + " .\n" +
				getURI() + " a glytoucan:derivatized_mass .\n" +
				getURI() + " glytoucan:has_derivatization_type glytoucan:derivatization_type_none .\n" +
				getURI() + " glytoucan:has_mass \"" + getSparqlEntity().getValue(Mass) + "\"^^xsd:double .\n";
		return rdf;
	}

	public String getSaccharideURI() {
		return "<" + getSparqlEntity().getValue(Saccharide.URI) + ">";
	}

	public String getURI() {
		return "<http://rdf.glycoinfo.org/derivatization_type_node/" + getSparqlEntity().getValue(Mass) + ">";
	}
}