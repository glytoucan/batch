package org.glycoinfo.rdf.dao;

import org.glycoinfo.rdf.dao.virt.SparqlDAOVirtSesameImpl;
import org.glycoinfo.rdf.dao.virt.VirtRepositoryConnectionFactory;
import org.glycoinfo.rdf.dao.virt.VirtSesameConnectionFactory;
import org.glycoinfo.rdf.dao.virt.VirtSesameTransactionManager;
import org.glycoinfo.rdf.utils.TripleStoreProperties;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;
import org.openrdf.spring.RepositoryConnectionFactory;
import org.openrdf.spring.SesameConnectionFactory;
import org.openrdf.spring.SesameTransactionManager;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

import virtuoso.sesame2.driver.VirtuosoRepository;

@Configuration
@EnableAutoConfiguration
public class VirtSesameDAOTestConfig {

	@Bean
	SparqlDAO getSparqlDAO() {
		return new SparqlDAOVirtSesameImpl();
	}
	
	@Bean
	VirtSesameConnectionFactory getSesameConnectionFactory() {
		return new VirtRepositoryConnectionFactory(getRepository());
	}
	
	@Bean
	public Repository getRepository() {
		return new VirtuosoRepository(
				getTripleStoreProperties().getUrl(), 
				getTripleStoreProperties().getUsername(),
				getTripleStoreProperties().getPassword());
	}

	@Bean
	TripleStoreProperties getTripleStoreProperties() {
		return new TripleStoreProperties();
	}
	
	@Bean
	VirtSesameTransactionManager transactionManager() throws RepositoryException {
		return new VirtSesameTransactionManager(getSesameConnectionFactory());
	}
}