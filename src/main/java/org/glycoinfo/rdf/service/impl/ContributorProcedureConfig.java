package org.glycoinfo.rdf.service.impl;

import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.glycan.ContributorInsertSparql;
import org.glycoinfo.rdf.glycan.ContributorNameSelectSparql;
import org.glycoinfo.rdf.glycan.ResourceEntryInsertSparql;
import org.glycoinfo.rdf.service.ContributorProcedure;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ContributorProcedureConfig implements GraphConfig {

	@Bean(name = "contributorProcedure")
	ContributorProcedure getContributorProcedure() throws SparqlException {
		ContributorProcedure cp = new ContributorProcedureRdf();
		return cp;
	}
	
	@Bean
	ContributorInsertSparql getContributorInsertSparql() {
		ContributorInsertSparql c = new ContributorInsertSparql();
		c.setGraph(graph + "/users");
		return c;
	}
	
	@Bean
	ContributorNameSelectSparql getContributorNameSelectSparql() {
		ContributorNameSelectSparql selectbyNameContributor = new ContributorNameSelectSparql();
		selectbyNameContributor.setFrom("FROM <" + graph + ">\nFROM <" + graph + "/users" + ">\n");
		return selectbyNameContributor;
	}
	
	@Bean
	ResourceEntryInsertSparql resourceEntryInsertSparql() {
		ResourceEntryInsertSparql ins = new ResourceEntryInsertSparql();
		ins.setGraph(graph + "/core");
		return ins;
	}
}
