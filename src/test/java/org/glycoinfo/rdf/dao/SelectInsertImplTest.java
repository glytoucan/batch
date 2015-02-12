package org.glycoinfo.rdf.dao;

import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.ConfigFileApplicationContextInitializer;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.qos.logback.classic.Logger;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SesameDAOTestConfig.class)
public class SelectInsertImplTest {

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
			+ "from <http://glytoucan.org/rdf/demo/0.2/motif>\n"
			+ "from <http://glytoucan.org/rdf/demo/msdb/7>\n"
			+ "from <http://purl.jp/bio/12/glyco/glycan/ontology/0.18>\n"
			+ "from <http://www.glytoucan.org/glyco/owl/glytoucan>\n";

	public static final String using = "USING <http://glytoucan.org/rdf/demo/0.2>\n"
			+ "USING <http://glytoucan.org/rdf/demo/msdb/8>\n"
			+ "USING <http://purl.jp/bio/12/glyco/glycan/ontology/0.18>\n"
			+ "USING <http://www.glytoucan.org/glyco/owl/glytoucan>\n";
	
	public static final String graph = "http://www.glytoucan.org/rdf/construction";

	@Test
	public void testInsertSelectGraphconstruction() throws SparqlException {
		schemaDAO.delete(prefix + "\n" + "INSERT\n"
				+ "{ GRAPH <"+graph+"> {\n"
				+ "  ?s a glycan:saccharide .\n"
				+ "  ?s glytoucan:has_primary_id ?AccessionNumber .\n"
				+ "  }\n" + "}\n" + using + "WHERE {\n"
				+ "  ?s a glycan:saccharide.\n"
				+ "  ?s glytoucan:has_primary_id ?AccessionNumber . " + "}");
	}

	@Test
	public void testInsertSelectGraphMass() throws SparqlException {
		schemaDAO.delete(prefix + "\n" + "INSERT\n"
				+ "{ GRAPH <"+graph+"> {\n"
				+ "?s glytoucan:has_mass ?Mass ." + "}}\n" + using + "WHERE {\n"
				+ "?s glytoucan:has_derivatized_mass ?dmass .\n"
				+ "				    ?dmass a ?dmassType ;\n"
				+ "				           glytoucan:has_mass ?Mass .\n" + "				}\n"

		);
	}

	@Test
	public void testInsertSelectGraphGlycoCT() throws SparqlException {
		schemaDAO.delete(prefix + "\n" + "INSERT\n"
				+ "{ GRAPH <"+graph+"> {\n"
				+ "	  ?s glycan:has_sequence ?Seq .\n" + "					}}\n" + using
				+ "WHERE {\n" + "	  ?s glycan:has_glycosequence ?gseq .\n"
				+ "   ?gseq glycan:has_sequence ?Seq .\n" + "				}\n");
	}

	@Test
	public void testInsertSelectGraphContributor() throws SparqlException {
		schemaDAO
				.execute(prefix
						+ "\n"
						+ "INSERT\n"
						+ "{ GRAPH <http://www.glytoucan.org/rdf/construction> {\n"
						+ "					    ?s glytoucan:date_registered ?ContributionTime ;\n"
						+ "			     glytoucan:contributor ?Contributor .\n"
						+ "					}}\n"
						+ using
						+ "WHERE {\n"
						+ "				?s glycan:has_resource_entry ?res .\n"
						+ "			    	    ?res a glycan:resource_entry ;\n"
						+ "			    	         glytoucan:date_registered ?ContributionTime ;\n"
						+ "			    	         glytoucan:contributor ?c .\n"
						+ "			    	    ?c foaf:name ?Contributor .\n"
						+ "				}\n");
	}

	@Test
	public void testInsertSelectGraphMotif() throws SparqlException {
		schemaDAO.delete(prefix + "\n" + "INSERT\n"
				+ "{ GRAPH <"+graph+"> {\n"
				+ "				?s glycan:has_motif ?motif .\n"
				+ "						   ?motif a glycan:glycan_motif ;\n"
				+ "					          foaf:name ?MotifName .\n" + "					  }\n"
				+ "					}\n" + using + "WHERE {\n"
				+ "					    ?s glycan:has_motif ?motif .\n"
				+ "	    ?motif a glycan:glycan_motif ;\n"
				+ "	           foaf:name ?MotifName .\n" + "				}\n");
	}

	@Test
	public void testInsertSelectGraphMono() throws SparqlException {
		schemaDAO
				.delete(prefix
						+ "\n"
						+ "INSERT\n"
						+ "{ GRAPH <"+graph+"> {\n"
						+ "				?s glycan:has_component ?comp .\n"
						+ "			    	    ?comp a glycan:component .\n"
						+ "			    	    ?comp glycan:has_cardinality ?cardinality .\n"
						+ "			    	    ?comp glycan:has_monosaccharide ?name .\n"
						+ "					  }\n"
						+ "					}\n"
						+ using
						+ "WHERE {\n"
						+ "     ?s glycan:has_component ?comp .\n"
						+ "			    	    ?comp a glycan:component .\n"
						+ "			    	    ?comp glycan:has_cardinality ?cardinality .\n"
						+ "			    	    ?comp glycan:has_monosaccharide ?mono .\n"
						+ "			    	    ?mono a glycan:monosaccharide .\n"
						+ "			    	    ?mono owl:sameAs ?msdbMono .\n"
						+ "			    	    ?msdb owl:sameAs ?msdbMono .\n"
						+ "			    	    ?msdb a glycan:monosaccharide_alias .\n"
						+ "			            ?msdb glycan:has_alias_name ?name .\n"
						+ "						?msdb glycan:is_primary_name true .\n"
						+ "						?msdb glycan:has_monosaccharide_notation_scheme glycan:monosaccharide_notation_scheme_carbbank   .\n"
						+ "				}\n");
	}
}