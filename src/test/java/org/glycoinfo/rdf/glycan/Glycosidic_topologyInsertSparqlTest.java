package org.glycoinfo.rdf.glycan;

import static org.junit.Assert.assertEquals;

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

/**
 * 
 * Test the insert sparql Glycosidic topology.
 * 
 * @author tokunaga
 *
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {Glycosidic_topologyInsertSparqlTest.class, VirtSesameDAOTestConfig.class })
@Configuration
@EnableAutoConfiguration
public class Glycosidic_topologyInsertSparqlTest {

	public static Logger logger = (Logger) LoggerFactory
			.getLogger(Glycosidic_topologyInsertSparqlTest.class);
	
	@Autowired
	SparqlDAO sparqlDAO;

	@Bean
	Glycosidic_topologyInsertSparql getGlycosidic_topologyInsertSparql() {
		Glycosidic_topologyInsertSparql ins = new Glycosidic_topologyInsertSparql();
		SparqlEntity sparqlentity = new SparqlEntity();
		sparqlentity.setValue(Glycosidic_topology.URI, "insertsacharideuri");
		sparqlentity.setValue(GlycoSequence.URI, "glycosequenceuri");
		sparqlentity.setValue(Glycosidic_topology.PrimaryId_1, "L1primaryid1");
		sparqlentity.setValue(Glycosidic_topology.PrimaryId_2, "L2primaryid");
		ins.setSparqlEntity(sparqlentity);
		ins.setGraph("http://test");
		return ins;
	}

	// 先に空を出させて、インサートして、セレクトもう一回する	
//	@Bean
//	SaccharideSelectSparql getSaccharideSelectSparql() {
//		SaccharideSelectSparql sis = new SaccharideSelectSparql();
//		SparqlEntity sparqlentity = new SparqlEntity();
//		sparqlentity.setValue(Saccharide.PrimaryId, "primaryid");
//		sis.setSparqlEntity(sparqlentity);
//		return sis;
//	}
	
	@Test
	public void testInsertSparql() throws SparqlException {
		logger.debug(getGlycosidic_topologyInsertSparql().getSparql());
		
		assertEquals(
				"prefix glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
				+ "prefix rocs: <http://www.glycoinfo.org/glyco/owl/relation#>\n"
				+ "INSERT DATA\n"
				+ "{ GRAPH <http://test>\n"
				+ "{ <http://rdf.glycoinfo.org/glycan/L1primaryid1>\n"
				+ " rocs:subsumes <http://rdf.glycoinfo.org/glycan/L2primaryid> ;\n"
				+ " rocs:has_topology <http://rdf.glycoinfo.org/glycan/L2primaryid> .\n"
				+ "<http://rdf.glycoinfo.org/glycan/L2primaryid>\n"
				+ " a rocs:Glycosidic_topology ;\n"
				+ "rocs:subsumed_by <http://rdf.glycoinfo.org/glycan/L1primaryid1> .\n"
						+ " }\n"
						+ "}\n",
				getGlycosidic_topologyInsertSparql().getSparql());
		
	}
	
	@Test
	@Transactional
	public void insertSparql() throws SparqlException {
		sparqlDAO.insert(getGlycosidic_topologyInsertSparql());
		
//		List<SparqlEntity> list = sparqlDAO.query(new SelectSparqlBean("prefix glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
//				+ "select ?s where { <http://rdf.glycoinfo.org/glycan/primaryid> a ?s }"));
//		
//		for (SparqlEntity sparqlEntity : list) {
//			String output = sparqlEntity.getValue("s");
//			Assert.assertEquals("http://purl.jp/bio/12/glyco/glycan#saccharide", output);
//		}
	}

//	@Test
//	@Transactional
//	public void insertSelectSparql() throws SparqlException {
//		sparqlDAO.insert(getGlycosidic_topologyInsertSparql());
//		
//		List<SparqlEntity> list = sparqlDAO.query(getSaccharideSelectSparql());
//		if (list.iterator().hasNext()) {
//			SparqlEntity se = list.iterator().next();
//			logger.debug(se.getValue(SaccharideSelectSparql.SaccharideURI));
//		}
//	}
//	
//	@Test
//	public void selectSparql() throws SparqlException {
//		SaccharideSelectSparql sss = getSaccharideSelectSparql();
//		SparqlEntity se = sss.getSparqlEntity();
//		se.setValue(Saccharide.PrimaryId, "G00031MO");
//		
//		sss.setSparqlEntity(se);
//		
//		List<SparqlEntity> list = sparqlDAO.query(sss);
//		if (list.iterator().hasNext()) {
//			se = list.iterator().next();
//			logger.debug(se.getValue(SaccharideSelectSparql.SaccharideURI));
//		}
//	}
}