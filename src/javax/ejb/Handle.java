/* Handle - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.ejb;
import java.io.Serializable;
import java.rmi.RemoteException;

public interface Handle extends Serializable
{
    public EJBObject getEJBObject() throws RemoteException;
}
