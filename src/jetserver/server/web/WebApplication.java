
package jetserver.server.web;

import java.io.*;
import java.util.*;

import jetserver.server.web.file.FileServer;
import jetserver.server.web.servlet.ServletDispatcher;
import jetserver.server.web.servlet.JSHttpServletRequest;
import jetserver.server.web.servlet.JSHttpServletResponse;
import jetserver.server.application.Application;
import jetserver.util.Log;

public class WebApplication {

    private Application application;
    private WebApplicationConfig config;
    private ServletDispatcher servletService;

    public WebApplication(Application application, WebApplicationConfig config) throws IOException {
        this.application = application;
        this.config = config;
        this.servletService = new ServletDispatcher(this);
    }

    public Application getApplication() {
        return this.application;
    }

    public WebApplicationConfig getConfig() {
        return this.config;
    }

    public void dispatchRequest(JSHttpServletRequest request,
                                JSHttpServletResponse response)
            throws IOException
    {
        servletService.dispatch(request, response);
    }

    public String toString() {
        return "[WebApplication http=" + config.getHttpRoot() + ", file=" + config.getFileRoot() + "]";
    }
}
