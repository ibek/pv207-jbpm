package com.system.handler;

import java.util.HashMap;
import java.util.Map;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemHandler;
import org.drools.runtime.process.WorkItemManager;

import com.system.data.Document;
import com.system.ui.ReworkDialog;

public class ReworkDocument implements WorkItemHandler {

    private StatefulKnowledgeSession ksession;

    public ReworkDocument(StatefulKnowledgeSession ksession) {
        this.ksession = ksession;
    }

    @Override
    public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
        manager.abortWorkItem(workItem.getId());
    }

    @Override
    public void executeWorkItem(final WorkItem workItem,
            final WorkItemManager manager) {
        final Document doc = (Document) workItem.getParameter("document");
        try {
            System.out.println("Reworking the rejected document ...");
        } catch (Exception ex) {
        }

        ReworkDialog rd = new ReworkDialog("Rework dialog for "
                + workItem.getParameter("ActorId"));
        final long id = workItem.getId();
        rd.show(doc, new ReworkDialog.CompleteHandler() {
            @Override
            public void complete() {
                Map<String, Object> results = new HashMap<String, Object>();
                results.put("document", doc);
                ksession.getWorkItemManager().completeWorkItem(id, results);
            }
        });
    }

}
