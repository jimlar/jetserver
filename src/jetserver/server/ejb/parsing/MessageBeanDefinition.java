/*
 * Created by IntelliJ IDEA.
 * User: jimmy
 * Date: Dec 12, 2001
 * Time: 10:49:23 PM
 * To change template for new class use 
 * Code Style | Class Templates options (Tools | IDE Options).
 */
package jetserver.server.ejb.parsing;

public class MessageBeanDefinition extends BeanDefinition {

    private String transactionType;
    private String messageSelector;
    private String acknowledgeMode;
    private String destinationType;
    private String subscriptionDurability;

    MessageBeanDefinition() {}

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public String getMessageSelector() {
        return messageSelector;
    }

    public void setMessageSelector(String messageSelector) {
        this.messageSelector = messageSelector;
    }

    public String getAcknowledgeMode() {
        return acknowledgeMode;
    }

    public void setAcknowledgeMode(String acknowledgeMode) {
        this.acknowledgeMode = acknowledgeMode;
    }

    public String getDestinationType() {
        return destinationType;
    }

    public void setDestinationType(String destinationType) {
        this.destinationType = destinationType;
    }

    public String getSubscriptionDurability() {
        return subscriptionDurability;
    }

    public void setSubscriptionDurability(String subscriptionDurability) {
        this.subscriptionDurability = subscriptionDurability;
    }
}
