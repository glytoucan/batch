package org.glycoinfo.rdf.glycan;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.glycoinfo.rdf.InsertSparql;
import org.glycoinfo.rdf.InsertSparqlBean;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.slf4j.LoggerFactory;

import ch.qos.logback.classic.Logger;

/**
 * 
 * <SaccharideURI> glycan#has_motif <SaccharideURI>
 * 
 * @author aoki
 *
 */
public class MotifInsertSparql extends InsertSparqlBean {

	public static Logger logger = (Logger) LoggerFactory
			.getLogger(MotifInsertSparql.class);
	
	public MotifInsertSparql() {
		this.prefix = "prefix xsd: <http://www.w3.org/2001/XMLSchema#>\n"
				+ "prefix wurcs: <http://www.glycoinfo.org/glyco/owl/wurcs#>\n"
				+ "PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
				+ "PREFIX toucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>\n";
//			insert.append("<" + getSupIri() + "> glycan:has_substructure <" + string + "> .\n");
//			insert.append("<" + string + "> glycan:has_superstructure <" + getSupIri() + "> .\n");

		this.graph = "http://rdf.glytoucan.org/motif";
	}
	
	@Override
	public String getInsert() {
		return "<" + getSparqlEntity().getValue(Saccharide.URI) + "> glycan:has_motif <" + getSparqlEntity().getValue(Motif.URI) + "> .";
	}
}