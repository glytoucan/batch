package org.glycoinfo.rdf.utils;

import org.glycoinfo.rdf.dao.SparqlEntity;

public interface SparqlEntityConverter<T> {
	
	public T converter(SparqlEntity e);
}