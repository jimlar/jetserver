package jetserver.server.ejb.parsing;

import java.util.Collection;
import java.util.ArrayList;

/**
 * This represents the configuration of an entitybean (from ejb-jar.xml)
 */
public class EntityBeanDefinition {

    private String remoteClass;
    private String localClass;
    private String homeClass;
    private String ejbClass;

    private Collection cmpFields = new ArrayList();
    private String primaryKeyField;
    private String primaryKeyClass;

    public EntityBeanDefinition(String remoteClass,
                                String localClass,
                                String homeClass,
                                String ejbClass,
                                Collection cmpFields,
                                String primaryKeyField,
                                String primaryKeyClass) {
        this.remoteClass = remoteClass;
        this.localClass = localClass;
        this.homeClass = homeClass;
        this.ejbClass = ejbClass;
        this.cmpFields = cmpFields;
        this.primaryKeyField = primaryKeyField;
        this.primaryKeyClass = primaryKeyClass;
    }

    public String getRemoteClass() {
        return remoteClass;
    }

    public String getLocalClass() {
        return localClass;
    }

    public String getHomeClass() {
        return homeClass;
    }

    public String getEJBClass() {
        return ejbClass;
    }

    public Collection getCmpFields() {
        return cmpFields;
    }

    public String getPrimaryKeyField() {
        return primaryKeyField;
    }

    public String getPrimaryKeyClass() {
        return primaryKeyClass;
    }

    /**
     * Info on one cmp-field element
     */
    public static class CMPField {
        private String name;

        public CMPField(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
