
package jetserver.server.web;

import java.io.*;
import java.util.*;

import jetserver.server.web.config.WebApplicationConfig;
import jetserver.server.web.config.ServletMapping;
import jetserver.server.application.Application;
import jetserver.util.Log;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletException;

/**
 * This represent a web application instance (a .war file that is deployed)
 */
public class WebApplication {

    private Application application;
    private WebApplicationConfig config;
    private ServletInstanceFactory servletInstanceFactory;
    private FileServer fileServer;

    public WebApplication(Application application, WebApplicationConfig config) throws IOException {
        this.application = application;
        this.config = config;
        this.fileServer = new FileServer(this);
        this.servletInstanceFactory = new ServletInstanceFactory(this);
    }

    public Application getApplication() {
        return this.application;
    }

    public WebApplicationConfig getConfig() {
        return this.config;
    }

    public void handleRequest(JSHttpServletRequest request,
                                JSHttpServletResponse response)
            throws IOException
    {
        /* search for a servlet mapping */
        ServletMapping mapping = getMapping(request.getRequestURI());
        if (mapping == null) {

            /* No mapping found, let the file server take over */
            fileServer.serveFile(request, response);

        } else {
            HttpServlet servlet = servletInstanceFactory.getServletInstance(mapping);
            try {
                servlet.service(request, response);

            } catch (ServletException e) {
                throw new IOException("got servlet exception: " + e);
            }
        }
    }

    /**
     * Fetch a mapping matching the request
     * @return the mapping found or null if no mapping
     */
    private ServletMapping getMapping(String requestURI) {

        Iterator mappings = getConfig().getServletMappings().iterator();
        while (mappings.hasNext()) {
            ServletMapping mapping = (ServletMapping) mappings.next();

            /* Should actually test with the contextpath prepended to the mapping */
            if (mapping.getUrlPattern().equals(requestURI)) {
                return mapping;
            }
        }
        return null;
    }
}
