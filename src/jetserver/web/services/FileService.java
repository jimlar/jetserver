

package jetserver.web.services;

import java.io.*;
import java.net.*;
import java.util.*;

import jetserver.web.*;
import jetserver.config.ServerConfig;

class FileService extends WebService {

    private static final String BUFFERSIZE_PROPERTY = "jetserver.webserver.buffersize";

    private final int bufferSize;
    private final FileInfoCache fileInfoCache;

    public FileService() throws IOException {
	ServerConfig config = ServerConfig.getInstance();
	this.bufferSize = config.getIntProperty(BUFFERSIZE_PROPERTY);
	this.fileInfoCache = new FileInfoCache();
    }

    public void service(HttpRequest request, HttpResponse response) 
	throws IOException
    {	
	FileInfo fileInfo = fileInfoCache.getFileInfo(request);

	if (fileInfo.fileExists()) {
	    
	    /* Try welcomefiles if a directory is requested */
	    if (fileInfo.isDirectoryIndexRequest()) {		
		sendDirectoryIndexResponse(request, response);
		return;			
	    }

	    response.addHeaderBytes(fileInfo.getHeaderBytes());

	    //	    response.setContentType(fileInfo.getMimeType());
	    //response.setContentLength(fileInfo.getSize());
	    
	    OutputStream out = response.getOutputStream();
	    InputStream fileIn = fileInfo.getInputStream();
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
	
	} else {
	    sendNotFoundResponse(request, response);
	}
    }

    private void sendNotFoundResponse(HttpRequest request, HttpResponse response) 
	throws IOException
    {
	System.out.println("Sending '404 Not found' for: " + request.getURI());
    }

    private void sendDirectoryIndexResponse(HttpRequest request, HttpResponse response) 
	throws IOException
    {
	System.out.println("Sending directory for: " + request.getURI());
    }

    
}
