
package jetserver.server.web;

import java.io.*;
import java.util.*;

import jetserver.server.web.services.file.FileService;
import jetserver.server.web.services.servlet.ServletService;
import jetserver.server.web.config.*;
import jetserver.util.Log;

class WebApplication {

    private FileService fileService;
    private ServletService servletService;
    private WebAppConfig config;

    public WebApplication(WebAppConfig config) throws IOException {
	this.config = config;
	this.fileService = new FileService(config);
	this.servletService = new ServletService(config);
    } 

    public WebAppConfig getConfig() {
	return this.config;
    }

    public void service(HttpRequest request, HttpResponse response) 
	throws IOException
    {
	if (servletService.service(request, response)) {
	    return;
	}

	fileService.service(request, response);
    }

    public String toString() {
	return "[WebApplication http=" + config.getHttpRoot() + ", file=" + config.getFileRoot() + "]";
    }
}
