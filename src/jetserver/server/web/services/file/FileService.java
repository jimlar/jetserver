

package jetserver.server.web.services.file;

import java.io.*;
import java.net.*;
import java.util.*;

import jetserver.server.web.*;
import jetserver.util.Log;

public class FileService  {

    private Log log;
    private static final int BUFFER_SIZE = 1024;
    private final FileInfoCache fileInfoCache;

    public FileService(WebApplicationConfig config) throws IOException {	
	this.fileInfoCache = new FileInfoCache(config);
	this.log = Log.getInstance(this);
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

	    OutputStream out = response.getOutputStream();
	    InputStream fileIn = fileInfo.getInputStream();
	    byte outputBuffer[] = new byte[BUFFER_SIZE];
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
	log.debug("Sending '404 Not found' for: " + request.getURI());
    }

    private void sendDirectoryIndexResponse(HttpRequest request, HttpResponse response) 
	throws IOException
    {
	log.debug("Sending directory for: " + request.getURI());
    }

    
}
