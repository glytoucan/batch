package org.glycoinfo.batch.glyconvert;

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
public class GlyConvertSparqlItemConfig {

  private int pageSize = 10000;
  
  @Autowired
  ConvertSparqlProcessor convertSparqlProcessor;

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

  @Bean
  public ItemProcessor<SparqlEntity, SparqlEntity> processor() {
    return convertSparqlProcessor;
  }
  
  @Bean
  public ConvertSparqlProcessor convertSparqlProcessor() {
    return new ConvertSparqlProcessor();
  }
  // @Bean
  // public Job importUserJob(JobBuilderFactory jobs, Step s1) {
  // return jobs.get("ConvertWurcs").incrementer(new
  // RunIdIncrementer()).flow(s1).end().build();
  // }
  //
  // @Bean
  // public Step step1(StepBuilderFactory stepBuilderFactory,
  // ItemReader<SparqlEntity> reader,
  // ItemWriter<SparqlEntity> writer, ItemProcessor<SparqlEntity, SparqlEntity>
  // processor) {
  // return stepBuilderFactory.get("step1").<SparqlEntity, SparqlEntity>
  // chunk(10).reader(reader)
  // .processor(processor).writer(writer).build();
  // }
}
