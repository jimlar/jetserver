

package jetserver.server.web.services.servlet;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import jetserver.server.web.*;
import jetserver.server.web.config.*;
import jetserver.util.Log;

/**
 * This is the entry point for the servlet engine
 * We have one instance per application instance
 */

public class ServletService {

    private WebAppConfig config;
    private Log log;

    private ServletInstanceFactory servletInstanceFactory;


    public ServletService(WebAppConfig config) {
	this.config = config;
	this.log = Log.getInstance(this);
	this.servletInstanceFactory = new ServletInstanceFactory(config);
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
	    HttpServletRequest httpServletRequest = new JetServerHttpServletRequest(request, config);
	    //HttpServletResponse httpServletResponse = new JetServerHttpServletResponse(request);

	    try {
		servlet.service(httpServletRequest, null);
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

	Iterator mappings = config.getServletMappings().iterator();
	while (mappings.hasNext()) {
	    ServletMapping mapping = (ServletMapping) mappings.next();

	    /* Should actually test with the contextpath prepended to the mapping */
	    if (mapping.getUrlPattern().equals(request.getURI())) {
		log.debug("found servlet mapping " + mapping);
		return mapping;
	    }
	}
	return null;
    }
}
