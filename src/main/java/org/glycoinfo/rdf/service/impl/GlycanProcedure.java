package org.glycoinfo.rdf.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.WURCSFramework.util.array.mass.WURCSMassException;
import org.glycoinfo.batch.mass.MassSparqlProcessor;
import org.glycoinfo.conversion.GlyConvertDetect;
import org.glycoinfo.conversion.error.ConvertException;
import org.glycoinfo.conversion.util.DetectFormat;
import org.glycoinfo.mass.MassInsertSparql;
import org.glycoinfo.mass.MassSelectSparql;
import org.glycoinfo.rdf.InsertSparql;
import org.glycoinfo.rdf.InsertSparqlBean;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.glycan.GlycoSequence;
import org.glycoinfo.rdf.glycan.ResourceEntryInsertSparql;
import org.glycoinfo.rdf.glycan.Saccharide;
import org.glycoinfo.rdf.glycan.SaccharideInsertSparql;
import org.glycoinfo.rdf.glycan.wurcs.GlycoSequenceToWurcsSelectSparql;
import org.glycoinfo.rdf.scint.ClassHandler;
import org.glycoinfo.rdf.service.ContributorProcedure;
import org.glycoinfo.rdf.utils.AccessionNumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GlycanProcedure implements org.glycoinfo.rdf.service.GlycanProcedure {

	public static Log logger = (Log) LogFactory.getLog(GlycanProcedure.class);

	@Autowired
	SparqlDAO sparqlDAO;

	@Autowired
	SaccharideInsertSparql saccharideInsertSparql;
	
	@Autowired
	ContributorProcedure contributorProcedure;
	
	@Autowired
	ResourceEntryInsertSparql resourceEntryInsertSparql;

	@Autowired
	SelectSparql glycoSequenceContributorSelectSparql;

	@Autowired
	InsertSparql wurcsRDFInsertSparql;

	@Autowired
	InsertSparql glycoSequenceInsert;
	
	@Autowired
	MassInsertSparql massInsertSparql;
	
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
	
	String sequence, image, id, sequenceResult, contributor, format;
	
	/**
	 * 
	 * {@PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#> .}
{@PREFIX glytoucan: <http://www.glytoucan.org/glyco/owl/glytoucan#> .}
{@PREFIX xsd:   <http://www.w3.org/2001/XMLSchema#> .}

<http://rdf.glycoinfo.org/glycan/G00054MO>
	a	glycan:saccharide ;
	glycan:has_resource_entry
		<http://www.glytoucan.org/Structures/Glycans/G00054MO> .
		
	 * <http://rdf.glycoinfo.org/Structures/Glycans/e06b141de8d13adfa0c3ad180b9eae06>
        a                          glycan:resource_entry ;
        glycan:in_glycan_database  glytoucan:database_glytoucan ;
        glytoucan:contributor      <http://rdf.glytoucan/contributor/userId/1> ;
        glytoucan:date_registered  "2014-10-20 06:47:31.204"^^xsd:dateTimeStamp .
     *
	 * @return
	 * @throws SparqlException
	 */

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
	
	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

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
	public SparqlEntity searchBySequence() throws SparqlException, ConvertException {
		
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
		List<SparqlEntity> list = searchSequence(sparqlSequence);
		logger.debug(list);
		if (list != null && list.size() < 1) {
			// if not wurcs, convert
			GlyConvertDetect glyconvert = new GlyConvertDetect();
			glyconvert.setToformat("wurcs");
			logger.debug("converting:>" + sequence + "<");
			glyconvert.setFromSequence(sequence);
			String wurcs = glyconvert.convert();
			logger.debug("converted:>" + wurcs + "<");
			list = searchSequence(wurcs);
			
			if (list != null && list.size() < 1) {
				if (null == se)
					se = new SparqlEntity();
				se.setValue(Sequence, wurcs);
				se.setValue(ResultSequence, wurcs);
				se.setValue(AccessionNumber, NotRegistered);
				setFormat(glyconvert.getFromFormat());
				return se;
			}
		}
		if (list != null && list.size() > 1)
		logger.warn("found more than one WURCS!");
		se = list.get(0);
		
		se.setValue(Image, "/glycans/" + se.getValue(AccessionNumber) + "/image?style=extended&format=png&notation=cfg");
		se.setValue(FromSequence, sparqlSequence);
		return se;
	}
	
	private List<SparqlEntity> searchSequence(String sparqlSequence) throws SparqlException {
		GlycoSequenceToWurcsSelectSparql select = new GlycoSequenceToWurcsSelectSparql();
		SparqlEntity se = new SparqlEntity();
		se.setValue(FromSequence, sparqlSequence);
		select.setSparqlEntity(se);
		
		return sparqlDAO.query(select);
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
				se = searchBySequence();
			} catch (ConvertException e) {
				logger.debug("convertexception seq:>" + getSequence());
				logger.debug("convertexception msg:>" + e.getMessage());
				se.setValue(Sequence, CouldNotConvertHeader + e.getMessage());
			}
			
			se.setValue(FromSequence, getSequence());
			list.add(se);
		}
		return list;
	}
	
	@Override
	public void setSequence(String sequence) {
		this.sequence=sequence;
	}

	/**
	 * @see org.glytoucan.ws.controller.RegistriesController#complete(javax.servlet.http.HttpServletRequest)
	 */
	@Override
	public String register(String sequence) throws SparqlException {
		setSequence(sequence);
		return register();
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
	public Map<String, String> register(List<String> inputs) throws SparqlException {
		Map<String, String> results = new HashMap<String, String>();
		for (Iterator iterator = inputs.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			String result = register(string);
			results.put(result, string);
		}
		return results;
	}
	
	/**
	 * 
	 * @throws SparqlException 
	 * @see org.glycoinfo.rdf.service.GlycanProcedure#register()
	 */
	@Override
	public String register() throws SparqlException {
		
		// check if it doesn't exist.
		try {
			SparqlEntity se = searchBySequence();
			if (null != se && se.getValue(AccessionNumber) != null && !se.getValue(AccessionNumber).equals(NotRegistered))
				throw new SparqlException(AlreadyRegistered + " as:>" + se.getValue(AccessionNumber) + "<");
		} catch (ConvertException e) {
			e.printStackTrace();
			// this should be impossible
			throw new SparqlException(e);
		}
		
		if (StringUtils.isBlank(contributor)) {
			throw new SparqlException("Contributor name cannot be blank");
		}
		
		String accessionNumber = "G" + AccessionNumberGenerator.generateRandomString(7);
		SparqlEntity result = searchByAccessionNumber(accessionNumber);
		while (result != null && StringUtils.isNotBlank(result.getValue(Saccharide.PrimaryId))) {
			logger.debug("rerolling... " + result.getValue(Saccharide.PrimaryId));
			
			result = searchByAccessionNumber(accessionNumber);
		}
		
		logger.debug("setting accession#:>" + accessionNumber + "<");
		
		setId(accessionNumber);
		SparqlEntity sparqlentity = new SparqlEntity();
		sparqlentity.setValue(Saccharide.PrimaryId, accessionNumber);
		
		saccharideInsertSparql.setSparqlEntity(sparqlentity);
		sparqlDAO.insert(saccharideInsertSparql);

		contributorProcedure.setName(contributor);
		String id = contributorProcedure.addContributor();
		
		resourceEntryInsertSparql.getSparqlEntity().setValue(Saccharide.URI, saccharideInsertSparql);
		resourceEntryInsertSparql.getSparqlEntity().setValue(ResourceEntryInsertSparql.AccessionNumber, accessionNumber);
		resourceEntryInsertSparql.getSparqlEntity().setValue(ResourceEntryInsertSparql.ContributorId, id);
		resourceEntryInsertSparql.getSparqlEntity().setValue(ResourceEntryInsertSparql.DataSubmittedDate, new Date());
		
//		resourceEntryInsertSparql.setSparqlEntity(reisSE);
		sparqlDAO.insert(resourceEntryInsertSparql);
		
		logger.debug("registering into wurcsRDF:" + accessionNumber + ":" + getSequence());
		
		if (null == wurcsRDFInsertSparql.getSparqlEntity())
			wurcsRDFInsertSparql.setSparqlEntity(new SparqlEntity());
		
		wurcsRDFInsertSparql.getSparqlEntity().setValue(Saccharide.PrimaryId, accessionNumber);
		wurcsRDFInsertSparql.getSparqlEntity().setValue(GlycoSequence.Sequence, getSequence());
		sparqlDAO.insert(wurcsRDFInsertSparql);
		
		// reasoning needed?
		// https://bitbucket.org/issaku/sparql/wiki/CONSTRUCT%20%E3%81%A7position%E3%82%92%E4%BB%98%E4%B8%8E%20%EF%BC%88WURCSSequence%EF%BC%89
		
		InsertSparqlBean insLabel = new InsertSparqlBean();
		insLabel.setGraph(wurcsRDFInsertSparql.getGraph());
		insLabel.setInsert("<http://rdf.glycoinfo.org/glycan/" + accessionNumber + "/wurcs/2.0> rdfs:label \"" + getSequence() + "\"^^xsd:string .");
		sparqlDAO.insert(insLabel);
		
		SparqlEntity massEntity = calculateMass(sequence);
		
		massEntity.setValue(Saccharide.URI, saccharideInsertSparql);
		massInsertSparql.setSparqlEntity(massEntity);
		sparqlDAO.insert(massInsertSparql);
		
		return accessionNumber;
	}

	private SparqlEntity calculateMass(String sequence2) {
		MassSparqlProcessor processor = new MassSparqlProcessor();
		SparqlEntity sparqlEntity = new SparqlEntity();
		sparqlEntity.setValue(MassSelectSparql.Sequence, sequence2);
		SparqlEntity se;
		try {
			se = processor.process(sparqlEntity);
		} catch (SparqlException | WURCSMassException e) {
			e.printStackTrace();
			logger.debug("could not calculate mass");
			se = new SparqlEntity();
			se.setValue(MassInsertSparql.Mass, 0);
			se.setValue(MassInsertSparql.MassLabel, "error calculating mass:>" + e.getMessage() + "<");
		}
		return se;
	}

	@Override
	public void deleteByAccessionNumber(String accessionNumber) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public SparqlEntity searchByAccessionNumber(String accessionNumber) throws SparqlException {
		SparqlEntity se = new SparqlEntity();
		se.setValue(Saccharide.PrimaryId, accessionNumber);
		glycoSequenceContributorSelectSparql.setSparqlEntity(se);
		List<SparqlEntity> list = sparqlDAO.query(glycoSequenceContributorSelectSparql);
		if (list.iterator().hasNext())
			return list.iterator().next();
		return null;
	}

	@Override
	public String getContributor() {
		return contributor;
	}

	@Override
	public void setContributor(String name) {
		this.contributor=name;
	}

	@Override
	public List<SparqlEntity> getGlycans() throws SparqlException {
		return sparqlDAO.query(glycoSequenceContributorSelectSparql);
	}

	@Override
	public void registerGlycoSequence() throws SparqlException {
		logger.debug("adding:" + getId() + " with >" + getSequence() + "< in " + getFormat() + " format.");
		glycoSequenceInsert.getSparqlEntity().setValue(Saccharide.PrimaryId, getId());
		glycoSequenceInsert.getSparqlEntity().setValue(GlycoSequence.Sequence, getSequence());
		glycoSequenceInsert.getSparqlEntity().setValue(GlycoSequence.Format, getFormat());

		SaccharideInsertSparql sis = new SaccharideInsertSparql();
		sis.setSparqlEntity(glycoSequenceInsert.getSparqlEntity());

		glycoSequenceInsert.getSparqlEntity().setValue(Saccharide.URI, sis.getUri());
		
		sparqlDAO.insert(glycoSequenceInsert);
	}

	@Override
	public String register(String orig, String newStructure) throws SparqlException {
		setId(register(newStructure));
		setSequence(orig);
		setFormat(DetectFormat.detect(orig));
		registerGlycoSequence();
		return getId();
	}
}