package org.glycoinfo.rdf.dao.virt;

import org.springframework.transaction.TransactionException;

/**
 * @author ameingast@gmail.com
 */
class VirtSesameTransactionException extends TransactionException {
    public VirtSesameTransactionException(Exception e) {
        super(e.getMessage(), e);
    }

    public VirtSesameTransactionException(String s) {
        super(s);
    }
}
