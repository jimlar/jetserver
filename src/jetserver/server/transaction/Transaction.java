/*
 * Created by IntelliJ IDEA.
 * User: jimmy
 * Date: Dec 8, 2001
 * Time: 10:22:48 PM
 * To change template for new class use
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package jetserver.server.transaction;

import javax.transaction.*;
import javax.transaction.xa.XAResource;

public class Transaction implements javax.transaction.Transaction {
    public void commit() throws RollbackException, HeuristicMixedException,
            HeuristicRollbackException, SecurityException,
            IllegalStateException, SystemException {
    }

    public boolean delistResource(XAResource xaresource, int i)
            throws IllegalStateException, SystemException {
        return false;
    }

    public boolean enlistResource(XAResource xaresource)
            throws RollbackException, IllegalStateException, SystemException {
        return false;
    }

    public int getStatus() throws SystemException {
        return 0;
    }

    public void registerSynchronization(javax.transaction.Synchronization synchronization)
            throws RollbackException, IllegalStateException, SystemException {
    }

    public void rollback() throws IllegalStateException, SystemException {
    }

    public void setRollbackOnly()
            throws IllegalStateException, SystemException {
    }
}
