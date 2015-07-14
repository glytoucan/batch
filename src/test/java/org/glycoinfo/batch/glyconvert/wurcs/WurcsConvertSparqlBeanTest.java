package org.glycoinfo.batch.glyconvert.wurcs;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.batch.glyconvert.ConvertInsertSparql;
import org.glycoinfo.conversion.GlyConvert;
import org.glycoinfo.conversion.wurcs.GlycoctToWurcsConverter;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.VirtSesameDAOTestConfig;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.dao.virt.SparqlDAOVirtSesameImpl;
import org.glycoinfo.rdf.glycan.GlycoSequence;
import org.glycoinfo.rdf.glycan.Saccharide;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.FilterType;
import org.springframework.stereotype.Repository;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = WurcsConvertSparqlBeanTest.class)
@Configuration
@EnableAutoConfiguration
public class WurcsConvertSparqlBeanTest {
	protected Log logger = LogFactory.getLog(getClass());

	@Bean
	GlyConvert getGlyConvert() {
		return new GlycoctToWurcsConverter();
	}
	
	@Bean
	WurcsConvertSelectSparql getConvertSelectSparql() {
		WurcsConvertSelectSparql convert = new WurcsConvertSelectSparql();
		convert.setGlyConvert(getGlyConvert());
		return convert;
	}

	@Bean
	ConvertInsertSparql getConvertInsertSparql() {
		ConvertInsertSparql convert = new ConvertInsertSparql();
		convert.setGraphBase("http://rdf.glytoucan.org/sequence");
		convert.setGlyConvert(getGlyConvert());
		return convert;
	}

	@Test
	public void testSelectSparql() throws SparqlException {
		logger.debug(getConvertSelectSparql().getSparql());
		
		assertEquals("PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
				+ "PREFIX glytoucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>\n"
				+ "SELECT DISTINCT ?SaccharideURI ?PrimaryId ?Sequence ?GlycanSequenceURI\n"
				+ "FROM <http://rdf.glytoucan.org>\n"				
				+ "WHERE {?SaccharideURI a glycan:saccharide .\n"
				+ "?SaccharideURI glytoucan:has_primary_id ?PrimaryId .\n"
				+ "?SaccharideURI glycan:has_glycosequence ?GlycanSequenceURI .\n"
				+ "?GlycanSequenceURI glycan:has_sequence ?Sequence .\n"
				+ "?GlycanSequenceURI glycan:in_carbohydrate_format glycan:carbohydrate_format_glycoct\n"
				+ "}\n", getConvertSelectSparql().getSparql());
	}
	
	@Test
	public void testInsertSparql() {

		ConvertInsertSparql convert = getConvertInsertSparql();
		SparqlEntity se = new SparqlEntity();
		se.setValue("SaccharideURI", "testSaccharideURI");
//		se.setValue("SequenceURI", "<testSequenceURI>");
		se.setValue(ConvertInsertSparql.ConvertedSequence, "testConvertedSequence!");
		se.setValue(Saccharide.PrimaryId, "123");
		convert.setSparqlEntity(se);
		logger.debug( convert.getSparql());
		assertEquals("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
				+ "PREFIX glytoucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>\n"
				+ "INSERT INTO\n"
				+ "GRAPH <http://rdf.glytoucan.org/sequence/wurcs>\n"
				+ "{ <testSaccharideURI> glycan:has_glycosequence <http://www.glycoinfo.org/rdf/glycan/123/wurcs> .\n"
				+ "<http://www.glycoinfo.org/rdf/glycan/123/wurcs> glycan:has_sequence \"testConvertedSequence!\"^^xsd:string .\n"
				+ "<http://www.glycoinfo.org/rdf/glycan/123/wurcs> glycan:in_carbohydrate_format glycan:carbohydrate_format_wurcs .\n"
				+ "<http://www.glycoinfo.org/rdf/glycan/123/wurcs> glytoucan:is_glycosequence_of <testSaccharideURI> .\n }\n", convert.getSparql());
	}
	
