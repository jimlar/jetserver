/* XAResource - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.transaction.xa;

public interface XAResource
{
    public static final int TMENDRSCAN = 8388608;
    public static final int TMFAIL = 536870912;
    public static final int TMJOIN = 2097152;
    public static final int TMNOFLAGS = 0;
    public static final int TMONEPHASE = 1073741824;
    public static final int TMRESUME = 134217728;
    public static final int TMSTARTRSCAN = 16777216;
    public static final int TMSUCCESS = 67108864;
    public static final int TMSUSPEND = 33554432;
    public static final int XA_RDONLY = 3;
    public static final int XA_OK = 0;
    
    public void commit(Xid xid, boolean bool) throws XAException;
    
    public void end(Xid xid, int i) throws XAException;
    
    public void forget(Xid xid) throws XAException;
    
    public int getTransactionTimeout() throws XAException;
    
    public boolean isSameRM(XAResource xaresource_0_) throws XAException;
    
    public int prepare(Xid xid) throws XAException;
    
    public Xid[] recover(int i) throws XAException;
    
    public void rollback(Xid xid) throws XAException;
    
    public boolean setTransactionTimeout(int i) throws XAException;
    
    public void start(Xid xid, int i) throws XAException;
}
