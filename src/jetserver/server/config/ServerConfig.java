package jetserver.server.config;

import java.io.*;

public class ServerConfig implements ConfigBlock {

    static final File CONFIG_DIR = new File("config");
    static final File CONFIG_FILE = new File(CONFIG_DIR, "server-config.xml");

    private static ServerConfig instance;

    private LogConfig log;
    private WebConfig web;
    private File dropZone;
    private File deployDir;

    public static ServerConfig getInstance() {

        try {
            if (instance == null) {
                instance = new ServerConfig();
                ConfigPersistence.load(instance, new FileInputStream(CONFIG_FILE));
            }

        } catch (IOException e) {
            throw new RuntimeException("Cant load config (" + CONFIG_FILE.getAbsolutePath() + "): " + e);
        }

        return instance;
    }

    public LogConfig getLog() {
        return log;
    }

    public void setLog(LogConfig log) {
        this.log = log;
    }

    public WebConfig getWeb() {
        return web;
    }

    public void setWeb(WebConfig web) {
        this.web = web;
    }

    public File getDropZone() {
        return dropZone;
    }

    public void setDropZone(File dropZone) {
        this.dropZone = dropZone;
    }

    public File getDeployDir() {
        return deployDir;
    }

    public void setDeployDir(File deployDir) {
        this.deployDir = deployDir;
    }
}
