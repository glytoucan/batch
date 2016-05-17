package org.glycoinfo.rdf.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.WURCSFramework.util.WURCSException;
import org.glycoinfo.WURCSFramework.util.WURCSFactory;
import org.glycoinfo.WURCSFramework.util.array.mass.WURCSMassException;
import org.glycoinfo.WURCSFramework.wurcs.array.WURCSArray;
import org.glycoinfo.batch.mass.MassSparqlProcessor;
import org.glycoinfo.batch.search.wurcs.SubstructureSearchSparql;
import org.glycoinfo.convert.GlyConvert;
import org.glycoinfo.convert.GlyConvertDetect;
import org.glycoinfo.convert.error.ConvertException;
import org.glycoinfo.convert.util.DetectFormat;
import org.glycoinfo.rdf.DuplicateException;
import org.glycoinfo.rdf.InsertSparql;
import org.glycoinfo.rdf.InsertSparqlBean;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SelectSparqlBean;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.glycan.DerivatizedMass;
import org.glycoinfo.rdf.glycan.GlycoSequence;
import org.glycoinfo.rdf.glycan.ResourceEntry;
import org.glycoinfo.rdf.glycan.Saccharide;
import org.glycoinfo.rdf.glycan.SaccharideInsertSparql;
import org.glycoinfo.rdf.glycan.SaccharideSelectSparql;
import org.glycoinfo.rdf.glycan.SaccharideUtil;
import org.glycoinfo.rdf.glycan.mass.MassInsertSparql;
import org.glycoinfo.rdf.glycan.mass.MassSelectSparql;
//import org.glycoinfo.rdf.glycan.msdb.MSInsertSparql;
import org.glycoinfo.rdf.glycan.wurcs.GlycoSequenceToWurcsSelectSparql;
import org.glycoinfo.rdf.glycan.wurcs.MonosaccharideSelectSparql;
import org.glycoinfo.rdf.glycan.wurcs.MotifSequenceSelectSparql;
import org.glycoinfo.rdf.glycan.wurcs.WurcsRDFMSInsertSparql;
import org.glycoinfo.rdf.scint.ClassHandler;
import org.glycoinfo.rdf.service.ContributorProcedure;
import org.glycoinfo.rdf.utils.NumberGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
public class GlycanProcedure implements org.glycoinfo.rdf.service.GlycanProcedure {


	public static Log logger = (Log) LogFactory.getLog(GlycanProcedure.class);

	@Autowired
	GlyConvertDetect glyConvertDetect;
	
	boolean isBatch;

	public boolean isBatch() {
		return isBatch;
	}

	public void setBatch(boolean isBatch) {
		this.isBatch = isBatch;
	}

	@Autowired
	SparqlDAO sparqlDAO;

	@Autowired
	@Qualifier("SaccharideInsert")
	InsertSparql saccharideInsertSparql;

	@Autowired
	ContributorProcedure contributorProcedure;

	@Autowired
	@Qualifier("ResourceEntryInsert")
	InsertSparql resourceEntryInsertSparql;

	@Autowired
	SelectSparql glycoSequenceContributorSelectSparql;

	@Autowired
	SelectSparql listAllGlycoSequenceContributorSelectSparql;

	@Autowired
	InsertSparql wurcsRDFInsertSparql;

	@Autowired
	WurcsRDFMSInsertSparql wurcsRDFMSInsertSparql;

	@Autowired
	@Qualifier("GlycosequenceInsert")
	InsertSparql glycoSequenceInsert;

	@Autowired
	MassInsertSparql massInsertSparql;

	@Autowired
	SubstructureSearchSparql substructureSearchSparql;

	@Autowired
	MotifSequenceSelectSparql motifSequenceSelectSparql;

	@Autowired
	SaccharideSelectSparql saccharideSelectSparql;

	@Autowired
	MonosaccharideSelectSparql monosaccharideSelectSparql;

//	@Autowired
//	public MSInsertSparql msInsertSparql;

	// @Autowired
	// public SparqlEntityFactory sparqlEntityFactory;

	//
	// @Autowired
	// @Qualifier(value = "insertscintperson")
	// InsertScint insertScintPerson;
	//
	// @Autowired
	// @Qualifier(value = "selectscintregisteraction")
	// SelectScint selectScintRegisterAction;
	//
	// @Autowired
	// @Qualifier(value = "insertscintregisteraction")
	// InsertScint insertScintRegisterAction;

