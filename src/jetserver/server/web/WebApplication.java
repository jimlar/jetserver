
package jetserver.server.web;

import java.io.*;
import java.util.*;
import java.net.URL;
import java.net.MalformedURLException;

import jetserver.server.Application;
import jetserver.util.Log;

import javax.servlet.http.HttpServlet;
import javax.servlet.ServletException;
import javax.servlet.ServletContext;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;

/**
 * This represent a web application instance (a .war file that is deployed)
 */
public class WebApplication implements ServletContext {

    private Log log;

    private Application application;
    private ServletInstanceFactory servletInstanceFactory;
    private FileServer fileServer;

    private String displayName;
    private String contextRoot;
    private File deployDir;
    private List welcomeFiles;
    private List servletMappings;
    private Map servletConfigsByName;

    private MimeTypes mimeTypes;
    private Map initParameters;
    private Map attributes;

    public WebApplication(Application application, File deployDir)
            throws IOException
    {
        this.application = application;
        this.deployDir = deployDir;

        this.welcomeFiles = new ArrayList();
        this.servletMappings = new ArrayList();
        this.servletConfigsByName = new HashMap();
        this.fileServer = new FileServer(this);
        this.servletInstanceFactory = new ServletInstanceFactory(this);
        this.mimeTypes = new MimeTypes();
        this.initParameters = new HashMap();
        this.attributes = new HashMap();
        this.log = Log.getInstance(this);
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public void setContextRoot(String contextRoot) {
        this.contextRoot = contextRoot;
    }

    public void addWelcomeFile(String welcomeFile) {
        this.welcomeFiles.add(welcomeFile);
    }

    public void addServletMapping(ServletMapping servletMapping) {
        this.servletMappings.add(servletMapping);
    }

    public void addServletConfig(JSServletConfig servletConfig) {
        this.servletConfigsByName.put(servletConfig.getServletName(), servletConfig);
    }

    public Application getApplication() {
        return this.application;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public String getContextRoot() {
        return this.contextRoot;
    }

    public File getDeployDir() {
        return this.deployDir;
    }

    public List getWelcomeFiles() {
        return this.welcomeFiles;
    }

    public JSServletConfig getServletConfig(String servletName) {
        return (JSServletConfig) servletConfigsByName.get(servletName);
    }

    void handleRequest(JSHttpServletRequest request,
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

        Iterator mappings = servletMappings.iterator();
        while (mappings.hasNext()) {
            ServletMapping mapping = (ServletMapping) mappings.next();

            /* Should actually test with the contextpath prepended to the mapping */
            if (mapping.getUrlPattern().equals(requestURI)) {
                return mapping;
            }
        }
        return null;
    }

    /*=== ServletContext implementation ===*/
    public ServletContext getContext(String uripath) {
        return null;
    }

    public int getMajorVersion() {
        return 2;
    }

    public int getMinorVersion() {
        return 3;
    }

    public String getMimeType(String file) {
        return mimeTypes.getTypeByFileName(file);
    }

    public URL getResource(String path) throws MalformedURLException {
        return null;
    }

    public InputStream getResourceAsStream(String path) {
        return null;
    }

    public RequestDispatcher getRequestDispatcher(String path) {
        return null;
    }

    public RequestDispatcher getNamedDispatcher(String name) {
        return null;
    }

    public Servlet getServlet(String name) {
        /**
         * The servlet 2.3 spec states that this methis sould always return null
         */
        return null;
    }

    public Enumeration getServlets() {
        /**
         * The servlet 2.3 spec states that we should return an empty enumeration here
         */
        return Collections.enumeration(new ArrayList());
    }

    public Enumeration getServletNames() {
        /**
         * The servlet 2.3 spec states that we should return an empty enumeration here
         */
        return Collections.enumeration(new ArrayList());
    }

    public void log(String msg) {
        this.log.debug(msg);
    }

    public void log(Exception exception, String msg) {
        this.log.debug(msg, exception);
    }

    public void log(String message, Throwable throwable) {
        this.log.debug(message, throwable);
    }

    public String getRealPath(String path) {
        /** It is ok to return null if we serve content from a war archive which we do */
        return null;
    }

    public String getServerInfo() {
        return "JetServer/0.0";
    }

    public String getInitParameter(String name) {
        if (initParameters.containsKey(name)) {
            return (String) initParameters.get(name);
        } else {
            return null;
        }
    }

    public Enumeration getInitParameterNames() {
        return Collections.enumeration(initParameters.keySet());
    }

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public Enumeration getAttributeNames() {
        return Collections.enumeration(attributes.keySet());
    }

    public void setAttribute(String name, Object object) {
        attributes.put(name, object);
    }

    public void removeAttribute(String name) {
        attributes.remove(name);
    }
}
