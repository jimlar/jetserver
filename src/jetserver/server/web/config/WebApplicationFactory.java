
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
import jetserver.server.web.WebApplication;
import jetserver.server.application.Application;

public class WebApplicationFactory {

    public static WebApplication createWebApplication(Application application, File fileRoot) throws IOException {
        try {
            WebApplication webApplication = new WebApplication(application, fileRoot);

            DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            parser.setEntityResolver(new JetServerEntityResolver());

            /* parse web xml */
            File webINF = new File(fileRoot, "WEB-INF");
            File webXML = new File(webINF, "web.xml");
            processWebXML(webApplication, parser.parse(webXML.getAbsolutePath()));

            /* parse jetserver web xml */
            File jetServerWebXML = new File(webINF, "jetserver-web.xml");
            processJetServerWebXML(webApplication, parser.parse(jetServerWebXML.getAbsolutePath()));

            return webApplication;

        } catch (ParserConfigurationException e) {
            throw new IOException("cant parse xml: " + e);
        } catch (FactoryConfigurationError error) {
            throw new IOException("cant parse xml: " + error);
        } catch (SAXException e) {
            throw new IOException("cant parse xml: " + e);
        }
    }

    private static void processWebXML(WebApplication webApplication, Document document)
            throws IOException
    {
        Node root = XMLUtilities.findFirstChildElement(document, "web-app");

        /** Fetch war info */
        webApplication.setDisplayName(XMLUtilities.findValue(root, "display-name"));

        /** Fetch servlets */
        NodeList servletNodes = document.getElementsByTagName("servlet");
        for (int i = 0; i < servletNodes.getLength(); i++) {
            processServletNode(webApplication, servletNodes.item(i));
        }

        /** Fetch servlet mappings */
        NodeList servletMappingNodes = document.getElementsByTagName("servlet-mapping");
        for (int i = 0; i < servletMappingNodes.getLength(); i++) {
            Node node = servletMappingNodes.item(i);
            ServletMapping servletMapping = new ServletMapping(XMLUtilities.findValue(node, "servlet-name"),
                                                               XMLUtilities.findValue(node, "url-pattern"));
            webApplication.addServletMapping(servletMapping);
        }

        /** Fetch welcome files */
        NodeList welcomeFileNodes = document.getElementsByTagName("welcome-file");
        for (int i = 0; i < welcomeFileNodes.getLength(); i++) {
            Node node = welcomeFileNodes.item(i);
            webApplication.addWelcomeFile(node.getFirstChild().getNodeValue());
        }
    }

    private static void processServletNode(WebApplication webApplication, Node node) {
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

        webApplication.addServletConfig(servletConfig);
    }

    private static void processJetServerWebXML(WebApplication webApplication, Document document)
            throws IOException
    {
        Node root = XMLUtilities.findFirstChildElement(document, "web-app");
        webApplication.setHttpRoot(XMLUtilities.findValue(root, "root"));
    }
}
