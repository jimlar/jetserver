

package jetserver.server.web.servlet;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import jetserver.server.web.*;
import jetserver.server.web.file.FileServer;
import jetserver.util.Log;

/**
 * This is the entry point for the servlet engine
 * We have one instance per application instance
 */

public class ServletDispatcher {

    private Log log;

    private WebApplication webApplication;
    private ServletInstanceFactory servletInstanceFactory;
    private FileServer fileServer;

    public ServletDispatcher(WebApplication webApplication) {
        this.webApplication = webApplication;
        this.fileServer = new FileServer(webApplication);
        this.servletInstanceFactory = new ServletInstanceFactory(webApplication);

        this.log = Log.getInstance(this);
    }

    /**
     * @return true if we handled the request 
     */
    public void dispatch(JSHttpServletRequest request,
                         JSHttpServletResponse response)
            throws IOException
    {
        /* search for a servlet mapping */
        ServletMapping mapping = getMapping(request);
        if (mapping == null) {

            /* No mapping found, let the file server take over */
            fileServer.serveFile(request, response);
            response.close();
            return;
        }

        HttpServlet servlet = servletInstanceFactory.getServletInstance(mapping.getServletName());

        if (servlet != null) {

            try {
                servlet.service(request, response);
                response.close();

            } catch (ServletException e) {
                throw new IOException("got servlet exception: " + e);
            }
        } else {
            throw new IOException("could not start servlet");
        }
    }

    /**
     * Fetch a mapping matching the request
     * @return the mapping found or null if no mapping
     */
    private ServletMapping getMapping(JSHttpServletRequest request) {

        Iterator mappings = webApplication.getConfig().getServletMappings().iterator();
        while (mappings.hasNext()) {
            ServletMapping mapping = (ServletMapping) mappings.next();

            /* Should actually test with the contextpath prepended to the mapping */
            if (mapping.getUrlPattern().equals(request.getRequestURI())) {
                return mapping;
            }
        }
        return null;
    }
}
