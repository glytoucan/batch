package org.glycoinfo.batch.convert.kcf;

import org.glycoinfo.batch.TripleStoreItemReader;
import org.glycoinfo.batch.TripleStoreItemWriter;
import org.glycoinfo.batch.convert.ConvertTriple;
import org.glycoinfo.batch.convert.kcf.KcfConvertTriple;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
//@EnableAutoConfiguration
@ComponentScan(basePackages = ("org.glycoinfo"))
public class KcfConvertTripleBatchConfiguration {
	
	private int pageSize = 10;

    @Bean
    public ItemReader<KcfConvertTriple> reader() {
    	TripleStoreItemReader<KcfConvertTriple> reader = new TripleStoreItemReader<KcfConvertTriple>();
    	reader.setConverter(new ConvertTripleStoreConverter());
    	reader.setTripleBean(new KcfConvertTriple());
    	reader.setPageSize(pageSize);

/*
 * 
 *     SELECT DISTINCT ?s ?AccessionNumber ?Seq ?type
WHERE {
?s a glycan:saccharide .
?s glytoucan:has_primary_id ?AccessionNumber .
?s glycan:has_glycosequence ?gseq .
?gseq glycan:has_sequence ?Seq .
?gseq glycan:in_carbohydrate_format glycan:carbohydrate_format_kcf
}
	
 */
        return reader;
    }
    @Bean
    public ItemProcessor<KcfConvertTriple, KcfConvertTriple> processor() {
        return new ConvertTripleProcessor();
    }

    @Bean
    public ItemWriter<KcfConvertTriple> writer() {
    	return new TripleStoreItemWriter<KcfConvertTriple>();
    }
    // end::readerwriterprocessor[]

    // tag::jobstep[]
    @Bean
    public Job importUserJob(JobBuilderFactory jobs, Step s1) {
        return jobs.get("KcfConvertTriple")
                .incrementer(new RunIdIncrementer())
                .flow(s1)
                .end()
                .build();
    }

    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<KcfConvertTriple> reader,
            ItemWriter<KcfConvertTriple> writer, ItemProcessor<KcfConvertTriple, KcfConvertTriple> processor) {
        return stepBuilderFactory.get("step1")
                .<KcfConvertTriple, KcfConvertTriple> chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}