package org.glycoinfo.rdf.glycan;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.glycoinfo.WURCSFramework.util.rdf.SearchSparql;
import org.glycoinfo.WURCSFramework.util.rdf.SearchSparqlBean;
import org.glycoinfo.batch.glyconvert.ConvertInsertSparql;
import org.glycoinfo.batch.glyconvert.ConvertSelectSparql;
import org.glycoinfo.batch.search.wurcs.MotifSearchSparql;
import org.glycoinfo.conversion.GlyConvert;
import org.glycoinfo.conversion.wurcs.GlycoctToWurcsConverter;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SesameDAOTestConfig;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlDAOSesameImpl;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.glycan.wurcs.MotifSelectSparql;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import ch.qos.logback.classic.Logger;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = MotifSparqlBeanTest.class)
@Configuration
@EnableAutoConfiguration
public class MotifSparqlBeanTest {

	public static Logger logger = (Logger) LoggerFactory
			.getLogger(MotifSparqlBeanTest.class);

	@Bean
	MotifSelectSparql getMotifSelectSparql() {
		return new MotifSelectSparql();
	}
	
	@Bean
	MotifSearchSparql getMotifSearchSparql() {
		MotifSearchSparql motfs = new MotifSearchSparql();
		SparqlEntity se = new SparqlEntity();
		// G00047MO
		se.setValue(GlycoSequence.Sequence, "WURCS=2.0/3,3,2/[x2122h-1x_1-5_2*NCC/3=O][12112h-1b_1-5][11221m-1a_1-5]/1-2-3/a3-b1_a4-c1");
		motfs.setSparqlEntity(se);
		motfs.setSearchSparql(getSearchSparql());
		return motfs;
	}
	
	@Bean
	SearchSparql getSearchSparql() {
		SearchSparqlBean ss = new SearchSparqlBean();
		ss.setGlycoSequenceVariable(GlycoSequence.URI);
		return ss;
	}

//	@Bean
//	ConvertInsertSparql getConvertInsertSparql() {
//		ConvertInsertSparql convert = new ConvertInsertSparql();
//		convert.setGraphBase("http://glytoucan.org/rdf/demo/0.7");
//		return convert;
//	}

	@Test
	public void testSelectSparql() throws SparqlException {
		logger.debug(getMotifSelectSparql().getSparql());
		assertEquals("PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
				+ "PREFIX toucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>\n"
				+ "SELECT ?SaccharideURI ?PrimaryId ?GlycoSequenceURI ?Sequence\n"
				+ "FROM <http://glytoucan.org/rdf/demo/0.2>\n"
				+ "FROM <http://glytoucan.org/rdf/demo/0.3/wurcs>\n"
				+ " WHERE {?SaccharideURI a glycan:glycan_motif .\n"
				+ "?SaccharideURI toucan:has_primary_id ?PrimaryId .\n"
				+ "?SaccharideURI glycan:has_glycosequence ?GlycoSequenceURI .\n"
				+ "?GlycoSequenceURI glycan:has_sequence ?Sequence .\n"
				+ "?GlycoSequenceURI glycan:in_carbohydrate_format glycan:carbohydrate_format_wurcs\n}\n", getMotifSelectSparql().getSparql());
	}

	@Test
	public void testSearchSelectSparql() throws SparqlException {
		logger.debug(getMotifSelectSparql().getSparql());
		assertEquals("PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
				+ "PREFIX toucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>\n"
				+ "SELECT DISTINCT ?SaccharideURI ?PrimaryId\n"
				+ "FROM <http://rdf.glytoucan.org>\n"
				+ "FROM <http://rdf.glytoucan.org/sequence/wurcs>\n"
				+ "FROM <http://www.glycoinfo.org/graph/wurcs/0.4>\n"
				+ "WHERE {?SaccharideURI toucan:has_primary_id ?PrimaryId .\n"
				+ "GRAPH <http://www.glycoinfo.org/graph/wurcs/0.4> {?SaccharideURI glycan:has_glycosequence ?GlycoSequenceURI .\n"
				+ "?null wurcs:has_uniqueRES  ?uRES1, ?uRES2, ?uRES3 .\n"
				+ "?uRES1 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/x2122h-1x_1-5_2*NCC%2F3%3DO> .\n"
				+ "?uRES2 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/12112h-1b_1-5> .\n"
				+ "?uRES3 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/11221m-1a_1-5> .\n"
				+ "# RES\n"
				+ "?RESa wurcs:is_uniqueRES ?uRES1 .\n"
				+ "?RESb wurcs:is_uniqueRES ?uRES2 .\n"
				+ "?RESc wurcs:is_uniqueRES ?uRES3 .\n"
				+ "# LIN\n"
				+ "?gseq wurcs:has_LIN ?LINa3b1 , ?LINa4c1 . \n"
				+ "# LIN1\n"
				+ "  ?LINa3b1 wurcs:has_GLIPS   ?GLIPSa3 ,   ?GLIPSb1 .  \n"
				+ "# LIN2\n"
				+ "  ?LINa4c1 wurcs:has_GLIPS   ?GLIPSa4 ,   ?GLIPSc1 .  \n"
				+ "\n"
				+ " # LIN1: GLIPS1\n"
				+ "  ?GLIPSa3 wurcs:has_GLIP ?GLIPa3 . \n"
				+ "  ?GLIPa3 wurcs:has_SC_position 3 .\n"
				+ "  ?GLIPa3 wurcs:has_RES ?RESa .\n"
				+ "  ?GLIPSa3 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
				+ "# LIN1: GLIPS2\n"
				+ "  ?GLIPSb1 wurcs:has_GLIP ?GLIPb1 . \n"
				+ "  ?GLIPb1 wurcs:has_SC_position 1 .\n"
				+ "  ?GLIPb1 wurcs:has_RES ?RESb .\n"
				+ "  ?GLIPSb1 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
						+ "# LIN2: GLIPS1\n"
						+ "  ?GLIPSa4 wurcs:has_GLIP ?GLIPa4 . \n"
						+ "  ?GLIPa4 wurcs:has_SC_position 4 .\n"
						+ "  ?GLIPa4 wurcs:has_RES ?RESa .\n"
						+ "  ?GLIPSa4 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
								+ "# LIN2: GLIPS2\n"
								+ "  ?GLIPSc1 wurcs:has_GLIP ?GLIPc1 . \n"
								+ "  ?GLIPc1 wurcs:has_SC_position 1 .\n"
								+ "  ?GLIPc1 wurcs:has_RES ?RESc .\n"
								+ "  ?GLIPSc1 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
								+ "}}\n"
								+ "", getMotifSearchSparql().getSparql());
	}

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