package org.glycoinfo.rdf.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.utils.SparqlEntityValueConverter;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;

/**
 * Class to hold information about Triplestore schema
 * 
 * @author aoki
 */
@JsonSerialize(using=SparqlEntityValueConverter.class)
public class SparqlEntity {
	Map<String, Object> data = new HashMap<String, Object>();
	String graph;

	public SparqlEntity() {
		super();
	}
	
	public SparqlEntity(Object primary) {
		setValue(SelectSparql.PRIMARY_KEY, primary);
	}
	
	/**
	 * @return the subject
	 */
	public Set<String> getColumns() {
		return data.keySet();
	}

	public Object getObjectValue(String key) {
		return data.get(key);
	}

	public String getValue(String key) {
		if (null != data.get(key))
			return data.get(key).toString();
		return null;
	}
	
	public Object setValue(String key, String value) {
		return data.put(key, value);
	}
	
	public Object setValue(String key, Object value) {
		return data.put(key, value);
	}

	/**
	 * @return the graph
	 */
	public String getGraph() {
		return data.get("graph").toString();
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
	
	public void putAll(Map<String, ?> m) { 
		data.putAll(m);
	}
	
	public Object remove(String key) {
		return data.remove(key);
	}
	
	private Map<String, Object> getData() {
		return data;
	}

	@Override
	public String toString() {
		return "SchemaEntity [columns=" + getColumns() + ", data=" + data
				+ ", graph=" + graph + "]";
	}	
}