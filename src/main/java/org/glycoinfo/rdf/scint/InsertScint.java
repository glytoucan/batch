package org.glycoinfo.rdf.scint;

import java.util.List;
import java.util.Set;

import org.glycoinfo.rdf.InsertSparqlBean;
import org.glycoinfo.rdf.SelectSparqlBean;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.schema.SchemaSparqlFormatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class InsertScint extends InsertSparqlBean {
	
	@Autowired
	@Qualifier(value = "ClassHandler")
	ClassHandler classHandler;
	
	public ClassHandler getClassHandler() {
		return classHandler;
	}

	public void setClassHandler(ClassHandler classHandler) {
		this.classHandler = classHandler;
		init();
	}

	public InsertScint() {
	}
	
	void init() {
		this.prefix = SchemaSparqlFormatter.getPrefixDefinition(getClassHandler()) + "\n";
		this.graph = "http://rdf.glytoucan.org/users";
//		List<String> domains = null;
//		try {
//			domains = classHandler.getDomains();
//		} catch (SparqlException e) {
//			this.insert = "error in getDomains";
//		}
	}

	@Override
	public void setSparqlEntity(SparqlEntity sparqlentity) {
		super.setSparqlEntity(sparqlentity);
		update();
	}
	
	public void update() {
		if (getSparqlEntity() != null) {
			this.insert="";
//			this.where = SchemaSparqlFormatter.getCommonClassWhere(classHandler) + " \n";
			Set<String> columns = getSparqlEntity().getColumns();
	
			for (String column : columns) {
				this.insert += SchemaSparqlFormatter.getInsert(classHandler, column, getSparqlEntity().getValue(column)) + " \n";
			}
		}
	}
	

	//	@Override
//	public String getPrefix() {
//		return SchemaSparqlFormatter.getPrefixDefinition(getClassHandler());
//	}

//	@Override
//	public String getSelect()  {
//		String select = "";
//		List<String> domains = null;
//		try {
//			domains = classHandler.getDomains();
//		} catch (SparqlException e) {
//			select = "error in getDomains";
//		}
//		for (String domain : domains) {
//			select += ("?" + SchemaSparqlFormatter.getDomainName(getClassHandler(), domain)) + " ";
//		}
//		return select;
//	}

//	@Override
//	public String getWhere() {
//		return SchemaSparqlFormatter.getCommonClassWhere(classHandler) + " \n";
//	}
}