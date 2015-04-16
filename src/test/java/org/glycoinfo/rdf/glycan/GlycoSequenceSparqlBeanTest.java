package org.glycoinfo.rdf.glycan;

import static org.junit.Assert.assertEquals;

import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.glycan.GlycoSequenceSelectSparql;
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
@SpringApplicationConfiguration(classes = GlycoSequenceSparqlBeanTest.class)
@Configuration
@EnableAutoConfiguration
public class GlycoSequenceSparqlBeanTest {

	public static Logger logger = (Logger) LoggerFactory
			.getLogger(GlycoSequenceSparqlBeanTest.class);

	@Bean
	GlycoSequenceSelectSparql getGlycoSequenceSparql() {
		GlycoSequenceSelectSparql ins = new GlycoSequenceSelectSparql();
		SparqlEntity sparqlentity = new SparqlEntity();
		sparqlentity.setValue(Saccharide.PrimaryId, "G00009BX");
		ins.setSparqlEntity(sparqlentity);
		return ins;
	}

	@Test
	public void testSelectSparql() throws SparqlException {
		logger.debug(getGlycoSequenceSparql().getSparql());
		
		assertEquals(
				"INSERT INTO\n"
				+ "{ insertsacharideuri a glycosequenceuri .\n"
				+ "insertsacharideuri glytoucan:has_primary_id primaryid .\n"
				+ " }\n",
				getGlycoSequenceSparql().getSparql());
	}
}