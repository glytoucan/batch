package org.glycoinfo.rdf;

public class SparqlException extends Exception {

	/*
	 * generated serial id
	 */
	private static final long serialVersionUID = -7329593754733340638L;

	public SparqlException(Exception e) {
		super(e);
	}
	
	public SparqlException(String message) {
		super(message);
	}
}