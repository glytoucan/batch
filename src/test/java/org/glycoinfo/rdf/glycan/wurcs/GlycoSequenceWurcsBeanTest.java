package org.glycoinfo.rdf.glycan.wurcs;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.glycoinfo.rdf.InsertSparql;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.dao.VirtSesameDAOTestConfig;
import org.glycoinfo.rdf.glycan.GlycoSequence;
import org.glycoinfo.rdf.glycan.Saccharide;
import org.glycoinfo.rdf.glycan.SaccharideSelectSparql;
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
	ComponentGroupSelectSparql componentGroupSelectSparql() {
		ComponentGroupSelectSparql cgs = new ComponentGroupSelectSparql();
		SparqlEntity se = new SparqlEntity();
		se.setValue(Saccharide.PrimaryId, "G00030MO");
		cgs.setSparqlEntity(se);
		return cgs;
	}
	
	@Bean
	GlycoSequenceResourceEntryContributorSelectSparql getGlycoSequenceResourceEntryContributorSelectSparql() {
		GlycoSequenceResourceEntryContributorSelectSparql sb = new GlycoSequenceResourceEntryContributorSelectSparql();
		SparqlEntity sparqlentity = new SparqlEntity();
		sparqlentity.setValue(Saccharide.PrimaryId, "G00031MO");
		sb.setSparqlEntity(sparqlentity);

		sb.setFrom("FROM <http://rdf.glytoucan.org>\nFROM <http://rdf.glytoucan.org/sequence/wurcs>");
		return sb;
	}
	
	@Bean
	WurcsRDFInsertSparql wurcsRDFInsertSparql() {
		WurcsRDFInsertSparql wrdf = new WurcsRDFInsertSparql();
		SparqlEntity se = new SparqlEntity();
		se.setValue(Saccharide.PrimaryId, "G00012MO");
		se.setValue(GlycoSequence.Sequence, "WURCS=2.0/4,4,3/[u2122h][a2112h-1b_1-5][a2112h-1a_1-5][a2112h-1b_1-5_2*NCC/3=O]/1-2-3-4/a4-b1_b3-c1_c3-d1");
		wrdf.setSparqlEntity(se);
		
		wrdf.setGraph("http://test");
		return wrdf;
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

	@Test
	@Transactional
	public void testInsert() throws SparqlException, UnsupportedEncodingException {

		sparqlDAO.insert(wurcsRDFInsertSparql());
		
//		List<SparqlEntity> list = sparqlDAO.query(wurcsRDFSelectSparql());
//		if (list.iterator().hasNext()) {
//			SparqlEntity se = list.iterator().next();
//			logger.debug(se.getValue(SaccharideSelectSparql.SaccharideURI));
//		}
	}

	private SelectSparql wurcsRDFSelectSparql() {
		WurcsRDFSelectSparql wrss = new WurcsRDFSelectSparql();
		SparqlEntity se = new SparqlEntity();
//		se.setValue(Saccharide.PrimaryId, "G00012MO");
		se.setValue(GlycoSequence.Sequence, "WURCS=2.0/4,4,3/[u2122h][a2112h-1b_1-5][a2112h-1a_1-5][a2112h-1b_1-5_2*NCC/3=O]/1-2-3-4/a4-b1_b3-c1_c3-d1");
		wrss.setSparqlEntity(se);
		return wrss;
	}
	
	
	@Test
	@Transactional
	public void testInsertMS() throws SparqlException, UnsupportedEncodingException {

		sparqlDAO.insert(wurcsRDFMSInsertSparql());
		
		List<SparqlEntity> list = sparqlDAO.query(wurcsRDFSelectSparql());
		if (list.iterator().hasNext()) {
			SparqlEntity se = list.iterator().next();
			logger.debug(se.getValue(SaccharideSelectSparql.SaccharideURI));
		}
	}

	@Test
	@Transactional
	public void testSelectComponentGroup() throws SparqlException, UnsupportedEncodingException {
		List<SparqlEntity> list = sparqlDAO.query(componentGroupSelectSparql());
		if (list.iterator().hasNext()) {
			SparqlEntity se = list.iterator().next();
			logger.debug(se.getValue("count"));
		}
	}

	
	private InsertSparql wurcsRDFMSInsertSparql() {
		WurcsRDFMSInsertSparql wrss = new WurcsRDFMSInsertSparql();
		wrss.setGraph("http://rdf.test.glycoinfo.org");
		SparqlEntity se = new SparqlEntity();
//		se.setValue(Saccharide.PrimaryId, "G00012MO");
		se.setValue(GlycoSequence.Sequence, "WURCS=2.0/4,4,3/[u2122h][a2112h-1b_1-5][a2112h-1a_1-5][a2112h-1b_1-5_2*NCC/3=O]/1-2-3-4/a4-b1_b3-c1_c3-d1");
		wrss.setSparqlEntity(se);
		return wrss;
	}
	
	@Test
	@Transactional
	public void testSelectMS() throws SparqlException, UnsupportedEncodingException {
		List<SparqlEntity> list = sparqlDAO.query(monosaccharideSelectSparql());
		if (list.size() < 1)
			Assert.fail();
		for (SparqlEntity sparqlEntity : list) {
			logger.debug(sparqlEntity.getValue(monosaccharideSelectSparql().MonosaccharideDBURI));
		}
	}
	
	@Bean
	MonosaccharideSelectSparql monosaccharideSelectSparql() {
		MonosaccharideSelectSparql sb = new MonosaccharideSelectSparql();
		SparqlEntity sparqlentity = new SparqlEntity();
		sparqlentity.setValue(Saccharide.PrimaryId, "G00031MO");
		sb.setSparqlEntity(sparqlentity);
		return sb;
	}

}