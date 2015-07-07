package org.glycoinfo.rdf.scint;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SesameDAOTestConfig;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlDAOSesameImpl;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.glycan.GlycoSequenceSelectSparql;
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
@SpringApplicationConfiguration(classes = {ScintTest.class, SesameDAOTestConfig.class})
@ComponentScan(basePackages = {"org.glycoinfo.rdf.scint"}, excludeFilters={
		  @ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE, value=SelectScintTest.class)})
@Configuration
@EnableAutoConfiguration
public class ScintTest {

	public static Logger logger = (Logger) LoggerFactory
			.getLogger(ScintTest.class);
	
	@Autowired
	@Qualifier(value = "ClassHandler")
	ClassHandler classHandler;

	@Bean(name = "ClassHandler")
	ClassHandler getClassHandler() throws SparqlException {
		ClassHandler scint = new ClassHandler("schema", "http://schema.org/", "Person");
		return scint; 
	}

	@Test
	public void testGetDomain() throws SparqlException {
		logger.debug("" + classHandler.getDomains());
	}
}