package org.glycoinfo.rdf.dao;

import org.glycoinfo.rdf.SparqlException;
import org.springframework.beans.factory.DisposableBean;

/**
 * <p>{@link SparqlEntityFactory} handles data corresponding to a {@link SparqlEntity}.</p>
 *
 * @author aokinobu@gmail.com
 * @see SparqlEntity
 */
public class SparqlEntityFactory implements DisposableBean {
    private static final ThreadLocal<SparqlEntity> localSparqlEntityThread = new ThreadLocal<SparqlEntity>();
    
    public static SparqlEntity getSparqlEntity() throws SparqlException {
    	return localSparqlEntityThread.get();
    }
    
    public static void set(SparqlEntity s) {
    	localSparqlEntityThread.set(s);
    }

    public static void unset() {
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
		unset();		
	}
}