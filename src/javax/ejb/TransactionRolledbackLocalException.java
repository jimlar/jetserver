/* TransactionRolledbackLocalException - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.ejb;

public class TransactionRolledbackLocalException extends EJBException
{
    public TransactionRolledbackLocalException() {
	/* empty */
    }
    
    public TransactionRolledbackLocalException(String message) {
	super(message);
    }
    
    public TransactionRolledbackLocalException(String message, Exception ex) {
	super(message, ex);
    }
}
