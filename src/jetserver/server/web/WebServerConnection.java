
package jetserver.server.web;

import java.io.*;
import java.net.*;
import java.util.*;

import jetserver.server.config.ServerConfig;
import jetserver.util.Log;

public class WebServerConnection implements Runnable {

    private Socket socket;
    private WebContainer webContainer;

    public WebServerConnection(Socket socket, WebContainer webContainer) {
	this.socket = socket;
	this.webContainer = webContainer;
    }

    public void run() {
	try {
	    InputStream in = new BufferedInputStream(socket.getInputStream());
	    HttpRequest request = HttpRequest.decodeRequest(in);
	    
	    OutputStream out = new BufferedOutputStream(socket.getOutputStream());
	    HttpResponse response = HttpResponse.createResponse(out);
	    
	    /* Ok, now find the correct web container instance */
	    webContainer.dispatchRequest(request, response);
	    
	} catch (IOException e) {
	    Log.getInstance(this).error("Error serving web request", e);
	} finally {
	    try {
		socket.close();
	    } catch (IOException e) {}
	}
    }
}
