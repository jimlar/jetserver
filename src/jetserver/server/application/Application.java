package jetserver.server.application;

import jetserver.server.web.WebApplication;
import jetserver.server.ejb.EJBJar;

import java.util.ArrayList;
import java.util.Collection;

/**
 * This class represents an J2EE applcaition instance
 */
public class Application {

    private ApplicationConfig config;
    private Collection webApplications = new ArrayList();
    private Collection ejbJars = new ArrayList();

    public Application(ApplicationConfig config) {
        this.config = config;
    }

    public ApplicationConfig getConfig() {
        return this.config;
    }

    public Collection getWebApplications() {
        return this.webApplications;
    }

    public void addWebApplication(WebApplication webApplication) {
        this.webApplications.add(webApplication);
    }

    public Collection getEJBJars() {
        return this.ejbJars;
    }

    public void addEJBJar(EJBJar ejbJar) {
        this.ejbJars.add(ejbJar);
    }
}
