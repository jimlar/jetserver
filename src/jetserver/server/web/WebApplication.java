
package jetserver.server.web;

import java.io.*;
import java.util.*;

import jetserver.server.web.services.file.*;

class WebApplication {

    private FileService fileService;
    private WebApplicationConfig config;

    public WebApplication(WebApplicationConfig config) throws IOException {
	this.config = config;
	this.fileService = new FileService(config);
    } 

    public WebApplicationConfig getConfig() {
	return this.config;
    }

    public void service(HttpRequest request, HttpResponse response) 
	throws IOException
    {
	fileService.service(request, response);
    }

    public String toString() {
	return "[WebApplication http=" + config.getHttpRoot() + ", file=" + config.getFileRoot() + "]";
    }
}
