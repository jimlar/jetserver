

package jetserver.server.web.services.servlet;

import java.io.*;
import java.net.*;
import java.util.*;

import jetserver.server.web.*;
import jetserver.server.web.config.*;
import jetserver.util.Log;

/**
 * This is the entry point for the servlet engine
 * We have one instance per application instance
 */

public class ServletService {

    private WebAppConfig config;
    
    public ServletService(WebAppConfig config) {
	this.config = config;
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

	System.out.println("Running servlet " 
			   + config.getServletDeclaration(mapping.getServletName()).getClassName());

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
