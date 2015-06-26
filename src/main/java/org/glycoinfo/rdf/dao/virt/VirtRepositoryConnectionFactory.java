package org.glycoinfo.rdf.dao.virt;

import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.spring.SesameConnectionFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.transaction.TransactionSystemException;

/**
 * <p>{@link RepositoryConnectionFactory} handles connections to a single corresponding {@link Repository} and manages
 * the transaction state (represented by {@link SesameTransactionObject}).</p>
 * <p/>
 * <p>This class provides methods to access <i>transactional</i> connections from the outside and is typically the
 * only class that library users interact with.</p>
 *
 * @author ameingast@gmail.com
 * @see SesameConnectionFactory
 */
public class VirtRepositoryConnectionFactory implements DisposableBean, VirtSesameConnectionFactory {
    private final ThreadLocal<VirtSesameTransactionObject> localTransactionObject;

    private final Repository repository;

    /**
     * <p>Creates a new {@link RepositoryConnectionFactory} for the provided {@link Repository}.</p>
     *
     * @param repository The repository to which connections are opened.
     */
    public VirtRepositoryConnectionFactory(Repository repository) {
        this.repository = repository;
        localTransactionObject = new ThreadLocal<VirtSesameTransactionObject>();
    }

    /**
     * @inheritDoc
     */
    @Override
    public RepositoryConnection getConnection() {
    	VirtSesameTransactionObject sesameTransactionObject = localTransactionObject.get();

        if (sesameTransactionObject == null) {
            throw new VirtSesameTransactionException("No transaction active");
        }

        RepositoryConnection repositoryConnection = sesameTransactionObject.getRepositoryConnection();

        try {
            if (!repositoryConnection.isOpen()) {
                throw new VirtSesameTransactionException("Connection closed during transaction");
            }

            if (!repositoryConnection.isActive()) {
                repositoryConnection.begin();
            }
        } catch (RepositoryException e) {
            throw new VirtSesameTransactionException(e);
        }

        return repositoryConnection;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void closeConnection() {
        VirtSesameTransactionObject sesameTransactionObject = null;
        RepositoryConnection repositoryConnection = null;

        try {
            sesameTransactionObject = localTransactionObject.get();

            if (sesameTransactionObject == null) {
                throw new VirtSesameTransactionException("No transaction active");
            }

            repositoryConnection = sesameTransactionObject.getRepositoryConnection();

            try {
                if (!repositoryConnection.isOpen()) {
                    throw new VirtSesameTransactionException("Connection closed during transaction");
                }
            } catch (RepositoryException e) {
                throw new VirtSesameTransactionException(e);
            }
        } finally {
            if (sesameTransactionObject != null && repositoryConnection != null) {
                try {
                    repositoryConnection.close();
                } catch (RepositoryException e) {
                    throw new VirtSesameTransactionException(e);
                }

                localTransactionObject.remove();
            }
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public VirtSesameTransactionObject createTransaction() throws RepositoryException {
        RepositoryConnection repositoryConnection = repository.getConnection();

        VirtSesameTransactionObject sesameTransactionObject = new VirtSesameTransactionObject(repositoryConnection);
        localTransactionObject.set(sesameTransactionObject);

        return sesameTransactionObject;
    }

    /**
     * @inheritDoc
     */
    @Override
    public void endTransaction(boolean rollback) throws RepositoryException {
    	VirtSesameTransactionObject sesameTransactionObject = localTransactionObject.get();

        if (sesameTransactionObject == null) {
            throw new TransactionSystemException("No transaction active");
        }

        RepositoryConnection repositoryConnection = sesameTransactionObject.getRepositoryConnection();

        if (!repositoryConnection.isOpen()) {
            throw new VirtSesameTransactionException("Connection closed during transaction");
        }

        if (repositoryConnection.isActive()) {
            if (rollback) {
                repositoryConnection.rollback();
            } else {
                repositoryConnection.commit();
            }
        }
    }

    /**
     * @inheritDoc
     */
    @Override
    public VirtSesameTransactionObject getLocalTransactionObject() {
        return localTransactionObject.get();
    }

    /**
     * <p>Shuts down the associated {@link Repository} if it was initialized before.</p>
     *
     * @throws Exception {@see Repository#shutDown}
     */
    @Override
    public void destroy() throws Exception {
        if (repository != null && repository.isInitialized()) {
            repository.shutDown();
        }
    }

    @Override
    public String toString() {
        return "RepositoryConnectionFactory{" +
                "repository=" + repository +
                ", localTransactionObject=" + localTransactionObject +
                '}';
    }
}
