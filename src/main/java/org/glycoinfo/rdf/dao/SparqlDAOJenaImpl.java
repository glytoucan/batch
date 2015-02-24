package org.glycoinfo.rdf.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.dbcp.BasicDataSource;
import org.glycoinfo.rdf.InsertSparql;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.utils.TripleStoreProperties;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import virtuoso.jena.driver.VirtGraph;
import virtuoso.jena.driver.VirtuosoQueryExecution;
import virtuoso.jena.driver.VirtuosoQueryExecutionFactory;
import ch.qos.logback.classic.Logger;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.RDFNode;


/**
 * @author aoki
 *
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class SparqlDAOJenaImpl implements SparqlDAO {
	public static Logger logger=(Logger) LoggerFactory.getLogger("org.glytoucan.registry.dao.SchemaDAOImpl");

	TripleStoreProperties datasource;

	public TripleStoreProperties getTripleStoreProperties() {
		return datasource;
	}

	public void setTripleSource(TripleStoreProperties datasource) {
		this.datasource = datasource;
	}

	VirtGraph set = new VirtGraph ("jdbc:virtuoso://superdell:1111", "dba", "dba");

	public List<SparqlEntity> getAllClass() {
		Query sparql = QueryFactory.create("SELECT * WHERE { GRAPH ?graph { ?s a <http://www.w3.org/2002/07/owl#Class> } } limit 100");
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, set);
		ResultSet results = vqe.execSelect();
		ArrayList al = new ArrayList();
//		while (results.hasNext()) {
//			QuerySolution result = results.nextSolution();
//			SchemaEntity
//
//		    al.add(s);
//		    logger.debug( " { " + s + " . }");
//		}
		return al;
	}

	public List<SparqlEntity> getDomain(String subject) {
		Query sparql = QueryFactory.create("SELECT * WHERE  { ?s ?p ?o } limit 100");
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, set);
		ResultSet results = vqe.execSelect();
		ArrayList al = new ArrayList();
//		while (results.hasNext()) {
//			QuerySolution result = results.nextSolution();
////		    RDFNode graph = result.get("graph");
//			SchemaEntity s = ;
//		    al.add(s);
//		    logger.debug( " { " + s + " " + p + " " + o + " . }");
//		}
		return al;
	}

	public List<SparqlEntity> getRange(String subject) {
		Query sparql = QueryFactory.create("SELECT * WHERE  { ?s ?p ?o } limit 100");
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, set);
		ResultSet results = vqe.execSelect();
		ArrayList al = new ArrayList();
//		while (results.hasNext()) {
//			QuerySolution result = results.nextSolution();
////		    RDFNode graph = result.get("graph");
//		    RDFNode s = result.get("s");
//		    RDFNode p = result.get("p");
//		    RDFNode o = result.get("o");
//
//		    al.add(s);
//		    logger.debug( " { " + s + " " + p + " " + o + " . }");
//		}
		return al;
	}
	
	public List<SparqlEntity> query(String subject) {
		Query sparql = QueryFactory.create(subject);
		sparql.setLimit(1000);
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, set);
		ResultSet results = vqe.execSelect();
		ArrayList al = new ArrayList();
		List columns = results.getResultVars();
		while (results.hasNext()) {
			QuerySolution result = results.nextSolution();
			logger.debug(results.getResultVars().toString());
//		    RDFNode graph = result.get("graph");
			SparqlEntity spo = new SparqlEntity();
			spo.setColumns(columns);
			for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
				String col = (String) iterator.next();
			    spo.setValue(col, result.get(col).toString());
			}

		    al.add(spo);
		    logger.debug( " { " + spo + " . }");
		}
		return al;
	}
	
	public List<SparqlEntity> query(String subject, String baseURI) {
		Query sparql = QueryFactory.create(subject, baseURI);
		sparql.setLimit(1000);
		VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create (sparql, set);
		ResultSet results = vqe.execSelect();
		ArrayList al = new ArrayList();
		List columns = results.getResultVars();
		while (results.hasNext()) {
			QuerySolution result = results.nextSolution();
			logger.debug(results.getResultVars().toString());
//		    RDFNode graph = result.get("graph");
			SparqlEntity spo = new SparqlEntity();
			spo.setColumns(columns);
			for (Iterator iterator = columns.iterator(); iterator.hasNext();) {
				String col = (String) iterator.next();
			    spo.setValue(col, result.get(col).toString());
			}

		    al.add(spo);
		    logger.debug( " { " + spo + " . }");
		}
		return al;
	}

	public List<SparqlEntity> insert(String graph, String insert, boolean clear) {
		return null;
	}

	@Override
	public void setTripleStoreProperties(TripleStoreProperties ts)
			throws SparqlException {
		
	}

	@Override
	public List<SparqlEntity> query(SelectSparql select) throws SparqlException {
		return query(select.getSparql());
	}

	public void insert(String insert) throws SparqlException {
		execute(insert);
	}

	@Override
	public void insert(InsertSparql insert) throws SparqlException {
		insert(insert.getSparql());
	}

	@Override
	public void delete(String string) throws SparqlException {
		execute(string);
	}

	@Override
	public void execute(String string) throws SparqlException {
		// TODO
	}
}