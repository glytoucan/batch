package org.glycoinfo.rdf;

import org.glycoinfo.rdf.dao.SparqlEntity;

/**
 * 
 * Representing the parts of an insert statement.  Nullable clauses can be ignored with null, whitespace or a blank string.
 * 
 * For simplicity, this also contains helper clauses such as the failInsert.  Which can be called when the original insert fails.
 * 
 * A simple insert statment:
 * 
 * insert into graph <" + graph + "> " + using + "{ " + insert + " }" + where
 * 
 *The following describes the parts of a complex statement. 
 * 
 * prefix:
 * "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n" +
 * "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
 * "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n" +
 * "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> \n" +
 * "PREFIX dc: <http://purl.org/dc/elements/1.1/> \n" +
 * "PREFIX dcterms: <http://purl.org/dc/terms/> \n" +
 * "PREFIX dbpedia2: <http://dbpedia.org/property/> \n" +
 * "PREFIX dbpedia: <http://dbpedia.org/> \n" +
 * "PREFIX foaf: <http://xmlns.com/foaf/0.1/> \n" +
 * "PREFIX skos: <http://www.w3.org/2004/02/skos/core#> \n" +
 * "PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#> \n" +
 * "PREFIX glytoucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#> \n";
 * 
 * graph:
 * into graph <"http://www.glytoucan.org/rdf/construction">
 * 
 * using:
 * "USING <http://glytoucan.org/rdf/demo/0.2>\n" +
 * "USING <http://glytoucan.org/rdf/demo/msdb/8>\n" +
 * "USING <http://purl.jp/bio/12/glyco/glycan/ontology/0.18>\n" +
 * "USING <http://www.glytoucan.org/glyco/owl/glytoucan>\n";
 * 
 * insert:
 * ?s a glycan:saccharide .
 * ?s glytoucan:has_primary_id ?AccessionNumber .
 * 
 * where:
 * ?s a glycan:saccharide.\n" +
 * ?s glytoucan:has_primary_id ?AccessionNumber .
 * 
 * prefix + "\n" + "INSERT\n" + "{ GRAPH <"+graph+"> {\n" + insert + "  }\n" + "}\n" +
 * using + "WHERE {\n" + where + "}"
 * 
 * @author aoki
 *
 */
public interface DeleteSparql {

	public static final String SPARQL = "SPARQL";
	public static final String Turtle = "Turtle";

	/**
	 * 
	 * Delete section.
	 * 
	 * @return
	 * @throws SparqlException 
	 */
	public String getDelete() throws SparqlException;

	/**
	 * 
	 * Set the delete section.
	 * 
	 * @param insert
	 */
	public void setDelete(String insert);

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
	 * Get the using clause.
	 * 
	 * @return
	 */
	public String getUsing();

	/**
	 * 
	 * Set the using clause.
	 * 
	 * @param using
	 */
	public void setUsing(String using);

	/**
	 * 
	 * Retrieve the overall sparql constructed from all of the parts above.
	 * 
	 * @return
	 * @throws SparqlException 
	 */
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
	public SparqlEntity getSparqlEntity();
	
	public String getFormat();
}