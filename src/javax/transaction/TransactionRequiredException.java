/* TransactionRequiredException - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.transaction;
import java.rmi.RemoteException;

public class TransactionRequiredException extends RemoteException
{
    public TransactionRequiredException() {
	/* empty */
    }
    
    public TransactionRequiredException(String msg) {
	super(msg);
    }
}
