package org.glycoinfo.batch.glyconvert;

import org.glycoinfo.conversion.GlyConvert;
import org.glycoinfo.rdf.InsertSparqlBean;
import org.glycoinfo.rdf.glycan.GlycoSequence;
import org.glycoinfo.rdf.glycan.GlycoSequenceInsertSparql;
import org.glycoinfo.rdf.glycan.Saccharide;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;

/**
 * 
 * An abstract class used to specify the insert command for when a
 * GlycanSequence is to be inserted after conversion. For example: glycoct
 * converted into wurcs.
 * 
 * @author aoki
 *
 */
public class GlycomeDBConvertInsertSparql extends ConvertInsertSparql implements
		GlyConvertSparql, InitializingBean {
	
	@Autowired
	GlyConvert glyConvert;

	public static String ConvertedSequence = "ConvertedSequence";
	
	public GlycomeDBConvertInsertSparql() {
		super();
	}
	
	@Override
	public String getGlycanSequenceUri() {
		getSparqlEntity().setValue(GlycoSequence.Format, getGlyConvert().getToFormat());
		return "<http://rdf.glycome-db.org/glycan/"
				+ getSparqlEntity().getValue(Saccharide.PrimaryId)
				+ "/"
				+ getSparqlEntity()
						.getValue(GlycoSequence.Format) + "/2.0>";
	}

	@Override
	public String getFormat() {
		getSparqlEntity().setValue(GlycoSequence.Format, getGlyConvert().getToFormat());
		return super.getFormat();
	}

	@Override
	public String getGraph() {
		return getGraphBase() + "/" + getGlyConvert().getToFormat();
	}
	
	@Override
	public String getInsert() {
		getSparqlEntity().setValue(GlycoSequence.Sequence, getSparqlEntity().getValue(GlycomeDBConvertInsertSparql.ConvertedSequence));
		return super.getInsert();
	}

	public String getSequence() {
		String sequence = getGlyConvert().getToSequence();
		if (null == sequence)
			sequence = getSparqlEntity().getValue(ConvertedSequence);
		return sequence;
	}

	// TODO: put in subclass
	public String getFailInsert() {
		String rdf = getInsert();
		return rdf += getGlycanSequenceUri() + " rdfs:label \" ERROR IN "
				+ getGlyConvert().getFromFormat() + " to "
				+ getGlyConvert().getToFormat()
				+ " CONVERSION\"^^xsd:string .\n";
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.state(getGraphBase() != null, "A graphbase is required");
		Assert.state(getGlyConvert() != null,
				"A conversion is required - is one configured for autowire?");
	}

	public GlyConvert getGlyConvert() {
		return glyConvert;
	}

	public void setGlyConvert(GlyConvert glyconvert) {
		this.glyConvert = glyconvert;
	}
}