
package jetserver.server.web.servlet;


public class ServletMapping {
    private String servletName;
    private String urlPattern;

    public ServletMapping(String servletName, String urlPattern) {
        this.servletName = servletName;
        this.urlPattern = urlPattern;
    }

    public String getServletName() {
        return this.servletName;
    }

    public String getUrlPattern() {
        return this.urlPattern;
    }
}


