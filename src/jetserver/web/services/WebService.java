

package jetserver.web.services;

import java.io.*;

import jetserver.web.*;

public abstract class WebService {

    private static final WebService fileService = new FileService();

    public static WebService getServiceInstance(HttpRequest request) {
	return fileService;
    }

    public abstract void service(HttpRequest request, HttpResponse response) throws IOException;
}
