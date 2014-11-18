package org.glycoinfo.batch;

import java.sql.SQLException;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.glycoinfo.ts.dao.SchemaDAO;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.neo4j.template.Neo4jOperations;
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
public class TripleStoreItemWriter<T extends TripleBean> implements ItemWriter<T>, InitializingBean {

		protected static final Log logger = LogFactory
				.getLog(TripleStoreItemWriter.class);

		private boolean delete = false;
		
		private String graph;

		public void setDelete(boolean delete) {
			this.delete = delete;
		}
		
		public String getGraph() {
			return graph;
		}

		public void setGraph(String graph) {
			this.graph = graph;
		}

		public boolean isDelete() {
			return delete;
		}



		@Autowired
		private SchemaDAO schemaDAO;

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
		public void write(List<? extends T> items) throws Exception {
			if(!CollectionUtils.isEmpty(items)) {
				doWrite(items);
			}
		}

		/**
		 * Performs the actual write using the template.  This can be overriden by
		 * a subclass if necessary.
		 *
		 * @param items the list of items to be persisted.
		 * @throws SQLException 
		 */
		protected void doWrite(List<? extends T> items) throws SQLException {
			for (T t : items) {
				logger.debug(t);
				
				try {
					if (!(t.getInsert().trim().length() == 0))
						schemaDAO.insert(getGraph(), t.getInsert(), isDelete());
				} catch (SQLException e) {
					e.printStackTrace();
					schemaDAO.insert(getGraph(), t.getFailInsert(), delete);
				}
			}
		}
	}
