
package jetserver.web;

import java.io.*;
import java.net.*;
import java.util.*;

public class HttpResponse {

    private OutputStream out;
    private Map headers;

    private HttpResponse(OutputStream out) {
	this.out = out;
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
	writer.write("HTTP/1.0 200 OK\r\n");

	Iterator iter = headers.keySet().iterator();
	while (iter.hasNext()) {
	    String headerName = (String) iter.next();
	    writer.write(headerName + ": " + headers.get(headerName) + "\r\n");
	}
	
	writer.write("\r\n");
	writer.flush();
    }

    public static HttpResponse createResponse(Socket socket) throws IOException {
	return new HttpResponse(socket.getOutputStream());
    }
}
