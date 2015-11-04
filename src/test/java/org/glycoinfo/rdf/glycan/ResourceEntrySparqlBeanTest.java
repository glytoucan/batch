package org.glycoinfo.rdf.glycan;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.List;

import org.glycoinfo.rdf.InsertSparql;
import org.glycoinfo.rdf.SelectSparql;
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
@SpringApplicationConfiguration(classes = { ResourceEntrySparqlBeanTest.class, VirtSesameDAOTestConfig.class })
@Configuration
@EnableAutoConfiguration
public class ResourceEntrySparqlBeanTest {
	public static Logger logger = (Logger) LoggerFactory.getLogger(ResourceEntrySparqlBeanTest.class);

	@Autowired
	SparqlDAO sparqlDAO;

	@Bean
	ResourceEntryInsertSparql getResourceEntrySparql() {
		ResourceEntryInsertSparql ins = new ResourceEntryInsertSparql();
		ins.setGraph("http://test");
		return ins;
	}

	@Bean
	ResourceEntrySelectSparql resourceEntrySelectSparql() {
		SparqlEntity se = new SparqlEntity();
		se.setValue(ResourceEntry.Identifier, "252275760");
		ResourceEntrySelectSparql re = new ResourceEntrySelectSparql();
		re.setSparqlEntity(se);
		return re;
	}

	@Bean
	public DatabaseSelectSparql databaseSelectSparql() {
		DatabaseSelectSparql dss = new DatabaseSelectSparql();
		// dss.setFrom();
		return dss;
	}

	@Test
	@Transactional
	public void testInsert() throws SparqlException, UnsupportedEncodingException {
		InsertSparql ins = getResourceEntrySparql();
		SparqlEntity sparqlentity = new SparqlEntity();
		sparqlentity.setValue(ResourceEntry.Identifier, "G00TESTDATA");
		sparqlentity.setValue(Saccharide.PrimaryId, "TEST");
		sparqlentity.setValue(ResourceEntry.ContributorId, "1234");
		sparqlentity.setValue(ResourceEntry.Database, "glytoucan");
		sparqlentity.setValue(ResourceEntry.DataSubmittedDate, new Date(0));

		ins.setSparqlEntity(sparqlentity);
		sparqlDAO.insert(ins);

		List<SparqlEntity> list = sparqlDAO.query(new SelectSparqlBean(
				"PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\nPREFIX glytoucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>\n"
						+ "select ?date where {?resource a glycan:resource_entry . ?resource glycan:in_glycan_database glycan:database_glytoucan . ?resource dcterms:identifier \"G00TESTDATA\"^^xsd:string . ?resource dcterms:dataSubmitted ?date . ?resource dc:contributor <http://rdf.glycoinfo.org/glytoucan/contributor/"
						+ "1234" + ">}"));
		if (list.size() < 1)
			Assert.fail("no results");
		for (SparqlEntity sparqlEntity : list) {
			Assert.assertEquals("Thu Jan 01 09:00:00 JST 1970", sparqlEntity.getValue("date"));
		}
	}

	@Test
	public void testSelect() throws SparqlException, UnsupportedEncodingException {
		List<SparqlEntity> list = sparqlDAO.query(resourceEntrySelectSparql());

		if (list.size() < 1)
			Assert.fail("no results");
		for (SparqlEntity sparqlEntity : list) {
			Assert.assertEquals("252275760", sparqlEntity.getValue(ResourceEntry.Identifier));
		}
	}

	@Test
	public void testSelectDatabase() throws SparqlException, UnsupportedEncodingException {
		SelectSparql ss = databaseSelectSparql();
		SparqlEntity se = new SparqlEntity();
		se.setValue(ResourceEntry.ContributorId, "254");
		ss.setSparqlEntity(se);
		List<SparqlEntity> list = sparqlDAO.query(ss);

		if (list.size() < 1)
			Assert.fail("no results");
		for (SparqlEntity sparqlEntity : list) {
		}
	}

	@Test
	@Transactional
	public void testInsertGlycanDBResource() throws SparqlException, UnsupportedEncodingException {

		// <http://rdf.glycoinfo.org/unicarb-db/324>
		// a glycan:resource_entry ;
		// dcterms:identirfier "324" ;
		// glycan:in_glycan_database glycan:database_unicarb_db ;
		// rdfs:seeAlso <http://unicarb-db.biomedicine.gu.se:9000/msData/324> ;
		// glytoucan:contributor
		// <http://rdf.glycoinfo.org/glytoucan/contributor/userId/5861> .
		SelectSparql ss = databaseSelectSparql();
		SparqlEntity se = new SparqlEntity();
		se.setValue(ResourceEntry.ContributorId, "254");
		ss.setSparqlEntity(se);
		List<SparqlEntity> list = sparqlDAO.query(ss);

		if (list != null && list.iterator().hasNext()) {
			SparqlEntity databaseresult = list.iterator().next();
			InsertSparql is = getResourceEntrySparql();
			databaseresult.setValue(ResourceEntry.Identifier, "323");
			databaseresult.setValue(ResourceEntry.DataSubmittedDate, new Date());
			is.setSparqlEntity(databaseresult);
			sparqlDAO.insert(is);
		} else
			Assert.fail();
	}

}