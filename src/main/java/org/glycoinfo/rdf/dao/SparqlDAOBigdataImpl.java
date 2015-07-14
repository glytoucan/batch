/**
 * @author aoki
 */
package org.glycoinfo.rdf.dao;


import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import org.glycoinfo.rdf.InsertSparql;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.utils.TripleStoreProperties;
import org.openrdf.model.Statement;
import org.openrdf.model.URI;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
import org.openrdf.model.impl.LiteralImpl;
import org.openrdf.model.impl.URIImpl;
import org.openrdf.model.impl.ValueFactoryImpl;
import org.openrdf.model.vocabulary.RDFS;
import org.openrdf.query.Binding;
import org.openrdf.query.BindingSet;
import org.openrdf.query.GraphQuery;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.query.Update;
import org.openrdf.query.UpdateExecutionException;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.helpers.StatementCollector;
import org.openrdf.spring.SesameConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
























//import com.bigdata.rdf.sail.BigdataSail;
//import com.bigdata.rdf.sail.BigdataSailRepository;
//
//
//
//
//
//
//
//import com.bigdata.rdf.sail.remote.BigdataSailFactory;
//import com.bigdata.rdf.sail.remote.BigdataSailRemoteRepository;
//import com.bigdata.rdf.store.BDS;
//import com.bigdata.service.AbstractTransactionService;















//import virtuoso.jdbc3.VirtuosoExtendedString;
//import virtuoso.jdbc3.VirtuosoRdfBox;
import virtuoso.sesame2.driver.VirtuosoRepository;

/**
 * @author aoki
 */
//@SuppressWarnings({ "unchecked", "rawtypes" })
public class SparqlDAOBigdataImpl implements SparqlDAO {

	public static Logger logger = (Logger) LoggerFactory
			.getLogger("org.glytoucan.registry.dao.SparqlDAOBigdataImpl");

	@Autowired
	protected SesameConnectionFactory sesameConnectionFactory;
	
//	/**
//     * Load a document into a repository.
//     * 
//     * @param repo
//     * @param resource
//     * @param baseURL
//     * @throws Exception
//     */
//    public void loadSomeDataFromADocument() throws Exception {
////    	Repository repo;
//
//    	String resource = getLoadFilePath();
//		repo = BigdataSailFactory.createRepository(     BigdataSailFactory.Option.Quads,     BigdataSailFactory.Option.TextIndex);
//		repo.initialize();
//
//        RepositoryConnection cxn = repo.getConnection();
//        cxn.setAutoCommit(false);
//        try {
//            InputStream is = getClass().getResourceAsStream(resource);
//            if (is == null && new File(resource).exists())
//                is = new FileInputStream(resource);
//            if (is == null)
//                throw new Exception("Could not locate resource: " + resource);
//            Reader reader = new InputStreamReader(new BufferedInputStream(is));
//            cxn.add(reader, getBaseUrl(), RDFFormat.TURTLE);
//            cxn.commit();
//        } catch (Exception ex) {
//            cxn.rollback();
//            throw ex;
//        } finally {
//            // close the repository connection
//            cxn.close();
//        }
//        
//    }
    
	public List<SparqlEntity> getAllClass() {
		// Query sparql =
		// QueryFactory.create("SELECT * WHERE { GRAPH ?graph { ?s a <http://www.w3.org/2002/07/owl#Class> } } limit 100");
		// VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create
		// (sparql, set);
		// ResultSet results = vqe.execSelect();
		ArrayList al = new ArrayList();
		// while (results.hasNext()) {
		// QuerySolution result = results.nextSolution();
		// SchemaEntity
		//
		// al.add(s);
		// logger.debug( " { " + s + " . }");
		// }
		return al;
	}
	
	public List<SparqlEntity> getDomain(String subject) {
		// Query sparql =
		// QueryFactory.create("SELECT * WHERE  { ?s ?p ?o } limit 100");
		// VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create
		// (sparql, set);
		// ResultSet results = vqe.execSelect();
		ArrayList al = new ArrayList();
		// while (results.hasNext()) {
		// QuerySolution result = results.nextSolution();
		// // RDFNode graph = result.get("graph");
		// SchemaEntity s = ;
		// al.add(s);
		// logger.debug( " { " + s + " " + p + " " + o + " . }");
		// }
		return al;
	}

