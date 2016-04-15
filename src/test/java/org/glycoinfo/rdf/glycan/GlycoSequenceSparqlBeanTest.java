package org.glycoinfo.rdf.glycan;

import static org.junit.Assert.assertEquals;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.dao.VirtSesameDAOTestConfig;
import org.glycoinfo.rdf.glycan.wurcs.GlycoSequenceSelectSparql;
import org.glycoinfo.rdf.glycan.wurcs.GlycoSequenceToWurcsSelectSparql;
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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { GlycoSequenceSparqlBeanTest.class , VirtSesameDAOTestConfig.class })
@Configuration
@EnableAutoConfiguration
public class GlycoSequenceSparqlBeanTest {

	public static Logger logger = (Logger) LoggerFactory
			.getLogger(GlycoSequenceSparqlBeanTest.class);
	
	@Autowired
	SparqlDAO sparqlDAO;

	@Bean
	GlycoSequenceSelectSparql getGlycoSequenceSparql() {
		GlycoSequenceSelectSparql ins = new GlycoSequenceSelectSparql();
		SparqlEntity sparqlentity = new SparqlEntity();
		sparqlentity.setValue(Saccharide.PrimaryId, "G00009BX");
		ins.setSparqlEntity(sparqlentity);
		return ins;
	}

//	@Test
	public void testSelectSparql() throws SparqlException {
		logger.debug(getGlycoSequenceSparql().getSparql());
		
		assertEquals(
				"INSERT INTO\n"
				+ "{ insertsacharideuri a glycosequenceuri .\n"
				+ "insertsacharideuri glytoucan:has_primary_id primaryid .\n"
				+ " }\n",
				getGlycoSequenceSparql().getSparql());
	}
	
//	@Test
	public void testSelectToWurcsSparql() throws SparqlException, UnsupportedEncodingException {
		GlycoSequenceToWurcsSelectSparql s = new GlycoSequenceToWurcsSelectSparql("glycoct");
		SparqlEntity se = new SparqlEntity();
		se.setValue(GlycoSequenceToWurcsSelectSparql.FromSequence, "RES\n1b:a-dgal-HEX-1:5\n2s:n-acetyl\n3b:b-dgal-HEX-1:5\n4b:b-dglc-HEX-1:5\n5s:n-acetyl\n6b:b-dgal-HEX-1:5\n7b:a-lgal-HEX-1:5|6:d\n8b:b-dglc-HEX-1:5\n9s:n-acetyl\n10b:b-dglc-HEX-1:5\n11s:n-acetyl\n12b:b-dgal-HEX-1:5\n13b:a-lgal-HEX-1:5|6:d\nLIN\n1:1d(2+1)2n\n2:1o(3+1)3d\n3:3o(3+1)4d\n4:4d(2+1)5n\n5:4o(4+1)6d\n6:6o(2+1)7d\n7:3o(6+1)8d\n8:8d(2+1)9n\n9:1o(6+1)10d\n10:10d(2+1)11n\n11:10o(4+1)12d\n12:12o(2+1)13d");
		s.setSparqlEntity(se);
		logger.debug(s.getSparql());
		
		assertEquals(
				"PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
				+ "PREFIX glytoucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>\n"
				+ " SELECT DISTINCT ?Sequence\n"
				+ " FROM <http://rdf.glytoucan.org>\n"
				+ "FROM <http://rdf.glytoucan.org/sequence/wurcs>\n"
				+ " WHERE {?SaccharideURI a glycan:saccharide .\n"
				+ "?SaccharideURI glycan:has_glycosequence ?GlycanSequenceURI .\n"
				+ "?GlycanSequenceURI glycan:has_sequence ?Sequence .\n"
				+ "?GlycanSequenceURI glycan:in_carbohydrate_format glycan:carbohydrate_format_wurcs .\n"
				+ "?SaccharideURI glycan:has_glycosequence ?FormatGlycoSequenceURI .\n"
				+ "?FormatGlycoSequenceURI glycan:in_carbohydrate_format glycan:carbohydrate_format_glycoct .\n"
				+ "?FormatGlycoSequenceURI glycan:has_sequence \"RES\n1b:a-dgal-HEX-1:5\n2s:n-acetyl\n3b:b-dgal-HEX-1:5\n4b:b-dglc-HEX-1:5\n5s:n-acetyl\n6b:b-dgal-HEX-1:5\n7b:a-lgal-HEX-1:5|6:d\n8b:b-dglc-HEX-1:5\n9s:n-acetyl\n10b:b-dglc-HEX-1:5\n11s:n-acetyl\n12b:b-dgal-HEX-1:5\n13b:a-lgal-HEX-1:5|6:d\nLIN\n1:1d(2+1)2n\n2:1o(3+1)3d\n3:3o(3+1)4d\n4:4d(2+1)5n\n5:4o(4+1)6d\n6:6o(2+1)7d\n7:3o(6+1)8d\n8:8d(2+1)9n\n9:1o(6+1)10d\n10:10d(2+1)11n\n11:10o(4+1)12d\n12:12o(2+1)13d\"^^xsd:string .\n"
						+ "}",
				s.getSparql());
	}
	
