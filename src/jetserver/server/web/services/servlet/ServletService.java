

package jetserver.server.web.services.servlet;

import java.io.*;
import java.net.*;
import java.util.*;

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
    private ServletClassLoader classLoader;


    public ServletService(WebAppConfig config) {
	this.config = config;
	this.log = Log.getInstance(this);
	this.classLoader = new ServletClassLoader(config);
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
	
	String servletClassName 
	    = config.getServletDeclaration(mapping.getServletName()).getClassName();

	log.debug("running servlet " + servletClassName);

	try {
	    Class servletClass = classLoader.loadClass(servletClassName);	
	    HttpServlet servlet = (HttpServlet) servletClass.newInstance();
	} catch (Exception e) {
	    throw new IOException("cant run servlet " + e);
	}

	log.debug("servlet " + servletClassName + " done");

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
		return mapping;
	    }
	}
	return null;
    }
}
