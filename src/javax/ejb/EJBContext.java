/* EntityContext - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.ejb;
import java.security.Identity;
import java.security.Principal;
import java.util.Properties;

import javax.transaction.UserTransaction;

public interface EJBContext
{
    public EJBHome getEJBHome();
    
    public EJBLocalHome getEJBLocalHome();
    
    /**
     * @deprecated
     */
    public Properties getEnvironment();
    
    /**
     * @deprecated
     */
    public Identity getCallerIdentity();
    
    public Principal getCallerPrincipal();
    
    /**
     * @deprecated
     */
    public boolean isCallerInRole(Identity identity);
    
    public boolean isCallerInRole(String string);
    
    public UserTransaction getUserTransaction() throws IllegalStateException;
    
    public void setRollbackOnly() throws IllegalStateException;
    
    public boolean getRollbackOnly() throws IllegalStateException;
}
