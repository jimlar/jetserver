/* MessageDrivenBean - Decompiled by JODE
 * Visit http://jode.sourceforge.net/
 */
package javax.ejb;

public interface MessageDrivenBean extends EnterpriseBean
{
    public void setMessageDrivenContext
	(MessageDrivenContext messagedrivencontext) throws EJBException;
    
    public void ejbRemove() throws EJBException;
}
