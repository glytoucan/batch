package org.glycoinfo.batch.search;

import static org.junit.Assert.assertEquals;

import org.glycoinfo.WURCSFramework.util.rdf.SearchSparql;
import org.glycoinfo.WURCSFramework.util.rdf.SearchSparqlBean;
import org.glycoinfo.batch.search.wurcs.MotifSearchSparql;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.glycan.GlycoSequence;
import org.glycoinfo.rdf.glycan.Saccharide;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SearchTest.class)
@Configuration
@EnableAutoConfiguration
public class SearchTest {

	@Bean
	MotifSearchSparql getMotifSearchSparql() {
		MotifSearchSparql search = new MotifSearchSparql();
		search.setSearchSparql(new SearchSparqlBean());
		SparqlEntity sparqlentity = new SparqlEntity();
		sparqlentity.setValue(GlycoSequence.URI, "rdf://sacharide.test");
		sparqlentity.setValue(GlycoSequence.Sequence, "WURCS=2.0/2,2,1/[11122h-1b_1-5][21122h-1a_1-5]/1-2/b1-a3|a6");
		search.setSparqlEntity(sparqlentity);
		
		return search;
	}
	
	@Bean
	SearchSparql getSearchSparql() {
		return new SearchSparqlBean();
	}
	
	@Test
	public void testSelectSparql() throws SparqlException {
		assertEquals("PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
				+ "PREFIX toucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>\n"
				+ "SELECT ?SaccharideURI ?PrimaryId\n"
				+ "SELECT ?SaccharideURI ?PrimaryId\n"
				+ "from <http://glytoucan.org/rdf/demo/0.2>\n"
				+ "from <http://glycoinfo.org/graph/wurcs>\n\n"
				+ "WHERE {<rdf://sacharide.test> wurcs:has_uniqueRES  ?uRES1, ?uRES2 .\n"
				+ "?uRES1 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/11122h-1b_1-5> .\n"
				+ "?uRES2 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/21122h-1a_1-5> .\n"
				+ "# RES\n"
				+ "?RESa wurcs:is_uniqueRES ?uRES1 .\n"
				+ "?RESb wurcs:is_uniqueRES ?uRES2 .\n"
				+ "# LIN\n"
				+ "?gseq wurcs:has_LIN ?LINb1a3a6 .\n"
				+ "# LIN1\n"
				+ "?LINb1a3a6 wurcs:has_GLIPS   ?GLIPSb1 ,   ?GLIPSa3a6 .\n"
				+ "# LIN1: GLIPS1\n"
				+ "?GLIPSb1 wurcs:has_GLIP ?GLIPb1 .\n"
				+ "?GLIPb1 wurcs:has_SC_position 1 .\n"
				+ "?GLIPb1 wurcs:has_RES ?RESb .\n"
				+ "?GLIPSb1 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
						+ "# LIN1: GLIPS2\n"
						+ "?GLIPSa3a6 wurcs:has_GLIP ?GLIPa3a6 .\n"
						+ "?GLIPa3 wurcs:has_SC_position 3 .\n"
						+ "?GLIPa3 wurcs:has_RES ?RESa .\n"
						+ "?GLIPa6 wurcs:has_SC_position 6 .\n"
						+ "?GLIPa6 wurcs:has_RES ?RESa .\n"
						+ "?GLIPSa3a6 wurcs:isFuzzy \"true\"^^xsd:boolean .\n"
								+ "}", getMotifSearchSparql().getSparql());
	}
//	
//	@Test
//	public void testInsertSparql() {
//		ConvertInsertSparql convert = getConvertInsertSparql();
//		SparqlEntity se = new SparqlEntity();
//		se.setValue("SaccharideURI", "<testSaccharideURI>");
//		se.setValue("SequenceURI", "<testSequenceURI>");
//		se.setValue("ConvertedSequence", "testConvertedSequence!");
//		convert.setSparqlEntity(se);
//		assertEquals("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
//				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
//				+ "PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
//				+ "PREFIX glytoucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>\n"
//				+ "INSERT INTO\n"
//				+ "GRAPH <http://glytoucan.org/rdf/demo/0.7wurcs>\n"
//				+ "USING <http://glytoucan.org/rdf/demo/0.2>\n"
//				+ "USING <http://glytoucan.org/rdf/demo/msdb/8>\n"
//				+ "USING <http://purl.jp/bio/12/glyco/glycan/ontology/0.18>\n"
//				+ "USING <http://www.glytoucan.org/glyco/owl/glytoucan>\n"
//				+ "{ <testSaccharideURI> glycan:has_glycosequence <testSequenceURI> .\n"
//				+ "<testSequenceURI> glycan:has_sequence \"testConvertedSequence!\"^^xsd:string .\n"
//				+ "<testSequenceURI> glycan:in_carbohydrate_format glycan:carbohydrate_format_wurcs .\n"
//				+ "<testSequenceURI> glytoucan:is_glycosequence_of <testSaccharideURI> .\n }\n", convert.getSparql());
//	}
}