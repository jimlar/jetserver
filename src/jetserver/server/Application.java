package jetserver.server;

import jetserver.server.web.WebApplication;
import jetserver.server.ejb.EJBJar;
import jetserver.util.Log;
import jetserver.util.xml.JetServerEntityResolver;
import jetserver.util.xml.XMLUtilities;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.FactoryConfigurationError;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.io.File;
import java.io.IOException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * This class represents an J2EE applcaition instance
 */
public class Application {

    private File deployDir;

    private List ejbModules;
    private List webModules;

    private Collection webApplications;
    private Collection ejbJars;

    public static Application createEmpty() {
        return new Application(null);
    }

    public static Application createFromEARFile(File deployDir) throws IOException {
        Application application = new Application(deployDir);
        application.parse();
        return application;
    }


    private Application(File deployDir) {
        this.deployDir = deployDir;
        this.ejbModules = new ArrayList();
        this.webModules = new ArrayList();
        this.webApplications = new ArrayList();
        this.ejbJars = new ArrayList();
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

    public File getDeployDir() {
        return deployDir;
    }

    public Collection getEJBModules() {
        return ejbModules;
    }

    public Collection getWebModules() {
        return webModules;
    }

    /**
     * Parse the application.xml
     */
    private void parse() throws IOException {
        try {
            File applicationXML = new File(deployDir, "META-INF" + File.separator + "application.xml");
            DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            parser.setEntityResolver(new JetServerEntityResolver());
            Document document = parser.parse(applicationXML.getAbsolutePath());
            processDocument(document);

        } catch (ParserConfigurationException e) {
            throw new IOException("cant parse application.xml: " + e);
        } catch (FactoryConfigurationError error) {
            throw new IOException("cant parse application.xml: " + error);
        } catch (SAXException e) {
            throw new IOException("cant parse application.xml: " + e);
        }
    }

    private void processDocument(Document document) {
        /* Find all modules */
        NodeList modules = document.getElementsByTagName("module");
        if (modules != null) {
            for (int i = 0; i < modules.getLength(); i++) {
                processModule(modules.item(i));
            }
        }
    }

    private void processModule(Node moduleNode) {
        /* Is it an ejb module? */
        String ejbModuleURI = XMLUtilities.findValue(moduleNode, "ejb");
        if (ejbModuleURI != null) {
            File file = new File(deployDir, ejbModuleURI);
            ejbModules.add(new ApplicationModule(file, ejbModuleURI));
            return;
        }

        /* Is it a web module? */
        NodeList children = moduleNode.getChildNodes();
        if (children != null) {
            for (int i = 0; i < children.getLength(); i++) {
                Node webNode = children.item(i);
                if (webNode.getNodeName().equals("web")) {
                    String uri = XMLUtilities.findValue(webNode, "web-uri");
                    File file = new File(deployDir, uri);
                    webModules.add(new ApplicationModule(file, uri));
                    return;
                }
            }
        }
    }
}
