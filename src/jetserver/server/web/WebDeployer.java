

package jetserver.server.web;

import java.io.*;
import java.util.*;

import jetserver.util.Log;
import jetserver.server.web.config.*;
import jetserver.server.application.Application;

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

        WebAppConfig config = WebAppConfigFactory.decode(applicationRoot);
        WebApplication webApplication = new WebApplication(application, config);
        application.addWebApplication(webApplication);

        log.info("Deployed " + config.getDisplayName());
        return webApplication;
    }
}