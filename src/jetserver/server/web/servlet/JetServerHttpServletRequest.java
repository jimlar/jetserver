
package jetserver.server.web.servlet;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import jetserver.server.web.HttpRequest;
import jetserver.server.web.WebApplicationConfig;
import jetserver.util.Log;

/**
 * This is the implementation of servlet request
 */

class JetServerHttpServletRequest implements HttpServletRequest {

    /**
     * This is an enumeration without contents
     */
    private static final Enumeration EMPTY_ENUMERATION = new Enumeration() {
        public boolean hasMoreElements() { return false; }
        public Object nextElement() { return null; }
    };

    private WebApplicationConfig config;
    private Log log;
    private HttpRequest httpRequest;

    private Map attributes = new HashMap();

    JetServerHttpServletRequest(HttpRequest httpRequest, WebApplicationConfig config) {
        this.httpRequest = httpRequest;
        this.config = config;
        this.log = Log.getInstance(this);
    }


    /*======== The ServletRequest interface implementation =========*/

    public Object getAttribute(String name) {
        return attributes.get(name);
    }

    public Enumeration getAttributeNames() {
        return Collections.enumeration(attributes.keySet());
    }

    public String getCharacterEncoding() {
        logUnsupportedMehod();
        return null;
    }

    public void setCharacterEncoding(String env)
            throws java.io.UnsupportedEncodingException
    {
        logUnsupportedMehod();
    }

    public int getContentLength() {
        String header = httpRequest.getHeader("content-length");
        if (header == null) {
            return -1;
        }
        try {
            return Integer.parseInt(header);
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    public String getContentType() {
        return httpRequest.getHeader("content-type");
    }

    public ServletInputStream getInputStream() throws IOException {
        logUnsupportedMehod();
        return null;
    }

    public String getParameter(String name) {
        logUnsupportedMehod();
        return null;
    }

    public Enumeration getParameterNames() {
        logUnsupportedMehod();
        return EMPTY_ENUMERATION;
    }

    public String[] getParameterValues(String name) {
        logUnsupportedMehod();
        return null;
    }

    public Map getParameterMap() {
        logUnsupportedMehod();
        return new HashMap();
    }

    public String getProtocol() {
        return httpRequest.getProtocol();
    }

    public String getScheme() {
        logUnsupportedMehod();
        return "http";
    }

    public String getServerName() {
        logUnsupportedMehod();
        return null;
    }

    public int getServerPort() {
        logUnsupportedMehod();
        return -1;
    }

    public BufferedReader getReader() throws IOException {
        logUnsupportedMehod();
        return null;
    }

    public String getRemoteAddr() {
        logUnsupportedMehod();
        return null;
    }

    public String getRemoteHost() {
        logUnsupportedMehod();
        return null;
    }

    public void setAttribute(String name, Object o) {
        attributes.put(name, o);
    }

    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    public Locale getLocale() {
        logUnsupportedMehod();
        return Locale.getDefault();
    }

    public Enumeration getLocales() {
        logUnsupportedMehod();
        return null;
    }

    public boolean isSecure() {
        logUnsupportedMehod();
        return false;
    }

    public RequestDispatcher getRequestDispatcher(String path) {
        logUnsupportedMehod();
        return null;
    }

    public String getRealPath(String path) {
        logUnsupportedMehod();
        return null;
    }

    /*======== The HttpServletRequest interface implementation =========*/

    public String getAuthType() {
        logUnsupportedMehod();
        return null;
    }

    public Cookie[] getCookies() {
        logUnsupportedMehod();
        return null;
    }

    public long getDateHeader(String name) {
        logUnsupportedMehod();
        return -1;
    }

    public String getHeader(String name) {
        return httpRequest.getHeader(name);
    }

    public Enumeration getHeaders(String name) {
        logUnsupportedMehod();
        return EMPTY_ENUMERATION;
    }

    public Enumeration getHeaderNames() {
        return Collections.enumeration(httpRequest.getHeaderNames());
    }

    public int getIntHeader(String name) throws NumberFormatException {
        String header = httpRequest.getHeader(name);
        if (header == null) {
            return -1;
        }
        return Integer.parseInt(header);
    }

    public String getMethod() {
        return httpRequest.getMethod();
    }

    public String getPathInfo() {
        logUnsupportedMehod();
        return null;
    }

    public String getPathTranslated() {
        logUnsupportedMehod();
        return null;
    }

    public String getContextPath() {
        return config.getHttpRoot();
    }

    public String getQueryString() {
        logUnsupportedMehod();
        return null;
    }

    public String getRemoteUser() {
        logUnsupportedMehod();
        return null;
    }

    public boolean isUserInRole(String role) {
        logUnsupportedMehod();
        return false;
    }

    public java.security.Principal getUserPrincipal() {
        logUnsupportedMehod();
        return null;
    }

    public String getRequestedSessionId() {
        logUnsupportedMehod();
        return null;
    }

    public String getRequestURI() {
        return this.httpRequest.getURI();
    }

    public StringBuffer getRequestURL() {
        logUnsupportedMehod();
        return null;
    }

    public String getServletPath() {
        logUnsupportedMehod();
        return null;
    }

    public HttpSession getSession(boolean create) {
        logUnsupportedMehod();
        return null;
    }

    public HttpSession getSession() {
        logUnsupportedMehod();
        return null;
    }

    public boolean isRequestedSessionIdValid() {
        logUnsupportedMehod();
        return false;
    }

    public boolean isRequestedSessionIdFromCookie() {
        logUnsupportedMehod();
        return false;
    }

    public boolean isRequestedSessionIdFromURL() {
        logUnsupportedMehod();
        return false;
    }

    public boolean isRequestedSessionIdFromUrl() {
        return isRequestedSessionIdFromURL();
    }


    private void logUnsupportedMehod() {
        try {
            throw new RuntimeException("method not yet implemented");
        } catch (RuntimeException e) {
            log.debug("mehod not implemented", e);
        }
    }
}
