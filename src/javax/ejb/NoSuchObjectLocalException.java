/* NoSuchObjectLocalException - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.ejb;

public class NoSuchObjectLocalException extends EJBException
{
    public NoSuchObjectLocalException() {
	/* empty */
    }
    
    public NoSuchObjectLocalException(String message) {
	super(message);
    }
    
    public NoSuchObjectLocalException(String message, Exception ex) {
	super(message, ex);
    }
}
