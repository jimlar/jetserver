
package jetserver.server;

import java.io.*;
import java.net.*;
import java.util.*;

import jetserver.server.web.WebServer;
import jetserver.server.deploymanager.DeployManager;
import jetserver.util.Log;

public class JetServer {

    private DeployManager deployManager;
    private ContainerManager containerManager;

    public JetServer() 
	throws IOException
    {
	this.containerManager = new ContainerManager();
	this.deployManager = new DeployManager(containerManager);
	Log.getInstance(this).info("JetServer started.");

	this.deployManager.start();
    }

    public static void main(String args[]) throws Exception {

	JetServer jetServer = new JetServer();
	
	while (true) {
	    try {
		Thread.sleep(3600000);
	    } catch (InterruptedException e) {}
	}
    }
}
