
package jetserver.server.web.servlet;


import java.io.*;

import javax.servlet.*;


class JSServletOutputStream extends ServletOutputStream {

    private JSHttpServletResponse response;
    private OutputStream out;

    public JSServletOutputStream(JSHttpServletResponse response)
            throws IOException
    {
        this.response = response;
        this.out = response.getSocketOutputStream();
    }

    public void write(int b) throws IOException {
        out.write(b);
    }
}
