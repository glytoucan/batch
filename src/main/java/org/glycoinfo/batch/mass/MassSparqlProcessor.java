package org.glycoinfo.batch.mass;

import java.math.BigDecimal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.WURCSFramework.util.array.WURCSFormatException;
import org.glycoinfo.WURCSFramework.util.array.WURCSImporter;
import org.glycoinfo.WURCSFramework.util.array.mass.WURCSMassCalculator;
import org.glycoinfo.WURCSFramework.util.array.mass.WURCSMassException;
import org.glycoinfo.WURCSFramework.wurcs.array.WURCSArray;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.glycan.mass.MassInsertSparql;
import org.glycoinfo.rdf.glycan.mass.MassSelectSparql;
import org.glycoinfo.rdf.service.GlycanProcedure;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

public class MassSparqlProcessor implements
		ItemProcessor<SparqlEntity, SparqlEntity> {
	
	@Autowired
	GlycanProcedure glycanProcedure;
	
	protected Log logger = LogFactory.getLog(getClass());

	@Override
	public SparqlEntity process(SparqlEntity sparqlEntity)
			throws SparqlException, WURCSMassException {

		// get the sequence
		String sequence = sparqlEntity.getValue(MassSelectSparql.Sequence);

		// calculate the mass
		WURCSArray t_objWURCS = null;
		WURCSImporter t_objImporter = new WURCSImporter();

		logger.debug("processing:>" + sequence + "<");
		// check for inconvertible glycoct
		BigDecimal testMass = new BigDecimal(0);
		boolean cancalculate = true;
		
		if (null != sequence && sequence.length() > 0) {
				sparqlEntity = glycanProcedure.calculateMass(sequence);
		} else  {
			testMass = new BigDecimal(-4); // glycoct could not be converted to wurcs
			sparqlEntity.setValue(MassInsertSparql.MassLabel, "cannot calculate: no sequence");
		}

		return sparqlEntity;
	}
}