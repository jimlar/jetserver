
package jetserver.server.web.config;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import java.util.Enumeration;


public class JSServletConfig implements ServletConfig {
    private String servletName;
    private String className;

    public JSServletConfig(String servletName, String className) {
        this.servletName = servletName;
        this.className = className;
    }

    public String getClassName() {
        return this.className;
    }

    /*=== ServletConfig implementation ===*/
    public ServletContext getServletContext() {
        return null;
    }

    public String getInitParameter(String name) {
        return null;
    }

    public Enumeration getInitParameterNames() {
        return null;
    }

    public String getServletName() {
        return this.servletName;
    }
}

