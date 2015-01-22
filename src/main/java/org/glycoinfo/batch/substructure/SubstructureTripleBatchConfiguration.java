package org.glycoinfo.batch.substructure;

import org.glycoinfo.batch.SparqlItemReader;
import org.glycoinfo.batch.SparqlItemWriter;
import org.glycoinfo.rdf.InsertSparql;
import org.glycoinfo.search.GlySearch;
import org.glycoinfo.search.glycoct.GlycoCTSearch;
import org.glycoinfo.search.kcam.KCAMSearch;
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
@EnableAutoConfiguration
@ComponentScan(basePackages = ("org.glycoinfo"))
public class SubstructureTripleBatchConfiguration {
	
	public static int pageSize = 10;
	
	@Bean
	GlySearch getGlySearch() {
		return new GlycoCTSearch();
	}

	@Bean
	public InsertSparql getTripleBean() {
		return new SubstructureTriple();
	}
	

    /**
     * 
     * Data read sparql process.
     * 
     * @return
     */
    @Bean
    public ItemReader<SubstructureTriple> reader() {
    	String where = "?glycans a glycan:saccharide . \n"
    			+ "?glycans toucan:has_primary_id ?AccessionNumber . \n"
    			+ "?glycans glycan:has_glycosequence ?gseq . \n"
    			+ "?gseq glycan:has_sequence ?Seq . \n"
    			+ "?gseq glycan:in_carbohydrate_format glycan:carbohydrate_format_glycoct .\n"
    			+ "FILTER NOT EXISTS {?glycans glycan:has_motif ?motif} \n";
    	
    	SparqlItemReader<SubstructureTriple> reader = new SparqlItemReader<SubstructureTriple>();
//    	reader.setPrefix(SubstructureTriple.prefix);
    	SubstructureTriple subTriple = (org.glycoinfo.batch.substructure.SubstructureTriple) getTripleBean();
    	subTriple.setWhere(where);
    	reader.setTripleBean(subTriple);
    	reader.setConverter(new SubstructureTripleStoreConverter());
    	reader.setPageSize(pageSize);
//    	reader.setFrom(SubstructureTriple.from);
//    	reader.setSelect("SELECT DISTINCT ?glycans ?AccessionNumber ?Seq\n");
//    	reader.setOrderBy("");

//    	reader.setWhere(where);
    	
        return reader;
    }

    @Bean
    public ItemProcessor<SubstructureTriple, SubstructureTriple> SubstructureTriple() {
        return new SubstructureTripleProcessor();
    }

    @Bean
    public ItemWriter<SubstructureTriple> writer() {
    	SparqlItemWriter<SubstructureTriple> writer = new SparqlItemWriter<SubstructureTriple>();
        return writer;
    }
    // end::readerwriterprocessor[]

    // tag::jobstep[]
    @Bean
    public Job importUserJob(JobBuilderFactory jobs, Step s1) {
        return jobs.get("SubstructureTriple")
                .incrementer(new RunIdIncrementer())
                .flow(s1)
                .end()
                .build();
    }

    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<SubstructureTriple> reader,
            ItemWriter<SubstructureTriple> writer, ItemProcessor<SubstructureTriple, SubstructureTriple> processor) {
    	System.gc();
        return stepBuilderFactory.get("step1")
                .<SubstructureTriple, SubstructureTriple> chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
    // end::jobstep[]

}