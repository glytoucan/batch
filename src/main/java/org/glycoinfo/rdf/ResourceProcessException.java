package org.glycoinfo.rdf;

public class ResourceProcessException extends Exception {

	public ResourceProcessException(ResourceProcessResult result) {
		super(result.getLogMessage().getDescription());
	}
}