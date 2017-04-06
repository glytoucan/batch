package org.glycoinfo.rdf;

public interface InsertWhereSparql extends SparqlBean {

	public static final String SPARQL = "SPARQL";
	public static final String Turtle = "Turtle";
	

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
	 * Get the graphs as target of insert used in sparql commands.
	 * 
	 * @return
	 */
	public String getToGraph();

	/**
	 * 
	 * Set the graphs as target of insert.
	 * 
	 * @param graph
	 */
	public void setToGraph(String toGraph);
	
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
