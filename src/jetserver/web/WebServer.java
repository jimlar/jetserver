
package jetserver.web;

import java.io.*;
import java.net.*;
import java.util.*;

public class WebServer extends Thread {

    private Socket socket;
    private File baseDir = new File("/home/httpd/html");

    public WebServer(Socket socket) {
	this.socket = socket;
	this.start();
    }

    public void run() {

	try {
	    InputStream in = socket.getInputStream();
	    HttpRequest request = HttpRequest.decodeRequest(in);

	    System.out.println("Got request: " + request);

	    HttpResponse response = HttpResponse.createResponse(socket);
	    

	    File requestedFile = new File(baseDir, request.getURI());
	    if (requestedFile.exists()) {
		
		response.setContentType("text/html");
		response.setContentLength((int) requestedFile.length());

		OutputStream out = response.getOutputStream();
		InputStream fileIn = new FileInputStream(requestedFile);
		byte buf[] = new byte[1024];
		int readLen = 0;

		while (readLen != -1) {
		    readLen = fileIn.read(buf);
		    if (readLen != -1) {
			out.write(buf, 0, readLen);
		    }
		}

		fileIn.close();
		out.close();
	    }

	    socket.close();

	} catch (IOException e) {
	    e.printStackTrace();
	}
    }

    public static void main(String args[]) throws Exception {
	
	ServerSocket serverSocket = new ServerSocket(8080);
	while(true) {
	    new WebServer(serverSocket.accept());
	}
    }
}
