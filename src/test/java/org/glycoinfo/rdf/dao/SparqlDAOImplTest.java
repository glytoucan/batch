package org.glycoinfo.rdf.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.sql.SQLException;
import java.util.List;

import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlDAOSesameImpl;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.qos.logback.classic.Logger;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SesameDAOTestConfig.class)
public class SparqlDAOImplTest {

	public static Logger logger = (Logger) LoggerFactory
			.getLogger("org.glytoucan.registry.dao.test.SchemaDAOImplTest");

	@Autowired
	SparqlDAO schemaDAO;
 
	public static final String prefix = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> \n"
			+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#> \n"
			+ "PREFIX owl: <http://www.w3.org/2002/07/owl#> \n"
			+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> \n"
			+ "PREFIX dc: <http://purl.org/dc/elements/1.1/> \n"
			+ "PREFIX dcterms: <http://purl.org/dc/terms/> \n"
			+ "PREFIX dbpedia2: <http://dbpedia.org/property/> \n"
			+ "PREFIX dbpedia: <http://dbpedia.org/> \n"
			+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/> \n"
			+ "PREFIX skos: <http://www.w3.org/2004/02/skos/core#> \n"
			+ "PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#> \n"
			+ "PREFIX glytoucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#> \n";

	public static final String from = "from <http://glytoucan.org/rdf/demo/0.2>\n"
			+ "from <http://glytoucan.org/rdf/demo/msdb/7>\n"
			+ "from <http://purl.jp/bio/12/glyco/glycan/ontology/0.18>\n"
			+ "from <http://www.glytoucan.org/glyco/owl/glytoucan>\n";

	public static final String using = "USING <http://glytoucan.org/rdf/demo/0.8>\n"
			+ "USING <http://glytoucan.org/rdf/demo/msdb/8>\n"
			+ "USING <http://purl.jp/bio/12/glyco/glycan/ontology/0.18>\n"
			+ "USING <http://www.glytoucan.org/glyco/owl/glytoucan>\n";

	@Test
	public void testQuery() {
		// http://macpro:8080/glyspace/service/schema/query.json?query=SELECT%20*%20WHERE%20{%20GRAPH%20%3Fgraph%20{%20%3Fs%20a%20%3Chttp%3A%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23Class%3E%20}%20}%20LIMIT%2010

		String query = "SELECT * WHERE { GRAPH ?graph { ?s a ?type } } LIMIT 100";
		try {
			List<SparqlEntity> list = schemaDAO.query(query);
			SparqlEntity row = list.get(0);
			// assertTrue("added glycan with id " + glycan.getGlycanId(), true);
			logger.debug("Node:>" + row.getValue("s"));
			logger.debug("graph:>" + row.getValue("graph"));
		} catch (Exception e) {
			e.printStackTrace();
			assertFalse("Exception occurred while querying schema.", true);
		}
	}

	@Test
	public void testQuery2() {
		// http://macpro:8080/glyspace/service/schema/query.json?query=SELECT%20*%20WHERE%20{%20GRAPH%20%3Fgraph%20{%20%3Fs%20a%20%3Chttp%3A%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23Class%3E%20}%20}%20LIMIT%2010

		String query = "SELECT distinct ?s WHERE  {[] a ?s}  LIMIT 100";
		try {
			List<SparqlEntity> list = schemaDAO.query(query);
			SparqlEntity row = list.get(0);
			// assertTrue("added glycan with id " + glycan.getGlycanId(), true);
			logger.debug("Node:>" + row.getValue("s"));
			logger.debug("graph:>" + row.getValue("graph"));
		} catch (Exception e) {
			e.printStackTrace();
			assertFalse("Exception occurred while querying schema.", true);
		}
	}

