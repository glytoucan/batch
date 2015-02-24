package org.glycoinfo.batch.search.wurcs;

import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.glycan.Motif;
import org.glycoinfo.rdf.glycan.Saccharide;
import org.glycoinfo.rdf.utils.SparqlEntityConverter;

public class MotifConverter implements SparqlEntityConverter<SparqlEntity> {

	@Override
	public SparqlEntity converter(SparqlEntity e) {
//		e.setValue(Motif.URI, e.getValue(Saccharide.URI)); actually dont need this.
		return e;
	}
}
