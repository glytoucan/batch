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
		// for example "UniCarb-DB"
		sparqlentity.setValue(Partner.PartnerUriName, "unicarb-db");
		sparqlentity.setValue(Partner.PartnerName, "UniCarb-DB");
		sparqlentity.setValue(Partner.PartnerDesc, "A comprehensive LC MS/MS library of N- and O- linked glycans released from glycoproteins that have been annotated with glycosidic and cross-ring fragmentation ions, retention times, and associated experimental metadata descriptions.");
		sparqlentity.setValue(Partner.PartnerURL, "<http://unicarb-db.biomedicine.gu.se/>");
		ins.setSparqlEntity(sparqlentity);
//		ins.setGraph("http://test");
		ins.setGraph("http://rdf.glytoucan.org/partner");
		return ins;
	}
	@Test
	public void testInsertSparql() throws SparqlException {
		logger.debug(getPartnerMetaInsertSparql().getSparql());
	}
	

	// Select sparql
	@Bean
	PartnerMetaSelectSparql getPartnerMetaSelectSparql() {
		PartnerMetaSelectSparql pss = new PartnerMetaSelectSparql();
		return pss ;
	}
	@Test
	public void testSelectSparql() throws SparqlException {
		logger.debug(getPartnerMetaSelectSparql().getSparql());
	}

	
	// execSparl
	@Test
	@Transactional
	public void insertSparql() throws SparqlException {
		sparqlDAO.query(getPartnerMetaSelectSparql());
		sparqlDAO.insert(getPartnerMetaInsertSparql());
		sparqlDAO.query(getPartnerMetaSelectSparql());
	}

}
