package org.glycoinfo.rdf;

/**
 * 
 * Representing the parts of an insert statement.  Nullable clauses can be ignored with null, whitespace or a blank string.
 * 
 * For simplicity, this also contains helper clauses such as the failInsert.  Which can be called when the original insert fails.
 * 
 * A simple insert statment:
 * 
 * insert into graph <" + graph + "> " + "{ " + insert + " }"
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
 * "http://www.glytoucan.org/rdf/construction";
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
public interface InsertSparql {

	/**
	 * 
	 * Insert command.
	 * 
	 * @return
	 */
	public String getInsert();

	/**
	 * 
	 * Set the insert command.
	 * 
	 * @param insert
	 */
	public void setInsert(String insert);

	/**
	 * 
	 * In case the original insert fails, a new fail condition is retrieved here
	 * and used, if this also fails, there is nothing more than can be done.
	 * 
	 * @return
	 */
	public String getFailInsert();

	/**
	 * 
	 * Set the fail command.
	 * 
	 * @param failInsert
	 */
	public void setFailInsert(String failInsert);

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
	 * Retrieve the overall sparql constructed from all of the parts above.
	 * 
	 * @return
	 */
	public String getSparql();
}