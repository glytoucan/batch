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
import org.springframework.beans.factory.annotation.Qualifier;

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

	@Autowired
  @Qualifier("itemProcessorSelectSparql")
	SelectSparql sparql;

	SelectSparql getSelectSparql() {
		return sparql;
	}

	public void setSelectSparql(SelectSparql select) {
		this.sparql = select;
	}

	@Autowired(required = true)
	SparqlDAO dao;

	SparqlDAO getSparqlDAO() {
		return dao;
	}

	SparqlEntityConverter<SparqlEntity> converter;
	SparqlEntityConverter<SparqlEntity> postConverter;

	public SparqlEntityConverter<SparqlEntity> getConverter() {
		return converter;
	}

	public void setConverter(SparqlEntityConverter<SparqlEntity> converter) {
		this.converter = converter;
	}
	
	private SparqlEntityConverter<SparqlEntity> getPostConverter() {
		return postConverter;
	}
	
	public void setPostConverter(SparqlEntityConverter<SparqlEntity> converter) {
		this.postConverter = converter;
	}
	
	@Override
	public List<SparqlEntity> process(final SparqlEntity sparqlEntity)
			throws Exception {
		logger.debug("PROCESSING:>" + sparqlEntity + "<");
		SelectSparql select = getSelectSparql();
		if (null != getConverter())
			select.setSparqlEntity(getConverter().convert(sparqlEntity));
		else
			select.setSparqlEntity(sparqlEntity);

		logger.debug("processor query:>" + select.getSparql() + "<");
		List<SparqlEntity> list = getSparqlDAO().query(select);
		
		if (null != getPostConverter()) {
			List<SparqlEntity> postList = new ArrayList<SparqlEntity>();
			for (SparqlEntity postSE : list) {
				postList.add(getPostConverter().convert(postSE));
			}
			list = postList;
		}
		
		if (putall) {
			ArrayList<SparqlEntity> newList = new ArrayList<SparqlEntity>();
			for (SparqlEntity sparqlEntity2 : list) {
				sparqlEntity2.putAll(sparqlEntity);
				newList.add(sparqlEntity2);
			}
			logger.debug("passing onto insert:>" + newList + "<");
			return newList;
		} else
			return list;
	}

	public void setSparqlDAO(SparqlDAO dao) {
		this.dao = dao;
	}
}