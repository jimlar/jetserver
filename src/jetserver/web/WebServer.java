
package jetserver.web;

import java.io.*;
import java.net.*;
import java.util.*;

import EDU.oswego.cs.dl.util.concurrent.PooledExecutor;

import jetserver.config.ServerConfig;
import jetserver.web.services.Dispatcher;

public class WebServer extends Thread {
    
    private int port;
    private int socketTimeout;
    private ServerSocket serverSocket;

    private PooledExecutor executor;

    private Dispatcher dispatcher;
    
    public WebServer() 
	throws IOException
    {
	ServerConfig config = ServerConfig.getInstance();

	this.socketTimeout = config.getInteger("jetserver.webserver.socket.timeout") * 1000;
	this.port = config.getInteger("jetserver.webserver.socket.port");
    	this.serverSocket = new ServerSocket(port);

	this.dispatcher = new Dispatcher();

	int maxThreads = config.getInteger("jetserver.webserver.threads.max");
	int minThreads = config.getInteger("jetserver.webserver.threads.min");
	int startThreads = config.getInteger("jetserver.webserver.threads.start");
	int keepAliveTime = config.getInteger("jetserver.webserver.threads.keep-alive-time") * 1000;
	this.executor = new PooledExecutor(maxThreads);
	this.executor.runWhenBlocked();
	this.executor.setMinimumPoolSize(minThreads);
	this.executor.setKeepAliveTime(keepAliveTime);
	this.executor.createThreads(startThreads);

	this.start();
    }

    public int getPort() {
	return this.port;
    }

    public void run() {
	while (true) {
	    Socket socket = null;
	    try {
		socket = serverSocket.accept();
		
		if (socket != null) {
		    socket.setSoTimeout(socketTimeout);
		    executor.execute(new WebServerConnection(socket, dispatcher));
		}

	    } catch (Exception e) {
		e.printStackTrace();
		hardKillSocket(socket);		
	    }
	}
    }

    private void hardKillSocket(Socket socket) {
	try {
	    if (socket != null) {
		socket.close();
	    }
	} catch (IOException e) {}
    }
}
