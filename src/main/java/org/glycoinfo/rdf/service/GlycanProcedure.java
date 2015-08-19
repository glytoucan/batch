package org.glycoinfo.rdf.service;

import java.util.List;

import org.glycoinfo.conversion.error.ConvertException;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlEntity;

public interface GlycanProcedure {
	public static final String Image = "image"; 
	public static final String CouldNotConvertHeader = "Failed Conversion:";
	public static final String NotRegistered = "not registered";
	public static final String CouldNotConvert = "could not convert";	

	public SparqlEntity search() throws SparqlException, ConvertException;
	public void register(String sequence) throws SparqlException;
	
	public void setSequence(String sequence);
	
	public String getSequence();
	
	public String getSequenceResult();

	public String getId();
	
	public String getImage();
	
	public void register(List<String> inputs) throws SparqlException;
	
	public void check(List<String> inputs);

	List<SparqlEntity> search(List<String> input) throws SparqlException;
}