package jetserver.server.ejb.parsing;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;
import org.xml.sax.HandlerBase;
import org.xml.sax.AttributeList;

import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.io.FileInputStream;
import java.util.Collection;
import java.util.ArrayList;

/**
 * Parser for ejb-jar.xml, creates objects for all elements
 */
public class EJBJarXMLParser {

    private File ejbJarXML;
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
    }

    /**
     * Parse the xml
     */
    public void parse() throws IOException {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            factory.setNamespaceAware(false);
            factory.setValidating(false);
            SAXParser parser = factory.newSAXParser();

            EJBJarXMLHandler handler = new EJBJarXMLHandler();
            parser.parse(new FileInputStream(ejbJarXML), handler);

        } catch (ParserConfigurationException e) {
            throw new IOException("cant parse config: " + e);
        } catch (SAXException e) {
            throw new IOException("cant parse config: " + e);
        }
    }

    /**
     * SAX handler for ejb-jar.xml
     */
    private class EJBJarXMLHandler extends HandlerBase {
        private StringBuffer characterBuffer = new StringBuffer();

        public void startElement(String        name,
                                 AttributeList attributes)
                throws SAXException
        {
            /* Clear charater buffer to receive new data */
            characterBuffer = new StringBuffer();
        }

        public void endElement(String name)
                throws SAXException
        {
            /* Clear charater buffer to receive new data */
            characterBuffer = new StringBuffer();
        }

        public void characters(char[] ch,
                               int start,
                               int length)
                throws SAXException
        {
            characterBuffer.append(ch, start, length);
        }
    }
}