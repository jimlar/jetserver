/* SessionContext - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.ejb;

public interface SessionContext extends EJBContext
{
    public EJBLocalObject getEJBLocalObject() throws IllegalStateException;
    
    public EJBObject getEJBObject() throws IllegalStateException;
}
