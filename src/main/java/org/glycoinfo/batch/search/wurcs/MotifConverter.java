package org.glycoinfo.batch.search.wurcs;

import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.glycan.GlycoSequence;
import org.glycoinfo.rdf.utils.SparqlEntityConverter;

public class MotifConverter implements SparqlEntityConverter<SparqlEntity> {

	@Override
	public SparqlEntity convert(SparqlEntity e) {
		e.remove(GlycoSequence.AccessionNumber);
		return e;
	}
}
