

package jetserver.web.services;

import java.io.*;

import jetserver.web.*;

public abstract class WebService {

    private static WebService fileService;

    public static WebService getServiceInstance(HttpRequest request) {
	if (fileService == null) {
	    try {
		fileService = new FileService();
	    } catch (IOException e) {
		throw new RuntimeException("cant start file service: " + e);
	    }
	}
	return fileService;
    }

    public abstract void service(HttpRequest request, HttpResponse response) throws IOException;
}
