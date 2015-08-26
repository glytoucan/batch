package org.glycoinfo.rdf.service;

import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.digest.DigestUtils;
import org.glycoinfo.conversion.error.ConvertException;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.dao.VirtSesameDAOTestConfig;
import org.glycoinfo.rdf.glycan.ContributorInsertSparql;
import org.glycoinfo.rdf.glycan.ContributorNameSelectSparql;
import org.glycoinfo.rdf.glycan.ResourceEntryInsertSparql;
import org.glycoinfo.rdf.glycan.SaccharideInsertSparql;
import org.glycoinfo.rdf.glycan.wurcs.GlycoSequenceResourceEntryContributorSelectSparql;
import org.glycoinfo.rdf.glycan.wurcs.GlycoSequenceToWurcsSelectSparql;
import org.glycoinfo.rdf.scint.ClassHandler;
import org.glycoinfo.rdf.scint.InsertScint;
import org.glycoinfo.rdf.scint.SelectScint;
import org.glycoinfo.rdf.service.impl.ContributorProcedureRdf;
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
import org.springframework.context.annotation.Scope;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {GlycanProcedureTest.class, VirtSesameDAOTestConfig.class})
//@ComponentScan(basePackages = {"org.glycoinfo.rdf.service", "org.glycoinfo.rdf.scint"})
//@ComponentScan(basePackages = {"org.glycoinfo.rdf"}, excludeFilters={
//		  @ComponentScan.Filter(type=FilterType.ASSIGNABLE_TYPE, value=Configuration.class)})
@Configuration
@EnableAutoConfiguration
public class GlycanProcedureTest {
	public static Logger logger = (Logger) LoggerFactory
			.getLogger(GlycanProcedureTest.class);
	
	@Autowired
	SparqlDAO sparqlDAO;
	
	private static final String graph = "http://rdf.test.glytoucan.org";
	
	@Bean(name = "selectscintperson")
	SelectScint getSelectPersonScint() throws SparqlException {
		SelectScint select = new SelectScint();
		
		select.setClassHandler(getPersonClassHandler());
		return select;
	}

	@Bean(name = "insertscintperson")
	InsertScint getInsertPersonScint() throws SparqlException {
		InsertScint insert = new InsertScint("http://rdf.glytoucan.org/users");
		insert.setClassHandler(getPersonClassHandler());
		return insert;
	}
	
	@Bean(name = "selectscintregisteraction")
	SelectScint getSelectRegisterActionScint() throws SparqlException {
		SelectScint select = new SelectScint();
		select.setClassHandler(getRegisterActionClassHandler());
		return select;
	}

	@Bean(name = "insertscintregisteraction")
	InsertScint getInsertRegisterActionScint() throws SparqlException {
		InsertScint insert = new InsertScint("http://rdf.glytoucan.org/users");
		insert.setClassHandler(getRegisterActionClassHandler());
		return insert;
	}
	
	@Bean
	ClassHandler getPersonClassHandler() throws SparqlException {
		ClassHandler ch = new ClassHandler("schema", "http://schema.org/", "Person");
		ch.setSparqlDAO(sparqlDAO);
		return ch; 
	}
	
	ClassHandler getRegisterActionClassHandler() throws SparqlException {
		ClassHandler ch = new ClassHandler("schema", "http://schema.org/", "RegisterAction");
		ch.setSparqlDAO(sparqlDAO);
		return ch; 
	}
	
	ClassHandler getDateTimeClassHandler() throws SparqlException {
		ClassHandler ch = new ClassHandler("schema", "http://schema.org/", "DateTime");
		ch.setSparqlDAO(sparqlDAO);
		return ch; 
	}
	
	@Autowired
	GlycanProcedure glycanProcedure;

	@Bean(name = "glycanProcedure")
	@Scope("prototype")
	GlycanProcedure getGlycanProcedure() throws SparqlException {
		GlycanProcedure glycan = new org.glycoinfo.rdf.service.impl.GlycanProcedure();
		return glycan;
	}
	
