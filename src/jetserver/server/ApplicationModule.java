package jetserver.server;

import java.io.File;

public class ApplicationModule {

    private File file;
    private String uri;

    ApplicationModule(File file, String uri) {
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
