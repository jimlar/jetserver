
package javax.transaction.xa;

public interface Xid
{
    public static final int MAXGTRIDSIZE = 64;
    public static final int MAXBQUALSIZE = 64;
    
    public int getFormatId();
    
    public byte[] getGlobalTransactionId();
    
    public byte[] getBranchQualifier();
}