	/**
	 * 
	 * {@PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#> .}
	 * {@PREFIX glytoucan: <http://www.glytoucan.org/glyco/owl/glytoucan#> .}
	 * {@PREFIX xsd: <http://www.w3.org/2001/XMLSchema#> .}
	 * 
	 * <http://rdf.glycoinfo.org/glycan/G00054MO> a glycan:saccharide ;
	 * glycan:has_resource_entry
	 * <http://www.glytoucan.org/Structures/Glycans/G00054MO> .
	 * 
	 * <http://rdf.glycoinfo.org/Structures/Glycans/e06b141de8d13adfa0c3ad180b9eae06>
	 * a glycan:resource_entry ; glycan:in_glycan_database
	 * glytoucan:database_glytoucan ; glytoucan:contributor
	 * <http://rdf.glytoucan/contributor/userId/1> ; glytoucan:date_registered
	 * "2014-10-20 06:47:31.204"^^xsd:dateTimeStamp .
	 *
	 * @return
	 * @throws SparqlException
	 */

	ClassHandler getPersonClassHandler() throws SparqlException {
		ClassHandler scint = new ClassHandler("schema", "http://schema.org/", "Person");
		scint.setSparqlDAO(sparqlDAO);
		return scint;
	}

	// ClassHandler getRegisterActionClassHandler() throws SparqlException {
	// ClassHandler scint = new ClassHandler("schema", "http://schema.org/",
	// "RegisterAction");
	// scint.setSparqlDAO(sparqlDAO);
	// return scint;
	// }

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

	// @Override
	// public void addUser() throws SparqlException {
	// Set<String> columns = getSparqlEntity().getColumns();
	// if (!columns.containsAll(Arrays.asList(requiredFields))) {
	// throw new SparqlException("not all required fields are supplied");
	// }
	//
	// String personUID = getSparqlEntity().getValue(SelectScint.PRIMARY_KEY);
	// // check for primary key of user. (should be at least at UUID)
	// if ( personUID == null || personUID.equals("")) {
	// throw new SparqlException("PRIMARY_KEY is >" + personUID + "<");
	// }
	//
	// SparqlEntity sparqlentityPerson = new SparqlEntity(personUID);
	//
	// for (String field : requiredFields) {
	// if (field.equals("verifiedEmail"))
	// continue;
	//
	// sparqlentityPerson.setValue(field, getSparqlEntity().getValue(field));
	// }
	//
	// // if you have a verified email, you are a contributor of the glytoucan
	// organization.
	//
	// // new Organization Class
	// SelectScint organizationSelect = new SelectScint();
	// organizationSelect.setClassHandler(getOrganizationClassHandler());
	//
	// // Organization entity
	// SparqlEntity sparqlentityOrganization = new SparqlEntity("glytoucan");
	// organizationSelect.setSparqlEntity(sparqlentityOrganization);
	//
	// String verified = getSparqlEntity().getValue("verifiedEmail");
	// if (verified != null && verified.equals("true")) {
	// sparqlentityPerson.setValue("contributor", organizationSelect);
	// }
	//
	// // otherwise just a member.
	// sparqlentityPerson.setValue("member", organizationSelect);
	// insertScintPerson.setSparqlEntity(sparqlentityPerson);
	// sparqlDAO.insert(insertScintPerson);
	//
	// // RegisterAction entity
	// // UID is userid + register class
	// SparqlEntity sparqlentityRegisterAction = new SparqlEntity(personUID);
	//
	// // new DateTime Class
	// SelectScint dateTimeSelect = new SelectScint();
	// dateTimeSelect.setClassHandler(getDateTimeClassHandler());
	//
	// // check for date time
	//
	// // no date time so use current
	//
	// // DateTime entity
	// SparqlEntity sparqlentityDateTime = new SparqlEntity(new Date());
	// dateTimeSelect.setSparqlEntity(sparqlentityDateTime);
	//
	// // set the datetime class to be the startTime range for the
	// registeraction domain.
	// sparqlentityRegisterAction.setValue("startTime", dateTimeSelect);
	//
	// SparqlEntity sparqlEntityPerson = new SparqlEntity(personUID);
	// SelectScint personSelect = new SelectScint();
	// personSelect.setClassHandler(getPersonClassHandler());
	// personSelect.setSparqlEntity(sparqlEntityPerson);
	//
	// sparqlentityRegisterAction.setValue("participant", personSelect);
	//
	// // set the sparqlentity for the registeraction.
	// insertScintRegisterAction.setSparqlEntity(sparqlentityRegisterAction);
	//
	// sparqlDAO.insert(insertScintRegisterAction);
	// }

