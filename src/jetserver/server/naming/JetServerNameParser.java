package jetserver.server.naming;

import javax.naming.*;
import java.util.Properties;

public class JetServerNameParser implements NameParser {

    private static final Properties properties = new Properties();

    static {
        properties.put("jndi.syntax.direction", "right_to_left");
        properties.put("jndi.syntax.separator", ".");
        properties.put("jndi.syntax.ignorecase", "false");
        properties.put("jndi.syntax.escape", "\\");
        properties.put("jndi.syntax.beginquote", "'");
    }

    /**
     * Parses a name into its components.
     *
     * @param name The non-null string name to parse.
     * @return A non-null parsed form of the name using the naming convention
     * of this parser.
     * @exception InvalidNameException If name does not conform to
     * 	syntax defined for the namespace.
     * @exception NamingException If a naming exception was encountered.
     */
    public Name parse(String name) throws NamingException {
        return new CompoundName(name, properties);
    }
}
