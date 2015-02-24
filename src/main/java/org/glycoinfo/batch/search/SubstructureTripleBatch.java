package org.glycoinfo.batch.search;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;

@Primary
@ComponentScan(basePackages = ("org.glycoinfo.batch.substructure"))
@EnableAutoConfiguration
// @EnableConfigurationProperties(TripleStoreProperties.class)
@SpringApplicationConfiguration(classes = SubstructureTripleBatch.class)
public class SubstructureTripleBatch {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(
				SubstructureTripleBatch.class, args);
	}

	public static int pageSize = 10;

//
//	/**
//	 * 
//	 * Data read sparql process.
//	 * 
//	 * @return
//	 */
//	@Bean
//	public ItemReader<SparqlEntity> reader() {
//		String where = "?glycans a glycan:saccharide . \n"
//				+ "?glycans toucan:has_primary_id ?AccessionNumber . \n"
//				+ "?glycans glycan:has_glycosequence ?gseq . \n"
//				+ "?gseq glycan:has_sequence ?Seq . \n"
//				+ "?gseq glycan:in_carbohydrate_format glycan:carbohydrate_format_glycoct .\n"
//				+ "FILTER NOT EXISTS {?glycans glycan:has_motif ?motif} \n";
//
//		SparqlItemReader<SparqlEntity> reader = new SparqlItemReader<SparqlEntity>();
//		// reader.setPrefix(SubstructureTriple.prefix);
//		MotifSelectSparql subTriple = (org.glycoinfo.batch.substructure.MotifSelectSparql) getTripleBean();
//		subTriple.setWhere(where);
//		// reader.setTripleBean(subTriple);
//		// reader.setConverter(new SubstructureTripleStoreConverter());
//		reader.setSelectSparql(new SelectSparqlBean());
//		reader.setPageSize(pageSize);
//		// reader.setFrom(SubstructureTriple.from);
//		// reader.setSelect("SELECT DISTINCT ?glycans ?AccessionNumber ?Seq\n");
//		// reader.setOrderBy("");
//
//		// reader.setWhere(where);
//
//		return reader;
//	}

//	@Bean
//	public ItemProcessor<MotifSelectSparql, MotifSelectSparql> SubstructureTriple() {
//		return new SubstructureTripleProcessor();
//	}
//
//	@Bean
//	public ItemWriter<SparqlEntity> writer() {
//		SparqlItemWriter<SparqlEntity> writer = new SparqlItemWriter<SparqlEntity>();
//		return writer;
//	}
//
//	// end::readerwriterprocessor[]
//
//	// tag::jobstep[]
//	@Bean
//	public Job importUserJob(JobBuilderFactory jobs, Step s1) {
//		return jobs.get("SubstructureTriple")
//				.incrementer(new RunIdIncrementer()).flow(s1).end().build();
//	}
//
//	@Bean
//	public Step step1(StepBuilderFactory stepBuilderFactory,
//			ItemReader<MotifSelectSparql> reader,
//			ItemWriter<MotifSelectSparql> writer,
//			ItemProcessor<MotifSelectSparql, MotifSelectSparql> processor) {
//		System.gc();
//		return stepBuilderFactory.get("step1")
//				.<MotifSelectSparql, MotifSelectSparql> chunk(10)
//				.reader(reader).processor(processor).writer(writer).build();
//	}
//	// end::jobstep[]

}