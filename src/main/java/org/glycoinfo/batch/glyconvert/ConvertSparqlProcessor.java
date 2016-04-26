package org.glycoinfo.batch.glyconvert;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.convert.GlyConvert;
import org.glycoinfo.convert.error.ConvertException;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.glycan.GlycoSequence;
import org.glycoinfo.rdf.glycan.wurcs.ConvertSelectSparql;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

public class ConvertSparqlProcessor implements
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
	public SparqlEntity process(final SparqlEntity sparqlEntity) throws SparqlException, ConvertException {
		
		// get the sequence
		String sequence = sparqlEntity.getValue(ConvertSelectSparql.Sequence);
		
		// convert the sequence
		GlyConvert converter = getGlyConvert();
		String convertedSeq = null;
		String errorMessage = null;
		try {
			convertedSeq = converter.convert(sequence);
		} catch (ConvertException e) {
			e.printStackTrace();
			logger.error("error processing:>" + sequence + "<");
			if (e.getMessage() != null && e.getMessage().length() > 0)
				errorMessage=e.getMessage();
			else
				throw e;
		}

		if (null != convertedSeq) {
			// return
			logger.debug("Converting (" + sequence + ") into (" + convertedSeq + ")");
			
			String encoded;
//			try {
//				encoded = URLEncoder.encode(convertedSeq, "UTF-8");
				encoded = convertedSeq;
//			} catch (UnsupportedEncodingException e) {
//				e.printStackTrace();
//				throw new ConvertException(e);
//			}
		
			logger.debug("Encoded (" + convertedSeq + ") into (" + encoded + ")");
			sparqlEntity.setValue(ConvertInsertSparql.ConvertedSequence, encoded);
		}

		if (null != errorMessage) {
			String encodedErrorMessage;
			try {
				encodedErrorMessage = URLEncoder.encode(errorMessage, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				throw new ConvertException(e);
			}

			sparqlEntity.setValue(GlycoSequence.ErrorMessage, encodedErrorMessage);
		}

		return sparqlEntity;
	}
}