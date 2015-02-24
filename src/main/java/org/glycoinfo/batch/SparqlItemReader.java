package org.glycoinfo.batch;

import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.rdf.SelectSparql;
import org.glycoinfo.rdf.SelectSparqlBean;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.AbstractPaginatedDataItemReader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

/**
 * <p>
 * Restartable {@link ItemReader} that reads objects from the triple store via a
 * paging technique.
 * </p>
 *
 * <p>
 * It executes queries built from the statement fragments provided to retrieve
 * the requested data. The query is executed using paged requests of a size
 * specified in {@link #setPageSize(int)}. Additional pages are requested as
 * needed when the {@link #read()} method is called. On restart, the reader will
 * begin again at the same number item it left off at.
 * </p>
 *
 * <p>
 * Performance is dependent on your triplestore configuration (embedded or
 * remote) as well as page size. Setting a fairly large page size and using a
 * commit interval that matches the page size should provide better performance.
 * </p>
 *
 * <p>
 * This implementation is thread-safe between calls to
 * {@link #open(org.springframework.batch.item.ExecutionContext)}, however you
 * should set <code>saveState=false</code> if used in a multi-threaded
 * environment (no restart available).
 * </p>
 *
 * SELECT DISTINCT ?s ?AccessionNumber ?Seq ?type WHERE { ?s a glycan:saccharide
 * . ?s glytoucan:has_primary_id ?AccessionNumber . ?s glycan:has_glycosequence
 * ?gseq . ?gseq glycan:has_sequence ?Seq . ?gseq glycan:in_carbohydrate_format
 * glycan:carbohydrate_format_kcf }
 * 
 *
 */

public class SparqlItemReader<T extends SparqlEntity> extends
		AbstractPaginatedDataItemReader<SparqlEntity> implements
		InitializingBean {

	protected Log logger = LogFactory.getLog(getClass());

	@Autowired
	private SparqlDAO schemaDAO;

	@Autowired
	private SelectSparql selectSparql;

	public SparqlItemReader() {
		setName(ClassUtils.getShortName(SparqlItemReader.class));
	}

	public void setSelectSparql(SelectSparql read) {
		this.selectSparql = read;
	}

	public SelectSparql getSelectSparql() {
		return selectSparql;
	}

	public SparqlDAO getSchemaDAO() {
		return schemaDAO;
	}

	public void setSchemaDAO(SparqlDAO schemaDAO) {
		this.schemaDAO = schemaDAO;
	}

	@Override
	protected Iterator<SparqlEntity> doPageRead() {

		SelectSparql selectSparql = getSelectSparql();
		selectSparql.setOffset(Integer.toString(pageSize * page));
		selectSparql.setLimit(Integer.toString(pageSize));
		String q;
		List<org.glycoinfo.rdf.dao.SparqlEntity> queryResults;
		try {
			q = selectSparql.getSparql();

			StringBuffer query = new StringBuffer(q);
			logger.debug("generated query:>" + query);

			queryResults = schemaDAO.query(selectSparql);
		} catch (SparqlException e) {
			logger.warn("sparql exception:>" + e.getMessage());
			e.printStackTrace();
			return null;
		}
		return queryResults.iterator();
	}

	/**
	 * Checks mandatory properties
	 *
	 * @see InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.state(schemaDAO != null, "A DAO is required");
	}
}