package org.glycoinfo.rdf.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.glycoinfo.WURCSFramework.util.WURCSException;
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
	
	public void setBatch(boolean batch);
	
	List<SparqlEntity> search(List<String> input) throws SparqlException;

	public String register(String sequence, String contributorId) throws SparqlException;

	public void deleteByAccessionNumber(String accessionNumber);
	
	public SparqlEntity searchByAccessionNumber(SparqlEntity accessionNumber) throws SparqlException;

	public SparqlEntity searchByAccessionNumber(String accessionNumber) throws SparqlException;

	public List<SparqlEntity> getGlycans(String offset, String limit) throws SparqlException;

	public List<SparqlEntity> substructureSearch(String sequence, String limit, String offset) throws SparqlException;

	public ArrayList<SparqlEntity> findMotifs(String wurcs) throws SparqlException;

	boolean checkExists(String id) throws SparqlException;

	SparqlEntity searchBySequence(String sequence) throws SparqlException,
			ConvertException;

	Map<String, String> register(List<String> inputs, String contributorId)
			throws SparqlException;

	void registerGlycoSequence(SparqlEntity data) throws SparqlException;

	String initialize(String sequence, String id) throws SparqlException;

	public String addResourceEntry(String accessionNumber, String name, String dbId) throws SparqlException;

	String convertToWurcs(String sequence) throws ConvertException;

	String validateWurcs(String sequence) throws WURCSException;
}