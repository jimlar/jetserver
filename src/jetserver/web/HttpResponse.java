
package jetserver.web;

import java.io.*;
import java.net.*;
import java.util.*;

public class HttpResponse {
    
    private OutputStream out;
    private String protocol;
    private String statusMessage;
    private int statusCode;
    private Map headers;
    
    public static HttpResponse createResponse(Socket socket) 
	throws IOException 
    {
	return new HttpResponse(socket.getOutputStream());
    }

    private HttpResponse(OutputStream out) {
	this.out = out;
	this.protocol = "HTTP/1.0";
	this.statusCode = 200;
	this.statusMessage = "OK";
	this.headers = new HashMap();
	this.headers.put("Server", "jetserver");
    }

    public OutputStream getOutputStream() throws IOException {
	flushHeaders();
	return out;
    }

    public void setContentLength(int length) {
	this.headers.put("Content-length", "" + length);
    }
    
    public void setContentType(String type) {
	this.headers.put("Content-type", type);
    }

    private void flushHeaders() throws IOException {

	Writer writer = new OutputStreamWriter(out);
	writer.write(this.protocol 
		     + " " + statusCode 
		     + " " + statusMessage + "\r\n");

	Iterator iter = headers.keySet().iterator();
	while (iter.hasNext()) {
	    String headerName = (String) iter.next();
	    writer.write(headerName + ": " + headers.get(headerName) + "\r\n");
	}
	
	writer.write("\r\n");
	writer.flush();
    }
}
