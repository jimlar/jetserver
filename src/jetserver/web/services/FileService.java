

package jetserver.web.services;

import java.io.*;
import java.net.*;
import java.util.*;

import jetserver.web.*;
import jetserver.config.ServerConfig;

class FileService extends WebService {

    private static final String ROOT_PROPERTY = "jetserver.webserver.root";
    private static final String BUFFERSIZE_PROPERTY = "jetserver.webserver.buffersize";
    private static final String WELCOMEFILES_PROPERTY = "jetserver.webserver.welcome-file-list.welcome-file";

    private final File baseDir;
    private final int bufferSize;
    private final String welcomeFiles[];
    private final MimeTypes mimeTypes;

    public FileService() throws IOException {
	ServerConfig config = ServerConfig.getInstance();
	this.baseDir = config.getFileProperty(ROOT_PROPERTY);
	this.bufferSize = config.getIntProperty(BUFFERSIZE_PROPERTY);
	this.mimeTypes = new MimeTypes();
	if (config.getNumValues(WELCOMEFILES_PROPERTY) > 0) {
	    this.welcomeFiles = config.getPropertyValues(WELCOMEFILES_PROPERTY);
	} else {
	    this.welcomeFiles = null;
	}
    }

    public void service(HttpRequest request, HttpResponse response) 
	throws IOException
    {	
	File requestedFile = new File(baseDir, request.getURI());

	if (requestedFile.exists()) {
	    
	    /* Try welcomefiles if a directory is requested */
	    if (requestedFile.isDirectory()) {

		File welcomeFile = getExistingWelcomeFile(requestedFile);
		if (welcomeFile == null) {
		    sendDirectoryIndexResponse(request, response);
		    return;			
		}		
		requestedFile = welcomeFile;
	    }

	    response.setContentType(mimeTypes.getTypeByFileName(requestedFile.getAbsolutePath()));
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
	
	} else {
	    sendNotFoundResponse(request, response);
	}
    }

    private File getExistingWelcomeFile(File requestedFile) {

	if (welcomeFiles != null) {
	    for (int i = 0; i < welcomeFiles.length; i++) {
		File candidate = new File(requestedFile, welcomeFiles[i]);
		if (candidate.exists() && candidate.isFile()) {
		    return candidate;
		}
	    }
	}

	return null;
    }

    private void sendNotFoundResponse(HttpRequest request, HttpResponse response) 
	throws IOException
    {

    }

    private void sendDirectoryIndexResponse(HttpRequest request, HttpResponse response) 
	throws IOException
    {

    }
}
