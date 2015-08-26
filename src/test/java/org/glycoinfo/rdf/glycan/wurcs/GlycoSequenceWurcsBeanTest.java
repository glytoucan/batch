package org.glycoinfo.rdf.glycan.wurcs;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.dao.VirtSesameDAOTestConfig;
import org.glycoinfo.rdf.glycan.Saccharide;
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
@SpringApplicationConfiguration(classes = { GlycoSequenceWurcsBeanTest.class , VirtSesameDAOTestConfig.class })
@Configuration
@EnableAutoConfiguration
public class GlycoSequenceWurcsBeanTest {
	public static Logger logger = (Logger) LoggerFactory
			.getLogger(GlycoSequenceWurcsBeanTest.class);
	
	@Autowired
	SparqlDAO sparqlDAO;

	@Bean
	GlycoSequenceResourceEntryContributorSelectSparql getGlycoSequenceResourceEntryContributorSelectSparql() {
		GlycoSequenceResourceEntryContributorSelectSparql sb = new GlycoSequenceResourceEntryContributorSelectSparql();
		SparqlEntity sparqlentity = new SparqlEntity();
		sparqlentity.setValue(Saccharide.PrimaryId, "G00031MO");
		sb.setSparqlEntity(sparqlentity);

		sb.setFrom("FROM <http://rdf.glytoucan.org>\nFROM <http://rdf.glytoucan.org/sequence/wurcs>");
		return sb;
	}

	@Test
	@Transactional
	public void testSelect() throws SparqlException, UnsupportedEncodingException {
		List<SparqlEntity> list = sparqlDAO.query(getGlycoSequenceResourceEntryContributorSelectSparql());
		if (list.size() < 1)
			Assert.fail();
		for (SparqlEntity sparqlEntity : list) {
			logger.debug(sparqlEntity.getValue("Sequence"));
		}
	}
}