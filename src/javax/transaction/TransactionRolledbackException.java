/* TransactionRolledbackException - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.transaction;
import java.rmi.RemoteException;

public class TransactionRolledbackException extends RemoteException
{
    public TransactionRolledbackException() {
	/* empty */
    }
    
    public TransactionRolledbackException(String msg) {
	super(msg);
    }
}
