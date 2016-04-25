package org.glycoinfo.batch;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.rdf.InsertSparql;
import org.glycoinfo.rdf.SparqlException;
import org.glycoinfo.rdf.dao.SparqlDAO;
import org.glycoinfo.rdf.dao.SparqlEntity;
import org.glycoinfo.rdf.dao.SparqlEntityFactory;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

/**
 * <p>
 * A {@link ItemWriter} implementation that writes to a Neo4j database using an
 * implementation of Spring Data's {@link Neo4jOperations}.
 * </p>
 *
 * <p>
 * This writer is thread-safe once all properties are set (normal singleton
 * behavior) so it can be used in multiple concurrent transactions.
 * </p>
 *
 */
public class SparqlItemWriter<T extends SparqlEntity> implements
		ItemWriter<SparqlEntity>, InitializingBean {

	protected static final Log logger = LogFactory
			.getLog(SparqlItemWriter.class);

	@Autowired
	private SparqlDAO schemaDAO;

	@Autowired
	@Qualifier("itemWriterInsertSparql")
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
		Assert.state(schemaDAO != null, "A dao is required");
	}

	/**
	 * Write all items to the data store.
	 *
	 * @see org.springframework.batch.item.ItemWriter#write(java.util.List)
	 */
	@Override
	public void write(List<? extends SparqlEntity> items) throws Exception {
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
	protected void doWrite(List<? extends SparqlEntity> items)
			throws SQLException, SparqlException {
		for (SparqlEntity t : items) {
			logger.debug(t);

			InsertSparql insertSparql = getInsertSparql();
			try {
				SparqlEntityFactory.set(t);
				insertSparql.setSparqlEntity(t);

				if (!(insertSparql.getSparql().trim().length() == 0)) {
					logger.debug("inserting >" + insertSparql.getSparql()
							+ "< into " + insertSparql.getGraph() + "<");
					
					schemaDAO.insert(insertSparql);
				}
			} catch (SparqlException e) {
				e.printStackTrace();
				InsertSparql fail = getInsertFailSparql();
				if (null != fail) {
					fail.setSparqlEntity(t);
					logger.debug("inserting " + fail.getSparql() + " into "
							+ t.getGraph() + "< failed");
					try {
						schemaDAO.insert(getInsertFailSparql());
					} catch (SparqlException sqle) {
						logger.debug("inserting " + sqle.getMessage() + "<");
					}
				}
			}
		}
	}
}
