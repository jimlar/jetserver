/* SessionBean - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.ejb;
import java.rmi.RemoteException;

public interface SessionBean extends EnterpriseBean
{
    public void setSessionContext(SessionContext sessioncontext)
	throws EJBException, RemoteException;
    
    public void ejbRemove() throws EJBException, RemoteException;
    
    public void ejbActivate() throws EJBException, RemoteException;
    
    public void ejbPassivate() throws EJBException, RemoteException;
}
