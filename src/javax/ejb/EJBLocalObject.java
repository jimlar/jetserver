/* EJBLocalObject - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.ejb;

public interface EJBLocalObject
{
    public EJBLocalHome getEJBLocalHome() throws EJBException;
    
    public Object getPrimaryKey() throws EJBException;
    
    public void remove() throws RemoveException, EJBException;
    
    public boolean isIdentical(EJBLocalObject ejblocalobject_0_)
	throws EJBException;
}
