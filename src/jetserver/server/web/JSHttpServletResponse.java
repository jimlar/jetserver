
package jetserver.server.web;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import jetserver.server.web.config.WebApplicationConfig;
import jetserver.util.Log;
import jetserver.util.Strings;

/**
 * This is an implementation of servlet response
 */

public class JSHttpServletResponse implements HttpServletResponse {

    public static final String HEADER_NEWLINE = "\r\n";
    private String encoding = "iso-8859-1";

    private WebApplication webApplication;
    private Log log;

    private OutputStream out;
    private String protocol;
    private String statusMessage;
    private int statusCode;
    private List headerBytes;
    private Map headers;

    private boolean headersFlushed = false;



    public JSHttpServletResponse(OutputStream out) {
        this.log = Log.getInstance(this);

        this.out = out;
        this.protocol = "HTTP/1.0";
        this.statusCode = 200;
        this.statusMessage = "OK";
        this.headers = new HashMap();
        this.headers.put("Server", "jetserver");
        this.headerBytes = new ArrayList();
    }

    public void setWebApplication(WebApplication webApplication) {
        this.webApplication = webApplication;
    }

    OutputStream getSocketOutputStream() throws IOException {
        flushHeaders();
        return out;
    }

    public void close() throws IOException {
        flushHeaders();
        out.close();
    }

    public void addHeaderBytes(byte bytes[]) {
        this.headerBytes.add(bytes);
    }

    public void flushHeaders() throws IOException {

        if (!headersFlushed) {
            StringBuffer buffer = new StringBuffer(256);

            buffer.append(this.protocol);
            buffer.append(" ");
            buffer.append(statusCode);
            buffer.append(" ");
            buffer.append(statusMessage);
            buffer.append(HEADER_NEWLINE);
            out.write(Strings.getAsciiBytes(buffer.toString()));

            Iterator iter = headerBytes.iterator();
            while (iter.hasNext()) {
                byte bytes[] = (byte[]) iter.next();
                out.write(bytes);
            }

            buffer = new StringBuffer(1024);
            iter = headers.keySet().iterator();
            while (iter.hasNext()) {
                String headerName = (String) iter.next();
                buffer.append(headerName);
                buffer.append(": ");
                buffer.append(headers.get(headerName));
                buffer.append(HEADER_NEWLINE);
            }

            buffer.append(HEADER_NEWLINE);

            out.write(Strings.getAsciiBytes(buffer.toString()));
            out.flush();
            headersFlushed = true;
        }
    }


    /*======== The ServletResponse interface implementation =========*/

    public String getCharacterEncoding() {
        return encoding;
    }

    public ServletOutputStream getOutputStream() throws IOException {
        return new JSServletOutputStream(this);
    }

    public PrintWriter getWriter() throws IOException, UnsupportedEncodingException {
        return new PrintWriter(new OutputStreamWriter(getOutputStream(), encoding));
    }

    public void setContentLength(int length) {
        setHeader("Content-length", "" + length);
    }

    public void setContentType(String type) {
        setHeader("Content-type", type);
    }

    public void setBufferSize(int size) {
        logUnsupportedMehod();
    }

    public int getBufferSize() {
        logUnsupportedMehod();
        return -1;
    }

    public void flushBuffer() throws IOException {
        flushHeaders();
    }

    public void resetBuffer() {
        logUnsupportedMehod();
    }

    public boolean isCommitted() {
        logUnsupportedMehod();
        return false;
    }

    public void reset() {
        logUnsupportedMehod();
    }

    public void setLocale(Locale loc) {
        logUnsupportedMehod();
    }

    public Locale getLocale() {
        logUnsupportedMehod();
        return null;
    }

    /*======== The HttpServletResponse interface implementation =========*/

    public void addCookie(Cookie cookie) {
        logUnsupportedMehod();
    }

    public boolean containsHeader(String name) {
        logUnsupportedMehod();
        return false;
    }

    public String encodeURL(String url) {
        logUnsupportedMehod();
        return null;
    }

    public String encodeRedirectURL(String url) {
        logUnsupportedMehod();
        return null;
    }

    public String encodeUrl(String url) {
        logUnsupportedMehod();
        return null;
    }

    public String encodeRedirectUrl(String url) {
        logUnsupportedMehod();
        return null;
    }

    public void sendError(int sc, String msg) throws IOException {
        logUnsupportedMehod();
    }

    public void sendError(int sc) throws IOException {
        logUnsupportedMehod();
    }

    public void sendRedirect(String location) throws IOException {
        logUnsupportedMehod();
    }

    public void setDateHeader(String name, long date) {
        logUnsupportedMehod();
    }

    public void addDateHeader(String name, long date) {
        logUnsupportedMehod();
    }

    public void setHeader(String name, String value) {
        this.headers.put(name, value);
    }

    public void addHeader(String name, String value) {
        logUnsupportedMehod();
    }

    public void setIntHeader(String name, int value) {
        setHeader(name, "" + value);
    }

    public void addIntHeader(String name, int value) {
        addHeader(name, "" + value);
    }

    public void setStatus(int sc) {
        logUnsupportedMehod();
    }

    public void setStatus(int sc, String sm) {
        logUnsupportedMehod();
    }

    private void logUnsupportedMehod() {
        try {
            throw new RuntimeException("method not yet implemented");
        } catch (RuntimeException e) {
            log.debug("mehod not implemented", e);
        }
    }
}
