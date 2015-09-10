package org.glycoinfo.batch.mass;

import java.math.BigDecimal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.WURCSFramework.util.array.WURCSFormatException;
import org.glycoinfo.WURCSFramework.util.array.WURCSImporter;
import org.glycoinfo.WURCSFramework.util.array.mass.WURCSMassCalculator;
import org.glycoinfo.WURCSFramework.util.array.mass.WURCSMassException;
import org.glycoinfo.WURCSFramework.wurcs.array.WURCSArray;
import org.glycoinfo.mass.MassInsertSparql;
import org.glycoinfo.mass.MassSelectSparql;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.springframework.batch.item.ItemProcessor;

public class MassSparqlProcessor implements
		ItemProcessor<SparqlEntity, SparqlEntity> {
	protected Log logger = LogFactory.getLog(getClass());

	@Override
	public SparqlEntity process(final SparqlEntity sparqlEntity)
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
			try {
//				String decodedSequence = URLDecoder.decode(sequence, "UTF-8");
//				logger.debug("processing decoded:>" + decodedSequence + "<");
				
				t_objWURCS = t_objImporter.extractWURCSArray(sequence);
			} catch (WURCSFormatException e) {
				sparqlEntity.setValue(MassInsertSparql.MassLabel, "cannot calculate invalid format");
				cancalculate = false;
//				throw new SparqlException(e);
//			} catch (UnsupportedEncodingException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
			}
			if (cancalculate) {

				try {
					testMass = WURCSMassCalculator.calcMassWURCS(t_objWURCS);
				} catch (WURCSMassException e) {
					if (e.getMessage().contains("repeating unit")) {
						testMass = new BigDecimal(-1);
						sparqlEntity.setValue(MassInsertSparql.MassLabel, "cannot calculate repeating units");
					}
					else if (e.getMessage().contains("unknown carbon length")) {
						testMass = new BigDecimal(-2);
						sparqlEntity.setValue(MassInsertSparql.MassLabel, "cannot calculate unknown carbon length");
					}
					else if (e.getMessage().contains(
							"Cannot calculate linkage with probability")) {
						testMass = new BigDecimal(-3);
						sparqlEntity.setValue(MassInsertSparql.MassLabel, "cannot calculate linkages with probability");
					}
					else
						throw e;
				}
			}
		} else  {
			testMass = new BigDecimal(-4); // glycoct could not be converted to wurcs
			sparqlEntity.setValue(MassInsertSparql.MassLabel, "cannot calculate: no sequence");
		}

		// return
		logger.debug("Mass of (" + sequence + ") is (" + testMass + ")");
		if (testMass.intValue() > -1) {
			sparqlEntity.setValue(MassInsertSparql.Mass, testMass);
			sparqlEntity.setValue(MassInsertSparql.MassLabel, testMass);
		}

		return sparqlEntity;
	}
}