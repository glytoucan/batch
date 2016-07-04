package org.glycoinfo.rdf.glycan;

import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.dao.VirtSesameDAOTestConfig;
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
import org.springframework.transaction.annotation.Transactional;


/*
 * Test the insert data for the partner metadata
 * @author shinmachi
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {PartnerMetaInsertSparqlTest.class, VirtSesameDAOTestConfig.class })
@Configuration
@EnableAutoConfiguration
public class PartnerMetaInsertSparqlTest {


	public static Logger logger = (Logger) LoggerFactory
			.getLogger(PartnerMetaInsertSparqlTest.class);
	
	@Autowired
	SparqlDAO sparqlDAO;

	@Bean
	PartnerMetaInsertSparql getPartnerMetaInsertSparql() {
		PartnerMetaInsertSparql ins = new PartnerMetaInsertSparql();
		SparqlEntity sparqlentity = new SparqlEntity();
		sparqlentity.setValue(Partner.PartnerName, "partnername");
		ins.setSparqlEntity(sparqlentity);
		ins.setGraph("http://test");
		return ins;
	}

	@Test
	public void testInsertSparql() throws SparqlException {
		logger.debug(getPartnerMetaInsertSparql().getSparql());
	}
	
	@Test
	@Transactional
	public void insertSparql() throws SparqlException {
		sparqlDAO.insert(getPartnerMetaInsertSparql());
	}

}
