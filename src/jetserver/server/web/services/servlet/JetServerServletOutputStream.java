
package jetserver.server.web.services.servlet;


import java.io.*;

import javax.servlet.*;


class JetServerServletOutputStream extends ServletOutputStream {

    private JetServerHttpServletResponse response;
    private OutputStream out;

    public JetServerServletOutputStream(JetServerHttpServletResponse response) 
	throws IOException
    {
	this.response = response;
	this.out = response.getSocketOutputStream();
    }

    public void write(int b) throws IOException {
	out.write(b);
    }
}
