/*
 * Created by IntelliJ IDEA.
 * User: jimmy
 * Date: Dec 8, 2001
 * Time: 10:11:30 PM
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package jetserver.server.ejb;

import javax.ejb.EJBHome;

public class EJBMetaData implements javax.ejb.EJBMetaData {
    public EJBHome getEJBHome() {
        return null;
    }

    public Class getHomeInterfaceClass() {
        return null;
    }

    public Class getRemoteInterfaceClass() {
        return null;
    }

    public Class getPrimaryKeyClass() {
        return null;
    }

    public boolean isSession() {
        return false;
    }

    public boolean isStatelessSession() {
        return false;
    }
}
