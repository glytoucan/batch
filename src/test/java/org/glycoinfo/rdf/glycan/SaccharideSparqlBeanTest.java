package org.glycoinfo.rdf.glycan;

import static org.junit.Assert.assertEquals;

import org.glycoinfo.rdf.dao.SparqlEntity;
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