
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

	/* Debug */
 	Iterator iter = properties.keySet().iterator();
 	while(iter.hasNext()) {
 	    String key = (String) iter.next();
	    Iterator values = ((List) properties.get(key)).iterator();
	    for(int i = 0; values.hasNext(); i++) {
		String value = (String) values.next();
		System.out.println(key + "[" + i + "]=" + value);
	    }
 	}
    }


    public int getNumValues(String name) {
	List values = (List) properties.get(name);
	if (values == null) {
	    throw new RuntimeException("no value for property " + name);
	}
	return values.size();
    }

    public String getProperty(String name, int index) {
	List values = (List) properties.get(name);
	if (values == null) {
	    throw new RuntimeException("no value for property " + name);
	}

	if (index < 0 || index >= values.size()) {
	    throw new RuntimeException("index " + index + " is out of bounds for property " + name);
	}

	return (String) values.get(index);
    }

    public String getProperty(String name) {
	return getProperty(name, 0);
    }

    /* 
     * Return a file, always absolute file relative filename values are translated 
     * (Interpreted as relative to the config file)
     */ 
    public File getFileProperty(String name, int index) {

	File value = new File(getProperty(name, index));
	if (value.isAbsolute()) {
	    return value;
	}

	/* it's relative, translate it */
	return new File(CONFIG_DIR, getProperty(name, index));
    }

    public File getFileProperty(String name) {
	return getFileProperty(name, 0);
    }

    public int getIntProperty(String name, int index) {
	try {
	    return Integer.parseInt(getProperty(name, index));
	} catch (NumberFormatException e) {
	    throw new RuntimeException("config value not integer: " 
				       + name + "=" + getProperty(name)
				       + ", index=" + index);
	}
    }

    public int getIntProperty(String name) {
	return getIntProperty(name, 0);
    }
}
