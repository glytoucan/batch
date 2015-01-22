package org.glycoinfo.batch.convert.kcf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.conversion.GlyConvert;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

public class ConvertSparqlProcessor implements
		ItemProcessor<KcfConvertSparql, KcfConvertSparql> {
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
	public KcfConvertSparql process(final KcfConvertSparql triple) throws Exception {
		KcfConvertSparql transformedTriple = new KcfConvertSparql();
		getGlyConvert().setFrom(triple.getSequence());
		transformedTriple.setIdent(triple.getIdent());
		transformedTriple.setSequence(getGlyConvert().convert());

		logger.debug("Converting (" + triple + ") into (" + transformedTriple + ")");

		return transformedTriple;
	}
}