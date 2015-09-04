package org.glycoinfo.rdf.glycan.wurcs;

import org.glycoinfo.WURCSFramework.util.array.WURCSFormatException;
import org.glycoinfo.WURCSFramework.util.array.WURCSImporter;
import org.glycoinfo.WURCSFramework.wurcs.array.WURCSArray;
import org.glycoinfo.WURCSFramework.wurcs.rdf.WURCSExporterRDF;
import org.glycoinfo.rdf.InsertSparqlBean;
import org.glycoinfo.rdf.glycan.GlycoSequence;
import org.glycoinfo.rdf.glycan.Saccharide;

public class WurcsRDFInsertSparql extends InsertSparqlBean {
	
	public WurcsRDFInsertSparql() {
		super();
		this.prefix="PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#> \nPREFIX wurcs: <http://www.glycoinfo.org/glyco/owl/wurcs#> \n";
	}
	
	@Override
	public String getInsert()  {
		WURCSImporter ws = new WURCSImporter();
		WURCSExporterRDF rdf = new WURCSExporterRDF();
		String sequence = getSparqlEntity().getValue(GlycoSequence.Sequence);
		String id = getSparqlEntity().getValue(Saccharide.PrimaryId);
		WURCSArray wurcs = null;
		try {
			wurcs = ws.extractWURCSArray(sequence);
		} catch (WURCSFormatException e) {
			e.printStackTrace();
			logger.debug(e.getMessage());
		}
		rdf.setWURCSrdfTriple(id, wurcs, false);
		String wurcsrdf = rdf.getWURCS_RDF();
		logger.debug(wurcsrdf);
//		String withPrefix = prefix + wurcsrdf;
//		return withPrefix;
//		sbMS.append(rdf.getWURCS_monosaccharide_RDF());
		return wurcsrdf;
	}
	
//	@Override
//	public String getFormat() {
//		return InsertSparql.Turtle;
//	}
}