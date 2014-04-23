package org.jbpm.example.data;

public enum ProductType {

    LAPTOP("Laptop"),
    TABLET("Tablet"),
    MOBILE("Mobile");
    
    private String label;
    
    private ProductType(String label) {
        this.label = label;
    }
    
    public String getLabel() {
        return label;
    }
    
}
