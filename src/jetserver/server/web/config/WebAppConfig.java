
package jetserver.server.web.config;

import java.io.*;
import java.util.*;

public class WebAppConfig {

    private String displayName;
    private String httpRoot;
    private File fileRoot;
    private Collection welcomeFiles = new ArrayList();    
 
    private List servletMappings = new ArrayList();
    private Map servletDeclarationsByName = new HashMap();

    
    WebAppConfig(String displayName,
		 File fileRoot, 
		 String httpRoot, 
		 Collection welcomeFiles,
		 Collection servletDeclarations,
		 List servletMappings) {
	
	this.displayName = displayName;
	this.httpRoot = httpRoot;
	this.fileRoot = fileRoot;
	this.welcomeFiles = welcomeFiles;
	
	this.servletMappings = servletMappings;
	this.servletDeclarationsByName = new HashMap();
	
	Iterator iter = servletDeclarations.iterator();
	while (iter.hasNext()) {
	    ServletDeclaration d = (ServletDeclaration) iter.next();
	    servletDeclarationsByName.put(d.getName(), d);
	}
    } 

    public String getDisplayName() {
	return this.displayName;
    }

    public String getHttpRoot() {
	return this.httpRoot;
    }

    public File getFileRoot() {
	return this.fileRoot;
    }

    public Collection getWelcomeFiles() {
	return this.welcomeFiles;
    }

    public List getServletMappings() {
	return this.servletMappings;
    }

    public ServletDeclaration getServletDeclaration(String servletName) {
	return (ServletDeclaration) this.servletDeclarationsByName.get(servletName);
    }
}
