package jetserver.server.ejb;

import jetserver.server.ejb.config.*;
import jetserver.util.Log;
import jetserver.util.xml.JetServerEntityResolver;
import jetserver.util.xml.XMLUtilities;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.FactoryConfigurationError;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.ArrayList;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

/**
 * This class represents the contents of and ejb-jar file
 */
public class EJBJar {

    private File deployDir;
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
     * Create instance and parse configs (ejb-jar.xml)
     */
    EJBJar(File deployDir) throws IOException {
        this.deployDir = deployDir;

        this.entityBeans = new ArrayList();
        this.sessionBeans = new ArrayList();
        this.messageBeans = new ArrayList();

        this.tempDir = new File(deployDir, "jetserver_temp");
        this.wrappersDir = new File(deployDir, "jetserver_wrappers");
        this.classLoader = new EJBClassLoader(this);

        log = Log.getInstance(this);
        parse();
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

    public File getDeployDir() {
        return deployDir;
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
            File ejbJarXML = new File(deployDir, "META-INF" + File.separator + "ejb-jar.xml");
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
            this.description = XMLUtilities.findValue(ejbJarNode, "description");
            this.displayName = XMLUtilities.findValue(ejbJarNode, "display-name");
            this.smallIcon = XMLUtilities.findValue(ejbJarNode, "small-icon");
            this.largeIcon = XMLUtilities.findValue(ejbJarNode, "large-icon");
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

        entityBean.setPersistenceType(XMLUtilities.findValue(entityBeanNode, "persistence-type"));
        entityBean.setPrimKeyClass(XMLUtilities.findValue(entityBeanNode, "prim-key-class"));
        entityBean.setPrimKeyClass(XMLUtilities.findValue(entityBeanNode, "primkey-field"));
        entityBean.setReentrant(XMLUtilities.findValue(entityBeanNode, "reentrant").equalsIgnoreCase("true"));

        entityBean.setCmpVersion(XMLUtilities.findValue(entityBeanNode, "cmp-version"));
        entityBean.setAbstractSchemaName(XMLUtilities.findValue(entityBeanNode, "abstract-schema-name"));

        /*
         *  Fetch CMP fields
         */
        Collection cmpFields = new ArrayList();
        NodeList nodeList = entityBeanNode.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeName().equals("cmp-field")) {
                cmpFields.add(new CMPField(XMLUtilities.findValue(node, "field-name")));
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

        sessionBean.setSessionType(XMLUtilities.findValue(sessionBeanNode, "session-type"));
        sessionBean.setTransactionType(XMLUtilities.findValue(sessionBeanNode, "transaction-type"));

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

        messageBean.setTransactionType(XMLUtilities.findValue(messageBeanNode, "transaction-type"));
        messageBean.setMessageSelector(XMLUtilities.findValue(messageBeanNode, "message-selector"));
        messageBean.setAcknowledgeMode(XMLUtilities.findValue(messageBeanNode, "acknowledge-mode"));

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
        bean.setDescription(XMLUtilities.findValue(beanNode, "description"));
        bean.setDisplayName(XMLUtilities.findValue(beanNode, "display-name"));
        bean.setSmallIcon(XMLUtilities.findValue(beanNode, "small-icon"));
        bean.setLargeIcon(XMLUtilities.findValue(beanNode, "large-icon"));
        bean.setEJBName(XMLUtilities.findValue(beanNode, "ejb-name"));

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
        bean.setSecurityIdentity(XMLUtilities.findValue(beanNode, "security-identity"));
    }


    /**
     *  Fetch a class-name property and load the class
     * @return null if no such property found
     * @throws IOException on class loading problems
     */
    private Class findClassProperty(Node node, String propertyName)
            throws IOException
    {
        String className = XMLUtilities.findValue(node, propertyName);
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
