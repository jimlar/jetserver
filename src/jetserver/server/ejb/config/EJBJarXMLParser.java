package jetserver.server.ejb.config;

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
    private Log log;

    private String description;
    private String displayName;
    private String smallIcon;
    private String largeIcon;

    private Collection entityBeans;
    private Collection sessionBeans;
    private Collection messageBeans;

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

        /* Fetch general info */
        NodeList ejbJarNodes = document.getElementsByTagName("ejb-jar");
        if (ejbJarNodes != null) {
            Node ejbJarNode = ejbJarNodes.item(0);
            this.description = findProperty(ejbJarNode, "description");
            this.displayName = findProperty(ejbJarNode, "display-name");
            this.smallIcon = findProperty(ejbJarNode, "small-icon");
            this.largeIcon = findProperty(ejbJarNode, "large-icon");
        }

        /* Fetch entity beans */
        NodeList entityBeans = document.getElementsByTagName("entity");
        if (entityBeans != null) {
            for (int i = 0; i < entityBeans.getLength(); i++) {
                processEntityBean(entityBeans.item(i), document);
            }
        }

        /* Fetch session beans */
        NodeList sessionBeans = document.getElementsByTagName("session");
        if (sessionBeans != null) {
            for (int i = 0; i < sessionBeans.getLength(); i++) {
                processSessionBean(sessionBeans.item(i), document);
            }
        }

        /* Fetch message beans */
        NodeList messageBeans = document.getElementsByTagName("message-driven");
        if (messageBeans != null) {
            for (int i = 0; i < messageBeans.getLength(); i++) {
                processMessageBean(messageBeans.item(i), document);
            }
        }

        /**
         * TODO: handle these elements:
         * - relationships
         * - assembly-descriptor
         * - ejb-client-jar
         */
    }

    /**
     * Process an entity bean declaration
     */
    private void processEntityBean(Node entityBeanNode, Document document) {
        EntityBeanDefinition entityBean = new EntityBeanDefinition();
        processBeanCommons(entityBean,  entityBeanNode, document);

        entityBean.setRemoteHomeClass(findProperty(entityBeanNode, "home"));
        entityBean.setRemoteClass(findProperty(entityBeanNode, "remote"));
        entityBean.setLocalHomeClass(findProperty(entityBeanNode, "local-home"));
        entityBean.setLocalClass(findProperty(entityBeanNode, "local"));

        entityBean.setPersistenceType(findProperty(entityBeanNode, "persistence-type"));
        entityBean.setPrimKeyClass(findProperty(entityBeanNode, "prim-key-class"));
        entityBean.setPrimKeyClass(findProperty(entityBeanNode, "primkey-field"));
        entityBean.setReentrant(findProperty(entityBeanNode, "reentrant").equalsIgnoreCase("true"));

        entityBean.setCmpVersion(findProperty(entityBeanNode, "cmp-version"));
        entityBean.setAbstractSchemaName(findProperty(entityBeanNode, "abstract-schema-name"));

        /*
         * TODO: handle these
         * - queries
         * - cmpFields
         */

        this.entityBeans.add(entityBean);
        log.debug("Found entity bean " + entityBean.getEJBName());
    }

    /**
     * Process a session bean declaration
     */
    private void processSessionBean(Node sessionBeanNode, Document document) {
        SessionBeanDefinition sessionBean = new SessionBeanDefinition();
        processBeanCommons(sessionBean,  sessionBeanNode, document);

        sessionBean.setRemoteHomeClass(findProperty(sessionBeanNode, "home"));
        sessionBean.setRemoteClass(findProperty(sessionBeanNode, "remote"));
        sessionBean.setLocalHomeClass(findProperty(sessionBeanNode, "local-home"));
        sessionBean.setLocalClass(findProperty(sessionBeanNode, "local"));

        sessionBean.setSessionType(findProperty(sessionBeanNode, "session-type"));
        sessionBean.setTransactionType(findProperty(sessionBeanNode, "transaction-type"));

        this.sessionBeans.add(sessionBean);
        log.debug("Found session bean " + sessionBean.getEJBName());
    }

    /**
     * Process an entity bean declaration
     */
    private void processMessageBean(Node messageBeanNode, Document document) {
        MessageBeanDefinition messageBean = new MessageBeanDefinition();
        processBeanCommons(messageBean,  messageBeanNode, document);

        messageBean.setTransactionType(findProperty(messageBeanNode, "transaction-type"));
        messageBean.setMessageSelector(findProperty(messageBeanNode, "message-selector"));
        messageBean.setAcknowledgeMode(findProperty(messageBeanNode, "acknowledge-mode"));

        /*
         * TODO: handle these
         * - destinationType
         * - subscriptionDurability;
         */

        this.messageBeans.add(messageBean);
        log.debug("Found message bean " + messageBean.getEJBName());
    }

    /**
     * Process an entity bean declaration
     */
    private void processBeanCommons(BeanDefinition bean, Node beanNode, Document document) {

        bean.setDescription(findProperty(beanNode, "description"));
        bean.setDisplayName(findProperty(beanNode, "display-name"));
        bean.setSmallIcon(findProperty(beanNode, "small-icon"));
        bean.setLargeIcon(findProperty(beanNode, "large-icon"));
        bean.setEJBName(findProperty(beanNode, "ejb-name"));

        bean.setEjbClass(findProperty(beanNode, "ejb-class"));

        /*
         * TODO: handle these
         * - environmentEntries
         * - ejbReferences
         * - localEJBReferences
         * - resourceReferences
         * - resourceEnvReferences
         * - roleReferences
         */
        bean.setSecurityIdentity(findProperty(beanNode, "security-identity"));
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
