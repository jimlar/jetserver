
import java.io.*;
import java.rmi.RemoteException;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class EJBCallerServlet extends HttpServlet {

    public EJBCallerServlet() {
        System.out.println("EJBCallerServlet instantiated");
    }

    public void doGet(HttpServletRequest request,
                      HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/plain");
        PrintWriter writer = response.getWriter();

        /** Call our simple entity bean */
        try {
            writer.println("Creating context");
            Context ctx = new InitialContext();
            writer.println("Context created, looking up bean");
            Account account = (Account) ctx.lookup("Account");
            writer.println("Bean found, reading value");
            long value = account.getValue();
            value++;
            writer.println("Value read, setting back increased value");
            account.setValue(value);
            writer.println("Value set, done.");
            writer.flush();
        } catch (NamingException e) {
            throw new ServletException(e);
        } catch (RemoteException e) {
            throw new ServletException(e);
        }
    }
}
