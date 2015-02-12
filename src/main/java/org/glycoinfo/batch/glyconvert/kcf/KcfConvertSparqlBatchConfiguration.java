//package org.glycoinfo.batch.glyconvert.kcf;
//
//import org.glycoinfo.batch.SparqlItemReader;
//import org.glycoinfo.batch.SparqlItemWriter;
//import org.glycoinfo.batch.glyconvert.ConvertReaderSparqlInterface;
//import org.glycoinfo.batch.glyconvert.kcf.KcfConvertSparql;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
//import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
//import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
//import org.springframework.batch.core.launch.support.RunIdIncrementer;
//import org.springframework.batch.item.ItemProcessor;
//import org.springframework.batch.item.ItemReader;
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.ComponentScan;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@EnableBatchProcessing
////@EnableAutoConfiguration
//@ComponentScan(basePackages = ("org.glycoinfo"))
//public class KcfConvertSparqlBatchConfiguration {
//	
//	private int pageSize = 10;
//
//    @Bean
//    public ItemReader<KcfConvertSparql> reader() {
//    	SparqlItemReader<KcfConvertSparql> reader = new SparqlItemReader<KcfConvertSparql>();
//    	reader.setConverter(new ConvertSparqlConverter());
//    	reader.setTripleBean(new KcfConvertSparql());
//    	reader.setPageSize(pageSize);
//
///*
// * 
// *     SELECT DISTINCT ?s ?AccessionNumber ?Seq ?type
//WHERE {
//?s a glycan:saccharide .
//?s glytoucan:has_primary_id ?AccessionNumber .
//?s glycan:has_glycosequence ?gseq .
//?gseq glycan:has_sequence ?Seq .
//?gseq glycan:in_carbohydrate_format glycan:carbohydrate_format_kcf
//}
//	
// */
//        return reader;
//    }
//    @Bean
//    public ItemProcessor<KcfConvertSparql, KcfConvertSparql> processor() {
//        return new ConvertSparqlProcessor();
//    }
//
//    @Bean
//    public ItemWriter<KcfConvertSparql> writer() {
//    	return new SparqlItemWriter<KcfConvertSparql>();
//    }
//    // end::readerwriterprocessor[]
//
//    // tag::jobstep[]
//    @Bean
//    public Job importUserJob(JobBuilderFactory jobs, Step s1) {
//        return jobs.get("KcfConvertTriple")
//                .incrementer(new RunIdIncrementer())
//                .flow(s1)
//                .end()
//                .build();
//    }
//
//    @Bean
//    public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<KcfConvertSparql> reader,
//            ItemWriter<KcfConvertSparql> writer, ItemProcessor<KcfConvertSparql, KcfConvertSparql> processor) {
//        return stepBuilderFactory.get("step1")
//                .<KcfConvertSparql, KcfConvertSparql> chunk(10)
//                .reader(reader)
//                .processor(processor)
//                .writer(writer)
//                .build();
//    }
//}