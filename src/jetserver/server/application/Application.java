package jetserver.server.application;

/**
 * This class represents an J2EE applcaition instance
 */
public class Application {
    private ApplicationConfig config;

    public Application(ApplicationConfig config) {
        this.config = config;
    }

    public ApplicationConfig getConfig() {
        return this.config;
    }
}
