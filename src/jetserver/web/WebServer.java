
package jetserver.web;

import java.io.*;
import java.net.*;
import java.util.*;

import jetserver.config.ServerConfig;

public class WebServer {

    private int numberOfThreads;
    private int port;
    private ServerSocket serverSocket;
    private WebServerThreadPool threadPool;

    public WebServer() 
	throws IOException
    {
	ServerConfig config = ServerConfig.getInstance();
	this.numberOfThreads = config.getIntProperty("jetserver.webserver.threads");
	this.port = config.getIntProperty("jetserver.webserver.port");
	this.serverSocket = new ServerSocket(port);
	this.threadPool = new WebServerThreadPool(1, serverSocket);
    }

    public static void main(String args[]) throws Exception {
	WebServer webServer = new WebServer();
	System.out.println("WebServer started on port " + webServer.port);
    }
}
