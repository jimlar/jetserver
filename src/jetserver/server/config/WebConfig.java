package jetserver.server.config;

import java.io.File;

public class WebConfig implements ConfigBlock {
    private String defaultMimeType;
    private File mimeTypesFile;
    private SocketConfig socket;
    private ThreadsConfig threads;

    public String getDefaultMimeType() {
        return defaultMimeType;
    }

    public void setDefaultMimeType(String defaultMimeType) {
        this.defaultMimeType = defaultMimeType;
    }

    public File getMimeTypesFile() {
        return mimeTypesFile;
    }

    public void setMimeTypesFile(File mimeTypesFile) {
        this.mimeTypesFile = mimeTypesFile;
    }

    public SocketConfig getSocket() {
        return socket;
    }

    public void setSocket(SocketConfig socket) {
        this.socket = socket;
    }

    public ThreadsConfig getThreads() {
        return threads;
    }

    public void setThreads(ThreadsConfig threads) {
        this.threads = threads;
    }
}
