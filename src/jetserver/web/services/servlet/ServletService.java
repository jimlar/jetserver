

package jetserver.web.services.servlet;

import java.io.*;
import java.net.*;
import java.util.*;

import jetserver.web.*;
import jetserver.web.services.WebService;
import jetserver.config.ServerConfig;

public class ServletService implements WebService {
    
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
