/* NoSuchEntityException - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.ejb;

public class NoSuchEntityException extends EJBException
{
    public NoSuchEntityException() {
	/* empty */
    }
    
    public NoSuchEntityException(String message) {
	super(message);
    }
    
    public NoSuchEntityException(Exception ex) {
	super(ex);
    }
}
