/*
 * Created by IntelliJ IDEA.
 * User: jimmy
 * Date: Dec 8, 2001
 * Time: 10:14:17 PM
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package jetserver.server.ejb;

import javax.ejb.EJBLocalObject;
import javax.ejb.EJBHome;
import javax.ejb.EJBObject;
import javax.ejb.EJBLocalHome;
import javax.transaction.UserTransaction;
import java.util.Properties;
import java.security.Identity;
import java.security.Principal;

public class SessionContext implements javax.ejb.SessionContext {
    public EJBLocalObject getEJBLocalObject() throws IllegalStateException {
        return null;
    }

    public EJBHome getEJBHome() {
        return null;
    }

    public EJBObject getEJBObject() throws IllegalStateException {
        return null;
    }

    public EJBLocalHome getEJBLocalHome() {
        return null;
    }

    /**
     * @deprecated
     */
    public Properties getEnvironment() {
        return null;
    }

    /**
     * @deprecated
     */
    public Identity getCallerIdentity() {
        return null;
    }

    public Principal getCallerPrincipal() {
        return null;
    }

    /**
     * @deprecated
     */
    public boolean isCallerInRole(Identity identity) {
        return false;
    }

    public boolean isCallerInRole(String string) {
        return false;
    }

    public UserTransaction getUserTransaction() throws IllegalStateException {
        return null;
    }

    public void setRollbackOnly() throws IllegalStateException {
    }

    public boolean getRollbackOnly() throws IllegalStateException {
        return false;
    }
}
