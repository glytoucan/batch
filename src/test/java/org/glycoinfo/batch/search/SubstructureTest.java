package org.glycoinfo.batch.search;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.glycoinfo.batch.search.wurcs.IsomerSubstructureSearchSparql;
import org.glycoinfo.batch.search.wurcs.SubstructureSearchSparql;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.glycan.GlycoSequence;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

//@RunWith(SpringJUnit4ClassRunner.class)
//@SpringApplicationConfiguration(classes = { SearchTest.class, GlycanProcedureConfig.class, VirtSesameTransactionConfig.class })
//@Configuration
//@EnableAutoConfiguration
public class SubstructureTest {
	public static Logger logger = (Logger) LoggerFactory
			.getLogger(SubstructureTest.class);

	@Autowired
	SparqlDAO sparqlDAO;
	
	@Bean
	SelectSparql getSelectSparql() {
		SubstructureSearchSparql search = new SubstructureSearchSparql();
		return search;
	}
	
//	@Test
	public void testSelectSparql() throws SparqlException {
		SelectSparql search = getSelectSparql();
		SparqlEntity sparqlentity = new SparqlEntity();
		sparqlentity.setValue(GlycoSequence.URI, "rdf://sacharide.test");
		sparqlentity.setValue(GlycoSequence.Sequence, "WURCS=2.0/2,3,2/[11122h-1b_1-5][21122h-1a_1-5]/1-2-2/a3-b1_a6-c1");
		search.setSparqlEntity(sparqlentity);

		assertEquals("PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
				+ "PREFIX toucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>\n"
				+ "SELECT ?SaccharideURI ?PrimaryId\n"
				+ "SELECT ?SaccharideURI ?PrimaryId\n"
				+ "from <http://glytoucan.org/rdf/demo/0.2>\n"
				+ "from <http://glycoinfo.org/graph/wurcs>\n\n"
				+ "WHERE {<rdf://sacharide.test> wurcs:has_uniqueRES  ?uRES1, ?uRES2 .\n"
				+ "?uRES1 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/11122h-1b_1-5> .\n"
				+ "?uRES2 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/21122h-1a_1-5> .\n"
				+ "# RES\n"
				+ "?RESa wurcs:is_uniqueRES ?uRES1 .\n"
				+ "?RESb wurcs:is_uniqueRES ?uRES2 .\n"
				+ "# LIN\n"
				+ "?gseq wurcs:has_LIN ?LINb1a3a6 .\n"
				+ "# LIN1\n"
				+ "?LINb1a3a6 wurcs:has_GLIPS   ?GLIPSb1 ,   ?GLIPSa3a6 .\n"
				+ "# LIN1: GLIPS1\n"
				+ "?GLIPSb1 wurcs:has_GLIP ?GLIPb1 .\n"
				+ "?GLIPb1 wurcs:has_SC_position 1 .\n"
				+ "?GLIPb1 wurcs:has_RES ?RESb .\n"
				+ "?GLIPSb1 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
						+ "# LIN1: GLIPS2\n"
						+ "?GLIPSa3a6 wurcs:has_GLIP ?GLIPa3a6 .\n"
						+ "?GLIPa3 wurcs:has_SC_position 3 .\n"
						+ "?GLIPa3 wurcs:has_RES ?RESa .\n"
						+ "?GLIPa6 wurcs:has_SC_position 6 .\n"
						+ "?GLIPa6 wurcs:has_RES ?RESa .\n"
						+ "?GLIPSa3a6 wurcs:isFuzzy \"true\"^^xsd:boolean .\n"
								+ "}", search.getSparql());
	}
	
//	@Test
	public void testSearchSelectSparql() throws SparqlException {
		SelectSparql search = getSelectSparql();
		SparqlEntity sparqlentity = new SparqlEntity();
		sparqlentity.setValue(GlycoSequence.URI, "rdf://sacharide.test");
		sparqlentity.setValue(GlycoSequence.Sequence, "WURCS=2.0/4,7,6/[u2122h_2*NCC/3=O][12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5]/1-2-3-4-2-4-2/a4-b1_b4-c1_c3-d1_c6-f1_e1-d2|d4_g1-f2|f4");
		search.setSparqlEntity(sparqlentity);

		logger.debug(search.getSparql());
		assertEquals("PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
				+ "PREFIX toucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>\n"
				+ "SELECT DISTINCT ?SaccharideURI ?PrimaryId\n"
				+ "FROM <http://rdf.glytoucan.org/core>\n"
				+ "FROM <http://rdf.glytoucan.org/sequence/wurcs>\n"
				+ "FROM <http://www.glycoinfo.org/graph/wurcs/0.4>\n"
				+ "WHERE {?SaccharideURI toucan:has_primary_id ?PrimaryId .\n"
				+ "GRAPH <http://www.glycoinfo.org/graph/wurcs/0.4> {?SaccharideURI glycan:has_glycosequence ?GlycoSequenceURI .\n"
				+ "?null wurcs:has_uniqueRES  ?uRES1, ?uRES2, ?uRES3 .\n"
				+ "?uRES1 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/x2122h-1x_1-5_2*NCC%2F3%3DO> .\n"
				+ "?uRES2 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/12112h-1b_1-5> .\n"
				+ "?uRES3 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/11221m-1a_1-5> .\n"
				+ "# RES\n"
				+ "?RESa wurcs:is_uniqueRES ?uRES1 .\n"
				+ "?RESb wurcs:is_uniqueRES ?uRES2 .\n"
				+ "?RESc wurcs:is_uniqueRES ?uRES3 .\n"
				+ "# LIN\n"
				+ "?gseq wurcs:has_LIN ?LINa3b1 , ?LINa4c1 . \n"
				+ "# LIN1\n"
				+ "  ?LINa3b1 wurcs:has_GLIPS   ?GLIPSa3 ,   ?GLIPSb1 .  \n"
				+ "# LIN2\n"
				+ "  ?LINa4c1 wurcs:has_GLIPS   ?GLIPSa4 ,   ?GLIPSc1 .  \n"
				+ "\n"
				+ " # LIN1: GLIPS1\n"
				+ "  ?GLIPSa3 wurcs:has_GLIP ?GLIPa3 . \n"
				+ "  ?GLIPa3 wurcs:has_SC_position 3 .\n"
				+ "  ?GLIPa3 wurcs:has_RES ?RESa .\n"
				+ "  ?GLIPSa3 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
				+ "# LIN1: GLIPS2\n"
				+ "  ?GLIPSb1 wurcs:has_GLIP ?GLIPb1 . \n"
				+ "  ?GLIPb1 wurcs:has_SC_position 1 .\n"
				+ "  ?GLIPb1 wurcs:has_RES ?RESb .\n"
				+ "  ?GLIPSb1 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
						+ "# LIN2: GLIPS1\n"
						+ "  ?GLIPSa4 wurcs:has_GLIP ?GLIPa4 . \n"
						+ "  ?GLIPa4 wurcs:has_SC_position 4 .\n"
						+ "  ?GLIPa4 wurcs:has_RES ?RESa .\n"
						+ "  ?GLIPSa4 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
								+ "# LIN2: GLIPS2\n"
								+ "  ?GLIPSc1 wurcs:has_GLIP ?GLIPc1 . \n"
								+ "  ?GLIPc1 wurcs:has_SC_position 1 .\n"
								+ "  ?GLIPc1 wurcs:has_RES ?RESc .\n"
								+ "  ?GLIPSc1 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
								+ "}}\n"
								+ "", search.getSparql());
	}
	
