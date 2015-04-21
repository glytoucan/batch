package org.glycoinfo.rdf.glycan;

import static org.junit.Assert.assertEquals;

import org.glycoinfo.batch.search.SearchSparql;
import org.glycoinfo.batch.search.wurcs.MotifSearchSparql;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.glycan.wurcs.MotifSelectSparql;
import org.glycoinfo.rdf.search.SearchSparqlBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


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
		ss.setGlycoSequenceUri(GlycoSequence.URI);
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
				+ "SELECT ?MotifURI ?PrimaryId ?GlycoSequenceURI ?Sequence\n"
				+ "FROM <http://rdf.glytoucan.org>\n"
				+ "FROM <http://glytoucan.org/rdf/demo/0.3/wurcs>\n"
				+ "WHERE {?MotifURI a glycan:glycan_motif .\n"
				+ "?MotifURI toucan:has_primary_id ?PrimaryId .\n"
				+ "?MotifURI glycan:has_glycosequence ?GlycoSequenceURI .\n"
				+ "?GlycoSequenceURI glycan:has_sequence ?Sequence .\n"
				+ "?GlycoSequenceURI glycan:in_carbohydrate_format glycan:carbohydrate_format_wurcs\n}\n", getMotifSelectSparql().getSparql());
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