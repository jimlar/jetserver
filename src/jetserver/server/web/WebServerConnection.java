
package jetserver.server.web;

import java.io.*;
import java.net.*;
import java.util.*;

import jetserver.config.ServerConfig;

public class WebServerConnection implements Runnable {

    private Socket socket;
    private WebContainerManager webContainerManager;

    public WebServerConnection(Socket socket, WebContainerManager webContainerManager) {
	this.socket = socket;
	this.webContainerManager = webContainerManager;
    }

    public void run() {
	try {
	    InputStream in = new BufferedInputStream(socket.getInputStream());
	    HttpRequest request = HttpRequest.decodeRequest(in);
	    
	    OutputStream out = new BufferedOutputStream(socket.getOutputStream());
	    HttpResponse response = HttpResponse.createResponse(out);
	    
	    /* Ok, now find the correct web container instance */
	    webContainerManager.dispatchRequest(request, response);
	    
	} catch (IOException e) {
	    e.printStackTrace();	    
	} finally {
	    try {
		socket.close();
	    } catch (IOException e) {}
	}
    }
}
