package org.glycoinfo.batch.glyconvert.wurcs;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SelectSparqlBean;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author aoki
 *
 * Test cases for the wurcs converter.  It will search for a number of glycoct that are not converted yet, store the number, execute the batch process.  If the number is less after the batch, it is successful.
 *
 * This work is licensed under the Creative Commons Attribution 4.0 International License. 
 * To view a copy of this license, visit http://creativecommons.org/licenses/by/4.0/.
 *
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {WurcsConvertSparqlBeanTest.class, WurcsConvertSparqlBatch.class})
@Configuration
//@EnableAutoConfiguration
public class WurcsConvertSparqlBeanTest {
	protected Log logger = LogFactory.getLog(getClass());

    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired
    SparqlDAO sparqlDAO;
    
    @Bean
    public JobLauncherTestUtils jobLauncherTestUtils() {
        return new JobLauncherTestUtils();
    }
    
    @Test
//    @Transactional
    public void testJob() throws Exception {
    	SelectSparql select = new SelectSparqlBean("PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
    			+ "SELECT count(distinct ?SaccharideURI) as ?test\n"
    			+ "FROM <http://rdf.glytoucan.org>\n"
    			+ "FROM <http://rdf.glytoucan.org/sequence/wurcs>\n"
    			+ "WHERE {\n"
    			+ "?SaccharideURI glycan:has_glycosequence ?GlycanSequenceURI .\n"
    			+ "?GlycanSequenceURI glycan:has_sequence ?Sequence .\n"
    			+ "?GlycanSequenceURI glycan:in_carbohydrate_format glycan:carbohydrate_format_glycoct .\n"
    			+ "FILTER NOT EXISTS {\n"
    			+ "?SaccharideURI glycan:has_glycosequence ?GlycanSequenceWURCSURI .\n"
    			+ "?GlycanSequenceWURCSURI glycan:in_carbohydrate_format glycan:carbohydrate_format_wurcs .\n"
    			+ "}\n"
    			+ "}\n");
    	List<SparqlEntity> results = sparqlDAO.query(select);
    	SparqlEntity next = results.iterator().next();
    	String count = next.getValue("test");
    	
    	logger.debug("count:>" + count);
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        Assert.assertEquals("COMPLETED", jobExecution.getExitStatus().getExitCode());

    	results = sparqlDAO.query(select);
    	next = results.iterator().next();
    	String countAfter = next.getValue("test");
    	Assert.assertTrue(Integer.getInteger(count) > Integer.getInteger(countAfter));
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