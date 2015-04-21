package org.glycoinfo.rdf;

import javax.xml.bind.annotation.XmlRootElement;

import org.glycoinfo.rdf.dao.SparqlEntity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRawValue;

/**
 * 
 * An interface used to define the parts of a sparql select query.  non-required fields should be nullable and thus blank.  
 * 
 * prefix:
 * PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>
 * PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
 * PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>
 * PREFIX sio: <http://semanticscience.org/resource/>
 * PREFIX glytoucan: <http://www.glytoucan.org/glyco/owl/glytoucan#>
 * 
 * select:
 * SELECT DISTINCT ?s ?AccessionNumber ?Seq ?type
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
 * offset:
 * OFFSET 10
 * 
 * limit:
 * LIMIT 10
 * 
 * @author aoki
 *
 */
@XmlRootElement (name="select-sparql")
public interface SelectSparql {
	
	@JsonProperty(value="define")
	public String getDefine();
	
	public void setDefine(String define);

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
	 * Retrieve the initial select usually used in the reader and or processor.
	 * 
	 * @return
	 */
	@JsonProperty(value="select")
	public String getSelect();

	/**
	 * 
	 * Set the select.
	 * 
	 * @param select
	 */
	public void setSelect(String select) throws SparqlException;

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

	/**
	 * 
	 * Get the graphs used in all sparql commands.
	 * 
	 * @return
	 */
	@JsonProperty(value="from")
	public String getFrom();

	/**
	 * 
	 * Set the graphs.
	 * 
	 * @param graph
	 */
	public void setFrom(String graph);
	
	/**
	 * 
	 * Retrieve the order by used in the original select.  If this is null it is ignored.
	 * 
	 * @return
	 */
	@JsonProperty(value="orderby")
	public String getOrderBy();

	/**
	 * 
	 * Set the order by.
	 * 
	 * @param orderByStatement
	 */
	public void setOrderBy(String orderByStatement);

	/**
	 * 
	 * Retrieve the Group by used in the original select.  If this is null it is ignored.
	 * 
	 * @return
	 */
	@JsonProperty(value="groupby")
	public String getGroupBy();

	/**
	 * 
	 * Set the group by.
	 * 
	 * @param groupByStatement
	 */
	public void setGroupBy(String groupByStatement);

	/**
	 * 
	 * Retrieve the having used in the original select.  If this is null it is ignored.
	 * 
	 * @return
	 */
	@JsonProperty(value="having")
	public String getHaving();

	/**
	 * 
	 * Set the having.
	 * 
	 * @param havingStatement
	 */
	public void setHaving(String havingStatement);

	@JsonProperty(value="construct")
	public String getConstruct();
	
	public void setConstruct(String construct);

	@JsonProperty(value="limit")
	public String getLimit();
	
	public void setLimit(String limit);

	@JsonProperty(value="offset")
	public String getOffset();
	
	public void setOffset(String offset);

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