
package jetserver.web;

import java.io.*;
import java.net.*;
import java.util.*;

import jetserver.config.ServerConfig;

public class WebServer {

    private int port;
    private ServerSocket serverSocket;
    private WebServerThreadPool threadPool;

    public WebServer(int port) 
	throws IOException
    {
	this.port = port;
	this.serverSocket = new ServerSocket(port);
	this.threadPool = new WebServerThreadPool(1, serverSocket);
    }

    public static void main(String args[]) throws Exception {
	WebServer webServer = new WebServer(ServerConfig.getInstance().getIntProperty("jetserver.webserver.port"));
    }
}
