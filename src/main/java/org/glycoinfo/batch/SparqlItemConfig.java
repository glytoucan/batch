package org.glycoinfo.batch;

import org.glycoinfo.batch.SparqlItemReader;
import org.glycoinfo.batch.SparqlItemWriter;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
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

  @Autowired
  SparqlItemReader<SparqlEntity> sparqlItemReader;

  @Autowired
  SparqlItemWriter<SparqlEntity> sparqlItemWriter;

  @Bean
  public ItemReader<SparqlEntity> reader() {
    return sparqlItemReader;
  }

  @Bean
  public ItemWriter<SparqlEntity> writer() {
    return sparqlItemWriter;
  }
}
