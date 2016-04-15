package org.glycoinfo.rdf.scint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.springframework.stereotype.Component;

@Component
public abstract class ScintillateParent extends ClassHandler implements Scintillate {

	private static final Log logger = LogFactory.getLog(ScintillateParent.class);

	public ScintillateParent() {
	}

	public ScintillateParent(String prefix, String prefixIri, String className) {
		this.prefix = prefix;
		this.prefixIri = prefixIri;
		this.className = className;
	}
	
	public void update(SparqlEntity e) throws SparqlException {
		getSparqlBean().setSparqlEntity(e);
		update();
	}
}
