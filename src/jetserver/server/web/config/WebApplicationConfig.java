
package jetserver.server.web.config;

import org.xml.sax.*;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;

import javax.xml.parsers.*;
import java.io.*;
import java.util.*;

import jetserver.util.xml.JetServerEntityResolver;
import jetserver.util.xml.XMLUtilities;
import jetserver.util.Log;

public class WebApplicationConfig {

    private String displayName;
    private String httpRoot;
    private File fileRoot;

    private List welcomeFiles;
    private List servletMappings;
    private Map servletConfigsByName;


    public WebApplicationConfig(File fileRoot) throws IOException {
        this.fileRoot = fileRoot;
        parse();
    }


    public String getDisplayName() {
        return this.displayName;
    }

    public String getHttpRoot() {
        return this.httpRoot;
    }

    public File getFileRoot() {
        return this.fileRoot;
    }

    public Collection getWelcomeFiles() {
        return this.welcomeFiles;
    }

    public List getServletMappings() {
        return this.servletMappings;
    }

    public JSServletConfig getServletConfig(String servletName) {
        return (JSServletConfig) this.servletConfigsByName.get(servletName);
    }

    /**
     * Parse the xml
     */
    private void parse() throws IOException {
        try {
            DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            parser.setEntityResolver(new JetServerEntityResolver());

            /* parse web xml */
            File webINF = new File(fileRoot, "WEB-INF");
            File webXML = new File(webINF, "web.xml");
            processWebXML(parser.parse(webXML.getAbsolutePath()));

            /* parse jetserver web xml */
            File jetServerWebXML = new File(webINF, "jetserver-web.xml");
            processJetServerWebXML(parser.parse(jetServerWebXML.getAbsolutePath()));


        } catch (ParserConfigurationException e) {
            throw new IOException("cant parse xml: " + e);
        } catch (FactoryConfigurationError error) {
            throw new IOException("cant parse xml: " + error);
        } catch (SAXException e) {
            throw new IOException("cant parse xml: " + e);
        }
    }

    private void processWebXML(Document document)
            throws IOException
    {
        Node root = XMLUtilities.findFirstChildElement(document, "web-app");

        /** Fetch war info */
        this.displayName = XMLUtilities.findValue(root, "display-name");

        /** Fetch servlets */
        this.servletConfigsByName = new HashMap();
        NodeList servletNodes = document.getElementsByTagName("servlet");
        for (int i = 0; i < servletNodes.getLength(); i++) {
            processServletNode(servletNodes.item(i));
        }

        /** Fetch servlet mappings */
        this.servletMappings = new ArrayList();
        NodeList servletMappingNodes = document.getElementsByTagName("servlet-mapping");
        for (int i = 0; i < servletMappingNodes.getLength(); i++) {
            Node node = servletMappingNodes.item(i);
            ServletMapping servletMapping = new ServletMapping(XMLUtilities.findValue(node, "servlet-name"),
                                                               XMLUtilities.findValue(node, "url-pattern"));
            this.servletMappings.add(servletMapping);
        }

        /** Fetch welcome files */
        this.welcomeFiles = new ArrayList();
        NodeList welcomeFileNodes = document.getElementsByTagName("welcome-file");
        for (int i = 0; i < welcomeFileNodes.getLength(); i++) {
            Node node = welcomeFileNodes.item(i);
            this.welcomeFiles.add(node.getFirstChild().getNodeValue());
        }
    }

    private void processServletNode(Node node) {
        String servletName = XMLUtilities.findValue(node, "servlet-name");
        String servletClass = XMLUtilities.findValue(node, "servlet-class");

        /** Load on startup has an optional integer value which is the priority (we default to zero) */
        boolean loadOnStartup = XMLUtilities.hasChildElement(node, "load-on-startup");
        int loadPriority = -1;
        if (loadOnStartup) {
            loadPriority = 0;
            Integer value = XMLUtilities.findIntegerValue(node, "load-on-startup");
            if (value != null) {
                loadPriority = value.intValue();
            }
        }

        JSServletConfig servletConfig = new JSServletConfig(servletName,
                                                            servletClass,
                                                            loadOnStartup,
                                                            loadPriority);

        /** Fetch init parameters */
        NodeList paramNodes = XMLUtilities.findChildElements(node, "init-param");
        for (int i = 0; i < paramNodes.getLength(); i++) {
            Node paramNode = paramNodes.item(i);
            String paramName = XMLUtilities.findValue(paramNode, "param-name");
            String paramValue = XMLUtilities.findValue(paramNode, "param-value");
            servletConfig.addInitParameter(paramName, paramValue);
        }

        this.servletConfigsByName.put(servletName, servletConfig);
    }

    private void processJetServerWebXML(Document document)
            throws IOException
    {
        Node root = XMLUtilities.findFirstChildElement(document, "web-app");
        this.httpRoot = XMLUtilities.findValue(root, "root");
    }
}
