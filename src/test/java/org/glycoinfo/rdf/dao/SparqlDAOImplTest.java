package org.glycoinfo.rdf.dao;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.util.List;

import org.glycoinfo.rdf.InsertSparqlBean;
import org.glycoinfo.rdf.SelectSparqlBean;
import org.glycoinfo.rdf.SparqlException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
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
		String query = "SELECT * WHERE { GRAPH ?graph { ?s a ?type } } LIMIT 100";
		try {
			List<SparqlEntity> list = schemaDAO.query(new SelectSparqlBean(query));
			SparqlEntity row = list.get(0);
			logger.debug("Node:>" + row.getValue("s"));
			logger.debug("graph:>" + row.getValue("graph"));
		} catch (Exception e) {
			e.printStackTrace();
			assertFalse("Exception occurred while querying schema.", true);
		}
	}

	@Test
	public void testQuery2() {
		String query = "SELECT distinct ?s WHERE  {[] a ?s}  LIMIT 100";
		try {
			List<SparqlEntity> list = schemaDAO.query(new SelectSparqlBean(query));
			SparqlEntity row = list.get(0);
			logger.debug("Node:>" + row.getValue("s"));
			logger.debug("graph:>" + row.getValue("graph"));
		} catch (Exception e) {
			e.printStackTrace();
			assertFalse("Exception occurred while querying schema.", true);
		}
	}

	@Test
	public void testQueryOptional() {
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
			List<SparqlEntity> list = schemaDAO.query(new SelectSparqlBean(query));
			if (list.size() > 0) {
				SparqlEntity row = list.get(0);
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
				.insert(new InsertSparqlBean("insert into graph <nobutest>  { <aa> <bb> \"cc\" . \n"
								+ "<xx> <yy> <zz> . \n"
								+ "<mm> <nn> \"Some long literal with language\"@en . \n"
								+ "<oo> <pp> \"12345\"^^<http://www.w3.org/2001/XMLSchema#int>\n }"));
		String query = prefix + "SELECT ?s ?v ?o\n" + "from <nobutest>\n"
				+ "WHERE { ?s ?v ?o }";

		try {
			logger.debug("query:>" + query);
			List<SparqlEntity> list = schemaDAO.query(new SelectSparqlBean(query));
			if (list.size() > 0) {
				SparqlEntity row = list.get(0);
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
				+ "?gseq glycan:in_carbohydrate_format ?type} LIMIT 10";

		try {
			logger.debug("query:>" + query);
			List<SparqlEntity> list = schemaDAO.query(new SelectSparqlBean(query));
			if (list.size() > 0) {
				SparqlEntity row = list.get(0);
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
				.insert(new InsertSparqlBean("insert into graph <nobutest>  {"
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
								+ "        glycan:in_carbohydrate_format  glycan:carbohydrate_format_kcf .\n }"));

		String query = "SELECT ?s ?v ?o\n" + "from <nobutest>\n"
				+ "WHERE { ?s ?v ?o }";

		try {
			logger.debug("query:>" + query);
			List<SparqlEntity> list = schemaDAO.query(new SelectSparqlBean(query));
			if (list.size() > 0) {
				SparqlEntity row = list.get(0);
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
	public void testKCFQuery() {
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
			List<SparqlEntity> list = schemaDAO.query(new SelectSparqlBean(query));
			SparqlEntity row = list.get(0);

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
		schemaDAO
				.delete("PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#> "
						+ "DELETE DATA FROM <http://glytoucan.org/rdf/demo/0.8> {"
						+ "<http://www.glycoinfo.org/rdf/glycan/G00021MO/sequence> glycan:has_sequence \"RES\\n1b:x-dglc-HEX-x:x\\n2s:sulfate\\n3s:n-acetyl\\n4b:a-dido-HEX-1:5|6:a\\n5s:sulfate\\n6b:b-dglc-HEX-1:5\\n7s:sulfate\\n8s:n-acetyl\\n9b:a-dido-HEX-1:5|6:a\\n10s:sulfate\\n11s:sulfate\\nLIN\\n1:1o(-1+-1)2n\\n2:1d(2+1)3n\\n3:1o(4+1)4d\\n4:4o(2+-1)5n\\n5:4o(4+1)6d\\n6:6o(-1+-1)7n\\n7:6d(2+1)8n\\n8:6o(4+1)9d\\n9:6o(6+1)10n\\n10:1o(6+1)11n\"^^xsd:string . }");
	}

	@Test
	public void testClearGraph() throws SparqlException {
		schemaDAO
				.execute("clear graph <nobutest>");
	}
	
	@Test
	public void testInsertWurcs() throws SparqlException {
		schemaDAO
				.insert(new InsertSparqlBean("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>"
						+ "		PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>"
						+ "		PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>"
						+ "		PREFIX glytoucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>"
						+ "		INSERT INTO"
						+ "		GRAPH <http://glytoucan.org/rdf/demo/0.3/wurcs>"
						+ "		{ <http://www.glycoinfo.org/rdf/glycan/G36373OJ> glycan:has_glycosequence <http://www.glycoinfo.org/rdf/glycan/G36373OJ/seq> ."
						+ "		<http://www.glycoinfo.org/rdf/glycan/G36373OJ/seq> glycan:has_sequence \"WURCS=2.0/5,4/[12211m-1a_1-5][12211m-1a_1-5][12211m-1a_1-5][12112m-1b_1-5][12122h-1b_1-5]a2-b1_b4-c1_c3-d1_c4-e1\"^^xsd:string ."
								+ "		<http://www.glycoinfo.org/rdf/glycan/G36373OJ/seq> glycan:in_carbohydrate_format glycan:carbohydrate_format_wurcs ."
								+ "		<http://www.glycoinfo.org/rdf/glycan/G36373OJ/seq> glytoucan:is_glycosequence_of <http://www.glycoinfo.org/rdf/glycan/G36373OJ> ."
								+ "		 }"));
	}
}