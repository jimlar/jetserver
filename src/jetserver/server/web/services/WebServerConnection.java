
package jetserver.server.web.services;

import java.io.*;
import java.net.*;
import java.util.*;

import jetserver.server.config.ServerConfig;
import jetserver.server.Deployer;
import jetserver.server.web.WebApplication;
import jetserver.util.Log;

public class WebServerConnection implements Runnable {

    private Socket socket;
    private Deployer deployer;

    public WebServerConnection(Socket socket, Deployer deployer) {
        this.socket = socket;
        this.deployer = deployer;
    }

    public void run() {
        try {
            InputStream in = new BufferedInputStream(socket.getInputStream());
            HttpRequest request = HttpRequest.decodeRequest(in);

            OutputStream out = new BufferedOutputStream(socket.getOutputStream());
            HttpResponse response = HttpResponse.createResponse(out);

            /* Ok, now find the correct web container instance */
            dispatchRequest(request, response);

        } catch (IOException e) {
            Log.getInstance(this).error("Error serving web request", e);
        } finally {
            try {
                socket.close();
            } catch (IOException e) {}
        }
    }

    /**
     * Dispatch an incoming request to the right web application
     */
    private void dispatchRequest(HttpRequest request, HttpResponse response) throws IOException {
        WebApplication application = findApplication(request);
        if (application != null) {
            application.service(request, response);
        } else {
            Log.getInstance(this).info("No application mapped for request: " + request.getURI());
        }
    }

    /**
     * Find the application handling a request
     */
    private WebApplication findApplication(HttpRequest request) {

        /* This is TOOO slow, change it later on */
        Iterator iter = deployer.getAllWebApplications().iterator();
        while (iter.hasNext()) {
            WebApplication application = (WebApplication) iter.next();
            if (request.getURI().startsWith(application.getConfig().getHttpRoot())) {
                return application;
            }
        }
        return null;
    }
}
