package org.glycoinfo.rdf.service;

import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SesameDAOTestConfig;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.scint.ClassHandler;
import org.glycoinfo.rdf.scint.InsertScint;
import org.glycoinfo.rdf.service.impl.UserProcedure;
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
import org.springframework.context.annotation.FilterType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {InitProcedureTest.class, SesameDAOTestConfig.class} )
@ComponentScan(basePackages = {"org.glycoinfo.rdf.service", "org.glycoinfo.rdf.scint"}, excludeFilters={
		  @ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE, value=UserProcedureTest.class), @ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE, value=UserProcedure.class) })
//@ComponentScan(basePackages = {"org.glycoinfo.rdf"}, excludeFilters={
//		  @ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE, value=Configuration.class)})
@Configuration
@EnableAutoConfiguration
public class InitProcedureTest {
	
	public static Logger logger = (Logger) LoggerFactory
			.getLogger(InitProcedureTest.class);
	
	@Autowired
	SparqlDAO sparqlDAO;
	
	@Bean(name = "insertscintorganization")
	InsertScint getInsertPersonScint() throws SparqlException {
		InsertScint insert = new InsertScint("schema", "http://schema.org/", "Organization");
		insert.getSparqlBean().setGraph("http://rdf.glytoucan.org/users");
//		insert.setClassHandler(getOrganizationClassHandler());
		return insert;
	}
	
	@Bean
	ClassHandler getOrganizationClassHandler() throws SparqlException {
		ClassHandler ch = new ClassHandler("schema", "http://schema.org/", "Organization");
		ch.setSparqlDAO(sparqlDAO);
		return ch; 
	}
	
	@Autowired
	InitProcedure initProcedure;

	@Bean(name = "initProcedure")
	InitProcedure getInitProcedure() throws SparqlException {
		InitProcedure init = new org.glycoinfo.rdf.service.impl.InitProcedure();
		return init;
	}

	@Test
	public void testInit() throws SparqlException {
		initProcedure.initialize();
	}
}