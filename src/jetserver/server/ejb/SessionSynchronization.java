/*
 * Created by IntelliJ IDEA.
 * User: jimmy
 * Date: Dec 8, 2001
 * Time: 10:17:55 PM
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package jetserver.server.ejb;

import javax.ejb.EJBException;
import java.rmi.RemoteException;

public class SessionSynchronization implements javax.ejb.SessionSynchronization {
    public void afterBegin() throws EJBException, RemoteException {
    }

    public void beforeCompletion() throws EJBException, RemoteException {
    }

    public void afterCompletion(boolean bool)
            throws EJBException, RemoteException {
    }
}
