package jetserver.server.web;

import jetserver.server.Deployer;
import jetserver.util.Log;

import java.io.*;
import java.net.Socket;
import java.util.Iterator;

class WebServerConnection implements Runnable {

    private Socket socket;
    private Deployer deployer;

    public WebServerConnection(Socket socket, Deployer deployer) {
        this.socket = socket;
        this.deployer = deployer;
    }

    public void run() {
        JSHttpServletResponse response = null;

        try {
            JSHttpServletRequest request = new JSHttpServletRequest(socket);

            OutputStream out = new BufferedOutputStream(socket.getOutputStream());
            response = new JSHttpServletResponse(out);

            /* Ok, now find the correct web application instance */
            dispatchRequest(request, response);

        } catch (IOException e) {
            Log.getInstance(this).error("Error serving web request", e);
        } catch (RuntimeException e) {
            Log.getInstance(this).error("Error serving web request", e);
        } finally {
            try {
                if (response != null) {
                    response.close();
                }

                socket.close();
            } catch (IOException e) {
            }
        }
    }

    /**
     * Dispatch an incoming request to the right web application
     */
    private void dispatchRequest(JSHttpServletRequest request,
                                 JSHttpServletResponse response)
            throws IOException {
        WebApplication application = findApplication(request);
        if (application != null) {
            request.setWebApplication(application);
            response.setWebApplication(application);
            application.handleRequest(request, response);
        } else {
            Log.getInstance(this).info("No application mapped for request: " + request.getRequestURI());
        }
    }

    /**
     * Find the application handling a request
     */
    private WebApplication findApplication(JSHttpServletRequest request) {

        /* This is TOOO slow, change it later on */
        Iterator iter = deployer.getAllWebApplications().iterator();
        while (iter.hasNext()) {
            WebApplication application = (WebApplication) iter.next();
            if (request.getRequestURI().startsWith(application.getContextRoot())) {
                return application;
            }
        }
        return null;
    }
}
