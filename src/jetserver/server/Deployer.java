
package jetserver.server;

import jetserver.server.config.ServerConfig;
import jetserver.server.ejb.EJBDeployer;
import jetserver.server.web.WebApplication;
import jetserver.server.web.WebDeployer;
import jetserver.util.EnterpriseJar;
import jetserver.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * This class manages all containers in JetServer, you can add all types of supported
 * applications to this manager to activate them
 */

public class Deployer {

    private Log log;

    private File deployDir;
    private WebDeployer webDeployer;
    private EJBDeployer ejbDeployer;

    private Collection applications;
    private Collection webApplications;

    public Deployer() throws IOException {
        this.webDeployer = new WebDeployer();
        this.ejbDeployer = new EJBDeployer();
        this.applications = new ArrayList();
        this.webApplications = new ArrayList();
        this.log = Log.getInstance(this);

        ServerConfig config = ServerConfig.getInstance();
        this.deployDir = config.getDeployDir();
    }

    /**
     * return all known web-apps regardless of which application they are
     * part of (useful for web request dispatching)
     */
    public Collection getAllWebApplications() {
        return this.webApplications;
    }

    /**
     * Identify an deploy a deployable file of any supported type
     * (ejb-jar, war, ear)
     */
    public void deploy(File file) {

        try {
            EnterpriseJar jar = new EnterpriseJar(file);
            Application newApplication = null;

            if (!jar.isValid()) {
                log.error("The file " + file.getAbsoluteFile() + " is not a valid JAR");
                return;
            }

            if (jar.isJ2EEApplication()) {
                newApplication = deployEAR(jar);

            } else if (jar.isWebApplication()) {
                newApplication = deployWebApplication(jar);

            } else if (jar.isEJBJar()) {
                newApplication = deployEJBJar(jar);
            }

            if (newApplication != null) {
                this.applications.add(newApplication);
            }

        } catch (IOException e) {
            log.error("Could not deploy", e);
        }
    }


    /**
     * Deploy a stand alone web application
     */
    private Application deployWebApplication(EnterpriseJar jar) throws IOException {
        Application application = Application.createEmpty(deployDir);
        return deployWebApplication(application, jar);
    }

    /**
     * Deploy a web application as part of an application
     */
    private Application deployWebApplication(Application application,
                                             EnterpriseJar jar) throws IOException {
        File webAppDir = new File(application.getDeployDir(),
                                  stripSuffix(jar.getFile().getName()));
        jar.unpackTo(webAppDir);
        WebApplication webApp = webDeployer.deploy(webAppDir, application);
        this.webApplications.add(webApp);
        return application;
    }

    /**
     * Deploy a standalone EJB jar
     */
    private Application deployEJBJar(EnterpriseJar jar) throws IOException {
        Application application = Application.createEmpty(deployDir);
        return deployEJBJar(application, jar);
    }

    /**
     * Deploy an EJB jar as part of an application
     */
    private Application deployEJBJar(Application application,
                                     EnterpriseJar jar) throws IOException {
        File ejbJarDir = new File(application.getDeployDir(),
                                  stripSuffix(jar.getFile().getName()));
        jar.unpackTo(ejbJarDir);
        ejbDeployer.deploy(ejbJarDir, application);
        return application;
    }

    /**
     * Deploy an EAR
     */
    private Application deployEAR(EnterpriseJar jar) throws IOException {
        File earDir = new File(deployDir, jar.getFile().getName());
        Log.getInstance(this).info("Deploying EAR (" + jar + ")");

        jar.unpackTo(earDir);
        Application application = Application.createFromEARFile(earDir);

        Iterator ejbModules = application.getEJBModules().iterator();
        while (ejbModules.hasNext()) {
            ApplicationModule ejbModule = (ApplicationModule) ejbModules.next();
            deployEJBJar(application, new EnterpriseJar(ejbModule.getFile()));
        }

        Iterator webModules = application.getWebModules().iterator();
        while (webModules.hasNext()) {
            ApplicationModule webModule = (ApplicationModule) webModules.next();
            deployWebApplication(application, new EnterpriseJar(webModule.getFile()));
        }
        return application;
    }

    private String stripSuffix(String filename) {
        int i = filename.lastIndexOf(".");
        if (i != -1) {
            return filename.substring(0, i);
        }
        return filename;
    }
}
