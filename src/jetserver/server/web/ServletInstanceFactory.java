
package jetserver.server.web;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import jetserver.util.Log;
import jetserver.server.web.config.ServletMapping;
import jetserver.server.web.config.JSServletConfig;

/**
 * This is a factory where you fetch the servlet instances
 */

class ServletInstanceFactory {

    private WebApplication webApp;
    private Log log;

    private ServletClassLoader classLoader;
    private Map instancesByName;

    public ServletInstanceFactory(WebApplication webApp) {
        this.webApp = webApp;
        this.log = Log.getInstance(this);
        this.classLoader = new ServletClassLoader(webApp);
        this.instancesByName = new HashMap();
    }

    /**
     * Fetch a servlet instance with the name of a servlet
     */

    public HttpServlet getServletInstance(ServletMapping servletMapping) throws IOException {

        String servletName = servletMapping.getServletName();
        HttpServlet servlet = (HttpServlet) instancesByName.get(servletName);

        if (servlet == null) {
            synchronized (instancesByName) {
                servlet = (HttpServlet) instancesByName.get(servletName);
                if (servlet != null) {
                    return servlet;
                }

                servlet = createInstance(servletName);
                instancesByName.put(servletName, servlet);
            }
        }
        return servlet;
    }

    private HttpServlet createInstance(String servletName) throws IOException {
        try {
            JSServletConfig servletConfig = webApp.getServletConfig(servletName);
            Class servletClass = classLoader.loadClass(servletConfig.getClassName());
            HttpServlet servlet = (HttpServlet) servletClass.newInstance();
            if (servlet instanceof SingleThreadModel) {
                throw new RuntimeException("single thread model servlets are not supported!");
            }

            try {
                servlet.init(servletConfig);
            } catch (ServletException e) {
                throw new IOException("Cant initialize servlet: " + e);
            }
            return servlet;

        } catch (ClassNotFoundException e) {
            throw new IOException("Cant load servlet class: " + e);
        } catch (IllegalAccessException e) {
            throw new IOException("Cant load servlet class: " + e);
        } catch (InstantiationException e) {
            throw new IOException("Cant instantiate servlet: " + e);
        }
    }
}
