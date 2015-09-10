package org.glycoinfo.batch.search.wurcs;

import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.glycan.Saccharide;
import org.glycoinfo.rdf.utils.SparqlEntityConverter;

public class MotifPostConverter implements SparqlEntityConverter<SparqlEntity> {

	@Override
	public SparqlEntity convert(SparqlEntity e) {
		e.setValue(Saccharide.URI, e.getValue(SubstructureSearchSparql.SubstructureSearchSaccharideURI));
		return e;
	}
}