	@Test
	public void testQueryOptional() {
		// http://macpro:8080/glyspace/service/schema/query.json?query=SELECT%20*%20WHERE%20{%20GRAPH%20%3Fgraph%20{%20%3Fs%20a%20%3Chttp%3A%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23Class%3E%20}%20}%20LIMIT%2010
		/*
		 * http://macpro:8080/glyspace/service/schema/query.json?query=PREFIX
		 * rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> PREFIX rdfs:
		 * <http://www.w3.org/2000/01/rdf-schema#> PREFIX owl:
		 * <http://www.w3.org/2002/07/owl#> PREFIX xsd:
		 * <http://www.w3.org/2001/XMLSchema#> PREFIX dc:
		 * <http://purl.org/dc/elements/1.1/> PREFIX dcterms:
		 * <http://purl.org/dc/terms/> PREFIX dbpedia2:
		 * <http://dbpedia.org/property/> PREFIX dbpedia: <http://dbpedia.org/>
		 * PREFIX foaf: <http://xmlns.com/foaf/0.1/> PREFIX skos:
		 * <http://www.w3.org/2004/02/skos/core#> PREFIX toucan:
		 * <http://www.glytoucan.org/owl/toucan#> PREFIX glycan:
		 * <http://purl.jp/bio/12/glyco/glycan#>
		 * 
		 * SELECT DISTINCT ?AccessionNumber ?img ?Mass ?Motif ?ID ?Contributor
		 * ?time from <http://glytoucan.org/rdf/demo> from
		 * <http://purl.jp/bio/12/glyco/glycan/ontology/0.18> from
		 * <http://www.glytoucan.org/owl/toucan/0.03> WHERE { ?s a
		 * glycan:saccharide . ?s glytoucan:has_primary_id ?AccessionNumber .
		 * 
		 * ?s glycan:has_image ?img . ?img a glycan:image . ?img dc:format
		 * "image/png"^^xsd:string . ?img glycan:has_symbol_format
		 * glycan:symbol_format_cfg .
		 * 
		 * ?s toucan:has_derivatized_mass ?dmass. ?dmass a
		 * toucan:derivatized_mass . ?dmass toucan:has_derivatization_type
		 * toucan:derivatization_type_none . ?dmass toucan:has_mass ?Mass .
		 * 
		 * OPTIONAL{ ?s glycan:has_motif ?motif . ?motif a glycan:glycan_motif .
		 * ?motif foaf:name ?Motif . ?motif glytoucan:has_primary_id ?ID . }
		 * 
		 * ?s glycan:has_resource_entry ?entry. ?entry a glycan:resource_entry .
		 * ?entry toucan:contributor ?contributor . ?contributor foaf:name
		 * ?Contributor . ?entry toucan:date_registered ?time .
		 * 
		 * 
		 * }
		 */
		String query = prefix
				+ "SELECT DISTINCT  ?AccessionNumber ?img ?Mass ?Motif ?ID ?Contributor ?time\n"
				+ from
				+ "WHERE {\n"
				+ "?s a glycan:saccharide .\n"
				+ "?s glytoucan:has_primary_id ?AccessionNumber .\n"
				+ "?s glycan:has_image ?img .\n"
				+ "?img a glycan:image .\n"
				+ "?img dc:format \"image/png\"^^xsd:string .\n"
				+ "?img glycan:has_symbol_format glycan:symbol_format_cfg .\n"
				+ "?s glytoucan:has_derivatized_mass ?dmass.\n"
				+ "?dmass a glytoucan:derivatized_mass .\n"
				+ "?dmass glytoucan:has_derivatization_type glytoucan:derivatization_type_none .\n"
				+ "?dmass glytoucan:has_mass ?Mass .\n" + "OPTIONAL{\n"
				+ "?s glycan:has_motif ?motif .\n"
				+ "?motif a glycan:glycan_motif .\n"
				+ "?motif foaf:name ?Motif .\n"
				+ "?motif glytoucan:has_primary_id ?ID .\n" + "}\n"
				+ "?s glycan:has_resource_entry ?entry.\n"
				+ "?entry a glycan:resource_entry .\n"
				+ "?entry glytoucan:contributor ?contributor .\n"
				+ "?contributor foaf:name ?Contributor .\n"
				+ "?entry glytoucan:date_registered ?time .\n" + "}";
		try {
			logger.debug("query:>" + query + "<");
			List<SparqlEntity> list = schemaDAO.query(query);
			if (list.size() > 0) {
				SparqlEntity row = list.get(0);
				// assertTrue("added glycan with id " + glycan.getGlycanId(),
				// true);
				logger.debug("Node:>" + row.getValue("s"));
				logger.debug("graph:>" + row.getValue("graph"));
			} else {
				fail();
			}
		} catch (Exception e) {
			e.printStackTrace();
			assertFalse("Exception occurred while querying schema.", true);
		}
	}

