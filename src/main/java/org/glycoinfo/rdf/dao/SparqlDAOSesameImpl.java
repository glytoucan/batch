/**
 * @author aoki
 */
package org.glycoinfo.rdf.dao;


import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.glycoinfo.rdf.DeleteSparql;
import org.glycoinfo.rdf.InsertSparql;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SparqlException;
import org.openrdf.model.Value;
import org.openrdf.query.Binding;
import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.query.Update;
import org.openrdf.query.UpdateExecutionException;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.rio.RDFFormat;
import org.openrdf.rio.RDFParseException;
import org.openrdf.spring.SesameConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
//import virtuoso.jdbc3.VirtuosoExtendedString;
//import virtuoso.jdbc3.VirtuosoRdfBox;

/**
 * @author aoki
 */
//@SuppressWarnings({ "unchecked", "rawtypes" })
public class SparqlDAOSesameImpl implements SparqlDAO {

	public static Logger logger = (Logger) LoggerFactory
			.getLogger(SparqlDAOSesameImpl.class);

	@Autowired
	protected SesameConnectionFactory sesameConnectionFactory;
	
	/**
     * Load a document into a repository.
     * 
	 * @throws SparqlException 
     */
    public void load() throws SparqlException {
    	load(getLoadFilePath());
    }
    
	private String getBaseUri() {
		// TODO Auto-generated method stub
		return null;
	}

	private String getLoadFilePath() {
		return "/home/aoki/workspace/rdfs";
	}

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
		logger.debug(query);
		
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
	
	@Transactional
	public void delete(String statement) throws SparqlException {
		execute(statement);
	}
	
	@Transactional
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
	@Transactional
	public List<SparqlEntity> query(SelectSparql select) throws SparqlException {
		return query(select.getSparql());
	}

	@Transactional
	public void insert(String insert) throws SparqlException {
		execute(insert);
	}

	@Override
	@Transactional
	public void insert(InsertSparql insert)
			throws SparqlException {
		insert(insert.getSparql());
	}

	@Override
	@Transactional
	public int load(String file) throws SparqlException {
		RepositoryConnection connection = sesameConnectionFactory.getConnection();
		
       	try {
			connection.add(new File(file), getBaseUri(), RDFFormat.RDFXML);
		} catch (RDFParseException | RepositoryException | IOException e) {
			throw new SparqlException(e);
		}
       	return 0;
	}


	@Override
	public void delete(DeleteSparql string) throws SparqlException {
	}

	@Override
	public void execute(InsertSparql string) throws SparqlException {
		// TODO Auto-generated method stub
		
	}
}