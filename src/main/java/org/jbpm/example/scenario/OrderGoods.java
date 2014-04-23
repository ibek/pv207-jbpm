package org.jbpm.example.scenario;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jbpm.example.EnvironmentProducer;
import org.jbpm.example.data.Order;
import org.jbpm.example.data.ProductType;
import org.jbpm.example.service.WarehouseManagementSystem;
import org.jbpm.example.util.JbpmHelper;
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
 * 
 * See assets/OrderGoodsProcess.bpmn2 for further details about the process
 * definition.
 * 
 * If the amount of goods is not available, the process instance signals
 * SupplyWarehouseProcess.
 * 
 * Clients with identifier 10-19 are on the black list and so an exception is
 * risen on the Pick up service task.
 */
public class OrderGoods {

	private static final String PROCESS_DEFINITION_ID = "org.jbpm.example.OrderGoods";

	private EnvironmentProducer env;

	private WarehouseManagementSystem wms = new WarehouseManagementSystem();

	public void run() {
		configure();
		/** maximum amount of products is 10 */

		 order(new Order(1, ProductType.LAPTOP.name(), 10));

		/** signals SupplyWarehouseProcess and OrderGoods process is aborted */
		// order(new Order(1, ProductType.MOBILE.name(), 20));

		/** client with id 10 is on the black list */
		//order(new Order(10, ProductType.MOBILE.name(), 5));

		env.close();
	}

	public void configure() {
		env = EnvironmentProducer.getInstance();
		KieSession ksession = env.getKieSession();

		ksession.getWorkItemManager().registerWorkItemHandler("Service Task",
				new ServiceTaskHandler(ksession));
	}

	public void order(Order order) {
		System.out.println("=== Starting process OrderGoods with " + order
				+ " ===\n");
		KieSession ksession = env.getKieSession();
		TaskService taskService = env.getEngine().getTaskService();

		/**
		 * TODO: start process PROCESS_DEFINITION_ID via ksession.startProcess
		 * method and pass "order" parameter
		 */
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("order", order);
		ProcessInstance processInstance = ksession.startProcess(
				PROCESS_DEFINITION_ID, parameters);

		/**
		 * TODO: for an active process claim, start and complete tasks
		 * "Pack goods" and "Ship goods" with user "mary". You can find these
		 * methods useful:
		 * 
		 * taskService.getTasksByProcessInstanceId, taskService.getTaskById,
		 * taskService.claim, taskService.start, taskService.complete
		 */
		if (processInstance.getState() == ProcessInstance.STATE_ACTIVE) {

			// Pack Goods

			/** need to update tasks list */
			List<Long> tasks = taskService
					.getTasksByProcessInstanceId(processInstance.getId());
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
			tasks = taskService.getTasksByProcessInstanceId(processInstance
					.getId());
			/** get current task id */
			taskId = tasks.get(tasks.size() - 1);

			currentTask = taskService.getTaskById(taskId);
			taskName = currentTask.getNames().get(0).getText();
			System.out.println("Current task is '" + taskName + "'");

			/**
			 * it is not necessary to claim the second task since the tasks are
			 * in one swimlane
			 */
			// taskService.claim(taskId, "mary");
			taskService.start(taskId, "mary");
			taskService.complete(taskId, "mary", null);

			System.out.println("Task '" + taskName + "' is completed\n");
		}

		System.out.println("Process is "
				+ JbpmHelper.getProcessStatus(PROCESS_DEFINITION_ID,
						env.getAuditService()) + "\n");
		System.out.println(wms.toString());
	}

	public static void main(String[] args) {
		new OrderGoods().run();
	}

}
