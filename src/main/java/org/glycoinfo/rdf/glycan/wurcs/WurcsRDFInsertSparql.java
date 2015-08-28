package org.glycoinfo.rdf.glycan.wurcs;

import org.glycoinfo.WURCSFramework.util.array.WURCSFormatException;
import org.glycoinfo.WURCSFramework.util.array.WURCSImporter;
import org.glycoinfo.WURCSFramework.wurcs.array.WURCSArray;
import org.glycoinfo.WURCSFramework.wurcs.rdf.WURCSExporterRDF;
import org.glycoinfo.rdf.InsertSparql;
import org.glycoinfo.rdf.InsertSparqlBean;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.glycan.GlycoSequence;
import org.glycoinfo.rdf.glycan.Saccharide;

public class WurcsRDFInsertSparql extends InsertSparqlBean {
	
	@Override
	public String getSparql() throws SparqlException {
		WURCSImporter ws = new WURCSImporter();
		WURCSExporterRDF rdf = new WURCSExporterRDF();
		String sequence = getSparqlEntity().getValue(GlycoSequence.Sequence);
		String id = getSparqlEntity().getValue(Saccharide.PrimaryId);
		WURCSArray wurcs;
		try {
			wurcs = ws.extractWURCSArray(sequence);
		} catch (WURCSFormatException e) {
			throw new SparqlException(e);
		}
		rdf.setWURCSrdfTriple(id, wurcs, true);
		String wurcsrdf = rdf.getWURCS_RDF();
		logger.debug(wurcsrdf);
		return wurcsrdf;
//		sbMS.append(rdf.getWURCS_monosaccharide_RDF());
	}
	
	@Override
	public String getFormat() {
		return InsertSparql.Turtle;
	}
}