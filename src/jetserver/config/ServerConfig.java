
package jetserver.config;

import java.io.*;
import java.util.*;

import javax.xml.parsers.*;
import org.xml.sax.*;

public class ServerConfig {

    private static final File CONFIG_FILE = new File("config/server-config.xml");
    private static final File CONFIG_DIR = CONFIG_FILE.getParentFile();

    private static ServerConfig instance;

    private Map properties = new HashMap();

    public static ServerConfig getInstance() {
	
	try {
	    if (instance == null) {
		InputStream in = new FileInputStream(CONFIG_FILE);
		instance = new ServerConfig(in);
		in.close();
	    }
	    
	} catch (IOException e) {
	    System.err.println("Cant load config (not found)");
	    throw new RuntimeException("Cant load config (" + CONFIG_FILE.getAbsolutePath() + "): " + e);
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
    }

    public List getStrings(String name) {
	List values = (List) properties.get(name);
	if (values == null) {
	    throw new RuntimeException("no value for property " + name);
	}
	
	return values;
    }

    public String getString(String name) {
	return (String) getStrings(name).get(0);
    }

    /* 
     * Return a file, always absolute file relative filename values are translated 
     * (Interpreted as relative to the config file)
     */ 
    public File getFile(String name) {

	File value = new File(getString(name));
	if (value.isAbsolute()) {
	    return value;
	}

	/* it's relative, translate it */
	return new File(CONFIG_DIR, getString(name));
    }

    public int getInteger(String name) {
	try {
	    return Integer.parseInt(getString(name));
	} catch (NumberFormatException e) {
	    throw new RuntimeException("value not integer: " 
				       + name + "=" + getString(name));
	}
    }
}
