
package jetserver.server.web.config;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.util.Enumeration;
import java.util.Map;
import java.util.HashMap;
import java.util.Collections;


public class JSServletConfig implements ServletConfig {
    private String servletName;
    private String className;
    private boolean loadOnStartup;
    private int loadPriority;
    private Map initParameters;

    public JSServletConfig(String servletName, String className, boolean loadOnStartup, int loadPriority) {
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
        return null;
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

