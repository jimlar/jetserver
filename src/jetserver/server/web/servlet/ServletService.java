

package jetserver.server.web.servlet;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import jetserver.server.web.*;
import jetserver.server.web.HttpRequest;
import jetserver.server.web.HttpResponse;
import jetserver.util.Log;

/**
 * This is the entry point for the servlet engine
 * We have one instance per application instance
 */

public class ServletService {

    private WebApplication webApp;
    private Log log;

    private ServletInstanceFactory servletInstanceFactory;


    public ServletService(WebApplication webApp) {
        this.webApp = webApp;
        this.log = Log.getInstance(this);
        this.servletInstanceFactory = new ServletInstanceFactory(webApp);
    }

    /**
     * @return true if we handled the request 
     */
    public boolean service(HttpRequest request, HttpResponse response)
            throws IOException
    {
        /* search for a servlet mapping */
        ServletMapping mapping = getMapping(request);
        if (mapping == null) {
            return false;
        }

        HttpServlet servlet = servletInstanceFactory.getServletInstance(mapping.getServletName());

        if (servlet != null) {
            JetServerHttpServletRequest httpServletRequest
                    = new JetServerHttpServletRequest(request, webApp.getConfig());
            JetServerHttpServletResponse httpServletResponse
                    = new JetServerHttpServletResponse(response, webApp.getConfig());

            try {
                servlet.service(httpServletRequest, httpServletResponse);
                httpServletResponse.close();

            } catch (ServletException e) {
                throw new IOException("got servlet exception: " + e);
            }
        } else {
            throw new IOException("could not start servlet");
        }

        return true;
    }

    /**
     * Fetch a mapping matching the request
     * @return the mapping found or null if no mapping
     */
    private ServletMapping getMapping(HttpRequest request) {

        Iterator mappings = webApp.getConfig().getServletMappings().iterator();
        while (mappings.hasNext()) {
            ServletMapping mapping = (ServletMapping) mappings.next();

            /* Should actually test with the contextpath prepended to the mapping */
            if (mapping.getUrlPattern().equals(request.getURI())) {
                return mapping;
            }
        }
        return null;
    }
}
