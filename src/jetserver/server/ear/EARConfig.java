package jetserver.server.ear;

import org.xml.sax.SAXException;
import org.w3c.dom.NodeList;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.FactoryConfigurationError;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.ArrayList;
import java.util.List;

import jetserver.util.Log;
import jetserver.util.xml.JetServerEntityResolver;
import jetserver.server.ejb.EJBClassLoader;
import jetserver.server.ejb.config.*;


/**
 * Parser for ejb-jar.xml, creates objects for all elements
 */
public class EARConfig {

    private Log log;
    private File earRoot;

    private List ejbModules;
    private List webModules;

    /**
     * Create a parser
     */
    public EARConfig(File earRoot) throws IOException {
        this.earRoot = earRoot;
        this.ejbModules = new ArrayList();
        this.webModules = new ArrayList();
        log = Log.getInstance(this);
    }

    public File getEARRoot() {
        return earRoot;
    }

    public Collection getEJBModules() {
        return ejbModules;
    }

    public Collection getWebModules() {
        return webModules;
    }

    /**
     * Parse the xml
     */
    public void parse() throws IOException {
        try {
            File applicationXML = new File(earRoot, "META-INF" + File.separator + "application.xml");
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
        String ejbModuleURI = findProperty(moduleNode, "ejb");
        if (ejbModuleURI != null) {
            File file = new File(earRoot, ejbModuleURI);
            ejbModules.add(new Module(file, ejbModuleURI));
            return;
        }

        /* Is it a web module? */
        Node webNode = moduleNode.getFirstChild();
        if (webNode != null && webNode.getNodeName().equals("web")) {
            String uri = findProperty(webNode, "web-uri");
            File file = new File(earRoot, uri);
            webModules.add(new Module(file, uri));
            return;
        }
    }

    /**
     * Fetch the value of a named child or attribute.
     * For childs, the body of the child is returned.
     * (Child nodes have predecence over attributes)
     * @param node the node
     * @param propertyName name of child or attribute to find.
     * @return the value or null if not found
     */
    private String findProperty(Node node, String propertyName) {
       /* Try children */
       NodeList children = node.getChildNodes();
       for (int i = 0; i < children.getLength(); i++) {
           if (children.item(i).getNodeName().equals(propertyName)) {
               /* Extract the body of the child */
               String bodyValue = "";
               NodeList bodies = children.item(i).getChildNodes();
               for (int j = 0; j < bodies.getLength(); j++) {
                   bodyValue += bodies.item(j).getNodeValue();
               }
               return bodyValue;
           }
       }

       /* Try attributes */
       Node attribute = node.getAttributes().getNamedItem(propertyName);
       if (attribute != null) {
           return attribute.getNodeValue();
       } else {
           return null;
       }
    }
}
