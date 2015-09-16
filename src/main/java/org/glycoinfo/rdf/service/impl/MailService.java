package org.glycoinfo.rdf.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.mail.Message;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.velocity.app.VelocityEngine;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.scint.ClassHandler;
import org.glycoinfo.rdf.scint.InsertScint;
import org.glycoinfo.rdf.scint.SelectScint;
import org.glycoinfo.rdf.service.ContributorProcedure;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

@Service
public class MailService {
	Log logger = LogFactory.getLog(MailService.class);
	
	@Autowired(required=false)
	JavaMailSender mailSender;
	
	@Value("${mail.address.from}")
	String fromAddress;

	@Value("${mail.address.admin}")
	String adminAddress;
	
	@Value("${mail.body.newRegistration}")
	String newRegistrationBodyPath;

	@Value("${mail.body.notifyRegistration}")
	String notifyRegistrationBodyPath;

	@Value("${mail.subject.newRegistration}")
	String newRegistrationSubject;

	@Value("${mail.subject.notifyRegistration}")
	String notifyRegistrationSubject;

    private VelocityEngine velocityEngine;
	
	public void newRegistrationAdmin(SparqlEntity se) {
		logger.debug("sending email to:>" + adminAddress + "\nwith:>" + se);
		sendMail(adminAddress, fromAddress, notifyRegistrationSubject, notifyRegistrationBodyPath, se);
	}

	public void newRegistration(String toAddress, String name) {
		logger.debug("sending email to:>" + toAddress);
		sendMail(toAddress, fromAddress, newRegistrationSubject, newRegistrationBodyPath, name);
	}

	
	public void sendMail(final String to, final String from, final String subject, final String bodyVMPath, final Object se) {
        MimeMessagePreparator preparator = new MimeMessagePreparator() {
        	public void prepare(MimeMessage mimeMessage) throws Exception {

                mimeMessage.setRecipient(Message.RecipientType.TO,
                        new InternetAddress(to));
                mimeMessage.setFrom(new InternetAddress(from));
                mimeMessage.setSubject(subject);
                Map<String, Object> model = new HashMap();
                model.put("se", se);
                String text = VelocityEngineUtils.mergeTemplateIntoString( 
                        velocityEngine, bodyVMPath, "UTF-8", model);
                mimeMessage.setText(text);
        	}
        };

        try {
            this.mailSender.send(preparator);
        }
        catch (MailException ex) {
            logger.error(ex.getMessage());
        }
	}
}