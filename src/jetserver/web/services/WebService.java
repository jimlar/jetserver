

package jetserver.web.services;

import java.io.*;

import jetserver.web.*;
import jetserver.web.services.file.*;
import jetserver.web.services.servlet.*;

public abstract class WebService {

    private static FileService fileService;
    private static ServletService servletService;
    
    public static WebService getServiceInstance(HttpRequest request) {
	if (!isInitialized()) {
	    try {
		fileService = new FileService();
	    } catch (IOException e) {
		throw new IllegalStateException("cant initialize services: " + e);
	    }
	}

	return fileService;
    }

    private static void initialize() throws IOException {
	fileService = new FileService();
    }

    private static boolean isInitialized() {
	return fileService != null;
    }

    public abstract void service(HttpRequest request, HttpResponse response) throws IOException;
}
