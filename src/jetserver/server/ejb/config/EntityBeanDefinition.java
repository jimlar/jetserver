package jetserver.server.ejb.config;

import java.util.Collection;
import java.util.ArrayList;

/**
 * This represents the configuration of an entitybean (from ejb-jar.xml)
 *
 */
public class EntityBeanDefinition extends BeanDefinition {

    private String remoteHomeClass;
    private String remoteClass;

    private String localHomeClass;
    private String localClass;

    private String persistenceType;
    private String primKeyClass;
    private boolean isReentrant;

    private String cmpVersion;
    private String abstractSchemaName;

    private Collection cmpFields = new ArrayList();
    private String primKeyField;

    private Collection queries = new ArrayList();

    EntityBeanDefinition() {}

    public String getRemoteHomeClass() {
        return remoteHomeClass;
    }

    public void setRemoteHomeClass(String remoteHomeClass) {
        this.remoteHomeClass = remoteHomeClass;
    }

    public String getRemoteClass() {
        return remoteClass;
    }

    public void setRemoteClass(String remoteClass) {
        this.remoteClass = remoteClass;
    }

    public String getLocalHomeClass() {
        return localHomeClass;
    }

    public void setLocalHomeClass(String localHomeClass) {
        this.localHomeClass = localHomeClass;
    }

    public String getLocalClass() {
        return localClass;
    }

    public void setLocalClass(String localClass) {
        this.localClass = localClass;
    }

    public String getPersistenceType() {
        return persistenceType;
    }

    public void setPersistenceType(String persistenceType) {
        this.persistenceType = persistenceType;
    }

    public String getPrimKeyClass() {
        return primKeyClass;
    }

    public void setPrimKeyClass(String primKeyClass) {
        this.primKeyClass = primKeyClass;
    }

    public boolean isReentrant() {
        return isReentrant;
    }

    public void setReentrant(boolean reentrant) {
        isReentrant = reentrant;
    }

    public String getCmpVersion() {
        return cmpVersion;
    }

    public void setCmpVersion(String cmpVersion) {
        this.cmpVersion = cmpVersion;
    }

    public String getAbstractSchemaName() {
        return abstractSchemaName;
    }

    public void setAbstractSchemaName(String abstractSchemaName) {
        this.abstractSchemaName = abstractSchemaName;
    }

    public Collection getCmpFields() {
        return cmpFields;
    }

    public void setCmpFields(Collection cmpFields) {
        this.cmpFields = cmpFields;
    }

    public String getPrimKeyField() {
        return primKeyField;
    }

    public void setPrimKeyField(String primKeyField) {
        this.primKeyField = primKeyField;
    }

    public Collection getQueries() {
        return queries;
    }

    public void setQueries(Collection queries) {
        this.queries = queries;
    }
}

