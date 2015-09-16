package org.glycoinfo.rdf.glycan.msdb;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.glycoinfo.client.MSdbClient;
import org.glycoinfo.rdf.InsertSparql;
import org.glycoinfo.rdf.InsertSparqlBean;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.dao.VirtSesameDAOTestConfig;
import org.glycoinfo.rdf.glycan.GlycoSequence;
import org.glycoinfo.rdf.glycan.Monosaccharide;
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
@SpringApplicationConfiguration(classes = { MonosaccharideBeanTest.class , VirtSesameDAOTestConfig.class })
@Configuration
@EnableAutoConfiguration
public class MonosaccharideBeanTest {
	public static Logger logger = (Logger) LoggerFactory
			.getLogger(MonosaccharideBeanTest.class);
	
	@Autowired
	SparqlDAO sparqlDAO;
	
	@Bean
	MSdbClient msdbClient() {
		return new MSdbClient();
	}
	
	@Autowired
	public MSInsertSparql msInsertSparql;

	
	@Test
	@Transactional
	public void testInsertMS3() throws SparqlException, UnsupportedEncodingException {

	String where = "PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>"				+ "		INSERT DATA\n"
				+ "		{ GRAPH <http://rdf.test.glycoinfo.org>\n"
				+ "		{ \n"
				+ "<http://www.monosaccharidedb.org/rdf/monosaccharide_alias.action?scheme=MONOSACCHARIDEDB&name=a-dall-HEX-1:5\\|2,3:anhydro>\n"
	+ " a       <http://purl.jp/bio/12/glyco/glycan#monosaccharide_alias> ;\n"
	+ "        <http://purl.jp/bio/12/glyco/glycan#has_alias_name>\n"
	+ "                \"a-dall-HEX-1:5|2,3:anhydro\" ;\n"
	+ "        <http://purl.jp/bio/12/glyco/glycan#has_monosaccharide_notation_scheme>\n"
	+ "                <http://purl.jp/bio/12/glyco/glycan#monosaccharide_notation_scheme_monosaccharidedb> ;\n"
	+ "        <http://purl.jp/bio/12/glyco/glycan#is_primary_name>\n"
	+ "                true ;\n"
	+ "        <http://purl.jp/bio/12/glyco/glycan#is_trivial_name>\n"
	+ "                false ;\n"
	+ "        <http://www.w3.org/2002/07/owl#sameAs>\n"
	+ "                <http://www.monosaccharidedb.org/rdf/monosaccharide.action?name=a-dall-HEX-1:5\\|2,3:anhydro> .\n"
	+ "\n"
//	+ "<http://www.monosaccharidedb.org/rdf/monosaccharide_alias.action?scheme=BCSDB&name=aD2,3anhAllp>\n"
//	+ "        a       <http://purl.jp/bio/12/glyco/glycan#monosaccharide_alias> ;\n"
//	+ "        <http://purl.jp/bio/12/glyco/glycan#has_alias_name>\n"
//	+ "                \"aD2,3anhAllp\" ;\n"
//	+ "        <http://purl.jp/bio/12/glyco/glycan#has_monosaccharide_notation_scheme>\n"
//	+ "                <http://purl.jp/bio/12/glyco/glycan#monosaccharide_notation_scheme_bcsdb> ;\n"
//	+ "        <http://purl.jp/bio/12/glyco/glycan#is_primary_name>\n"
//	+ "                true ;\n"
//	+ "        <http://purl.jp/bio/12/glyco/glycan#is_trivial_name>\n"
//	+ "                false ;\n"
//	+ "        <http://www.w3.org/2002/07/owl#sameAs>\n"
//	+ "                <http://www.monosaccharidedb.org/rdf/monosaccharide.action?name=a-dall-HEX-1:5|2,3:anhydro> .\n"
//	+ "\n"
//	+ "<http://www.monosaccharidedb.org/rdf/monosaccharide_alias.action?scheme=GLYCOSCIENCES&name=a-D-2,3-anhydro-Allp>\n"
//	+ "        a       <http://purl.jp/bio/12/glyco/glycan#monosaccharide_alias> ;\n"
//	+ "        <http://purl.jp/bio/12/glyco/glycan#has_alias_name>\n"
//	+ "                \"a-D-2,3-anhydro-Allp\" ;\n"
//			+ "        <http://purl.jp/bio/12/glyco/glycan#has_monosaccharide_notation_scheme>\n"
//			+ "                <http://purl.jp/bio/12/glyco/glycan#monosaccharide_notation_scheme_glycosciences_de> ;\n"
//			+ "        <http://purl.jp/bio/12/glyco/glycan#is_primary_name>\n"
//			+ "                true ;\n"
//			+ "        <http://purl.jp/bio/12/glyco/glycan#is_trivial_name>\n"
//			+ "                false ;\n"
//			+ "        <http://www.w3.org/2002/07/owl#sameAs>\n"
//			+ "                <http://www.monosaccharidedb.org/rdf/monosaccharide.action?name=a-dall-HEX-1:5|2,3:anhydro> .\n"
//			+ "\n"
//			+ "<http://www.monosaccharidedb.org/rdf/monosaccharide_alias.action?scheme=IUPAC&name=2,3-anhydro-alpha-D-Allopyranose>\n"
//			+ "        a       <http://purl.jp/bio/12/glyco/glycan#monosaccharide_alias> ;\n"
//			+ "        <http://purl.jp/bio/12/glyco/glycan#has_alias_name>\n"
//			+ "                \"2,3-anhydro-alpha-D-Allopyranose\" ;\n"
//			+ "        <http://purl.jp/bio/12/glyco/glycan#has_monosaccharide_notation_scheme>\n"
//			+ "                <http://purl.jp/bio/12/glyco/glycan#monosaccharide_notation_scheme_iupac> ;\n"
//			+ "        <http://purl.jp/bio/12/glyco/glycan#is_primary_name>\n"
//			+ "                true ;\n"
//			+ "        <http://purl.jp/bio/12/glyco/glycan#is_trivial_name>\n"
//			+ "                false ;\n"
//			+ "        <http://www.w3.org/2002/07/owl#sameAs>\n"
//			+ "                <http://www.monosaccharidedb.org/rdf/monosaccharide.action?name=a-dall-HEX-1:5|2,3:anhydro> .\n"
//			+ "\n"
//			+ "<http://www.monosaccharidedb.org/rdf/monosaccharide.action?name=a-dall-HEX-1:5|2,3:anhydro> \n"
//			+ "        a       <http://purl.jp/bio/12/glyco/glycan#monosaccharide> ;\n"
//			+ "        <http://www.w3.org/2000/01/rdf-schema#seeAlso>\n"
//			+ "                <http://www.monosaccharidedb.org/display_monosaccharide.action?name=a-dall-HEX-1:5|2,3:anhydro&scheme=msdb> ;\n"
//			+ "        <http://purl.jp/bio/12/glyco/glycan#has_basetype>\n"
//			+ "                <http://www.monosaccharidedb.org/rdf/basetype.action?name=a-dall-HEX-1:5|2,3:anhydro&scheme=msdb> ;\n"
//			+ "        <http://purl.jp/bio/12/glyco/glycan#has_linkage_position>\n"
//			+ "                4 , 1 , 6 ;\n"
//			+ "        <http://purl.jp/bio/12/glyco/glycan#has_monoisotopic_molecular_weight>\n"
//			+ "                \"162.05282343149997\"^^<http://www.w3.org/2001/XMLSchema#double> ;\n"
//			+ "        <http://www.w3.org/2002/07/owl#sameAs>\n"
//			+ "                <http://www.monosaccharidedb.org/rdf/monosaccharide.action?name=a-dall-HEX-1:5|2,3:anhydro> .\n"
//			+ "\n"
//			+ "<http://www.monosaccharidedb.org/rdf/monosaccharide_alias.action?scheme=CARBBANK&name=a-D-2,3-anhydro-Allp>\n"
//			+ "        a       <http://purl.jp/bio/12/glyco/glycan#monosaccharide_alias> ;\n"
//			+ "        <http://purl.jp/bio/12/glyco/glycan#has_alias_name>\n"
//			+ "                \"a-D-2,3-anhydro-Allp\" ;\n"
//					+ "        <http://purl.jp/bio/12/glyco/glycan#has_monosaccharide_notation_scheme>\n"
//					+ "                <http://purl.jp/bio/12/glyco/glycan#monosaccharide_notation_scheme_carbbank> ;\n"
//					+ "        <http://purl.jp/bio/12/glyco/glycan#is_primary_name>\n"
//					+ "                true ;\n"
//					+ "        <http://purl.jp/bio/12/glyco/glycan#is_trivial_name>\n"
//					+ "                false ;\n"
//					+ "        <http://www.w3.org/2002/07/owl#sameAs>\n"
//					+ "                <http://www.monosaccharidedb.org/rdf/monosaccharide.action?name=a-dall-HEX-1:5|2,3:anhydro> .\n"
//					+ "\n"
//					+ "<http://www.monosaccharidedb.org/rdf/monosaccharide_alias.action?scheme=GLYCOCT&name=a-dall-HEX-1:5&substName=epoxy&substPos=2&substLink=d>\n"
//					+ "        a       <http://purl.jp/bio/12/glyco/glycan#monosaccharide_alias> ;\n"
//					+ "        <http://purl.jp/bio/12/glyco/glycan#has_alias_name>\n"
//					+ "                \"a-dall-HEX-1:5\" ;\n"
//							+ "        <http://purl.jp/bio/12/glyco/glycan#has_external_substituent>\n"
//							+ "                [ <http://purl.jp/bio/12/glyco/glycan#has_linkage_type>\n"
//							+ "                          <http://purl.jp/bio/12/glyco/glycan#linkage_type_deoxy> ;\n"
//							+ "                  <http://purl.jp/bio/12/glyco/glycan#has_modification_position>\n"
//							+ "                          \"2\" ;\n"
//							+ "                  <http://purl.jp/bio/12/glyco/glycan#has_substitution_name>\n"
//							+ "                          \"epoxy\"\n"
//							+ "                ] ;\n"
//							+ "        <http://purl.jp/bio/12/glyco/glycan#has_monosaccharide_notation_scheme>\n"
//							+ "                <http://purl.jp/bio/12/glyco/glycan#monosaccharide_notation_scheme_glycoct> ;\n"
//							+ "        <http://purl.jp/bio/12/glyco/glycan#is_primary_name>\n"
//							+ "                true ;\n"
//							+ "        <http://purl.jp/bio/12/glyco/glycan#is_trivial_name>\n"
//							+ "                false ;\n"
//							+ "        <http://www.w3.org/2002/07/owl#sameAs>\n"
//							+ "                <http://www.monosaccharidedb.org/rdf/monosaccharide.action?name=a-dall-HEX-1:5|2,3:anhydro> .\n"
							+ " } }";
		InsertSparqlBean isb = new InsertSparqlBean(where);
		sparqlDAO.insert(isb);

	}

