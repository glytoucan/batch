package org.glycoinfo.rdf.service;

import java.util.List;

import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.dao.virt.VirtSesameTransactionConfig;
import org.glycoinfo.rdf.scint.Scintillate;
import org.glycoinfo.rdf.scint.SelectScint;
import org.glycoinfo.rdf.service.impl.GlycanProcedureConfig;
import org.glycoinfo.rdf.service.impl.UserProcedureConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {UserProcedureTest.class, VirtSesameTransactionConfig.class, GlycanProcedureConfig.class, UserProcedureConfig.class })
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

	@Autowired
	UserProcedure userProcedure;

	@Autowired
	@Qualifier(value = "selectscintperson")
	SelectScint selectScintPerson;

	@Test(expected=SparqlException.class)
	@Transactional
	public void testInsufficientUser() throws SparqlException {
		SparqlEntity se = new SparqlEntity();
		se.setValue("id", "person456");
		userProcedure.add(se);
	}
	
	@Test
	@Transactional
	public void testUser() throws SparqlException {
		SparqlEntity se = new SparqlEntity();
		se.setValue(SelectSparql.PRIMARY_KEY, "person789");
		se.setValue("email", "person789@person.com");
		se.setValue("givenName", "person");
		se.setValue("familyName", "789");
		se.setValue("verifiedEmail", "true");
		userProcedure.add(se);

		se.setValue("member", "");
		se.setValue("contributor", "");
		se.remove("verifiedEmail");
		se.setValue(Scintillate.NO_DOMAINS, SelectSparql.TRUE);

		SelectScint personScint = selectScintPerson;
		personScint.update(se);
		List<SparqlEntity> results = sparqlDAO.query(personScint.getSparqlBean());
		
		Assert.assertFalse(results.size() == 0);
		for (SparqlEntity sparqlEntity : results) {
			logger.debug(sparqlEntity.toString());
			Assert.assertNotNull(sparqlEntity.getValue("contributor"));
			Assert.assertNotNull(sparqlEntity.getValue("member"));
		}
	}

	@Test
	@Transactional
	public void testUserNotVerified() throws SparqlException {
		SparqlEntity se = new SparqlEntity("person456");
		se.setValue("email", "person456@person.com");
		se.setValue("givenName", "person");
		se.setValue("familyName", "456");
		se.setValue("verifiedEmail", "false");
		userProcedure.add(se);
		se.setValue("member", null);
		se.remove("verifiedEmail");
		se.setValue(Scintillate.NO_DOMAINS, SelectSparql.TRUE);
		
		selectScintPerson.update(se);
		List<SparqlEntity> results = sparqlDAO.query(selectScintPerson.getSparqlBean());
		
		Assert.assertFalse(results.size() == 0);
		for (SparqlEntity sparqlEntity : results) {
			logger.debug(sparqlEntity.toString());
			Assert.assertNull(sparqlEntity.getValue("contributor"));
			Assert.assertNotNull(sparqlEntity.getValue("member"));
		}
	}
	
//	@Test see joinmembership
//	public void testGetUser() throws SparqlException {
//		SparqlEntity results = userProcedure.getUser("aokinobu@gmail.com");
//		Assert.assertNotNull(results);
//		Assert.assertNotNull(results.getValue("familyName"));
//		Assert.assertNotNull(results.getValue("Name"));
//		Assert.assertNotNull(results.getValue("givenName"));
//		Assert.assertNotNull(results.getValue("alternateName"));
//	}

	@Test(expected=SparqlException.class)
	@Transactional
	public void testJoinBadMembership() throws SparqlException {
		String results = userProcedure.generateHash("person123@test.com");
		Assert.assertNotNull(results);
	}
	
	@Test
	@Transactional
	public void testJoinMembership() throws SparqlException {
		SparqlEntity se = new SparqlEntity();
		se.setValue(SelectSparql.PRIMARY_KEY, "person789");
		se.setValue(UserProcedure.EMAIL, "person789@person.com");
		se.setValue(UserProcedure.GIVEN_NAME, "testperson789given");
		se.setValue(UserProcedure.FAMILY_NAME, "testperson789family");
		se.setValue(UserProcedure.VERIFIED_EMAIL, "true");
		userProcedure.add(se);

		String results = userProcedure.generateHash("person789");
		Assert.assertNotNull(results);
		
		se = userProcedure.getById("person789");
		logger.debug(se.getData().toString());
		Assert.assertNotNull(se.getValue(UserProcedure.MEMBER_OF));
		Assert.assertNotNull(se.getValue(UserProcedure.MEMBERSHIP_NUMBER));
	}
	

	@Test
	@Transactional
	public void testJoinMembershipTwice() throws SparqlException {
		SparqlEntity se = new SparqlEntity();
		se.setValue(SelectSparql.PRIMARY_KEY, "person789");
		se.setValue(UserProcedure.EMAIL, "person789@person.com");
		se.setValue(UserProcedure.GIVEN_NAME, "testperson789given");
		se.setValue(UserProcedure.FAMILY_NAME, "testperson789family");
		se.setValue(UserProcedure.VERIFIED_EMAIL, "true");
		userProcedure.add(se);

		String results = userProcedure.generateHash("person789");
		Assert.assertNotNull(results);
		
		se = userProcedure.getById("person789");
		logger.debug(se.getData().toString());
		Assert.assertNotNull(se.getValue(UserProcedure.MEMBER_OF));
		Assert.assertNotNull(se.getValue(UserProcedure.MEMBERSHIP_NUMBER));
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String results2 = userProcedure.generateHash("person789");
		Assert.assertNotNull(results2);
		Assert.assertNotEquals(results, results2);
		se = userProcedure.getById("person789");
		logger.debug(se.getData().toString());
		Assert.assertNotNull(se.getValue(UserProcedure.MEMBER_OF));
		Assert.assertNotNull(se.getValue(UserProcedure.MEMBERSHIP_NUMBER));
	}
	
	
	@Test
	@Transactional
	public void testCheck() throws SparqlException {
		SparqlEntity se = new SparqlEntity();
		se.setValue(SelectSparql.PRIMARY_KEY, "person789");
		se.setValue(UserProcedure.EMAIL, "person789@person.com");
		se.setValue(UserProcedure.GIVEN_NAME, "testperson789given");
		se.setValue(UserProcedure.FAMILY_NAME, "testperson789family");
		se.setValue(UserProcedure.VERIFIED_EMAIL, "true");
		userProcedure.add(se);
		
		String hash = userProcedure.generateHash("person789");
		
		SparqlEntity sePerson = userProcedure.getById("person789");
		
		Assert.assertTrue(userProcedure.checkApiKey(sePerson.getValue(UserProcedure.CONTRIBUTOR_ID), hash));
	}

}