/* EJBException - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.ejb;
import java.io.PrintStream;
import java.io.PrintWriter;

public class EJBException extends RuntimeException
{
    private Exception causeException = null;
    
    public EJBException() {
	/* empty */
    }
    
    public EJBException(String message) {
	super(message);
    }
    
    public EJBException(Exception ex) {
	causeException = ex;
    }
    
    public EJBException(String message, Exception ex) {
	super(message);
	causeException = ex;
    }
    
    public Exception getCausedByException() {
	return causeException;
    }
    
    public String getMessage() {
	String msg = super.getMessage();
	if (causeException == null)
	    return msg;
	if (msg == null)
	    return "nested exception is: " + causeException.toString();
	return msg + "; nested exception is: " + causeException.toString();
    }
    
    public void printStackTrace(PrintStream ps) {
	if (causeException == null)
	    super.printStackTrace(ps);
	else {
	    synchronized (ps) {
		ps.println(this);
		causeException.printStackTrace(ps);
		super.printStackTrace(ps);
	    }
	}
    }
    
    public void printStackTrace() {
	printStackTrace(System.err);
    }
    
    public void printStackTrace(PrintWriter pw) {
	if (causeException == null)
	    super.printStackTrace(pw);
	else {
	    synchronized (pw) {
		pw.println(this);
		causeException.printStackTrace(pw);
		super.printStackTrace(pw);
	    }
	}
    }
}