	// @Override
	// public List<SparqlEntity> getUser(String email) throws SparqlException {
	//
	// SparqlEntity userSE = new SparqlEntity();
	// userSE.setValue("email", email);
	//
	// // Select Person
	// SelectScint personScint = new SelectScint();
	// personScint.setClassHandler(getPersonClassHandler());
	// personScint.setSparqlEntity(userSE);
	// return sparqlDAO.query(personScint);
	// }

	// @Override
	// public void setSparqlEntity(SparqlEntity s) {
	// this.se=s;
	// }
	//
	// @Override
	// public SparqlEntity getSparqlEntity() {
	// return se;
	// }

	/**
	 * 
	 * Search for structure using any sequence format. This process executes the
	 * following:
	 * 
	 * 1. convert to wurcs 2. search rdf 3. convert into image format 4. set the
	 * id to accession number
	 * 
	 * @throws ConvertException
	 * 
	 * @see org.glycoinfo.rdf.service.impl.GlycanProcedure#convertToWurcs(String
	 *      sequence)
	 */
	@Override
	public SparqlEntity searchBySequence(String sequence) throws SparqlException, ConvertException {
		logger.debug("sequence:>" + sequence + "<");

		String wurcs = convertToWurcs(sequence);

		// check RDF for wurcs
		logger.debug("searching for:>" + wurcs + "<");
		List<SparqlEntity> list = searchSequence(wurcs);
		logger.debug(list);

		SparqlEntity searchResultSE = new SparqlEntity();
		if (list != null && list.size() < 1) {
			logger.debug("not found:>" + sequence + "|" + sequence + "<");
			searchResultSE.setValue(FromSequence, sequence);
			searchResultSE.setValue(Sequence, wurcs);
			searchResultSE.setValue(ResultSequence, wurcs);
			searchResultSE.setValue(AccessionNumber, NotRegistered);
			return searchResultSE;
		}

		if (list != null && list.size() > 1)
			logger.warn("found more than one WURCS!");
		searchResultSE = list.get(0);

		searchResultSE.setValue(Image, "/glycans/" + searchResultSE.getValue(AccessionNumber)
				+ "/image?style=extended&format=png&notation=cfg");
		searchResultSE.setValue(FromSequence, sequence);
		return searchResultSE;
	}

	private List<SparqlEntity> searchSequence(String sparqlSequence) throws SparqlException {
		GlycoSequenceToWurcsSelectSparql select = new GlycoSequenceToWurcsSelectSparql();
		SparqlEntity se = new SparqlEntity();
		se.setValue(FromSequence, sparqlSequence);
		select.setSparqlEntity(se);
		logger.debug("searching with:>" + select + "<");
		return sparqlDAO.query(select);
	}

	@Override
	public List<SparqlEntity> search(List<String> input) throws SparqlException {
		List<SparqlEntity> list = new ArrayList<SparqlEntity>();
		for (Iterator iterator = input.iterator(); iterator.hasNext();) {
			String sequence = (String) iterator.next();
			String sparqlSequence = null;
			sparqlSequence = sequence.replaceAll("(?:\\r\\n|\\n)", "\\\\n");

			logger.debug("sequence:>" + sparqlSequence + "<");

			sparqlSequence = sparqlSequence.trim();

			SparqlEntity se = new SparqlEntity();

			try {
				se = searchBySequence(sparqlSequence);
			} catch (ConvertException e) {
				logger.error("convertexception seq:>" + sparqlSequence);
				logger.error("convertexception msg:>" + e.getMessage());
				se.setValue(Sequence, CouldNotConvertHeader + e.getMessage());
			}

			// se.setValue(FromSequence, getSequence());
			logger.debug("adding to search list:>" + se + "<");
			list.add(se);
		}
		return list;
	}

	@Override
	public Map<String, String> register(List<String> inputs, String contributorId) throws SparqlException {
		Map<String, String> results = new HashMap<String, String>();
		for (Iterator iterator = inputs.iterator(); iterator.hasNext();) {
			String string = (String) iterator.next();
			String result = register(string, contributorId);
			results.put(result, string);
		}
		return results;
	}

