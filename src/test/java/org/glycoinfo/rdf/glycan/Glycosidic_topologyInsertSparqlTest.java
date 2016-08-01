package org.glycoinfo.rdf.glycan;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.glycoinfo.rdf.SelectSparqlBean;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.dao.virt.VirtSesameTransactionConfig;
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
@SpringApplicationConfiguration(classes = {Glycosidic_topologyInsertSparqlTest.class, VirtSesameTransactionConfig.class })
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
		sparqlentity.setValue(Glycosidic_topology.PrimaryId_1, "G14728XI");
		sparqlentity.setValue(Glycosidic_topology.PrimaryId_2, "G24678II");
		ins.setSparqlEntity(sparqlentity);
		ins.setGraph("http://rdf.glytoucan.org/topology");
		return ins;
	}

	//先に空を出させて、インサートして、セレクトもう一回する	
	@Bean
	D3SequenceSelectSparql_has_topology getToplogySelectSparql() {
		D3SequenceSelectSparql_has_topology sis = new D3SequenceSelectSparql_has_topology();
		SparqlEntity sparqlentity = new SparqlEntity();
		sparqlentity.setValue(Glycosidic_topology.PrimaryId_1, "G14728XI");
		sis.setSparqlEntity(sparqlentity);
		return sis;
	}
	
	@Bean
	D3SequenceSelectSparql_topology_by getToplogySelectSparql_toplogy_by() {
		D3SequenceSelectSparql_topology_by sis = new D3SequenceSelectSparql_topology_by();
		SparqlEntity sparqlentity = new SparqlEntity();
		sparqlentity.setValue(Glycosidic_topology.PrimaryId_2, "G24678II");
		sis.setSparqlEntity(sparqlentity);
		return sis;
	}
	
	@Test
	public void testInsertSparql() throws SparqlException {
		logger.debug(getGlycosidic_topologyInsertSparql().getSparql());
		
//		assertEquals(
//				"PREFIX rocs: <http://www.glycoinfo.org/glyco/owl/relation#>\n"
//				+ "INSERT DATA\n"
//				+ "{ GRAPH <http://rdf,glytoucan.org/topology>\n"
//				+ "{ <http://rdf.glycoinfo.org/glycan/G14728XI>\n"
//				+ " rocs:has_topology <http://rdf.glycoinfo.org/glycan/G24678II> .\n"
//				+ "<http://rdf.glycoinfo.org/glycan/L2primaryid>\n"
//				+ " a rocs:Glycosidic_topology .\n"
//				+ " }\n"
//				+ "}\n",
//				getGlycosidic_topologyInsertSparql().getSparql());
		
	}
	
	@Test
	@Transactional
	public void insertSparql() throws SparqlException {
		sparqlDAO.insert(getGlycosidic_topologyInsertSparql());
		
		List<SparqlEntity> list = sparqlDAO.query(getToplogySelectSparql());
		for (SparqlEntity sparqlEntity : list) {
			String output = sparqlEntity.getValue("s");
			//Assert.assertEquals("http://purl.jp/bio/12/glyco/glycan#saccharide", output);
		}
	}

	@Test
	@Transactional
	public void insertSelectSparql() throws SparqlException {
		sparqlDAO.insert(getGlycosidic_topologyInsertSparql());
		
		List<SparqlEntity> list = sparqlDAO.query(getToplogySelectSparql());
		if (list.iterator().hasNext()) {
			SparqlEntity se = list.iterator().next();
			if ("G24678II".equals(se.getValue("topology_id")))
				Assert.assertTrue("pass",true);
		}
	}
	
	@Test
	@Transactional
	public void insertSelectSparql_topology_by() throws SparqlException {
		sparqlDAO.insert(getGlycosidic_topologyInsertSparql());
		
		List<SparqlEntity> list = sparqlDAO.query(getToplogySelectSparql_toplogy_by());
		if (list.iterator().hasNext()) {
			SparqlEntity se = list.iterator().next();
			if ("G14728XI".equals(se.getValue("id")))
				Assert.assertTrue("pass",true);
		}
	}
	
	@Test
	public void selectSparql() throws SparqlException {
		D3SequenceSelectSparql_has_topology sss = getToplogySelectSparql();
		SparqlEntity se = sss.getSparqlEntity();
		se.setValue(Glycosidic_topology.PrimaryId_1, "G00031MO");
		
		sss.setSparqlEntity(se);
		
		List<SparqlEntity> list = sparqlDAO.query(sss);
		if (list.iterator().hasNext()) {
			se = list.iterator().next();
			logger.debug(se.getValue(D3SequenceSelectSparql_has_topology.SaccharideURI));
		}
	}
}