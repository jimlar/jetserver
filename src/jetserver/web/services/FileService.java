

package jetserver.web.services;

import java.io.*;
import java.net.*;
import java.util.*;

import jetserver.web.*;
import jetserver.config.ServerConfig;

class FileService extends WebService {

    private final File baseDir;
    private final int bufferSize;

    public FileService() {
	ServerConfig config = ServerConfig.getInstance();
	this.baseDir = config.getFileProperty("jetserver.webserver.root");
	this.bufferSize = config.getIntProperty("jetserver.webserver.buffersize");
    }

    public void service(HttpRequest request, HttpResponse response) 
	throws IOException
    {	
	File requestedFile = new File(baseDir, request.getURI());
	if (requestedFile.exists()) {
	    
	    response.setContentType("text/html");
	    response.setContentLength((int) requestedFile.length());
	    
	    OutputStream out = response.getOutputStream();
	    InputStream fileIn = new FileInputStream(requestedFile);
	    byte outputBuffer[] = new byte[bufferSize];
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
    }
}
