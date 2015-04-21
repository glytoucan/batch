///**
// * @author aoki
// */
//package org.glycoinfo.rdf.dao;
//
//
//import java.io.BufferedInputStream;
//import java.io.File;
//import java.io.FileInputStream;
//import java.io.IOException;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.Reader;
//import java.sql.Connection;
//import java.sql.DriverManager;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.LinkedHashSet;
//import java.util.List;
//import java.util.Properties;
//import java.util.Set;
//
//import org.glycoinfo.rdf.InsertSparql;
//import org.glycoinfo.rdf.SelectSparql;
//import org.glycoinfo.rdf.SparqlException;
//import org.glycoinfo.rdf.utils.TripleStoreProperties;
//import org.openrdf.model.Statement;
//import org.openrdf.model.URI;
//import org.openrdf.model.Value;
//import org.openrdf.model.impl.LiteralImpl;
//import org.openrdf.model.impl.URIImpl;
//import org.openrdf.model.vocabulary.RDFS;
//import org.openrdf.query.Binding;
//import org.openrdf.query.BindingSet;
//import org.openrdf.query.GraphQuery;
//import org.openrdf.query.MalformedQueryException;
//import org.openrdf.query.QueryEvaluationException;
//import org.openrdf.query.QueryLanguage;
//import org.openrdf.query.TupleQuery;
//import org.openrdf.query.TupleQueryResult;
//import org.openrdf.repository.Repository;
//import org.openrdf.repository.RepositoryConnection;
//import org.openrdf.repository.RepositoryException;
//import org.openrdf.rio.RDFFormat;
//import org.openrdf.rio.helpers.StatementCollector;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
//
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
//
//
//
//
//
//
//
//
//
//
////import virtuoso.jdbc3.VirtuosoExtendedString;
////import virtuoso.jdbc3.VirtuosoRdfBox;
//import virtuoso.sesame2.driver.VirtuosoRepository;
//
///**
// * @author aoki
// */
////@SuppressWarnings({ "unchecked", "rawtypes" })
//public class SparqlDAOBigdataImpl implements SparqlDAO {
//
//	public static Logger logger = (Logger) LoggerFactory
//			.getLogger("org.glytoucan.registry.dao.SparqlDAOBigdataImpl");
//
//	@Autowired
//	TripleStoreProperties datasource;
//	
//	public static org.openrdf.repository.Repository repo;
//	
//	public static String prefix = "PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#> \n"
//			+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"
//			+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> \n"
//			+ "PREFIX sio: <http://semanticscience.org/resource/> \n"
//			+ "PREFIX glytoucan: <http://www.glytoucan.org/glyco/owl/glytoucan#> \n";
//	
//	public SparqlDAOBigdataImpl() {
//		init();
//	}
//	
//	public void init() {
//		if (null == repo) {
//			repo = new BigdataSailRemoteRepository("http://localhost:8081/bigdata", false);
//			try {
////				loadSomeDataFromADocument();
//			} catch (Exception e) {
//				e.printStackTrace();
//				return;
//			}
//		}
//		
//	}
//
////	@ConfigurationProperties(prefix="spring.triplestore")
//	public TripleStoreProperties getTripleStoreProperties() {
//		return datasource;
//	}
//
//	public void setTripleSource(TripleStoreProperties datasource) {
//		this.datasource = datasource;
//	}
//	
//	String loadFilePath, baseUrl; 
//	
//	public String getLoadFilePath() {
//		return loadFilePath;
//	}
//
//	public void setLoadFilePath(String loadFilePath) {
//		this.loadFilePath = loadFilePath;
//	}
//
//	public String getBaseUrl() {
//		return baseUrl;
//	}
//
//	public void setBaseUrl(String baseUrl) {
//		this.baseUrl = baseUrl;
//	}
//	
//	
//
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
//    
//	public List<SparqlEntity> getAllClass() {
//		// Query sparql =
//		// QueryFactory.create("SELECT * WHERE { GRAPH ?graph { ?s a <http://www.w3.org/2002/07/owl#Class> } } limit 100");
//		// VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create
//		// (sparql, set);
//		// ResultSet results = vqe.execSelect();
//		ArrayList al = new ArrayList();
//		// while (results.hasNext()) {
//		// QuerySolution result = results.nextSolution();
//		// SchemaEntity
//		//
//		// al.add(s);
//		// logger.debug( " { " + s + " . }");
//		// }
//		return al;
//	}
//	
//	public List<SparqlEntity> getDomain(String subject) {
//		// Query sparql =
//		// QueryFactory.create("SELECT * WHERE  { ?s ?p ?o } limit 100");
//		// VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create
//		// (sparql, set);
//		// ResultSet results = vqe.execSelect();
//		ArrayList al = new ArrayList();
//		// while (results.hasNext()) {
//		// QuerySolution result = results.nextSolution();
//		// // RDFNode graph = result.get("graph");
//		// SchemaEntity s = ;
//		// al.add(s);
//		// logger.debug( " { " + s + " " + p + " " + o + " . }");
//		// }
//		return al;
//	}
//
//	public List<SparqlEntity> getRange(String subject) {
//		// Query sparql =
//		// QueryFactory.create("SELECT * WHERE  { ?s ?p ?o } limit 100");
//		// VirtuosoQueryExecution vqe = VirtuosoQueryExecutionFactory.create
//		// (sparql, set);
//		// ResultSet results = vqe.execSelect();
//		ArrayList al = new ArrayList();
//		// while (results.hasNext()) {
//		// QuerySolution result = results.nextSolution();
//		// // RDFNode graph = result.get("graph");
//		// RDFNode s = result.get("s");
//		// RDFNode p = result.get("p");
//		// RDFNode o = result.get("o");
//		//
//		// al.add(s);
//		// logger.debug( " { " + s + " " + p + " " + o + " . }");
//		// }
//		return al;
//	}
//
//	public List<SparqlEntity> query(String query) throws SparqlException {
//		
////        final String propertiesFile = "/quads.properties";
////
////        logger.info("Reading properties from file: " + propertiesFile);
////        final Properties properties = new Properties();
////        InputStream is = getClass().getResourceAsStream(propertiesFile);
////        try {
////			properties.load(new InputStreamReader(new BufferedInputStream(is)));
////		} catch (IOException e1) {
////			// TODO Auto-generated catch block
////			e1.printStackTrace();
////		}
////        
////		BigdataSail sail = new BigdataSail(properties);
////		repo = new BigdataSailRepository(sail );
//
////        if (properties.getProperty(com.bigdata.journal.Options.FILE) == null) {
////            /*
////             * Create a backing file iff none was specified in the
////             * properties file.
////             */
////            File journal = null;
////			try {
////				journal = File.createTempFile("bigdata", ".jnl");
////			} catch (IOException e) {
////				// TODO Auto-generated catch block
////				e.printStackTrace();
////			}
////            logger.info(journal.getAbsolutePath());
////            properties.setProperty(BigdataSail.Options.FILE, journal
////                    .getAbsolutePath());
////        }
////        if (properties
////                .getProperty(AbstractTransactionService.Options.MIN_RELEASE_AGE) == null) {
////            // Retain old commit points for at least 60s.
////            properties.setProperty(
////                    AbstractTransactionService.Options.MIN_RELEASE_AGE,
////                    "60000");
////        }
//
//        try {
//        
////            repo.initialize();
//            // demonstrate some basic functionality
////            final URI MIKE = new URIImpl(
////                    "http://www.bigdata.com/rdf#Mike");
////            sampleCode.loadSomeData(repo);
////            System.out.println("Loaded sample data.");
////            sampleCode.readSomeData(repo, MIKE);
////            sampleCode.executeSelectQuery(repo,
////                    "select ?p ?o where { <" + MIKE.toString()
////                            + "> ?p ?o . }", QueryLanguage.SPARQL);
////            System.out.println("Did SELECT query.");
////            sampleCode.executeConstructQuery(repo,
////                    "construct { <" + MIKE.toString()
////                            + "> ?p ?o . } where { <" + MIKE.toString()
////                            + "> ?p ?o . }", QueryLanguage.SPARQL);
////            System.out.println("Did CONSTRUCT query.");
////            executeFreeTextQuery(repo);
//            
////            executeSelectQuery(repo, query, QueryLanguage.SPARQL);
//            // run one of the LUBM tests
//            // sampleCode.doU10(); // I see loaded: 1752215 in 116563 millis: 15032 stmts/sec, what do you see?
//            // sampleCode.doU1();
//            List<SparqlEntity> al = null;
//            RepositoryConnection con;
//            if (repo instanceof BigdataSailRepository) { 
//                con = ((BigdataSailRepository) repo).getReadOnlyConnection();
//            } else {
//                con = repo.getConnection();
//            }
//    			// try {
//    			// log("Loading data from URL: " + strurl);
//    			// con.add(url, "", RDFFormat.RDFXML, context);
//    			try {
//    				al = doTupleQuery(con, query);
//    			} catch (MalformedQueryException | QueryEvaluationException e) {
//    				e.printStackTrace();
//    				throw new SparqlException(e);
//    			}
//    			return al;
//        
//    } catch (Exception ex) {
//        ex.printStackTrace();
//    } finally {
//        try {
//			repo.shutDown();
//		} catch (RepositoryException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//    }
//
//return null;
//
//	}
//
//	public static List<SparqlEntity> doTupleQuery(RepositoryConnection con,
//			String query) throws RepositoryException, MalformedQueryException,
//			QueryEvaluationException {
//		TupleQuery resultsTable = con.prepareTupleQuery(QueryLanguage.SPARQL,
//				query);
//
//		TupleQueryResult bindings = resultsTable.evaluate();
//		
//		ArrayList<SparqlEntity> results = new ArrayList<SparqlEntity>();
//		for (int row = 0; bindings.hasNext(); row++) {
//			logger.debug("RESULT " + (row + 1) + ": ");
//			BindingSet pairs = bindings.next();
//			List<String> names = bindings.getBindingNames();
//			Value[] rv = new Value[names.size()];
//
//			SparqlEntity values = new SparqlEntity();
//			values.setColumns(names);
//			for (int i = 0; i < names.size(); i++) {
//				String name = names.get(i);
//				Value value = pairs.getValue(name);
//				Binding bind = pairs.getBinding(name);
//
//				rv[i] = value;
//				// if(column > 0) System.out.print(", ");
//				logger.debug("\t" + name + "=" + value);
//				// vars.add(value);
//				// if(column + 1 == names.size()) logger.debug(";");
//				String stringvalue = null;
//				if (null == value)
//					stringvalue = "";
//				else
//					stringvalue = value.stringValue();
//
//				values.setValue(name, stringvalue);
//			}
//			results.add(values);
//		}
//		return results;
//	}
//
//	public List<SparqlEntity> insert(String graph, String insert, boolean clear) throws SQLException {
//		try {
//			Class.forName(datasource.getDriverClassName());
//			Connection connection = DriverManager.getConnection(
//					datasource.getUrl(), datasource.getUsername(),
//					datasource.getPassword());
//			java.sql.Statement stmt = connection.createStatement();
//			ResultSet rs = null;
//			if (clear) {
//				stmt.execute("sparql clear graph <" + graph + ">");
//				rs = stmt.getResultSet();
//				while (rs.next())
//					;
//			}
//
//			logger.debug("sparql " + prefix + "insert into graph <" + graph + "> " + "{ " + insert + " }");
//			stmt.execute("sparql " + prefix + "insert into graph <" + graph + "> " + "{ " + insert + " }");
//			rs = stmt.getResultSet();
//			while (rs.next())
//				logger.debug(rs.toString());
//
//			connection.close();
//		} catch (ClassNotFoundException  e) {
//			e.printStackTrace();
//			logger.debug("class not found, check config" + e.getMessage());
//		}
//		return null;
//	}
//
//	public void delete(String statement) throws SparqlException {
//		execute(statement);
//	}
//	
//	public void execute(String statement) throws SparqlException {
//		try {
//			Class.forName(datasource.getDriverClassName());
//			Connection connection = DriverManager.getConnection(
//					datasource.getUrl(), datasource.getUsername(),
//					datasource.getPassword());
//			java.sql.Statement stmt = connection.createStatement();
//			ResultSet rs = null;
//
//			logger.debug("sparql " + statement);
//			stmt.execute("sparql " + statement);
//			rs = stmt.getResultSet();
//			while (rs.next())
//				logger.debug(rs.toString());
//
//			connection.close();
//		} catch (ClassNotFoundException  e) {
//			logger.debug("class not found, check config" + e.getMessage());
//			throw new SparqlException(e);
//		} catch (SQLException e) {
//			throw new SparqlException(e);
//		}
//	}
//
//	@Override
//	public void setTripleStoreProperties(TripleStoreProperties ts)
//			throws SparqlException {
//	}
//
//	@Override
//	public List<SparqlEntity> query(SelectSparql select) throws SparqlException {
//		return query(select.getSparql());
//	}
//
//	public void insert(String insert) throws SparqlException {
//		execute(insert);
//	}
//
//	@Override
//	public void insert(InsertSparql insert)
//			throws SparqlException {
//		insert(insert.getSparql());
//	}
//	
//	
////    /**
////     * Execute a "construct" query.
////     * 
////     * @param repo
////     * @param query
////     * @param ql
////     * @throws Exception
////     */
////    public void executeConstructQuery(Repository repo, String query, 
////        QueryLanguage ql) throws Exception {
////        
////        /*
////         * With MVCC, you read from a historical state to avoid blocking and
////         * being blocked by writers.  BigdataSailRepository.getQueryConnection
////         * gives you a view of the repository at the last commit point.
////         */
////        RepositoryConnection cxn;
////        if (repo instanceof BigdataSailRepository) { 
////            cxn = ((BigdataSailRepository) repo).getReadOnlyConnection();
////        }
//////        else {
//////            cxn = repo.getConnection();
//////        }
////        
////        try {
////
////            // silly construct queries, can't guarantee distinct results
////            final Set<Statement> results = new LinkedHashSet<Statement>();
////            final GraphQuery graphQuery = cxn.prepareGraphQuery(ql, query);
////            graphQuery.setIncludeInferred(true /* includeInferred */);
////            graphQuery.evaluate(new StatementCollector(results));
////            // do something with the results
////            for (Statement stmt : results) {
////                log.info(stmt);
////            }
////
////        } finally {
////            // close the repository connection
////            cxn.close();
////        }
////        
////    }
//
//    public boolean executeFreeTextQuery(Repository repo) throws Exception {
//        if (((BigdataSailRepository) repo).getDatabase().getLexiconRelation()
//                .getSearchEngine() == null) {
//            /*
//             * Only if the free text index exists.
//             */
//            return false;
//        }
//        RepositoryConnection cxn = repo.getConnection();
//        cxn.setAutoCommit(false);
//        try {
//            cxn.add(new URIImpl("http://www.bigdata.com/A"), RDFS.LABEL,
//                    new LiteralImpl("Yellow Rose"));
//            cxn.add(new URIImpl("http://www.bigdata.com/B"), RDFS.LABEL,
//                    new LiteralImpl("Red Rose"));
//            cxn.add(new URIImpl("http://www.bigdata.com/C"), RDFS.LABEL,
//                    new LiteralImpl("Old Yellow House"));
//            cxn.add(new URIImpl("http://www.bigdata.com/D"), RDFS.LABEL,
//                    new LiteralImpl("Loud Yell"));
//            cxn.commit();
//        } catch (Exception ex) {
//            cxn.rollback();
//            throw ex;
//        } finally {
//            // close the repository connection
//            cxn.close();
//        }
//        
//        String query = "select ?x where { ?x <"+BDS.SEARCH+"> \"Yell\" . }";
//        executeSelectQuery(repo, query, QueryLanguage.SPARQL);
//        // will match A, C, and D
//        return true;
//    }
//    
//    /**
//     * Execute a "select" query.
//     * 
//     * @param repo
//     * @param query
//     * @param ql
//     * @throws Exception
//     */
//    public void executeSelectQuery(Repository repo, String query, 
//        QueryLanguage ql) throws Exception {
//        
//        /*
//         * With MVCC, you read from a historical state to avoid blocking and
//         * being blocked by writers.  BigdataSailRepository.getQueryConnection
//         * gives you a view of the repository at the last commit point.
//         */
//        RepositoryConnection cxn;
//        if (repo instanceof BigdataSailRepository) { 
//            cxn = ((BigdataSailRepository) repo).getReadOnlyConnection();
//        } else {
//            cxn = repo.getConnection();
//        }
//        
//        try {
//
//            final TupleQuery tupleQuery = cxn.prepareTupleQuery(ql, query);
//            tupleQuery.setIncludeInferred(true /* includeInferred */);
//            TupleQueryResult result = tupleQuery.evaluate();
//            // do something with the results
//            while (result.hasNext()) {
//                BindingSet bindingSet = result.next();
//                logger.info(bindingSet.toString());
//            }
//            
//        } finally {
//            // close the repository connection
//            cxn.close();
//        }
//        
//    }
//}