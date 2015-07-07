package org.glycoinfo.rdf.service;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SesameDAOTestConfig;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlDAOSesameImpl;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.glycan.GlycoSequenceSelectSparql;
import org.glycoinfo.rdf.scint.ClassHandler;
import org.glycoinfo.rdf.scint.InsertScint;
import org.glycoinfo.rdf.scint.SelectScint;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {UserProcedureTest.class, SesameDAOTestConfig.class})
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

	@Test(expected=SparqlException.class)
	public void testInsufficientUser() throws SparqlException {
		SparqlEntity se = new SparqlEntity();
		se.setValue("id", "person456");
		userProcedure.setSparqlEntity(se);
		userProcedure.addUser();
	}
	
	@Test
	public void testUser() throws SparqlException {
		SparqlEntity se = new SparqlEntity();
		se.setValue("id", "person456");
		se.setValue("email", "person456@person.com");
		se.setValue("givenName", "person");
		se.setValue("familyName", "456");
		se.setValue("verifiedEmail", "false");
		userProcedure.setSparqlEntity(se);
		userProcedure.addUser();
	}

	
}