	@Test
	public void testInsert() throws SparqlException {
		schemaDAO
				.insert("insert into graph <nobutest>  { <aa> <bb> \"cc\" . \n"
								+ "<xx> <yy> <zz> . \n"
								+ "<mm> <nn> \"Some long literal with language\"@en . \n"
								+ "<oo> <pp> \"12345\"^^<http://www.w3.org/2001/XMLSchema#int>\n }");
		String query = prefix + "SELECT ?s ?v ?o\n" + "from <nobutest>\n"
				+ "WHERE { ?s ?v ?o }";

		try {
			logger.debug("query:>" + query);
			List<SparqlEntity> list = schemaDAO.query(query);
			if (list.size() > 0) {
				SparqlEntity row = list.get(0);
				// assertTrue("added glycan with id " + glycan.getGlycanId(),
				// true);
				logger.debug("s:>" + row.getValue("s"));
				logger.debug("v:>" + row.getValue("v"));
				logger.debug("o:>" + row.getValue("o"));
			} else
				fail();
		} catch (Exception e) {
			e.printStackTrace();
			assertFalse("Exception occurred while querying schema.", true);
		}

	}

	@Test
	public void testConvertQuery() {
		String query = prefix
				+ "SELECT DISTINCT ?s ?AccessionNumber ?Seq ?type\n" + from
				+ "WHERE {" + "?s a glycan:saccharide . "
				+ "?s glytoucan:has_primary_id ?AccessionNumber . "
				+ "?s glycan:has_glycosequence ?gseq . "
				+ "?gseq glycan:has_sequence ?Seq . \n"
				+ "?gseq glycan:in_carbohydrate_format ?type}";

		try {
			logger.debug("query:>" + query);
			List<SparqlEntity> list = schemaDAO.query(query);
			if (list.size() > 0) {
				SparqlEntity row = list.get(0);
				// assertTrue("added glycan with id " + glycan.getGlycanId(),
				// true);
				logger.debug("s:>" + row.getValue("s"));
				logger.debug("Seq:>" + row.getValue("Seq"));
				logger.debug("type:>" + row.getValue("type"));
				logger.debug("AccessionNumber:>"
						+ row.getValue("AccessionNumber"));

			} else
				fail();
		} catch (Exception e) {
			e.printStackTrace();
			assertFalse("Exception occurred while querying schema.", true);
		}
	}

