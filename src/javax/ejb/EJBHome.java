/* EJBHome - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.ejb;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface EJBHome extends Remote
{
    public void remove(Handle handle) throws RemoteException, RemoveException;
    
    public void remove(Object object) throws RemoteException, RemoveException;
    
    public EJBMetaData getEJBMetaData() throws RemoteException;
    
    public HomeHandle getHomeHandle() throws RemoteException;
}
