/*
 * Created by IntelliJ IDEA.
 * User: jimmy
 * Date: Dec 8, 2001
 * Time: 10:24:36 PM
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package jetserver.server.transaction;

import javax.transaction.*;

public class UserTransaction implements javax.transaction.UserTransaction {
    public void begin() throws NotSupportedException, SystemException {
    }

    public void commit() throws RollbackException, HeuristicMixedException,
            HeuristicRollbackException, SecurityException,
            IllegalStateException, SystemException {
    }

    public void rollback()
            throws IllegalStateException, SecurityException, SystemException {
    }

    public void setRollbackOnly()
            throws IllegalStateException, SystemException {
    }

    public int getStatus() throws SystemException {
        return 0;
    }

    public void setTransactionTimeout(int i) throws SystemException {
    }
}
