
package jetserver.web;

import java.io.*;
import java.net.*;
import java.util.*;

import jetserver.config.ServerConfig;
import jetserver.web.services.WebService;

public class WebServerThread implements Runnable {

    private Socket socket;

    public WebServerThread(Socket socket) {
	this.socket = socket;
    }

    public void run() {
	try {
	    InputStream in = new BufferedInputStream(socket.getInputStream());
	    HttpRequest request = HttpRequest.decodeRequest(in);
	    
	    OutputStream out = new BufferedOutputStream(socket.getOutputStream());
	    HttpResponse response = HttpResponse.createResponse(out);
	    
	    WebService service = WebService.getServiceInstance(request);
	    service.service(request, response);
	    
	    socket.close();

	} catch (IOException e) {
	    e.printStackTrace();
	    
	} 
    }
}
