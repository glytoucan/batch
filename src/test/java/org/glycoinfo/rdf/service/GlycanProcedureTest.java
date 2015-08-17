package org.glycoinfo.rdf.service;

import org.glycoinfo.batch.glyconvert.wurcs.sparql.GlycoSequenceToWurcsSelectSparql;
import org.glycoinfo.conversion.error.ConvertException;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.dao.VirtSesameDAOTestConfig;
import org.glycoinfo.rdf.scint.ClassHandler;
import org.glycoinfo.rdf.scint.InsertScint;
import org.glycoinfo.rdf.scint.SelectScint;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {GlycanProcedureTest.class, VirtSesameDAOTestConfig.class})
@ComponentScan(basePackages = {"org.glycoinfo.rdf.service", "org.glycoinfo.rdf.scint"})
//@ComponentScan(basePackages = {"org.glycoinfo.rdf"}, excludeFilters={
//		  @ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE, value=Configuration.class)})
@Configuration
@EnableAutoConfiguration
public class GlycanProcedureTest {
	public static Logger logger = (Logger) LoggerFactory
			.getLogger(GlycanProcedureTest.class);
	
	@Autowired
	SparqlDAO sparqlDAO;
	
	@Bean(name = "selectscintperson")
	SelectScint getSelectPersonScint() throws SparqlException {
		SelectScint select = new SelectScint();
		
		select.setClassHandler(getPersonClassHandler());
		return select;
	}

	@Bean(name = "insertscintperson")
	InsertScint getInsertPersonScint() throws SparqlException {
		InsertScint insert = new InsertScint("http://rdf.glytoucan.org/users");
		insert.setClassHandler(getPersonClassHandler());
		return insert;
	}
	
	@Bean(name = "selectscintregisteraction")
	SelectScint getSelectRegisterActionScint() throws SparqlException {
		SelectScint select = new SelectScint();
		select.setClassHandler(getRegisterActionClassHandler());
		return select;
	}

	@Bean(name = "insertscintregisteraction")
	InsertScint getInsertRegisterActionScint() throws SparqlException {
		InsertScint insert = new InsertScint("http://rdf.glytoucan.org/users");
		insert.setClassHandler(getRegisterActionClassHandler());
		return insert;
	}
	
	@Bean
	ClassHandler getPersonClassHandler() throws SparqlException {
		ClassHandler ch = new ClassHandler("schema", "http://schema.org/", "Person");
		ch.setSparqlDAO(sparqlDAO);
		return ch; 
	}
	
	ClassHandler getRegisterActionClassHandler() throws SparqlException {
		ClassHandler ch = new ClassHandler("schema", "http://schema.org/", "RegisterAction");
		ch.setSparqlDAO(sparqlDAO);
		return ch; 
	}
	
	ClassHandler getDateTimeClassHandler() throws SparqlException {
		ClassHandler ch = new ClassHandler("schema", "http://schema.org/", "DateTime");
		ch.setSparqlDAO(sparqlDAO);
		return ch; 
	}
	
	@Autowired
	GlycanProcedure glycanProcedure;

	@Bean(name = "glycanProcedure")
	GlycanProcedure getGlycanProcedure() throws SparqlException {
		GlycanProcedure glycan = new org.glycoinfo.rdf.service.impl.GlycanProcedure();
		return glycan;
	}

//	@Test(expected=SparqlException.class)
//	public void testInsufficientUser() throws SparqlException {
//		SparqlEntity se = new SparqlEntity();
//		se.setValue("id", "person456");
//		userProcedure.setSparqlEntity(se);
//		userProcedure.addUser();
//	}
	
