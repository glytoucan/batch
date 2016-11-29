package org.glycoinfo.rdf;

/*
 * Representing the parts of  an Replace statement.
 *  
 * A simple replace statement: 
 * 
 * delete from graph "<" + graph + ">" + insert to graph "<" + graph + ">" + using graph + "<" + graph + ">" + where
 *  
 * prefix:
 * "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n" +
 * "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n" +
 * "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n" +
 * "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> \n" +
 * "PREFIX dc: <http://purl.org/dc/elements/1.1/> \n" +
 * "PREFIX dcterms: <http://purl.org/dc/terms/> \n" +
 * "PREFIX foaf: <http://xmlns.com/foaf/0.1/> \n" +
 * "PREFIX skos: <http://www.w3.org/2004/02/skos/core#> \n" +
 * "PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#> \n" +
 * "PREFIX glytoucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#> \n";
 * "PREFIX gly:  <http://rdf.glycoinfo.org/glycan/> \n";
 * 
 * delete:
 * ?Saccharide glycan:has_motif ?GlycanMotif.
 * ?GlycanMotif a glycan:glycan_motif.
 * ?GlycanMotif glycan:has_glycosequence ?GSequence.
 * ?GlycanMotif rdfs:label ?MotifName.
 * ?GlycanMotif glytoucan:is_reducing_end ?ReducingEnd. 
 * 
 * insert:
 * gly:G12345BB a glycan:glycan_motif.
 * gly:G12345BB glycan:has_glycosequence ?GSequence.
 * gly:G12345BB rdfs:label ?MotifName.
 * gly:G12345BB glytoucan:is_reducing_end ?ReducingEnd. 
 * 
 * using:
 * USING <http://rdf.glytoucan.org/motif>
 * 
 * where:
 * ?Saccharide glycan:has_motif gly:G122345AA.
 * gly:G12345AA a glycan:glycan_motif.
 * gly:G12345AA glycan:has_glycosequence ?GSequence.
 * gly:G12345AA rdfs:label ?MotifName.
 * gly:G12345AA glytoucan:is_reducing_end ?ReducingEnd. 
 * 
 * 
 * DELETE "{" + GRAPH "<" + graph + ">" + "{" delete + "}}"
 * INSERT "{" + GRAPH "<" + graph + ">" + "{" insert + "}}"
 * USINGS "<" + graph + ">"
 * WHERE "{" + where + "}"
 * 
 * @author shinmachi
 * 
 */

public interface DeleteInsertSparql {

	public static final String SPARQL = "SPARQL";
	public static final String Turtle = "Turtle";
	public static final String DELETEWHERE = "DELETE WHERE";
	
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
	 * Insert command.
	 * 
	 * @return
	 * @throws SparqlException 
	 */
	public String getInsert() throws SparqlException;

	/**
	 * 
	 * Set the insert command.
	 * 
	 * @param insert
	 */
	public void setInsert(String insert);
	
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

	public String getFormat();
}
