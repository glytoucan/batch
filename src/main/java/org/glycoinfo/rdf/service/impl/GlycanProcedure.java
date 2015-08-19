package org.glycoinfo.rdf.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.batch.glyconvert.wurcs.sparql.GlycoSequenceToWurcsSelectSparql;
import org.glycoinfo.conversion.GlyConvertDetect;
import org.glycoinfo.conversion.error.ConvertException;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.scint.ClassHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GlycanProcedure implements org.glycoinfo.rdf.service.GlycanProcedure {

	public static Log logger = (Log) LogFactory.getLog(GlycanProcedure.class);
	
	
	@Autowired
	SparqlDAO sparqlDAO;

//	@Autowired
//	@Qualifier(value = "selectscintglycosequence")
//	SelectScint selectScintPerson;
//
//	@Autowired
//	@Qualifier(value = "insertscintperson")
//	InsertScint insertScintPerson;
//	
//	@Autowired
//	@Qualifier(value = "selectscintregisteraction")
//	SelectScint selectScintRegisterAction;
//
//	@Autowired
//	@Qualifier(value = "insertscintregisteraction")
//	InsertScint insertScintRegisterAction;
	
	String sequence, image, id, sequenceResult; 

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
	
//	@Override
//	public void addUser() throws SparqlException {
//		Set<String> columns = getSparqlEntity().getColumns();
//		if (!columns.containsAll(Arrays.asList(requiredFields))) {
//			throw new SparqlException("not all required fields are supplied");
//		}
//
//		String personUID = getSparqlEntity().getValue(SelectScint.PRIMARY_KEY);
//		// check for primary key of user.  (should be at least at UUID)
//		if ( personUID == null || personUID.equals("")) {
//			throw new SparqlException("PRIMARY_KEY is >" + personUID + "<");
//		}
//
//		SparqlEntity sparqlentityPerson = new SparqlEntity(personUID);
//		
//		for (String field : requiredFields) {
//			if (field.equals("verifiedEmail"))
//				continue;
//			
//			sparqlentityPerson.setValue(field, getSparqlEntity().getValue(field));
//		}
//		
//		// if you have a verified email, you are a contributor of the glytoucan organization.
//		
//		// new Organization Class
//		SelectScint organizationSelect = new SelectScint();
//		organizationSelect.setClassHandler(getOrganizationClassHandler());
//		
//		// Organization entity
//		SparqlEntity sparqlentityOrganization = new SparqlEntity("glytoucan");
//		organizationSelect.setSparqlEntity(sparqlentityOrganization);
//		
//		String verified = getSparqlEntity().getValue("verifiedEmail");
//		if (verified != null && verified.equals("true")) {
//			sparqlentityPerson.setValue("contributor", organizationSelect);
//		}
//		
//		// otherwise just a member.
//		sparqlentityPerson.setValue("member", organizationSelect);
//		insertScintPerson.setSparqlEntity(sparqlentityPerson);
//		sparqlDAO.insert(insertScintPerson);
//		
//		// RegisterAction entity
//		// UID is userid + register class
//		SparqlEntity sparqlentityRegisterAction = new SparqlEntity(personUID);
//		
//		// new DateTime Class
//		SelectScint dateTimeSelect = new SelectScint();
//		dateTimeSelect.setClassHandler(getDateTimeClassHandler());
//		
//		// check for date time
//		
//		// no date time so use current
//		
//		// DateTime entity
//		SparqlEntity sparqlentityDateTime = new SparqlEntity(new Date());
//		dateTimeSelect.setSparqlEntity(sparqlentityDateTime);
//		
//		// set the datetime class to be the startTime range for the registeraction domain.
//		sparqlentityRegisterAction.setValue("startTime", dateTimeSelect);
//
//		SparqlEntity sparqlEntityPerson = new SparqlEntity(personUID);
//		SelectScint personSelect = new SelectScint();
//		personSelect.setClassHandler(getPersonClassHandler());
//		personSelect.setSparqlEntity(sparqlEntityPerson);
//		
//		sparqlentityRegisterAction.setValue("participant", personSelect);
//
//		// set the sparqlentity for the registeraction. 
//		insertScintRegisterAction.setSparqlEntity(sparqlentityRegisterAction);
//		
//		sparqlDAO.insert(insertScintRegisterAction);
//	}
	
//	@Override
//	public List<SparqlEntity> getUser(String email) throws SparqlException {
//
//		SparqlEntity userSE = new SparqlEntity();
//		userSE.setValue("email", email);
//
//		// Select Person
//		SelectScint personScint = new SelectScint();
//		personScint.setClassHandler(getPersonClassHandler());
//		personScint.setSparqlEntity(userSE);
//		return sparqlDAO.query(personScint);
//	}

//	@Override
//	public void setSparqlEntity(SparqlEntity s) {
//		this.se=s;
//	}
//
//	@Override
//	public SparqlEntity getSparqlEntity() {
//		return se;
//	}

	
	/**
	 * 
	 * Search for an image using any sequence format.
	 * This process takes in the input from setSequence and executes the following:
	 * 1. determine the sequence type using DetectFormat.
	 * 2. based on format type, select the WURCS format from RDF using GlycoSequence.
	 * 3. if it doesn't exist, convert using GlyConvert.
	 * 4. convert into image format
	 * 5. set the id to accession number
	 * @throws ConvertException 
	 * 
	 * @see org.glycoinfo.rdf.service.GlycanProcedure#search()
	 */
	@Override
	public SparqlEntity search() throws SparqlException, ConvertException {
		
		String sequence = getSequence().trim();
		logger.debug("sequence:>"+sequence+"<");
//		try {
//			logger.debug("sequenceencode:>"+URLEncoder.encode(sequence, "UTF-8")+"<");
//		} catch (UnsupportedEncodingException e) {
//		}
		String sparqlSequence = null;
		sequence = sequence.trim();
		sparqlSequence = sequence.replaceAll("(?:\\r\\n|\\n)", "\\\\n");
		
//		String format = DetectFormat.detect(sequence);
		
		// check RDF for wurcs
		logger.debug("searching for:>" + sparqlSequence + "<");
		GlycoSequenceToWurcsSelectSparql select = new GlycoSequenceToWurcsSelectSparql();
		SparqlEntity se = new SparqlEntity();
		se.setValue(GlycoSequenceToWurcsSelectSparql.FromSequence, sparqlSequence);
		select.setSparqlEntity(se);
		
		List<SparqlEntity> list = sparqlDAO.query(select);
		logger.debug(list);
		if (list != null && list.size() < 1) {
			// if not wurcs, convert
			GlyConvertDetect glyconvert = new GlyConvertDetect();
			glyconvert.setToformat("wurcs");
			logger.debug("converting:>" + sequence + "<");
			glyconvert.setFromSequence(sequence);
			String wurcs = glyconvert.convert();
			se.setValue(GlycoSequenceToWurcsSelectSparql.Sequence, wurcs);
			se.setValue(GlycoSequenceToWurcsSelectSparql.AccessionNumber, NotRegistered);
		} else {
			if (list != null && list.size() > 1)
				logger.warn("found more than one WURCS!");
			se = list.get(0);
			se.setValue(Image, "/glyspace/service/glycans/" + se.getValue(GlycoSequenceToWurcsSelectSparql.AccessionNumber) + "/image?style=extended&format=png&notation=cfg");
			se.setValue(GlycoSequenceToWurcsSelectSparql.FromSequence, sparqlSequence);
		}
		return se;
	}
	
	@Override
	public List<SparqlEntity> search(List<String> input) throws SparqlException {
		List<SparqlEntity> list = new ArrayList<SparqlEntity>();
		for (Iterator iterator = input.iterator(); iterator.hasNext();) {
			String sequence = (String) iterator.next();
			sequence = sequence.trim();
			if (sequence.endsWith("\\r\\n"))
				sequence = sequence.substring(0, sequence.length() - 4);
			if (sequence.endsWith("\\n"))
				sequence = sequence.substring(0, sequence.length() - 2);
			logger.debug("sequence:>"+sequence+"<");
			
			setSequence(sequence.trim());
			SparqlEntity se = new SparqlEntity();
			
			try {
				se = search();
			} catch (ConvertException e) {
				logger.debug("convertexception seq:>" + getSequence());
				logger.debug("convertexception msg:>" + e.getMessage());
				se.setValue(GlycoSequenceToWurcsSelectSparql.FromSequence, getSequence());
				se.setValue(GlycoSequenceToWurcsSelectSparql.Sequence, CouldNotConvertHeader + e.getMessage());
			}
			
			list.add(se);
		}
		return list;
	}
	
	@Override
	public void setSequence(String sequence) {
		this.sequence=sequence;
	}

	@Override
	public void register(String sequence) throws SparqlException {
		
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSequenceResult() {
		return sequenceResult;
	}

	public void setSequenceResult(String sequenceResult) {
		this.sequenceResult = sequenceResult;
	}

	public String getSequence() {
		return sequence;
	}

	@Override
	public void register(List<String> inputs) throws SparqlException {
		for (Iterator iterator = inputs.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			register(string);
		}
	}

	@Override
	public void check(List<String> inputs) {
		
		
	}
}