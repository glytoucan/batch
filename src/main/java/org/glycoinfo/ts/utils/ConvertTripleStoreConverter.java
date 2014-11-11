package org.glycoinfo.ts.utils;

import org.glycoinfo.batch.ConvertTriple;
import org.glycoinfo.ts.dao.SchemaEntity;

public class ConvertTripleStoreConverter implements TripleStoreConverter<ConvertTriple> {

	@Override
	public ConvertTriple converter(SchemaEntity e) {
		ConvertTriple converted = new ConvertTriple();
		converted.setIdent(e.getValue("AccessionNumber"));
		converted.setSubjectLocation(e.getValue("s"));
		converted.setSequence(e.getValue("Seq"));

		return converted;
	}
}
