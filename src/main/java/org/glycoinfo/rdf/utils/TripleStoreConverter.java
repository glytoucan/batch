package org.glycoinfo.rdf.utils;

import org.glycoinfo.rdf.dao.SparqlEntity;

public interface TripleStoreConverter<T> {
	
	public T converter(SparqlEntity e);
}