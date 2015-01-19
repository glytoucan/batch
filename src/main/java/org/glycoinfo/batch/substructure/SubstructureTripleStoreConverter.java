package org.glycoinfo.batch.substructure;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.ts.dao.SchemaEntity;
import org.glycoinfo.ts.utils.TripleStoreConverter;

public class SubstructureTripleStoreConverter implements TripleStoreConverter<SubstructureTriple> {
	protected Log logger = LogFactory.getLog(getClass());

	@Override
	public SubstructureTriple converter(SchemaEntity e) {
		logger.debug("converting:>" + e + "<");

		SubstructureTriple converted = new SubstructureTriple();
		converted.setIdent(e.getValue("AccessionNumber"));
		converted.setSupIri(e.getValue("glycans"));
		converted.setKeyword(e.getValue("Seq"));

		return converted;
	}
}
