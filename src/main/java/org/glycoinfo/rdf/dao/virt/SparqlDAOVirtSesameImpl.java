/**
 * @author aoki
 */
package org.glycoinfo.rdf.dao.virt;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.rdf.DeleteSparql;
import org.glycoinfo.rdf.InsertSparql;
import org.glycoinfo.rdf.SparqlBean;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.dao.SparqlEntityFactory;
import org.glycoinfo.rdf.utils.TripleStoreProperties;
import org.openrdf.model.Resource;
import org.openrdf.model.Value;
import org.openrdf.model.ValueFactory;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
//import virtuoso.jdbc3.VirtuosoExtendedString;
//import virtuoso.jdbc3.VirtuosoRdfBox;

/**
 * @author aoki
 */
@Repository
@Scope(value="request", proxyMode=ScopedProxyMode.TARGET_CLASS)
//@SuppressWarnings({ "unchecked", "rawtypes" })
public class SparqlDAOVirtSesameImpl implements SparqlDAO {
	
	public static Log logger = (Log) LogFactory
			.getLog("org.glytoucan.registry.dao.SparqlDAOVirtSesameImpl");

	@Autowired(required=false)
	TripleStoreProperties datasource;
	
//	@Autowired
//	SparqlEntityFactory sparqlEntityFactory;
	
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

//	@Transactional
//	public List<SparqlEntity> insert(String graph, String insert) throws SparqlException {
//		return insert(QueryLanguage.SPARQL.toString(), graph, insert);
//	}
	
//	@Transactional
//	public List<SparqlEntity> insert(String format, String graph, String insert) throws SparqlException {
//			RepositoryConnection connection = sesameConnectionFactory.getConnection();
//
//			if (format.equals(QueryLanguage.SPARQL)) {
//				String insertstatement = prefix + "insert into graph <" + graph + "> " + "{ " + insert + " }"; 
//				logger.debug(insertstatement);
//				Update update;
//				try {
//					update = connection.prepareUpdate(QueryLanguage.SPARQL, insertstatement);
//				} catch (RepositoryException | MalformedQueryException e) {
//					e.printStackTrace();
//					throw new SparqlException(e);
//				}
//			}
//			return null;
//	}

	@Override
	@Transactional
	public void delete(SparqlBean deletesparql) throws SparqlException {
		if (!(deletesparql instanceof DeleteSparql))
			throw new SparqlException("expected delete SPARQL");
		DeleteSparql delete = (DeleteSparql)deletesparql;
		SparqlEntity se = SparqlEntityFactory.getSparqlEntity();
		if (null != se) {
			delete.setSparqlEntity(se);
			SparqlEntityFactory.unset();
		}
		
		RepositoryConnection connection = sesameConnectionFactory.getConnection();

		String format = delete.getFormat();
		String statement = delete.getSparql();
		logger.debug("format:>"+format);
		logger.debug(statement);
		
		try {
			if (format.equals(InsertSparql.SPARQL)) {
				Update update;
				update = connection.prepareUpdate(QueryLanguage.SPARQL, statement);
				update.execute();
				BindingSet bindings = update.getBindings();
				ArrayList<SparqlEntity> results = new ArrayList<SparqlEntity>();
				bindings.iterator();
				for (Binding binding : bindings) {
					logger.debug("binding Name:>" + binding.getName());
					logger.debug("binding Value:>" + binding.getValue());
				}
			} else if (format.equals(InsertSparql.Turtle)) {
				StringReader reader = new StringReader(statement);
				ValueFactory f = connection.getValueFactory();
				Resource res = f.createURI(delete.getGraph());
				connection.add(reader, "", RDFFormat.TURTLE, res);
			}
		} catch (RepositoryException | MalformedQueryException | UpdateExecutionException | RDFParseException | IOException e) {
			e.printStackTrace();
			throw new SparqlException(e);
		}
	}
	
	@Override
	@Transactional
	public void execute(SparqlBean insert) throws SparqlException {
		if (!(insert instanceof InsertSparql))
			throw new SparqlException("expected Insert SPARQL");
		InsertSparql thisInsert = (InsertSparql)insert;
		SparqlEntity se = SparqlEntityFactory.getSparqlEntity();
		if (null != se) {
			thisInsert.setSparqlEntity(se);
			SparqlEntityFactory.unset();
		}
		
		logger.debug(thisInsert);

		RepositoryConnection connection = sesameConnectionFactory.getConnection();

		String format = thisInsert.getFormat();
		String statement = thisInsert.getSparql();
		logger.debug("format:>"+format);
		logger.debug(statement);
		
		try {
			if (format.equals(InsertSparql.SPARQL)) {
				Update update;
				update = connection.prepareUpdate(QueryLanguage.SPARQL, statement);
				update.execute();
				BindingSet bindings = update.getBindings();
				ArrayList<SparqlEntity> results = new ArrayList<SparqlEntity>();
				bindings.iterator();
				for (Binding binding : bindings) {
					logger.debug("binding Name:>" + binding.getName());
					logger.debug("binding Value:>" + binding.getValue());
				}
			} else if (format.equals(InsertSparql.Turtle)) {
				StringReader reader = new StringReader(statement);
				ValueFactory f = connection.getValueFactory();
				Resource res = f.createURI(thisInsert.getGraph());
				connection.add(reader, "", RDFFormat.TURTLE, res);
			}
		} catch (RepositoryException | MalformedQueryException | UpdateExecutionException | RDFParseException | IOException e) {
			e.printStackTrace();
			throw new SparqlException(e);
		}
	}

	public void setTripleStoreProperties(TripleStoreProperties ts)
			throws SparqlException {
	}

	@Override
	@Transactional
	public List<SparqlEntity> query(SparqlBean select) throws SparqlException {
		SparqlEntity se = SparqlEntityFactory.getSparqlEntity();
		if (null != se) {
			select.setSparqlEntity(se);
			SparqlEntityFactory.unset();
		}
		
//		logger.debug("sparqlEntityFactory:>" + sparqlEntityFactory + "<");
//		logger.debug("SparqlEntity:>" + se + "<");
		
		return query(select.getSparql());
	}

	@Override
	@Transactional
	public void insert(SparqlBean insert)
			throws SparqlException {
		execute(insert);
	}

	@Override
	public int load(String file) throws SparqlException {
		// TODO Auto-generated method stub
		return 0;
	}

	/*
	 * @see org.glycoinfo.rdf.dao.SparqlDAO#archive(org.glycoinfo.rdf.Sparql)
	 */
	@Override
	public void archive(SparqlBean something) throws SparqlException {
		
		
		
	}
}