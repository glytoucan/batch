package org.glycoinfo.rdf.service;

import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.VirtSesameDAOTestConfig;
import org.glycoinfo.rdf.dao.virt.SparqlDAOVirtSesameImpl;
import org.glycoinfo.rdf.glycan.ContributorInsertSparql;
import org.glycoinfo.rdf.glycan.ContributorNameSelectSparql;
import org.glycoinfo.rdf.service.impl.ContributorProcedureRdf;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author aoki
 *
 * This work is licensed under the Creative Commons Attribution 4.0 International License. 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by/4.0/.
 *
 */
//@SpringApplicationConfiguration(classes = UserProcedureRdfTest.class)
//@ComponentScan(basePackages = {"org.glycoinfo.rdf.service"})
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {ContributorProcedureRdfTest.class, VirtSesameDAOTestConfig.class})
//@ComponentScan(basePackages = {"org.glycoinfo.rdf.service"})
//@ComponentScan(basePackages = {"org.glycoinfo.rdf"}, excludeFilters={
//		  @ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE, value=Configuration.class)})
@Configuration
@EnableAutoConfiguration
public class ContributorProcedureRdfTest extends AbstractTransactionalJUnit4SpringContextTests {

	@Autowired
	ContributorProcedure contributorProcedure;
	
	@Bean
	SparqlDAO getSparqlDAO() {
		return new SparqlDAOVirtSesameImpl();
	}
	
	@Bean
	ContributorInsertSparql getContributorInsertSparql() {
		ContributorInsertSparql c = new ContributorInsertSparql();
		c.setGraph("http://rdf.glcoinfo.org/contributors");
		return c;
	}
	
	@Bean
	ContributorNameSelectSparql getContributorNameSelectSparql() {
		ContributorNameSelectSparql selectbyNameContributor = new ContributorNameSelectSparql();
		selectbyNameContributor.setFrom("FROM <http://rdf.glcoinfo.org/contributors>");
		return selectbyNameContributor;
	}
	
	@Bean
	public ContributorProcedure getContributorProcedure() {
		return (ContributorProcedure) new ContributorProcedureRdf();
	}
	
	@Test
	@Transactional
	public void testAddContributor() throws SparqlException {
		contributorProcedure.setName("test");
		String id = contributorProcedure.addContributor();
		Assert.assertNotNull(id);
	}
	
	@Test
	@Transactional
	public void testAddSearch() throws SparqlException {
		contributorProcedure.setName("test2");
		String id = contributorProcedure.addContributor();
		Assert.assertNotNull(id);
		
		contributorProcedure.setName("test2");
		String id2 = contributorProcedure.searchContributor();
		
		Assert.assertEquals(id, id2);
	}
}