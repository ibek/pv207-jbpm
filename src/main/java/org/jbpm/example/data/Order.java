package org.jbpm.example.data;

@javax.xml.bind.annotation.XmlRootElement
public class Order implements java.io.Serializable {

    static final long serialVersionUID = 1L;

    @org.kie.api.definition.type.Position(value = 2)
    private java.lang.Integer amount;

    @org.kie.api.definition.type.Position(value = 0)
    private java.lang.Integer clientId;

    @org.kie.api.definition.type.Position(value = 1)
    private java.lang.String type;

    public Order() {
    }

    public Order(java.lang.Integer clientId, java.lang.String type, java.lang.Integer amount) {
        this.clientId = clientId;
        this.type = type;
        this.amount = amount;
    }

    public java.lang.Integer getAmount() {
        return this.amount;
    }

    public void setAmount(java.lang.Integer amount) {
        this.amount = amount;
    }

    public java.lang.Integer getClientId() {
        return this.clientId;
    }

    public void setClientId(java.lang.Integer clientId) {
        this.clientId = clientId;
    }

    public java.lang.String getType() {
        return this.type;
    }

    public void setType(java.lang.String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Order{clientId:" + clientId + ", type:" + type + ", amount:" + amount + " }";
    }

}