	/*
	 * RES
1b:b-dglc-HEX-1:5
2s:n-acetyl
3b:b-dgal-HEX-1:5
LIN
1:1d(2+1)2n
2:1o(4+1)3d
	 */
	@Test
	public void testSearch() throws SparqlException, ConvertException {
		glycanProcedure.setSequence("RES\n" +
				"1b:b-dglc-HEX-1:5\n" +
				"2s:n-acetyl\n"
				+ "3b:b-dgal-HEX-1:5\n"
				+ "LIN\n"
				+ "1:1d(2+1)2n\n"
				+ "2:1o(4+1)3d");
		SparqlEntity se = glycanProcedure.search();
		
		logger.debug(se.getValue(GlycoSequenceToWurcsSelectSparql.AccessionNumber));
		logger.debug(se.getValue(GlycanProcedure.Image));
		logger.debug(se.getValue(GlycoSequenceToWurcsSelectSparql.Sequence));
		Assert.assertNotNull(se.getValue(GlycoSequenceToWurcsSelectSparql.AccessionNumber));
		Assert.assertNotNull(se.getValue(GlycanProcedure.Image));
		Assert.assertNotNull(se.getValue(GlycoSequenceToWurcsSelectSparql.Sequence));
		Assert.assertEquals("G00055MO", se.getValue(GlycoSequenceToWurcsSelectSparql.AccessionNumber));
	}
	
	@Test
	public void testSearchG00031MO() throws SparqlException, ConvertException {
		// ne
//		data-wurcs="WURCS=2.0/2,2,1/[a2112h-1a_1-5_2*NCC/3=O][a2112h-1b_1-5]/1-2/a3-b1" >
	
//test
//		data-wurcs="WURCS=2.0/2,2,1/[22112h-1a_1-5_2*NCC/3=O][12112h-1b_1-5]/1-2/a3-b1">
//		data-wurcs="WURCS=2.0/2,2,1/[22112h-1a_1-5_2*NCC/3=O][12112h-1b_1-5]/1-2/a3-b1">
		glycanProcedure.setSequence("RES\n"
				+ "1b:a-dgal-HEX-1:5\n"
				+ "2s:n-acetyl\n"
				+ "3b:b-dgal-HEX-1:5\n"
				+ "LIN\n"
				+ "1:1d(2+1)2n\n"
				+ "2:1o(3+1)3d");
		SparqlEntity se = glycanProcedure.search();

		logger.debug(se.getValue(GlycoSequenceToWurcsSelectSparql.AccessionNumber));
		logger.debug(se.getValue(GlycanProcedure.Image));
		logger.debug(se.getValue(GlycoSequenceToWurcsSelectSparql.Sequence));
		Assert.assertNotNull(se.getValue(GlycoSequenceToWurcsSelectSparql.AccessionNumber));
		Assert.assertNotNull(se.getValue(GlycanProcedure.Image));
		Assert.assertNotNull(se.getValue(GlycoSequenceToWurcsSelectSparql.Sequence));
		Assert.assertEquals("G00031MO", se.getValue(GlycoSequenceToWurcsSelectSparql.AccessionNumber));
	}

//	@Test
//	public void testUserNotVerified() throws SparqlException {
//		SparqlEntity se = new SparqlEntity("person456");
//		se.setValue("email", "person456@person.com");
//		se.setValue("givenName", "person");
//		se.setValue("familyName", "456");
//		se.setValue("verifiedEmail", "false");
//		userProcedure.setSparqlEntity(se);
//		userProcedure.addUser();
//		se.setValue("member", "");
//		se.remove("verifiedEmail");
//		
//		SelectScint personScint = getSelectPersonScint();
//		personScint.setSparqlEntity(se);
//		List<SparqlEntity> results = sparqlDAO.query(personScint);
//		
//		Assert.assertFalse(results.size() == 0);
//		for (SparqlEntity sparqlEntity : results) {
//			logger.debug(sparqlEntity.toString());
//			Assert.assertNull(sparqlEntity.getValue("contributor"));
//			Assert.assertNotNull(sparqlEntity.getValue("member"));
//		}
//	}
//	
//	@Test
//	public void testGetUser() throws SparqlException {
//		List<SparqlEntity> results = userProcedure.getUser("aokinobu@gmail.com");
//		Assert.assertFalse(results.size() == 0);
//		for (SparqlEntity sparqlEntity : results) {
//			logger.debug(sparqlEntity.toString());
//			Assert.assertNull(sparqlEntity.getValue("member"));
//			Assert.assertNotNull(sparqlEntity.getValue("contributor"));
//		}
//	}

}