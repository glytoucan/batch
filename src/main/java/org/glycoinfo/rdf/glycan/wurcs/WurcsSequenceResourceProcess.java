package org.glycoinfo.rdf.glycan.wurcs;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.WURCSFramework.util.WURCSException;
import org.glycoinfo.WURCSFramework.util.WURCSFactory;
import org.glycoinfo.WURCSFramework.wurcs.array.WURCSArray;
import org.glycoinfo.convert.GlyConvertDetect;
import org.glycoinfo.convert.error.ConvertException;
import org.glycoinfo.rdf.InsertSparql;
import org.glycoinfo.rdf.ResourceProcessException;
import org.glycoinfo.rdf.ResourceProcessParent;
import org.glycoinfo.rdf.ResourceProcessResult;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.glycan.GlycoSequenceResourceProcess;
import org.glycoinfo.rdf.glycan.ResourceEntry;
import org.glycoinfo.rdf.glycan.Saccharide;
import org.glycoinfo.rdf.service.impl.GlycanProcedure;
import org.glycoinfo.rdf.utils.NumberGenerator;
import org.glytoucan.core.Status;
import org.springframework.beans.factory.annotation.Autowired;

public class WurcsSequenceResourceProcess extends ResourceProcessParent implements GlycoSequenceResourceProcess {

	private static final Log logger = LogFactory.getLog(WurcsSequenceResourceProcess.class);

	@Autowired
	GlyConvertDetect glyConvertDetect;

	@Autowired
	GlycanProcedure glycanProcedure;

	@Autowired
	InsertSparql saccharideInsertSparql;

	@Autowired
	InsertSparql resourceEntryInsertSparql;

	public ResourceProcessResult processGlycoSequence(String sequence, String contributorId)
			throws ResourceProcessException {
		SparqlEntity sparqlentity = new SparqlEntity();
		String accessionNumber = null;

		if (StringUtils.isBlank(contributorId)) {
			throw new ResourceProcessException(
					new ResourceProcessResult("Contributor id cannot be blank", Status.ERROR));
		}

		try {
			// check if it doesn't exist.
			sparqlentity = glycanProcedure.searchBySequence(sequence);
			if (null != sparqlentity && sparqlentity.getValue(GlycanProcedure.AccessionNumber) != null
					&& !sparqlentity.getValue(GlycanProcedure.AccessionNumber).equals(GlycanProcedure.NotRegistered)) {
				return new ResourceProcessResult(GlycanProcedure.AlreadyRegistered + " as:>"
						+ sparqlentity.getValue(GlycanProcedure.AccessionNumber) + "<", Status.WARNING);
			}
		} catch (ConvertException e) {
			logger.error("convert exception processing:>" + sequence + "<");
			String errorMessage = null;
			if (e.getMessage() != null && e.getMessage().length() > 0)
				errorMessage = e.getMessage();
			throw new ResourceProcessException(new ResourceProcessResult(errorMessage, Status.ERROR));
		} catch (SparqlException e) {
			logger.error("Sparql exception processing:>" + sequence + "<");
			String errorMessage = null;
			if (e.getMessage() != null && e.getMessage().length() > 0)
				errorMessage = e.getMessage();
			throw new ResourceProcessException(new ResourceProcessResult(errorMessage, Status.ERROR));
		}

		logger.debug("registering wurcs sequence:>" + sparqlentity.getValue(GlycanProcedure.Sequence));

		accessionNumber = "G" + NumberGenerator.generateRandomString(7);

		try {
			
			// search for the glycosequence using accession number to determine if randomly generated one is used already.
			SparqlEntity result = glycanProcedure.searchByAccessionNumber(accessionNumber);
			while (result != null && StringUtils.isNotBlank(result.getValue(Saccharide.PrimaryId))) {
				logger.debug("rerolling... " + result.getValue(Saccharide.PrimaryId));

				// loop until new one is generated.
				result = glycanProcedure.searchByAccessionNumber(accessionNumber);
			}
			logger.debug("setting accession#:>" + accessionNumber + "<");

			sparqlentity.setValue(Saccharide.PrimaryId, accessionNumber);

			// set details into sparql entity for the Saccharide class template
			saccharideInsertSparql.setSparqlEntity(sparqlentity);

			// contributorProcedure.setId(contributorId);
			// String id = contributorProcedure.searchContributor(userin);

			// Resource Entry class template
			resourceEntryInsertSparql.getSparqlEntity().setValue(Saccharide.PrimaryId, accessionNumber);
			resourceEntryInsertSparql.getSparqlEntity().setValue(ResourceEntry.Identifier, accessionNumber);
			resourceEntryInsertSparql.getSparqlEntity().setValue(ResourceEntry.Database,
					ResourceEntry.Database_Glytoucan);

			resourceEntryInsertSparql.getSparqlEntity().setValue(ResourceEntry.ContributorId, contributorId);
			resourceEntryInsertSparql.getSparqlEntity().setValue(ResourceEntry.DataSubmittedDate, new Date());
			getSparqlDAO().insert(saccharideInsertSparql);
			getSparqlDAO().insert(resourceEntryInsertSparql);

			// add WURCS RDF from wurcs RDF library.  this will add the WURCS GlycoSequence class.
			glycanProcedure.addWurcs(sparqlentity);

		} catch (SparqlException e) {
			logger.error("Sparql exception processing:>" + sequence + "<");
			String errorMessage = null;
			if (e.getMessage() != null && e.getMessage().length() > 0)
				errorMessage = e.getMessage();
			throw new ResourceProcessException(new ResourceProcessResult(errorMessage, Status.ERROR));
		}
		
		return new ResourceProcessResult(accessionNumber, Status.SUCCESS);
	}

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
}