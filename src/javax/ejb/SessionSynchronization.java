/* SessionSynchronization - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.ejb;
import java.rmi.RemoteException;

public interface SessionSynchronization
{
    public void afterBegin() throws EJBException, RemoteException;
    
    public void beforeCompletion() throws EJBException, RemoteException;
    
    public void afterCompletion(boolean bool)
	throws EJBException, RemoteException;
}
