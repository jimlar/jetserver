/*
 * Created by IntelliJ IDEA.
 * User: jimmy
 * Date: Dec 8, 2001
 * Time: 10:15:40 PM
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package jetserver.server.ejb;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;

public class Handle implements javax.ejb.Handle {
    public EJBObject getEJBObject() throws RemoteException {
        return null;
    }
}
