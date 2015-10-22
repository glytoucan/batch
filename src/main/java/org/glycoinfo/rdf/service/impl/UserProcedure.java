package org.glycoinfo.rdf.service.impl;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.rdf.SelectSparqlBean;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.glycan.Contributor;
import org.glycoinfo.rdf.scint.ClassHandler;
import org.glycoinfo.rdf.scint.InsertScint;
import org.glycoinfo.rdf.scint.SelectScint;
import org.glycoinfo.rdf.service.ContributorProcedure;
import org.glycoinfo.rdf.utils.NumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
	@Qualifier(value = "insertScintProgramMembership")
	InsertScint insertScintProgramMembership;
	
	@Autowired
	@Qualifier(value = "selectScintProgramMembership")
	SelectScint selectScintProgramMembership;

	@Autowired
	@Qualifier(value = "contributorProcedure")
	ContributorProcedure contributorProcedure;
	
	@Autowired
	MailService mailService;

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

	ClassHandler getProgramMembershipClassHandler() throws SparqlException {
		ClassHandler classHandler = new ClassHandler("schema", "http://schema.org/", "ProgramMembership");
		classHandler.setSparqlDAO(sparqlDAO);
		return classHandler; 
	}
	
	@Override
	@Transactional
	public void addUser(SparqlEntity userSparqlEntity) throws SparqlException {
		Set<String> columns = userSparqlEntity.getColumns();
		if (!columns.containsAll(Arrays.asList(requiredFields))) {
			throw new SparqlException("not all required fields are supplied");
		}

		String email = userSparqlEntity.getValue("email");
		SparqlEntity usersWithEmail = getUser(email);

		// if have email and contributor id, then mapping was complete.
		if (null != usersWithEmail && StringUtils.isNotBlank(usersWithEmail.getValue(CONTRIBUTOR_ID)))
			return;

		// get primary key from parameter (security)
		String personUID = userSparqlEntity.getValue(SelectScint.PRIMARY_KEY);
		// check for primary key of user.  (should be at least at UUID)
		if ( personUID == null || personUID.equals("")) {
			throw new SparqlException("PRIMARY_KEY is >" + personUID + "<");
		}

		final SparqlEntity sparqlentityPerson = new SparqlEntity(personUID);
		
		for (String field : requiredFields) {
			if (field.equals("verifiedEmail"))
				continue;
			
			sparqlentityPerson.setValue(field, userSparqlEntity.getValue(field));
		}
		
		// if you have a verified email, you are a contributor of the glytoucan organization.
		
		// new Organization Class
		SelectScint organizationSelect = new SelectScint();
		organizationSelect.setClassHandler(getOrganizationClassHandler());
		
		// Organization entity
		SparqlEntity sparqlentityOrganization = new SparqlEntity("glytoucan");
		organizationSelect.setSparqlEntity(sparqlentityOrganization);
		
		String verified = userSparqlEntity.getValue(VERIFIED_EMAIL);
		if (verified != null && verified.equals("true")) {
			sparqlentityPerson.setValue("contributor", organizationSelect);
		}
		
		// otherwise just a member.
		sparqlentityPerson.setValue("member", organizationSelect);

		if (StringUtils.isBlank(userSparqlEntity.getValue(org.glycoinfo.rdf.service.UserProcedure.EMAIL)))
			throw new SparqlException("email cannot be blank.  Please fix account information.");
		
		if (StringUtils.isBlank(nameMap(sparqlentityPerson)))
			throw new SparqlException("given name cannot be blank.  Please fix account information or login with google+.");
		
		// check if Contributor exists, and map. 
		String contributorId = contributorProcedure.addContributor(nameMap(sparqlentityPerson));

		sparqlentityPerson.setValue(CONTRIBUTOR_ID, contributorId);
		
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
		
		String name = userSparqlEntity.getValue("givenName");

		String sendEmail = userSparqlEntity.getValue("sendEmail");
		if (StringUtils.isNotBlank(sendEmail)) {
		mailService.newRegistration(email, name);
		mailService.newRegistrationAdmin(sparqlEntityPerson);
		}
	}

	@Override
	@Transactional
	public String generateHash(String id) throws SparqlException {
//		SparqlEntity userSE = getUser(email);
//		String userid = userSE.getValue(ID);
		
		// check if it exists
		SparqlEntity sparqlEntityPerson = new SparqlEntity(id);
		selectScintPerson.setSparqlEntity(sparqlEntityPerson);
		List<SparqlEntity> person = sparqlDAO.query(selectScintPerson);
		if (null == person || !person.iterator().hasNext())
			throw new SparqlException("id >" + id + "< doesnt exist");
		
		insertScintPerson.setClassHandler(getPersonClassHandler());
		insertScintPerson.setSparqlEntity(sparqlEntityPerson);

		// ProgramMembership entity
		SparqlEntity sparqlentityProgramMembership = new SparqlEntity(GLYTOUCAN_PROGRAM + sparqlEntityPerson.getValue(SelectScint.PRIMARY_KEY));
		sparqlentityProgramMembership.setValue("programName", "Glytoucan Partner");
		
		sparqlentityProgramMembership.setValue("member", insertScintPerson);
		
		Date dateVal = new Date();
		
		sparqlentityProgramMembership.setValue(MEMBERSHIP_NUMBER, NumberGenerator.generateHash(sparqlEntityPerson.getValue(SelectScint.PRIMARY_KEY) + sparqlentityProgramMembership.getValue("programName") + sparqlentityProgramMembership.getValue(SelectScint.PRIMARY_KEY) + "SEED", dateVal));

		// set the sparqlentity for the registeraction. 
		insertScintProgramMembership.setClassHandler(getProgramMembershipClassHandler());
		insertScintProgramMembership.setSparqlEntity(sparqlentityProgramMembership);
		logger.debug(insertScintProgramMembership.getSparql());
		sparqlDAO.insert(insertScintProgramMembership);
		
		sparqlEntityPerson.setValue(MEMBER_OF, insertScintProgramMembership);
		insertScintPerson.setSparqlEntity(sparqlEntityPerson);
		sparqlDAO.insert(insertScintPerson);

		return sparqlentityProgramMembership.getValue(MEMBERSHIP_NUMBER);
	}

	private String nameMap(SparqlEntity sparqlEntity) {
		if (sparqlEntity.getValue(EMAIL).equals("aokinobu@gmail.com"))
			return "aoki";
		if (sparqlEntity.getValue(EMAIL).equals("d.shinmachi.aist@gmail.com"))
			return "daisuke shinmachi";
		if (sparqlEntity.getValue(EMAIL).equals("glytoucan@gmail.com"))
			return "Administrator";
		if (sparqlEntity.getValue(EMAIL).equals("kkiyoko@soka.ac.jp"))
			return "Kiyoko F. Aoki-Kinoshita";
		if (sparqlEntity.getValue(EMAIL).equals("yamadaissaku@gmail.com"))
			return "Issaku YAMADA";
		if (sparqlEntity.getValue(EMAIL).equals("e1156225@soka-u.jp"))
			return "Risa Sekimoto";
		if (sparqlEntity.getValue(EMAIL).equals("t.shikanai@aist.go.jp"))
			return "Toshihide Shikanai";
		if (sparqlEntity.getValue(EMAIL).equals("matsubara@noguchi.or.jp"))
			return "Masaaki Matsubara";
		if (sparqlEntity.getValue(EMAIL).equals("yusyahassy@gmail.com"))
			return "Nobuyuki Hashimoto";
		if (sparqlEntity.getValue(EMAIL).equals("dfsmith@emory.edu"))
			return "David F. Smith";
		if (sparqlEntity.getValue(EMAIL).equals("e0956224@gmail.com"))
			return "Yushi Takahashi";
		
		return sparqlEntity.getValue(org.glycoinfo.rdf.service.UserProcedure.GIVEN_NAME);
	}

	@Override
	public SparqlEntity getUser(String primaryId) throws SparqlException {
		SparqlEntity userSE = new SparqlEntity(primaryId);
		
		// Select Person
		SelectScint personScint = new SelectScint();
		personScint.setClassHandler(getPersonClassHandler());
		personScint.setSparqlEntity(userSE);
		
		List<SparqlEntity> userData = sparqlDAO.query(personScint);
		
		if (userData.size() > 1) 
			logger.warn("more than one userData for :>" + userSE);
		if (userData.iterator().hasNext()) {
			SparqlEntity first = userData.iterator().next();

			// if contributor id doesnt exist, find it.
			SparqlEntity contributor = null;
			if (StringUtils.isBlank(first.getValue(CONTRIBUTOR_ID))) {
				contributor = contributorProcedure.searchContributor(nameMap(first));
				if (StringUtils.isNotBlank(contributor.getValue(Contributor.ID))) {
					first.setValue(CONTRIBUTOR_ID, contributor.getValue(Contributor.ID));
				}
			}
			
			// set contributor name.
			first.setValue(Contributor.NAME, nameMap(first));
			
			SparqlEntity programMembership = new SparqlEntity();
			selectScintProgramMembership.setClassHandler(getProgramMembershipClassHandler());
			programMembership.setValue("member", personScint);
			
			selectScintProgramMembership.setSparqlEntity(programMembership);
			List<SparqlEntity> membershipData = sparqlDAO.query(selectScintProgramMembership);

			// if memberOf program, find id.
			if (!membershipData.isEmpty()) {
				// check for Hash
				for (Iterator iterator = membershipData.iterator(); iterator
						.hasNext();) {
					SparqlEntity sparqlEntity = (SparqlEntity) iterator
							.next();
					String programName = sparqlEntity.getValue("programName");
					String hash = null;
					if (StringUtils.isNotBlank(programName) && programName.equals("Glytoucan Partner"))
						hash = sparqlEntity.getValue("membershipNumber");
					first.setValue("membershipNumber", hash);
				}
			}
				
			return first;
		} else
			return null;
	}

	public String[] getRequiredFields() {
		return requiredFields;
	}

	public void setRequiredFields(String[] requiredFields) {
		this.requiredFields = requiredFields;
	}
	
	@Override
	public String getUserId(String email) throws SparqlException {
		SparqlEntity first = getUser(email);
		return first.getValue(CONTRIBUTOR_ID);
	}	
}