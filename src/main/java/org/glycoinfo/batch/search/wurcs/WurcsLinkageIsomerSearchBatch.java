package org.glycoinfo.batch.search.wurcs;

import java.util.ArrayList;
import java.util.List;

import org.glycoinfo.batch.SparqlItemReader;
import org.glycoinfo.batch.SparqlListProcessor;
import org.glycoinfo.batch.SparqlListWriter;
import org.glycoinfo.rdf.InsertSparql;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.dao.virt.SparqlDAOVirtSesameImpl;
import org.glycoinfo.rdf.dao.virt.VirtRepositoryConnectionFactory;
import org.glycoinfo.rdf.dao.virt.VirtSesameConnectionFactory;
import org.glycoinfo.rdf.dao.virt.VirtSesameTransactionConfig;
import org.glycoinfo.rdf.dao.virt.VirtSesameTransactionManager;
import org.glycoinfo.rdf.glycan.GlycoSequence;
import org.glycoinfo.rdf.glycan.relation.LinkageIsomerInsertSparql;
import org.glycoinfo.rdf.glycan.wurcs.WurcsGlycoSequenceSelectSparql;
import org.glycoinfo.rdf.utils.TripleStoreProperties;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import virtuoso.sesame2.driver.VirtuosoRepository;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = ("org.glycoinfo.batch.search.wurcs"))
@SpringApplicationConfiguration(classes = { WurcsLinkageIsomerSearchBatch.class, VirtSesameTransactionConfig.class } )
@EnableBatchProcessing
public class WurcsLinkageIsomerSearchBatch {
	
	@Value("${wurcs.graph.target}")
	private String graphtarget;

	@Value("${wurcs.graph.ms}")
	private String graphms;

	// graph base to set the graph to insert into. The format type (toFormat()) will be added to the end. 
	// example: http://glytoucan.org/rdf/demo/0.7/wurcs
	public static String graphbase = "http://rdf.glytoucan.org/isomer";
	private int pageSize = 10000;

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		ApplicationContext ctx = SpringApplication.run(
				WurcsLinkageIsomerSearchBatch.class, args);
	}

	@Bean(name="itemReaderSelectSparql")
	SelectSparql getSelectSparql() {
		SelectSparql select = new WurcsGlycoSequenceSelectSparql();
		SparqlEntity se = new SparqlEntity();
		List<String> ignores = new ArrayList<>();
		ignores.add("G70676RO");
		ignores.add("G92020OA");
		ignores.add("G92150BS");
		ignores.add("G54517TS");
		ignores.add("G03842PF");
		ignores.add("G25700FN");
		ignores.add("G26252RJ");
		ignores.add("G32534WD");
		ignores.add("G39503QD");
		ignores.add("G42520LX");
		ignores.add("G54517TS");
		ignores.add("G34809KE");
		ignores.add("G75329TA");
		
		// from :https://docs.google.com/spreadsheets/d/1pFpN8YtyN7Xu7WXqTdnp1NBaRfIsQFUuRA_vrC4Y-wk/edit#gid=1200173560
		se.setValue(GlycoSequence.IdentifiersToIgnore, ignores);
		select.setSparqlEntity(se);
		select.setFrom("FROM <http://rdf.glytoucan.org/core>\nFROM <http://rdf.glytoucan.org/sequence/wurcs>\nFROM <http://rdf.glytoucan.org/isomer>\n");
		return select;
	}

	@Bean
	InsertSparql getInsertSparql() {
		LinkageIsomerInsertSparql insert = new LinkageIsomerInsertSparql();
		insert.setGraphBase(graphbase);
		return insert;
	}

	@Bean
	SparqlDAO getSparqlDAO() {
		return new SparqlDAOVirtSesameImpl();
	}

//	@Bean
//	TripleStoreProperties getTripleStoreProperties() {
//		return new TripleStoreProperties();
//	}

	@Bean
	public ItemReader<SparqlEntity> reader() {
		SparqlItemReader<SparqlEntity> reader = new SparqlItemReader<SparqlEntity>();
		reader.setSelectSparql(getSelectSparql());
		reader.setPageSize(pageSize);
		return reader;
	}

	@Bean
	public ItemWriter<List<SparqlEntity>> writer() {
		SparqlListWriter<List<SparqlEntity>> writer = new SparqlListWriter<List<SparqlEntity>>();
		writer.setInsertSparql(getInsertSparql());
		return writer;
	}

	@Bean
	public Job importUserJob(JobBuilderFactory jobs, Step s1) {
		return jobs.get("WurcsLinkageIsomer").incrementer(new RunIdIncrementer())
				.flow(s1).end().build();
	}

	@Bean
	public Step step1(StepBuilderFactory stepBuilderFactory,
			ItemReader<SparqlEntity> reader, ItemWriter<List<SparqlEntity>> writer,
			ItemProcessor<SparqlEntity, List<SparqlEntity>> processor) {
		return stepBuilderFactory.get("step1")
				.<SparqlEntity, List<SparqlEntity>> chunk(1).reader(reader)
				.processor(processor).writer(writer).build();
	}

	@Bean
	public ItemProcessor<SparqlEntity, List<SparqlEntity>> processor() {
		SparqlListProcessor process = new SparqlListProcessor();
		IsomerSubstructureSearchSparql subSearchSparql = new IsomerSubstructureSearchSparql();
//		subSearchSparql.setFilterOutSelf(true);
		subSearchSparql.setGraphms(graphms);
		subSearchSparql.setGraphtarget(graphtarget);
		process.setSelectSparql(subSearchSparql);
//		process.setConverter(new MotifConverter());
		process.setPostConverter(new LinkageIsomerPostConverter());
		process.setPutall(true);
		return process;
	}
}