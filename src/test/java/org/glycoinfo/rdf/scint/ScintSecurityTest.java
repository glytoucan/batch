//package org.glycoinfo.rdf.scint;
//
//import static org.junit.Assert.assertEquals;
//
//import java.util.List;
//
//import org.glycoinfo.rdf.SelectSparql;
//import org.glycoinfo.rdf.SparqlException;
//import org.glycoinfo.rdf.dao.SesameDAOTestConfig;
//import org.glycoinfo.rdf.dao.SparqlDAO;
//import org.glycoinfo.rdf.dao.SparqlDAOSesameImpl;
//import org.glycoinfo.rdf.dao.SparqlEntity;
//import org.glycoinfo.rdf.glycan.GlycoSequenceSelectSparql;
//import org.junit.Assert;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.test.SpringApplicationConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import scala.reflect.internal.Trees.Select;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = {ScintSecurityTest.class, SesameDAOTestConfig.class})
//@ComponentScan(basePackages = ("org.glycoinfo.rdf.scint"))
//@Configuration
//@EnableAutoConfiguration
//public class ScintSecurityTest {
//
//	public static Logger logger = (Logger) LoggerFactory
//			.getLogger(ScintSecurityTest.class);
//	
//	@Autowired
//	@Qualifier(value = "ClassHandler")
//	ClassHandler classHandler;
//	
//	@Autowired
//	SparqlDAO sparqlDAO;
//	
//	@Autowired
//	@Qualifier(value = "scint")
//	SelectScint selectScint;
//
//	@Autowired
//	@Qualifier(value = "insertscint")
//	InsertScint insertScint;
//	
//	
//	@Bean(name = "ClassHandler")
//	ClassHandler getClassHandler() throws SparqlException {
//		ClassHandler scint = new ClassHandler("security", "http://rdf.bluetree.jp/security/openid#", "OAuthClientToken");
//		return scint; 
//	}
//	
//	@Bean(name = "scint")
//	SelectScint getSelectScint() throws SparqlException {
//		SelectScint select = new SelectScint(getClassHandler());
//		return select;
//	}
//
//	@Bean(name = "insertscint")
//	InsertScint getInsertScint() throws SparqlException {
//		InsertScint insert = new InsertScint(getClassHandler());
//		return insert;
//	}
//
//	@Test
//	public void testGetDomain() throws SparqlException {
//		logger.debug("" + classHandler.getDomains());
//	}
//	
//	@Test
//	public void testSelectDomain() throws SparqlException {
//		SparqlEntity sparqlentity = new SparqlEntity();
//		sparqlentity.setValue("client_id", "");
//		sparqlentity.setValue("user_name", "");
//		sparqlentity.setValue("authentication_id", "support@glytoucan.org");
//		selectScint.setSparqlEntity(sparqlentity);
//		logger.debug(selectScint.getSparql());
//		List<SparqlEntity> results = sparqlDAO.query(selectScint);
//		
//		for (SparqlEntity result : results) {
//			Assert.assertEquals("Aoki", result.getValue("client_id"));
//			Assert.assertEquals("Nobu", result.getValue("user_name"));
//			Assert.assertEquals("support@glytoucan.org", result.getValue("authentication_id"));
//		}
//	}
//
//	@Test
//	public void testInsertDomain() throws SparqlException {
//		SparqlEntity sparqlentity = new SparqlEntity();
//		sparqlentity.setValue("client_id", "Aoki");
//		sparqlentity.setValue("user_name", "Nobu");
//		sparqlentity.setValue("authentication_id", "support@glytoucan.org");
//		insertScint.setSparqlEntity(sparqlentity);
//		logger.debug(insertScint.getSparql());
//		sparqlDAO.insert(insertScint);
//	}
//}