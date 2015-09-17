package org.glycoinfo.rdf.glycan;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.glycoinfo.rdf.InsertSparqlBean;
import org.glycoinfo.rdf.UriProvider;

/**
 * Generates a ResourceEntry Insert updateSPARQL.
 * Required fields are: PrimaryId, Database, AccessionNumber, ContributorId, and date submitted.  Please refer to ResourceEntry.class.
 * 
 * prefix dc: <http://purl.org/dc/elements/1.1/> 
 * prefix dcterms: <http://purl.org/dc/terms/> 
 * prefix glycan: <http://purl.jp/bio/12/glyco/glycan#> 
 * <http://rdf.glycoinfo.org/resource-entry/331ebfcfc29a997790a7a4f1671a9882>
 *     a    glycan:resource_entry ;
 *     glycan:in_glycan_database glycan:database_glytoucan ;
 *     dcterms:identifier "G00021MO" ;
 *     rdfs:seeAlso <https://glytoucan.org/Structures/Glycans/G00021MO> ;
 *     dc:contributor    <http://rdf.glycoinfo.org/glytoucan/contributor/1> ;
 *     dcterms:dataSubmitted  "2014-10-20 06:47:31.204"^^xsd:dateTimeStamp .
 * 
 * This work is licensed under the Creative Commons Attribution 4.0 International License. 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by/4.0/.
 * 
 * @author aoki
 *
 */
public class ResourceEntryInsertSparql extends InsertSparqlBean implements ResourceEntry, UriProvider {

	/*
	 * @PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#> .
@PREFIX glytoucan: <http://www.glytoucan.org/glyco/owl/glytoucan#> .
@PREFIX xsd:   <http://www.w3.org/2001/XMLSchema#> .
	 */
	void init() {
		this.prefix="prefix glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
				+ "prefix dc: <http://purl.org/dc/elements/1.1/>\n"
				+ "prefix dcterms: <http://purl.org/dc/terms/>\n"
				+ "prefix glytoucan: <http://www.glytoucan.org/glyco/owl/glytoucan#>\n";
    }

	public ResourceEntryInsertSparql() {
		super();
		init();
	}
	
	public String getURI() {
		return "http://rdf.glycoinfo.org/resource-entry/" + getSparqlEntity().getValue(Identifier);
	}

	public String getInsert()  {
//		if (StringUtils.isBlank(getSparqlEntity().getValue(Saccharide.PrimaryId)))
//			throw new SparqlException("requires primary id");
//		else if (StringUtils.isBlank(getSparqlEntity().getValue(Database)))
//			throw new SparqlException("requires database");
//		else if(StringUtils.isBlank(getSparqlEntity().getValue(ContributorId)))
//			throw new SparqlException("requires contributor id");
//		else if (StringUtils.isBlank(getSparqlEntity().getValue(DataSubmittedDate))) {
//			throw new SparqlException("requires date submitted");
//		}
		String saccharideRelation = null;
		if (null != getSparqlEntity().getValue(Saccharide.URI)) {
			UriProvider saccharideUri = (UriProvider) getSparqlEntity().getObjectValue(Saccharide.URI);
			saccharideRelation = "<" + saccharideUri.getUri() + "> glycan:has_resource_entry <" + getURI() + "> .\n";
		}
		this.insert = (StringUtils.isBlank(saccharideRelation)? "" : saccharideRelation)
				+ "<" + getURI() + ">" + " a " + "glycan:resource_entry .\n"
				+ "<" + getURI() + ">" + " glycan:in_glycan_database glytoucan:database_" + getSparqlEntity().getValue(Database) + " .\n"
				+ "<" + getURI() + ">" + " dcterms:identifier \"" + getSparqlEntity().getValue(Identifier) + "\" .\n"
				+ "<" + getURI() + ">" + " rdfs:seeAlso <https://glytoucan.org/Structures/Glycans/" + getSparqlEntity().getValue(Identifier) + "> .\n"
				+ "<" + getURI() + ">" + " glytoucan:contributor <http://rdf.glycoinfo.org/glytoucan/contributor/userId/" + getSparqlEntity().getValue(ContributorId) + "> .\n"
				+ "<" + getURI() + ">" + " glytoucan:date_registered \"" + dateTimeStamp((Date) getSparqlEntity().getObjectValue(DataSubmittedDate)) + "\"^^xsd:dateTimeStamp ."; 
		return this.insert;
	}

	private String dateTimeStamp(Date value) {
//		2014-07-27 09:45:28.913
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
		return sdf.format(value);
	}
	@Override
	public String getUri() {
		// TODO Auto-generated method stub
		return null;
	}

//	public String getFormat() {
//		return InsertSparql.Turtle;
//	}
}