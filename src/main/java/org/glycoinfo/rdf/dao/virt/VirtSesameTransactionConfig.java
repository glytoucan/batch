package org.glycoinfo.rdf.dao.virt;

import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.utils.TripleStoreProperties;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
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
	TripleStoreProperties getTripleStoreProperties() {
		return new TripleStoreProperties();
	}
	
	@Bean
	VirtSesameConnectionFactory getSesameConnectionFactory() {
		return new VirtRepositoryConnectionFactory(getRepository());
	}
}
