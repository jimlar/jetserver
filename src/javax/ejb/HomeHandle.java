/* HomeHandle - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.ejb;
import java.io.Serializable;
import java.rmi.RemoteException;

public interface HomeHandle extends Serializable
{
    public EJBHome getEJBHome() throws RemoteException;
}
