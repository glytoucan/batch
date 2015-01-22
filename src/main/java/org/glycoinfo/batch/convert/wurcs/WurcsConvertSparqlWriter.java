package org.glycoinfo.batch.convert.wurcs;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.glycoinfo.batch.convert.ConvertSparqlWriter;
import org.glycoinfo.rdf.InsertSparql;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.utils.TripleStoreConverter;

public class WurcsConvertSparqlWriter extends ConvertSparqlWriter implements
		TripleStoreConverter<WurcsConvertSparqlWriter> {

	String ident;
	String sequence;
	String format = "wurcs";
	String glycanSequence, glycanUri;

	public String getIdent() {
		return ident;
	}

	public void setIdent(String ident) {
		this.ident = ident;
	}

	public String getSequence() {
		return sequence;
	}

	public void setSequence(String sequence) {
		this.sequence = sequence;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	@Override
	public String getGlycanSequenceUri() {
		if (this.glycanSequence != null)
			return this.glycanSequence;
		else {
			return "<http://glycoinfo.org/rdf/glycan/wurcs/"
					+ getSequenceFormatted() + ">";
		}
	}

	@Override
	public void setGlycanSequenceUri(String glycanSequenceUri) {
		this.glycanSequence = glycanSequenceUri;
	}

	@Override
	public WurcsConvertSparqlWriter converter(SparqlEntity e) {
		WurcsConvertSparqlWriter converted = new WurcsConvertSparqlWriter();
		converted.setIdent(e.getValue("AccessionNumber"));
		converted.setGlycanUri(e.getValue("s"));
		converted.setSequence(e.getValue("Seq"));

		return converted;
	}

	@Override
	public String getOrderBy() {
		return null;
	}

	@Override
	public void setOrderBy(String orderByStatement) {
	}

	@Override
	public String getSequenceFormatted() {
		try {
			return URLEncoder.encode(getSequence(), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			logger.debug("UnsupportedEncodingException" + e.getMessage());
		}
		return getSequence();
	}

	@Override
	public void setSequenceFormatted(String sequence) {
	}
}