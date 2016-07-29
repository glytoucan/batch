package org.glycoinfo.batch.glyconvert;

import org.glycoinfo.batch.SparqlItemConfig;
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
import org.springframework.context.annotation.Import;

@Configuration
@Import(SparqlItemConfig.class)
public class GlyConvertSparqlItemConfig {

  @Autowired
  ConvertSparqlProcessor convertSparqlProcessor;

  @Bean
  public ItemProcessor<SparqlEntity, SparqlEntity> processor() {
    return convertSparqlProcessor;
  }
  
  @Bean
  public ConvertSparqlProcessor convertSparqlProcessor() {
    return new ConvertSparqlProcessor();
  }

}
