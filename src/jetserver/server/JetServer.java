
package jetserver.server;

import java.io.*;
import java.net.*;
import java.util.*;

import jetserver.server.web.WebServer;
import jetserver.server.deploymanager.DeployManager;

public class JetServer {

    private WebServer webServer;
    private DeployManager deployManager;

    public JetServer() 
	throws IOException
    {
	this.webServer = new WebServer();
	this.deployManager = new DeployManager(webServer.getWebContainerManager());
	System.out.println("JetServer started.");

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
