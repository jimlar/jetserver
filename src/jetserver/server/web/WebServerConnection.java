
package jetserver.server.web;

import java.io.*;
import java.net.*;
import java.util.*;

import jetserver.config.ServerConfig;
import jetserver.server.web.services.Dispatcher;

public class WebServerConnection implements Runnable {

    private Socket socket;
    private Dispatcher dispatcher;

    public WebServerConnection(Socket socket, Dispatcher dispatcher) {
	this.socket = socket;
	this.dispatcher = dispatcher;
    }

    public void run() {
	try {
	    InputStream in = new BufferedInputStream(socket.getInputStream());
	    HttpRequest request = HttpRequest.decodeRequest(in);
	    
	    OutputStream out = new BufferedOutputStream(socket.getOutputStream());
	    HttpResponse response = HttpResponse.createResponse(out);
	    
	    dispatcher.dispatch(request, response);
	    
	} catch (IOException e) {
	    e.printStackTrace();	    
	} finally {
	    try {
		socket.close();
	    } catch (IOException e) {}
	}
    }
}
