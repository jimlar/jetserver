package jetserver.server.ejb.config;

/**
 * Represent a cmp field for a CMP entity bean
 */
public class CMPField {

    private String name;

    CMPField(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String toString() {
        return name;
    }
}
