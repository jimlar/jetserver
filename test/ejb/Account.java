import java.rmi.RemoteException;
import javax.ejb.*;

public interface Account extends EJBObject
{
	public long getId() throws RemoteException;

	public long getValue() throws RemoteException;

	public void setValue(long value) throws RemoteException;
}
