package org.glycoinfo.rdf.dao;

import org.glycoinfo.rdf.utils.TripleStoreProperties;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@Configuration
@EnableAutoConfiguration
public class SesameDAOTestConfig {

	@Bean
	SparqlDAO getSparqlDAO() {
		return new SparqlDAOSesameImpl();
	}

	@Bean
	TripleStoreProperties getTripleStoreProperties() {
		return new TripleStoreProperties();
	}
}