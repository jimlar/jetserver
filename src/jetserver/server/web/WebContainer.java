
package jetserver.server.web;

import java.io.*;
import java.util.*;

import jetserver.server.web.services.file.*;

class WebContainer {

    private FileService fileService;

    private String httpRoot;
    private File fileRoot;

    /**
     * @param httpRoot the root of this instance the http way
     * @param fileRoot the directory where whe web-app has been unpacked
     */
    public WebContainer(String httpRoot, File fileRoot) throws IOException {
	this.httpRoot = httpRoot;
	this.fileRoot = fileRoot;

	Collection welcomeFiles = new ArrayList();
	welcomeFiles.add("index.html");
	welcomeFiles.add("index.htm");
	this.fileService = new FileService(fileRoot, welcomeFiles);
    } 

    public String getHttpRoot() {
	return this.httpRoot;
    }

    public void service(HttpRequest request, HttpResponse response) 
	throws IOException
    {
	fileService.service(request, response);
    }
}
