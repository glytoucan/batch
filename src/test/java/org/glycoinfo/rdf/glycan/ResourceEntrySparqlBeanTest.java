package org.glycoinfo.rdf.glycan;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import org.glycoinfo.rdf.SelectSparqlBean;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.dao.VirtSesameDAOTestConfig;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { ResourceEntrySparqlBeanTest.class , VirtSesameDAOTestConfig.class })
@Configuration
@EnableAutoConfiguration
public class ResourceEntrySparqlBeanTest {
	public static Logger logger = (Logger) LoggerFactory
			.getLogger(ResourceEntrySparqlBeanTest.class);
	
	@Autowired
	SparqlDAO sparqlDAO;

	@Bean
	ResourceEntryInsertSparql getResourceEntrySparql() {
		ResourceEntryInsertSparql ins = new ResourceEntryInsertSparql();
		SparqlEntity sparqlentity = new SparqlEntity();
		sparqlentity.setValue(ResourceEntry.AccessionNumber, "G00TESTDATA");
		sparqlentity.setValue(Saccharide.PrimaryId, "TEST");
		sparqlentity.setValue(ResourceEntry.ContributorId, "1234");
		sparqlentity.setValue(ResourceEntry.Database, "glytoucan");
		sparqlentity.setValue(ResourceEntry.DataSubmittedDate, new Date(0));
		
		ins.setSparqlEntity(sparqlentity);
		ins.setGraph("http://test");
		return ins;
	}

	@Test
	@Transactional
	public void testInsert() throws SparqlException, UnsupportedEncodingException {
		sparqlDAO.insert(getResourceEntrySparql());
		
		List<SparqlEntity> list = sparqlDAO.query(new SelectSparqlBean(getResourceEntrySparql().getPrefix() + "select ?date where {?resource a glycan:resource_entry . ?resource glycan:in_glycan_database glycan:database_glytoucan . ?resource dcterms:identifier \"G00TESTDATA\"^^xsd:string . ?resource dcterms:dataSubmitted ?date . ?resource dc:contributor <http://rdf.glycoinfo.org/glytoucan/contributor/" + "1234" + ">}"));
		for (SparqlEntity sparqlEntity : list) {
			Assert.assertEquals("Thu Jan 01 09:00:00 JST 1970", sparqlEntity.getValue("date"));
		}
	}
}