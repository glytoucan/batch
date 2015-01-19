package org.glycoinfo.batch.convert.wurcs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.conversion.GlyConvert;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import org.springframework.test.context.ContextConfiguration;

public class WurcsConvertTripleProcessor implements
		ItemProcessor<WurcsConvertTriple, WurcsConvertTriple> {
	protected Log logger = LogFactory.getLog(getClass());
	
	@Autowired(required=true)
	@Qualifier("GlycoctToWurcs")
	GlyConvert glyConvert;

	public GlyConvert getGlyConvert() {
		return glyConvert;
	}

	public void setGlyConvert(GlyConvert glyConvert) {
		this.glyConvert = glyConvert;
	}

	@Override
	public WurcsConvertTriple process(final WurcsConvertTriple triple) throws Exception {
		WurcsConvertTriple transformedTriple = new WurcsConvertTriple();
		getGlyConvert().setFrom(triple.getSequence());
		transformedTriple.setIdent(triple.getIdent());
		transformedTriple.setSequence(getGlyConvert().convert());

		logger.debug("Converting (" + triple + ") into (" + transformedTriple + ")");

		return transformedTriple;
	}
}