
package jetserver.web;

import java.io.*;
import java.net.*;
import java.util.*;

import jetserver.util.Strings;

public class HttpResponse {

    private static final String NEWLINE = "\r\n";
    
    private OutputStream out;
    private String protocol;
    private String statusMessage;
    private int statusCode;
    private Map headers;
    
    public static HttpResponse createResponse(OutputStream out)
	throws IOException 
    {
	return new HttpResponse(out);
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

	StringBuffer buffer = new StringBuffer(1024);

	buffer.append(this.protocol);
	buffer.append(" ");
	buffer.append(statusCode);
	buffer.append(" ");
	buffer.append(statusMessage);
	buffer.append(NEWLINE);

	Iterator iter = headers.keySet().iterator();
	while (iter.hasNext()) {
	    String headerName = (String) iter.next();
	    buffer.append(headerName);
	    buffer.append(": ");
	    buffer.append(headers.get(headerName));
	    buffer.append(NEWLINE);
	}
	
	buffer.append(NEWLINE);

	out.write(Strings.getAsciiBytes(buffer.toString()));
    }
}
