package org.glycoinfo.rdf;

import org.glycoinfo.rdf.dao.SparqlEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * 
 * A parent interface used to combine common parts of a sparql statement.  non-required fields should be nullable and thus blank.  
 * 
 * prefix:
 * PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>
 * PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
 * PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
 * PREFIX sio: <http://semanticscience.org/resource/>
 * PREFIX glytoucan: <http://www.glytoucan.org/glyco/owl/glytoucan#>
 * 
 * graph:
 * FROM <http://glytoucan.org/rdf/demo/0.5>
 * FROM <nobutest>
 * 
 * where:
 * WHERE {
 * ?s a glycan:saccharide .
 * ?s glytoucan:has_primary_id ?AccessionNumber .
 * ?s glycan:has_glycosequence ?gseq .
 * ?gseq glycan:has_sequence ?Seq .
 * ?gseq glycan:in_carbohydrate_format ?type
 * }
 * 
 * @author aoki
 *
 */
public interface Sparql {
	
	public final static String PRIMARY_KEY = "primary_key";

	public final static String URI = "uri";
	public final static String TRUE = "1";
	public final static String FALSE = "0";
	

	/**
	 * 
	 * The prefix used for all sparql.
	 * 
	 * @return
	 */
	@JsonProperty(value="prefix")
	public String getPrefix();

	/**
	 * 
	 * Setting the prefix.
	 * 
	 * @param prefix
	 */
	public void setPrefix(String prefix);

	/**
	 * 
	 * Retrieve the where clause.  Note the variables must match those in the getSelect method.
	 * 
	 * @return
	 * @throws SparqlException 
	 */
	@JsonProperty(value="where")
	public String getWhere() throws SparqlException;



	/**
	 * 
	 * Set the where clause.
	 * 
	 * @param where
	 */
	void setWhere(String where);

	public void setSparql(String sparql);
	
	/**
	 * 
	 * Retrieve the overall sparql constructed from all of the parts above.
	 * 
	 * @return
	 * @throws SparqlException 
	 */
	@JsonProperty(value="sparql")
	public String getSparql() throws SparqlException;
	

	/**
	 * 
	 * The sparqlentity is a simple Map where "value" can be retrieved based on a key.  This is mainly to represent the results of a SparqlQuery, but can be any kind of data Map.
	 * The primary use of this is to enable dynamic queries based on the data stored in the SparqlEntity. 
	 * 
	 */
	public void setSparqlEntity(SparqlEntity sparqlentity);

	/**
	 * 
	 * See @setSparqlEntity above
	 * 
	 * @return SparqlEntity the current data stored and used to generate the query.
	 */
	@JsonIgnore
	public SparqlEntity getSparqlEntity();
}