/* Synchronization - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.transaction;

public interface Synchronization
{
    public void beforeCompletion();
    
    public void afterCompletion(int i);
}
