/* EJBObject - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.ejb;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface EJBObject extends Remote
{
    public EJBHome getEJBHome() throws RemoteException;
    
    public Object getPrimaryKey() throws RemoteException;
    
    public void remove() throws RemoteException, RemoveException;
    
    public Handle getHandle() throws RemoteException;
    
    public boolean isIdentical(EJBObject ejbobject_0_) throws RemoteException;
}
