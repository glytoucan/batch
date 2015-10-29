package org.glycoinfo.rdf;

public class DuplicateException extends SparqlException {
	private static final long serialVersionUID = 1L;

	String id;
	
	public DuplicateException(Exception e) {
		super(e);
	}

	public DuplicateException(Exception e, String id) {
		super(e);
		this.id = id;
	}

	public DuplicateException(String msg, String id) {
		super(msg);
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