	// 	0030MO
//	@Test
	public void testSearchSelectSparql0030MO() throws SparqlException {
		SelectSparql search = getSelectSparql();
		SparqlEntity sparqlentity = new SparqlEntity();
		sparqlentity.setValue(GlycoSequence.URI, "rdf://sacharide.test");
		sparqlentity.setValue(GlycoSequence.Sequence, "WURCS=2.0/1,4,3/[12122h-1b_1-5]/1-1-1-1/a4-b1_b4-c1_c4-d1");
		search.setSparqlEntity(sparqlentity);

		logger.debug(search.getSparql());
		assertEquals("PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
				+ "PREFIX toucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>\n"
				+ "SELECT DISTINCT ?SaccharideURI ?PrimaryId\n"
				+ "FROM <http://rdf.glytoucan.org/core>\n"
				+ "FROM <http://rdf.glytoucan.org/sequence/wurcs>\n"
				+ "FROM <http://www.glycoinfo.org/graph/wurcs/0.4>\n"
				+ "WHERE {?SaccharideURI toucan:has_primary_id ?PrimaryId .\n"
				+ "GRAPH <http://www.glycoinfo.org/graph/wurcs/0.4> {?SaccharideURI glycan:has_glycosequence ?GlycoSequenceURI .\n"
				+ "?null wurcs:has_uniqueRES  ?uRES1, ?uRES2, ?uRES3 .\n"
				+ "?uRES1 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/x2122h-1x_1-5_2*NCC%2F3%3DO> .\n"
				+ "?uRES2 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/12112h-1b_1-5> .\n"
				+ "?uRES3 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/11221m-1a_1-5> .\n"
				+ "# RES\n"
				+ "?RESa wurcs:is_uniqueRES ?uRES1 .\n"
				+ "?RESb wurcs:is_uniqueRES ?uRES2 .\n"
				+ "?RESc wurcs:is_uniqueRES ?uRES3 .\n"
				+ "# LIN\n"
				+ "?gseq wurcs:has_LIN ?LINa3b1 , ?LINa4c1 . \n"
				+ "# LIN1\n"
				+ "  ?LINa3b1 wurcs:has_GLIPS   ?GLIPSa3 ,   ?GLIPSb1 .  \n"
				+ "# LIN2\n"
				+ "  ?LINa4c1 wurcs:has_GLIPS   ?GLIPSa4 ,   ?GLIPSc1 .  \n"
				+ "\n"
				+ " # LIN1: GLIPS1\n"
				+ "  ?GLIPSa3 wurcs:has_GLIP ?GLIPa3 . \n"
				+ "  ?GLIPa3 wurcs:has_SC_position 3 .\n"
				+ "  ?GLIPa3 wurcs:has_RES ?RESa .\n"
				+ "  ?GLIPSa3 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
				+ "# LIN1: GLIPS2\n"
				+ "  ?GLIPSb1 wurcs:has_GLIP ?GLIPb1 . \n"
				+ "  ?GLIPb1 wurcs:has_SC_position 1 .\n"
				+ "  ?GLIPb1 wurcs:has_RES ?RESb .\n"
				+ "  ?GLIPSb1 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
						+ "# LIN2: GLIPS1\n"
						+ "  ?GLIPSa4 wurcs:has_GLIP ?GLIPa4 . \n"
						+ "  ?GLIPa4 wurcs:has_SC_position 4 .\n"
						+ "  ?GLIPa4 wurcs:has_RES ?RESa .\n"
						+ "  ?GLIPSa4 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
								+ "# LIN2: GLIPS2\n"
								+ "  ?GLIPSc1 wurcs:has_GLIP ?GLIPc1 . \n"
								+ "  ?GLIPc1 wurcs:has_SC_position 1 .\n"
								+ "  ?GLIPc1 wurcs:has_RES ?RESc .\n"
								+ "  ?GLIPSc1 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
								+ "}}\n"
								+ "", search.getSparql());
	}

