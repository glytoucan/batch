package org.glycoinfo.rdf.scint;

import java.util.List;

import org.glycoinfo.convert.GlyConvertConfig;
import org.glycoinfo.rdf.InsertSparql;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.dao.virt.VirtSesameTransactionConfig;
import org.glycoinfo.rdf.service.impl.ContributorProcedureConfig;
import org.glycoinfo.rdf.service.impl.GlycanProcedureConfig;
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
import org.springframework.transaction.annotation.Transactional;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {ScintTest.class, VirtSesameTransactionConfig.class, ContributorProcedureConfig.class, GlycanProcedureConfig.class, GlyConvertConfig.class})
@ComponentScan(basePackages = {"org.glycoinfo.rdf.scint"}, excludeFilters={
		  @ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE, value=SelectScintTest.class)})
@Configuration
@EnableAutoConfiguration
public class ScintTest {

	public static Logger logger = (Logger) LoggerFactory
			.getLogger(ScintTest.class);
	
	public static final String graphUser = "http://test/schema/users";
	
	@Autowired
	@Qualifier(value = "ClassHandler")
	ClassHandler classHandler;
	
	@Autowired
	SparqlDAO sparqlDAO;
	
//	@Autowired
//	@Qualifier(value = "selectscintperson")
//	SelectScint selectScintPerson;

	@Autowired
	@Qualifier(value = "insertscintperson")
	InsertScint insertScintPerson;
	
//	@Autowired
//	@Qualifier(value = "selectscintregisteraction")
//	SelectScint selectScintRegisterAction;
//
//	@Autowired
//	@Qualifier(value = "insertscintregisteraction")
//	InsertScint insertScintRegisterAction;
//	
//	@Autowired
//	@Qualifier(value = "insertScintProgramMembership")
//	InsertScint insertScintProgramMembership;
//
//	@Autowired
//	@Qualifier(value = "deleteScintProgramMembership")
//	DeleteScint deleteScintProgramMembership;
//	
//	@Autowired
//	@Qualifier(value = "selectScintProgramMembership")
//	SelectScint selectScintProgramMembership;

	@Bean(name = "ClassHandler")
	ClassHandler getClassHandler() throws SparqlException {
		ClassHandler scint = new ClassHandler("schema", "http://schema.org/", "Person");
		return scint; 
	}

//	@Test
	public void testGetDomain() throws SparqlException {
		logger.debug("" + classHandler.getDomains());
	}
	

	
	@Bean(name = "insertscintperson")
	InsertScint getInsertPersonScint() throws SparqlException {
		InsertScint insert = new InsertScint(graphUser, "schema", "http://schema.org/", "Person");
//		insert.setClassHandler(getPersonClassHandler());
		return insert;
	}
	
	@Test
	@Transactional
	public void testInsertUri() throws SparqlException {
  SparqlEntity sparqlEntityPerson = new SparqlEntity("TestID123");
  sparqlEntityPerson.setValue(Scintillate.NO_DOMAINS, SelectSparql.TRUE);
  insertScintPerson.update(sparqlEntityPerson);

	sparqlDAO.insert((InsertSparql) insertScintPerson.getSparqlBean());
	}
}