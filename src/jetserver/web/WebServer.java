
package jetserver.web;

import java.io.*;
import java.net.*;
import java.util.*;

import jetserver.util.*;

public class WebServer extends Thread {

    private int port;
    private ServerSocket serverSocket;
    private ThreadPool threadPool;

    public WebServer(int port) {
	this.port = port;
	this.start();
    }

    public void run() {

	try {
	    this.serverSocket = new ServerSocket(port);
	    this.threadPool = new ThreadPool(50);

	    while (true) {
		WebServerThread wst = new WebServerThread(serverSocket.accept());

		//Thread thread = new Thread(wst);
		//thread.start();

		threadPool.startRunnable(wst);
	    }
	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public static void main(String args[]) throws Exception {
	WebServer webServer = new WebServer(8080);
    }
}
