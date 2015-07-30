package org.glycoinfo.rdf.dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.glycoinfo.rdf.InsertSparql;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.utils.TripleStoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.query.ResultSetFormatter;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.sparql.util.Context;
import com.hp.hpl.jena.update.UpdateAction;
import com.hp.hpl.jena.update.UpdateExecutionFactory;
import com.hp.hpl.jena.update.UpdateFactory;
import com.hp.hpl.jena.update.UpdateProcessor;
import com.hp.hpl.jena.update.UpdateRequest;


/**
 * @author aoki
 *
 */
public class SparqlDAOJenaImpl implements SparqlDAO {
	
	public static Logger logger=(Logger) LoggerFactory.getLogger("org.glytoucan.registry.dao.SparqlDAOJenaImpl");

	TripleStoreProperties datasource;
	String graph = null;

	public TripleStoreProperties getTripleStoreProperties() {
		return datasource;
	}

	public void setTripleSource(TripleStoreProperties datasource) {
		this.datasource = datasource;
	}

	public List<SparqlEntity> query(String select) {
//		SimpleAuthenticator authenticator = new SimpleAuthenticator("admin", "pw123".toCharArray());
//		PreemptiveBasicAuthenticator basicAuth = new PreemptiveBasicAuthenticator(authenticator);
		
        Query query = QueryFactory.create(select); //s2 = the query above
		QueryExecution qe = QueryExecutionFactory.sparqlService("http://localhost:3030/a/sparql",
		                                                            query);

//		qef = new QueryExecutionFactoryRetry(qef, 5, 10000);
		
		// Add delay in order to be nice to the remote server (delay in milli seconds)
//		qef = new QueryExecutionFactoryDelay(qef, 5000);

		// Set up a cache
		// Cache entries are valid for 1 day
		long timeToLive = 24l * 60l * 60l * 1000l; 
		
		// This creates a 'cache' folder, with a database file named 'sparql.db'
		// Technical note: the cacheBackend's purpose is to only deal with streams,
		// whereas the frontend interfaces with higher level classes - i.e. ResultSet and Model

//		CacheBackend cacheBackend = CacheCoreH2.create("sparql", timeToLive, true);
//		CacheFrontend cacheFrontend = new CacheFrontendImpl(cacheBackend);		
//		qef = new QueryExecutionFactoryCacheEx(qef, cacheFrontend);
 
		

//		QueryExecutionFactoryHttp foo = qef.unwrap(QueryExecutionFactoryHttp.class);
//		System.out.println(foo);
		
		// Add pagination
//		qef = new QueryExecutionFactoryPaginated(qef, 900);

		// Create a QueryExecution object from a query string ...
//		QueryExecution qe = qef.createQueryExecution(select);
		
		// and run it.
		ResultSet rs = qe.execSelect();
//		logger.debug(ResultSetFormatter.asText(rs));
//        ResultSetFormatter.out(System.out, rs, query);
		List<SparqlEntity> results = new ArrayList<SparqlEntity>();
		if (!rs.hasNext())
			logger.debug("no results");
		logger.debug(">" + rs.getRowNumber() + "<");
//		OntModel ontology; //replace with your method for creating an OntModel
		
		while (rs.hasNext()) {
		   QuerySolution row = rs.next();
		   Iterator<String> columns = row.varNames();
		   SparqlEntity se = new SparqlEntity();
		   while (columns.hasNext()) {
			  String column = columns.next();
		      RDFNode cell = row.get(column);

		      if (cell.isResource()) {
		    	  Resource resource =  cell.asResource();
		    	  //do something maybe with the OntModel???
		    	  if (resource.isLiteral())
		    		  se.setValue(column, resource.asLiteral().getString());
		    	  else
		    		  se.setValue(column, resource.toString());
		      }
		      else if (cell.isLiteral()) {
		    	  se.setValue(column, cell.asLiteral().getString());
		      } else if (cell.isAnon()) {
		    	  se.setValue(column, "anon");
		      } else {
		    	  se.setValue(column, cell.toString());
		      }
		   }
		   results.add(se);
		}
		return results; 
	}
	
	public List<SparqlEntity> insert(String graph, String insert, boolean clear) {
		return null;
	}

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
	public void execute(String execute) throws SparqlException {
		UpdateRequest queryObj=UpdateFactory.create(execute);
	    UpdateProcessor qexec=UpdateExecutionFactory.createRemoteForm(queryObj,"http://localhost:3030/a/update");
//	    queryObj.
	    qexec.execute();
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
	}

	public String getGraph() {
		return graph;
	}

	public void setGraph(String graph) {
		this.graph = graph;
	}

	@Override
	public int load(String file) throws SparqlException {
		// TODO Auto-generated method stub
		return 0;
	}
}
