package org.glycoinfo.rdf.dao;

import org.glycoinfo.rdf.SparqlException;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryException;
import org.openrdf.spring.SesameConnectionFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.transaction.TransactionSystemException;

/**
 * <p>{@link SparqlEntityFactory} handles data corresponding to a {@link SparqlEntity}.</p>
 *
 * @author aokinobu@gmail.com
 * @see SparqlEntity
 */
public class SparqlEntityFactory implements DisposableBean {
    private final ThreadLocal<SparqlEntity> localSparqlEntityThread;


    /**
     * <p>Creates a new {@link SparqlEntityFactory} for the provided {@link Repository}.</p>
     *
     * @param repository The repository to which connections are opened.
     */
    public SparqlEntityFactory() {
    	localSparqlEntityThread = new ThreadLocal<SparqlEntity>();
    }
    
    public SparqlEntity create() {
    	localSparqlEntityThread.set(new SparqlEntity());
    	return localSparqlEntityThread.get();
    }

    public SparqlEntity getSparqlEntity() throws SparqlException {
    	SparqlEntity sparqlEntityObject = localSparqlEntityThread.get();

        if (sparqlEntityObject == null) {
            throw new SparqlException("No sparqlentity active");
        }

        return sparqlEntityObject;
    }
    
    public void set(SparqlEntity s) {
    	localSparqlEntityThread.set(s);
    }

    public void close() {
    	localSparqlEntityThread.remove();
    }

    @Override
    public String toString() {
        try {
			return "SparqlEntityFactory{localSparqlEntityThread=" + localSparqlEntityThread + ", sparqlEntity" + getSparqlEntity() +
			        "}";
		} catch (SparqlException e) {
		e.printStackTrace();
		return e.getMessage();
		}
    }

	@Override
	public void destroy() throws Exception {
		close();		
	}
}
