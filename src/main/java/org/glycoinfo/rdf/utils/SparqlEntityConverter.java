package org.glycoinfo.rdf.utils;

import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlEntity;

public interface SparqlEntityConverter<T> {
	
	public T convert(SparqlEntity e) throws SparqlException;
}