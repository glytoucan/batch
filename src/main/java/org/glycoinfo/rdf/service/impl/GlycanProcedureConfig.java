package org.glycoinfo.rdf.service.impl;

import org.glycoinfo.batch.search.wurcs.SubstructureSearchSparql;
import org.glycoinfo.client.MSdbClient;
import org.glycoinfo.mass.MassInsertSparql;
import org.glycoinfo.rdf.InsertSparql;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.glycan.ContributorInsertSparql;
import org.glycoinfo.rdf.glycan.ContributorNameSelectSparql;
import org.glycoinfo.rdf.glycan.GlycoSequenceInsertSparql;
import org.glycoinfo.rdf.glycan.ResourceEntryInsertSparql;
import org.glycoinfo.rdf.glycan.SaccharideInsertSparql;
import org.glycoinfo.rdf.glycan.SaccharideSelectSparql;
import org.glycoinfo.rdf.glycan.msdb.MSInsertSparql;
import org.glycoinfo.rdf.glycan.wurcs.GlycoSequenceResourceEntryContributorSelectSparql;
import org.glycoinfo.rdf.glycan.wurcs.MonosaccharideSelectSparql;
import org.glycoinfo.rdf.glycan.wurcs.MotifSequenceSelectSparql;
import org.glycoinfo.rdf.glycan.wurcs.WurcsRDFInsertSparql;
import org.glycoinfo.rdf.glycan.wurcs.WurcsRDFMSInsertSparql;
import org.glycoinfo.rdf.service.ContributorProcedure;
import org.glycoinfo.rdf.service.GlycanProcedure;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GlycanProcedureConfig {
	private static final String graph = "http://rdf.glytoucan.org";

	@Bean
	SaccharideInsertSparql getSaccharideInsertSparql() {
		SaccharideInsertSparql sis = new SaccharideInsertSparql();
		sis.setGraph(graph);
		return sis;
	}
	
	@Bean(name = "contributorProcedure")
	ContributorProcedure getContributorProcedure() throws SparqlException {
		ContributorProcedure cp = new ContributorProcedureRdf();
		return cp;
	}
	
	@Bean
	ResourceEntryInsertSparql getResourceEntryInsertSparql() {
		ResourceEntryInsertSparql resourceEntryInsertSparql = new ResourceEntryInsertSparql();
		SparqlEntity se = new SparqlEntity();
		se.setValue(ResourceEntryInsertSparql.Database, "glytoucan");
		resourceEntryInsertSparql.setSparqlEntity(se);
		resourceEntryInsertSparql.setGraph(graph);
		return resourceEntryInsertSparql;
	}

	@Bean
	SelectSparql glycoSequenceContributorSelectSparql() {
		GlycoSequenceResourceEntryContributorSelectSparql sb = new GlycoSequenceResourceEntryContributorSelectSparql();
		sb.setFrom("FROM <http://rdf.glytoucan.org>\nFROM <http://rdf.glytoucan.org/sequence/wurcs>\nFROM <http://rdf.glytoucan.org/mass>\n");
		return sb;
	}
	
	@Bean
	SelectSparql listAllGlycoSequenceContributorSelectSparql() {
		GlycoSequenceResourceEntryContributorSelectSparql sb = new GlycoSequenceResourceEntryContributorSelectSparql();
		sb.setFrom("FROM <http://rdf.glytoucan.org>\nFROM <http://rdf.glytoucan.org/sequence/wurcs>\nFROM <http://rdf.glytoucan.org/mass>\n");
		return sb;
	}
	
	@Bean
	MassInsertSparql massInsertSparql() {
		MassInsertSparql mass = new MassInsertSparql();
		mass.setGraphBase(graph);
		return mass;
	}
	
	@Bean
	WurcsRDFInsertSparql wurcsRDFInsertSparql() {
		WurcsRDFInsertSparql wrdf = new WurcsRDFInsertSparql();
		wrdf.setSparqlEntity(new SparqlEntity());
		wrdf.setGraph("http://rdf.glytoucan.org/sequence/wurcs");
		return wrdf;
	}
	
	@Bean
	WurcsRDFMSInsertSparql wurcsRDFMSInsertSparql() {
		WurcsRDFMSInsertSparql wrdf = new WurcsRDFMSInsertSparql();
		wrdf.setSparqlEntity(new SparqlEntity());
		wrdf.setGraph("http://rdf.glytoucan.org/wurcs/ms");
		return wrdf;
	}
	
	@Bean
	InsertSparql glycoSequenceInsert() {
		GlycoSequenceInsertSparql gsis = new GlycoSequenceInsertSparql();
		gsis.setSparqlEntity(new SparqlEntity());
		gsis.setGraph("http://rdf.glytoucan.org");
		return gsis;
	}
	
	@Bean
	MotifSequenceSelectSparql motifSequenceSelectSparql() {
		MotifSequenceSelectSparql select = new MotifSequenceSelectSparql();
//		select.setFrom("FROM <http://rdf.glytoucan.org>\nFROM <http://rdf.glytoucan.org/sequence/wurcs>");
		return select;
	}
	
	@Bean
	SaccharideSelectSparql saccharideSelectSparql() {
		SaccharideSelectSparql select = new SaccharideSelectSparql();
		select.setFrom("FROM <http://rdf.glytoucan.org>\n");
		return select;
	}
	
	@Bean
	MonosaccharideSelectSparql monosaccharideSelectSparql() {
		MonosaccharideSelectSparql sb = new MonosaccharideSelectSparql();
		return sb;
	}
	
	@Bean
	MSdbClient msdbClient() {
		return new MSdbClient();
	}
	
	@Bean
	public MSInsertSparql msInsertSparql() {
		MSInsertSparql wrss = new MSInsertSparql();
		wrss.setGraph("http://rdf.glytoucan.org/msdb");
		return wrss;
	}
	
	@Bean
	GlycanProcedure glycanProcedure() {
		org.glycoinfo.rdf.service.impl.GlycanProcedure glycanProc = new org.glycoinfo.rdf.service.impl.GlycanProcedure();
		glycanProc.setBatch(true);
		return glycanProc; 
	}
	
	@Bean
	ContributorInsertSparql getContributorInsertSparql() {
		ContributorInsertSparql c = new ContributorInsertSparql();
		c.setGraph(graph);
		return c;
	}
	
	@Bean
	ContributorNameSelectSparql getContributorNameSelectSparql() {
		ContributorNameSelectSparql selectbyNameContributor = new ContributorNameSelectSparql();
		selectbyNameContributor.setFrom("FROM <" + graph + ">");
		return selectbyNameContributor;
	}
	
	@Bean
	SubstructureSearchSparql substructureSearchSparql() {
		SubstructureSearchSparql sss = new SubstructureSearchSparql();
		sss.setGraphtarget("<http://rdf.glytoucan.org/sequence/wurcs>");
		sss.setGraphms("<http://rdf.glytoucan.org/wurcs/ms>");
		return sss;
	}

}