	/**
	 * 
	 * Registration of a glycan using standard registration method via either
	 * the text entry, graphical, file upload, or REST api. It first checks the
	 * structure by converting it to WURCS format. If the translation fails, an
	 * exception is thrown with the ConvertException embedded.
	 * 
	 * A primary policy that should be protected is if the sequence cannot be
	 * converted to wurcs, it will not be accepted.
	 * 
	 * If found in either case above, then it is already registered and a
	 * SparqlException is thrown.
	 * 
	 * Otherwise, it sets the FromSequence as the one first input, with the
	 * Sequence element assumed to be the wurcs sequence.
	 * 
	 * It is now possible to move onto the registration process. It cannot be
	 * registered if the user's information (contributor) is blank.
	 *
	 * Core info such as Saccharide Contributor, and ResourceEntry are then
	 * inserted. For backwards compatibility, when the wurcs file could not be
	 * converted, it was added into the rdfs label for the sequence. This
	 * functionality is kept even though WurcsRDF adds similar information.
	 * 
	 * addWurcs() method is then called
	 * 
	 * @throws SparqlException
	 *             AlreadyRegistered + " as:>" + se.getValue(AccessionNumber) +
	 *             "<"
	 * @see org.glycoinfo.rdf.service.GlycanProcedure#register()
	 */
	@Override
	public String register(String sequence, String contributorId) throws SparqlException {

		if (StringUtils.isBlank(contributorId)) {
			throw new SparqlException("Contributor id cannot be blank");
		}

		SparqlEntity sparqlentity = new SparqlEntity();
		String accessionNumber = null;

		// check if it doesn't exist.
		try {
			sparqlentity = searchBySequence(sequence);
			if (null != sparqlentity && sparqlentity.getValue(AccessionNumber) != null
					&& !sparqlentity.getValue(AccessionNumber).equals(NotRegistered)) {
				throw new DuplicateException(AlreadyRegistered + " as:>" + sparqlentity.getValue(AccessionNumber) + "<",
						sparqlentity.getValue(AccessionNumber));
			}
			logger.debug("setting from sequence:>" + sparqlentity.getValue(GlycanProcedure.FromSequence)
					+ "< sequence:>" + sparqlentity.getValue(GlycanProcedure.Sequence) + "<");
		} catch (ConvertException e) {
			logger.error("convert exception processing:>" + sequence + "<");
			String errorMessage = null;
			if (e.getMessage() != null && e.getMessage().length() > 0)
				errorMessage = e.getMessage();
			throw new SparqlException(errorMessage, e);
		}

		logger.debug("registering wurcs sequence:>" + sparqlentity.getValue(GlycanProcedure.Sequence));

		accessionNumber = "G" + NumberGenerator.generateRandomString(7);

		// SparqlEntity searchAccNumEntity = sparqlEntityFactory.create();
		SparqlEntity result = searchByAccessionNumber(accessionNumber);
		while (result != null && StringUtils.isNotBlank(result.getValue(Saccharide.PrimaryId))) {
			logger.debug("rerolling... " + result.getValue(Saccharide.PrimaryId));

			result = searchByAccessionNumber(accessionNumber);
		}

		logger.debug("setting accession#:>" + accessionNumber + "<");

		sparqlentity.setValue(Saccharide.PrimaryId, accessionNumber);

		saccharideInsertSparql.setSparqlEntity(sparqlentity);
		sparqlDAO.insert(saccharideInsertSparql);

		// contributorProcedure.setId(contributorId);
		// String id = contributorProcedure.searchContributor(userin);

		resourceEntryInsertSparql.getSparqlEntity().setValue(Saccharide.PrimaryId, accessionNumber);
		resourceEntryInsertSparql.getSparqlEntity().setValue(ResourceEntry.Identifier, accessionNumber);
		resourceEntryInsertSparql.getSparqlEntity().setValue(ResourceEntry.Database, ResourceEntry.Database_Glytoucan);

		resourceEntryInsertSparql.getSparqlEntity().setValue(ResourceEntry.ContributorId, contributorId);
		resourceEntryInsertSparql.getSparqlEntity().setValue(ResourceEntry.DataSubmittedDate, new Date());

		// resourceEntryInsertSparql.setSparqlEntity(reisSE);
		sparqlDAO.insert(resourceEntryInsertSparql);
		// }

		logger.debug("registering wurcs:>" + accessionNumber + "\nsequence:>"
				+ sparqlentity.getValue(GlycanProcedure.Sequence));

		addWurcs(sparqlentity);

		// if adding wurcs is fine, and the original isn't wurcs, then the
		// translation was valid. So record the original as well. (in a
		// different graph of course)
		SparqlEntity glycoctSE = sparqlentity;
		glycoctSE.setValue(GlycoSequence.Sequence, sparqlentity.getValue(GlycanProcedure.FromSequence));

		registerGlycoSequence(sparqlentity);

		return accessionNumber;
	}

