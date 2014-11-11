/**
 * @author aoki
 */
package org.glycoinfo.ts.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.glycoinfo.ts.utils.TripleStoreProperties;
import org.openrdf.model.Value;
import org.openrdf.query.Binding;
import org.openrdf.query.BindingSet;
import org.openrdf.query.MalformedQueryException;
import org.openrdf.query.QueryEvaluationException;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;










//import virtuoso.jdbc3.VirtuosoExtendedString;
//import virtuoso.jdbc3.VirtuosoRdfBox;
import virtuoso.sesame2.driver.VirtuosoRepository;
import ch.qos.logback.classic.Logger;

/**
 * @author aoki
 */
@Primary
@Repository
//@SuppressWarnings({ "unchecked", "rawtypes" })
//@SpringApplicationConfiguration(classes = TripleStoreProperties.class)
public class SchemaDAOSesameImpl implements SchemaDAO {

	public static Logger logger = (Logger) LoggerFactory
			.getLogger("org.glytoucan.registry.dao.SchemaDAOSesameImpl");

	@Autowired
	TripleStoreProperties datasource;
	
	public static String prefix = "PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#> \n"
			+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"
			+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> \n"
			+ "PREFIX sio: <http://semanticscience.org/resource/> \n"
			+ "PREFIX glytoucan: <http://www.glytoucan.org/glyco/owl/glytoucan#> \n";

//	@ConfigurationProperties(prefix="spring.triplestore")
	public TripleStoreProperties getTripleSource() {
		return datasource;
	}

	public void setTripleSource(TripleStoreProperties datasource) {
		this.datasource = datasource;
	}

	@Override
	public List<SchemaEntity> getAllClass() {
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

	@Override
	public List<SchemaEntity> getDomain(String subject) {
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

	@Override
	public List<SchemaEntity> getRange(String subject) {
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

	@Override
	public List<SchemaEntity> query(String subject) {
		org.openrdf.repository.Repository repository = new VirtuosoRepository(
				datasource.getUrl(), 
				datasource.getUsername(),
				datasource.getPassword());
		RepositoryConnection con = null;
		List<SchemaEntity> al = null;

		try {
			con = repository.getConnection();
			con.setAutoCommit(true);

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
			try {
				al = doTupleQuery(con, query);
			} catch (MalformedQueryException | QueryEvaluationException e) {
				e.printStackTrace();
			}

		} catch (RepositoryException e) {
			logger.debug("Error[" + e + "]");
			e.printStackTrace();
		}

		SchemaEntity spo = new SchemaEntity();

		return al;
	}

	private static List<SchemaEntity> doTupleQuery(RepositoryConnection con,
			String query) throws RepositoryException, MalformedQueryException,
			QueryEvaluationException {
		TupleQuery resultsTable = con.prepareTupleQuery(QueryLanguage.SPARQL,
				query);
		TupleQueryResult bindings = resultsTable.evaluate();

		ArrayList<SchemaEntity> results = new ArrayList<SchemaEntity>();
		for (int row = 0; bindings.hasNext(); row++) {
			System.out.println("RESULT " + (row + 1) + ": ");
			BindingSet pairs = bindings.next();
			List<String> names = bindings.getBindingNames();
			Value[] rv = new Value[names.size()];

			SchemaEntity values = new SchemaEntity();
			values.setColumns(names);
			for (int i = 0; i < names.size(); i++) {
				String name = names.get(i);
				Value value = pairs.getValue(name);
				Binding bind = pairs.getBinding(name);

				rv[i] = value;
				// if(column > 0) System.out.print(", ");
				System.out.println("\t" + name + "=" + value);
				// vars.add(value);
				// if(column + 1 == names.size()) System.out.println(";");
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

	public List<SchemaEntity> insert(String graph, String insert, boolean clear) throws SQLException {
		try {
			Class.forName(datasource.getDriverClassName());
			Connection connection = DriverManager.getConnection(
					datasource.getUrl(), datasource.getUsername(),
					datasource.getPassword());
			java.sql.Statement stmt = connection.createStatement();
			ResultSet rs = null;
			if (clear) {
				stmt.execute("sparql clear graph <" + graph + ">");
				rs = stmt.getResultSet();
				while (rs.next())
					;
			}

			logger.debug("sparql " + prefix + "insert into graph <" + graph + "> " + "{ " + insert + " }");
			stmt.execute("sparql " + prefix + "insert into graph <" + graph + "> " + "{ " + insert + " }");
			rs = stmt.getResultSet();
			while (rs.next())
				logger.debug(rs.toString());

			connection.close();
		} catch (ClassNotFoundException  e) {
			e.printStackTrace();
			logger.debug("class not found, check config" + e.getMessage());
		}
		return null;
	}

	@Override
	public List<SchemaEntity> query(String subject, String baseURI) {
		// TODO Auto-generated method stub
		return null;
	}
}