package com.system.handler;

import java.util.ArrayList;
import java.util.List;

import org.drools.runtime.process.WorkItem;
import org.drools.runtime.process.WorkItemHandler;
import org.drools.runtime.process.WorkItemManager;

public class HumanTask implements WorkItemHandler {

    private List<WorkItemHandler> handlers = new ArrayList<WorkItemHandler>();

    @Override
    public void abortWorkItem(WorkItem workItem, WorkItemManager manager) {
        for (WorkItemHandler handler : handlers) {
            if (workItem.getParameter("TaskName").equals(
                    handler.getClass().getSimpleName())) {
                handler.abortWorkItem(workItem, manager);
                break;
            }
        }
    }

    @Override
    public void executeWorkItem(WorkItem workItem, WorkItemManager manager) {
        for (WorkItemHandler handler : handlers) {
            if (workItem.getParameter("TaskName").equals(
                    handler.getClass().getSimpleName())) {
                handler.executeWorkItem(workItem, manager);
                break;
            }
        }
    }

    public void registerHandler(WorkItemHandler handler) {
        handlers.add(handler);
    }

}
