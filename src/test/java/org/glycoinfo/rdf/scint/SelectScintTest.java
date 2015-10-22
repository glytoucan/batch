package org.glycoinfo.rdf.scint;

import static org.junit.Assert.assertEquals;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SesameDAOTestConfig;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlDAOSesameImpl;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.dao.virt.VirtSesameTransactionConfig;
import org.glycoinfo.rdf.glycan.GlycoSequenceSelectSparql;
import org.glycoinfo.rdf.schema.org.DateTime;
import org.glycoinfo.rdf.utils.NumberGenerator;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
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
@SpringApplicationConfiguration(classes = {SelectScintTest.class, VirtSesameTransactionConfig.class})
@ComponentScan(basePackages = {"org.glycoinfo.rdf.scint"}, excludeFilters={
		  @ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE, value=ScintTest.class)})
@Configuration
@EnableAutoConfiguration
public class SelectScintTest {

		private static final Log logger = LogFactory.getLog(SelectScintTest.class);
	
	@Autowired
	SparqlDAO sparqlDAO;
	
	@Autowired
	@Qualifier(value = "selectscintperson")
	SelectScint selectScintPerson;

	@Autowired
	@Qualifier(value = "insertscintperson")
	InsertScint insertScintPerson;
	
	@Autowired
	@Qualifier(value = "selectscintregisteraction")
	SelectScint selectScintRegisterAction;

	@Autowired
	@Qualifier(value = "insertscintregisteraction")
	InsertScint insertScintRegisterAction;
	
	ClassHandler getPersonClassHandler() throws SparqlException {
		ClassHandler classHandler = new ClassHandler("schema", "http://schema.org/", "Person");
		classHandler.setSparqlDAO(sparqlDAO);
		return classHandler; 
	}

	ClassHandler getProgramMembershipClassHandler() throws SparqlException {
		ClassHandler classHandler = new ClassHandler("schema", "http://schema.org/", "ProgramMembership");
		classHandler.setSparqlDAO(sparqlDAO);
		return classHandler; 
	}

	ClassHandler getRegisterActionClassHandler() throws SparqlException {
		ClassHandler classHandler = new ClassHandler("schema", "http://schema.org/", "RegisterAction");
		classHandler.setSparqlDAO(sparqlDAO);
		return classHandler; 
	}

	ClassHandler getDateTimeClassHandler() throws SparqlException {
		ClassHandler classHandler = new ClassHandler("schema", "http://schema.org/", "DateTime");
		classHandler.setSparqlDAO(sparqlDAO);
		return classHandler; 
	}
	
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
	
	@Test
	public void testSelectDomain() throws SparqlException {
		SparqlEntity sparqlentity = new SparqlEntity("person123");
		sparqlentity.setValue("familyName", "");
		sparqlentity.setValue("givenName", "");
		sparqlentity.setValue("email", "support@glytoucan.org");
		selectScintPerson.setSparqlEntity(sparqlentity);
		logger.debug(selectScintPerson.getSparql());
		List<SparqlEntity> results = sparqlDAO.query(selectScintPerson);
		
		for (SparqlEntity result : results) {
			Assert.assertEquals("Aoki", result.getValue("familyName"));
			Assert.assertEquals("Nobu", result.getValue("givenName"));
			Assert.assertEquals("support@glytoucan.org", result.getValue("email"));
		}
	}

	@Test
	@Transactional
	public void testInsertDomain() throws SparqlException {
		SparqlEntity sparqlentity = new SparqlEntity("person123");
		sparqlentity.setValue("familyName", "Aoki");
		sparqlentity.setValue("givenName", "Nobu");
		sparqlentity.setValue("email", "support@glytoucan.org");
//		insertScintPerson.setClassHandler(getPersonClassHandler());
		insertScintPerson.setSparqlEntity(sparqlentity);

		logger.debug(insertScintPerson.getSparql());
		sparqlDAO.insert(insertScintPerson);
	}

