

package jetserver.web.services;

import java.io.*;
import java.net.*;
import java.util.*;

import jetserver.web.*;
import jetserver.config.ServerConfig;

class ServletService extends WebService {

    public ServletService() throws IOException {
	ServerConfig config = ServerConfig.getInstance();
    }

    public void service(HttpRequest request, HttpResponse response) 
	throws IOException
    {	
	/* fetch the correct servlet */

	/* Instantiate */

	/* Run the servlet */


    }
}
