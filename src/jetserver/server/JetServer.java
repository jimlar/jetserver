
package jetserver.server;

import java.io.*;
import java.net.*;
import java.util.*;

import jetserver.server.web.WebServer;
import jetserver.util.Log;

public class JetServer {

    private DropZoneWatch dropZoneWatch;
    private ContainerManager containerManager;

    public JetServer() 
	throws IOException
    {
	this.containerManager = new ContainerManager();
	this.dropZoneWatch = new DropZoneWatch(containerManager);
	this.dropZoneWatch.start();

	Log.getInstance(this).info("started");
	System.out.println("Jetserver started");
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
