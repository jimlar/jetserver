/* UserTransaction - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.transaction;

public interface UserTransaction
{
    public void begin() throws NotSupportedException, SystemException;
    
    public void commit() throws RollbackException, HeuristicMixedException,
				HeuristicRollbackException, SecurityException,
				IllegalStateException, SystemException;
    
    public void rollback()
	throws IllegalStateException, SecurityException, SystemException;
    
    public void setRollbackOnly()
	throws IllegalStateException, SystemException;
    
    public int getStatus() throws SystemException;
    
    public void setTransactionTimeout(int i) throws SystemException;
}
