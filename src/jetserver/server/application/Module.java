package jetserver.server.application;

import java.io.File;

public class Module {

    private File file;
    private String uri;

    Module(File file, String uri) {
        this.file = file;
        this.uri = uri;
    }

    public File getFile() {
        return file;
    }

    public String getURI() {
        return this.uri;
    }
}
