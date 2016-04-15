package org.glycoinfo.rdf.dao;

import org.glycoinfo.rdf.utils.TripleStoreProperties;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
public class JenaDAOTestConfig {

//	@Bean
//	SparqlDAO getSparqlDAO() {
//		return new SparqlDAOJenaImpl();
//	}

	@Bean
	TripleStoreProperties getTripleStoreProperties() {
		return new TripleStoreProperties();
	}
}