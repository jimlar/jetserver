
package jetserver.server.web.services.servlet;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import jetserver.server.web.config.*;
import jetserver.util.Log;

/**
 * This is a factory where you fetch the servlet instances
 */

class ServletInstanceFactory {

    private WebAppConfig config;
    private Log log;

    private ServletClassLoader classLoader;
    private Map instancesByName;

    ServletInstanceFactory(WebAppConfig config) {
	this.config = config;
	this.log = Log.getInstance(this);
	this.classLoader = new ServletClassLoader(config);
	this.instancesByName = new HashMap();
    }

    /**
     * Fetch a servlet instance with the name of a servlet
     */

    public HttpServlet getServletInstance(String servletName) {

	HttpServlet servlet = (HttpServlet) instancesByName.get(servletName);
	
	if (servlet == null) {
	    synchronized (instancesByName) {
		servlet = (HttpServlet) instancesByName.get(servletName);
		if (servlet != null) {
		    return servlet;
		}

		servlet = createInstance(servletName);
		if (servlet != null) {
		    instancesByName.put(servletName, servlet);
		}
	    }
	} 
	return servlet;
    }

    private HttpServlet createInstance(String servletName) {
	try {
	    String servletClassName = config.getServletDeclaration(servletName).getClassName();
	    Class servletClass = classLoader.loadClass(servletClassName);	
	    HttpServlet servlet = (HttpServlet) servletClass.newInstance();
	    if (servlet instanceof SingleThreadModel) {
		throw new RuntimeException("single thread model servlets are not supported!");
	    }
	    return servlet;

	} catch (ClassNotFoundException e) {
	    log.error("Cant load servlet class", e);
	} catch (IllegalAccessException e) {
	    log.error("Cant load servlet class", e);
	} catch (InstantiationException e) {
	    log.error("Cant instantiate servlet", e);
	}
	return null;
    }
}
