package com.system.dao;

import java.util.ArrayList;
import java.util.List;

import com.system.data.Document;

/**
 * Singleton manager to access all the documents.
 */
public class DocumentManager {

    private static DocumentManager instance;
    private List<Document>         docs = new ArrayList<Document>();

    private DocumentManager() {

    }

    public static DocumentManager getInstance() {
        if (instance == null) {
            instance = new DocumentManager();
        }
        return instance;
    }

    public Document getDocument(int id) {
        return docs.get(id);
    }

    public void addDocument(Document doc) {
        doc.setId(docs.size());
        docs.add(doc);
    }

    public void setDocument(int id, Document doc) {
        docs.set(id, doc);
    }

}
