
package jetserver.config;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;
import org.xml.sax.*;

public class ServerConfig {

    private static final String CONFIG_FILENAME = "config/server-config.xml";
    private static ServerConfig instance;

    private Map properties = new HashMap();

    public static ServerConfig getInstance() {
	
	try {
	    if (instance == null) {
		InputStream in = new FileInputStream(CONFIG_FILENAME);
		instance = new ServerConfig(in);
		in.close();
	    }
	    
	} catch (IOException e) {
	    System.err.println("Cant load config (not found)");
	    throw new RuntimeException("Cant load config: " + e);
	}
	
	return instance;
    }
    
    private ServerConfig(InputStream in) {

	/* Read config file */
	try {
	    
	    SAXParserFactory factory = SAXParserFactory.newInstance();
	    factory.setNamespaceAware(false);
	    factory.setValidating(false);
	    SAXParser parser = factory.newSAXParser();
	    
	    ServerConfigHandler handler = new ServerConfigHandler();
	    parser.parse(in, handler);
	    
	    this.properties = handler.getProperties();	
	} catch (ParserConfigurationException e) {
	    System.err.println("Cant parse config: " + e);
	    throw new RuntimeException("cant parse config: " + e);
	} catch (SAXException e) {
	    System.err.println("Cant parse config: " + e);
	    throw new RuntimeException("cant parse config: " + e);
	} catch (IOException e) {
	    System.err.println("Cant parse config: " + e);
	    throw new RuntimeException("cant parse config: " + e);
	}

	/* Debug */
// 	Iterator iter = properties.keySet().iterator();
// 	while(iter.hasNext()) {
// 	    String key = (String) iter.next();
// 	    System.out.println(key + "=" + properties.get(key));
// 	}
    }


    public String getProperty(String name) {
	return (String) properties.get(name);
    }

    public File getFileProperty(String name) {
	return new File(getProperty(name));
    }

    public int getIntProperty(String name) {
	try {
	    return Integer.parseInt(getProperty(name));
	} catch (NumberFormatException e) {
	    throw new RuntimeException("config value not integer: " + name + "=" + getProperty(name));
	}
    }
}
