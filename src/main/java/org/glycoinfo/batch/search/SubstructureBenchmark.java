package org.glycoinfo.batch.search;

import java.util.List;

import org.glycoinfo.batch.search.wurcs.WurcsMotifSearchBatch;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
@SpringApplicationConfiguration(classes = WurcsMotifSearchBatch.class)
public class SubstructureBenchmark implements SubstructureSearch {

	@Override
	public List<String> search(String sequence) {
		return null;
	}
	

	public void main(String[] args) {
		
	}

}
