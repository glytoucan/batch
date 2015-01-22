package org.glycoinfo.batch.convert.kcf;

import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.utils.TripleStoreConverter;

public class ConvertSparqlConverter implements TripleStoreConverter<KcfConvertSparql> {

	@Override
	public KcfConvertSparql converter(SparqlEntity e) {
		KcfConvertSparql converted = new KcfConvertSparql();
		converted.setIdent(e.getValue("AccessionNumber"));
		converted.setGlycanUri(e.getValue("s"));
		converted.setSequence(e.getValue("Seq"));

		return converted;
	}
}