	//http://ts.nobu.glycoinfo.org/sparql?default-graph-uri=http%3A%2F%2Fwww.glycoinfo.org%2Fwurcs&query=PREFIX+glycan%3A+%3Chttp%3A%2F%2Fpurl.jp%2Fbio%2F12%2Fglyco%2Fglycan%23%3E%0D%0APREFIX+toucan%3A++%3Chttp%3A%2F%2Fwww.glytoucan.org%2Fglyco%2Fowl%2Fglytoucan%23%3E%0D%0ASELECT+%3FMotifURI+%3FPrimaryId+%3FGlycoSequenceURI+%3FSequence%0D%0AFROM+%3Chttp%3A%2F%2Frdf.glytoucan.org%3E%0D%0AFROM+%3Chttp%3A%2F%2Frdf.glytoucan.org%2Fsequence%2Fwurcs%3E%0D%0AWHERE+{%3FMotifURI+a+glycan%3Aglycan_motif+.%0D%0A%3FMotifURI+toucan%3Ahas_primary_id+%3FPrimaryId+.%0D%0A%3FMotifURI+glycan%3Ahas_glycosequence+%3FGlycoSequenceURI+.%0D%0A%3FGlycoSequenceURI+glycan%3Ahas_sequence+%3FSequence+.%0D%0A%3FGlycoSequenceURI+glycan%3Ain_carbohydrate_format+glycan%3Acarbohydrate_format_wurcs+.}%0D%0A&format=text%2Fhtml&timeout=0&debug=on
	// G00026MO

//	@Test
	public void testSearchSelectSparql0026MO() throws SparqlException {
		SelectSparql search = getSelectSparql();
		SparqlEntity sparqlentity = new SparqlEntity();
		sparqlentity.setValue(GlycoSequence.URI, "rdf://sacharide.test");
		sparqlentity.setValue(GlycoSequence.Sequence, "WURCS=2.0/4,5,4/[u2122h_2*NCC/3=O][12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5]/1-2-3-4-4/a4-b1_b4-c1_c3-d1_c6-e1");
		//											   WURCS=2.0/4,5,4/[u2122h_2*NCC/3=O][12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5]/1-2-3-4-4/a4-b1_b4-c1_c3-d1_c6-e1
		search.setSparqlEntity(sparqlentity);

		logger.debug(search.getSparql());
		assertEquals("PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
				+ "PREFIX toucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>\n"
				+ "SELECT DISTINCT ?SaccharideURI ?PrimaryId\n"
				+ "FROM <http://rdf.glytoucan.org/core>\n"
				+ "FROM <http://rdf.glytoucan.org/sequence/wurcs>\n"
				+ "FROM <http://www.glycoinfo.org/graph/wurcs/0.4>\n"
				+ "WHERE {?SaccharideURI toucan:has_primary_id ?PrimaryId .\n"
				+ "GRAPH <http://www.glycoinfo.org/graph/wurcs/0.4> {?SaccharideURI glycan:has_glycosequence ?GlycoSequenceURI .\n"
				+ "?null wurcs:has_uniqueRES  ?uRES1, ?uRES2, ?uRES3 .\n"
				+ "?uRES1 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/x2122h-1x_1-5_2*NCC%2F3%3DO> .\n"
				+ "?uRES2 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/12112h-1b_1-5> .\n"
				+ "?uRES3 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/11221m-1a_1-5> .\n"
				+ "# RES\n"
				+ "?RESa wurcs:is_uniqueRES ?uRES1 .\n"
				+ "?RESb wurcs:is_uniqueRES ?uRES2 .\n"
				+ "?RESc wurcs:is_uniqueRES ?uRES3 .\n"
				+ "# LIN\n"
				+ "?gseq wurcs:has_LIN ?LINa3b1 , ?LINa4c1 . \n"
				+ "# LIN1\n"
				+ "  ?LINa3b1 wurcs:has_GLIPS   ?GLIPSa3 ,   ?GLIPSb1 .  \n"
				+ "# LIN2\n"
				+ "  ?LINa4c1 wurcs:has_GLIPS   ?GLIPSa4 ,   ?GLIPSc1 .  \n"
				+ "\n"
				+ " # LIN1: GLIPS1\n"
				+ "  ?GLIPSa3 wurcs:has_GLIP ?GLIPa3 . \n"
				+ "  ?GLIPa3 wurcs:has_SC_position 3 .\n"
				+ "  ?GLIPa3 wurcs:has_RES ?RESa .\n"
				+ "  ?GLIPSa3 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
				+ "# LIN1: GLIPS2\n"
				+ "  ?GLIPSb1 wurcs:has_GLIP ?GLIPb1 . \n"
				+ "  ?GLIPb1 wurcs:has_SC_position 1 .\n"
				+ "  ?GLIPb1 wurcs:has_RES ?RESb .\n"
				+ "  ?GLIPSb1 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
						+ "# LIN2: GLIPS1\n"
						+ "  ?GLIPSa4 wurcs:has_GLIP ?GLIPa4 . \n"
						+ "  ?GLIPa4 wurcs:has_SC_position 4 .\n"
						+ "  ?GLIPa4 wurcs:has_RES ?RESa .\n"
						+ "  ?GLIPSa4 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
								+ "# LIN2: GLIPS2\n"
								+ "  ?GLIPSc1 wurcs:has_GLIP ?GLIPc1 . \n"
								+ "  ?GLIPc1 wurcs:has_SC_position 1 .\n"
								+ "  ?GLIPc1 wurcs:has_RES ?RESc .\n"
								+ "  ?GLIPSc1 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
								+ "}}\n"
								+ "", search.getSparql());
	}

	
	// WURCS=2.0/4,7,6/[u2122h_2*NCC/3=O][12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5]/1-2-3-4-4-4-4/a4-b1_b4-c1_c3-d1_c6-e1_e3-f1_e6-g1
	// 28MO
	
//	@Test
	public void testSearchSelectSparql0028MO() throws SparqlException {
		SelectSparql search = getSelectSparql();
		SparqlEntity sparqlentity = new SparqlEntity();
		sparqlentity.setValue(GlycoSequence.URI, "rdf://sacharide.test");
		sparqlentity.setValue(GlycoSequence.Sequence, "WURCS=2.0/4,7,6/[u2122h_2*NCC/3=O][12122h-1b_1-5_2*NCC/3=O][11122h-1b_1-5][21122h-1a_1-5]/1-2-3-4-4-4-4/a4-b1_b4-c1_c3-d1_c6-e1_e3-f1_e6-g1");
		search.setSparqlEntity(sparqlentity);

		logger.debug(search.getSparql());
		assertEquals("PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
				+ "PREFIX toucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>\n"
				+ "SELECT DISTINCT ?SaccharideURI ?PrimaryId\n"
				+ "FROM <http://rdf.glytoucan.org/core>\n"
				+ "FROM <http://rdf.glytoucan.org/sequence/wurcs>\n"
				+ "FROM <http://www.glycoinfo.org/graph/wurcs/0.4>\n"
				
				+ "WHERE {?SaccharideURI toucan:has_primary_id ?PrimaryId .\n"
				+ "GRAPH <http://www.glycoinfo.org/graph/wurcs/0.4> {?SaccharideURI glycan:has_glycosequence ?GlycoSequenceURI .\n"
				+ "?null wurcs:has_uniqueRES  ?uRES1, ?uRES2, ?uRES3 .\n"
				+ "?uRES1 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/x2122h-1x_1-5_2*NCC%2F3%3DO> .\n"
				+ "?uRES2 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/12112h-1b_1-5> .\n"
				+ "?uRES3 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/11221m-1a_1-5> .\n"
				+ "# RES\n"
				+ "?RESa wurcs:is_uniqueRES ?uRES1 .\n"
				+ "?RESb wurcs:is_uniqueRES ?uRES2 .\n"
				+ "?RESc wurcs:is_uniqueRES ?uRES3 .\n"
				+ "# LIN\n"
				+ "?gseq wurcs:has_LIN ?LINa3b1 , ?LINa4c1 . \n"
				+ "# LIN1\n"
				+ "  ?LINa3b1 wurcs:has_GLIPS   ?GLIPSa3 ,   ?GLIPSb1 .  \n"
				+ "# LIN2\n"
				+ "  ?LINa4c1 wurcs:has_GLIPS   ?GLIPSa4 ,   ?GLIPSc1 .  \n"
				+ "\n"
				+ " # LIN1: GLIPS1\n"
				+ "  ?GLIPSa3 wurcs:has_GLIP ?GLIPa3 . \n"
				+ "  ?GLIPa3 wurcs:has_SC_position 3 .\n"
				+ "  ?GLIPa3 wurcs:has_RES ?RESa .\n"
				+ "  ?GLIPSa3 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
				+ "# LIN1: GLIPS2\n"
				+ "  ?GLIPSb1 wurcs:has_GLIP ?GLIPb1 . \n"
				+ "  ?GLIPb1 wurcs:has_SC_position 1 .\n"
				+ "  ?GLIPb1 wurcs:has_RES ?RESb .\n"
				+ "  ?GLIPSb1 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
						+ "# LIN2: GLIPS1\n"
						+ "  ?GLIPSa4 wurcs:has_GLIP ?GLIPa4 . \n"
						+ "  ?GLIPa4 wurcs:has_SC_position 4 .\n"
						+ "  ?GLIPa4 wurcs:has_RES ?RESa .\n"
						+ "  ?GLIPSa4 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
								+ "# LIN2: GLIPS2\n"
								+ "  ?GLIPSc1 wurcs:has_GLIP ?GLIPc1 . \n"
								+ "  ?GLIPc1 wurcs:has_SC_position 1 .\n"
								+ "  ?GLIPc1 wurcs:has_RES ?RESc .\n"
								+ "  ?GLIPSc1 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
								+ "}}\n"
								+ "", search.getSparql());
	}

	
	// 31MO
	// WURCS=2.0/2,2,1/[22112h-1a_1-5_2*NCC/3=O][12112h-1b_1-5]/1-2/a3-b1
//	@Test
	public void testSearchSelectSparql0031MO() throws SparqlException {
		SelectSparql search = getSelectSparql();
		SparqlEntity sparqlentity = new SparqlEntity();
		sparqlentity.setValue(GlycoSequence.URI, "rdf://sacharide.test");
		sparqlentity.setValue(GlycoSequence.Sequence, "WURCS=2.0/2,2,1/[22112h-1a_1-5_2*NCC/3=O][12112h-1b_1-5]/1-2/a3-b1");
		search.setSparqlEntity(sparqlentity);

		logger.debug(search.getSparql());
		assertEquals("PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
				+ "PREFIX toucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>\n"
				+ "SELECT DISTINCT ?SaccharideURI ?PrimaryId\n"
				+ "FROM <http://rdf.glytoucan.org/core>\n"
				+ "FROM <http://rdf.glytoucan.org/sequence/wurcs>\n"
				+ "FROM <http://www.glycoinfo.org/graph/wurcs/0.4>\n"
				+ "WHERE {?SaccharideURI toucan:has_primary_id ?PrimaryId .\n"
				+ "GRAPH <http://www.glycoinfo.org/graph/wurcs/0.4> {?SaccharideURI glycan:has_glycosequence ?GlycoSequenceURI .\n"
				+ "?null wurcs:has_uniqueRES  ?uRES1, ?uRES2, ?uRES3 .\n"
				+ "?uRES1 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/x2122h-1x_1-5_2*NCC%2F3%3DO> .\n"
				+ "?uRES2 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/12112h-1b_1-5> .\n"
				+ "?uRES3 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/11221m-1a_1-5> .\n"
				+ "# RES\n"
				+ "?RESa wurcs:is_uniqueRES ?uRES1 .\n"
				+ "?RESb wurcs:is_uniqueRES ?uRES2 .\n"
				+ "?RESc wurcs:is_uniqueRES ?uRES3 .\n"
				+ "# LIN\n"
				+ "?gseq wurcs:has_LIN ?LINa3b1 , ?LINa4c1 . \n"
				+ "# LIN1\n"
				+ "  ?LINa3b1 wurcs:has_GLIPS   ?GLIPSa3 ,   ?GLIPSb1 .  \n"
				+ "# LIN2\n"
				+ "  ?LINa4c1 wurcs:has_GLIPS   ?GLIPSa4 ,   ?GLIPSc1 .  \n"
				+ "\n"
				+ " # LIN1: GLIPS1\n"
				+ "  ?GLIPSa3 wurcs:has_GLIP ?GLIPa3 . \n"
				+ "  ?GLIPa3 wurcs:has_SC_position 3 .\n"
				+ "  ?GLIPa3 wurcs:has_RES ?RESa .\n"
				+ "  ?GLIPSa3 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
				+ "# LIN1: GLIPS2\n"
				+ "  ?GLIPSb1 wurcs:has_GLIP ?GLIPb1 . \n"
				+ "  ?GLIPb1 wurcs:has_SC_position 1 .\n"
				+ "  ?GLIPb1 wurcs:has_RES ?RESb .\n"
				+ "  ?GLIPSb1 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
						+ "# LIN2: GLIPS1\n"
						+ "  ?GLIPSa4 wurcs:has_GLIP ?GLIPa4 . \n"
						+ "  ?GLIPa4 wurcs:has_SC_position 4 .\n"
						+ "  ?GLIPa4 wurcs:has_RES ?RESa .\n"
						+ "  ?GLIPSa4 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
								+ "# LIN2: GLIPS2\n"
								+ "  ?GLIPSc1 wurcs:has_GLIP ?GLIPc1 . \n"
								+ "  ?GLIPc1 wurcs:has_SC_position 1 .\n"
								+ "  ?GLIPc1 wurcs:has_RES ?RESc .\n"
								+ "  ?GLIPSc1 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
								+ "}}\n"
								+ "", search.getSparql());
	}
	
