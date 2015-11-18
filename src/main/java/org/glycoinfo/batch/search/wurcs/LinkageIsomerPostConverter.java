package org.glycoinfo.batch.search.wurcs;

import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.glycan.relation.LinkageIsomer;
import org.glycoinfo.rdf.utils.SparqlEntityConverter;

public class LinkageIsomerPostConverter implements SparqlEntityConverter<SparqlEntity> {

	@Override
	public SparqlEntity convert(SparqlEntity e) {
		e.setValue(LinkageIsomer.URI, e.getValue(IsomerSubstructureSearchSparql.SubstructureSearchSaccharideURI));
		return e;
	}
}