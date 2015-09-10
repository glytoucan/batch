package org.glycoinfo.batch.glyconvert.kcf;

import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.utils.SparqlEntityConverter;

public class ConvertSparqlConverter implements SparqlEntityConverter<KcfConvertSparql> {

	@Override
	public KcfConvertSparql convert(SparqlEntity e) {
		KcfConvertSparql converted = new KcfConvertSparql();
		converted.setIdent(e.getValue("AccessionNumber"));
//		converted.setGlycanUri(e.getValue("s"));
		converted.setSequence(e.getValue("Seq"));

		return converted;
	}
}