package org.glycoinfo.batch;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.rdf.InsertSparql;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * A {@link ItemWriter} implementation that writes to an RDF database using {@link SparqlBeans}.
 * </p>
 *
 */
public class SparqlListWriter<T extends List<SparqlEntity>> implements
		ItemWriter<List<SparqlEntity>>, InitializingBean {

	protected static final Log logger = LogFactory
			.getLog(SparqlListWriter.class);

	@Autowired
	private SparqlDAO dao;

	@Autowired
	private InsertSparql insertSparql;

	@Autowired
	private InsertSparql insertFailSparql;

	public InsertSparql getInsertSparql() {
		return insertSparql;
	}

	public void setInsertSparql(InsertSparql insertSparql) {
		this.insertSparql = insertSparql;
	}

	public InsertSparql getInsertFailSparql() {
		return insertSparql;
	}

	public void setInsertFailSparql(InsertSparql insertSparql) {
		this.insertSparql = insertSparql;
	}

	/**
	 * Checks mandatory properties
	 *
	 * @see InitializingBean#afterPropertiesSet()
	 */
	@Override
	public void afterPropertiesSet() throws Exception {
		Assert.state(dao != null, "A dao is required");
	}

	/**
	 * Write all items to the data store.
	 *
	 * @see org.springframework.batch.item.ItemWriter#write(java.util.List)
	 */
	@Override
	public void write(List<? extends List<SparqlEntity>> items) throws Exception {
		if (!CollectionUtils.isEmpty(items)) {
			doWrite(items);
		}
	}

	/**
	 * Performs the actual write using the template. This can be overriden by a
	 * subclass if necessary.
	 *
	 * @param items
	 *            the list of items to be persisted.
	 * @throws SQLException
	 * @throws SparqlException 
	 */
	protected void doWrite(List<? extends List<SparqlEntity>> items)
			throws SQLException, SparqlException {
		for (List<SparqlEntity> t : items) {
			logger.debug(t);

			for (SparqlEntity sparqlEntity : t) {
				InsertSparql insertSparql = getInsertSparql();
				try {
					insertSparql.setSparqlEntity(sparqlEntity);

					if (!(insertSparql.getSparql().trim().length() == 0)) {
						logger.debug("inserting >" + insertSparql.getSparql()
								+ "< into " + insertSparql.getGraph() + "<");
						dao.insert(insertSparql);
					}
				} catch (SparqlException e) {
					e.printStackTrace();
					InsertSparql fail = getInsertFailSparql();
					if (null != fail) {
						fail.setSparqlEntity(sparqlEntity);
						logger.debug("inserting " + fail.getSparql() + " into "
								+ sparqlEntity.getGraph() + "<");
						try {
							dao.insert(getInsertFailSparql());
						} catch (SparqlException sqle) {
							logger.debug("failed fail insert:>" + sqle.getMessage() + "<");
						}
					}
				}
			}
		}
	}
}
