import javax.ejb.*;

public abstract class AccountEJB implements EntityBean {
	protected EntityContext context;

	public abstract long getId();
	public abstract void setId(long value);
	public abstract long getValue();
	public abstract void setValue(long value);

	public void ejbLoad() 	{
	}

	public void ejbStore() 	{
	}

	public void ejbActivate() 	{
	}

	public void ejbPassivate() 	{
	}

	public void ejbRemove() 	{
	}

	public void setEntityContext(EntityContext context) 	{
		this.context = context;
	}

	public void unsetEntityContext() 	{
		this.context = null;
	}

	public Long ejbCreate(long id) throws CreateException 	{
		this.setId(id);
		return null; // Return null when using CMP
	}

	public void ejbPostCreate(long id) 	{
	}
}
