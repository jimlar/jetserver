
package jetserver.web;

import java.io.*;
import java.net.*;
import java.util.*;

public class HttpRequest {

    private String method;
    private String uri;
    private String protocol;
    private Map headers;

    private HttpRequest(String method, String uri, String protocol, Map headers) {
	this.method = method;
	this.uri = uri;
	this.protocol = protocol;
	this.headers = headers;
    }

    public String getURI() {
	return uri;
    }

    public String toString() {
	return "[HttpRequest method=" + method + ", uri=" + uri + ", protocol=" + protocol + "]";
    }

    public static HttpRequest decodeRequest(InputStream in) throws IOException {

	String line = readLine(in);
	
	int i = line.indexOf(" ");
	String method = line.substring(0, i);

	int j = line.indexOf(" ", i + 1);
	String uri = line.substring(i + 1, j);
	String protocol = line.substring(j + 1);
	    
	/* Fetch headers */
	Map headers = new HashMap();
	line = readLine(in);
	while (line != null && !line.equals("")) {
	    
	    i = line.indexOf(":");
	    String headerKey = line.substring(0, i);
	    String headerValue = line.substring(i + 2);		
	    headers.put(headerKey, headerValue);
		
	    line = readLine(in);
	}
	
	return new HttpRequest(method, uri, protocol, headers);
    }

    private static String readLine(InputStream in) 
	throws IOException 
    {
	StringBuffer buffer = new StringBuffer();

	int readChar = in.read();
	while (readChar != -1 && readChar != '\r' && readChar != '\n') {
	    buffer.append((char) readChar);
	    readChar = in.read();
	}

	/* if last char was a CR, read the LF */
	if (readChar == '\r') {
	    in.read();
	}

	return buffer.toString();
    }
}
