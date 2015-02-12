package org.glycoinfo.rdf.glycan;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.glycoinfo.batch.glyconvert.ConvertInsertSparql;
import org.glycoinfo.batch.glyconvert.ConvertSelectSparql;
import org.glycoinfo.conversion.GlyConvert;
import org.glycoinfo.conversion.wurcs.GlycoctToWurcsConverter;
import org.glycoinfo.rdf.dao.SesameDAOTestConfig;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlDAOSesameImpl;
import org.glycoinfo.rdf.dao.SparqlEntity;
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
@SpringApplicationConfiguration(classes = SaccharideSparqlBeanTest.class)
@Configuration
@EnableAutoConfiguration
public class SaccharideSparqlBeanTest {

	public static Logger logger = (Logger) LoggerFactory
			.getLogger(SaccharideSparqlBeanTest.class);

	@Bean
	SaccharideInsertSparql getSaccharideInsertSparql() {
		return new SaccharideInsertSparql();
	}

	@Test
	public void testInsertSparql() {
		logger.debug(getSaccharideInsertSparql().getSparql());
		assertEquals(
				"PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
						+ "PREFIX toucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>\n"
						+ "SELECT ?SaccharideURI ?PrimaryId ?GlycoSequenceURI ?Sequence\n"
						+ "FROM <http://glytoucan.org/rdf/demo/0.2>\n"
						+ "FROM <http://glytoucan.org/rdf/demo/0.3/wurcs>\n"
						+ " WHERE {?SaccharideURI a glycan:glycan_motif .\n"
						+ "?SaccharideURI toucan:has_primary_id ?PrimaryId .\n"
						+ "?SaccharideURI glycan:has_glycosequence ?GlycoSequenceURI .\n"
						+ "?GlycoSequenceURI glycan:has_sequence ?Sequence .\n"
						+ "?GlycoSequenceURI glycan:in_carbohydrate_format glycan:carbohydrate_format_wurcs\n}\n",
				getSaccharideInsertSparql().getSparql());
	}
}