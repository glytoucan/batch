package org.glycoinfo.batch.mass;

import static org.junit.Assert.assertEquals;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.batch.glyconvert.ConvertInsertSparql;
import org.glycoinfo.convert.GlyConvert;
import org.glycoinfo.convert.glycoct.GlycoctToWurcsConverter;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.glycan.Saccharide;
import org.glycoinfo.rdf.glycan.mass.MassSelectSparql;
import org.springframework.context.annotation.Bean;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = MassSparqlBeanTest.class)
//@Configuration
//@EnableAutoConfiguration
public class MassSparqlBeanTest {
	protected Log logger = LogFactory.getLog(getClass());

	@Bean
	GlyConvert getGlyConvert() {
		return new GlycoctToWurcsConverter();
	}
	
	@Bean
	MassSelectSparql getMassSelectSparql() {
		MassSelectSparql convert = new MassSelectSparql();
		return convert;
	}

	@Bean
	ConvertInsertSparql getConvertInsertSparql() {
		ConvertInsertSparql convert = new ConvertInsertSparql();
		convert.setGraphBase("http://rdf.glytoucan.org/sequence");
		convert.setGlyConvert(getGlyConvert());
		return convert;
	}

//	@Test
	public void testSelectSparql() throws SparqlException {
		logger.debug(getMassSelectSparql().getSparql());
		
		assertEquals("PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
				+ "PREFIX glytoucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>\n"
				+ "SELECT DISTINCT ?SaccharideURI ?PrimaryId ?Sequence ?GlycanSequenceURI\n"
				+ "FROM <http://rdf.glytoucan.org>\n"
				+ "FROM <http://rdf.glytoucan.org/sequence/wurcs>\n"
				+ "WHERE {?SaccharideURI a glycan:saccharide .\n"
				+ "?SaccharideURI glytoucan:has_primary_id ?PrimaryId .\n"
				+ "?SaccharideURI glycan:has_glycosequence ?GlycanSequenceURI .\n"
				+ "?GlycanSequenceURI glycan:has_sequence ?Sequence .\n"
				+ "?GlycanSequenceURI glycan:in_carbohydrate_format glycan:carbohydrate_format_wurcs\n"
				+ "FILTER NOT EXISTS {\n"
				+ "?SaccharideURI glytoucan:has_derivatized_mass ?existingmass .\n}"
				+ "}\n", getMassSelectSparql().getSparql());
	}

//	@Test
	public void testInsertSparql() throws SparqlException {
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

}