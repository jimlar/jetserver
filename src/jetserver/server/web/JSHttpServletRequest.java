
package jetserver.server.web;

import java.io.*;
import java.util.*;
import java.net.Socket;

import javax.servlet.*;
import javax.servlet.http.*;

import jetserver.util.Log;
import jetserver.util.Strings;

/**
 * This is the implementation of servlet request
 */

class JSHttpServletRequest implements HttpServletRequest {

    private static final int MAX_HEADER_LENGTH = 2048;
    private static final byte ASCII_CR = 0xd;
    private static final byte ASCII_LF = 0xa;
    private static final Enumeration EMPTY_ENUMERATION = Collections.enumeration(Collections.EMPTY_LIST);

    private byte readLineBuffer[] = new byte[MAX_HEADER_LENGTH];
    private WebApplication webApplication;
    private Log log;

    private Socket socket;
    private JSServletInputStream inputStream;
    private String method;
    private String uri;
    private String protocol;
    private Map headers;
    private Map attributes = new HashMap();

    /**
     * Create a request instance, the request is decoded from the input stream
     */
    public JSHttpServletRequest(Socket socket) throws IOException {
        this.log = Log.getInstance(this);
        this.socket = socket;
        this.inputStream = new JSServletInputStream(socket.getInputStream());

        String line = readLine(inputStream);

        int i = line.indexOf(" ");
        this.method = line.substring(0, i);

        int j = line.indexOf(" ", i + 1);
        this.uri = line.substring(i + 1, j);
        this.protocol = line.substring(j + 1);

        /* Fetch headers */
        this.headers = new HashMap();
        line = readLine(inputStream);
        while (line != null && !line.equals("")) {

            i = line.indexOf(":");
            String headerKey = line.substring(0, i);
            String headerValue = line.substring(i + 2);
            this.headers.put(headerKey.toLowerCase(), headerValue);

            line = readLine(inputStream);
        }

    }

    public void setWebApplication(WebApplication webApplication) {
        this.webApplication = webApplication;
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
            throws UnsupportedEncodingException
    {
        logUnsupportedMehod();
    }

    public int getContentLength() {
        String header = getHeader("content-length");
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
        return getHeader("content-type");
    }

    public ServletInputStream getInputStream() throws IOException {
        return this.inputStream;
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
        return this.protocol;
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
        return socket.getInetAddress().getHostAddress();
    }

    public String getRemoteHost() {
        return socket.getInetAddress().getHostName();
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
        /** We are allowed to return null if we are running in a war, which we are */
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
        return (String) headers.get(name.toLowerCase());
    }

    public Enumeration getHeaders(String name) {
        logUnsupportedMehod();
        return EMPTY_ENUMERATION;
    }

    public Enumeration getHeaderNames() {
        return Collections.enumeration(headers.keySet());
    }

    public int getIntHeader(String name) throws NumberFormatException {
        String header = getHeader(name);
        if (header == null) {
            return -1;
        }
        return Integer.parseInt(header);
    }

    public String getMethod() {
        return this.method;
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
        return webApplication.getContextRoot();
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
        return this.uri;
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

    public String toString() {
        return "[JSHttpServletRequest method=" + method + ", uri=" + uri + ", protocol=" + protocol + "]";
    }

    private void logUnsupportedMehod() {
        try {
            throw new RuntimeException("method not yet implemented");
        } catch (RuntimeException e) {
            log.debug("mehod not implemented", e);
        }
    }

    private String readLine(InputStream in)
            throws IOException
    {
        int totalRead = 0;
        byte b[] = new byte[1];

        int numRead = in.read(b);
        while (numRead != -1 && numRead != 0 && b[0] != ASCII_LF && b[0] != ASCII_CR) {
            readLineBuffer[totalRead] = b[0];
            totalRead += numRead;
            numRead = in.read(b);
        }

        /* if last char was a CR, read the LF */
        if (b[0] == ASCII_CR) {
            in.read(b);
        }

        return Strings.toStringFromAscii(readLineBuffer, 0, totalRead);
    }
}