	// G07490EK
//	WURCS=2.0/2,2,1/[11122h-1b_1-5][21122h-1a_1-5]/1-2/a3-b1
	
			
			// 31MO
			// WURCS=2.0/2,2,1/[22112h-1a_1-5_2*NCC/3=O][12112h-1b_1-5]/1-2/a3-b1
//			@Test
			public void testSearchSelectSparqlG07490EK() throws SparqlException {
				SelectSparql search = getSelectSparql();
				SparqlEntity sparqlentity = new SparqlEntity();
				sparqlentity.setValue(GlycoSequence.URI, "rdf://sacharide.test");
				sparqlentity.setValue(GlycoSequence.Sequence, "WURCS=2.0/2,2,1/[11122h-1b_1-5][21122h-1a_1-5]/1-2/a3-b1");
				search.setSparqlEntity(sparqlentity);

				logger.debug(search.getSparql());
				assertEquals("PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
						+ "PREFIX toucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>\n"
						+ "SELECT DISTINCT ?SaccharideURI ?PrimaryId\n"
						+ "FROM <http://rdf.glytoucan.org/core>\n"
						+ "FROM <http://rdf.glytoucan.org/sequence/wurcs>\n"
						+ "FROM <http://www.glycoinfo.org/graph/wurcs/0.4>\n"
						+ "WHERE {?SaccharideURI toucan:has_primary_id ?PrimaryId .\n"
						+ "GRAPH <http://www.glycoinfo.org/graph/wurcs/0.4> {?SaccharideURI glycan:has_glycosequence ?GlycoSequenceURI .\n"
						+ "?null wurcs:has_uniqueRES  ?uRES1, ?uRES2, ?uRES3 .\n"
						+ "?uRES1 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/x2122h-1x_1-5_2*NCC%2F3%3DO> .\n"
						+ "?uRES2 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/12112h-1b_1-5> .\n"
						+ "?uRES3 wurcs:is_monosaccharide <http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/11221m-1a_1-5> .\n"
						+ "# RES\n"
						+ "?RESa wurcs:is_uniqueRES ?uRES1 .\n"
						+ "?RESb wurcs:is_uniqueRES ?uRES2 .\n"
						+ "?RESc wurcs:is_uniqueRES ?uRES3 .\n"
						+ "# LIN\n"
						+ "?gseq wurcs:has_LIN ?LINa3b1 , ?LINa4c1 . \n"
						+ "# LIN1\n"
						+ "  ?LINa3b1 wurcs:has_GLIPS   ?GLIPSa3 ,   ?GLIPSb1 .  \n"
						+ "# LIN2\n"
						+ "  ?LINa4c1 wurcs:has_GLIPS   ?GLIPSa4 ,   ?GLIPSc1 .  \n"
						+ "\n"
						+ " # LIN1: GLIPS1\n"
						+ "  ?GLIPSa3 wurcs:has_GLIP ?GLIPa3 . \n"
						+ "  ?GLIPa3 wurcs:has_SC_position 3 .\n"
						+ "  ?GLIPa3 wurcs:has_RES ?RESa .\n"
						+ "  ?GLIPSa3 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
						+ "# LIN1: GLIPS2\n"
						+ "  ?GLIPSb1 wurcs:has_GLIP ?GLIPb1 . \n"
						+ "  ?GLIPb1 wurcs:has_SC_position 1 .\n"
						+ "  ?GLIPb1 wurcs:has_RES ?RESb .\n"
						+ "  ?GLIPSb1 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
								+ "# LIN2: GLIPS1\n"
								+ "  ?GLIPSa4 wurcs:has_GLIP ?GLIPa4 . \n"
								+ "  ?GLIPa4 wurcs:has_SC_position 4 .\n"
								+ "  ?GLIPa4 wurcs:has_RES ?RESa .\n"
								+ "  ?GLIPSa4 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
										+ "# LIN2: GLIPS2\n"
										+ "  ?GLIPSc1 wurcs:has_GLIP ?GLIPc1 . \n"
										+ "  ?GLIPc1 wurcs:has_SC_position 1 .\n"
										+ "  ?GLIPc1 wurcs:has_RES ?RESc .\n"
										+ "  ?GLIPSc1 wurcs:isFuzzy \"false\"^^xsd:boolean .\n"
										+ "}}\n"
										+ "", search.getSparql());
			}
	
