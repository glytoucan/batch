package org.glycoinfo.batch.glyconvert.wurcs;

import java.io.File;

import org.glycoinfo.batch.SparqlItemReader;
import org.glycoinfo.batch.TtlItemWriter;
import org.glycoinfo.batch.glyconvert.ConvertInsertSparql;
import org.glycoinfo.batch.glyconvert.ConvertSelectSparql;
import org.glycoinfo.batch.glyconvert.ConvertSparqlProcessor;
import org.glycoinfo.convert.GlyConvert;
import org.glycoinfo.convert.glycoct.GlycoctToWurcsConverter;
import org.glycoinfo.rdf.InsertSparql;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.dao.virt.SparqlDAOVirtSesameImpl;
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
import org.springframework.core.io.FileSystemResource;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = ("org.glycoinfo.batch.glyconvert.wurcs"))
@SpringApplicationConfiguration(classes = WurcsConvertTtlBatch.class)
@EnableBatchProcessing
public class WurcsConvertTtlBatch {

	// graph base to set the graph to insert into. The format type (toFormat()) will be added to the end. 
	// example: http://glytoucan.org/rdf/demo/0.7/wurcs
	public static String graphbase = "http://rdf.glytoucan.org/sequence";
	private int pageSize = 10;

	public static void main(String[] args) {
		@SuppressWarnings("unused")
		ApplicationContext ctx = SpringApplication.run(
				WurcsConvertTtlBatch.class, args);
	}

	@Bean
	GlyConvert getGlyConvert() {
		return new GlycoctToWurcsConverter();
	}

	@Bean
	SelectSparql getSelectSparql() {
		SelectSparql select = new ConvertSelectSparql();
		select.setFrom("FROM <http://rdf.glytoucan.org>");
		return select;
	}

	@Bean
	InsertSparql getInsertSparql() {
		ConvertInsertSparql convert = new ConvertInsertSparql();
		convert.setGraphBase(graphbase);
		return convert;
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
		reader.setPageSize(100);
		reader.setSelectSparql(getSelectSparql());
		reader.setPageSize(pageSize);
		return reader;
	}

	@Bean
	public ItemWriter<SparqlEntity> writer() {
		
//		FlatFileItemWriter<ConvertInsertSparql> writer = new FlatFileItemWriter<ConvertInsertSparql>();
		TtlItemWriter<SparqlEntity> writer = new TtlItemWriter<SparqlEntity>();
		writer.setResource(new FileSystemResource(new File("./convertwurcs.ttl")));
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
			ItemReader<SparqlEntity> reader, ItemWriter<SparqlEntity> writer,
			ItemProcessor<SparqlEntity, SparqlEntity> processor) {
		return stepBuilderFactory.get("step1")
				.<SparqlEntity, SparqlEntity> chunk(100).reader(reader)
				.processor(processor).writer(writer).build();
	}

	@Bean
	public ItemProcessor<SparqlEntity, SparqlEntity> processor() {
		ConvertSparqlProcessor process = new ConvertSparqlProcessor();
		process.setGlyConvert(getGlyConvert());

		return process;
	}
}