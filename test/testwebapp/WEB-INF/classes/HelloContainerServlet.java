
import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

public class HelloContainerServlet extends HttpServlet {

    public HelloContainerServlet() {
	System.out.println("HelloContainerServlet instantiated");
    }

    public void doGet(HttpServletRequest request,
		      HttpServletResponse response)
	throws ServletException, IOException {

	System.out.println("Hello container!");
    }

}
