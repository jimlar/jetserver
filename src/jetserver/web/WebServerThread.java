
package jetserver.web;

import java.io.*;
import java.net.*;
import java.util.*;

import jetserver.config.ServerConfig;

public class WebServerThread extends Thread {

    private static final File baseDir = ServerConfig.getInstance().getFileProperty("jetserver.webserver.root");

    private WebServerThreadPool threadPool;
    private byte outputBuffer[] = new byte[1024];
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
		    
		    File requestedFile = new File(baseDir, request.getURI());
		    if (requestedFile.exists()) {
			
			response.setContentType("text/html");
			response.setContentLength((int) requestedFile.length());
			
			OutputStream out = response.getOutputStream();
			InputStream fileIn = new FileInputStream(requestedFile);
			int readLen = 0;
			
			while (readLen != -1) {
			    readLen = fileIn.read(outputBuffer);
			    if (readLen != -1) {
				out.write(outputBuffer, 0, readLen);
			    }
			}
			
			fileIn.close();
			out.close();
		    }
		    
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
