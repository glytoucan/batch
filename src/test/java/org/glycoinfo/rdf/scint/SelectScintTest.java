package org.glycoinfo.rdf.scint;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.dao.virt.VirtSesameTransactionConfig;
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

		private static final String graph = "http://rdf.glytoucan.org/schema/users";
	
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

	@Autowired
	@Qualifier(value = "programMembershipSelect")
	SelectScint programMembershipSelect;
	
	@Autowired
	@Qualifier(value = "programMembershipInsert")
	InsertScint programMembershipInsert;
	
	
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
		SelectScint select = new SelectScint("schema", "http://schema.org/", "Person");
//		select.setClassHandler(getPersonClassHandler());
		return select;
	}

	@Bean(name = "insertscintperson")
	InsertScint getInsertPersonScint() throws SparqlException {
		InsertScint insert = new InsertScint("schema", "http://schema.org/", "Person");
		insert.getSparqlBean().setGraph("http://rdf.glytoucan.org/users");
//		insert.setClassHandler(getPersonClassHandler());
		return insert;
	}
	
	@Bean(name = "selectscintregisteraction")
	SelectScint getSelectRegisterActionScint() throws SparqlException {
		return new SelectScint("schema", "http://schema.org/", "RegisterAction");
	}

	@Bean(name = "insertscintregisteraction")
	InsertScint getInsertRegisterActionScint() throws SparqlException {
		InsertScint insert = new InsertScint("schema", "http://schema.org/", "RegisterAction");
		insert.getSparqlBean().setGraph("http://rdf.glytoucan.org/users");
		return insert;
	}
	
	@Bean(name = "programMembershipSelect")
	SelectScint programMembershipSelect() throws SparqlException {
		return new SelectScint("schema", "http://schema.org/", "ProgramMembership");
	}

	@Bean(name = "programMembershipInsert")
	InsertScint programMembershipInsert() throws SparqlException {
		return new InsertScint("schema", "http://schema.org/", "ProgramMembership");
	}
	
	@Test
	public void testSelectDomain() throws SparqlException {
		SparqlEntity sparqlentity = new SparqlEntity("person123");
		sparqlentity.setValue("familyName", "");
		sparqlentity.setValue("givenName", "");
		sparqlentity.setValue("email", "support@glytoucan.org");
		selectScintPerson.setSparqlEntity(sparqlentity);
		logger.debug(selectScintPerson.getSparqlBean().getSparql());
		List<SparqlEntity> results = sparqlDAO.query(selectScintPerson.getSparqlBean());
		
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
		insertScintPerson.getSparqlBean().setSparqlEntity(sparqlentity);

		logger.debug(insertScintPerson.getSparqlBean().getSparql());
		sparqlDAO.insert(insertScintPerson.getSparqlBean());
	}

	@Test
	@Transactional
	public void testInsertSelectPerson() throws SparqlException {
		SparqlEntity sparqlentity = new SparqlEntity("person1234");
		sparqlentity.setValue("familyName", "person1234familyName");
		sparqlentity.setValue("givenName", "person1234givenName");
		sparqlentity.setValue("email", "person1234@test.org");
//		insertScintPerson.setClassHandler(getPersonClassHandler());
		insertScintPerson.getSparqlBean().setSparqlEntity(sparqlentity);

		logger.debug(insertScintPerson.getSparqlBean().getSparql());
		sparqlDAO.insert(insertScintPerson.getSparqlBean());

		SparqlEntity sparqlentitySelect = new SparqlEntity("person1234");
		sparqlentitySelect.setValue(SelectScint.NO_DOMAINS, SelectSparql.TRUE);
		sparqlentitySelect.setValue("givenName", null);

		selectScintPerson.setSparqlEntity(sparqlentitySelect);
		List<SparqlEntity> results = sparqlDAO.query(selectScintPerson.getSparqlBean());
		
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
		logger.debug(selectScintRegisterAction.getSparqlBean().getSparql());
		List<SparqlEntity> results = sparqlDAO.query(selectScintRegisterAction.getSparqlBean());
		
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
		SelectScint dateTimeSelect = new SelectScint("schema", "http://schema.org/", "DateTime");
//		dateTimeSelect.setClassHandler(getDateTimeClassHandler());
		
		// DateTime entity
		SparqlEntity sparqlentityDateTime = new SparqlEntity(new Date(0));
		dateTimeSelect.setSparqlEntity(sparqlentityDateTime);
		
		// set the datetime class to be the startTime range for the registeraction domain.
		sparqlentityRegisterAction.setValue("startTime", dateTimeSelect);
		
		SparqlEntity sparqlEntityPerson = new SparqlEntity("person123");
		SelectScint personSelect = new SelectScint("schema", "http://schema.org/", "Person");
		personSelect.setSparqlEntity(sparqlEntityPerson);
		
		sparqlentityRegisterAction.setValue("participant", personSelect);

		// set the sparqlentity for the registeraction. 
//		insertScintRegisterAction.getSparqlBean().setClassHandler(getRegisterActionClassHandler());
		insertScintRegisterAction.getSparqlBean().setSparqlEntity(sparqlentityRegisterAction);
		logger.debug(insertScintRegisterAction.getSparqlBean().getSparql());
		
		sparqlDAO.insert(insertScintRegisterAction.getSparqlBean());
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
//		InsertScint personScint = new InsertScint(graph);
//		getInsertPersonScint().setClassHandler(getPersonClassHandler());
		getInsertPersonScint().getSparqlBean().setSparqlEntity(sparqlEntityPerson);

		logger.debug(getInsertPersonScint().getSparqlBean().getSparql());

		
		// ProgramMembership entity
		SparqlEntity sparqlentityProgramMembership = new SparqlEntity("partner123" + sparqlEntityPerson.getValue(SelectSparql.PRIMARY_KEY));
		sparqlentityProgramMembership.setValue("programName", "Glytoucan Partner");
		
		sparqlentityProgramMembership.setValue("member", getInsertPersonScint());
		
		Date dateVal = new Date();
		
		sparqlentityProgramMembership.setValue("membershipNumber", NumberGenerator.generateHash(sparqlEntityPerson.getValue(SelectSparql.PRIMARY_KEY) + sparqlentityProgramMembership.getValue("programName") + sparqlentityProgramMembership.getValue(SelectSparql.PRIMARY_KEY) + "SEED", dateVal));

//		InsertScint insertScintProgramMembership = new InsertScint("http://rdf.glytoucan.org/schema/users");
		// set the sparqlentity for the registeraction. 
//		programMembershipInsert.setClassHandler(getProgramMembershipClassHandler());
		programMembershipInsert.getSparqlBean().setSparqlEntity(sparqlentityProgramMembership);
		logger.debug(programMembershipInsert.getSparqlBean().getSparql());
		sparqlDAO.insert(programMembershipInsert.getSparqlBean());
		
		sparqlEntityPerson.setValue("email", "person1234@test.org");
		sparqlEntityPerson.setValue("memberOf", programMembershipInsert);
		getInsertPersonScint().getSparqlBean().setSparqlEntity(sparqlEntityPerson);
		logger.debug(getInsertPersonScint().getSparqlBean().getSparql());
		sparqlDAO.insert(getInsertPersonScint().getSparqlBean());

	}
	
	@Test
	@Transactional
	public void testSelectUri() throws SparqlException {
		SparqlEntity sparqlentity = new SparqlEntity("person1234");
		sparqlentity.setValue("familyName", "person1234familyName");
		sparqlentity.setValue("givenName", "person1234givenName");
		sparqlentity.setValue("email", "person1234@test.org");
//		insertScintPerson.setClassHandler(getPersonClassHandler());
		insertScintPerson.getSparqlBean().setSparqlEntity(sparqlentity);

		logger.debug(insertScintPerson.getSparqlBean().getSparql());
		sparqlDAO.insert(insertScintPerson.getSparqlBean());

		SparqlEntity sparqlEntityPerson = new SparqlEntity();
		sparqlEntityPerson.setValue("email", "person1234@test.org");
		sparqlEntityPerson.setValue("givenName", null);
		sparqlEntityPerson.setValue(SelectScint.NO_DOMAINS, SelectSparql.TRUE);
		selectScintPerson.setSparqlEntity(sparqlEntityPerson);

		List<SparqlEntity> results = sparqlDAO.query(selectScintPerson.getSparqlBean());
		
		Assert.assertNotNull(results);
		SparqlEntity se = results.iterator().next();
		Assert.assertNotNull(se.getValue(SelectSparql.URI));
	}
	
	@Test
	@Transactional
	public void testSelectUriContributorId() throws SparqlException {
		SparqlEntity sparqlentity = new SparqlEntity("person1234");
		sparqlentity.setValue("familyName", "person1234familyName");
		sparqlentity.setValue("givenName", "person1234givenName");
		sparqlentity.setValue("email", "person1234@test.org");
		sparqlentity.setValue("alternateName", "456");
//		insertScintPerson.setClassHandler(getPersonClassHandler());
		insertScintPerson.getSparqlBean().setSparqlEntity(sparqlentity);

		logger.debug(insertScintPerson.getSparqlBean().getSparql());
		sparqlDAO.insert(insertScintPerson.getSparqlBean());

		SparqlEntity sparqlEntityPerson = new SparqlEntity();
		sparqlEntityPerson.setValue("alternateName", "456");
		sparqlEntityPerson.setValue("givenName", null);
		sparqlEntityPerson.setValue("email", null);
		sparqlEntityPerson.setValue(SelectScint.NO_DOMAINS, SelectSparql.TRUE);
		selectScintPerson.setSparqlEntity(sparqlEntityPerson);

		List<SparqlEntity> results = sparqlDAO.query(selectScintPerson.getSparqlBean());
		
		Assert.assertNotNull(results);
		SparqlEntity se = results.iterator().next();
		Assert.assertNotNull(se.getValue(SelectSparql.URI));
		Assert.assertNotNull(se.getValue("alternateName"));
		Assert.assertNotNull(se.getValue("givenName"));
		Assert.assertNotNull(se.getValue("email"));
	}
	
	@Test
	@Transactional
	public void testSelectProgramMembershipUsingEmail() throws SparqlException {
		// insert person and put into membership
		testInsertProgramMembership();
		
		// person by email
		SparqlEntity sparqlEntityPerson = new SparqlEntity();
		sparqlEntityPerson.setValue("email", "person1234@test.org");
		sparqlEntityPerson.setValue("memberOf", null);
//		sparqlEntityPerson.setValue(SelectScint.NO_DOMAINS, SelectSparql.TRUE);
		selectScintPerson.setSparqlEntity(sparqlEntityPerson);

		List<SparqlEntity> results = sparqlDAO.query(selectScintPerson.getSparqlBean());
		
		Assert.assertNotNull(results);
		SparqlEntity se = results.iterator().next();
		Assert.assertNotNull(se.getValue(SelectSparql.URI));
		Assert.assertTrue(StringUtils.isNotBlank(se.getValue("memberOf")));
		
//		programMembershipSelect = new SelectScint("schema", "http://schema.org/", "ProgramMembership");
//		programMembershipSelect.setClassHandler(getProgramMembershipClassHandler());
		selectScintPerson.setSparqlEntity(se);
		SparqlEntity pmSE = new SparqlEntity();
		pmSE.setValue("member", selectScintPerson);
		programMembershipSelect.setSparqlEntity(pmSE);
		
		List<SparqlEntity> resultsPM = sparqlDAO.query(programMembershipSelect.getSparqlBean());
		Assert.assertNotNull(resultsPM);
		SparqlEntity pmResultsSE = resultsPM.iterator().next();
		Assert.assertNotNull(pmResultsSE.getValue(SelectSparql.URI));
	}
}