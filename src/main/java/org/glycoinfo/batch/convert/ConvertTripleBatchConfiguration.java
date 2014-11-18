package org.glycoinfo.batch.convert;

import org.glycoinfo.batch.TripleStoreItemReader;
import org.glycoinfo.batch.TripleStoreItemWriter;
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
public class ConvertTripleBatchConfiguration {
	
	public static int pageSize = 10;
	
	public static final String prefix = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n"
			+ "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n"
			+ "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n"
			+ "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n"
			+ "PREFIX dc: <http://purl.org/dc/elements/1.1/>\n"
			+ "PREFIX dcterms: <http://purl.org/dc/terms/>\n"
			+ "PREFIX dbpedia2: <http://dbpedia.org/property/>\n"
			+ "PREFIX dbpedia: <http://dbpedia.org/>\n"
			+ "PREFIX foaf: <http://xmlns.com/foaf/0.1/>\n"
			+ "PREFIX skos: <http://www.w3.org/2004/02/skos/core#>\n"
			+ "PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
			+ "PREFIX glytoucan:  <http://www.glytoucan.org/glyco/owl/glytoucan#>\n";
	
	public static final String from = "from <http://glytoucan.org/rdf/demo/0.7>\n";
//			+ "from <http://glytoucan.org/rdf/demo/msdb/7>\n"
//			+ "from <http://purl.jp/bio/12/glyco/glycan/ontology/0.18>\n"
//			+ "from <http://www.glytoucan.org/glyco/owl/glytoucan>\n";

    // tag::readerwriterprocessor[]
    @Bean
    public ItemReader<ConvertTriple> reader() {
    	String where = "{ ?s a glycan:saccharide . "
    			+ "?s glytoucan:has_primary_id ?AccessionNumber . "
    			+ "?s glycan:has_glycosequence ?gseq . "
    			+ "?gseq glycan:has_sequence ?Seq . "
    			+ "?gseq glycan:in_carbohydrate_format glycan:carbohydrate_format_glycoct }\n";
    	
    	TripleStoreItemReader<ConvertTriple> reader = new TripleStoreItemReader<ConvertTriple>();
    	reader.setConverter(new ConvertTripleStoreConverter());
    	reader.setPageSize(pageSize);
    	reader.setPrefix(prefix);
    	reader.setSelect("SELECT DISTINCT ?s ?AccessionNumber ?Seq ?type\n");
    	reader.setFrom(from);
//    	reader.setOrderBy("");

    	reader.setWhere(where);

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
    public ItemProcessor<ConvertTriple, ConvertTriple> processor() {
        return new ConvertTripleProcessor();
    }

    @Bean
    public ItemWriter<ConvertTriple> writer() {
    	TripleStoreItemWriter<ConvertTriple> writer = new TripleStoreItemWriter<ConvertTriple>();
    	writer.setGraph(ConvertTriple.graph);
    	writer.setDelete(false);
        return writer;
    }
    // end::readerwriterprocessor[]

    // tag::jobstep[]
    @Bean
    public Job importUserJob(JobBuilderFactory jobs, Step s1) {
        return jobs.get("ConvertTripleKcf")
                .incrementer(new RunIdIncrementer())
                .flow(s1)
                .end()
                .build();
    }

    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<ConvertTriple> reader,
            ItemWriter<ConvertTriple> writer, ItemProcessor<ConvertTriple, ConvertTriple> processor) {
        return stepBuilderFactory.get("step1")
                .<ConvertTriple, ConvertTriple> chunk(10)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
    // end::jobstep[]

//    @Bean
//    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
//        return new JdbcTemplate(dataSource);
//    }

}