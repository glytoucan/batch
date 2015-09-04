package org.glycoinfo.rdf.service;

import java.util.List;
import java.util.Map;

import org.glycoinfo.conversion.error.ConvertException;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.glycan.Saccharide;

public interface GlycanProcedure {
	public static final String Image = "image"; 
	public static final String CouldNotConvertHeader = "Failed Conversion:";
	public static final String NotRegistered = "not registered";
	public static final String CouldNotConvert = "could not convert";
	public static final String AlreadyRegistered = "already registered";
	public static final String AccessionNumber = Saccharide.PrimaryId;
	public static final String Sequence = "Sequence";
	public static final String FromSequence = "FromSequence";
	public static final String Format = "Format";
	public static final String Id = "Id";
	public static final String ResultSequence = "ResultSequence";

	

	public SparqlEntity searchBySequence() throws SparqlException, ConvertException;

	public String register(String sequence) throws SparqlException;
	
	public void setSequence(String sequence);
	
	public String getSequence();
	
	public String getSequenceResult();

	public String getId();
	
	public String getImage();

	public String getContributor();

	public void setContributor(String name);

	public Map<String, String> register(List<String> inputs) throws SparqlException;
	
	List<SparqlEntity> search(List<String> input) throws SparqlException;

	public String register() throws SparqlException;

	public void deleteByAccessionNumber(String accessionNumber);
	
	public SparqlEntity searchByAccessionNumber(String accessionNumber) throws SparqlException;

	public List<SparqlEntity> getGlycans() throws SparqlException;

	void registerGlycoSequence() throws SparqlException;

	public void setFormat(String value);

	public void setId(String value);

	public String register(String string, String string2) throws SparqlException;
}