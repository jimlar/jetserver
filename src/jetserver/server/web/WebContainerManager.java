

package jetserver.server.web;

import java.io.*;
import java.util.*;

public class WebContainerManager {

    private Collection webContainers;

    
    public WebContainerManager() {
	this.webContainers = new ArrayList();
    }

    /**
     * Dispatch an incoming request to the right web container instance
     */
    public void dispatchRequest(HttpRequest request, HttpResponse response) throws IOException {
	WebContainer container = findContainer(request);
	if (container != null) {
	    container.service(request, response);
	} else {
	    System.out.println("No container active for request: " + request.getURI());
	}
    }

    /**
     * Activate a web application (deploy) if nessecary
     */
    public void deployWebApplication(File applicationRoot) throws IOException {
	System.out.println("activating: " + applicationRoot);
	webContainers.add(new WebContainer("/", applicationRoot));
    }

    /**
     * Find the container handling a request
     */
    private WebContainer findContainer(HttpRequest request) {

	/* This is TOOO slow, change it later on */
	Iterator iter = webContainers.iterator();
	while (iter.hasNext()) {
	    WebContainer container = (WebContainer) iter.next();
	    if (request.getURI().startsWith(container.getHttpRoot())) {
		return container;
	    }
	}
	return null;
    }   
}
