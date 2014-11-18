package org.glycoinfo.batch.convert;

import org.glycoinfo.ts.dao.SchemaEntity;
import org.glycoinfo.ts.utils.TripleStoreConverter;

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
