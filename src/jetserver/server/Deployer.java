

package jetserver.server;

import java.io.*;
import java.util.*;

import jetserver.server.web.*;
import jetserver.server.ejb.EJBDeployer;
import jetserver.server.application.ApplicationConfig;
import jetserver.server.application.Module;
import jetserver.server.application.Application;
import jetserver.server.config.ServerConfig;
import jetserver.util.Log;
import jetserver.util.EnterpriseJar;

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
        this.deployDir = config.getFile("jetserver.deployer.deploy-dir");
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
        Application application = new Application(ApplicationConfig.createEmptyConfig());
        return deployWebApplication(application, jar);
    }

    /**
     * Deploy a web application as part of an application
     */
    private Application deployWebApplication(Application application,
                                            EnterpriseJar jar) throws IOException {
        File webAppRoot = new File(deployDir, jar.getFile().getName());
        jar.unpackTo(webAppRoot);
        WebApplication webApp = webDeployer.deploy(webAppRoot,
                                                   application);
        this.webApplications.add(webApp);
        return application;
    }

    /**
     * Deploy a standalone EJB jar
     */
    private Application deployEJBJar(EnterpriseJar jar) throws IOException {
        Application application = new Application(ApplicationConfig.createEmptyConfig());
        return deployEJBJar(application, jar);
    }

    /**
     * Deploy an EJB jar as part of an application
     */
    private Application deployEJBJar(Application application,
                             EnterpriseJar jar) throws IOException {
        File ejbJarRoot = new File(deployDir, jar.getFile().getName());
        jar.unpackTo(ejbJarRoot);
        ejbDeployer.deploy(ejbJarRoot, application);
        return application;
    }

    /**
     * Deploy an EAR
     */
    private Application deployEAR(EnterpriseJar jar) throws IOException {
        File earRoot = new File(deployDir, jar.getFile().getName());
        Log.getInstance(this).info("Deploying EAR (" + earRoot + ")");

        jar.unpackTo(earRoot);
        ApplicationConfig applicationConfig = ApplicationConfig.createFromEARFile(earRoot);

        Application application = new Application(applicationConfig);

        Iterator ejbModules = applicationConfig.getEJBModules().iterator();
        while (ejbModules.hasNext()) {
            Module ejbModule = (Module) ejbModules.next();
            deployEJBJar(application, new EnterpriseJar(ejbModule.getFile()));
        }

        Iterator webModules = applicationConfig.getWebModules().iterator();
        while (webModules.hasNext()) {
            Module webModule = (Module) webModules.next();
            deployWebApplication(application, new EnterpriseJar(webModule.getFile()));
        }
        return application;
    }
}
