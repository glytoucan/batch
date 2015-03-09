package org.glycoinfo.mass;

import java.util.LinkedList;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.WURCSFramework.util.WURCSImporter;
import org.glycoinfo.WURCSFramework.util.mass.WURCSMassCalculator;
import org.glycoinfo.WURCSFramework.wurcs.RES;
import org.glycoinfo.WURCSFramework.wurcs.WURCSArray;
import org.glycoinfo.WURCSFramework.wurcs.WURCSFormatException;
import org.glycoinfo.conversion.GlyConvert;
import org.glycoinfo.mass.MassInsertSparql;
import org.glycoinfo.mass.MassSelectSparql;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;

public class MassSparqlProcessor implements
		ItemProcessor<SparqlEntity, SparqlEntity> {
	protected Log logger = LogFactory.getLog(getClass());
	
	@Autowired(required=true)
	GlyConvert glyConvert;

	public GlyConvert getGlyConvert() {
		return glyConvert;
	}

	public void setGlyConvert(GlyConvert glyConvert) {
		this.glyConvert = glyConvert;
	}

	@Override
	public SparqlEntity process(final SparqlEntity sparqlEntity) throws Exception {
		
		// get the sequence
		String sequence = sparqlEntity.getValue(MassSelectSparql.Sequence);
		
		// calculate the mass
		WURCSArray t_objWURCS = null;
		WURCSImporter t_objImporter = new WURCSImporter();

		try {
			t_objWURCS = t_objImporter.extractWURCSArray(sequence);
		} catch (WURCSFormatException e) {
			throw new SparqlException(e);
		}

		LinkedList<RES> testRESs = t_objWURCS.getRESs();
		double testMass = WURCSMassCalculator.calcMassWURCS(t_objWURCS);

		// return
		logger.debug("Mass of (" + sequence + ") is (" + testMass + ")");
		sparqlEntity.setValue(MassInsertSparql.Mass, Double.toString(testMass));

		return sparqlEntity;
	}
}