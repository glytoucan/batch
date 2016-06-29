package org.glycoinfo.batch.glytoucan;

import org.glycoinfo.batch.SparqlItemReader;
import org.glycoinfo.batch.glyconvert.GlyConvertSparql;
import org.glycoinfo.rdf.InsertSparql;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.dao.virt.SparqlDAOVirtSesameImpl;
import org.glycoinfo.rdf.dao.virt.VirtRepositoryConnectionFactory;
import org.glycoinfo.rdf.dao.virt.VirtSesameConnectionFactory;
import org.glycoinfo.rdf.dao.virt.VirtSesameTransactionConfig;
import org.glycoinfo.rdf.dao.virt.VirtSesameTransactionManager;
import org.glycoinfo.rdf.glycan.wurcs.GlycoSequenceFilterNoWurcsSelectSparql;
import org.glycoinfo.rdf.service.GlycanProcedure;
import org.glycoinfo.rdf.service.impl.GlycanProcedureConfig;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import virtuoso.sesame2.driver.VirtuosoRepository;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = ("org.glycoinfo.batch.glytoucan"))
@Import(value = { GlycanProcedureConfig.class, VirtSesameTransactionConfig.class})
@EnableBatchProcessing
public class GlycoCTProcessBatch {
 
	private static final String graph = "http://rdf.glytoucan.org";
	private int pageSize = 5000;

	@Autowired
	TripleStoreProperties tripleStoreProperties;
	
	public static void main(String[] args) {
		@SuppressWarnings("unused")
		ApplicationContext ctx = SpringApplication.run(
				GlycoCTProcessBatch.class, args);
	}

	@Bean(name="itemReaderSelectSparql")
	SelectSparql getSelectSparql() {
		SelectSparql select = new GlycoSequenceFilterNoWurcsSelectSparql();
		select.setFrom("FROM <http://rdf.glytoucan.org>\nFROM <http://rdf.glytoucan.org/sequence/wurcs>\n");
		SparqlEntity se = new SparqlEntity();
//		se.setValue(GlyConvertSparql.FormattedFilter, "false");
		
		select.setSparqlEntity(se);
		return select;
	}

	@Bean
	InsertSparql getInsertSparql() {
		return null;
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
	public ItemWriter<SparqlEntity> writer() {
		return null;
	}

	@Bean
	public Job importUserJob(JobBuilderFactory jobs, Step s1) {
		return jobs.get("MassWurcs").incrementer(new RunIdIncrementer())
				.flow(s1).end().build();
	}

	@Bean
	public Step step1(StepBuilderFactory stepBuilderFactory,
			ItemReader<SparqlEntity> reader, ItemWriter<SparqlEntity> writer,
			ItemProcessor<SparqlEntity, SparqlEntity> processor) {
		return stepBuilderFactory.get("step1")
				.<SparqlEntity, SparqlEntity> chunk(10).reader(reader)
				.processor(processor).writer(writer).build();
	}

	@Bean
	public ItemProcessor<SparqlEntity, SparqlEntity> processor() {
		GlycoCTProcessor process = new GlycoCTProcessor();
		return process;
	}
	
	@Bean
	GlycanProcedure glycanProcedure() {
		org.glycoinfo.rdf.service.impl.GlycanProcedure glycanProc = new org.glycoinfo.rdf.service.impl.GlycanProcedure();
		glycanProc.setBatch(true);
		return glycanProc; 
	}
}