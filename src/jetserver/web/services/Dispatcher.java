

package jetserver.web.services;

import java.io.*;

import jetserver.web.*;
import jetserver.web.services.file.*;
import jetserver.web.services.servlet.*;

public class Dispatcher {

    private FileService fileService;
    private ServletService servletService;
    
    public Dispatcher() {
	try {
	    this.fileService = new FileService();
	} catch (IOException e) {
	    throw new IllegalStateException("cant initialize: " + e);
	}
    }

    public void dispatch(HttpRequest request, HttpResponse response) throws IOException {
	fileService.service(request, response);
    }
}
