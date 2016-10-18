package org.glycoinfo.rdf.glycan;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.glycoinfo.rdf.DeleteSparqlBean;
import org.glycoinfo.rdf.InsertSparqlBean;
import org.glycoinfo.rdf.SparqlException;
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
public class ResourceEntryDeleteSparql extends DeleteSparqlBean implements ResourceEntry, UriProvider {

	void init() {
		this.prefix="prefix glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
				+ "prefix dc: <http://purl.org/dc/elements/1.1/>\n"
				+ "prefix dcterms: <http://purl.org/dc/terms/>\n"
				+ "prefix glytoucan: <http://www.glytoucan.org/glyco/owl/glytoucan#>\n";
    }

	public ResourceEntryDeleteSparql() {
		super();
		init();
	}
	
	public String getURI() {
		if (null != getSparqlEntity().getValue(ResourceEntry.ResourceEntryURI) && null != getSparqlEntity().getValue(ResourceEntry.Identifier)) {
			String url = getSparqlEntity().getValue(ResourceEntry.ResourceEntryURI) + getSparqlEntity().getValue(ResourceEntry.Identifier);
			return url;
		} else {
			String database = null;
			if (StringUtils.isNotBlank(getSparqlEntity().getValue(Database)))
				database = getSparqlEntity().getValue(Database);
			if (StringUtils.isNotBlank(getSparqlEntity().getValue(PartnerId)))
				database = getSparqlEntity().getValue(PartnerId);
				
			return "http://rdf.glycoinfo.org/glycan/resource-entry/" + database + "/" + getSparqlEntity().getValue(Identifier);
		}
	}

	@Override
	public String getDelete()  {
		StringBuilder deleteBuilder = null;
    try {
      deleteBuilder = new StringBuilder(getUri() + " ?v ?o . ");
    } catch (SparqlException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
		    
		this.delete = deleteBuilder.toString();
		return this.delete;
	}

  @Override
  public String getUri() throws SparqlException {
    String database = null;
    if (StringUtils.isNotBlank(getSparqlEntity().getValue(Database)))
      database = getSparqlEntity().getValue(Database);
    if (StringUtils.isNotBlank(getSparqlEntity().getValue(PartnerId)))
      database = getSparqlEntity().getValue(PartnerId);
      
    return "http://rdf.glycoinfo.org/glycan/resource-entry/" + database + "/" + getSparqlEntity().getValue(Identifier);
  }
  
  @Override
  public String getFormat() {
    return DELETEWHERE;
  }
  
}