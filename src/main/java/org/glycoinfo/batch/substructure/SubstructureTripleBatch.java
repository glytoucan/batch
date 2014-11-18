package org.glycoinfo.batch.substructure;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Primary;

@Primary
@ComponentScan(basePackages = ("org.glycoinfo.batch.substructure"))
@EnableAutoConfiguration
//@EnableConfigurationProperties(TripleStoreProperties.class)
@SpringApplicationConfiguration(classes = SubstructureTripleBatchConfiguration.class)
public class SubstructureTripleBatch {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(SubstructureTripleBatch.class, args);
	}
}