			// G51379UR
			//WURCS=2.0/2,2,1/[11122h-1b_1-5][21122h-1a_1-5]/1-2/a6-b1
		
//			@Test
			public void testSearchSelectSparqlG51379UR() throws SparqlException {
				SelectSparql search = getSelectSparql();
				SparqlEntity sparqlentity = new SparqlEntity();
				sparqlentity.setValue(GlycoSequence.URI, "rdf://sacharide.test");
				sparqlentity.setValue(GlycoSequence.Sequence, "WURCS=2.0/2,2,1/[11122h-1b_1-5][21122h-1a_1-5]/1-2/a6-b1");
				search.setSparqlEntity(sparqlentity);

				logger.debug(search.getSparql());
				assertEquals("", search.getSparql());
			}

			// G72618IM
			// WURCS=2.0/2,3,2/[11122h-1b_1-5][21122h-1a_1-5]/1-2-2/a3-b1_a6-c1

//			@Test
			public void testSearchSelectSparqlG72618IM() throws SparqlException {
				SelectSparql search = getSelectSparql();
				SparqlEntity sparqlentity = new SparqlEntity();
				sparqlentity.setValue(GlycoSequence.URI, "rdf://sacharide.test");
				sparqlentity.setValue(GlycoSequence.Sequence, "WURCS=2.0/2,3,2/[11122h-1b_1-5][21122h-1a_1-5]/1-2-2/a3-b1_a6-c1");
				search.setSparqlEntity(sparqlentity);

				logger.debug(search.getSparql());
				assertEquals("", search.getSparql());
			}
			
//	@Test
//	public void testInsertSparql() {
//		ConvertInsertSparql convert = getConvertInsertSparql();
//		SparqlEntity se = new SparqlEntity();
//		se.setValue("SaccharideURI", "<testSaccharideURI>");
//		se.setValue("SequenceURI", "<testSequenceURI>");
//		se.setValue("ConvertedSequence", "testConvertedSequence!");
//		convert.setSparqlEntity(se);
//		assertEquals("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
//				+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
//				+ "PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
//				+ "PREFIX glytoucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>\n"
//				+ "INSERT INTO\n"
//				+ "GRAPH <http://glytoucan.org/rdf/demo/0.7wurcs>\n"
//				+ "USING <http://glytoucan.org/rdf/demo/0.2>\n"
//				+ "USING <http://glytoucan.org/rdf/demo/msdb/8>\n"
//				+ "USING <http://purl.jp/bio/12/glyco/glycan/ontology/0.18>\n"
//				+ "USING <http://www.glytoucan.org/glyco/owl/glytoucan>\n"
//				+ "{ <testSaccharideURI> glycan:has_glycosequence <testSequenceURI> .\n"
//				+ "<testSequenceURI> glycan:has_sequence \"testConvertedSequence!\"^^xsd:string .\n"
//				+ "<testSequenceURI> glycan:in_carbohydrate_format glycan:carbohydrate_format_wurcs .\n"
//				+ "<testSequenceURI> glytoucan:is_glycosequence_of <testSaccharideURI> .\n }\n", convert.getSparql());
//	}
			
//			@Test
			public void testSelectSparql2() throws SparqlException {
				SelectSparql search = getSelectSparql();
				SparqlEntity sparqlentity = new SparqlEntity();
				sparqlentity.setValue(GlycoSequence.URI, "rdf://sacharide.test");
				sparqlentity.setValue(GlycoSequence.Sequence, "WURCS=2.0/2,2,1/[a2112h-1x_1-5][a2122h-1x_1-5]/1-2/a?-b1");
				search.setSparqlEntity(sparqlentity);
				
				List<SparqlEntity> results = sparqlDAO.query(search);
				
				Assert.assertTrue(results.size() > 0);
			}
			
//			@Test
			public void testIsomerSubSelectSparqlG80382WR() throws SparqlException {
				IsomerSubstructureSearchSparql search = new IsomerSubstructureSearchSparql();
				search.setFilterOutSelf(true);
			    String graphtarget = "<http://rdf.glytoucan.org/sequence/wurcs>";
			    String graphms = "<http://rdf.glytoucan.org/wurcs/ms>";

				search.setGraphms(graphms);
				search.setGraphtarget(graphtarget);

				SparqlEntity sparqlentity = new SparqlEntity();
				sparqlentity.setValue(GlycoSequence.URI, "http://rdf.glytoucan.org/");
				sparqlentity.setValue(GlycoSequence.Sequence, "WURCS=2.0/5,9,8/[a2122h-1x_1-5_2*NCC/3=O][a2122h-1b_1-5_2*NCC/3=O][a1122h-1b_1-5][a1122h-1a_1-5][axxxxh-1b_1-?_2*NCC/3=O]/1-2-3-4-2-4-2-5-5/a4-b1_b4-c1_c3-d1_c6-f1_d2-e1_f2-g1_h1-a?|b?|c?|d?|e?|f?|g?}_i1-a?|b?|c?|d?|e?|f?|g?}");
				search.setSparqlEntity(sparqlentity);
				
				logger.debug(search.getSparql());
				
				List<SparqlEntity> results = sparqlDAO.query(search);
				
				Assert.assertTrue(results.size() > 0);
			}
			
			
//			G09256ZZ
					
//					@Test
					public void testIsomerSubSelectSparqlG09256ZZ() throws SparqlException {
						IsomerSubstructureSearchSparql search = new IsomerSubstructureSearchSparql();
						search.setFilterOutSelf(true);
					    String graphtarget = "<http://rdf.glytoucan.org/sequence/wurcs>";
					    String graphms = "<http://rdf.glytoucan.org/wurcs/ms>";

						search.setGraphms(graphms);
						search.setGraphtarget(graphtarget);

						SparqlEntity sparqlentity = new SparqlEntity();
						sparqlentity.setValue(GlycoSequence.URI, "http://rdf.glytoucan.org/");
						sparqlentity.setValue(GlycoSequence.Sequence, "WURCS=2.0/5,7,6/[h2122h_2*NCC/3=O][a2112h-1b_1-5][Aad21122h-2a_2-6][a1221m-1a_1-5][a2122h-1b_1-5_2*NCC/3=O]/1-2-3-4-5-2-4/a3-b1_a6-e1_b3-c2_c4-d1_e4-f1_f2-g1");
						search.setSparqlEntity(sparqlentity);
						
						logger.debug(search.getSparql());
						
						List<SparqlEntity> results = sparqlDAO.query(search);
						
						Assert.assertTrue(results.size() > 0);
					}
}