
package jetserver.server.web.services.servlet;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import jetserver.server.web.HttpResponse;
import jetserver.server.web.config.*;
import jetserver.util.Log;

/**
 * This is an implementation of servlet response
 */

class JetServerHttpServletResponse implements HttpServletResponse {

    private WebAppConfig config;
    private Log log;
    private HttpResponse httpResponse;

    private String encoding = "iso-8859-1";

    JetServerHttpServletResponse(HttpResponse httpResponse, WebAppConfig config) {
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

    
    /**
     * Returns the name of the charset used for
     * the MIME body sent in this response.
     *
     * <p>If no charset has been assigned, it is implicitly
     * set to <code>ISO-8859-1</code> (<code>Latin-1</code>).
     *
     * <p>See RFC 2047 (http://ds.internic.net/rfc/rfc2045.txt)
     * for more information about character encoding and MIME.
     *
     * @return		a <code>String</code> specifying the
     *			name of the charset, for
     *			example, <code>ISO-8859-1</code>
     *
     */
  
    public String getCharacterEncoding() {
	return encoding;
    }

    /**
     * Returns a {@link ServletOutputStream} suitable for writing binary 
     * data in the response. The servlet container does not encode the
     * binary data.  
     
     * <p> Calling flush() on the ServletOutputStream commits the response.
     
     * Either this method or {@link #getWriter} may 
     * be called to write the body, not both.
     *
     * @return				a {@link ServletOutputStream} for writing binary data	
     *
     * @exception IllegalStateException if the <code>getWriter</code> method
     * 					has been called on this response
     *
     * @exception IOException 		if an input or output exception occurred
     *
     * @see 				#getWriter
     *
     */

    public ServletOutputStream getOutputStream() throws IOException {
	return new JetServerServletOutputStream(this);
    }
    
    /**
     * Returns a <code>PrintWriter</code> object that 
     * can send character text to the client. 
     * The character encoding used is the one specified 
     * in the <code>charset=</code> property of the
     * {@link #setContentType} method, which must be called
     * <i>before</i> calling this method for the charset to take effect. 
     *
     * <p>If necessary, the MIME type of the response is 
     * modified to reflect the character encoding used.
     *
     * <p> Calling flush() on the PrintWriter commits the response.
     *
     * <p>Either this method or {@link #getOutputStream} may be called
     * to write the body, not both.
     *
     * 
     * @return 				a <code>PrintWriter</code> object that 
     *					can return character data to the client 
     *
     * @exception UnsupportedEncodingException  if the charset specified in
     *						<code>setContentType</code> cannot be
     *						used
     *
     * @exception IllegalStateException    	if the <code>getOutputStream</code>
     * 						method has already been called for this 
     *						response object
     *
     * @exception IOException   		if an input or output exception occurred
     *
     * @see 					#getOutputStream
     * @see 					#setContentType
     *
     */

    public PrintWriter getWriter() throws IOException, UnsupportedEncodingException {
	return new PrintWriter(new OutputStreamWriter(getOutputStream(), encoding));
    }
    

    /**
     * Sets the length of the content body in the response
     * In HTTP servlets, this method sets the HTTP Content-Length header.
     *
     *
     * @param len 	an integer specifying the length of the 
     * 			content being returned to the client; sets
     *			the Content-Length header
     *
     */

    public void setContentLength(int len) {
	httpResponse.setContentLength(len);
    }
    
    

    /**
     * Sets the content type of the response being sent to
     * the client. The content type may include the type of character
     * encoding used, for example, <code>text/html; charset=ISO-8859-4</code>.
     *
     * <p>If obtaining a <code>PrintWriter</code>, this method should be 
     * called first.
     *
     *
     * @param type 	a <code>String</code> specifying the MIME 
     *			type of the content
     *
     * @see 		#getOutputStream
     * @see 		#getWriter
     *
     */

    public void setContentType(String type) {
	httpResponse.setContentType(type);
    }
    

    /**
     * Sets the preferred buffer size for the body of the response.  
     * The servlet container will use a buffer at least as large as 
     * the size requested.  The actual buffer size used can be found
     * using <code>getBufferSize</code>.
     *
     * <p>A larger buffer allows more content to be written before anything is
     * actually sent, thus providing the servlet with more time to set
     * appropriate status codes and headers.  A smaller buffer decreases 
     * server memory load and allows the client to start receiving data more
     * quickly.
     *
     * <p>This method must be called before any response body content is
     * written; if content has been written, this method throws an 
     * <code>IllegalStateException</code>.
     *
     * @param size 	the preferred buffer size
     *
     * @exception  IllegalStateException  	if this method is called after
     *						content has been written
     *
     * @see 		#getBufferSize
     * @see 		#flushBuffer
     * @see 		#isCommitted
     * @see 		#reset
     *
     */

    public void setBufferSize(int size) {
	logUnsupportedMehod();
    }
    
    /**
     * Returns the actual buffer size used for the response.  If no buffering
     * is used, this method returns 0.
     *
     * @return	 	the actual buffer size used
     *
     * @see 		#setBufferSize
     * @see 		#flushBuffer
     * @see 		#isCommitted
     * @see 		#reset
     *
     */

    public int getBufferSize() {
	logUnsupportedMehod();
	return -1;
    }
    

    /**
     * Forces any content in the buffer to be written to the client.  A call
     * to this method automatically commits the response, meaning the status 
     * code and headers will be written.
     *
     * @see 		#setBufferSize
     * @see 		#getBufferSize
     * @see 		#isCommitted
     * @see 		#reset
     *
     */

    public void flushBuffer() throws IOException {
	httpResponse.flushHeaders();
    }
    
    
    /**
     * Clears the content of the underlying buffer in the response without
     * clearing headers or status code. If the 
     * response has been committed, this method throws an 
     * <code>IllegalStateException</code>.
     *
     * @see 		#setBufferSize
     * @see 		#getBufferSize
     * @see 		#isCommitted
     * @see 		#reset
     *
     * @since 2.3
     */

    public void resetBuffer() {
	logUnsupportedMehod();
    }
    

    /**
     * Returns a boolean indicating if the response has been
     * committed.  A commited response has already had its status 
     * code and headers written.
     *
     * @return		a boolean indicating if the response has been
     *  		committed
     *
     * @see 		#setBufferSize
     * @see 		#getBufferSize
     * @see 		#flushBuffer
     * @see 		#reset
     *
     */

    public boolean isCommitted() {
	logUnsupportedMehod();
	return false;
    }
    
    

    /**
     * Clears any data that exists in the buffer as well as the status code and
     * headers.  If the response has been committed, this method throws an 
     * <code>IllegalStateException</code>.
     *
     * @exception IllegalStateException  if the response has already been
     *                                   committed
     *
     * @see 		#setBufferSize
     * @see 		#getBufferSize
     * @see 		#flushBuffer
     * @see 		#isCommitted
     *
     */

    public void reset() {
	logUnsupportedMehod();
    }
    
    

    /**
     * Sets the locale of the response, setting the headers (including the
     * Content-Type's charset) as appropriate.  This method should be called
     * before a call to {@link #getWriter}.  By default, the response locale
     * is the default locale for the server.
     * 
     * @param loc  the locale of the response
     *
     * @see 		#getLocale
     *
     */

    public void setLocale(Locale loc) {
	logUnsupportedMehod();
    }    
    

    /**
     * Returns the locale assigned to the response.
     * 
     * 
     * @see 		#setLocale
     *
     */

    public Locale getLocale() {
	logUnsupportedMehod();
	return null;
    }

    /*======== The HttpServletResponse interface implementation =========*/


    /**
     * Adds the specified cookie to the response.  This method can be called
     * multiple times to set more than one cookie.
     *
     * @param cookie the Cookie to return to the client
     *
     */

    public void addCookie(Cookie cookie) {
	logUnsupportedMehod();
    }

    /**
     * Returns a boolean indicating whether the named response header 
     * has already been set.
     * 
     * @param	name	the header name
     * @return		<code>true</code> if the named response header 
     *			has already been set; 
     * 			<code>false</code> otherwise
     */

    public boolean containsHeader(String name) {
	logUnsupportedMehod();
	return false;
    }

    /**
     * Encodes the specified URL by including the session ID in it,
     * or, if encoding is not needed, returns the URL unchanged.
     * The implementation of this method includes the logic to
     * determine whether the session ID needs to be encoded in the URL.
     * For example, if the browser supports cookies, or session
     * tracking is turned off, URL encoding is unnecessary.
     * 
     * <p>For robust session tracking, all URLs emitted by a servlet 
     * should be run through this
     * method.  Otherwise, URL rewriting cannot be used with browsers
     * which do not support cookies.
     *
     * @param	url	the url to be encoded.
     * @return		the encoded URL if encoding is needed;
     * 			the unchanged URL otherwise.
     */

    public String encodeURL(String url) {
	logUnsupportedMehod();
	return null;
    }


    /**
     * Encodes the specified URL for use in the
     * <code>sendRedirect</code> method or, if encoding is not needed,
     * returns the URL unchanged.  The implementation of this method
     * includes the logic to determine whether the session ID
     * needs to be encoded in the URL.  Because the rules for making
     * this determination can differ from those used to decide whether to
     * encode a normal link, this method is seperate from the
     * <code>encodeURL</code> method.
     * 
     * <p>All URLs sent to the <code>HttpServletResponse.sendRedirect</code>
     * method should be run through this method.  Otherwise, URL
     * rewriting cannot be used with browsers which do not support
     * cookies.
     *
     * @param	url	the url to be encoded.
     * @return		the encoded URL if encoding is needed;
     * 			the unchanged URL otherwise.
     *
     * @see #sendRedirect
     * @see #encodeUrl
     */

    public String encodeRedirectURL(String url) {
	logUnsupportedMehod();
	return null;
    }

    /**
     * @deprecated	As of version 2.1, use encodeURL(String url) instead
     *
     * @param	url	the url to be encoded.
     * @return		the encoded URL if encoding is needed; 
     * 			the unchanged URL otherwise.
     */

    public String encodeUrl(String url) {
	logUnsupportedMehod();
	return null;
    }
    
    /**
     * @deprecated	As of version 2.1, use 
     *			encodeRedirectURL(String url) instead
     *
     * @param	url	the url to be encoded.
     * @return		the encoded URL if encoding is needed; 
     * 			the unchanged URL otherwise.
     */

    public String encodeRedirectUrl(String url) {
	logUnsupportedMehod();
	return null;
    }

    /**
     * Sends an error response to the client using the specified
     * status clearing the buffer.  The server defaults to creating the 
     * response to look like an HTML-formatted server error page containing the specified message, setting the content type
     * to "text/html", leaving cookies and other headers unmodified.
     *
     * If an error-page declaration has been made for the web application
     * corresponding to the status code passed in, it will be served back in 
     * preference to the suggested msg parameter. 
     *
     * <p>If the response has already been committed, this method throws 
     * an IllegalStateException.
     * After using this method, the response should be considered
     * to be committed and should not be written to.
     *
     * @param	sc	the error status code
     * @param	msg	the descriptive message
     * @exception	IOException	If an input or output exception occurs
     * @exception	IllegalStateException	If the response was committed
     */
   
    public void sendError(int sc, String msg) throws IOException {
	logUnsupportedMehod();
    }

    /**
     * Sends an error response to the client using the specified status
     * code and clearing the buffer. 
     * <p>If the response has already been committed, this method throws 
     * an IllegalStateException.
     * After using this method, the response should be considered
     * to be committed and should not be written to.
     *
     * @param	sc	the error status code
     * @exception	IOException	If an input or output exception occurs
     * @exception	IllegalStateException	If the response was committed
     *						before this method call
     */

    public void sendError(int sc) throws IOException {
	logUnsupportedMehod();
    }

    /**
     * Sends a temporary redirect response to the client using the
     * specified redirect location URL.  This method can accept relative URLs;
     * the servlet container must convert the relative URL to an absolute URL
     * before sending the response to the client. If the location is relative 
     * without a leading '/' the container interprets it as relative to
     * the current request URI. If the location is relative with a leading
     * '/' the container interprets it as relative to the servlet container root.
     *
     * <p>If the response has already been committed, this method throws 
     * an IllegalStateException.
     * After using this method, the response should be considered
     * to be committed and should not be written to.
     *
     * @param		location	the redirect location URL
     * @exception	IOException	If an input or output exception occurs
     * @exception	IllegalStateException	If the response was committed
     */

    public void sendRedirect(String location) throws IOException {
	logUnsupportedMehod();
    }
    
    /**
     * 
     * Sets a response header with the given name and
     * date-value.  The date is specified in terms of
     * milliseconds since the epoch.  If the header had already
     * been set, the new value overwrites the previous one.  The
     * <code>containsHeader</code> method can be used to test for the
     * presence of a header before setting its value.
     * 
     * @param	name	the name of the header to set
     * @param	value	the assigned date value
     * 
     * @see #containsHeader
     * @see #addDateHeader
     */

    public void setDateHeader(String name, long date) {
	logUnsupportedMehod();
    }
    
    /**
     * 
     * Adds a response header with the given name and
     * date-value.  The date is specified in terms of
     * milliseconds since the epoch.  This method allows response headers 
     * to have multiple values.
     * 
     * @param	name	the name of the header to set
     * @param	value	the additional date value
     * 
     * @see #setDateHeader
     */

    public void addDateHeader(String name, long date) {
	logUnsupportedMehod();
    }
    
    /**
     *
     * Sets a response header with the given name and value.
     * If the header had already been set, the new value overwrites the
     * previous one.  The <code>containsHeader</code> method can be
     * used to test for the presence of a header before setting its
     * value.
     * 
     * @param	name	the name of the header
     * @param	value	the header value
     *
     * @see #containsHeader
     * @see #addHeader
     */

    public void setHeader(String name, String value) {
	logUnsupportedMehod();
    }
    
    /**
     * Adds a response header with the given name and value.
     * This method allows response headers to have multiple values.
     * 
     * @param	name	the name of the header
     * @param	value	the additional header value
     *
     * @see #setHeader
     */

    public void addHeader(String name, String value) {
	logUnsupportedMehod();
    }

    /**
     * Sets a response header with the given name and
     * integer value.  If the header had already been set, the new value
     * overwrites the previous one.  The <code>containsHeader</code>
     * method can be used to test for the presence of a header before
     * setting its value.
     *
     * @param	name	the name of the header
     * @param	value	the assigned integer value
     *
     * @see #containsHeader
     * @see #addIntHeader
     */

    public void setIntHeader(String name, int value) {
	setHeader(name, "" + value);
    }

    /**
     * Adds a response header with the given name and
     * integer value.  This method allows response headers to have multiple
     * values.
     *
     * @param	name	the name of the header
     * @param	value	the assigned integer value
     *
     * @see #setIntHeader
     */

    public void addIntHeader(String name, int value) {
	addHeader(name, "" + value);
    }
    
    /**
     * Sets the status code for this response.  This method is used to
     * set the return status code when there is no error (for example,
     * for the status codes SC_OK or SC_MOVED_TEMPORARILY).  If there
     * is an error, and the caller wishes to invoke an <error-page> defined
     * in the web applicaion, the <code>sendError</code> method should be used
     * instead.
     * <p> The container clears the buffer and sets the Location header, preserving
     * cookies and other headers.
     *
     * @param	sc	the status code
     *
     * @see #sendError
     */

    public void setStatus(int sc) {
	logUnsupportedMehod();
    }
  
    /**
     * @deprecated As of version 2.1, due to ambiguous meaning of the 
     * message parameter. To set a status code 
     * use <code>setStatus(int)</code>, to send an error with a description
     * use <code>sendError(int, String)</code>.
     *
     * Sets the status code and message for this response.
     * 
     * @param	sc	the status code
     * @param	sm	the status message
     */

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
