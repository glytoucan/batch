package org.glycoinfo.rdf;

import org.glytoucan.core.Log;
import org.glytoucan.core.Status;

public class ResourceProcessResult {

	Log logMessage;
	String id;

	public ResourceProcessResult(String logDescription, Status status) {
		logMessage = new Log();
		logMessage.setDescription(logDescription);
		logMessage.setStatus(status);
	}

	public Log getLogMessage() {
		return logMessage;
	}

	public void setLogMessage(Log logMessage) {
		this.logMessage = logMessage;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
