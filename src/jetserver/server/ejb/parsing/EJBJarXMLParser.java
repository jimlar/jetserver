package jetserver.server.ejb.parsing;

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

import jetserver.util.Log;


/**
 * Parser for ejb-jar.xml, creates objects for all elements
 */
public class EJBJarXMLParser {

    private File ejbJarXML;
    private Collection entityBeans;
    private Collection sessionBeans;
    private Collection messageBeans;
    private Log log;

    /**
     * Create a parser
     */
    public EJBJarXMLParser(File ejbJarXML) {
        this.ejbJarXML = ejbJarXML;
        this.entityBeans = new ArrayList();
        this.sessionBeans = new ArrayList();
        this.messageBeans = new ArrayList();

        log = Log.getInstance(this);
    }

    public Collection getEntityBeans() {
        return entityBeans;
    }

    public Collection getSessionBeans() {
        return sessionBeans;
    }

    public Collection getMessageBeans() {
        return messageBeans;
    }

    /**
     * Parse the xml
     */
    public void parse() throws IOException {

        try {
            DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document document = parser.parse(ejbJarXML.getAbsolutePath());
            processDocument(document);

        } catch (ParserConfigurationException e) {
            throw new IOException("cant parse ejb-jar.xml: " + e);
        } catch (FactoryConfigurationError error) {
            throw new IOException("cant parse ejb-jar.xml: " + error);
        } catch (SAXException e) {
            throw new IOException("cant parse ejb-jar.xml: " + e);
        }
    }

    private void processDocument(Document document) {

        NodeList entityBeans = document.getElementsByTagName("entity");
        if (entityBeans != null) {
            for (int i = 0; i < entityBeans.getLength(); i++) {
                processEntityBean(entityBeans.item(i), document);
            }
        }
    }

    /**
     * Process an entity bean declaration
     */
    private void processEntityBean(Node entityNode, Document document) {
        EntityBeanDefinition entity = new EntityBeanDefinition();
        entity.setDescription(findProperty(entityNode, "description"));
        entity.setName(findProperty(entityNode, "ejb-name"));

        this.entityBeans.add(entity);
        log.debug("Found entity bean " + entity.getName());
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
