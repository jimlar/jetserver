/* InvalidTransactionException - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.transaction;
import java.rmi.RemoteException;

public class InvalidTransactionException extends RemoteException
{
    public InvalidTransactionException() {
	/* empty */
    }
    
    public InvalidTransactionException(String msg) {
	super(msg);
    }
}
