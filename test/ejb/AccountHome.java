import javax.ejb.*;
import java.rmi.RemoteException;

public interface AccountHome extends EJBHome
{
	public Account create(long id) throws CreateException, RemoteException;

	public Account findByPrimaryKey(long key) throws RemoteException, FinderException;
}
