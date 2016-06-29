package org.glycoinfo.rdf.dao.virt;

import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.utils.TripleStoreProperties;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import virtuoso.sesame2.driver.VirtuosoRepository;

@Configuration
public class VirtSesameTransactionConfig {

	@Bean
	SparqlDAO getSparqlDAO() {
		return new SparqlDAOVirtSesameImpl();
	}
	
  @Bean
  public TripleStoreProperties getTripleStoreProperties() {
	  return new TripleStoreProperties();
	}
	
	@Bean
	public Repository getRepository() {
		return new VirtuosoRepository(
		    getTripleStoreProperties().getUrl(), 
		    getTripleStoreProperties().getUsername(),
		    getTripleStoreProperties().getPassword());
	}

	@Bean
	VirtSesameTransactionManager transactionManager() throws RepositoryException {
		return new VirtSesameTransactionManager(getSesameConnectionFactory());
	}

	@Bean
	VirtSesameConnectionFactory getSesameConnectionFactory() {
		return new VirtRepositoryConnectionFactory(getRepository());
	}
}