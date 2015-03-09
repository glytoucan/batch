package org.glycoinfo.batch.search.wurcs;

import java.util.List;

import org.glycoinfo.batch.ListSparqlProcessor;
import org.glycoinfo.batch.SparqlItemReader;
import org.glycoinfo.batch.SparqlListWriter;
import org.glycoinfo.batch.search.SearchSparql;
import org.glycoinfo.rdf.InsertSparql;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlDAOSesameImpl;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.glycan.GlycoSequence;
import org.glycoinfo.rdf.glycan.MotifInsertSparql;
import org.glycoinfo.rdf.glycan.MotifSequenceSelectSparql;
import org.glycoinfo.rdf.utils.TripleStoreProperties;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = ("org.glycoinfo.batch.search.wurcs"))
@SpringApplicationConfiguration(classes = WurcsMotifSearchBatch.class)
@EnableBatchProcessing
public class WurcsMotifSearchBatch {

	// graph base to set the graph to insert into. The format type (toFormat()) will be added to the end. 
	// example: http://glytoucan.org/rdf/demo/0.7/wurcs
	public static String graphbase = "http://rdf.glytoucan.org/motif";
	private int pageSize = 10;

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		ApplicationContext ctx = SpringApplication.run(
				WurcsMotifSearchBatch.class, args);
	}

	@Bean
	SelectSparql getSelectSparql() {
		SelectSparql select = new MotifSequenceSelectSparql();
//		select.setFrom("FROM <http://rdf.glytoucan.org>\nFROM <http://rdf.glytoucan.org/sequence/wurcs>");
		return select;
	}

	@Bean
	InsertSparql getInsertSparql() {
		MotifInsertSparql insert = new MotifInsertSparql();
		insert.setGraphBase(graphbase);
		return insert;
	}

	SearchSparql getSearchSparql() {
		SearchSparqlBean ssb = new SearchSparqlBean();
		ssb.setGlycoSequenceUri(GlycoSequence.URI);
		return ssb;
	}
	
	@Bean
	SparqlDAO getSparqlDAO() {
		return new SparqlDAOSesameImpl();
	}

	@Bean
	TripleStoreProperties getTripleStoreProperties() {
		return new TripleStoreProperties();
	}

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
		return jobs.get("ConvertWurcs").incrementer(new RunIdIncrementer())
				.flow(s1).end().build();
	}

	@Bean
	public Step step1(StepBuilderFactory stepBuilderFactory,
			ItemReader<SparqlEntity> reader, ItemWriter<List<SparqlEntity>> writer,
			ItemProcessor<SparqlEntity, List<SparqlEntity>> processor) {
		return stepBuilderFactory.get("step1")
				.<SparqlEntity, List<SparqlEntity>> chunk(10).reader(reader)
				.processor(processor).writer(writer).build();
	}

	@Bean
	public ItemProcessor<SparqlEntity, List<SparqlEntity>> processor() {
		ListSparqlProcessor process = new ListSparqlProcessor();
		MotifSearchSparql motifSparql = new MotifSearchSparql();
		motifSparql.setSearchSparql(getSearchSparql());
		process.setSelectSparql(motifSparql);
		return process;
	}
}