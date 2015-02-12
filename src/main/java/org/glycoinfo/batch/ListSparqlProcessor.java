package org.glycoinfo.batch;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.utils.SparqlEntityConverter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

public class ListSparqlProcessor implements
		ItemProcessor<SparqlEntity, List<SparqlEntity>> {
	protected Log logger = LogFactory.getLog(getClass());

	SelectSparql sparql;
	
	/**
	 * 
	 * Select Sparql to retrieve the Motifs of the ItemReader.
	 * 
	 * @return
	 */
	SelectSparql getSelectSparql() {
		return sparql;
	}
	
	public void setSelectSparql(SelectSparql select) {
		this.sparql = select;
	}
	
	@Autowired(required=true)
	SparqlDAO dao;
	
	SparqlDAO getSparqlDAO() {
		return dao;
	}
	
	SparqlEntityConverter<SparqlEntity> converter;
	
	public SparqlEntityConverter<SparqlEntity> getConverter() {
		return converter;
	}

	public void setConverter(SparqlEntityConverter<SparqlEntity> converter) {
		this.converter = converter;
	}

	@Override
	public List<SparqlEntity> process(final SparqlEntity sparqlEntity) throws Exception {
		SelectSparql select = getSelectSparql();
		if (null != getConverter())
			select.setSparqlEntity(getConverter().converter(sparqlEntity));
		else
			select.setSparqlEntity(sparqlEntity);
		return getSparqlDAO().query(select);
	}
}