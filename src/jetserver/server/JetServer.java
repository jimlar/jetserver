
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
	this.deployManager = new DeployManager();
	this.webServer = new WebServer();
	System.out.println("JetServer started.");
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
