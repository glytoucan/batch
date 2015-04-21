package org.glycoinfo.batch;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.utils.SparqlEntityConverter;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;

public class SparqlListProcessor implements
		ItemProcessor<SparqlEntity, List<SparqlEntity>> {
	protected Log logger = LogFactory.getLog(getClass());

	boolean putall = true;

	public boolean isPutall() {
		return putall;
	}

	public void setPutall(boolean putall) {
		this.putall = putall;
	}

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

//	@Autowired(required = true)
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
	public List<SparqlEntity> process(final SparqlEntity sparqlEntity)
			throws Exception {
		logger.debug("PROCESSING:>" + sparqlEntity + "<");
		SelectSparql select = getSelectSparql();
		if (null != getConverter())
			select.setSparqlEntity(getConverter().converter(sparqlEntity));
		else
			select.setSparqlEntity(sparqlEntity);

		logger.debug("processor query:>" + select.getSparql() + "<");
		List<SparqlEntity> list = getSparqlDAO().query(select);
		if (putall) {
			ArrayList<SparqlEntity> newList = new ArrayList();
			for (SparqlEntity sparqlEntity2 : list) {
				sparqlEntity2.putAll(sparqlEntity);
				newList.add(sparqlEntity2);
			}
			return newList;
		} else
			return list;
	}

	public void setSparqlDAO(SparqlDAO dao) {
		this.dao = dao;
	}
}