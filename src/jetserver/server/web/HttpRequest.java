
package jetserver.server.web;

import java.io.*;
import java.net.*;
import java.util.*;

import jetserver.util.Strings;

public class HttpRequest {

    private static final int MAX_HEADER_LENGTH = 2048;
    private static final byte ASCII_CR = 0xd;
    private static final byte ASCII_LF = 0xa;

    private byte readLineBuffer[] = new byte[MAX_HEADER_LENGTH];
    private String method;
    private String uri;
    private String protocol;
    private Map headers;
    
    private HttpRequest(InputStream in) 
	throws IOException
    {
	String line = readLine(in);

	int i = line.indexOf(" ");
	this.method = line.substring(0, i);

	int j = line.indexOf(" ", i + 1);
	this.uri = line.substring(i + 1, j);
	this.protocol = line.substring(j + 1);
	    
	/* Fetch headers */
	this.headers = new HashMap();
	line = readLine(in);
	while (line != null && !line.equals("")) {
	    
	    i = line.indexOf(":");
	    String headerKey = line.substring(0, i);
	    String headerValue = line.substring(i + 2);		
	    this.headers.put(headerKey, headerValue);
		
	    line = readLine(in);
	}	
    }

    public String getURI() {
	return uri;
    }

    public String getMethod() {
	return this.method;
    }

    public String getProtocol() {
	return this.protocol;
    }



    public String toString() {
	return "[HttpRequest method=" + method + ", uri=" + uri + ", protocol=" + protocol + "]";
    }

    public static HttpRequest decodeRequest(InputStream in) throws IOException {
	return new HttpRequest(in);
    }

    private String readLine(InputStream in) 
	throws IOException 
    {
	int totalRead = 0;
	byte b[] = new byte[1];

	int numRead = in.read(b);
	while (numRead != -1 && numRead != 0 && b[0] != ASCII_LF && b[0] != ASCII_CR) {
	    readLineBuffer[totalRead] = b[0];
	    totalRead += numRead;
	    numRead = in.read(b);
	}
	
	/* if last char was a CR, read the LF */
	if (b[0] == ASCII_CR) {
	    in.read(b);
	}
	
	return Strings.toStringFromAscii(readLineBuffer, 0, totalRead);
    }
}
