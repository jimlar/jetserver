/* HandleDelegate - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.ejb.spi;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.ejb.EJBHome;
import javax.ejb.EJBObject;

public interface HandleDelegate
{
    public void writeEJBObject
	(EJBObject ejbobject, ObjectOutputStream objectoutputstream)
	throws IOException;
    
    public EJBObject readEJBObject(ObjectInputStream objectinputstream)
	throws IOException, ClassNotFoundException;
    
    public void writeEJBHome
	(EJBHome ejbhome, ObjectOutputStream objectoutputstream)
	throws IOException;
    
    public EJBHome readEJBHome(ObjectInputStream objectinputstream)
	throws IOException, ClassNotFoundException;
}
