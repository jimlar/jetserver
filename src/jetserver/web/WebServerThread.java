
package jetserver.web;

import java.io.*;
import java.net.*;
import java.util.*;

import jetserver.config.ServerConfig;
import jetserver.web.services.WebService;

public class WebServerThread extends Thread {

    private WebServerThreadPool threadPool;
    private ServerSocket serverSocket;
    private Socket socket;
    private int threadNumber;

    public WebServerThread(WebServerThreadPool threadPool, 
			   int threadNumber, 
			   ServerSocket serverSocket) 
    {
	super("WebServerThread-" + threadNumber);
	this.threadPool = threadPool;
	this.threadNumber = threadNumber;
	this.serverSocket = serverSocket;
    }

    public int getThreadNumber() {
	return threadNumber;
    }

    public void run() {

	while (true) {
		
	    try {
		socket = serverSocket.accept();
		threadPool.markThreadBusy(this);
		
		InputStream in = new BufferedInputStream(socket.getInputStream());
		HttpRequest request = HttpRequest.decodeRequest(in);

		OutputStream out = new BufferedOutputStream(socket.getOutputStream());
		HttpResponse response = HttpResponse.createResponse(out);

		WebService service = WebService.getServiceInstance(request);
		service.service(request, response);

		in.close();
		out.close();
		socket.close();
		
	    } catch (IOException e) {
		e.printStackTrace();
	    }

	    threadPool.releaseThread(this);	    
	}
    }
}
