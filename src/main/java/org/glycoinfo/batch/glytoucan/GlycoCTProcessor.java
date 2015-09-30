package org.glycoinfo.batch.glytoucan;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.glycan.GlycoSequence;
import org.glycoinfo.rdf.service.GlycanProcedure;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

public class GlycoCTProcessor implements
		ItemProcessor<SparqlEntity, SparqlEntity> {
	protected Log logger = LogFactory.getLog(getClass());

	@Autowired
	GlycanProcedure glycanProcedure;
	
	@Override
	public SparqlEntity process(final SparqlEntity sparqlEntity) throws SparqlException {
		// get the sequence
		String sequence = sparqlEntity.getValue(GlycoSequence.Sequence);
		String id = sparqlEntity.getValue(GlycoSequence.AccessionNumber);
		glycanProcedure.setSequence(sequence);
		glycanProcedure.setId(id);
		glycanProcedure.setBatch(true);
		try {
			glycanProcedure.register();
		} catch (SparqlException e) {
			e.printStackTrace();
			logger.debug("FAILURE:>" + sequence + "<");
			throw e;
		}
		
		
		return new SparqlEntity();
	}
}