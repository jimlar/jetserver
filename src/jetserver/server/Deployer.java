

package jetserver.server;

import java.io.*;
import java.util.*;

import jetserver.server.web.*;
import jetserver.server.ejb.EJBDeployer;

/**
 * This class manages all containers in JetServer, you can add all types of supported
 * applications to this manager to activate them
 */

public class Deployer {

    private WebContainer webContainer;
    private EJBDeployer ejbDeployer;
    
    public Deployer() throws IOException {
        this.webContainer = new WebContainer();
        this.ejbDeployer = new EJBDeployer();
    }

    /**
     * Deploy a web application
     */
    public void deployWebApplication(File applicationRoot) throws IOException {
        webContainer.deploy(applicationRoot);
    }

    /**
     * Deploy an EJB jar
     */
    public void deployEJBJar(File jarRoot) throws IOException {
        ejbDeployer.deploy(jarRoot);
    }
}
