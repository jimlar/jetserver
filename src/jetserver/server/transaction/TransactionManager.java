/*
 * Created by IntelliJ IDEA.
 * User: jimmy
 * Date: Dec 8, 2001
 * Time: 10:23:38 PM
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package jetserver.server.transaction;

import javax.transaction.*;
import javax.transaction.Transaction;

public class TransactionManager implements javax.transaction.TransactionManager {
    public void begin() throws NotSupportedException, SystemException {
    }

    public void commit() throws RollbackException, HeuristicMixedException,
            HeuristicRollbackException, SecurityException,
            IllegalStateException, SystemException {
    }

    public int getStatus() throws SystemException {
        return 0;
    }

    public Transaction getTransaction() throws SystemException {
        return null;
    }

    public void resume(Transaction transaction)
            throws InvalidTransactionException, IllegalStateException,
            SystemException {
    }

    public void rollback()
            throws IllegalStateException, SecurityException, SystemException {
    }

    public void setRollbackOnly()
            throws IllegalStateException, SystemException {
    }

    public void setTransactionTimeout(int i) throws SystemException {
    }

    public Transaction suspend() throws SystemException {
        return null;
    }
}
