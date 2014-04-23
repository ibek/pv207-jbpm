package org.jbpm.example.service.exception;

public class ForbiddenClientException extends Exception {

    private static final long serialVersionUID = 1L;

    public ForbiddenClientException(String message) {
        super(message);
    }
    
}
