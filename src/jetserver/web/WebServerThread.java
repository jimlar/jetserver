
package jetserver.web;

import java.io.*;
import java.net.*;
import java.util.*;

public class WebServerThread implements Runnable {

    private byte outputBuffer[] = new byte[1024];
    private Socket socket;
    private static final File baseDir = new File("/home/jimmy/orion/default-web-app");

    public WebServerThread(Socket socket) {
	this.socket = socket;
    }

    public void run() {
	try {
	    InputStream in = socket.getInputStream();
	    HttpRequest request = HttpRequest.decodeRequest(in);
	    HttpResponse response = HttpResponse.createResponse(socket);
	    

	    File requestedFile = new File(baseDir, request.getURI());
	    if (requestedFile.exists()) {
		
		response.setContentType("text/html");
		response.setContentLength((int) requestedFile.length());

		OutputStream out = response.getOutputStream();
		InputStream fileIn = new FileInputStream(requestedFile);
		int readLen = 0;

		while (readLen != -1) {
		    readLen = fileIn.read(outputBuffer);
		    if (readLen != -1) {
			out.write(outputBuffer, 0, readLen);
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
}
