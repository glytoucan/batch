package org.glycoinfo.batch.convert.kcf;

import org.glycoinfo.ts.dao.SchemaEntity;
import org.glycoinfo.ts.utils.TripleStoreConverter;

public class ConvertTripleStoreConverter implements TripleStoreConverter<KcfConvertTriple> {

	@Override
	public KcfConvertTriple converter(SchemaEntity e) {
		KcfConvertTriple converted = new KcfConvertTriple();
		converted.setIdent(e.getValue("AccessionNumber"));
		converted.setGlycanUri(e.getValue("s"));
		converted.setSequence(e.getValue("Seq"));

		return converted;
	}
}