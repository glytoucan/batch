package org.glycoinfo.batch;

public class SubstructureTriple {
	
	String subNumber;
	String checkNumber;
	
	/*PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>
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
SELECT DISTINCT ?AccessionNumber ?Seq ?type
 WHERE {
  ?s a glycan:saccharide .
  ?s glytoucan:has_primary_id ?AccessionNumber .
  ?s glycan:has_glycosequence ?gseq .
  ?gseq glycan:has_sequence ?Seq .
  ?type glycan:in_carbohydrate_format ?Seq
}
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
			// retrieve all structures
			// convert to kcf
			// search for substructure - kcam
	 
}