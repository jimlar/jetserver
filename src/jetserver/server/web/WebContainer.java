

package jetserver.server.web;

import java.io.*;
import java.util.*;

import jetserver.util.Log;
import jetserver.server.web.config.*;

public class WebContainer {

    private WebServer webServer;
    private Collection webApplications;

    private Log log;

    public WebContainer() throws IOException {
        this.webApplications = new ArrayList();
        this.webServer = new WebServer(this);
        this.webServer.start();
        this.log = Log.getInstance(this);

        log.info("initialized");
    }

    /**
     * Dispatch an incoming request to the right web application
     */
    void dispatchRequest(HttpRequest request, HttpResponse response) throws IOException {
        WebApplication application = findApplication(request);
        if (application != null) {
            application.service(request, response);
        } else {
            log.info("No application mapped for request: " + request.getURI());
        }
    }

    /**
     * Activate a web application (deploy) if nessecary
     */
    public void deploy(File applicationRoot) throws IOException {
        log.info("Deploying webapp in dir " + applicationRoot);

        WebAppConfig config = WebAppConfigFactory.decode(applicationRoot);
        WebApplication webApplication = new WebApplication(config);
        webApplications.add(webApplication);

        log.info("Deployed " + config.getDisplayName());
        System.out.println("Deployed " + config.getDisplayName());
    }

    /**
     * Find the application handling a request
     */
    private WebApplication findApplication(HttpRequest request) {

        /* This is TOOO slow, change it later on */
        Iterator iter = webApplications.iterator();
        while (iter.hasNext()) {
            WebApplication application = (WebApplication) iter.next();
            if (request.getURI().startsWith(application.getConfig().getHttpRoot())) {
                return application;
            }
        }
        return null;
    }   
}
