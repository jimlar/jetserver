/* SystemException - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.transaction;

public class SystemException extends Exception
{
    public int errorCode;
    
    public SystemException() {
	/* empty */
    }
    
    public SystemException(String s) {
	super(s);
    }
    
    public SystemException(int errcode) {
	errorCode = errcode;
    }
}
