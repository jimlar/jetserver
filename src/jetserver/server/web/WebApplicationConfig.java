
package jetserver.server.web;

import java.io.*;
import java.util.*;

import org.xml.sax.*;
import javax.xml.parsers.*;

import jetserver.util.Log;

public class WebApplicationConfig {

    private String httpRoot;
    private File fileRoot;
    private Collection welcomeFiles;

    
    static WebApplicationConfig decode(File applicationRoot) throws IOException {
	
	/* Read web.xml and jetserver-web.xml files */
	try {
	    
	    SAXParserFactory factory = SAXParserFactory.newInstance();
	    factory.setNamespaceAware(false);
	    factory.setValidating(false);
	    SAXParser parser = factory.newSAXParser();
	    
	    File webINF = new File(applicationRoot, "WEB-INF");

	    File webXML = new File(webINF, "web.xml");
	    WebXMLHandler webXMLHandler = new WebXMLHandler();	    
	    parser.parse(new FileInputStream(webXML), webXMLHandler);
	    
	    File jetServerWebXML = new File(webINF, "jetserver-web.xml");
	    JetServerWebXMLHandler jetServerWebXMLHandler = new JetServerWebXMLHandler();	    
	    parser.parse(new FileInputStream(jetServerWebXML), jetServerWebXMLHandler);

	    return new WebApplicationConfig(applicationRoot, 
					    jetServerWebXMLHandler.httpRoot,
					    webXMLHandler.welcomeFiles);
	    
	} catch (ParserConfigurationException e) {
	    Log.getInstance(WebApplicationConfig.class).error("Cant parse web application config", e);
	    throw new IOException("cant parse config: " + e);
	} catch (SAXException e) {
	    Log.getInstance(WebApplicationConfig.class).error("Cant parse web application config", e);
	    throw new IOException("cant parse config: " + e);
	}
    }

    private WebApplicationConfig(File fileRoot, String httpRoot, Collection welcomeFiles) {
	this.httpRoot = httpRoot;
	this.fileRoot = fileRoot;
	this.welcomeFiles = welcomeFiles;
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

    /* --- XML handler for web.xml --- */

    private static class WebXMLHandler extends HandlerBase {
	public Collection welcomeFiles = new ArrayList();

	private StringBuffer characterBuffer = new StringBuffer();

	public void endElement(String name) throws SAXException {
	    
	    if (name.equals("welcome-file") && characterBuffer.length() > 0) {
		welcomeFiles.add(characterBuffer.toString().trim());
	    }
	    
	    /* Clear charater buffer to receive new data */
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
