
package jetserver.server.web.servlet;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import jetserver.server.web.HttpResponse;
import jetserver.server.web.WebApplicationConfig;
import jetserver.util.Log;

/**
 * This is an implementation of servlet response
 */

class JetServerHttpServletResponse implements HttpServletResponse {

    private WebApplicationConfig config;
    private Log log;
    private HttpResponse httpResponse;

    private String encoding = "iso-8859-1";

    JetServerHttpServletResponse(HttpResponse httpResponse, WebApplicationConfig config) {
        this.httpResponse = httpResponse;
        this.config = config;
        this.log = Log.getInstance(this);
    }

    OutputStream getSocketOutputStream() throws IOException {
        return httpResponse.getOutputStream();
    }

    void close() throws IOException {
        httpResponse.close();
    }

    /*======== The ServletResponse interface implementation =========*/

    public String getCharacterEncoding() {
        return encoding;
    }

    public ServletOutputStream getOutputStream() throws IOException {
        return new JetServerServletOutputStream(this);
    }

    public PrintWriter getWriter() throws IOException, UnsupportedEncodingException {
        return new PrintWriter(new OutputStreamWriter(getOutputStream(), encoding));
    }

    public void setContentLength(int len) {
        httpResponse.setContentLength(len);
    }

    public void setContentType(String type) {
        httpResponse.setContentType(type);
    }

    public void setBufferSize(int size) {
        logUnsupportedMehod();
    }

    public int getBufferSize() {
        logUnsupportedMehod();
        return -1;
    }

    public void flushBuffer() throws IOException {
        httpResponse.flushHeaders();
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
        logUnsupportedMehod();
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
