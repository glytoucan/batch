package org.glycoinfo.rdf.glycan;

import static org.junit.Assert.*;

import java.util.Date;

import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SelectSparqlBean;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.glycan.D3SequenceSelectSparql_motif;
import org.glycoinfo.rdf.glycan.GlycoSequenceSelectSparql;
import org.glycoinfo.rdf.glycan.Saccharide;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
//import com.sun.research.ws.wadl.Application;

public class D3SparqlConfirmTest_topology {
	public static Logger logger = (Logger) LoggerFactory
			.getLogger(D3SparqlConfirmTest_topology.class);

	@Test
	public void jsonSerializationTest() throws Exception {
	    D3SequenceSelectSparql_has_topology dp = new D3SequenceSelectSparql_has_topology(); 
	    D3SequenceSelectSparql_topology_by dpb = new D3SequenceSelectSparql_topology_by();
	    GRABSequenceSelectSparql_subsumedby gdp = new GRABSequenceSelectSparql_subsumedby();
	    GRABSequenceSelectSparql_subsumes gdpb = new GRABSequenceSelectSparql_subsumes();
	    
	    SparqlEntity sparqlentity_1 = new SparqlEntity();
	    SparqlEntity sparqlentity_2 = new SparqlEntity();
	    SparqlEntity sparqlentity_3 = new SparqlEntity();
	    SparqlEntity sparqlentity_4 = new SparqlEntity();
	    
//	    sparqlentity.setValue(Saccharide.PrimaryId, "G000TEST");
	    sparqlentity_1.setValue(Glycosidic_topology.PrimaryId_1, "G63977XF");
	    dp.setSparqlEntity(sparqlentity_1);
	    
	    sparqlentity_2.setValue(Glycosidic_topology.PrimaryId_2, "G63977XF");
	    dpb.setSparqlEntity(sparqlentity_2);
	    
	    sparqlentity_3.setValue(Glycosidic_topology.PrimaryId_1, "G63977XF");
	    gdpb.setSparqlEntity(sparqlentity_1);
	    
	    sparqlentity_4.setValue(Glycosidic_topology.PrimaryId_2, "G63977XF");
	    gdp.setSparqlEntity(sparqlentity_2);

	     logger.debug(dp.getSparql());
	     logger.debug("\n");
	     logger.debug(dpb.getSparql());
	     logger.debug("\n");
	     logger.debug(gdp.getSparql());
	     logger.debug("\n");
	     logger.debug(gdpb.getSparql());
	     
//	     ObjectMapper mapper = new ObjectMapper();
//	     // {"glycanId":0,"accessionNumber":"G001234","dateEntered":"1970-01-01T00:00:00Z","structure":"structureString","structureLength":null,"mass":123.0,"motifs":null,"contributor":{"userId":1,"userName":"aoki","loginId":"aokinobu","email":"support@glytoucan.org","active":true,"validated":false,"affiliation":"Soka University","dateRegistered":"1970-01-01T00:00:00Z","lastLoggedIn":"1970-01-01T00:00:00Z","roles":null}}
//	     logger.debug(mapper.writeValueAsString(g));
//	     assertEquals("{\"glycanId\":0,\"accessionNumber\":\"G001234\",\"dateEntered\":\"1970-01-01T00:00:00Z\",\"structure\":\"structureString\",\"structureLength\":null,\"mass\":123.0,\"motifs\":null,\"contributor\":{\"userId\":1,\"userName\":\"aoki\",\"loginId\":\"aokinobu\",\"email\":\"support@glytoucan.org\",\"active\":true,\"validated\":false,\"affiliation\":\"Soka University\",\"dateRegistered\":\"1970-01-01T00:00:00Z\",\"lastLoggedIn\":\"1970-01-01T00:00:00Z\",\"roles\":null}}", 
//	    		 mapper.writeValueAsString(g));
	}     
}
