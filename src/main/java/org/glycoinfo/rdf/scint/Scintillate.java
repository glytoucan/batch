package org.glycoinfo.rdf.scint;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;

@Component
public class Scintillate extends ClassHandler {
	public final static String NO_DOMAINS = "no_domains";
	private static final Log logger = LogFactory.getLog(Scintillate.class);

	public Scintillate() {
	}

	public Scintillate(String prefix, String prefixIri, String className) {
		this.prefix = prefix;
		this.prefixIri = prefixIri;
		this.className = className;
	}
}
