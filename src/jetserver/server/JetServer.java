
package jetserver.server;

import java.io.*;
import java.net.*;
import java.util.*;

import jetserver.server.web.WebServer;

public class JetServer {

    WebServer webServer;

    public JetServer() 
	throws IOException
    {
	this.webServer = new WebServer();
	System.out.println("WebServer started on port " + webServer.getPort());
    }

    public static void main(String args[]) throws Exception {

	JetServer jetServer = new JetServer();
	System.out.println("JetServer started.");
	
	while (true) {
	    try {
		Thread.sleep(3600000);
	    } catch (InterruptedException e) {}
	}
    }
}
