#PV207 - jBPM Lab

## Set up environment and tools

1. Install JBDS 7.1.1.GA from [this link](http://www.redhat.com/j/elqNow/elqRedir.htm?ref=http://www.jboss.org/download-manager/content/origin/files/sha256/93/9377ef0b9a50beebd1ccc26bfa8390a1f3b96a1a9f1c2dc415d107c05b1c50ad/jbdevstudio-product-universal-7.1.1.GA-v20140314-2145-B688.jar). To download the instalator, you need to be logged in jboss.org. It is not neccessary to install EAP within JBDS.
2. Install JBoss Business Process and Rules Development tools to JBDS (via Help->Install New Software)

  2.1 [JBoss Business Process and Rules Development](https://devstudio.jboss.com/updates/7.0-development/integration-stack/)

## Steps

1. Clone this project or directly download it through the [Direct link](https://github.com/ibek/pv207-jbpm/archive/master.zip)
2. File->Import->Maven->Existing Maven Projects
3. Click on the Next button
4. Browse the directory where is your project
5. A one checked pom.xml should appear. Click on the Finish button
6. Wait until all dependencies are downloaded
7. To test that jBPM engine works, run org.jbpm.example.scenario.HelloWorld  
  7.1 Open the java class
  
  7.2 (optional) The process definition is located in src/main/resources/assets/HelloWorldProcess.bpmn2 
  
  7.3 Click on the green button with an arrow which is called Run As and select "Java Application" in the dialog window.
  
  7.4 After you confirm the dialog window, a new tab named "Console" will appear with "Hello World!!!" message.
8. To test that persistence in jBPM engine and taskService work, run org.jbpm.example.scenario.SingleHumanTask
  
  8.1 Open the java class
  
  8.2 (optional) The process definition is located in src/main/resources/assets/SingleHumanTaskProcess.bpmn2
  
  8.3 Click on the green button with an arrow which is called Run As and select "Java Application" in the dialog window.
  
  8.4 After you confirm the dialog window, the tab named "Console" will show a message containing "The task has been completed.".
9. Implement order method to call and go through the OrderGoods process
  
  9.1 Start the process definition named "org.jbpm.example.OrderGoods" via ksession.startProcess method and pass "order" parameter

        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("order", order);
		ProcessInstance processInstance = ksession.startProcess(PROCESS_DEFINITION_ID, parameters);

  9.2 Run the scenatio and see that the process did not ended ("Process is Active"). That is because there are human tasks which need to be completed first.
  
  9.3 For an active process claim, start and complete tasks "Pack goods" and "Ship goods" with user "mary". To accomplish this step use these commands (taskService.getTasksByProcessInstanceId, taskService.getTaskById, taskService.claim, taskService.start, taskService.complete)

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

  9.4 When you run the scenario, notice that the status of stotage has not changed. Lets look at org.jbpm.example.service.WarehouseManagementSystem to add the functionality.
  
  9.5 It is simple java code where an amount of the required product should be substracted from the storage.

        ProductType type = ProductType.valueOf(order.getType());
        Integer count = storage.get(type) - order.getAmount();
        storage.put(type, count);

  9.6 Run the scenario and see the updated storage status where LAPTOPS run out.
  
  9.7 Comment the first order method "order(new Order(1, ProductType.LAPTOP.name(), 10));" and use the second instead.
  
  9.8 Start the scenario and see that the process is aborted but SupplyWarehouse process is not triggered. To do that, we need to fix SupplyWarehouseProcess.bpmn2 located in src/main/resources/assets.
  
  * 9.8.1 Open the project definition in the Bpmn2 Diagram Editor.
        9.8.2 Click on the surface of the process definition and select Properties tab.
        9.8.3 In the Data Items tab, add a new variable. (Click on Add button next to the variable list)
  * 9.8.4 (workaround to BZ 1090375) Call the variable "report" and choose String as its type.
  * 9.8.5 Click on start event and add a new event definition. (Click on Add button in the Event tab)
  * 9.8.6 In the dialog window select Signal Event Definition and confirm the selection.
  * 9.8.7 In the opened panel called "Signal Event Definition Details" select from Signal list the "NotAvailableGoods".
  
  * 9.8.8 Map incoming signal data to the created variable "SupplyWarehouse/report".
  
  * 9.8.9 Click on script task and update the script to print also the report variable.

        System.out.println("Received a request to supply warehouse " + report);

  9.9 Run the scenario and see the new output from the edited SupplyWarehouse process "Received a request to supply warehouse Signal_1_Output".
  
  9.10 Comment the second order method "order(new Order(1, ProductType.MOBILE.name(), 20));" and use the last one instead.
  
  9.11 Run the scenario and see another output "Error while picking up goods". That is because client with id 10 is on the blackList of org.jbpm.example.service.WarehouseManagementSystem.

10 Congratulations, you have finished the lab
  







