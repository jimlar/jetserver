/*
 * Created by IntelliJ IDEA.
 * User: jimmy
 * Date: Dec 8, 2001
 * Time: 10:19:32 PM
 * To change template for new class use
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package jetserver.server.ejb;

import javax.ejb.EJBHome;
import javax.ejb.EJBObject;
import java.io.*;

public class HandleDelegate implements javax.ejb.spi.HandleDelegate {
    public void writeEJBObject
            (EJBObject ejbobject, ObjectOutputStream objectoutputstream)
            throws IOException {
    }

    public EJBObject readEJBObject(ObjectInputStream objectinputstream)
            throws IOException, ClassNotFoundException {
        return null;
    }

    public void writeEJBHome
            (EJBHome ejbhome, ObjectOutputStream objectoutputstream)
            throws IOException {
    }

    public EJBHome readEJBHome(ObjectInputStream objectinputstream)
            throws IOException, ClassNotFoundException {
        return null;
    }
}
