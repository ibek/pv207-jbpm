package com.system.handler;

import java.util.HashMap;
import java.util.Map;

import org.drools.runtime.StatefulKnowledgeSession;
import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemHandler;
import org.drools.runtime.process.WorkItemManager;

import com.system.data.Document;
import com.system.ui.ReviewDialog;

public class ReviewDocument implements WorkItemHandler {

    public static enum Result {
        APPROVED, REJECTED
    }

    private StatefulKnowledgeSession ksession;

    public ReviewDocument(StatefulKnowledgeSession ksession) {
        this.ksession = ksession;
    }

    private void completeWorkItem(long id, WorkItemManager manager,
            Result result) {
        Map<String, Object> results = new HashMap<String, Object>();
        results.put("result", result.name());
        manager.completeWorkItem(id, results);
    }

    @Override
    public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
        completeWorkItem(workItem.getId(), manager, Result.REJECTED);
    }

    @Override
    public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
        Document doc = (Document) workItem.getParameter("document");
        System.out.println("Reviewing " + doc);

        ReviewDialog rd = new ReviewDialog("Review dialog for "
                + workItem.getParameter("ActorId"));
        final long id = workItem.getId();
        rd.show(doc, new ReviewDialog.CompleteHandler() {
            @Override
            public void reject() {
                completeWorkItem(id, ksession.getWorkItemManager(),
                        Result.REJECTED);
            }

            @Override
            public void approve() {
                completeWorkItem(id, ksession.getWorkItemManager(),
                        Result.APPROVED);
            }
        });

    }

}
