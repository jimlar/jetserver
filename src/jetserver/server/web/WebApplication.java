
package jetserver.server.web;

import java.io.*;
import java.util.*;

import jetserver.server.web.services.file.FileService;
import jetserver.server.web.services.servlet.ServletService;
import jetserver.server.web.config.*;
import jetserver.server.application.Application;
import jetserver.util.Log;

public class WebApplication {

    private Application application;
    private WebAppConfig config;
    private FileService fileService;
    private ServletService servletService;

    public WebApplication(Application application, WebAppConfig config) throws IOException {
        this.application = application;
        this.config = config;
        this.fileService = new FileService(this);
        this.servletService = new ServletService(this);
    }

    public Application getApplication() {
        return this.application;
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
