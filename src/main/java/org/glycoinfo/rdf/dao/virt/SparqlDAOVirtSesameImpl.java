/**
 * @author aoki
 */
package org.glycoinfo.rdf.dao.virt;

import java.util.ArrayList;
import java.util.List;

import org.glycoinfo.rdf.InsertSparql;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.utils.TripleStoreProperties;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
//import virtuoso.jdbc3.VirtuosoExtendedString;
//import virtuoso.jdbc3.VirtuosoRdfBox;

/**
 * @author aoki
 */
@Repository
//@SuppressWarnings({ "unchecked", "rawtypes" })
public class SparqlDAOVirtSesameImpl implements SparqlDAO {

	public static Logger logger = (Logger) LoggerFactory
			.getLogger("org.glytoucan.registry.dao.SparqlDAOVirtSesameImpl");

	@Autowired(required=false)
	TripleStoreProperties datasource;
	
	public static String prefix = "PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#> \n"
			+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"
			+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> \n"
			+ "PREFIX sio: <http://semanticscience.org/resource/> \n"
			+ "PREFIX glytoucan: <http://www.glytoucan.org/glyco/owl/glytoucan#> \n";

	@Autowired(required=false)
	protected VirtSesameConnectionFactory sesameConnectionFactory;

	
//	@ConfigurationProperties(prefix="spring.triplestore")
	public TripleStoreProperties getTripleStoreProperties() {
		return datasource;
	}

	public void setTripleSource(TripleStoreProperties datasource) {
		this.datasource = datasource;
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

	@Transactional
	public List<SparqlEntity> query(String subject) throws SparqlException {

		List<SparqlEntity> al = null;

		RepositoryConnection con = sesameConnectionFactory.getConnection();

		
		try {
//			con = repository.getConnection();
//			con.setAutoCommit(true);

			// // test ask query
			// String ask = "ask { ?s <http://mso.monrai.com/foaf/name> ?o }";
			// doQuery(con, ask);

			// test add data to the repository
			String query = null;

			// test query data
			// query = "SELECT distinct ?s WHERE {[] a ?s} LIMIT 100";
			query = subject;
			// try {
			// log("Loading data from URL: " + strurl);
			// con.add(url, "", RDFFormat.RDFXML, context);
			logger.debug("query:>" + query);
			try {
				al = doTupleQuery(con, query);
			} catch (MalformedQueryException | QueryEvaluationException e) {
				e.printStackTrace();
				throw new SparqlException(e);
			}

		} catch (RepositoryException e) {
			logger.debug("Error[" + e + "]");
			e.printStackTrace();
			throw new SparqlException(e);
		}

		SparqlEntity spo = new SparqlEntity();

		return al;
	}

	private static List<SparqlEntity> doTupleQuery(RepositoryConnection con,
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
	public List<SparqlEntity> insert(String graph, String insert) throws SparqlException {
//			Class.forName(datasource.getDriverClassName());
//			Connection connection = DriverManager.getConnection(
//					datasource.getUrl(), datasource.getUsername(),
//					datasource.getPassword());
			RepositoryConnection connection = sesameConnectionFactory.getConnection();

//			TupleQuery resultsTable = connection.prepareTupleQuery(QueryLanguage.SPARQL,
//					query);
			String insertstatement = prefix + "insert into graph <" + graph + "> " + "{ " + insert + " }"; 
			logger.debug(insertstatement);
			Update update;
			try {
				update = connection.prepareUpdate(QueryLanguage.SPARQL, insertstatement);
			} catch (RepositoryException | MalformedQueryException e) {
				e.printStackTrace();
				throw new SparqlException(e);
			}
			return null;
	}

	@Transactional
	public void delete(String statement) throws SparqlException {
		execute(statement);
	}
	
	@Transactional
	public void execute(String statement) throws SparqlException {
		RepositoryConnection connection = sesameConnectionFactory.getConnection();

		logger.debug(statement);
		Update update;
		try {
			update = connection.prepareUpdate(QueryLanguage.SPARQL, statement);
			update.execute();
			BindingSet bindings = update.getBindings();
			ArrayList<SparqlEntity> results = new ArrayList<SparqlEntity>();
			bindings.iterator();
			for (Binding binding : bindings) {
				logger.debug("binding Name:>" + binding.getName());
				logger.debug("binding Value:>" + binding.getValue());
			}
		} catch (RepositoryException | MalformedQueryException | UpdateExecutionException e) {
			e.printStackTrace();
			throw new SparqlException(e);
		}
	}

	public void setTripleStoreProperties(TripleStoreProperties ts)
			throws SparqlException {
	}

	@Override
	@Transactional
	public List<SparqlEntity> query(SelectSparql select) throws SparqlException {
		return query(select.getSparql());
	}

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
	public int load(String file) throws SparqlException {
		// TODO Auto-generated method stub
		return 0;
	}
}