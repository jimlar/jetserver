
package jetserver.server.web.config;


public class ServletMapping {
    private String servletName;
    private String urlPattern;
    
    ServletMapping(String servletName, String urlPattern) {
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
    

