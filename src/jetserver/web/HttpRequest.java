
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

	BufferedReader reader = new BufferedReader(new InputStreamReader(in));
	
	String line = reader.readLine();
	StringTokenizer st = new StringTokenizer(line, " ");
	String method = st.nextToken().toUpperCase();
	String uri = st.nextToken();
	String protocol = st.nextToken();
	    
	/* Fetch headers */
	Map headers = new HashMap();
	line = reader.readLine();
	while (line != null && !line.equals("")) {
	    
	    int i = line.indexOf(":");
	    String headerKey = line.substring(0, i);
	    String headerValue = line.substring(i + 2);		
	    headers.put(headerKey, headerValue);
		
	    line = reader.readLine();
	}
	
	return new HttpRequest(method, uri, protocol, headers);
    }
}
