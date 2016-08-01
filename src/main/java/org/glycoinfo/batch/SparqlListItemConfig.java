package org.glycoinfo.batch;

import java.util.List;

import org.glycoinfo.rdf.dao.SparqlEntity;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SparqlListItemConfig {

  @Value("${pageSize:10000}")
  private int pageSize = 10000;
  
  @Bean
  public ItemReader<SparqlEntity> reader() {
    SparqlItemReader<SparqlEntity> reader = new SparqlItemReader<SparqlEntity>();
    reader.setPageSize(pageSize);
    return reader;
  }

  @Bean
  public ItemWriter<List<SparqlEntity>> writer() {
    SparqlListWriter<List<SparqlEntity>> writer = new SparqlListWriter<List<SparqlEntity>>();
    return writer;
  } 
}