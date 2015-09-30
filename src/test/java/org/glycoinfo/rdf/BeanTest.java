package org.glycoinfo.rdf;

import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.dao.virt.VirtSesameTransactionConfig;
import org.glycoinfo.rdf.glycan.GlycoSequence;
import org.glycoinfo.rdf.glycan.wurcs.MotifSequenceSelectSparql;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { BeanTest.class, VirtSesameTransactionConfig.class } )
@Configuration
@EnableAutoConfiguration
public class BeanTest {
	public static Logger logger = (Logger) LoggerFactory
			.getLogger(BeanTest.class);

	@Autowired
	SparqlDAO sparqlDAO;
	
	@Bean
	SelectSparql getSparql() {
		MotifSequenceSelectSparql sparql = new MotifSequenceSelectSparql();
		return sparql;
	}
	
	@Test
	public void testSparql() throws SparqlException {
		SparqlEntity sparqlentity = new SparqlEntity();
		sparqlentity.setValue(GlycoSequence.URI, "rdf://sacharide.test");
		sparqlentity.setValue(GlycoSequence.Sequence, "WURCS=2.0/2,3,2/[11122h-1b_1-5][21122h-1a_1-5]/1-2-2/a3-b1_a6-c1");
		getSparql().setSparqlEntity(sparqlentity);

		logger.debug(getSparql().getSparql());
	}
}