
package jetserver.server.web.services.servlet;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import jetserver.server.web.services.HttpRequest;
import jetserver.server.web.config.*;
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

    /**
     *
     * Returns the value of the named attribute as an <code>Object</code>,
     * or <code>null</code> if no attribute of the given name exists. 
     *
     * <p> Attributes can be set two ways.  The servlet container may set
     * attributes to make available custom information about a request.
     * For example, for requests made using HTTPS, the attribute
     * <code>javax.servlet.request.X509Certificate</code> can be used to
     * retrieve information on the certificate of the client.  Attributes
     * can also be set programatically using 
     * {@link ServletRequest#setAttribute}.  This allows information to be
     * embedded into a request before a {@link RequestDispatcher} call.
     *
     * <p>Attribute names should follow the same conventions as package
     * names. This specification reserves names matching <code>java.*</code>,
     * <code>javax.*</code>, and <code>sun.*</code>. 
     *
     * @param name	a <code>String</code> specifying the name of 
     *			the attribute
     *
     * @return		an <code>Object</code> containing the value 
     *			of the attribute, or <code>null</code> if
     *			the attribute does not exist
     *
     */

    public Object getAttribute(String name) {
        return attributes.get(name);
    }


    /**
     * Returns an <code>Enumeration</code> containing the
     * names of the attributes available to this request. 
     * This method returns an empty <code>Enumeration</code>
     * if the request has no attributes available to it.
     * 
     *
     * @return		an <code>Enumeration</code> of strings 
     *			containing the names 
     * 			of the request's attributes
     *
     */

    public Enumeration getAttributeNames() {
        return Collections.enumeration(attributes.keySet());
    }

    /**
     * Returns the name of the character encoding used in the body of this
     * request. This method returns <code>null</code> if the request
     * does not specify a character encoding
     * 
     *
     * @return		a <code>String</code> containing the name of 
     *			the chararacter encoding, or <code>null</code>
     *			if the request does not specify a character encoding
     *
     */

    public String getCharacterEncoding() {
        logUnsupportedMehod();
        return null;
    }

    /**
     * Overrides the name of the character encoding used in the body of this
     * request. This method must be called prior to reading request parameters
     * or reading input using getReader().
     * 
     *
     * @param		a <code>String</code> containing the name of 
     *			the chararacter encoding.
     * @throws		java.io.UnsupportedEncodingException if this is not a valid encoding
     */

    public void setCharacterEncoding(String env)
            throws java.io.UnsupportedEncodingException
    {
        logUnsupportedMehod();
    }

    /**
     * Returns the length, in bytes, of the request body 
     * and made available by the input stream, or -1 if the
     * length is not known. For HTTP servlets, same as the value
     * of the CGI variable CONTENT_LENGTH.
     *
     * @return		an integer containing the length of the 
     * 			request body or -1 if the length is not known
     *
     */

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

    /**
     * Returns the MIME type of the body of the request, or 
     * <code>null</code> if the type is not known. For HTTP servlets, 
     * same as the value of the CGI variable CONTENT_TYPE.
     *
     * @return		a <code>String</code> containing the name 
     *			of the MIME type of 
     * 			the request, or null if the type is not known
     *
     */

    public String getContentType() {
        return httpRequest.getHeader("content-type");
    }

    /**
     * Retrieves the body of the request as binary data using
     * a {@link ServletInputStream}.  Either this method or 
     * {@link #getReader} may be called to read the body, not both.
     *
     * @return			a {@link ServletInputStream} object containing
     * 				the body of the request
     *
     * @exception IllegalStateException  if the {@link #getReader} method
     * 					 has already been called for this request
     *
     * @exception IOException    	if an input or output exception occurred
     *
     */

    public ServletInputStream getInputStream() throws IOException {
        logUnsupportedMehod();
        return null;
    }

    /**
     * Returns the value of a request parameter as a <code>String</code>,
     * or <code>null</code> if the parameter does not exist. Request parameters
     * are extra information sent with the request.  For HTTP servlets,
     * parameters are contained in the query string or posted form data.
     *
     * <p>You should only use this method when you are sure the
     * parameter has only one value. If the parameter might have
     * more than one value, use {@link #getParameterValues}.
     *
     * <p>If you use this method with a multivalued
     * parameter, the value returned is equal to the first value
     * in the array returned by <code>getParameterValues</code>.
     *
     * <p>If the parameter data was sent in the request body, such as occurs
     * with an HTTP POST request, then reading the body directly via {@link
     * #getInputStream} or {@link #getReader} can interfere
     * with the execution of this method.
     *
     * @param name 	a <code>String</code> specifying the 
     *			name of the parameter
     *
     * @return		a <code>String</code> representing the 
     *			single value of the parameter
     *
     * @see 		#getParameterValues
     *
     */

    public String getParameter(String name) {
        logUnsupportedMehod();
        return null;
    }

    /**
     *
     * Returns an <code>Enumeration</code> of <code>String</code>
     * objects containing the names of the parameters contained
     * in this request. If the request has 
     * no parameters, the method returns an 
     * empty <code>Enumeration</code>. 
     *
     * @return		an <code>Enumeration</code> of <code>String</code>
     *			objects, each <code>String</code> containing
     * 			the name of a request parameter; or an 
     *			empty <code>Enumeration</code> if the
     *			request has no parameters
     *
     */

    public Enumeration getParameterNames() {
        logUnsupportedMehod();
        return EMPTY_ENUMERATION;
    }

    /**
     * Returns an array of <code>String</code> objects containing 
     * all of the values the given request parameter has, or 
     * <code>null</code> if the parameter does not exist.
     *
     * <p>If the parameter has a single value, the array has a length
     * of 1.
     *
     * @param name	a <code>String</code> containing the name of 
     *			the parameter whose value is requested
     *
     * @return		an array of <code>String</code> objects 
     *			containing the parameter's values
     *
     * @see		#getParameter
     *
     */

    public String[] getParameterValues(String name) {
        logUnsupportedMehod();
        return null;
    }

    /** Returns a java.util.Map of the parameters of this request.
     * Request parameters
     * are extra information sent with the request.  For HTTP servlets,
     * parameters are contained in the query string or posted form data.
     *
     * @return an immutable java.util.Map containing parameter names as 
     * keys and parameter values as map values. The keys in the parameter
     * map are of type String. The values in the parameter map are of type
     * String array.
     *
     */

    public Map getParameterMap() {
        logUnsupportedMehod();
        return new HashMap();
    }

    /**
     * Returns the name and version of the protocol the request uses
     * in the form <i>protocol/majorVersion.minorVersion</i>, for 
     * example, HTTP/1.1. For HTTP servlets, the value
     * returned is the same as the value of the CGI variable 
     * <code>SERVER_PROTOCOL</code>.
     *
     * @return		a <code>String</code> containing the protocol 
     *			name and version number
     *
     */

    public String getProtocol() {
        return httpRequest.getProtocol();
    }

    /**
     * Returns the name of the scheme used to make this request, 
     * for example,
     * <code>http</code>, <code>https</code>, or <code>ftp</code>.
     * Different schemes have different rules for constructing URLs,
     * as noted in RFC 1738.
     *
     * @return		a <code>String</code> containing the name 
     *			of the scheme used to make this request
     *
     */

    public String getScheme() {
        logUnsupportedMehod();
        return "http";
    }

    /**
     * Returns the host name of the server that received the request.
     * For HTTP servlets, same as the value of the CGI variable 
     * <code>SERVER_NAME</code>.
     *
     * @return		a <code>String</code> containing the name 
     *			of the server to which the request was sent
     */

    public String getServerName() {
        logUnsupportedMehod();
        return null;
    }

    /**
     * Returns the port number on which this request was received.
     * For HTTP servlets, same as the value of the CGI variable 
     * <code>SERVER_PORT</code>.
     *
     * @return		an integer specifying the port number
     *
     */

    public int getServerPort() {
        logUnsupportedMehod();
        return -1;
    }

    /**
     * Retrieves the body of the request as character data using
     * a <code>BufferedReader</code>.  The reader translates the character
     * data according to the character encoding used on the body.
     * Either this method or {@link #getInputStream} may be called to read the
     * body, not both.
     * 
     *
     * @return					a <code>BufferedReader</code>
     *						containing the body of the request	
     *
     * @exception UnsupportedEncodingException 	if the character set encoding
     * 						used is not supported and the 
     *						text cannot be decoded
     *
     * @exception IllegalStateException   	if {@link #getInputStream} method
     * 						has been called on this request
     *
     * @exception IOException  			if an input or output exception occurred
     *
     * @see 					#getInputStream
     *
     */

    public BufferedReader getReader() throws IOException {
        logUnsupportedMehod();
        return null;
    }

    /**
     * Returns the Internet Protocol (IP) address of the client 
     * that sent the request.  For HTTP servlets, same as the value of the 
     * CGI variable <code>REMOTE_ADDR</code>.
     *
     * @return		a <code>String</code> containing the 
     *			IP address of the client that sent the request
     *
     */

    public String getRemoteAddr() {
        logUnsupportedMehod();
        return null;
    }

    /**
     * Returns the fully qualified name of the client that sent the
     * request. If the engine cannot or chooses not to resolve the hostname 
     * (to improve performance), this method returns the dotted-string form of 
     * the IP address. For HTTP servlets, same as the value of the CGI variable 
     * <code>REMOTE_HOST</code>.
     *
     * @return		a <code>String</code> containing the fully 
     * qualified name of the client
     *
     */

    public String getRemoteHost() {
        logUnsupportedMehod();
        return null;
    }

    /**
     *
     * Stores an attribute in this request.
     * Attributes are reset between requests.  This method is most
     * often used in conjunction with {@link RequestDispatcher}.
     *
     * <p>Attribute names should follow the same conventions as
     * package names. Names beginning with <code>java.*</code>,
     * <code>javax.*</code>, and <code>com.sun.*</code>, are
     * reserved for use by Sun Microsystems.
     *<br> If the value passed in is null, the effect is the same as
     * calling {@link #removeAttribute}.
     *
     *
     *
     * @param name			a <code>String</code> specifying 
     *					the name of the attribute
     *
     * @param o				the <code>Object</code> to be stored
     *
     */

    public void setAttribute(String name, Object o) {
        attributes.put(name, o);
    }

    /**
     *
     * Removes an attribute from this request.  This method is not
     * generally needed as attributes only persist as long as the request
     * is being handled.
     *
     * <p>Attribute names should follow the same conventions as
     * package names. Names beginning with <code>java.*</code>,
     * <code>javax.*</code>, and <code>com.sun.*</code>, are
     * reserved for use by Sun Microsystems.
     *
     *
     * @param name			a <code>String</code> specifying 
     *					the name of the attribute to remove
     *
     */

    public void removeAttribute(String name) {
        attributes.remove(name);
    }

    /**
     *
     * Returns the preferred <code>Locale</code> that the client will 
     * accept content in, based on the Accept-Language header.
     * If the client request doesn't provide an Accept-Language header,
     * this method returns the default locale for the server.
     *
     *
     * @return		the preferred <code>Locale</code> for the client
     *
     */

    public Locale getLocale() {
        logUnsupportedMehod();
        return Locale.getDefault();
    }

    /**
     *
     * Returns an <code>Enumeration</code> of <code>Locale</code> objects
     * indicating, in decreasing order starting with the preferred locale, the
     * locales that are acceptable to the client based on the Accept-Language
     * header.
     * If the client request doesn't provide an Accept-Language header,
     * this method returns an <code>Enumeration</code> containing one 
     * <code>Locale</code>, the default locale for the server.
     *
     *
     * @return		an <code>Enumeration</code> of preferred 
     *                  <code>Locale</code> objects for the client
     *
     */

    public Enumeration getLocales() {
        logUnsupportedMehod();
        return null;
    }

    /**
     *
     * Returns a boolean indicating whether this request was made using a
     * secure channel, such as HTTPS.
     *
     *
     * @return		a boolean indicating if the request was made using a
     *                  secure channel
     *
     */

    public boolean isSecure() {
        logUnsupportedMehod();
        return false;
    }

    /**
     *
     * Returns a {@link RequestDispatcher} object that acts as a wrapper for
     * the resource located at the given path.  
     * A <code>RequestDispatcher</code> object can be used to forward
     * a request to the resource or to include the resource in a response.
     * The resource can be dynamic or static.
     *
     * <p>The pathname specified may be relative, although it cannot extend
     * outside the current servlet context.  If the path begins with 
     * a "/" it is interpreted as relative to the current context root.  
     * This method returns <code>null</code> if the servlet container
     * cannot return a <code>RequestDispatcher</code>.
     *
     * <p>The difference between this method and {@link
     * ServletContext#getRequestDispatcher} is that this method can take a
     * relative path.
     *
     * @param path      a <code>String</code> specifying the pathname
     *                  to the resource
     *
     * @return          a <code>RequestDispatcher</code> object
     *                  that acts as a wrapper for the resource
     *                  at the specified path
     *
     * @see             RequestDispatcher
     * @see             ServletContext#getRequestDispatcher
     *
     */

    public RequestDispatcher getRequestDispatcher(String path) {
        logUnsupportedMehod();
        return null;
    }

    /**
     * 
     * @deprecated 	As of Version 2.1 of the Java Servlet API,
     * 			use {@link ServletContext#getRealPath} instead.
     *
     */

    public String getRealPath(String path) {
        logUnsupportedMehod();
        return null;
    }



    /*======== The HttpServletRequest interface implementation =========*/


    /**
     * Returns the name of the authentication scheme used to protect
     * the servlet. All servlet containers support basic, form and client 
     * certificate authentication, and may additionally support digest 
     * authentication.
     * If the servlet is not authenticated <code>null</code> is returned. 
     *
     * <p>Same as the value of the CGI variable AUTH_TYPE.
     *
     *
     * @return		one of the static members BASIC_AUTH, 
     *			FORM_AUTH, CLIENT_CERT_AUTH, DIGEST_AUTH
     *			(suitable for == comparison) 
     *			indicating the authentication scheme, or 
     *			<code>null</code> if the request was 
     *			not authenticated.     
     *
     */

    public String getAuthType() {
        logUnsupportedMehod();
        return null;
    }

    /**
     *
     * Returns an array containing all of the <code>Cookie</code>
     * objects the client sent with this request.
     * This method returns <code>null</code> if no cookies were sent.
     *
     * @return		an array of all the <code>Cookies</code>
     *			included with this request, or <code>null</code>
     *			if the request has no cookies
     *
     *
     */

    public Cookie[] getCookies() {
        logUnsupportedMehod();
        return null;
    }

    /**
     *
     * Returns the value of the specified request header
     * as a <code>long</code> value that represents a 
     * <code>Date</code> object. Use this method with
     * headers that contain dates, such as
     * <code>If-Modified-Since</code>. 
     *
     * <p>The date is returned as
     * the number of milliseconds since January 1, 1970 GMT.
     * The header name is case insensitive.
     *
     * <p>If the request did not have a header of the
     * specified name, this method returns -1. If the header
     * can't be converted to a date, the method throws
     * an <code>IllegalArgumentException</code>.
     *
     * @param name		a <code>String</code> specifying the
     *				name of the header
     *
     * @return			a <code>long</code> value
     *				representing the date specified
     *				in the header expressed as
     *				the number of milliseconds
     *				since January 1, 1970 GMT,
     *				or -1 if the named header
     *				was not included with the
     *				reqest
     *
     * @exception	IllegalArgumentException	If the header value
     *							can't be converted
     *							to a date
     *
     */

    public long getDateHeader(String name) {
        logUnsupportedMehod();
        return -1;
    }

    /**
     *
     * Returns the value of the specified request header
     * as a <code>String</code>. If the request did not include a header
     * of the specified name, this method returns <code>null</code>.
     * The header name is case insensitive. You can use
     * this method with any request header.
     *
     * @param name		a <code>String</code> specifying the
     *				header name
     *
     * @return			a <code>String</code> containing the
     *				value of the requested
     *				header, or <code>null</code>
     *				if the request does not
     *				have a header of that name
     *
     */

    public String getHeader(String name) {
        return httpRequest.getHeader(name);
    }

    /**
     *
     * Returns all the values of the specified request header
     * as an <code>Enumeration</code> of <code>String</code> objects.
     *
     * <p>Some headers, such as <code>Accept-Language</code> can be sent
     * by clients as several headers each with a different value rather than
     * sending the header as a comma separated list.
     *
     * <p>If the request did not include any headers
     * of the specified name, this method returns an empty
     * <code>Enumeration</code>.
     * The header name is case insensitive. You can use
     * this method with any request header.
     *
     * @param name		a <code>String</code> specifying the
     *				header name
     *
     * @return			an <code>Enumeration</code> containing
     *                  	the values of the requested header. If
     *                  	the request does not have any headers of
     *                  	that name return an empty
     *                  	enumeration. If 
     *                  	the container does not allow access to
     *                  	header information, return null
     *
     */

    public Enumeration getHeaders(String name) {
        logUnsupportedMehod();
        return EMPTY_ENUMERATION;
    }

    /**
     *
     * Returns an enumeration of all the header names
     * this request contains. If the request has no
     * headers, this method returns an empty enumeration.
     *
     * <p>Some servlet containers do not allow do not allow
     * servlets to access headers using this method, in
     * which case this method returns <code>null</code>
     *
     * @return			an enumeration of all the
     *				header names sent with this
     *				request; if the request has
     *				no headers, an empty enumeration;
     *				if the servlet container does not
     *				allow servlets to use this method,
     *				<code>null</code>
     *				
     *
     */

    public Enumeration getHeaderNames() {
        return Collections.enumeration(httpRequest.getHeaderNames());
    }

    /**
     *
     * Returns the value of the specified request header
     * as an <code>int</code>. If the request does not have a header
     * of the specified name, this method returns -1. If the
     * header cannot be converted to an integer, this method
     * throws a <code>NumberFormatException</code>.
     *
     * <p>The header name is case insensitive.
     *
     * @param name		a <code>String</code> specifying the name
     *				of a request header
     *
     * @return			an integer expressing the value 
     * 				of the request header or -1
     *				if the request doesn't have a
     *				header of this name
     *
     * @exception	NumberFormatException		If the header value
     *							can't be converted
     *							to an <code>int</code>
     */

    public int getIntHeader(String name) throws NumberFormatException {
        String header = httpRequest.getHeader(name);
        if (header == null) {
            return -1;
        }
        return Integer.parseInt(header);
    }

    /**
     *
     * Returns the name of the HTTP method with which this 
     * request was made, for example, GET, POST, or PUT.
     * Same as the value of the CGI variable REQUEST_METHOD.
     *
     * @return			a <code>String</code> 
     *				specifying the name
     *				of the method with which
     *				this request was made
     *
     */

    public String getMethod() {
        return httpRequest.getMethod();
    }

    /**
     *
     * Returns any extra path information associated with
     * the URL the client sent when it made this request.
     * The extra path information follows the servlet path
     * but precedes the query string.
     * This method returns <code>null</code> if there
     * was no extra path information.
     *
     * <p>Same as the value of the CGI variable PATH_INFO.
     *
     *
     * @return		a <code>String</code>, decoded by the
     *			web container, specifying 
     *			extra path information that comes
     *			after the servlet path but before
     *			the query string in the request URL;
     *			or <code>null</code> if the URL does not have
     *			any extra path information
     *
     */

    public String getPathInfo() {
        logUnsupportedMehod();
        return null;
    }

    /**
     *
     * Returns any extra path information after the servlet name
     * but before the query string, and translates it to a real
     * path. Same as the value of the CGI variable PATH_TRANSLATED.
     *
     * <p>If the URL does not have any extra path information,
     * this method returns <code>null</code>.
     *
     * The web container does not decode thins string.
     *
     *
     * @return		a <code>String</code> specifying the
     *			real path, or <code>null</code> if
     *			the URL does not have any extra path
     *			information
     *
     *
     */

    public String getPathTranslated() {
        logUnsupportedMehod();
        return null;
    }

    /**
     *
     * Returns the portion of the request URI that indicates the context
     * of the request.  The context path always comes first in a request
     * URI.  The path starts with a "/" character but does not end with a "/"
     * character.  For servlets in the default (root) context, this method
     * returns "". The container does not decode this string.
     *
     *
     * @return		a <code>String</code> specifying the
     *			portion of the request URI that indicates the context
     *			of the request
     *
     *
     */

    public String getContextPath() {
        return config.getHttpRoot();
    }

    /**
     *
     * Returns the query string that is contained in the request
     * URL after the path. This method returns <code>null</code>
     * if the URL does not have a query string. Same as the value
     * of the CGI variable QUERY_STRING. 
     *
     * @return		a <code>String</code> containing the query
     *			string or <code>null</code> if the URL 
     *			contains no query string. The value is not
     *			decoded by the container.
     *
     */

    public String getQueryString() {
        logUnsupportedMehod();
        return null;
    }

    /**
     *
     * Returns the login of the user making this request, if the
     * user has been authenticated, or <code>null</code> if the user 
     * has not been authenticated.
     * Whether the user name is sent with each subsequent request
     * depends on the browser and type of authentication. Same as the 
     * value of the CGI variable REMOTE_USER.
     *
     * @return		a <code>String</code> specifying the login
     *			of the user making this request, or <code>null</code
     *			if the user login is not known
     *
     */

    public String getRemoteUser() {
        logUnsupportedMehod();
        return null;
    }

    /**
     *
     * Returns a boolean indicating whether the authenticated user is included
     * in the specified logical "role".  Roles and role membership can be
     * defined using deployment descriptors.  If the user has not been
     * authenticated, the method returns <code>false</code>.
     *
     * @param role		a <code>String</code> specifying the name
     *				of the role
     *
     * @return		a <code>boolean</code> indicating whether
     *			the user making this request belongs to a given role;
     *			<code>false</code> if the user has not been 
     *			authenticated
     *
     */

    public boolean isUserInRole(String role) {
        logUnsupportedMehod();
        return false;
    }

    /**
     *
     * Returns a <code>java.security.Principal</code> object containing
     * the name of the current authenticated user. If the user has not been
     * authenticated, the method returns <code>null</code>.
     *
     * @return		a <code>java.security.Principal</code> containing
     *			the name of the user making this request;
     *			<code>null</code> if the user has not been 
     *			authenticated
     *
     */

    public java.security.Principal getUserPrincipal() {
        logUnsupportedMehod();
        return null;
    }

    /**
     *
     * Returns the session ID specified by the client. This may
     * not be the same as the ID of the actual session in use.
     * For example, if the request specified an old (expired)
     * session ID and the server has started a new session, this
     * method gets a new session with a new ID. If the request
     * did not specify a session ID, this method returns
     * <code>null</code>.
     *
     *
     * @return		a <code>String</code> specifying the session
     *			ID, or <code>null</code> if the request did
     *			not specify a session ID
     *
     * @see		#isRequestedSessionIdValid
     *
     */

    public String getRequestedSessionId() {
        logUnsupportedMehod();
        return null;
    }

    /**
     *
     * Returns the part of this request's URL from the protocol
     * name up to the query string in the first line of the HTTP request.
     * The web container does not decode this String.
     * For example:
     *
     *

     * <table>
     * <tr align=left><th>First line of HTTP request      </th>
     * <th>     Returned Value</th>
     * <tr><td>POST /some/path.html HTTP/1.1<td><td>/some/path.html
     * <tr><td>GET http://foo.bar/a.html HTTP/1.0
     * <td><td>/a.html
     * <tr><td>HEAD /xyz?a=b HTTP/1.1<td><td>/xyz
     * </table>
     *      
     * <p>To reconstruct an URL with a scheme and host, use
     * {@link HttpUtils#getRequestURL}.
     *
     * @return		a <code>String</code> containing
     *			the part of the URL from the 
     *			protocol name up to the query string
     *
     * @see		HttpUtils#getRequestURL
     *
     */

    public String getRequestURI() {
        return this.httpRequest.getURI();
    }

    /**
     *
     * Reconstructs the URL the client used to make the request.
     * The returned URL contains a protocol, server name, port
     * number, and server path, but it does not include query
     * string parameters.
     * 
     * <p>Because this method returns a <code>StringBuffer</code>,
     * not a string, you can modify the URL easily, for example,
     * to append query parameters.
     *
     * <p>This method is useful for creating redirect messages
     * and for reporting errors.
     *
     * @return		a <code>StringBuffer</code> object containing
     *			the reconstructed URL
     *
     */
    public StringBuffer getRequestURL() {
        logUnsupportedMehod();
        return null;
    }

    /**
     *
     * Returns the part of this request's URL that calls
     * the servlet. This includes either the servlet name or
     * a path to the servlet, but does not include any extra
     * path information or a query string. Same as the value 
     * of the CGI variable SCRIPT_NAME.
     *
     *
     * @return		a <code>String</code> containing
     *			the name or path of the servlet being
     *			called, as specified in the request URL,
     *			decoded.
     *
     *
     */

    public String getServletPath() {
        logUnsupportedMehod();
        return null;
    }

    /**
     *
     * Returns the current <code>HttpSession</code>
     * associated with this request or, if if there is no
     * current session and <code>create</code> is true, returns 
     * a new session.
     *
     * <p>If <code>create</code> is <code>false</code>
     * and the request has no valid <code>HttpSession</code>,
     * this method returns <code>null</code>.
     *
     * <p>To make sure the session is properly maintained,
     * you must call this method before 
     * the response is committed. If the container is using cookies
     * to maintain session integrity and is asked to create a new session
     * when the response is committed, an IllegalStateException is thrown.
     *
     *
     *
     *
     * @param		<code>true</code> to create
     *			a new session for this request if necessary; 
     *			<code>false</code> to return <code>null</code>
     *			if there's no current session
     *			
     *
     * @return 		the <code>HttpSession</code> associated 
     *			with this request or <code>null</code> if
     * 			<code>create</code> is <code>false</code>
     *			and the request has no valid session
     *
     * @see	#getSession()
     *
     *
     */

    public HttpSession getSession(boolean create) {
        logUnsupportedMehod();
        return null;
    }

    /**
     *
     * Returns the current session associated with this request,
     * or if the request does not have a session, creates one.
     * 
     * @return		the <code>HttpSession</code> associated
     *			with this request
     *
     * @see	#getSession(boolean)
     *
     */

    public HttpSession getSession() {
        logUnsupportedMehod();
        return null;
    }

    /**
     *
     * Checks whether the requested session ID is still valid.
     *
     * @return			<code>true</code> if this
     *				request has an id for a valid session
     *				in the current session context;
     *				<code>false</code> otherwise
     *
     * @see			#getRequestedSessionId
     * @see			#getSession
     * @see			HttpSessionContext
     *
     */

    public boolean isRequestedSessionIdValid() {
        logUnsupportedMehod();
        return false;
    }


    /**
     *
     * Checks whether the requested session ID came in as a cookie.
     *
     * @return			<code>true</code> if the session ID
     *				came in as a
     *				cookie; otherwise, <code>false</code>
     *
     *
     * @see			#getSession
     *
     */

    public boolean isRequestedSessionIdFromCookie() {
        logUnsupportedMehod();
        return false;
    }

    /**
     *
     * Checks whether the requested session ID came in as part of the 
     * request URL.
     *
     * @return			<code>true</code> if the session ID
     *				came in as part of a URL; otherwise,
     *				<code>false</code>
     *
     *
     * @see			#getSession
     *
     */

    public boolean isRequestedSessionIdFromURL() {
        logUnsupportedMehod();
        return false;
    }

    /**
     *
     * @deprecated		As of Version 2.1 of the Java Servlet
     *				API, use {@link #isRequestedSessionIdFromURL}
     *				instead.
     *
     */

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
