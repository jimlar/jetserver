/* Transaction - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.transaction;
import javax.transaction.xa.XAResource;

public interface Transaction
{
    public void commit() throws RollbackException, HeuristicMixedException,
				HeuristicRollbackException, SecurityException,
				IllegalStateException, SystemException;
    
    public boolean delistResource(XAResource xaresource, int i)
	throws IllegalStateException, SystemException;
    
    public boolean enlistResource(XAResource xaresource)
	throws RollbackException, IllegalStateException, SystemException;
    
    public int getStatus() throws SystemException;
    
    public void registerSynchronization(Synchronization synchronization)
	throws RollbackException, IllegalStateException, SystemException;
    
    public void rollback() throws IllegalStateException, SystemException;
    
    public void setRollbackOnly()
	throws IllegalStateException, SystemException;
}
