package org.glycoinfo.batch;

import org.glycoinfo.ts.utils.TripleStoreProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan(basePackages = ("org.glycoinfo"))
@EnableAutoConfiguration
//@EnableConfigurationProperties(TripleStoreProperties.class)
//@SpringApplicationConfiguration(classes = TripleStoreProperties.class)
public class ConvertTripleBatch {

	public static void main(String[] args) {
		ApplicationContext ctx = SpringApplication.run(ConvertTripleBatch.class, args);
	}
}