	@Test
	@Transactional
	public void testInsertSelectPerson() throws SparqlException {
		SparqlEntity sparqlentity = new SparqlEntity("person1234");
		sparqlentity.setValue("familyName", "person1234familyName");
		sparqlentity.setValue("givenName", "person1234givenName");
		sparqlentity.setValue("email", "person1234@test.org");
//		insertScintPerson.setClassHandler(getPersonClassHandler());
		insertScintPerson.setSparqlEntity(sparqlentity);

		logger.debug(insertScintPerson.getSparql());
		sparqlDAO.insert(insertScintPerson);

		SparqlEntity sparqlentitySelect = new SparqlEntity("person1234");
		selectScintPerson.setSparqlEntity(sparqlentitySelect);
		List<SparqlEntity> results = sparqlDAO.query(selectScintPerson);
		
		logger.debug(results.size());
		Assert.assertTrue(results.size() == 1);
		
		boolean pass=false;
		for (SparqlEntity result : results) {
			if (!pass) {
				logger.debug("givenName:>" + result.getValue("givenName") + "<");
				if (result.getValue("givenName").equals("person1234givenName")) {
					pass = true;
				}
			}
		}
		Assert.assertTrue(pass);
	}

	
	@Test
	@Transactional
	public void testSelectRegisterAction() throws SparqlException {
		testInsertRegisterAction();
		SparqlEntity sparqlentity = new SparqlEntity("register123");
		sparqlentity.setValue("startTime", "");
		selectScintRegisterAction.setSparqlEntity(sparqlentity);
		logger.debug(selectScintRegisterAction.getSparql());
		List<SparqlEntity> results = sparqlDAO.query(selectScintRegisterAction);
		
		boolean pass=false;
		for (SparqlEntity result : results) {
			TimeZone tz = TimeZone.getTimeZone("UTC");
			DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
			df.setTimeZone(tz);
			String nowAsISO = df.format(new Date(0));
			if (!pass) {
				logger.debug("startTime:>" + result.getValue("startTime") + "<");
				logger.debug("startTime:>" + "<http://schema.org/DateTime#" + nowAsISO + "><");
				if (result.getValue("startTime").equals("http://schema.org/DateTime#" + nowAsISO + "")) {
					pass = true;
				}
			}
		}
		Assert.assertTrue(pass);
	}

	@Test
	@Transactional
	public void testInsertRegisterAction() throws SparqlException {
		// RegisterAction entity
		SparqlEntity sparqlentityRegisterAction = new SparqlEntity("register123");
		
		// new DateTime Class
		SelectScint dateTimeSelect = new SelectScint();
		dateTimeSelect.setClassHandler(getDateTimeClassHandler());
		
		// DateTime entity
		SparqlEntity sparqlentityDateTime = new SparqlEntity(new Date(0));
		dateTimeSelect.setSparqlEntity(sparqlentityDateTime);
		
		// set the datetime class to be the startTime range for the registeraction domain.
		sparqlentityRegisterAction.setValue("startTime", dateTimeSelect);
		
		SparqlEntity sparqlEntityPerson = new SparqlEntity("person123");
		SelectScint personSelect = new SelectScint();
		personSelect.setClassHandler(getPersonClassHandler());
		personSelect.setSparqlEntity(sparqlEntityPerson);
		
		sparqlentityRegisterAction.setValue("participant", personSelect);

		// set the sparqlentity for the registeraction. 
		insertScintRegisterAction.setClassHandler(getRegisterActionClassHandler());
		insertScintRegisterAction.setSparqlEntity(sparqlentityRegisterAction);
		logger.debug(insertScintRegisterAction.getSparql());
		
		sparqlDAO.insert(insertScintRegisterAction);
	}
	
	
	@Test
	@Transactional
	public void testInsertProgramMembership() throws SparqlException {
		// ProgramMembership insertion
		// testPerson a Person
		// testGlytoucanPartnerProgramMembership a ProgramMembership
		// testGlytoucanPartnerProgramMembership programName "glytoucan partnership"
		// testPerson memberOf testGlycoinfoPartnerProgramMembership 
		// testGlytoucanPartnerProgramMembership hostingOrganization GlytoucanOrganization
		// testGlycoinfoPartnerProgramMembership memberShipNumber "123hash"

		
		SparqlEntity sparqlEntityPerson = new SparqlEntity("person123");
		InsertScint personScint = new InsertScint("http://rdf.glytoucan.org/schema/users");
		personScint.setClassHandler(getPersonClassHandler());
		personScint.setSparqlEntity(sparqlEntityPerson);

		logger.debug(personScint.getSparql());

		
		// ProgramMembership entity
		SparqlEntity sparqlentityProgramMembership = new SparqlEntity("partner123" + sparqlEntityPerson.getValue(SelectScint.PRIMARY_KEY));
		sparqlentityProgramMembership.setValue("programName", "Glytoucan Partner");
		
		sparqlentityProgramMembership.setValue("member", personScint);
		
		Date dateVal = new Date();
		
		sparqlentityProgramMembership.setValue("membershipNumber", NumberGenerator.generateHash(sparqlEntityPerson.getValue(SelectScint.PRIMARY_KEY) + sparqlentityProgramMembership.getValue("programName") + sparqlentityProgramMembership.getValue(SelectScint.PRIMARY_KEY) + "SEED", dateVal));

		InsertScint insertScintProgramMembership = new InsertScint("http://rdf.glytoucan.org/schema/users");
		// set the sparqlentity for the registeraction. 
		insertScintProgramMembership.setClassHandler(getProgramMembershipClassHandler());
		insertScintProgramMembership.setSparqlEntity(sparqlentityProgramMembership);
		logger.debug(insertScintProgramMembership.getSparql());
		sparqlDAO.insert(insertScintProgramMembership);
		
		sparqlEntityPerson.setValue("memberOf", insertScintProgramMembership);
		personScint.setSparqlEntity(sparqlEntityPerson);
		logger.debug(personScint.getSparql());
		sparqlDAO.insert(personScint);

	}
	
}