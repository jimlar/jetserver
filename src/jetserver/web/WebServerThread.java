
package jetserver.web;

import java.io.*;
import java.net.*;
import java.util.*;

import jetserver.config.ServerConfig;
import jetserver.web.services.WebService;

public class WebServerThread extends Thread {

    private WebServerThreadPool threadPool;
    private Socket socket;
    private int threadNumber;

    public WebServerThread(WebServerThreadPool threadPool, int threadNumber) {
	super("WebServerThread-" + threadNumber);
	this.threadPool = threadPool;
	this.threadNumber = threadNumber;
    }

    public void setSocketAndStart(Socket socket) {
	this.socket = socket;
	synchronized (this) {
	    this.notify();
	}
    }

    public int getThreadNumber() {
	return threadNumber;
    }

    public void run() {
	while (true) {
	    if (socket != null) {
		
		try {
		    InputStream in = socket.getInputStream();
		    HttpRequest request = HttpRequest.decodeRequest(in);
		    HttpResponse response = HttpResponse.createResponse(socket);
		    
		    WebService service = WebService.getServiceInstance(request);
		    service.service(request, response);
		    
		    socket.close();
		    
		} catch (IOException e) {
		    e.printStackTrace();
		}
	    }
		
	    threadPool.returnThread(this);

	    try {
		synchronized (this) {
		    this.wait();
		}
	    } catch (InterruptedException e) {
		e.printStackTrace();
	    }
	}
    }
}
