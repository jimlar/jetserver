
package jetserver.web;

import java.io.*;
import java.net.*;
import java.util.*;

import jetserver.config.ServerConfig;

public class WebServer extends Thread {

    private int port;
    private ServerSocket serverSocket;
    private WebServerThreadPool threadPool;

    public WebServer(int port) {
	this.port = port;
	this.start();
    }

    public void run() {

	try {
	    this.serverSocket = new ServerSocket(port);
	    this.threadPool = new WebServerThreadPool(50);

	    while (true) {
		threadPool.startThread(serverSocket.accept());
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public static void main(String args[]) throws Exception {
	WebServer webServer = new WebServer(ServerConfig.getInstance().getIntProperty("jetserver.webserver.port"));
    }
}
