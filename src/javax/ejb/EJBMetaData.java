/* EJBMetaData - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.ejb;

public interface EJBMetaData
{
    public EJBHome getEJBHome();
    
    public Class getHomeInterfaceClass();
    
    public Class getRemoteInterfaceClass();
    
    public Class getPrimaryKeyClass();
    
    public boolean isSession();
    
    public boolean isStatelessSession();
}
