package org.glycoinfo.ts.utils;

import org.glycoinfo.ts.dao.SchemaEntity;

public interface TripleStoreConverter<T> {
	
	public T converter(SchemaEntity e);
}