package org.glycoinfo.batch.glyconvert;

import org.glycoinfo.batch.SparqlItemReader;
import org.glycoinfo.batch.SparqlItemWriter;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SparqlItemConfig {
	
	private int pageSize = 10000;
	
	@Bean
	public SparqlItemWriter<SparqlEntity> sparqlItemWriter() {
		SparqlItemWriter<SparqlEntity> reader = new SparqlItemWriter<SparqlEntity>();
		return reader;
	}
	
	@Bean
	SparqlItemReader<SparqlEntity> sparqlItemReader() {
		SparqlItemReader<SparqlEntity> reader = new SparqlItemReader<SparqlEntity>();
		reader.setPageSize(pageSize);
		return reader;
	}
}
