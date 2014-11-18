package org.glycoinfo.batch.substructure;

import org.glycoinfo.ts.dao.SchemaEntity;
import org.glycoinfo.ts.utils.TripleStoreConverter;

public class SubstructureTripleStoreConverter implements TripleStoreConverter<SubstructureTriple> {

	@Override
	public SubstructureTriple converter(SchemaEntity e) {
		SubstructureTriple converted = new SubstructureTriple();
		converted.setIdent(e.getValue("AccessionNumber"));
		converted.setSupIri(e.getValue("glycans"));
		converted.setKeyword(e.getValue("Seq"));

		return converted;
	}
}