	@Bean
	SaccharideInsertSparql getSaccharideInsertSparql() {
		SaccharideInsertSparql sis = new SaccharideInsertSparql();
		sis.setGraph(graph);
		return sis;
	}
	
	@Bean(name = "contributorProcedure")
	ContributorProcedure getContributorProcedure() throws SparqlException {
		ContributorProcedure cp = new ContributorProcedureRdf();
		return cp;
	}

	@Bean
	ContributorInsertSparql getContributorInsertSparql() {
		ContributorInsertSparql c = new ContributorInsertSparql();
		c.setGraph(graph);
		return c;
	}
	
	@Bean
	ContributorNameSelectSparql getContributorNameSelectSparql() {
		ContributorNameSelectSparql selectbyNameContributor = new ContributorNameSelectSparql();
		selectbyNameContributor.setFrom("FROM <" + graph + ">");
		return selectbyNameContributor;
	}
	
	@Bean
	ResourceEntryInsertSparql getResourceEntryInsertSparql() {
		ResourceEntryInsertSparql resourceEntryInsertSparql = new ResourceEntryInsertSparql();
		SparqlEntity se = new SparqlEntity();
		se.setValue(ResourceEntryInsertSparql.Database, "glytoucan");
		resourceEntryInsertSparql.setSparqlEntity(se);
		resourceEntryInsertSparql.setGraph("FROM <" + graph + ">");
		return resourceEntryInsertSparql;
	}
	
	@Bean
	@Scope("prototype")
	SelectSparql glycoSequenceContributorSelectSparql() {
		GlycoSequenceResourceEntryContributorSelectSparql sb = new GlycoSequenceResourceEntryContributorSelectSparql();
		sb.setFrom("FROM <http://rdf.glytoucan.org>\nFROM <http://rdf.glytoucan.org/sequence/wurcs>");
		return sb;
	}

	
//	@Test(expected=SparqlException.class)
//	public void testInsufficientUser() throws SparqlException {
//		SparqlEntity se = new SparqlEntity();
//		se.setValue("id", "person456");
//		userProcedure.setSparqlEntity(se);
//		userProcedure.addUser();
//	}
	
	/*
	 * RES
1b:b-dglc-HEX-1:5
2s:n-acetyl
3b:b-dgal-HEX-1:5
LIN
1:1d(2+1)2n
2:1o(4+1)3d
	 */
	@Test
	public void testSearch() throws SparqlException, ConvertException {
		glycanProcedure.setSequence("RES\n" +
				"1b:b-dglc-HEX-1:5\n" +
				"2s:n-acetyl\n"
				+ "3b:b-dgal-HEX-1:5\n"
				+ "LIN\n"
				+ "1:1d(2+1)2n\n"
				+ "2:1o(4+1)3d");
		SparqlEntity se = glycanProcedure.searchBySequence();
		
		logger.debug(se.getValue(GlycoSequenceToWurcsSelectSparql.AccessionNumber));
		logger.debug(se.getValue(GlycanProcedure.Image));
		logger.debug(se.getValue(GlycoSequenceToWurcsSelectSparql.Sequence));
		Assert.assertNotNull(se.getValue(GlycoSequenceToWurcsSelectSparql.AccessionNumber));
		Assert.assertNotNull(se.getValue(GlycanProcedure.Image));
		Assert.assertNotNull(se.getValue(GlycoSequenceToWurcsSelectSparql.Sequence));
		Assert.assertEquals("G00055MO", se.getValue(GlycoSequenceToWurcsSelectSparql.AccessionNumber));
	}
	
