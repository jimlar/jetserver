/*
 * Created by IntelliJ IDEA.
 * User: jimmy
 * Date: Dec 8, 2001
 * Time: 10:16:15 PM
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package jetserver.server.ejb;

import javax.ejb.EJBHome;
import java.rmi.RemoteException;

public class HomeHandle implements javax.ejb.HomeHandle {
    public EJBHome getEJBHome() throws RemoteException {
        return null;
    }
}
