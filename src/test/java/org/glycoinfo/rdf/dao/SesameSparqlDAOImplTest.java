package org.glycoinfo.rdf.dao;

import org.glycoinfo.rdf.SparqlException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = SesameDAOTestConfig.class)
public class SesameSparqlDAOImplTest {

	public static Logger logger = (Logger) LoggerFactory
			.getLogger("org.glytoucan.registry.dao.test.SchemaDAOImplTest");

	@Autowired
	SparqlDAO schemaDAO;
	
	@Test
	public void testLoad() throws SparqlException {
		schemaDAO.load("/home/aoki/workspace/rdfs/schema.org.rdf.xml");
	}
}