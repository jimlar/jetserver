/* RollbackException - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.transaction;

public class RollbackException extends Exception
{
    public RollbackException() {
	/* empty */
    }
    
    public RollbackException(String msg) {
	super(msg);
    }
}
