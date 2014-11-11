package org.glycoinfo.batch;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.ts.dao.SchemaDAO;
import org.glycoinfo.ts.dao.SchemaEntity;
import org.glycoinfo.ts.utils.TripleStoreConverter;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.data.AbstractPaginatedDataItemReader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.neo4j.conversion.ResultConverter;
import org.springframework.data.neo4j.mapping.MappingPolicy;
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

public class TripleStoreItemReader<T> extends
		AbstractPaginatedDataItemReader<T> implements InitializingBean {

	protected Log logger = LogFactory.getLog(getClass());

	@Autowired
	private SchemaDAO schemaDAO;

	private String prefix;
	private String select;
	private String where;
	private String orderBy;
	private String from;

	private Class<T> targetType;
	
	private TripleStoreConverter<T> converter;

	private Map<String, Object> parameterValues;

	public TripleStoreItemReader() {
		setName(ClassUtils.getShortName(TripleStoreItemReader.class));
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	/**
	 * A list of properties to order the results by. This is required so that
	 * subsequent page requests pull back the segment of results correctly.
	 * ORDER BY is prepended to the statement provided and should <em>not</em>
	 * be included.
	 *
	 * @param orderByStatement
	 *            order by fragment of the cypher query.
	 */
	public void setOrderBy(String orderByStatement) {
		this.orderBy = orderByStatement;
	}

	
	
	public SchemaDAO getSchemaDAO() {
		return schemaDAO;
	}

	
	public void setSchemaDAO(SchemaDAO schemaDAO) {
		this.schemaDAO = schemaDAO;
	}

	public String getSelect() {
		return select;
	}

	public void setSelect(String select) {
		this.select = select;
	}

	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}
	
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	@Override
	protected Iterator<T> doPageRead() {
		logger.debug("generated query:>" + generateLimitQuery());
		List<SchemaEntity> queryResults = schemaDAO.query(generateLimitQuery());
		ArrayList<T> result = new ArrayList<T>();
		if (queryResults != null) {
			for (SchemaEntity schemaEntity : queryResults) {
				result.add(getConverter().converter(schemaEntity));
			}
			return result.iterator();
		} else {
			return new ArrayList<T>().iterator();
		}
	}

	private String generateLimitQuery() {
		StringBuilder query = new StringBuilder();

		query.append(prefix);
		query.append(select);
		query.append(from);
//		query.append(matchStatement != null ? " MATCH " + matchStatement : "");
		query.append(where != null ? " WHERE " + where : "");
//		query.append(" RETURN ").append(returnStatement);
		query.append(orderBy != null ? orderBy : "");
		query.append(" OFFSET " + (pageSize * page));
		query.append(" LIMIT " + pageSize);

		String resultingQuery = query.toString();

		if (logger.isDebugEnabled()) {
			logger.debug(resultingQuery);
		}

		return resultingQuery;
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
		Assert.state(from != null, "The from is required");
		Assert.state(StringUtils.hasText(select),
				"A SELECT is required");
//		Assert.state(StringUtils.hasText(orderBy),
//				"A ORDER BY statement is required");
	}

	public TripleStoreConverter<T> getConverter() {
		return converter;
	}

	public void setConverter(TripleStoreConverter<T> converter) {
		this.converter = converter;
	}
}
