package org.glycoinfo.ts.dao;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbcp.BasicDataSource;
import org.glycoinfo.ts.utils.TripleStoreProperties;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;

public interface SchemaDAO {
	
	TripleStoreProperties getTripleSource();
	void setTripleSource(TripleStoreProperties ts);
	
	List<SchemaEntity> getAllClass();
	List<SchemaEntity> getDomain(String subject);
	List<SchemaEntity> getRange(String subject);
	List<SchemaEntity> query(String subject);
	List<SchemaEntity> query(String subject, String baseURI);
	List<SchemaEntity> insert(String graph, String insert, boolean clear) throws SQLException;
}