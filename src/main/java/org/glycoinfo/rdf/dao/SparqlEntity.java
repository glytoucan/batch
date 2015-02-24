package org.glycoinfo.rdf.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.glycoinfo.rdf.utils.SparqlEntityValueConverter;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Class to hold information about Triplestore schema
 * 
 * @author aoki
 */
@JsonSerialize(using=SparqlEntityValueConverter.class)
public class SparqlEntity {
	List<String> columns = new ArrayList<String>();
	
	Map<String, String> data = new HashMap<String, String>();
	String graph;

	/**
	 * @return the subject
	 */
	public List<String> getColumns() {
		return columns;
	}

	/**
	 * @param columns
	 *            the subject to set
	 */
	public void setColumns(List<String> columns) {
		this.columns = columns;
	}
	
	public String getValue(String key) {
		return data.get(key);
	}

	public String setValue(String key, String value) {
		return data.put(key, value);
	}

	/**
	 * @return the graph
	 */
	public String getGraph() {
		return data.get("graph");
	}

	/**
	 * @param graph
	 *            the graph to set
	 */
	public void setGraph(String graph) {
		this.data.put("graph", graph);
	}
	
	public void putAll(SparqlEntity m) { 
		data.putAll(m.getData());
	}
	
	private Map getData() {
		return data;
	}

	@Override
	public String toString() {
		return "SchemaEntity [columns=" + columns + ", data=" + data
				+ ", graph=" + graph + "]";
	}	
}