	@Test
	@Transactional
	public void testInsertMS() throws SparqlException, UnsupportedEncodingException {
		

		sparqlDAO.insert(msInsertSparql);
		
//		List<SparqlEntity> list = sparqlDAO.query(msInsertSparql());
//		if (list.iterator().hasNext()) {
//			SparqlEntity se = list.iterator().next();
//			logger.debug(se.getValue(SaccharideSelectSparql.SaccharideURI));
//		}
	}

	@Test
	@Transactional
	public void testInsertMS2() throws SparqlException, UnsupportedEncodingException {

		InsertSparqlBean isb = new InsertSparqlBean(	"PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
				+ "		INSERT DATA\n"
				+ "		{ GRAPH <http://rdf.test.glycoinfo.org>\n"
				+ "		{ \n"
				+ "		<http://www.monosaccharidedb.org/rdf/monosaccharide_alias.action?scheme=CARBBANK&name=a-D-2,3-anhydro-Allp>\n"
				+ "		a       glycan:monosaccharide_alias ;\n"
				+ "		glycan:has_alias_name\n"
				+ "		\"a-D-2,3-anhydro-Allp\" ;\n"
						+ "		glycan:has_monosaccharide_notation_scheme\n"
						+ "		\"glycan:monosaccharide_notation_scheme_carbbank\" ;\n"
								+ "		glycan:is_primary_name\n"
								+ "		true ;\n"
										+ "		glycan:is_trivial_name\n"
										+ "		false ;\n"
												+ "		<http://www.w3.org/2002/07/owl#sameAs>\n"
												+ "		\"http://www.monosaccharidedb.org/rdf/monosaccharide.action?name=a-dall-HEX-1:5|2,3:anhydro\" .\n"
												+ "\n"
												+ "<http://www.monosaccharidedb.org/rdf/monosaccharide_alias.action?scheme=CARBBANK&name=a-D-2,3-anhydro-Allp> \n"
												+ "        a       <http://purl.jp/bio/12/glyco/glycan#monosaccharide_alias> ;\n"
												+ "        <http://purl.jp/bio/12/glyco/glycan#has_alias_name>\n"
												+ "                \"a-D-2,3-anhydro-Allp\" ;\n"
												+ "        <http://purl.jp/bio/12/glyco/glycan#has_monosaccharide_notation_scheme>\n"
												+ "                <http://purl.jp/bio/12/glyco/glycan#monosaccharide_notation_scheme_carbbank> ;\n"
												+ "        <http://purl.jp/bio/12/glyco/glycan#is_primary_name>\n"
												+ "                true ;\n"
												+ "        <http://purl.jp/bio/12/glyco/glycan#is_trivial_name>\n"
												+ "                false ;\n"
												+ "        <http://www.w3.org/2002/07/owl#sameAs>\n"
												+ "                <http://www.monosaccharidedb.org/rdf/monosaccharide.action?name=a-dall-HEX-1:5|2,3:anhydro> .\n"
												+ " } }");

		sparqlDAO.insert(isb);
		
//		List<SparqlEntity> list = sparqlDAO.query(msInsertSparql());
//		if (list.iterator().hasNext()) {
//			SparqlEntity se = list.iterator().next();
//			logger.debug(se.getValue(SaccharideSelectSparql.SaccharideURI));
//		}
	}
	
	
	@Bean
	public MSInsertSparql msInsertSparql() {
		MSInsertSparql wrss = new MSInsertSparql();
		wrss.setGraph("http://rdf.test.glycoinfo.org");
		SparqlEntity se = new SparqlEntity();
		se.setValue(Monosaccharide.Residue, "a-dall-HEX-1:5|2,3:anhydro");
		wrss.setSparqlEntity(se);
		return wrss;
	}
}