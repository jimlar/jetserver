
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
    private Collection welcomeFiles = new ArrayList();

    private List servletMappings = new ArrayList();
    private Map servletDeclarationsByName = new HashMap();


    public WebApplicationConfig(File fileRoot) {
        this.fileRoot = fileRoot;
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

    public JSServletConfig getServletDeclaration(String servletName) {
        return (JSServletConfig) this.servletDeclarationsByName.get(servletName);
    }

    /**
     * Parse the xml
     */
    public void parse() throws IOException {
        try {
            DocumentBuilder parser = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            parser.setEntityResolver(new JetServerEntityResolver());

            /* parse web xml */
            File webINF = new File(fileRoot, "WEB-INF");
            File webXML = new File(webINF, "web.xml");
            processWebXML(parser.parse(webXML.getAbsolutePath()));

            /* parse jetserver web xml */
            File jetServerWebXML = new File(webINF, "jetserver-web.xml");
            processJetServerWebXML(parser.parse(webXML.getAbsolutePath()));


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
        NodeList servletNodes = document.getElementsByTagName("servlet");
        NodeList servletMappingNodes = document.getElementsByTagName("servlet-mapping");
    }

    private void processJetServerWebXML(Document document)
            throws IOException
    {
        this.httpRoot = XMLUtilities.findProperty(document.getFirstChild(), "root");
    }


    /* --- XML handler for web.xml --- */

    private static class WebXMLHandler extends HandlerBase {
        private StringBuffer characterBuffer = new StringBuffer();

        public String displayName;
        public Collection welcomeFiles = new ArrayList();

        private String servletName;
        private String servletClass;
        private String urlPattern;

        public List servletDeclarations = new ArrayList();;
        public List servletMappings = new ArrayList();;

        public void startElement(String name, AttributeList attributes) throws SAXException {
            characterBuffer = new StringBuffer();
        }

        public void endElement(String name) throws SAXException {

            if (name.equals("welcome-file") && characterBuffer.length() > 0) {
                welcomeFiles.add(characterBuffer.toString().trim());

            } else if (name.equals("display-name")) {
                displayName = characterBuffer.toString().trim();

            } else if (name.equals("servlet-name")) {
                servletName = characterBuffer.toString().trim();
            } else if (name.equals("servlet-class")) {
                servletClass = characterBuffer.toString().trim();
            } else if (name.equals("url-pattern")) {
                urlPattern = characterBuffer.toString().trim();

            } else if (name.equals("servlet")) {
                servletDeclarations.add(new JSServletConfig(servletName, servletClass));

            } else if (name.equals("servlet-mapping")) {
                servletMappings.add(new ServletMapping(servletName, urlPattern));
            }

            characterBuffer = new StringBuffer();
        }

        public void characters(char[] ch, int start, int length) throws SAXException {
            characterBuffer.append(ch, start, length);
        }
    }
}
