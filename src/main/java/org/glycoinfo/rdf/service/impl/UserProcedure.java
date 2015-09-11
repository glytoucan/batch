package org.glycoinfo.rdf.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.scint.ClassHandler;
import org.glycoinfo.rdf.scint.InsertScint;
import org.glycoinfo.rdf.scint.SelectScint;
import org.glycoinfo.rdf.service.ContributorProcedure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;

@Service
public class UserProcedure implements org.glycoinfo.rdf.service.UserProcedure {

	Log logger = LogFactory.getLog(UserProcedure.class);
	
	String[] requiredFields = {SelectScint.PRIMARY_KEY, "email", "givenName", "familyName", "verifiedEmail"};
	
	@Autowired
	SparqlDAO sparqlDAO;
	
	@Autowired
	@Qualifier(value = "selectscintperson")
	SelectScint selectScintPerson;

	@Autowired
	@Qualifier(value = "insertscintperson")
	InsertScint insertScintPerson;
	
	@Autowired
	@Qualifier(value = "selectscintregisteraction")
	SelectScint selectScintRegisterAction;

	@Autowired
	@Qualifier(value = "insertscintregisteraction")
	InsertScint insertScintRegisterAction;
	
	@Autowired
	ContributorProcedure contributorProcedure;

	@Autowired(required=false)
	JavaMailSender mailSender;

	ClassHandler getPersonClassHandler() throws SparqlException {
		ClassHandler scint = new ClassHandler("schema", "http://schema.org/", "Person");
		scint.setSparqlDAO(sparqlDAO);
		return scint; 
	}
	
//	ClassHandler getRegisterActionClassHandler() throws SparqlException {
//		ClassHandler scint = new ClassHandler("schema", "http://schema.org/", "RegisterAction");
//		scint.setSparqlDAO(sparqlDAO);
//		return scint; 
//	}
	
	ClassHandler getDateTimeClassHandler() throws SparqlException {
		ClassHandler scint = new ClassHandler("schema", "http://schema.org/", "DateTime");
		scint.setSparqlDAO(sparqlDAO);
		return scint; 
	}
	
	ClassHandler getOrganizationClassHandler() throws SparqlException {
		ClassHandler ch = new ClassHandler("schema", "http://schema.org/", "Organization");
		ch.setSparqlDAO(sparqlDAO);
		return ch; 
	}
	
	SparqlEntity se;
	
	@Override
	public void addUser() throws SparqlException {
		Set<String> columns = getSparqlEntity().getColumns();
		if (!columns.containsAll(Arrays.asList(requiredFields))) {
			throw new SparqlException("not all required fields are supplied");
		}

		String personUID = getSparqlEntity().getValue(SelectScint.PRIMARY_KEY);
		// check for primary key of user.  (should be at least at UUID)
		if ( personUID == null || personUID.equals("")) {
			throw new SparqlException("PRIMARY_KEY is >" + personUID + "<");
		}

		final SparqlEntity sparqlentityPerson = new SparqlEntity(personUID);
		
		for (String field : requiredFields) {
			if (field.equals("verifiedEmail"))
				continue;
			
			sparqlentityPerson.setValue(field, getSparqlEntity().getValue(field));
		}
		
		// if you have a verified email, you are a contributor of the glytoucan organization.
		
		// new Organization Class
		SelectScint organizationSelect = new SelectScint();
		organizationSelect.setClassHandler(getOrganizationClassHandler());
		
		// Organization entity
		SparqlEntity sparqlentityOrganization = new SparqlEntity("glytoucan");
		organizationSelect.setSparqlEntity(sparqlentityOrganization);
		
		String verified = getSparqlEntity().getValue(verifiedEmail);
		if (verified != null && verified.equals("true")) {
			sparqlentityPerson.setValue("contributor", organizationSelect);
		}
		
		// otherwise just a member.
		sparqlentityPerson.setValue("member", organizationSelect);
		insertScintPerson.setSparqlEntity(sparqlentityPerson);
		sparqlDAO.insert(insertScintPerson);
		
		// RegisterAction entity
		// UID is userid + register class
		SparqlEntity sparqlentityRegisterAction = new SparqlEntity(personUID);
		
		// new DateTime Class
		SelectScint dateTimeSelect = new SelectScint();
		dateTimeSelect.setClassHandler(getDateTimeClassHandler());
		
		// check for date time
		
		// no date time so use current
		
		// DateTime entity
		SparqlEntity sparqlentityDateTime = new SparqlEntity(new Date());
		dateTimeSelect.setSparqlEntity(sparqlentityDateTime);
		
		// set the datetime class to be the startTime range for the registeraction domain.
		sparqlentityRegisterAction.setValue("startTime", dateTimeSelect);

		SparqlEntity sparqlEntityPerson = new SparqlEntity(personUID);
		SelectScint personSelect = new SelectScint();
		personSelect.setClassHandler(getPersonClassHandler());
		personSelect.setSparqlEntity(sparqlEntityPerson);
		
		sparqlentityRegisterAction.setValue("participant", personSelect);

		// set the sparqlentity for the registeraction. 
		insertScintRegisterAction.setSparqlEntity(sparqlentityRegisterAction);
		
		sparqlDAO.insert(insertScintRegisterAction);
		
		contributorProcedure.setName(getSparqlEntity().getValue(org.glycoinfo.rdf.service.UserProcedure.givenName));
		contributorProcedure.addContributor();
		
		
        MimeMessagePreparator preparator = new MimeMessagePreparator() {

        	public void prepare(MimeMessage mimeMessage) throws Exception {

                mimeMessage.setRecipient(Message.RecipientType.TO,
                        new InternetAddress(sparqlentityPerson.getValue(email)));
                mimeMessage.setFrom(new InternetAddress("admin@glytoucan.org"));
                mimeMessage.setSubject("registration:" + sparqlentityPerson.getValue(givenName) + " " + sparqlentityPerson.getValue(email));
                mimeMessage.setText(
                        "new user info:\nFirst Name:" + sparqlentityPerson.getValue(givenName) + "\nLast Name:"
                            + sparqlentityPerson.getValue(familyName) + "\nemail:" + sparqlentityPerson.getValue(email) + "\nverified:" + sparqlentityPerson.getValue(verifiedEmail));
            }
        };

        try {
            this.mailSender.send(preparator);
        }
        catch (MailException ex) {
            logger.error(ex.getMessage());
        }
	}
	
	@Override
	public List<SparqlEntity> getUser(String email) throws SparqlException {

		SparqlEntity userSE = new SparqlEntity();
		userSE.setValue("email", email);

		// Select Person
		SelectScint personScint = new SelectScint();
		personScint.setClassHandler(getPersonClassHandler());
		personScint.setSparqlEntity(userSE);
		return sparqlDAO.query(personScint);
	}

	@Override
	public void setSparqlEntity(SparqlEntity s) {
		this.se=s;
	}

	@Override
	public SparqlEntity getSparqlEntity() {
		return se;
	}

	public String[] getRequiredFields() {
		return requiredFields;
	}

	public void setRequiredFields(String[] requiredFields) {
		this.requiredFields = requiredFields;
	}
}