

package jetserver.server;

import java.io.*;
import java.util.*;

import jetserver.server.web.*;
import jetserver.server.ejb.EJBContainer;

/**
 * This class manages all containers in JetServer, you can add all types of supported
 * applications to this manager to activate them
 */

public class ContainerManager {

    private WebContainer webContainer;
    private EJBContainer ejbContainer;
    
    public ContainerManager() throws IOException {
        this.webContainer = new WebContainer();
        this.ejbContainer = new EJBContainer();
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
        ejbContainer.deploy(jarRoot);
    }
}
