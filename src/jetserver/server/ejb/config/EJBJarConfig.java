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
import jetserver.util.xml.JetServerEntityResolver;
import jetserver.util.xml.XMLUtilities;
import jetserver.server.ejb.EJBClassLoader;


/**
 * Parser for ejb-jar.xml, creates objects for all elements
 */
public class EJBJarConfig {

    private File ejbJarRoot;
    private File tempDir;
    private File wrappersDir;
    private EJBClassLoader classLoader;
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
    public EJBJarConfig(File ejbJarRoot) throws IOException {
        this.ejbJarRoot = ejbJarRoot;

        this.entityBeans = new ArrayList();
        this.sessionBeans = new ArrayList();
        this.messageBeans = new ArrayList();

        this.tempDir = new File(ejbJarRoot, "jetserver_temp");
        this.wrappersDir = new File(ejbJarRoot, "jetserver_wrappers");
        this.classLoader = new EJBClassLoader(this);

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

    public File getEjbJarRoot() {
        return ejbJarRoot;
    }

    public File getTempDir() {
        return tempDir;
    }

    public File getWrappersDir() {
        return wrappersDir;
    }

    public EJBClassLoader getClassLoader() {
        return classLoader;
    }

    /**
     * Parse the xml
     */
    public void parse() throws IOException {
        try {
            File ejbJarXML = new File(ejbJarRoot, "META-INF" + File.separator + "ejb-jar.xml");
            DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            parser.setEntityResolver(new JetServerEntityResolver());
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

    private void processDocument(Document document)
            throws IOException
    {
        /* Fetch general info */
        NodeList ejbJarNodes = document.getElementsByTagName("ejb-jar");
        if (ejbJarNodes != null) {
            Node ejbJarNode = ejbJarNodes.item(0);
            this.description = XMLUtilities.findProperty(ejbJarNode, "description");
            this.displayName = XMLUtilities.findProperty(ejbJarNode, "display-name");
            this.smallIcon = XMLUtilities.findProperty(ejbJarNode, "small-icon");
            this.largeIcon = XMLUtilities.findProperty(ejbJarNode, "large-icon");
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
    private void processEntityBean(Node entityBeanNode, Document document)
            throws IOException
    {
        EntityBeanDefinition entityBean = new EntityBeanDefinition();
        processBeanCommons(entityBean,  entityBeanNode, document);

        entityBean.setRemoteHomeClass(findClassProperty(entityBeanNode, "home"));
        entityBean.setRemoteClass(findClassProperty(entityBeanNode, "remote"));
        entityBean.setLocalHomeClass(findClassProperty(entityBeanNode, "local-home"));
        entityBean.setLocalClass(findClassProperty(entityBeanNode, "local"));

        entityBean.setPersistenceType(XMLUtilities.findProperty(entityBeanNode, "persistence-type"));
        entityBean.setPrimKeyClass(XMLUtilities.findProperty(entityBeanNode, "prim-key-class"));
        entityBean.setPrimKeyClass(XMLUtilities.findProperty(entityBeanNode, "primkey-field"));
        entityBean.setReentrant(XMLUtilities.findProperty(entityBeanNode, "reentrant").equalsIgnoreCase("true"));

        entityBean.setCmpVersion(XMLUtilities.findProperty(entityBeanNode, "cmp-version"));
        entityBean.setAbstractSchemaName(XMLUtilities.findProperty(entityBeanNode, "abstract-schema-name"));

        /*
         *  Fetch CMP fields
         */
        Collection cmpFields = new ArrayList();
        NodeList nodeList = entityBeanNode.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeName().equals("cmp-field")) {
                cmpFields.add(new CMPField(XMLUtilities.findProperty(node, "field-name")));
            }
        }
        entityBean.setCmpFields(cmpFields);

        /*
         * TODO: handle these
         * - queries
         */

        this.entityBeans.add(entityBean);
        log.debug("Found entity bean " + entityBean.getEJBName());
    }

    /**
     * Process a session bean declaration
     */
    private void processSessionBean(Node sessionBeanNode, Document document)
            throws IOException
    {
        SessionBeanDefinition sessionBean = new SessionBeanDefinition();
        processBeanCommons(sessionBean,  sessionBeanNode, document);

        sessionBean.setRemoteHomeClass(findClassProperty(sessionBeanNode, "home"));
        sessionBean.setRemoteClass(findClassProperty(sessionBeanNode, "remote"));
        sessionBean.setLocalHomeClass(findClassProperty(sessionBeanNode, "local-home"));
        sessionBean.setLocalClass(findClassProperty(sessionBeanNode, "local"));

        sessionBean.setSessionType(XMLUtilities.findProperty(sessionBeanNode, "session-type"));
        sessionBean.setTransactionType(XMLUtilities.findProperty(sessionBeanNode, "transaction-type"));

        this.sessionBeans.add(sessionBean);
        log.debug("Found session bean " + sessionBean.getEJBName());
    }

    /**
     * Process an entity bean declaration
     */
    private void processMessageBean(Node messageBeanNode, Document document)
            throws IOException
    {
        MessageBeanDefinition messageBean = new MessageBeanDefinition();
        processBeanCommons(messageBean,  messageBeanNode, document);

        messageBean.setTransactionType(XMLUtilities.findProperty(messageBeanNode, "transaction-type"));
        messageBean.setMessageSelector(XMLUtilities.findProperty(messageBeanNode, "message-selector"));
        messageBean.setAcknowledgeMode(XMLUtilities.findProperty(messageBeanNode, "acknowledge-mode"));

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
    private void processBeanCommons(BeanDefinition bean, Node beanNode, Document document)
            throws IOException
    {
        bean.setDescription(XMLUtilities.findProperty(beanNode, "description"));
        bean.setDisplayName(XMLUtilities.findProperty(beanNode, "display-name"));
        bean.setSmallIcon(XMLUtilities.findProperty(beanNode, "small-icon"));
        bean.setLargeIcon(XMLUtilities.findProperty(beanNode, "large-icon"));
        bean.setEJBName(XMLUtilities.findProperty(beanNode, "ejb-name"));

        bean.setEjbClass(findClassProperty(beanNode, "ejb-class"));

        /*
         * TODO: handle these
         * - environmentEntries
         * - ejbReferences
         * - localEJBReferences
         * - resourceReferences
         * - resourceEnvReferences
         * - roleReferences
         */
        bean.setSecurityIdentity(XMLUtilities.findProperty(beanNode, "security-identity"));
    }


    /**
     *  Fetch a class-name property and load the class
     * @return null if no such property found
     * @throws IOException on class loading problems
     */
    private Class findClassProperty(Node node, String propertyName)
            throws IOException
    {
        String className = XMLUtilities.findProperty(node, propertyName);
        if (className == null) {
            return null;
        }
        try {
            return classLoader.loadClass(className);
        } catch (ClassNotFoundException e) {
            throw new IOException("Class could not be loaded " + className);
        }
    }
}
