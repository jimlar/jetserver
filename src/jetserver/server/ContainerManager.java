

package jetserver.server;

import java.io.*;
import java.util.*;

import jetserver.server.web.*;

/**
 * This class manages all containers in JetServer, you can add all types of supported
 * applications to this manager to activate them
 */

public class ContainerManager {

    private WebContainer webContainer;

    
    public ContainerManager() throws IOException {
	this.webContainer = new WebContainer();
    }

    /**
     * Deploy a web application
     */
    public void deployWebApplication(File applicationRoot) throws IOException {
	webContainer.deploy(applicationRoot);
    }
}
