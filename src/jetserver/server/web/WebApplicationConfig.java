
package jetserver.server.web;

import org.xml.sax.*;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.*;

import jetserver.util.xml.JetServerEntityResolver;
import jetserver.util.Log;
import jetserver.server.web.servlet.ServletDeclaration;
import jetserver.server.web.servlet.ServletMapping;

public class WebApplicationConfig {

    private String displayName;
    private String httpRoot;
    private File fileRoot;
    private Collection welcomeFiles = new ArrayList();

    private List servletMappings = new ArrayList();
    private Map servletDeclarationsByName = new HashMap();


    WebApplicationConfig(String displayName,
                 File fileRoot,
                 String httpRoot,
                 Collection welcomeFiles,
                 Collection servletDeclarations,
                 List servletMappings) {

        this.displayName = displayName;
        this.httpRoot = httpRoot;
        this.fileRoot = fileRoot;
        this.welcomeFiles = welcomeFiles;

        this.servletMappings = servletMappings;
        this.servletDeclarationsByName = new HashMap();

        Iterator iter = servletDeclarations.iterator();
        while (iter.hasNext()) {
            ServletDeclaration d = (ServletDeclaration) iter.next();
            servletDeclarationsByName.put(d.getName(), d);
        }
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

    public ServletDeclaration getServletDeclaration(String servletName) {
        return (ServletDeclaration) this.servletDeclarationsByName.get(servletName);
    }

    /**
     * This method would probably be much nice if I used DOM as in the ejb-jar parser
     */
    public static WebApplicationConfig decode(File applicationRoot) throws IOException {

        /* Read web.xml and jetserver-web.xml files */
        try {

            File webINF = new File(applicationRoot, "WEB-INF");
            File webXML = new File(webINF, "web.xml");
            WebXMLHandler webXMLHandler = new WebXMLHandler();

            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(false);
            factory.setValidating(false);
            Parser parser = factory.newSAXParser().getParser();
            parser.setEntityResolver(new JetServerEntityResolver());
            parser.setDocumentHandler(webXMLHandler);
            parser.parse(new InputSource(new FileInputStream(webXML)));

            File jetServerWebXML = new File(webINF, "jetserver-web.xml");
            JetServerWebXMLHandler jetServerWebXMLHandler = new JetServerWebXMLHandler();
            parser.setDocumentHandler(jetServerWebXMLHandler);
            parser.parse(new InputSource(new FileInputStream(jetServerWebXML)));

            return new WebApplicationConfig(webXMLHandler.displayName,
                                    applicationRoot,
                                    jetServerWebXMLHandler.httpRoot,
                                    webXMLHandler.welcomeFiles,
                                    webXMLHandler.servletDeclarations,
                                    webXMLHandler.servletMappings);

        } catch (ParserConfigurationException e) {
            Log.getInstance(WebApplicationConfig.class).error("Cant parse web application config", e);
            throw new IOException("cant parse config: " + e);
        } catch (SAXException e) {
            Log.getInstance(WebApplicationConfig.class).error("Cant parse web application config", e);
            throw new IOException("cant parse config: " + e);
        }
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
                servletDeclarations.add(new ServletDeclaration(servletName, servletClass));

            } else if (name.equals("servlet-mapping")) {
                servletMappings.add(new ServletMapping(servletName, urlPattern));
            }

            characterBuffer = new StringBuffer();
        }

        public void characters(char[] ch, int start, int length) throws SAXException {
            characterBuffer.append(ch, start, length);
        }
    }

    /* --- XML handler for jetserver-web.xml --- */

    private static class JetServerWebXMLHandler extends HandlerBase {
        public String httpRoot;

        private StringBuffer characterBuffer = new StringBuffer();

        public void startElement(String name, AttributeList attributes) throws SAXException {
            characterBuffer = new StringBuffer();
        }

        public void endElement(String name) throws SAXException {

            if (name.equals("root") && characterBuffer.length() > 0) {
                httpRoot = characterBuffer.toString().trim();
            }

            /* Clear charater buffer to receive new data */
            characterBuffer = new StringBuffer();
        }

        public void characters(char[] ch, int start, int length) throws SAXException {
            characterBuffer.append(ch, start, length);
        }
    }
}
