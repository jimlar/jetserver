
package jetserver.server.web;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import jetserver.util.Log;

/**
 * This is a factory where you fetch the servlet instances
 */

public class ServletInstanceFactory {

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

    public HttpServlet getServletInstance(String servletName) throws IOException {

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
            String servletClassName = webApp.getConfig().getServletDeclaration(servletName).getClassName();
            Class servletClass = classLoader.loadClass(servletClassName);
            HttpServlet servlet = (HttpServlet) servletClass.newInstance();
            if (servlet instanceof SingleThreadModel) {
                throw new RuntimeException("single thread model servlets are not supported!");
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
