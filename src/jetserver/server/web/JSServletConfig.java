package jetserver.server.web;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.util.*;


class JSServletConfig implements ServletConfig {

    private WebApplication webApplication;
    private String servletName;
    private String className;
    private boolean loadOnStartup;
    private int loadPriority;
    private Map initParameters;

    public JSServletConfig(WebApplication webApplication, String servletName, String className, boolean loadOnStartup, int loadPriority) {
        this.webApplication = webApplication;
        this.servletName = servletName;
        this.className = className;
        this.loadOnStartup = loadOnStartup;
        this.loadPriority = loadPriority;
        this.initParameters = new HashMap();
    }

    public String getClassName() {
        return this.className;
    }

    public boolean loadOnStartup() {
        return this.loadOnStartup;
    }

    public int loadPriority() {
        return this.loadPriority;
    }

    public void addInitParameter(String name, String value) {
        this.initParameters.put(name, value);
    }

    /*=== ServletConfig implementation ===*/
    public ServletContext getServletContext() {
        return webApplication;
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

    public String getServletName() {
        return this.servletName;
    }
}

