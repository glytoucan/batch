package org.glycoinfo.rdf.service.impl;

import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.scint.InsertScint;
import org.glycoinfo.rdf.scint.SelectScint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UserProcedureConfig implements GraphConfig {
	
	public static final String graphUser = graph + "/schema/users";
	
	@Autowired
	SparqlDAO sparqlDAO;

//	@Bean(name = "selectscintperson")
//	SelectScint getSelectPersonScint() throws SparqlException {
//		SelectScint select = new SelectScint();
//		
//		select.setClassHandler(getPersonClassHandler());
//		select.setFrom("FROM <" + graphUser + ">");
//		return select;
//	}
//
//	@Bean(name = "insertscintperson")
//	InsertScint getInsertPersonScint() throws SparqlException {
//		InsertScint insert = new InsertScint(graphUser);
//		insert.setClassHandler(getPersonClassHandler());
//		return insert;
//	}
//
//	@Bean(name = "deletescintperson")
//	DeleteScint getDeletePersonScint() throws SparqlException {
//		DeleteScint delete = new DeleteScint(graphUser);
//		delete.setClassHandler(getPersonClassHandler());
//		return delete;
//	}
	
	@Bean(name = "selectscintregisteraction")
	SelectScint getSelectRegisterActionScint() throws SparqlException {
		return new SelectScint("schema", "http://schema.org/", "RegisterAction");
	}

	@Bean(name = "selectScintOrganization")
	SelectScint getSelectScintOrganization() throws SparqlException {
		return new SelectScint("schema", "http://schema.org/", "Organization");
	}
	
	@Bean(name = "selectScintDateTime")
	SelectScint getSelectScintDateTime() throws SparqlException {
		return new SelectScint("schema", "http://schema.org/", "DateTime");
	}

	@Bean(name = "insertscintregisteraction")
	InsertScint getInsertRegisterActionScint() throws SparqlException {
		InsertScint insert = new InsertScint("schema", "http://schema.org/", "Organization");
		insert.getSparqlBean().setGraph(graphUser);
//		insert.setClassHandler(new SelectScint());
		return insert;
	}
	
//	@Bean(name = "insertScintProgramMembership")
//	InsertScint getInsertScintProgramMembership() throws SparqlException {
//		InsertScint insertScintProgramMembership = new InsertScint(graphUser);
//		insertScintProgramMembership.setClassHandler(getProgramMembershipClassHandler());
//		return insertScintProgramMembership;
//	}
//
//	@Bean(name = "deleteScintProgramMembership")
//	DeleteScint getDeleteScintProgramMembership() throws SparqlException {
//		DeleteScint deleteScintProgramMembership = new DeleteScint(graphUser);
//		deleteScintProgramMembership.setClassHandler(getProgramMembershipClassHandler());
//		return deleteScintProgramMembership;
//	}
//	
//	@Bean(name = "selectScintProgramMembership")
//	SelectScint getSelectProgramMembership() throws SparqlException {
//		SelectScint select = new SelectScint();
//		select.setClassHandler(getProgramMembershipClassHandler());
//		select.setFrom("FROM <" + graphUser + ">");
//		return select;
//	}
//	
//	ClassHandler getPersonClassHandler() throws SparqlException {
//		ClassHandler ch = new ClassHandler("schema", "http://schema.org/", "Person");
//		ch.setSparqlDAO(sparqlDAO);
//		return ch;
//	}
//	
////	ClassHandler getRegisterActionClassHandler() throws SparqlException {
////		ClassHandler ch = new ClassHandler("schema", "http://schema.org/", "RegisterAction");
////		ch.setSparqlDAO(sparqlDAO);
////		return ch; 
////	}
//	
//	ClassHandler getDateTimeClassHandler() throws SparqlException {
//		ClassHandler ch = new ClassHandler("schema", "http://schema.org/", "DateTime");
//		ch.setSparqlDAO(sparqlDAO);
//		return ch; 
//	}
//	
//	ClassHandler getProgramMembershipClassHandler() throws SparqlException {
//		ClassHandler classHandler = new ClassHandler("schema", "http://schema.org/", "ProgramMembership");
//		classHandler.setSparqlDAO(sparqlDAO);
//		return classHandler; 
//	}
//	
//	@Bean(name = "userProcedure")
//	UserProcedure getUserProcedure() throws SparqlException {
//		UserProcedure user = new org.glycoinfo.rdf.service.impl.UserProcedure();
//		return user;
//	}
//	
//	@Bean(name = "contributorProcedure")
//	ContributorProcedure getContributorProcedure() throws SparqlException {
//		ContributorProcedure cp = new ContributorProcedureRdf();
//		return cp;
//	}
//	
//	@Bean
//	MailService mailService() {
//		return new MailService();
//	}
}