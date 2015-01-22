package org.glycoinfo.rdf.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

import org.glycoinfo.rdf.utils.SchemaEntityValueConverter;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
//import com.wordnik.swagger.annotations.ApiModel;

/**
 * Class to hold information about Triplestore schema
 * 
 * @author aoki
 *
 */

//@ApiModel(value = "SchemaEntity", description = "Schema query results")
@JsonSerialize(using=SchemaEntityValueConverter.class)
public class SparqlEntity {
	List<String> columns = new ArrayList<String>();
	
	Map<String, String> data = new HashMap<String, String>();
	String graph;
	String s;
	String p;
	String o;

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
	
	public String getS() {
		return data.get("s");
	}

	public void setS(String s) {
		this.data.put("s", s);
	}

	@Override
	public String toString() {
		return "SchemaEntity [columns=" + columns + ", data=" + data
				+ ", graph=" + graph + ", s=" + s + ", p=" + p + ", o=" + o
				+ "]";
	}

//	public String getP() {
//		return data.get("p");
//	}
//
//	public void setP(String p) {
//		this.data.put("p", p);
//	}
//
//	public String getO() {
//		return data.get("o");
//	}
//
//	public void setO(String o) {
//		this.data.put("o", o);
//	}
	
	
}