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

	@Autowired
	WurcsRDFInsertSparql wurcsRDFInsertSparqlNew;

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

		sb.setFrom("FROM <http://rdf.glytoucan.org/users>\nFROM <http://rdf.glytoucan.org>\nFROM <http://rdf.glytoucan.org/sequence/wurcs>\nFROM <http://rdf.glytoucan.org/sequence/glycoct>\nFROM <http://rdf.glytoucan.org/core>");
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

	
	@Bean
	WurcsRDFInsertSparql wurcsRDFInsertSparqlNew() {
		WurcsRDFInsertSparql wrdf = new WurcsRDFInsertSparql();
		SparqlEntity se = new SparqlEntity();
		se.setValue(Saccharide.PrimaryId, "G00012MO");
		se.setValue(GlycoSequence.Sequence, "WURCS=2.0/2,2,1/[a1122h-1x_1-5][a2112h-1x_1-5_2*NCC/3=O]/1-2/a?-b1");
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
	
//	WURCS=2.0/6,13,12/[a2122h-1x_1-5_2*NCC/3=O][a2122h-1b_1-5_2*NCC/3=O][a1122h-1b_1-5][a1122h-1a_1-5][a2112h-1b_1-5][Aad21122h-2a_2-6_5*NCC/3=O]/1-2-3-4-2-5-6-2-5-6-4-4-4/a4-b1_b4-c1_c3-d1_c6-k1_d2-e1_d4-h1_e4-f1_h4-i1_k3-l1_k6-m1_g2-f3|f6_j2-i3|i6

	private SelectSparql wurcsRDFSelectSparql() {
		WurcsRDFSelectSparql wrss = new WurcsRDFSelectSparql();
		SparqlEntity se = new SparqlEntity();
//		se.setValue(Saccharide.PrimaryId, "G00012MO");
		se.setValue(GlycoSequence.Sequence, "WURCS=2.0/4,4,3/[u2122h][a2112h-1b_1-5][a2112h-1a_1-5][a2112h-1b_1-5_2*NCC/3=O]/1-2-3-4/a4-b1_b3-c1_c3-d1");
		wrss.setSparqlEntity(se);
		return wrss;
	}
	
	
//	@Test
	@Transactional
	public void testInsertMS() throws SparqlException, UnsupportedEncodingException {

		sparqlDAO.insert(wurcsRDFMSInsertSparql());
		
		List<SparqlEntity> list = sparqlDAO.query(wurcsRDFSelectSparql());
		if (list.iterator().hasNext()) {
			SparqlEntity se = list.iterator().next();
			logger.debug(se.getValue(SaccharideSelectSparql.SaccharideURI));
		}
	}
	
//	@Test
	@Transactional
	public void testInsertMSBrokenSparql() throws SparqlException, UnsupportedEncodingException {
		InsertSparql wris = wurcsRDFMSInsertSparql();
		SparqlEntity se = new SparqlEntity();
		se.setValue(GlycoSequence.Sequence, "WURCS=2.0/7,14,13/[a2122h-1x_1-5_2*NCC/3=O][a2122h-1b_1-5_2*NCC/3=O][a1122h-1b_1-5][a1122h-1a_1-5][a2112h-1b_1-5][a1221m-1a_1-5][Aad21122h-2a_2-6_5*NCC/3=O]/1-2-3-4-2-5-2-5-6-4-2-5-6-7/a4-b1_a6-m1_b4-c1_c3-d1_c6-j1_d2-e1_d4-g1_e4-f1_g4-h1_h2-i1_j2-k1_k4-l1_n2-a3|b3|c3|d3|e3|f3|g3|h3|i3|j3|k3|l3|m3}");
		wris.setSparqlEntity(se);

		sparqlDAO.insert(wris);
		
		List<SparqlEntity> list = sparqlDAO.query(wurcsRDFSelectSparql());
		if (list.iterator().hasNext()) {
			se = list.iterator().next();
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
	
	@Test
	public void testFilter() throws SparqlException {
		GlycoSequenceFilterNoWurcsSelectSparql gsf = new GlycoSequenceFilterNoWurcsSelectSparql();
//		gsf.setLimit("1");
		List<SparqlEntity> list = sparqlDAO.query(gsf);
		if (list.size() < 1)
			Assert.fail();
		logger.debug(list.size() + "");
		for (SparqlEntity sparqlEntity : list) {
			logger.debug(sparqlEntity.getValue(GlycoSequence.Sequence));
		}
	}
	
	@Test
	@Transactional
	public void testInsertRdf() throws SparqlException, UnsupportedEncodingException {
		logger.debug(wurcsRDFInsertSparqlNew.getSparql());
	}
}