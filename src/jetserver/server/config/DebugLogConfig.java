package jetserver.server.config;

import java.io.File;

public class DebugLogConfig implements ConfigBlock {
    private File logFile;
    private boolean copyToStandardOut;
    private boolean enabled;

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

    public boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
