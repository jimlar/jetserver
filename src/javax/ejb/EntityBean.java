/* EntityBean - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.ejb;
import java.rmi.RemoteException;

public interface EntityBean extends EnterpriseBean
{
    public void setEntityContext(EntityContext entitycontext)
	throws EJBException, RemoteException;
    
    public void unsetEntityContext() throws EJBException, RemoteException;
    
    public void ejbRemove()
	throws RemoveException, EJBException, RemoteException;
    
    public void ejbActivate() throws EJBException, RemoteException;
    
    public void ejbPassivate() throws EJBException, RemoteException;
    
    public void ejbLoad() throws EJBException, RemoteException;
    
    public void ejbStore() throws EJBException, RemoteException;
}