	@Test
	public void testGlycomeDBSelectSparql() throws SparqlException {
		logger.debug(getConvertSelectSparql().getSparql());
		
		assertEquals("PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
				+ "SELECT DISTINCT ?SaccharideURI ?PrimaryId ?Sequence ?GlycanSequenceURI\n"
				+ "FROM <http://rdf.glycoinfo.org/glycome-db>\n"				
				+ "WHERE {?SaccharideURI a glycan:saccharide .\n"
				+ "BIND(STRAFTER(str(?SaccharideURI), \"http://rdf.glycome-db.org/glycan/\") AS ?PrimaryId )\n"
				+ "?SaccharideURI glycan:has_glycosequence ?GlycanSequenceURI .\n"
				+ "?GlycanSequenceURI glycan:has_sequence ?Sequence .\n"
//				+ "BIND(CONCAT(STRBEFORE(str(?GlycanSequenceURI), \"/ct\"), \"/wurcs/2.0\") AS ?GlycanSequenceURI)\n"
				+ "?GlycanSequenceURI glycan:in_carbohydrate_format glycan:carbohydrate_format_glycoct\n"
				+ "}\n", getConvertSelectSparql().getSparql());
	}

	@Test
	public void testGlycomeDBInsertSparql() {

		ConvertInsertSparql convert = getConvertInsertSparql();
		SparqlEntity se = new SparqlEntity();
		se.setValue("SaccharideURI", "testSaccharideURI");
//		se.setValue("SequenceURI", "<testSequenceURI>");
		se.setValue(ConvertInsertSparql.ConvertedSequence, "testConvertedSequence!");
		se.setValue(Saccharide.PrimaryId, "123");
		convert.setSparqlEntity(se);
		logger.debug( convert.getSparql());
		assertEquals("PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
				+ "PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
				+ "PREFIX glytoucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>\n"
				+ "INSERT INTO\n"
				+ "GRAPH <http://rdf.glytoucan.org/sequence/wurcs>\n"
				+ "{ <testSaccharideURI> glycan:has_glycosequence <http://www.glycoinfo.org/rdf/glycan/5963/wurcs/2.0> .\n"
				+ "<http://rdf.glycome-db.org/glycan/5963/wurcs/2.0> glycan:has_sequence \"testConvertedSequence!\"^^xsd:string .\n"
				+ "<http://rdf.glycome-db.org/glycan/5963/wurcs/2.0> glycan:in_carbohydrate_format glycan:carbohydrate_format_wurcs .\n"
				+ "<http://rdf.glycome-db.org/glycan/5963/wurcs/2.0> glytoucan:is_glycosequence_of <testSaccharideURI> .\n }\n", convert.getSparql());
	}
/*	
	PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>
		SELECT DISTINCT ?SaccharideURI ?id ?SeqWurcs ?Sequence 
		FROM <http://rdf.glycoinfo.org/glycome-db>
		WHERE {
		?SaccharideURI glycan:has_glycosequence ?GlycanSequenceURI .
		BIND(STRAFTER(str(?SaccharideURI), "http://rdf.glycome-db.org/glycan/") AS ?id )
		?GlycanSequenceURI glycan:has_sequence ?Sequence .
		BIND(CONCAT(STRBEFORE(str(?GlycanSequenceURI), "/ct"), "/wurcs/2.0") AS ?SeqWurcs)
		?GlycanSequenceURI glycan:in_carbohydrate_format glycan:carbohydrate_format_glycoct .
		}
	
		PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>
		PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>
		PREFIX glytoucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>
		INSERT INTO
		GRAPH <http://rdf.glycoinfo.org/glycome-db/sequence/wurcs>
		{ 
		<testSaccharideURI> glycan:has_glycosequence <http://www.glycoinfo.org/rdf/glycan/5963/wurcs/2.0> .
		<http://rdf.glycome-db.org/glycan/5963/wurcs/2.0> glycan:has_sequence "testConvertedSequence!"^^xsd:string .
		<http://rdf.glycome-db.org/glycan/5963/wurcs/2.0> glycan:in_carbohydrate_format glycan:carbohydrate_format_wurcs .
		<http://rdf.glycome-db.org/glycan/5963/wurcs/2.0> glytoucan:is_glycosequence_of <testSaccharideURI> .
		 }
*/
}