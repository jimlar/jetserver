
package jetserver.server.web.services.servlet;

import java.io.*;
import java.util.*;

import javax.servlet.*;
import javax.servlet.http.*;

import jetserver.server.web.HttpRequest;
import jetserver.server.web.config.*;
import jetserver.util.Log;

/**
 * This is a cache where you fetch the servlet instances
 */

class JetServetHttpServletRequest implements HttpServletRequest {

    private WebAppConfig config;
    private Log log;

    private HttpRequest httpRequest;

    JetServerHttpServletRequest(HttpRequest httpRequest, WebAppConfig config) {
	this.httpRequest = httpRequest;
	this.config = config;
	this.log = Log.getInstance(this);
    }

    

}
