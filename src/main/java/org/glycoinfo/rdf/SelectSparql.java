package org.glycoinfo.rdf;

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
public interface SelectSparql {

	/**
	 * 
	 * The prefix used for all sparql.
	 * 
	 * @return
	 */
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
	public String getSelect();

	/**
	 * 
	 * Set the select.
	 * 
	 * @param select
	 */
	public void setSelect(String select);

	/**
	 * 
	 * Retrieve the where clause.
	 * 
	 * @return
	 */
	public String getWhere();

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
	public String getGraph();

	/**
	 * 
	 * Set the graphs.
	 * 
	 * @param graph
	 */
	public void setGraph(String graph);
	
	/**
	 * 
	 * Retrieve the order by used in the original select.  If this is null it is ignored.
	 * 
	 * @return
	 */
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
	public String getHaving();

	/**
	 * 
	 * Set the having.
	 * 
	 * @param havingStatement
	 */
	public void setHaving(String havingStatement);

	public String getConstruct();
	
	public void setConstruct(String construct);
	
	/**
	 * 
	 * Retrieve the overall sparql constructed from all of the parts above.
	 * 
	 * @return
	 */
	public String getSparql();
}