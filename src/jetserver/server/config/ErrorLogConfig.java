package jetserver.server.config;

import java.io.File;

public class ErrorLogConfig implements ConfigBlock {
    private File logFile;
    private boolean copyToStandardOut;

    public File getLogFile() {
        return logFile;
    }

    public void setLogFile(File logFile) {
        this.logFile = logFile;
    }

    public boolean getCopyToStandardOut() {
        return copyToStandardOut;
    }

    public void setCopyToStandardOut(boolean copyToStandardOut) {
        this.copyToStandardOut = copyToStandardOut;
    }
}
