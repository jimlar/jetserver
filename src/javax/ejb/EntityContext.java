/* EntityContext - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.ejb;

public interface EntityContext extends EJBContext
{
    public EJBLocalObject getEJBLocalObject() throws IllegalStateException;
    
    public EJBObject getEJBObject() throws IllegalStateException;
    
    public Object getPrimaryKey() throws IllegalStateException;
}
