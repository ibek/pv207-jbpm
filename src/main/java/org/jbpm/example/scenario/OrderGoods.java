package org.jbpm.example.scenario;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbpm.example.EnvironmentProducer;
import org.jbpm.example.data.Order;
import org.jbpm.example.data.ProductType;
import org.jbpm.example.service.WarehouseManagementSystem;
import org.jbpm.process.workitem.bpmn2.ServiceTaskHandler;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.process.ProcessInstance;
import org.kie.api.task.TaskService;
import org.kie.api.task.model.Task;

/**
 * This scenario starts an order process of wanted goods.
 * 
 * Besides client identifier and product type, it is required to specify amount
 * of the order. In default, warehouse contains 10 pieces of each product type.
 * See assets/OrderGoodsProcess.bpmn2 for further details about the process
 * definition. If the amount of goods is not available, the process instance
 * signals SupplyWarehouseProcess.
 */
public class OrderGoods {

    private EnvironmentProducer env;

    private WarehouseManagementSystem wms = new WarehouseManagementSystem();

    public void run() {
        configure();
        /** maximum amount of products is 10 */
        System.out.println(wms.toString());

        order(new Order(1, ProductType.LAPTOP.name(), 10));

        order(new Order(1, ProductType.MOBILE.name(), 20));

        order(new Order(10, ProductType.MOBILE.name(), 5));

        env.close();
    }

    public void configure() {
        env = EnvironmentProducer.getInstance();
        KieSession ksession = env.getKieSession();

        ksession.getWorkItemManager().registerWorkItemHandler("Service Task", new ServiceTaskHandler(ksession));
    }

    public void order(Order order) {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("order", order);

        System.out.println("=== Starting process OrderGoods with " + order + " ===\n");
        try {
            ProcessInstance pi = env.getKieSession().startProcess("org.jbpm.example.OrderGoods", params);

            if (pi.getState() == ProcessInstance.STATE_ACTIVE) {
                TaskService taskService = env.getEngine().getTaskService();

                // Pack Goods

                /** need to update tasks list */
                List<Long> tasks = taskService.getTasksByProcessInstanceId(pi.getId());
                /** get current task id */
                Long taskId = tasks.get(tasks.size() - 1);

                Task currentTask = taskService.getTaskById(taskId);
                String taskName = currentTask.getNames().get(0).getText();
                System.out.println("Current task is '" + taskName + "'");

                taskService.claim(taskId, "mary");
                taskService.start(taskId, "mary");
                taskService.complete(taskId, "mary", null);

                System.out.println("Task '" + taskName + "' is completed\n");

                // Ship Goods

                /** need to update tasks list */
                tasks = taskService.getTasksByProcessInstanceId(pi.getId());
                /** get current task id */
                taskId = tasks.get(tasks.size() - 1);

                currentTask = taskService.getTaskById(taskId);
                taskName = currentTask.getNames().get(0).getText();
                System.out.println("Current task is '" + taskName + "'");

                /**
                 * it is not necessary to claim the second task since the tasks
                 * are in one swimlane
                 */
                // taskService.claim(taskId, "mary");
                taskService.start(taskId, "mary");
                taskService.complete(taskId, "mary", null);

                System.out.println("Task '" + taskName + "' is completed\n");

            } else if (pi.getState() == ProcessInstance.STATE_ABORTED) {
                System.out.println("OrderGoods process was aborted due to "
                        + ((org.jbpm.process.instance.ProcessInstance) pi).getOutcome() + "\n");
            }
        } catch (Exception ex) {
            System.out.println("OrderGoods process failed due to " + ex.getMessage());
        }
        System.out.println(wms.toString());
    }

    public static void main(String[] args) {
        new OrderGoods().run();
    }

}