	@Test
	public void testInsertConvert() throws SparqlException {
		schemaDAO
				.insert("insert into graph <nobutest>  {"
						+ "<http://www.glycoinfo.org/rdf/glycan/G63838JW/sequence>"
								+ "        a                              glycan:glycosequence ;\n"
								+ "        glycan:has_sequence            "
								+ "\"ENTRY         CT-1             Glycan"
								+ "NODE  10"
								+ "     1  GlcNAc   0   0"
								+ "     2  GlcNAc   -8   0"
								+ "     3  GlcNAc   -32   4"
								+ "     4  GlcNAc   -32   8"
								+ "     5  Man   -24   6"
								+ "     6  Man   -32   -8"
								+ "     7  Gal   -40   8"
								+ "     8  Man   -16   0"
								+ "     9  Man   -32   -4"
								+ "     10  Man   -24   -6"
								+ "EDGE  9"
								+ "     1  7:b1     4:4"
								+ "     2  10:a1     8:6"
								+ "     3  6:a1     10:3"
								+ "     4  9:a1     10:6"
								+ "     5  2:b1     1:4"
								+ "     6  8:b1     2:4"
								+ "     7  5:a1     8:3"
								+ "     8  3:b1     5:2"
								+ "     9  4:b1     5:4"
								+ "///\""
								+ "^^xsd:string ;\n"
								+ "        glycan:in_carbohydrate_format  glycan:carbohydrate_format_kcf .\n }");

		String query = "SELECT ?s ?v ?o\n" + "from <nobutest>\n"
				+ "WHERE { ?s ?v ?o }";

		try {
			logger.debug("query:>" + query);
			List<SparqlEntity> list = schemaDAO.query(query);
			if (list.size() > 0) {
				SparqlEntity row = list.get(0);
				// assertTrue("added glycan with id " + glycan.getGlycanId(),
				// true);
				logger.debug("s:>" + row.getValue("s"));
				logger.debug("v:>" + row.getValue("v"));
				logger.debug("o:>" + row.getValue("o"));
			} else
				fail();
		} catch (Exception e) {
			e.printStackTrace();
			assertFalse("Exception occurred while querying schema.", true);
		}

	}

//	@Test
//	public void testInsertConvert2() throws SQLException {
//		schemaDAO
//				.insert("nobutest",
//						"<http://glycoinfo.org/rdf/glycan/G72943US> glycan:has_glycosequence <http://glycoinfo.org/rdf/glycan/G72943US/sequence/kcf> .\n"
//								+ "<http://glycoinfo.org/rdf/glycan/G72943US/sequence/kcf> rdfs:label \"G72943US KCF\"^^xsd:string .\n"
//								+ "<http://glycoinfo.org/rdf/glycan/G72943US/sequence/kcf> glycan:has_sequence \"ENTRY         CT-1             Glycan\\n"
//								+ "NODE  7\\n"
//								+ "1  GlcNAc   0   0\\n"
//								+ "2  GlcNAc   -8   0\\n"
//								+ "3  Man   -24   -4\\n"
//								+ "4  Man   -32   2\\n"
//								+ "5  Man   -32   6\\n"
//								+ "6  Man   -24   4\\n"
//								+ "7  Man   -16   0\\n"
//								+ "EDGE  6\\n"
//								+ "1  2:b1     1:4\\n"
//								+ "2  7:b1     2:4\\n"
//								+ "3  3:a1     7:3\\n"
//								+ "4  6:a1     7:6\\n"
//								+ "5  4:a1     6:3\\n"
//								+ "6  5:a1     6:6\\n"
//								+ "///\"^^xsd:string .\n"
//								+ "<http://glycoinfo.org/rdf/glycan/G72943US/sequence/kcf> glycan:in_carbohydrate_format glycan:carbohydrate_format_kcf .\n"
//								+ "<http://glycoinfo.org/rdf/glycan/G72943US/sequence/kcf> glytoucan:is_glycosequence_of <http://glycoinfo.org/rdf/glycan/G72943US> .\n",
//						true);
//
//		String query = "SELECT ?s ?v ?o\n" + "from <nobutest>\n"
//				+ "WHERE { ?s ?v ?o }";
//
//		try {
//			logger.debug("query:>" + query);
//			List<SparqlEntity> list = schemaDAO.query(query);
//			if (list.size() > 0) {
//				SparqlEntity row = list.get(0);
//				// assertTrue("added glycan with id " + glycan.getGlycanId(),
//				// true);
//				logger.debug("s:>" + row.getValue("s"));
//				logger.debug("v:>" + row.getValue("v"));
//				logger.debug("o:>" + row.getValue("o"));
//			} else
//				fail();
//		} catch (Exception e) {
//			e.printStackTrace();
//			assertFalse("Exception occurred while querying schema.", true);
//		}
//
//	}