	public List<SparqlEntity> getRange(String subject) {
		// Query sparql =
		// QueryFactory.create("SELECT * WHERE  { ?s ?p ?o } limit 100");
		// VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create
		// (sparql, set);
		// ResultSet results = vqe.execSelect();
		ArrayList al = new ArrayList();
		// while (results.hasNext()) {
		// QuerySolution result = results.nextSolution();
		// // RDFNode graph = result.get("graph");
		// RDFNode s = result.get("s");
		// RDFNode p = result.get("p");
		// RDFNode o = result.get("o");
		//
		// al.add(s);
		// logger.debug( " { " + s + " " + p + " " + o + " . }");
		// }
		return al;
	}

	public List<SparqlEntity> query(String query) throws SparqlException {
		
        try {
        
//            repo.initialize();
            // demonstrate some basic functionality
//            final URI MIKE = new URIImpl(
//                    "http://www.bigdata.com/rdf#Mike");
//            sampleCode.loadSomeData(repo);
//            System.out.println("Loaded sample data.");
//            sampleCode.readSomeData(repo, MIKE);
//            sampleCode.executeSelectQuery(repo,
//                    "select ?p ?o where { <" + MIKE.toString()
//                            + "> ?p ?o . }", QueryLanguage.SPARQL);
//            System.out.println("Did SELECT query.");
//            sampleCode.executeConstructQuery(repo,
//                    "construct { <" + MIKE.toString()
//                            + "> ?p ?o . } where { <" + MIKE.toString()
//                            + "> ?p ?o . }", QueryLanguage.SPARQL);
//            System.out.println("Did CONSTRUCT query.");
//            executeFreeTextQuery(repo);
            
//            executeSelectQuery(repo, query, QueryLanguage.SPARQL);
            // run one of the LUBM tests
            // sampleCode.doU10(); // I see loaded: 1752215 in 116563 millis: 15032 stmts/sec, what do you see?
            // sampleCode.doU1();
            List<SparqlEntity> al = null;

    		RepositoryConnection connection = sesameConnectionFactory.getConnection();
    			try {
    				al = doTupleQuery(connection, query);
    			} catch (MalformedQueryException | QueryEvaluationException e) {
    				e.printStackTrace();
    				throw new SparqlException(e);
    			}
    			return al;
        
    } catch (Exception ex) {
        ex.printStackTrace();
    } finally {
    }

return null;

	}

	public static List<SparqlEntity> doTupleQuery(RepositoryConnection con,
			String query) throws RepositoryException, MalformedQueryException,
			QueryEvaluationException {
		TupleQuery resultsTable = con.prepareTupleQuery(QueryLanguage.SPARQL,
				query);

		TupleQueryResult bindings = resultsTable.evaluate();
		
		ArrayList<SparqlEntity> results = new ArrayList<SparqlEntity>();
		for (int row = 0; bindings.hasNext(); row++) {
			logger.debug("RESULT " + (row + 1) + ": ");
			BindingSet pairs = bindings.next();
			List<String> names = bindings.getBindingNames();
			Value[] rv = new Value[names.size()];

			SparqlEntity values = new SparqlEntity();
			for (int i = 0; i < names.size(); i++) {
				String name = names.get(i);
				Value value = pairs.getValue(name);
				Binding bind = pairs.getBinding(name);

				rv[i] = value;
				// if(column > 0) System.out.print(", ");
				logger.debug("\t" + name + "=" + value);
				// vars.add(value);
				// if(column + 1 == names.size()) logger.debug(";");
				String stringvalue = null;
				if (null == value)
					stringvalue = "";
				else
					stringvalue = value.stringValue();

				values.setValue(name, stringvalue);
			}
			results.add(values);
		}
		return results;
	}

	public void delete(String statement) throws SparqlException {
		execute(statement);
	}
	
	public void execute(String statement) throws SparqlException {
		RepositoryConnection connection = sesameConnectionFactory.getConnection();

		logger.debug("executing update:>" + statement + "<");
		Update update;
		try {
			update = connection.prepareUpdate(QueryLanguage.SPARQL, statement);
			update.execute();
		} catch (RepositoryException | MalformedQueryException | UpdateExecutionException e) {
			throw new SparqlException(e);
		}
	}

	@Override
	public List<SparqlEntity> query(SelectSparql select) throws SparqlException {
		return query(select.getSparql());
	}

	public void insert(String insert) throws SparqlException {
		execute(insert);
	}

	@Override
	public void insert(InsertSparql insert)
			throws SparqlException {
		insert(insert.getSparql());
	}

	@Override
	public int load(String file) throws SparqlException {
		// TODO Auto-generated method stub
		return 0;
	}
}