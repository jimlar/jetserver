

package jetserver.server;

import java.io.*;
import java.util.*;

import jetserver.server.web.*;
import jetserver.server.ejb.EJBDeployer;
import jetserver.server.ear.EARConfig;
import jetserver.server.ear.Module;
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
    private WebContainer webContainer;
    private EJBDeployer ejbDeployer;

    public Deployer() throws IOException {
        this.webContainer = new WebContainer();
        this.ejbDeployer = new EJBDeployer();
        this.log = Log.getInstance(this);

        ServerConfig config = ServerConfig.getInstance();
        this.deployDir = config.getFile("jetserver.deployer.deploy-dir");
    }

    /**
     * Identify an deploy a deployable file of any supported type
     * (ejb-jar, war, ear)
     */
    public void deploy(File file) {

        try {
            EnterpriseJar jar = new EnterpriseJar(file);

            if (jar.isWebApplication()) {
                deployWebApplication(jar);

            } else if (jar.isEJBJar()) {
                deployEJBJar(jar);

            } else if (jar.isJ2EEApplication()) {
                deployEAR(jar);
            }

        } catch (IOException e) {
            log.error("Could not deploy", e);
        }
    }


    /**
     * Deploy a web application
     */
    public void deployWebApplication(EnterpriseJar jar) throws IOException {
        File applicationRoot = new File(deployDir, jar.getFile().getName());
        jar.unpackTo(applicationRoot);
        webContainer.deploy(applicationRoot);
    }

    /**
     * Deploy an EJB jar
     */
    public void deployEJBJar(EnterpriseJar jar) throws IOException {
        File ejbJarRoot = new File(deployDir, jar.getFile().getName());
        jar.unpackTo(ejbJarRoot);
        ejbDeployer.deploy(ejbJarRoot);
    }

    /**
     * Deploy an EAR
     */
    public void deployEAR(EnterpriseJar jar) throws IOException {
        File earRoot = new File(deployDir, jar.getFile().getName());
        Log.getInstance(this).info("Deploying EAR (" + earRoot + ")");

        jar.unpackTo(earRoot);
        EARConfig earConfig = new EARConfig(earRoot);
        earConfig.parse();

        Iterator ejbModules = earConfig.getEJBModules().iterator();
        while (ejbModules.hasNext()) {
            Module ejbModule = (Module) ejbModules.next();
            deploy(ejbModule.getFile());
        }

        Iterator webModules = earConfig.getWebModules().iterator();
        while (webModules.hasNext()) {
            Module webModule = (Module) webModules.next();
            deploy(webModule.getFile());
        }
    }
}
