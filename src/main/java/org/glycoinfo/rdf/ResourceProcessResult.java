package org.glycoinfo.rdf;

import jp.bluetree.log.Entry;
import jp.bluetree.log.LevelType;

public class ResourceProcessResult {

	Entry logMessage;
	String id;

	public ResourceProcessResult(String logDescription, LevelType status) {
		logMessage = new Entry();
		logMessage.setMessage(logDescription);
		logMessage.setLevel(status);
	}

	public Entry getLogMessage() {
		return logMessage;
	}

	public void setLogMessage(Entry logMessage) {
		this.logMessage = logMessage;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
}
