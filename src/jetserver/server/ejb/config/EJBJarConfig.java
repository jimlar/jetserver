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


    /**
     * Create a parser
     */
    public EJBJarConfig(File deployDir) throws IOException {
    }


}