	@Test
	public void testSearchG00031MO() throws SparqlException, ConvertException {
		// ne
//		data-wurcs="WURCS=2.0/2,2,1/[a2112h-1a_1-5_2*NCC/3=O][a2112h-1b_1-5]/1-2/a3-b1" >
	
//test
//		data-wurcs="WURCS=2.0/2,2,1/[22112h-1a_1-5_2*NCC/3=O][12112h-1b_1-5]/1-2/a3-b1">
//		data-wurcs="WURCS=2.0/2,2,1/[22112h-1a_1-5_2*NCC/3=O][12112h-1b_1-5]/1-2/a3-b1">
		glycanProcedure.setSequence("RES\n"
				+ "1b:a-dgal-HEX-1:5\n"
				+ "2s:n-acetyl\n"
				+ "3b:b-dgal-HEX-1:5\n"
				+ "LIN\n"
				+ "1:1d(2+1)2n\n"
				+ "2:1o(3+1)3d");
		SparqlEntity se = glycanProcedure.searchBySequence();

		logger.debug(se.getValue(GlycoSequenceToWurcsSelectSparql.AccessionNumber));
		logger.debug(se.getValue(GlycanProcedure.Image));
		logger.debug(se.getValue(GlycoSequenceToWurcsSelectSparql.Sequence));
		Assert.assertNotNull(se.getValue(GlycoSequenceToWurcsSelectSparql.AccessionNumber));
		Assert.assertNotNull(se.getValue(GlycanProcedure.Image));
		Assert.assertNotNull(se.getValue(GlycoSequenceToWurcsSelectSparql.Sequence));
		Assert.assertEquals("G00031MO", se.getValue(GlycoSequenceToWurcsSelectSparql.AccessionNumber));
	}

	
	@Test
	public void testHash() {
		String sequence="RES\n"
				+ "1b:x-dglc-HEX-1:5|1:a\n"
				+ "2b:b-dgal-HEX-1:5\n"
				+ "LIN\n"
				+ "1:1o(4+1)2d";
		String hashtext = DigestUtils.md5Hex(sequence);
		Assert.assertEquals("e06b141de8d13adfa0c3ad180b9eae06", hashtext);
		hashtext = DigestUtils.md5Hex("WURCS=2.0/4,4,3/[u2122h][a2112h-1b_1-5][a2112h-1a_1-5][a2112h-1b_1-5_2*NCC/3=O]/1-2-3-4/a4-b1_b3-c1_c3-d1");
		Assert.assertEquals("497ea4c9a0680f9aa7d6541dca211967", hashtext);
		logger.debug(hashtext);

		//		WURCS=2.0/4,4,3/[u2122h_2*NCC/3=O_6*OSO/3=O/3=O_?*OSO/3=O/3=O][a1212A-1a_1-5_2*OSO/3=O/3=O][a2122h-1b_1-5_2*NCC/3=O_6*OSO/3=O/3=O_?*OSO/3=O/3=O][a1212A-1a_1-5]/1-2-3-4/a4-b1_b4-c1_c4-d1
		hashtext = DigestUtils.md5Hex("WURCS=2.0/4,4,3/[u2122h_2*NCC/3=O_6*OSO/3=O/3=O_?*OSO/3=O/3=O][a1212A-1a_1-5_2*OSO/3=O/3=O][a2122h-1b_1-5_2*NCC/3=O_6*OSO/3=O/3=O_?*OSO/3=O/3=O][a1212A-1a_1-5]/1-2-3-4/a4-b1_b4-c1_c4-d1");
		logger.debug(hashtext);
		Assert.assertEquals("331ebfcfc29a997790a7a4f1671a9882", hashtext);
	}
	
	@Test
	public void testRegisterNew() throws SparqlException, NoSuchAlgorithmException {
		
		String sequence="RES\n"
				+ "1b:x-dglc-HEX-1:5|1:a\n"
				+ "2b:b-dgal-HEX-1:5\n"
				+ "LIN\n"
				+ "1:1o(4+1)2d";

		glycanProcedure.setSequence(sequence);
		glycanProcedure.setContributor("testname");
		String se = glycanProcedure.register();

		logger.debug(se);
//		Assert.assertNotNull(se);
		
	}
	
	@Test
	public void testSequenceScope() throws SparqlException, NoSuchAlgorithmException {
		
		SparqlEntity se = glycanProcedure.searchByAccessionNumber("G00026MO");
		logger.debug(se.getValue("Mass"));
		se = glycanProcedure.searchByAccessionNumber("G00031MO");
		logger.debug(se.getValue("Mass"));
//		Assert.assertNotNull(se);
		
	}
}