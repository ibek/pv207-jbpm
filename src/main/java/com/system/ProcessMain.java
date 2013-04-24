package com.system;

import java.util.HashMap;
import java.util.Map;

import org.drools.KnowledgeBase;
import org.drools.builder.KnowledgeBuilder;
import org.drools.builder.KnowledgeBuilderFactory;
import org.drools.builder.ResourceType;
import org.drools.event.process.ProcessCompletedEvent;
import org.drools.event.process.ProcessEventListener;
import org.drools.event.process.ProcessNodeLeftEvent;
import org.drools.event.process.ProcessNodeTriggeredEvent;
import org.drools.event.process.ProcessStartedEvent;
import org.drools.event.process.ProcessVariableChangedEvent;
import org.drools.io.ResourceFactory;
import org.drools.runtime.StatefulKnowledgeSession;
import org.h2.tools.Server;
import org.jbpm.test.JBPMHelper;

import com.system.dao.DocumentManager;
import com.system.data.Document;
import com.system.handler.HumanTask;
import com.system.handler.ReviewDocument;
import com.system.handler.ReworkDocument;

/**
 * This is a sample file to launch a process.
 */
public class ProcessMain {

    private static Server server;

    public static final void main(String[] args) throws Exception {
        startUp();
        final Document doc = new Document();
        doc.setContent("Hello World! This is the Document.");
        DocumentManager.getInstance().addDocument(doc);

        StatefulKnowledgeSession ksession = null;

        // load up the knowledge base
        try {
            KnowledgeBase kbase = readKnowledgeBase();
            ksession = JBPMHelper.newStatefulKnowledgeSession(kbase);

            HumanTask humanTask = new HumanTask();
            humanTask.registerHandler(new ReviewDocument(ksession));
            humanTask.registerHandler(new ReworkDocument(ksession));
            ksession.getWorkItemManager().registerWorkItemHandler("Human Task",
                    humanTask);

            Map<String, Object> params = new HashMap<String, Object>();
            params.put("document", doc);

            ksession.addEventListener(new ProcessEventListener() {
                @Override
                public void beforeVariableChanged(
                        ProcessVariableChangedEvent arg0) {
                }

                @Override
                public void beforeProcessStarted(ProcessStartedEvent arg0) {
                }

                @Override
                public void beforeProcessCompleted(ProcessCompletedEvent arg0) {
                }

                @Override
                public void beforeNodeTriggered(ProcessNodeTriggeredEvent arg0) {
                }

                @Override
                public void beforeNodeLeft(ProcessNodeLeftEvent arg0) {
                }

                @Override
                public void afterVariableChanged(
                        ProcessVariableChangedEvent arg0) {
                }

                @Override
                public void afterProcessStarted(ProcessStartedEvent arg0) {
                }

                @Override
                public void afterProcessCompleted(ProcessCompletedEvent arg0) {
                    System.out
                            .println("\n========================================================================\n Process completed - "
                                    + DocumentManager.getInstance()
                                            .getDocument(doc.getId()));
                    stop();
                }

                @Override
                public void afterNodeTriggered(ProcessNodeTriggeredEvent arg0) {
                }

                @Override
                public void afterNodeLeft(ProcessNodeLeftEvent arg0) {
                }
            });

            // start a new process instance
            ksession.startProcess("com.system.bpmn.documentReview", params);

        } catch (Exception ex) {
            ex.printStackTrace();
            stop();
        }

    }

    private static KnowledgeBase readKnowledgeBase() throws Exception {
        KnowledgeBuilder kbuilder = KnowledgeBuilderFactory
                .newKnowledgeBuilder();
        kbuilder.add(
                ResourceFactory.newClassPathResource("documentReview.bpmn"),
                ResourceType.BPMN2);
        return kbuilder.newKnowledgeBase();
    }

    private static void startUp() {
        server = JBPMHelper.startH2Server();
        JBPMHelper.setupDataSource();
        // please comment this line if you already have the task service
        // running,
        // for example when running the jbpm-installer
        JBPMHelper.startTaskService();
    }

    private static void stop() {
        server.stop();
        System.exit(0);
    }

}