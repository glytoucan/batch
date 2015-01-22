package org.glycoinfo.batch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.rdf.InsertSparql;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.utils.TripleStoreConverter;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.AbstractPaginatedDataItemReader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

/**
 * <p>
 * Restartable {@link ItemReader} that reads objects from the triple store via a paging technique.
 * </p>
 *
 * <p>
 * It executes queries built from the statement fragments provided to
 * retrieve the requested data. The query is executed using paged requests of a
 * size specified in {@link #setPageSize(int)}. Additional pages are requested
 * as needed when the {@link #read()} method is called. On restart, the reader
 * will begin again at the same number item it left off at.
 * </p>
 *
 * <p>
 * Performance is dependent on your triplestore configuration (embedded or remote) as
 * well as page size. Setting a fairly large page size and using a commit
 * interval that matches the page size should provide better performance.
 * </p>
 *
 * <p>
 * This implementation is thread-safe between calls to
 * {@link #open(org.springframework.batch.item.ExecutionContext)}, however you
 * should set <code>saveState=false</code> if used in a multi-threaded
 * environment (no restart available).
 * </p>
 *
 SELECT DISTINCT ?s ?AccessionNumber ?Seq ?type
WHERE {
?s a glycan:saccharide .
?s glytoucan:has_primary_id ?AccessionNumber .
?s glycan:has_glycosequence ?gseq .
?gseq glycan:has_sequence ?Seq .
?gseq glycan:in_carbohydrate_format glycan:carbohydrate_format_kcf
}

 *
 */

public class SparqlItemReader<T extends InsertSparql> extends
		AbstractPaginatedDataItemReader<T> implements InitializingBean {

	protected Log logger = LogFactory.getLog(getClass());

	@Autowired
	private SparqlDAO schemaDAO;

	private T readSparql;
	
	private TripleStoreConverter<T> converter;

	public SparqlItemReader() {
		setName(ClassUtils.getShortName(SparqlItemReader.class));
	}

	public void setSparqlReader(T read) {
		this.readSparql = read;
	}

	public T getSparqlReader() {
		return this.readSparql;
	}
	
	public SparqlDAO getSchemaDAO() {
		return schemaDAO;
	}

	public void setSchemaDAO(SparqlDAO schemaDAO) {
		this.schemaDAO = schemaDAO;
	}

	@Override
	protected Iterator<T> doPageRead() {
		String q = getSparqlReader().getSparql();

		StringBuffer query = new StringBuffer(q);
		query.append(" OFFSET " + (pageSize * page));
		query.append(" LIMIT " + pageSize);
		logger.debug("generated query:>" + query);

		List<SparqlEntity> queryResults = schemaDAO.query(query.toString());
		ArrayList<T> result = new ArrayList<T>();
		if (queryResults != null) {
			for (SparqlEntity schemaEntity : queryResults) {
				logger.info("converting:>" + schemaEntity);
				T converted = getConverter().converter(schemaEntity);
				logger.info("converted:>" + converted.getIdent());
				result.add(converted);
			}
			return result.iterator();
		} else {
			return new ArrayList<T>().iterator();
		}
	}


	/**
	 * Checks mandatory properties
	 *
	 * @see InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.state(schemaDAO != null,
				"A DAO is required");
	}

	public TripleStoreConverter<T> getConverter() {
		return converter;
	}

	public void setConverter(TripleStoreConverter<T> converter) {
		this.converter = converter;
	}
}