package jetserver.server.naming;

import jetserver.util.Log;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;
import java.util.Hashtable;

public class JetServerContextFactory implements InitialContextFactory {

    private static JetServerContext context;

    /**
     * Creates an Initial Context for beginning name resolution.
     * Special requirements of this context are supplied
     * using <code>environment</code>.
     *<p>
     * The environment parameter is owned by the caller.
     * The implementation will not modify the object or keep a reference
     * to it, although it may keep a reference to a clone or copy.
     *
     * @param environment The possibly null environment
     * 		specifying information to be used in the creation
     * 		of the initial context.
     * @return A non-null initial context object that implements the Context
     *		interface.
     * @exception NamingException If cannot create an initial context.
     */
    public Context getInitialContext(Hashtable environment)
            throws NamingException {

        Context ctx = getContext(environment);
        Log.getInstance(this).debug("returning initialcontext");
        return ctx;
    }

    private JetServerContext getContext(Hashtable environment) {
        if (context == null) {
            context = new JetServerContext(environment);
            Log.getInstance(this).debug("created initialcontext");
        }
        return context;
    }
}