	@Override
	public String convertToWurcs(String sequence) throws ConvertException {
		logger.debug("sequence to convert:>" + sequence + "<");

		// conversion will validate
		String wurcs = glyConvertDetect.convert(sequence, GlyConvert.WURCS);

		// just in case, validate it.
		try {
			wurcs = validateWurcs(wurcs);
		} catch (WURCSException e) {
			logger.error("wurcs expception:>" + e.getMessage());
			throw new ConvertException(e.getMessage(), e);
		}

		return wurcs;
	}

	@Override
	public String validateWurcs(String sequence) throws WURCSException {
		sequence = sequence.trim();
		logger.debug("validating:>" + sequence + "<");
		WURCSFactory factory;
		factory = new WURCSFactory(sequence);
		WURCSArray t_oArray = factory.getArray();
		// WURCSSequence2 t_oSeq2 = factory.getSequence();
		String wurcs = factory.getWURCS();
		return wurcs;
	}

	/**
	 * 
	 * sets the sequence, and primary id and calls the wurcsRDFInsertSparql.
	 * 
	 * then runs the wurcsRDFMSInsertSparql for the monosaccharide data.
	 * 
	 * calculates mass based upon wurcs.
	 * 
	 * @throws SparqlException
	 */
	public void addWurcs(SparqlEntity data) throws SparqlException {
		logger.debug("registering wurcs:>" + data.getValue(Saccharide.PrimaryId) + "\nsequence:>"
				+ data.getValue(GlycanProcedure.Sequence));
		wurcsRDFInsertSparql.setSparqlEntity(data);
		sparqlDAO.insert(wurcsRDFInsertSparql);

		wurcsRDFMSInsertSparql.setSparqlEntity(data);
		sparqlDAO.insert(wurcsRDFMSInsertSparql);

		// reasoning needed?
		// https://bitbucket.org/issaku/sparql/wiki/CONSTRUCT%20%E3%81%A7position%E3%82%92%E4%BB%98%E4%B8%8E%20%EF%BC%88WURCSSequence%EF%BC%89

		// make sure glycosequence inserted correctly

		InsertSparqlBean insLabel = new InsertSparqlBean();

		insLabel.setGraph(wurcsRDFInsertSparql.getGraph());

		insLabel.setInsert("<http://rdf.glycoinfo.org/glycan/" + data.getValue(Saccharide.PrimaryId)
				+ "/wurcs/2.0> rdfs:label \"" + data.getValue(GlycanProcedure.Sequence) + "\"^^xsd:string .");
		sparqlDAO.insert(insLabel);

		// SparqlEntity msSE = new SparqlEntity();
		// msSE.setValue(WurcsMonosaccharide.PrimaryId, getId());
		// monosaccharideSelectSparql.setSparqlEntity(msSE);
		// List<SparqlEntity> msReslist =
		// sparqlDAO.query(monosaccharideSelectSparql);
		// for (SparqlEntity msResSE : msReslist) {
		// String wurcsMSUri =
		// msResSE.getValue(WurcsMonosaccharide.WurcsMonosaccharideURI);
		//// http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/a2112h-1a_1-5_2*NCC%2F3%3DO
		// logger.debug("wurcsMSUri:>" + wurcsMSUri);
		// String key =
		// wurcsMSUri.replace("http://rdf.glycoinfo.org/glycan/wurcs/2.0/monosaccharide/",
		// "");
		// try {
		// key = URLDecoder.decode(key, "UTF-8");
		// } catch (UnsupportedEncodingException e) {
		// e.printStackTrace();
		// }
		// SparqlEntity msISE = new SparqlEntity();
		// logger.debug("key:>" + key);
		// msISE.setValue(Monosaccharide.Residue, key);
		// msInsertSparql.setSparqlEntity(msISE);
		// sparqlDAO.insert(msInsertSparql);
		// }

		SparqlEntity massEntity = calculateMass(data.getValue(GlycanProcedure.Sequence));

		saccharideInsertSparql.setSparqlEntity(wurcsRDFInsertSparql.getSparqlEntity());
		massEntity.setValue(Saccharide.URI, saccharideInsertSparql);
		massInsertSparql.setSparqlEntity(massEntity);
		sparqlDAO.insert(massInsertSparql);
	}

