package jetserver.server.config;

public class LogConfig implements ConfigBlock {
    private InfoLogConfig infoLog;
    private ErrorLogConfig errorLog;
    private DebugLogConfig debugLog;

    public InfoLogConfig getInfoLog() {
        return infoLog;
    }

    public void setInfoLog(InfoLogConfig infoLog) {
        this.infoLog = infoLog;
    }

    public ErrorLogConfig getErrorLog() {
        return errorLog;
    }

    public void setErrorLog(ErrorLogConfig errorLog) {
        this.errorLog = errorLog;
    }

    public DebugLogConfig getDebugLog() {
        return debugLog;
    }

    public void setDebugLog(DebugLogConfig debugLog) {
        this.debugLog = debugLog;
    }
}
