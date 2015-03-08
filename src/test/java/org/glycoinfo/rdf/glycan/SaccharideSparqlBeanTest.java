package org.glycoinfo.rdf.glycan;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.glycoinfo.batch.glyconvert.ConvertInsertSparql;
import org.glycoinfo.batch.glyconvert.wurcs.WurcsConvertSelectSparql;
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
		SaccharideInsertSparql ins = new SaccharideInsertSparql();
		SparqlEntity sparqlentity = new SparqlEntity();
		sparqlentity.setValue(Saccharide.URI, "insertsacharideuri");
		sparqlentity.setValue(GlycoSequence.URI, "glycosequenceuri");
		sparqlentity.setValue(Saccharide.PrimaryId, "primaryid");
		ins.setSparqlEntity(sparqlentity);
		return ins;
	}

	@Test
	public void testInsertSparql() {
		logger.debug(getSaccharideInsertSparql().getSparql());
		
		assertEquals(
				"INSERT INTO\n"
				+ "{ insertsacharideuri a glycosequenceuri .\n"
				+ "insertsacharideuri glytoucan:has_primary_id primaryid .\n"
				+ " }\n",
				getSaccharideInsertSparql().getSparql());
	}
}