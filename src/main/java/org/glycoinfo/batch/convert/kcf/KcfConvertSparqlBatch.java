package org.glycoinfo.batch.convert.kcf;

import org.glycoinfo.rdf.utils.TripleStoreProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = ("org.glycoinfo.batch.convert.kcf"))
@EnableAutoConfiguration
//@EnableConfigurationProperties(TripleStoreProperties.class)
//@SpringApplicationConfiguration(classes = TripleStoreProperties.class)
public class KcfConvertSparqlBatch {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(KcfConvertSparqlBatch.class, args);
	}
}