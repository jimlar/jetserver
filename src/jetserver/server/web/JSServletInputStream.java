package jetserver.server.web;

import javax.servlet.ServletInputStream;
import java.io.IOException;
import java.io.InputStream;

class JSServletInputStream extends ServletInputStream {
    private InputStream input;

    public JSServletInputStream(InputStream input) {
        this.input = input;
    }

    public int read() throws IOException {
        return input.read();
    }

    public void close() throws IOException {
        input.close();
    }
}
