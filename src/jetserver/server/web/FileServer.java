

package jetserver.server.web;

import java.io.*;
import java.net.*;
import java.util.*;

import jetserver.util.Log;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FileServer {

    private Log log;
    private static final int BUFFER_SIZE = 1024;
    private final FileInfoCache fileInfoCache;

    public FileServer(WebApplication webApplication) {
        this.fileInfoCache = new FileInfoCache(webApplication);
        this.log = Log.getInstance(this);
    }

    public void serveFile(HttpServletRequest req,
                          HttpServletResponse res)
            throws IOException
    {
        JSHttpServletRequest request = (JSHttpServletRequest) req;
        JSHttpServletResponse response = (JSHttpServletResponse) res;

        FileInfo fileInfo = fileInfoCache.getFileInfo(request);

        if (fileInfo.fileExists()) {

            /* Try welcomefiles if a directory is requested */
            if (fileInfo.isDirectoryIndexRequest()) {
                sendDirectoryIndexResponse(request, response);
                return;
            }

            response.addHeaderBytes(fileInfo.getHeaderBytes());

            OutputStream out = response.getOutputStream();
            InputStream fileIn = fileInfo.getInputStream();
            byte outputBuffer[] = new byte[BUFFER_SIZE];
            int readLen = 0;

            while (readLen != -1) {
                readLen = fileIn.read(outputBuffer);
                if (readLen != -1) {
                    out.write(outputBuffer, 0, readLen);
                }
            }

            fileIn.close();
            out.close();

        } else {
            sendNotFoundResponse(request, response);
        }
    }

    private void sendNotFoundResponse(HttpServletRequest request,
                                      HttpServletResponse response)
            throws IOException
    {
        log.debug("Sending '404 Not found' for: " + request.getRequestURI());
    }

    private void sendDirectoryIndexResponse(HttpServletRequest request,
                                            HttpServletResponse response)
            throws IOException
    {
        log.debug("Sending directory for: " + request.getRequestURI());
    }
}
