/*
 * Created by IntelliJ IDEA.
 * User: jimmy
 * Date: Dec 12, 2001
 * Time: 10:42:46 PM
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package jetserver.server.ejb.config;


public class SessionBeanDefinition extends BeanDefinition {

    private String remoteHomeClass;
    private String remoteClass;

    private String localHomeClass;
    private String localClass;

    private String sessionType;
    private String transactionType;

    SessionBeanDefinition() {}

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

    public String getSessionType() {
        return sessionType;
    }

    public void setSessionType(String sessionType) {
        this.sessionType = sessionType;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }
}
