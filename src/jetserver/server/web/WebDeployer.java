

package jetserver.server.web;

import java.io.*;
import java.util.*;

import jetserver.util.Log;
import jetserver.server.application.Application;
import jetserver.server.web.config.WebApplicationFactory;

public class WebDeployer {

    private Log log;

    public WebDeployer() throws IOException {
        this.log = Log.getInstance(this);
    }

    /**
     * Activate a web application (deploy) if nessecary
     */
    public WebApplication deploy(File applicationRoot, Application application) throws IOException {
        log.info("Deploying webapp in dir " + applicationRoot);

        WebApplication webApplication = WebApplicationFactory.createWebApplication(application, applicationRoot);
        application.addWebApplication(webApplication);

        log.info("Deployed " + webApplication.getDisplayName());
        return webApplication;
    }
}