	private SparqlEntity calculateMass(String sequence2) {
		MassSparqlProcessor processor = new MassSparqlProcessor();
		SparqlEntity sparqlEntity = new SparqlEntity();
		sparqlEntity.setValue(MassSelectSparql.Sequence, sequence2);
		SparqlEntity se;
		try {
			se = processor.process(sparqlEntity);
		} catch (SparqlException | WURCSMassException e) {
			logger.error("could not calculate mass" + e.getMessage());
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
		// logger.debug("sparqlEntityFactory:>" + sparqlEntityFactory + "<");
		// logger.debug("SparqlEntity:>" + se + "<");
		se.setValue(Saccharide.PrimaryId, accessionNumber);
		return searchByAccessionNumber(se);
	}

	@Override
	public SparqlEntity searchByAccessionNumber(SparqlEntity accessionNumber) throws SparqlException {
		// SparqlEntity se = new SparqlEntity();
		// se.setValue(Saccharide.PrimaryId, accessionNumber);
		glycoSequenceContributorSelectSparql.setSparqlEntity(accessionNumber);
		List<SparqlEntity> list = sparqlDAO.query(glycoSequenceContributorSelectSparql);
		if (list.iterator().hasNext())
			return list.iterator().next();
		return null;
	}

	/**
	 * 
	 * Currently the following are the core data points that need to be
	 * retrieved for a detailed description of the glycan:
	 * 
	 * accession number mass IUPAC formatted sequence string:
	 * GlycoSequence.Sequence
	 * 
	 * @param accessionNumber
	 * @return
	 */
	 @Override
	public SparqlEntity getDescription(String accessionNumber) {
		// "?s glytoucan:has_primary_id \"" + accessionNumber + "\" .\n" +
		String sparql = "PREFIX glycan: <http://purl.jp/bio/12/glyco/glycan#>\n"
				+ "PREFIX glytoucan: <http://www.glytoucan.org/glyco/owl/glytoucan#>\n"
				+ "SELECT distinct ?iupac ?" + GlycoSequence.Sequence + " ?" + DerivatizedMass.MassLabel + " ?" 
				+ DerivatizedMass.MassType + " ?" + DerivatizedMass.MassValue + "\n"
				+ "WHERE { \n"
				+ "?s a glycan:saccharide .\n" + "?s glytoucan:has_primary_id \"" + accessionNumber + "\" .\n"
        + "?s glycan:has_glycosequence ?wurcsgseq .\n" + "?wurcsgseq glycan:has_sequence ?" + GlycoSequence.Sequence + " .\n"
        + "?wurcsgseq glycan:in_carbohydrate_format glycan:carbohydrate_format_wurcs .\n" 
				+ "optional {\n"
				+ "?s glycan:has_glycosequence ?gseq .\n" + "?gseq glycan:has_sequence ?iupac .\n"
				+ "?gseq glycan:in_carbohydrate_format glycan:carbohydrate_format_iupac .\n"
				+ "}" 
				+ "optional {\n"
				+ "?s glytoucan:has_derivatized_mass ?mass .\n" + "?mass rdf:type        glytoucan:derivatized_mass .\n"
				+ "?mass rdfs:label        ?" + DerivatizedMass.MassLabel + " .\n" + "?mass glytoucan:has_mass        ?" + DerivatizedMass.MassValue + " .\n"
				+ "?mass glytoucan:has_derivatization_type ?" 
				+ DerivatizedMass.MassType + " .\n" + "}\n" + "}\n" + "limit 10";
		StringBuilder desc = new StringBuilder("Accession Number " + accessionNumber + " is a Glycan Sequence registered in the GlyTouCan repository.");
		SparqlEntity se = null;

		List<SparqlEntity> seList = null;
		try {
			seList = sparqlDAO.query(new SelectSparqlBean(sparql));
		} catch (SparqlException e) {
			e.printStackTrace();
			se = new SparqlEntity();
			se.setValue(Description, desc.toString());
			
			return se;
		}

		String iupac = null, massvalue = null, masslabel = null, masstype = null, sequence = null;

		if (seList.iterator().hasNext()) {
			se = seList.iterator().next();
			iupac = se.getValue("iupac");
			sequence= se.getValue(GlycoSequence.Sequence);
			massvalue = se.getValue(DerivatizedMass.MassValue);
			masslabel = se.getValue(DerivatizedMass.MassLabel);
			masstype = se.getValue(DerivatizedMass.MassType);
		}

		logger.debug("mass label:>" + masslabel);
		logger.debug("mass type:>" + masstype);
		logger.debug("mass value:>" + massvalue);
		logger.debug("IUPAC:>" + iupac);
		
		if (StringUtils.isNotBlank(iupac))
			desc.append("  The IUPAC representation is " + iupac + ".");
		if (StringUtils.isNotBlank(sequence))
			desc.append("  The WURCS representation is " + sequence + ".");

		if (null != massvalue) {
			desc.append("  It has a mass of " + masslabel);
			if (masstype.contains("derivatization_type_none"))
				desc.append(", computed without any derivatization");
			desc.append(".");
		}
		se.setValue(Description, desc.toString());
		se.setValue(ResourceEntry.Identifier, accessionNumber);

		return se;
	}

	@Override
	public List<SparqlEntity> getGlycans(String offset, String limit) throws SparqlException {
		listAllGlycoSequenceContributorSelectSparql.setLimit(limit);
		listAllGlycoSequenceContributorSelectSparql.setOffset(offset);
		return sparqlDAO.query(listAllGlycoSequenceContributorSelectSparql);
	}

	/**
	 * 
	 * Registers into RDF using the Saccharide.PrimaryId for the pid,
	 * GlycoSequence.Sequence for the sequence string, and DetectFormat.detect
	 * to confirm the format.
	 * 
	 * 
	 * @see org.glycoinfo.rdf.service.GlycanProcedure#registerGlycoSequence(org.glycoinfo.rdf.dao.SparqlEntity)
	 */
	@Override
	public void registerGlycoSequence(SparqlEntity data) throws SparqlException {
		String sequence = data.getValue(GlycanProcedure.Sequence);
		if (sequence.contains("\n")) {
			sequence = sequence.replaceAll("(?:\\r\\n|\\n)", "\\\\n");
		}

		logger.debug("adding:" + data.getValue(Saccharide.PrimaryId) + " with >" + sequence + "< in "
				+ data.getValue(GlycanProcedure.Format) + " format.");
		glycoSequenceInsert.getSparqlEntity().setValue(Saccharide.PrimaryId, data.getValue(Saccharide.PrimaryId));
		glycoSequenceInsert.getSparqlEntity().setValue(GlycoSequence.Sequence, sequence);
		glycoSequenceInsert.getSparqlEntity().setValue(GlycoSequence.Format, DetectFormat.detect(sequence));

		SaccharideInsertSparql sis = new SaccharideInsertSparql();
		sis.setSparqlEntity(glycoSequenceInsert.getSparqlEntity());

		glycoSequenceInsert.getSparqlEntity().setValue(Saccharide.URI,
				SaccharideUtil.getURI(data.getValue(Saccharide.PrimaryId)));

		sparqlDAO.insert(glycoSequenceInsert);
	}

	@Override
	public ArrayList<SparqlEntity> findMotifs(String acc) throws SparqlException {
		motifSequenceSelectSparql.setOrderBy("?PrimaryId");
		List<SparqlEntity> motifList = sparqlDAO.query(motifSequenceSelectSparql);

		ArrayList<SparqlEntity> results = new ArrayList<SparqlEntity>();
		for (SparqlEntity motifSparqlEntity : motifList) {
			String motifSequence = motifSparqlEntity.getValue(GlycoSequence.Sequence);
			String motifId = motifSparqlEntity.getValue(GlycoSequence.AccessionNumber);
			SparqlEntity se = new SparqlEntity();

			se.setValue(GlycoSequence.Sequence, motifSequence);
			se.setValue(GlycoSequence.AccessionNumber, acc);
			se.setValue(SubstructureSearchSparql.LIMITID, "true");
			substructureSearchSparql.setSparqlEntity(se);
			List<SparqlEntity> listResult = sparqlDAO.query(substructureSearchSparql);
			logger.debug("checking motif:>" + motifId);
			logger.debug("listResult:>" + listResult);
			if (!listResult.isEmpty()) {
				logger.debug("adding!" + motifId);
				results.add(motifSparqlEntity);
			}
		}

		return results;
	}

	@Override
	public boolean checkExists(String id) throws SparqlException {
		SparqlEntity se = new SparqlEntity();

		se.setValue(Saccharide.PrimaryId, id);
		saccharideSelectSparql.setSparqlEntity(se);
		List<SparqlEntity> listResult = sparqlDAO.query(saccharideSelectSparql);
		return !(listResult.isEmpty());
	}

	@Override
	public List<SparqlEntity> substructureSearch(String sequence, String limit, String offset) throws SparqlException {

		// wurcs to sparql
		SparqlEntity se = new SparqlEntity();
		se.setValue(GlycoSequence.Sequence, sequence);
		SubstructureSearchSparql sss = substructureSearchSparql;
		sss.setLimit(limit);
		sss.setOffset(offset);
		sss.setSparqlEntity(se);

		return sparqlDAO.query(sss);
	}

	/**
	 * 
	 * This is for the case where a glycoct(_condensed) formatted GlycoSequence
	 * already exists. (data loaded from another database) If the wurcs already
	 * exists, that can be expected if the process is being rerun for some
	 * reason, returns the existing id.
	 * 
	 * Assuming the ResourceEntry, Contributor for the Saccharide is already
	 * input.
	 * 
	 * Conversion exceptions can occur, however unexpected. In these cases
	 * record the error message and place into rdfs label for the glycosequence.
	 * Will then have to exit clean as we cannot process anymore since no wurcs.
	 * 
	 * However the wurcs, mass, wurcsRDF, wurcsRDFMS etc still do not exist, add
	 * using addWurcs();
	 * 
	 * @see org.glycoinfo.rdf.service.GlycanProcedure#initialize()
	 */
	@Override
	public String initialize(String sequence, String id) throws SparqlException {
		SparqlEntity sparqlentity = new SparqlEntity();
		String errorMessage = null;

		// check if it doesn't exist.
		try {
			sparqlentity = searchBySequence(sequence);
			if (null != sparqlentity && sparqlentity.getValue(AccessionNumber) != null
					&& !sparqlentity.getValue(AccessionNumber).equals(NotRegistered)) {
				logger.debug(AlreadyRegistered + " as:>" + sparqlentity.getValue(AccessionNumber) + "<");
				return sparqlentity.getValue(AccessionNumber);
			}
			logger.debug("setting from sequence:>" + sparqlentity.getValue(GlycanProcedure.FromSequence)
					+ "< sequence:>" + sparqlentity.getValue(GlycanProcedure.Sequence) + "<");
			// setFromSequence(sparqlentity.getValue(GlycanProcedure.FromSequence));
			// setSequence(sparqlentity.getValue(GlycanProcedure.Sequence));
		} catch (ConvertException e) {
			logger.error("convert exception processing:>" + sequence + "<");
			if (e.getMessage() != null && e.getMessage().length() > 0)
				errorMessage = e.getMessage();
			else
				throw new SparqlException(errorMessage, e);
		}

		// logger.debug("registering wurcs:>" + getId() + "\nsequence:>" +
		// getSequence());

		// record the errorMessage
		sparqlentity.setValue(Saccharide.PrimaryId, id);
		if (null != errorMessage) {
			// don't have wurcs, just exit clean.
			glycoSequenceInsert.setGraph(wurcsRDFInsertSparql.getGraph());
			glycoSequenceInsert.getSparqlEntity().setValue(Saccharide.PrimaryId, id);
			glycoSequenceInsert.getSparqlEntity().setValue(GlycoSequence.Format, "wurcs");
			try {
				glycoSequenceInsert.getSparqlEntity().setValue(GlycoSequence.ErrorMessage,
						URLEncoder.encode(errorMessage, "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			glycoSequenceInsert.setGraph(wurcsRDFInsertSparql.getGraph());
			sparqlDAO.insert(glycoSequenceInsert);
		} else
			addWurcs(sparqlentity);
		return sparqlentity.getValue(Saccharide.PrimaryId);
	}

	@Override
	public String addResourceEntry(String accessionNumber, String name, String dbId) throws SparqlException {
		List<SparqlEntity> list = contributorProcedure.selectDatabaseByContributor(name);
		String uri = "http://rdf.glycoinfo.org/glycan/" + accessionNumber;

		for (SparqlEntity sparqlEntity : list) {
			sparqlEntity.setValue(Saccharide.URI, uri);
		}

		contributorProcedure.insertResourceEntry(list, dbId);
		return null;
	}
}