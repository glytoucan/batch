package org.glycoinfo.batch;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.conversion.GlyConvert;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

public class ConvertTripleProcessor implements
		ItemProcessor<ConvertTriple, ConvertTriple> {
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
	public ConvertTriple process(final ConvertTriple triple) throws Exception {
		ConvertTriple transformedTriple = new ConvertTriple();
		getGlyConvert().setFrom(triple.getSequence());
		transformedTriple.setIdent(triple.getIdent());
		transformedTriple.setSequence(getGlyConvert().convert());

		logger.debug("Converting (" + triple + ") into (" + transformedTriple + ")");

		return transformedTriple;
	}
}