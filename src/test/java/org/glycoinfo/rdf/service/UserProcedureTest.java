package org.glycoinfo.rdf.service;

import java.util.List;

import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.dao.virt.VirtSesameTransactionConfig;
import org.glycoinfo.rdf.scint.ClassHandler;
import org.glycoinfo.rdf.scint.InsertScint;
import org.glycoinfo.rdf.scint.SelectScint;
import org.glycoinfo.rdf.service.impl.ContributorProcedureRdf;
import org.glycoinfo.rdf.service.impl.GlycanProcedureConfig;
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
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {UserProcedureTest.class, VirtSesameTransactionConfig.class, GlycanProcedureConfig.class })
@ComponentScan(basePackages = {"org.glycoinfo.rdf.service", "org.glycoinfo.rdf.scint"})
//@ComponentScan(basePackages = {"org.glycoinfo.rdf"}, excludeFilters={
//		  @ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE, value=Configuration.class)})
@Configuration
@EnableAutoConfiguration
public class UserProcedureTest {
	
	public static Logger logger = (Logger) LoggerFactory
			.getLogger(UserProcedureTest.class);
	
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
	UserProcedure userProcedure;

	@Bean(name = "userProcedure")
	UserProcedure getUserProcedure() throws SparqlException {
		UserProcedure user = new org.glycoinfo.rdf.service.impl.UserProcedure();
		return user;
	}
	
	@Bean(name = "contributorProcedure")
	ContributorProcedure getContributorProcedure() throws SparqlException {
		ContributorProcedure cp = new ContributorProcedureRdf();
		return cp;
	}

	@Test(expected=SparqlException.class)
	@Transactional
	public void testInsufficientUser() throws SparqlException {
		SparqlEntity se = new SparqlEntity();
		se.setValue("id", "person456");
		userProcedure.setSparqlEntity(se);
		userProcedure.addUser();
	}
	
	@Test
	@Transactional
	public void testUser() throws SparqlException {
		SparqlEntity se = new SparqlEntity();
		se.setValue(SelectScint.PRIMARY_KEY, "person789");
		se.setValue("email", "person789@person.com");
		se.setValue("givenName", "person");
		se.setValue("familyName", "789");
		se.setValue("verifiedEmail", "true");
		userProcedure.setSendEmail(false);
		userProcedure.setSparqlEntity(se);
		userProcedure.addUser();

		se.setValue("member", "");
		se.setValue("contributor", "");
		se.remove("verifiedEmail");

		SelectScint personScint = getSelectPersonScint();
		personScint.setSparqlEntity(se);
		List<SparqlEntity> results = sparqlDAO.query(personScint);
		
		Assert.assertFalse(results.size() == 0);
		for (SparqlEntity sparqlEntity : results) {
			logger.debug(sparqlEntity.toString());
			Assert.assertNotNull(sparqlEntity.getValue("contributor"));
			Assert.assertNotNull(sparqlEntity.getValue("member"));
		}
	}

	@Test
	public void testUserNotVerified() throws SparqlException {
		SparqlEntity se = new SparqlEntity("person456");
		se.setValue("email", "person456@person.com");
		se.setValue("givenName", "person");
		se.setValue("familyName", "456");
		se.setValue("verifiedEmail", "false");
		userProcedure.setSparqlEntity(se);
		userProcedure.addUser();
		se.setValue("member", "");
		se.remove("verifiedEmail");
		
		SelectScint personScint = getSelectPersonScint();
		personScint.setSparqlEntity(se);
		List<SparqlEntity> results = sparqlDAO.query(personScint);
		
		Assert.assertFalse(results.size() == 0);
		for (SparqlEntity sparqlEntity : results) {
			logger.debug(sparqlEntity.toString());
			Assert.assertNull(sparqlEntity.getValue("contributor"));
			Assert.assertNotNull(sparqlEntity.getValue("member"));
		}
	}
	
	@Test
	public void testGetUser() throws SparqlException {
		List<SparqlEntity> results = userProcedure.getUser("aokinobu@gmail.com");
		Assert.assertFalse(results.size() == 0);
		for (SparqlEntity sparqlEntity : results) {
			logger.debug(sparqlEntity.toString());
			Assert.assertNotNull(sparqlEntity.getValue("familyName"));
			Assert.assertNotNull(sparqlEntity.getValue("givenName"));
			Assert.assertNotNull(sparqlEntity.getValue("alternateName"));
		}
	}

}