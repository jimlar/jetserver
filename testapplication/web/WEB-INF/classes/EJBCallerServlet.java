
import java.io.*;
import java.rmi.RemoteException;

import javax.servlet.*;
import javax.servlet.http.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.ejb.FinderException;

public class EJBCallerServlet extends HttpServlet {

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
            AccountHome accountHome = (AccountHome) ctx.lookup("Account");
            writer.println("Home found, finding bean");
            Account account = accountHome.findByPrimaryKey(1);

            if (account != null) {
                writer.println("Bean found, reading value");
                long value = account.getValue();
                value++;
                writer.println("Value read, setting back increased value");
                account.setValue(value);
                writer.println("Value set, done.");
            } else {
                writer.println("Bean NOT found, aborted!");
            }

            writer.flush();
        } catch (NamingException e) {
            throw new ServletException(e);
        } catch (FinderException e) {
            throw new ServletException(e);
        } catch (RemoteException e) {
            throw new ServletException(e);
        }
    }
}
