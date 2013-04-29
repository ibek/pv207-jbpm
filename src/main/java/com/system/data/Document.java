package com.system.data;

import java.io.Serializable;

public class Document implements Serializable {

    public static enum Status {
        AVAILABLE, OPERATING
    }

    private static final long serialVersionUID = 6572542912930564332L;

    private int               id;
    private String            content;
    private Status            status           = Status.OPERATING;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Document(" + status.name() + ") contains \"" + content + "\"";
    }

}