	@Test
	public void testRunSelectToWurcsSparql() throws SparqlException {
		GlycoSequenceToWurcsSelectSparql s = new GlycoSequenceToWurcsSelectSparql("glycoct");
		SparqlEntity se = new SparqlEntity();
		String glycoct = "RES\n1b:a-dgal-HEX-1:5\n2s:n-acetyl\n3b:b-dgal-HEX-1:5\n4b:b-dglc-HEX-1:5\n5s:n-acetyl\n6b:b-dgal-HEX-1:5\n7b:a-lgal-HEX-1:5|6:d\n8b:b-dglc-HEX-1:5\n9s:n-acetyl\n10b:b-dglc-HEX-1:5\n11s:n-acetyl\n12b:b-dgal-HEX-1:5\n13b:a-lgal-HEX-1:5|6:d\nLIN\n1:1d(2+1)2n\n2:1o(3+1)3d\n3:3o(3+1)4d\n4:4d(2+1)5n\n5:4o(4+1)6d\n6:6o(2+1)7d\n7:3o(6+1)8d\n8:8d(2+1)9n\n9:1o(6+1)10d\n10:10d(2+1)11n\n11:10o(4+1)12d\n12:12o(2+1)13d";
		String input = glycoct.replace("\n", "\\n");
		se.setValue(GlycoSequenceToWurcsSelectSparql.FromSequence, input);
		s.setSparqlEntity(se);
		logger.debug(s.getSparql());

		List<SparqlEntity> list = sparqlDAO.query(s);
		
		logger.debug(""+list);
		Assert.assertNotEquals(list.size(), 0);
	}
	
	@Test
	public void testRunSelectToWurcsSparqlG00031MO() throws SparqlException {
		GlycoSequenceToWurcsSelectSparql s = new GlycoSequenceToWurcsSelectSparql("glycoct");
		SparqlEntity se = new SparqlEntity();
		String glycoct = "RES\\n"
				+ "1b:a-dgal-HEX-1:5\\n"
				+ "2s:n-acetyl\\n"
				+ "3b:b-dgal-HEX-1:5\\n"
				+ "LIN\\n"
				+ "1:1d(2+1)2n\\n"
				+ "2:1o(3+1)3d";
		se.setValue(GlycoSequenceToWurcsSelectSparql.FromSequence, glycoct);
		s.setSparqlEntity(se);
		logger.debug(s.getSparql());

		List<SparqlEntity> list = sparqlDAO.query(s);
		
		logger.debug(""+list);
		Assert.assertNotEquals(list.size(), 0);
	}
	
	@Test
	public void testRunSelectToWurcsSparqlG00031MONoFormat() throws SparqlException {
		GlycoSequenceToWurcsSelectSparql s = new GlycoSequenceToWurcsSelectSparql();
		SparqlEntity se = new SparqlEntity();
		String glycoct = "RES\\n"
				+ "1b:a-dgal-HEX-1:5\\n"
				+ "2s:n-acetyl\\n"
				+ "3b:b-dgal-HEX-1:5\\n"
				+ "LIN\\n"
				+ "1:1d(2+1)2n\\n"
				+ "2:1o(3+1)3d";
		se.setValue(GlycoSequenceToWurcsSelectSparql.FromSequence, glycoct);
		s.setSparqlEntity(se);
		logger.debug(s.getSparql());

		List<SparqlEntity> list = sparqlDAO.query(s);
		
		logger.debug(""+list);
		Assert.assertNotEquals(list.size(), 0);
	}
//	@Test
	public void testRunSelectToWurcsSparqlNoFormat() throws SparqlException {
		GlycoSequenceToWurcsSelectSparql s = new GlycoSequenceToWurcsSelectSparql();
		SparqlEntity se = new SparqlEntity();
		String glycoct = "RES\n1b:a-dgal-HEX-1:5\n2s:n-acetyl\n3b:b-dgal-HEX-1:5\n4b:b-dglc-HEX-1:5\n5s:n-acetyl\n6b:b-dgal-HEX-1:5\n7b:a-lgal-HEX-1:5|6:d\n8b:b-dglc-HEX-1:5\n9s:n-acetyl\n10b:b-dglc-HEX-1:5\n11s:n-acetyl\n12b:b-dgal-HEX-1:5\n13b:a-lgal-HEX-1:5|6:d\nLIN\n1:1d(2+1)2n\n2:1o(3+1)3d\n3:3o(3+1)4d\n4:4d(2+1)5n\n5:4o(4+1)6d\n6:6o(2+1)7d\n7:3o(6+1)8d\n8:8d(2+1)9n\n9:1o(6+1)10d\n10:10d(2+1)11n\n11:10o(4+1)12d\n12:12o(2+1)13d";
		String input = glycoct.replace("\n", "\\n");
		se.setValue(GlycoSequenceToWurcsSelectSparql.FromSequence, input);
		s.setSparqlEntity(se);
		logger.debug(s.getSparql());

		List<SparqlEntity> list = sparqlDAO.query(s);
		
		logger.debug(""+list);
		Assert.assertNotEquals(list.size(), 0);
		
		assertEquals(
				"INSERT INTO\n"
				+ "{ insertsacharideuri a glycosequenceuri .\n"
				+ "insertsacharideuri glytoucan:has_primary_id primaryid .\n"
				+ " }\n",
				s.getSparql());
	}
	
	
}