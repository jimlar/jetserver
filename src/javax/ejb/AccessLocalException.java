/* AccessLocalException - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.ejb;

public class AccessLocalException extends EJBException
{
    public AccessLocalException() {
	/* empty */
    }
    
    public AccessLocalException(String message) {
	super(message);
    }
    
    public AccessLocalException(String message, Exception ex) {
	super(message, ex);
    }
}