	@Test
	public void testKCFQuery() {
		// http://macpro:8080/glyspace/service/schema/query.json?query=SELECT%20*%20WHERE%20{%20GRAPH%20%3Fgraph%20{%20%3Fs%20a%20%3Chttp%3A%2F%2Fwww.w3.org%2F2002%2F07%2Fowl%23Class%3E%20}%20}%20LIMIT%2010

		String query = prefix
				+ "SELECT DISTINCT ?s ?name ?AccessionNumber ?Seq\n"
				+ "from <http://glytoucan.org/rdf/demo/0.2>\n"
				+ "from <http://glytoucan.org/rdf/demo/0.2/kcf>\n"
				+ "from <http://glytoucan.org/rdf/demo/msdb/7>\n"
				+ "from <http://purl.jp/bio/12/glyco/glycan/ontology/0.18>\n"
				+ "from <http://www.glytoucan.org/glyco/owl/glytoucan>\n"
				+ " WHERE { ?s a glycan:glycan_motif .\n"
				+ "?s foaf:name ?name .\n"
				+ "        ?s glytoucan:has_primary_id ?AccessionNumber .\n"
				+ "       ?s glycan:has_glycosequence ?gseq .\n"
				+ "        ?gseq glycan:has_sequence ?Seq .\n"
				+ "        ?gseq glycan:in_carbohydrate_format glycan:carbohydrate_format_kcf }\n"
				+ "order by ?AccessionNumber";
		try {
			List<SparqlEntity> list = schemaDAO.query(query);
			SparqlEntity row = list.get(0);
			// assertTrue("added glycan with id " + glycan.getGlycanId(), true);
			logger.debug("Node:>" + row.getValue("s"));
			logger.debug("graph:>" + row.getValue("name"));
			logger.debug("Seq:>" + row.getValue("Seq"));
		} catch (Exception e) {
			e.printStackTrace();
			assertFalse("Exception occurred while querying schema.", true);
		}
	}

	@Test
	public void testDelete() throws SparqlException {
		// schemaDAO
		// .delete("PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#> "
		// + "DELETE DATA FROM <http://glytoucan.org/rdf/demo/0.7> "
		// +
		// "<http://www.glycoinfo.org/rdf/glycan/G00021MO/sequence> glycan:has_sequence \"RES\n1b:x-dglc-HEX-x:x\n2s:sulfate\n3s:n-acetyl\n4b:a-dido-HEX-1:5|6:a\n5s:sulfate\n6b:b-dglc-HEX-1:5\n7s:sulfate\n8s:n-acetyl\n9b:a-dido-HEX-1:5|6:a\n10s:sulfate\n11s:sulfate\nLIN\n1:1o(-1+-1)2n\n2:1d(2+1)3n\n3:1o(4+1)4d\n4:4o(2+-1)5n\n5:4o(4+1)6d\n6:6o(-1+-1)7n\n7:6d(2+1)8n\n8:6o(4+1)9d\n9:6o(6+1)10n\n10:1o(6+1)11n\"^^xsd:string . }");
		schemaDAO
				.delete("PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#> "
						+ "DELETE DATA FROM <http://glytoucan.org/rdf/demo/0.8> {"
						+ "<http://www.glycoinfo.org/rdf/glycan/G00021MO/sequence> glycan:has_sequence \"RES\\n1b:x-dglc-HEX-x:x\\n2s:sulfate\\n3s:n-acetyl\\n4b:a-dido-HEX-1:5|6:a\\n5s:sulfate\\n6b:b-dglc-HEX-1:5\\n7s:sulfate\\n8s:n-acetyl\\n9b:a-dido-HEX-1:5|6:a\\n10s:sulfate\\n11s:sulfate\\nLIN\\n1:1o(-1+-1)2n\\n2:1d(2+1)3n\\n3:1o(4+1)4d\\n4:4o(2+-1)5n\\n5:4o(4+1)6d\\n6:6o(-1+-1)7n\\n7:6d(2+1)8n\\n8:6o(4+1)9d\\n9:6o(6+1)10n\\n10:1o(6+1)11n\"^^xsd:string . }");

		// String query = prefix + "SELECT ?s ?v ?o\n"
		// + "from <gr-test>\n"
		// + "WHERE { ?s ?v ?o }";
		//
		// try {
		// logger.debug("query:>" + query);
		// List<SchemaEntity> list = schemaDAO.query(query);
		// if (list.size() > 0) {
		// SchemaEntity row = list.get(0);
		// // assertTrue("added glycan with id " + glycan.getGlycanId(),
		// // true);
		// logger.debug("s:>" + row.getValue("s"));
		// logger.debug("v:>" + row.getValue("v"));
		// logger.debug("o:>" + row.getValue("o"));
		// } else
		// fail();
		// } catch (Exception e) {
		// e.printStackTrace();
		// assertFalse("Exception occurred while querying schema.", true);
		// }

	}

	@Test
	public void testClearGraph() throws SparqlException {
		schemaDAO
				.execute("clear graph <nobutest>");
		//sparql clear graph <http://glytoucan.org/rdf/demo/0.2/wurcs>
	}

}