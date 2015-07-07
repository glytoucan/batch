package org.glycoinfo.rdf.dao;

import org.glycoinfo.rdf.utils.TripleStoreProperties;
import org.openrdf.repository.Repository;
import org.openrdf.repository.http.HTTPRepository;
import org.openrdf.spring.RepositoryConnectionFactory;
import org.openrdf.spring.SesameConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
public class BigdataDAOTestConfig {
	
    @Value("${triplestore.blazegraph.url}")
    private String url;
	
	@Bean
	SparqlDAO getSparqlDAO() {
		return new SparqlDAOBigdataImpl();
	}

	@Bean
	TripleStoreProperties getTripleStoreProperties() {
		return new TripleStoreProperties();
	}
	
	@Bean
	SesameConnectionFactory getSesameConnectionFactory() {
		return new RepositoryConnectionFactory(getRepository());
	}

	@Bean
	public Repository getRepository() {
		return new HTTPRepository(url);
	}
}